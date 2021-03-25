package Compiler.Model;

import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.Elements.ThreadElement;
import Compiler.Service.Store;
import Compiler.Service.Timer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

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

    public SpaceModel(JSONObject data) {
        super(data);
        JSONArray errorsJson = (JSONArray) data.get("errors");
        errorsJson.forEach(errorJson -> {
            this.errors.add(new ValidationError(this, (String) errorJson));
        });
    }

    public void addElement(AbstractElement element) {
        element.setSpaceModel(this);
        this.elements.add(element);
        this.support.firePropertyChange(EVENT_ELEMENT_ADDED, null, element);// add to tabs
    }

    public ArrayList<AbstractElement> getElements() {
        return this.elements;
    }

    public boolean createConnection(ConnectionPointModel inConnection, ConnectionPointModel outConnection) {
        if (inConnection.isAllowedToConnectTo(outConnection)) {
            if (inConnection.getElementModel().getClass() == ThreadElement.class) {
                inConnection = new ConnectionPointModel(inConnection.getType(), inConnection.getElementModel());
            }

            if (outConnection.getElementModel().getClass() == ThreadElement.class) {
                outConnection = new ConnectionPointModel(outConnection.getType(), outConnection.getElementModel());
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
        if (futureConnection != null) {
            return;
        }

        futureConnection = newConnectionPointModel;
        this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
    }


    public void finishConnection(ConnectionPointModel newConnectionPointModel) throws Exception {
        if (futureConnection == null) {
            return;
        }

        if (newConnectionPointModel.getElementModel().equals(futureConnection.getElementModel())) {
            futureConnection = null;
            this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
            return;
        }

        if (newConnectionPointModel.isAllowedToConnectTo(futureConnection)) {
            futureConnection.setConnectsTo(newConnectionPointModel);
            this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
            Timer.setTimeout(() -> {
                switch (newConnectionPointModel.getType()) {
                    case IN -> this.createConnection(newConnectionPointModel, futureConnection);
                    case OUT -> this.createConnection(futureConnection, newConnectionPointModel);
                }
                futureConnection = null;
            }, 100);
        } else {
            throw new Exception("The elements can connect");
        }
    }


    public ConnectionPointModel getFutureConnection() {
        return futureConnection;
    }

    public ArrayList<ValidationError> validate() {
        this.errors.clear();
        ArrayList<ValidationError> childErrors = new ArrayList<>();

        for (AbstractElement element : this.elements) {
            childErrors.addAll(element.validate());
        }

        if (childErrors.size() > 0) {
            this.addError(new ValidationError(this, "Elements have errors"));
        }

        return childErrors;
    }

    public void addError(ValidationError error) {
        this.errors.add(error);
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

    public void importRelationships(JSONObject json) {
        Store store = Store.getInstance();
        String futureConnectionId = (String) json.get("futureConnectionId");
        if (futureConnectionId != null && !futureConnectionId.isEmpty()) {
            this.futureConnection = store.getConnectionPointById(futureConnectionId);
        }

        JSONArray elementsJson = (JSONArray) json.get("elements");
        elementsJson.forEach(elementId -> {
            this.elements.add(store.getElementById((String) elementId));
        });
    }
}
