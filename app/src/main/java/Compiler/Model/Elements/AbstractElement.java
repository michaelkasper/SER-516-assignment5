package Compiler.Model.Elements;

import Compiler.Model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public abstract class AbstractElement extends AbstractModel {

    public static final String EVENT_CONNECTION_MADE = "event_connection_made";
    public static final String EVENT_POSITION_UPDATED = "event_position_updated";
    public static final String EVENT_CREATING_CONNECTION = "event_creating_connection";


    /**
     * TODO: Add ConnectionsIn ArrayList to other AbstractElements
     * TODO: Add ConnectionsOut ArrayList to other AbstractElements
     * TODO: Broadcast change to views when either list is updated
     * TODO: Broadcast change when values is updated
     * <p>
     * TODO: Link to view???
     * TODO: Update view???
     * TODO: Update Space View???
     * TODO: Draw Connections from the Space View???
     * TODO: ????
     */

    private SpaceModel spaceModel;
    public final String symbol;
    public final int inputs;
    public final int outputs;
    private Point position = new Point(-1, -1);
    private Stack<String> callStack = new Stack<>();
    private final ConnectionPointModel[] inConnectionPoints;
    private final ConnectionPointModel[] outConnectionPoints;
    private final ArrayList<ConnectionModel> inConnections = new ArrayList<ConnectionModel>();
    private final ArrayList<ConnectionModel> outConnections = new ArrayList<ConnectionModel>();
    private final ArrayList<ValidationError> errors = new ArrayList<>();

    public AbstractElement(String symbol, int inputs, int outputs) {
        super();
        this.symbol = symbol;
        this.inputs = inputs;
        this.outputs = outputs;


        inConnectionPoints = new ConnectionPointModel[Math.abs(inputs)];
        for (int i = 0; i < inConnectionPoints.length; i++) {
            inConnectionPoints[i] = new ConnectionPointModel(ConnectionPointModel.Type.IN, this);
        }

        outConnectionPoints = new ConnectionPointModel[Math.abs(outputs)];
        for (int i = 0; i < outConnectionPoints.length; i++) {
            outConnectionPoints[i] = new ConnectionPointModel(ConnectionPointModel.Type.OUT, this);
        }
    }

    public void addConnection(ConnectionModel connection) {
        if (connection.inPoint.getElementModel().equals(this)) {
            this.inConnections.add(connection);
        } else {
            this.outConnections.add(connection);
        }

        this.updateCallStack();

        this.support.firePropertyChange(EVENT_CONNECTION_MADE, null, connection);
    }

    public void setPosition(Point dropPoint) {
        this.position = dropPoint;
        this.support.firePropertyChange(AbstractElement.EVENT_POSITION_UPDATED, null, this);
    }

    public Point getPosition() {
        return this.position;
    }

    public void setSpaceModel(SpaceModel spaceModel) {
        this.spaceModel = spaceModel;
    }

    public SpaceModel getSpaceModel() {
        return spaceModel;
    }

    public void setCallStack(Stack<String> callStack) {
        this.callStack = callStack;
    }

    @SuppressWarnings("unchecked")
    public void updateCallStack() {
        if (this.getInConnections().size() > 0) {
            AbstractElement fromElement = this.getInConnections().get(0).outPoint.getElementModel();

            Stack<String> newCallStack = (Stack<String>) fromElement.callStack.clone();
            if (this.inputs > 1) {
                newCallStack.pop();
            }
            this.setCallStack(newCallStack);
        }

        for (ConnectionModel connection : this.getOutConnections()) {
            AbstractElement toElement = connection.inPoint.getElementModel();

            Stack<String> newCallStack = (Stack<String>) this.callStack.clone();
            if (this.outputs > 1) {
                newCallStack.add(this.id);
            }
            toElement.setCallStack(newCallStack);
            toElement.updateCallStack();
        }
    }


    public Stack<String> getCallStack() {
        return callStack;
    }

    public ArrayList<ValidationError> validate() {
        this.errors.clear();
        ArrayList<ValidationError> errors = new ArrayList<>();
        ArrayList<ValidationError> childErrors = new ArrayList<>();

        if (this.getInConnections().size() != this.inputs) {
            errors.add(new ValidationError(this, "Missing In Connections"));
        }

        if (this.getOutConnections().size() != this.outputs) {
            errors.add(new ValidationError(this, "Missing Out Connections"));
        }

        boolean foundSyncError = false;
        String connectionStackItem = null;
        for (ConnectionModel connection : this.getOutConnections()) {
            if (connection.inPoint.getElementModel().callStack.size() > 0) {
                //in
                if (connectionStackItem == null) {
                    connectionStackItem = connection.inPoint.getElementModel().callStack.peek();
                } else if (!connection.inPoint.getElementModel().callStack.peek().equals(connectionStackItem)) {
                    foundSyncError = true;
                }
            }
        }
        if (foundSyncError) {
            errors.add(new ValidationError(this, "Open and Close do not sync"));
        }
        this.addErrors(errors);

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

    public static AbstractElement Factory(String className) {
        switch (className) {
            case "OpenIfElement":
                return new OpenIfElement();
            case "CloseIfElement":
                return new CloseIfElement();
            case "CommandElement":
                return new CommandElement();
            case "LoopElement":
                return new LoopElement();
            case "MethodEndElement":
                return new MethodEndElement();
            case "MethodStartElement":
                return new MethodStartElement();
            case "ThreadElement":
                return new ThreadElement();
        }
        return null;
    }


    public ConnectionPointModel[] getInConnectionPoints() {
        return inConnectionPoints;
    }

    public ConnectionPointModel[] getOutConnectionPoints() {
        return outConnectionPoints;
    }

    public ArrayList<ConnectionPointModel> getAllConnectionPoints() {
        ArrayList<ConnectionPointModel> points = new ArrayList<>();
        Collections.addAll(points, this.getInConnectionPoints());
        Collections.addAll(points, this.getOutConnectionPoints());
        return points;
    }

    public ArrayList<ConnectionModel> getInConnections() {
        return this.inConnections;
    }

    public ArrayList<ConnectionModel> getOutConnections() {
        return this.outConnections;
    }
}
