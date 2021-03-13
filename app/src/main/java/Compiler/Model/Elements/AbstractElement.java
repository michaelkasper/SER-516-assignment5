package Compiler.Model.Elements;

import Compiler.Controller.ConnectionPointController;
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
    private ConnectionPointModel[] inConnectionPoints;
    private ConnectionPointModel[] outConnectionPoints;
    private ArrayList<ConnectionModel> connections = new ArrayList<ConnectionModel>();

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

    @SuppressWarnings("unchecked")
    public void addConnection(ConnectionModel connection) {

        AbstractElement inElement = connection.inPoint.getElementModel();
        AbstractElement outElement = connection.outPoint.getElementModel();

        if (inElement.getId().equals(this.getId())) {
            Stack<String> newCallStack = (Stack<String>) outElement.callStack.clone();
            if (this.inputs > 1) {
                newCallStack.pop();
            }
            this.setCallStack(newCallStack);
        }

        if (outElement.getId().equals(this.getId())) {
            Stack<String> newCallStack = (Stack<String>) this.callStack.clone();
            if (this.outputs > 1) {
                newCallStack.add(this.id);
            }
            outElement.setCallStack(newCallStack);
        }

        this.connections.add(connection);
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

    public ArrayList<ValidationError> validate() {
        ArrayList<ValidationError> errors = new ArrayList<>();

        //TODO: logic for verification
//        if (this.connectionsIn.size() != this.inputs) {
//            errors.add(new ValidationError(this, "Missing Input Connections"));
//        }
//
//        if (this.connectionsOut.size() != this.outputs) {
//            errors.add(new ValidationError(this, "Missing Output Connections"));
//        }
//
//        boolean foundSyncError = false;
//        for (AbstractElement element : this.connectionsIn) {
//            if (!element.callStack.peek().equals(this.connectionsIn.get(0).callStack.peek())) {
//                foundSyncError = true;
//            }
//        }
//        if (foundSyncError) {
//            errors.add(new ValidationError(this, "Open and Close do not sync"));
//        }
//
//
//        for (AbstractElement element : this.connectionsOut) {
//            errors.addAll(element.validate());
//        }

        return errors;
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

    public ArrayList<ConnectionModel> getConnections() {
        return this.connections;
    }
}
