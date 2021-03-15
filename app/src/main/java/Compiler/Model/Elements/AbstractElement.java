package Compiler.Model.Elements;

import Compiler.Model.AbstractModel;
import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Connections.LoopConnectionPointModel;
import Compiler.Model.SpaceModel;
import Compiler.Model.ValidationError;

import java.awt.*;
import java.util.ArrayList;

public abstract class AbstractElement extends AbstractModel {

    public static final String EVENT_POSITION_UPDATED = "event_position_updated";


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

    public final String symbol;
    public final int inputs;
    public final int outputs;
    protected SpaceModel spaceModel;
    protected ArrayList<ConnectionPointModel> inConnectionPoints;
    protected ArrayList<ConnectionPointModel> outConnectionPoints;

    protected final ArrayList<ValidationError> errors = new ArrayList<>();
    protected Point position = new Point(-1, -1);

    public AbstractElement(String symbol, int inputs, int outputs) {
        super();
        this.symbol = symbol;
        this.inputs = inputs;
        this.outputs = outputs;

        this.createConnectionPoints();
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

    public ArrayList<ValidationError> validate() {
        this.errors.clear();
        this.errors.addAll(this.validateConnections());
        return this.errors;
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

    public ArrayList<ConnectionPointModel> getInConnectionPoints() {
        return inConnectionPoints;
    }

    public ArrayList<ConnectionPointModel> getOutConnectionPoints() {
        return outConnectionPoints;
    }

    public ArrayList<ConnectionPointModel> getAllConnectionPoints() {
        ArrayList<ConnectionPointModel> points = new ArrayList<>();
        points.addAll(this.getInConnectionPoints());
        points.addAll(this.getOutConnectionPoints());

        return points;
    }


    protected ArrayList<ValidationError> validateConnections() {
        ArrayList<ValidationError> errors = new ArrayList<>();

        for (ConnectionPointModel connectionPoint : this.inConnectionPoints) {
            if (connectionPoint.getConnectsTo() == null) {
                errors.add(new ValidationError(this, "Missing In Connections"));
                break;
            }
        }

        for (ConnectionPointModel connectionPoint : this.outConnectionPoints) {
            if (connectionPoint.getConnectsTo() == null) {
                errors.add(new ValidationError(this, "Missing Out Connections"));
                break;
            }
        }
        return errors;
    }

    protected void createConnectionPoints() {
        inConnectionPoints = new ArrayList<>();
        for (int i = 0; i < Math.abs(inputs); i++) {
            inConnectionPoints.add(new ConnectionPointModel(ConnectionPointModel.Type.IN, this));
        }

        outConnectionPoints = new ArrayList<>();
        for (int i = 0; i < Math.abs(outputs); i++) {
            outConnectionPoints.add(new ConnectionPointModel(ConnectionPointModel.Type.OUT, this));
        }
    }


    public boolean verifyNoLoop(ConnectionPointModel toPoint) {
        for (ConnectionPointModel connectionPoint : this.getInConnectionPoints()) {
            if (connectionPoint.getConnectsTo() != null) {
                if (connectionPoint.getConnectsTo().getElementModel().equals(toPoint.getElementModel())) {
                    if (connectionPoint.getConnectsTo().getClass() == LoopConnectionPointModel.class && toPoint.getClass() == LoopConnectionPointModel.class) {
                        return true;
                    }
                    return false;
                } else {
                    return connectionPoint.getConnectsTo().getElementModel().verifyNoLoop(toPoint);
                }
            }
        }
        return true;
    }

    public void addConnectionPoint(ConnectionPointModel connectionPointModel) {
        switch (connectionPointModel.getType()) {
            case IN -> this.inConnectionPoints.add(connectionPointModel);
            case OUT -> this.outConnectionPoints.add(connectionPointModel);
        }
    }
}
