package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.Timer;

import java.util.ArrayList;
import java.util.function.Consumer;

public class SpaceModel extends AbstractModel {

    public final static String EVENT_ELEMENT_ADDED = "event_element_added";
    public final static String EVENT_CONNECTION_CREATED = "event_connection_created";
    public final static String EVENT_CONNECTION_STARTED = "event_connection_started";
    public final static String EVENT_UPDATE_ERRORS = "event_update_errors";


    private ArrayList<AbstractElement> elements = new ArrayList<>();
    private ConnectionModel futureConnection = new ConnectionModel();
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
        return this.createConnection(new ConnectionModel(inConnection, this.getElementConnectionPointById(outConnectionId)));
    }

    public boolean createConnection(String inConnectionId, ConnectionPointModel outConnection) {
        return this.createConnection(new ConnectionModel(this.getElementConnectionPointById(inConnectionId), outConnection));
    }

    public boolean createConnection(ConnectionModel newConnection) {
        if (newConnection.isValid()) {
            newConnection.accept();

            futureConnection = new ConnectionModel();
            this.support.firePropertyChange(EVENT_CONNECTION_CREATED, null, true);
            return true;
        }
        return false;
    }

    public void startConnection(ConnectionPointModel newConnectionPointModel) {
        ConnectionPointModel currentPoint = null;
        Consumer<ConnectionPointModel> setPoint = point -> {
        };
        switch (newConnectionPointModel.getType()) {
            case IN -> {
                currentPoint = futureConnection.getInPoint();
                setPoint = point -> futureConnection.setInPoint(point);
            }
            case OUT -> {
                currentPoint = futureConnection.getOutPoint();
                setPoint = point -> futureConnection.setOutPoint(point);
            }
        }


        if (currentPoint != null && currentPoint.equals(newConnectionPointModel)) {
            setPoint.accept(null);
            this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
            return;
        }

        setPoint.accept(newConnectionPointModel);
        if (!futureConnection.isValid()) {
            setPoint.accept(null);
        }

        this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);

        if (futureConnection.isComplete()) {
            Timer.setTimeout(() -> {
                this.createConnection(futureConnection);
            }, 150);
        }
    }

    public boolean isFutureConnection(ConnectionPointModel pointModel) {
        ConnectionPointModel inPoint = futureConnection.getInPoint();
        ConnectionPointModel outPoint = futureConnection.getOutPoint();
        return (inPoint != null && inPoint.equals(pointModel)) || (outPoint != null && outPoint.getId().equals(pointModel.getId()));
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
}
