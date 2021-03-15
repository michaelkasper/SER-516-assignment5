package Compiler.Model;

import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.Elements.ThreadElement;
import Compiler.Service.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SpaceModel extends AbstractModel {

    public final static String EVENT_ELEMENT_ADDED = "event_element_added";
    public final static String EVENT_CONNECTION_CREATED = "event_connection_created";
    public final static String EVENT_CONNECTION_STARTED = "event_connection_started";
    public final static String EVENT_UPDATE_ERRORS = "event_update_errors";


    private ArrayList<AbstractElement> elements = new ArrayList<>();
    private ConnectionPointModel futureConnection = null;
    private ArrayList<ValidationError> errors = new ArrayList<>();

    public SpaceModel() {
        super();
    }

    public void addElement(AbstractElement element) {
        element.setSpaceModel(this);
        this.elements.add(element);
        this.support.firePropertyChange(EVENT_ELEMENT_ADDED, null, element);// add to tabs
    }

    public boolean hasElementId(String id) {
        return this.getElementById(id) != null;
    }


    public AbstractElement getElementById(String id) {
        return this.elements.stream()
                .filter(element -> id.equals(element.getId()))
                .findFirst()
                .orElse(null);
    }

    public ConnectionPointModel getElementConnectionPointById(String id) {
        for (AbstractElement element : this.elements) {
            for (ConnectionPointModel point : element.getAllConnectionPoints()) {
                if (point.getId().equals(id)) {
                    return point;
                }
            }
        }
        return null;
    }

    public ArrayList<AbstractElement> getElements() {
        return this.elements;
    }

    public boolean createConnection(ConnectionPointModel inConnection, String outConnectionId) {
        return this.createConnection(inConnection, this.getElementConnectionPointById(outConnectionId));
    }

    public boolean createConnection(String inConnectionId, ConnectionPointModel outConnection) {
        return this.createConnection(this.getElementConnectionPointById(inConnectionId), outConnection);
    }

    public boolean createConnection(ConnectionPointModel inConnection, ConnectionPointModel outConnection) {
        if (inConnection.isAllowedToConnectTo(outConnection)) {
            if (inConnection.getElementModel().getClass() == ThreadElement.class) {
                inConnection = new ConnectionPointModel(inConnection.getType(), inConnection.getElementModel(), inConnection.getConnectionPointView());
            }

            if (outConnection.getElementModel().getClass() == ThreadElement.class) {
                outConnection = new ConnectionPointModel(outConnection.getType(), outConnection.getElementModel(), outConnection.getConnectionPointView());
            }

            inConnection.setConnectsTo(outConnection);
            outConnection.setConnectsTo(inConnection);

            futureConnection = null;
            this.support.firePropertyChange(EVENT_CONNECTION_CREATED, null, true);
            return true;
        }
        return false;
    }

    public void startConnection(ConnectionPointModel newConnectionPointModel) {
        if (futureConnection == null) {
            futureConnection = newConnectionPointModel;
            this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
            return;
        }


        if (newConnectionPointModel.equals(futureConnection)) {
            futureConnection = null;
            this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
            return;
        }


        if (newConnectionPointModel.isAllowedToConnectTo(futureConnection)) {
            futureConnection.setConnectsTo(newConnectionPointModel);
            Timer.setTimeout(() -> {
                switch (newConnectionPointModel.getType()) {
                    case IN -> this.createConnection(newConnectionPointModel, futureConnection);
                    case OUT -> this.createConnection(futureConnection, newConnectionPointModel);
                }
                futureConnection = null;
            }, 150);
        }
    }

    public ConnectionPointModel getFutureConnection() {
        return futureConnection;
    }

    public ArrayList<ValidationError> validate() {
        this.errors.clear();
        ArrayList<ValidationError> errors = new ArrayList<>();
        ArrayList<ValidationError> childErrors = new ArrayList<>();

        int startCount = 0;
        for (AbstractElement element : this.elements) {
            if (element.inputs == 0) {
                startCount++;
            }
            childErrors.addAll(element.validate());
        }
        if (startCount > 1) {
            errors.add(new ValidationError(this, "Multiple Start Commands"));
        }
        if (startCount == 0) {
            errors.add(new ValidationError(this, "No Start Command"));
        }

        this.addErrors(errors);
        if (childErrors.size() > 0) {
            this.addError(new ValidationError(this, "Elements have errors"));
        }

        errors.addAll(childErrors);
        return errors;
    }


    public void addError(ValidationError error) {
        this.errors.add(error);
    }

    public void addErrors(ArrayList<ValidationError> errors) {
        for (ValidationError error : errors) {
            this.addError(error);
        }
    }

    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

    public String getErrorsAsString() {
        String errorMsg = "Errors:";
        for (ValidationError error : this.errors) {
            errorMsg += " " + error.getErrMessage() + ";";
        }

        return errorMsg;
    }


    public JSONObject export() {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("futureConnectionId", futureConnection != null ? futureConnection.getId() : null);

        JSONArray elements = new JSONArray();
        for (AbstractElement element : this.elements) {
            elements.add(element.getId());
        }
        obj.put("elements", elements);

        JSONArray errors = new JSONArray();
        for (ValidationError error : this.errors) {
            errors.add(error.getErrMessage());
        }
        obj.put("errors", errors);

        return obj;
    }

    public void importJson(JSONObject json) {
        this.id = (String) json.get("id");

        JSONArray errorsJson = (JSONArray) json.get("errors");
        errorsJson.forEach(errorJson -> {
            this.errors.add(new ValidationError(this, (String) errorJson));
        });
    }

    public void importRelationships(JSONObject json, HashMap<String, AbstractElement> elementsMap, HashMap<String, ConnectionPointModel> pointsMap) {
        String futureConnectionId = (String) json.get("futureConnectionId");
        if (futureConnectionId != null && !futureConnectionId.isEmpty()) {
            this.futureConnection = pointsMap.get(futureConnectionId);
        }

        JSONArray elementsJson = (JSONArray) json.get("elements");
        elementsJson.forEach(elementId -> {
            this.elements.add(elementsMap.get(elementId));
        });
    }
}
