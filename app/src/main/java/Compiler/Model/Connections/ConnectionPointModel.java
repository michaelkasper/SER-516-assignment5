package Compiler.Model.Connections;

import Compiler.Model.AbstractModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.Elements.ThreadElement;
import Compiler.View.Components.ConnectionPoint.AbstractConnectionPoint;
import Compiler.View.Components.ConnectionPoint.ConnectionPointIn;
import Compiler.View.Components.ConnectionPoint.ConnectionPointOut;

public class ConnectionPointModel extends AbstractModel {

    private final Type type;
    private final AbstractElement abstractElement;
    private boolean isDragging = false;
    private AbstractConnectionPoint connectionPointView;
    private ConnectionPointModel connectsToPoint;
    private boolean isHidden = false;

    public enum Type {IN, OUT}

    public ConnectionPointModel(Type type, AbstractElement abstractElement) {
        this.type = type;
        this.abstractElement = abstractElement;
    }

    public ConnectionPointModel(Type type, AbstractElement elementModel, AbstractConnectionPoint connectionPointView) {
        this(type, elementModel);
        this.connectionPointView = connectionPointView;
        this.isHidden = true;
        elementModel.addConnectionPoint(this);
    }

    public boolean isHidden() {
        return isHidden;
    }

    public boolean isInSpace() {
        return this.getElementModel().getSpaceModel() != null;
    }

    public AbstractElement getElementModel() {
        return abstractElement;
    }

    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
    }

    public void setView(AbstractConnectionPoint connectionPointView) {
        this.connectionPointView = connectionPointView;

        if (this.getElementModel().getClass() == ThreadElement.class) {
            switch (type) {
                case IN -> {
                    for (ConnectionPointModel connectionPoint : this.getElementModel().getInConnectionPoints()) {
                        connectionPoint.connectionPointView = connectionPointView;
                    }
                }
                case OUT -> {
                    for (ConnectionPointModel connectionPoint : this.getElementModel().getOutConnectionPoints()) {
                        connectionPoint.connectionPointView = connectionPointView;
                    }
                }
            }
        }

    }

    public ConnectionPointModel.Type getType() {
        return type;
    }

    public AbstractConnectionPoint getConnectionPointView() {
        return connectionPointView;
    }

    public void setConnectsTo(ConnectionPointModel connectsToPoint) {
        this.connectsToPoint = connectsToPoint;
    }

    public ConnectionPointModel getConnectsTo() {
        return connectsToPoint;
    }

    public boolean isAllowedToConnectTo(ConnectionPointModel otherPoint) {
        ConnectionPointModel outPoint = this.type == Type.OUT ? this : null;
        ConnectionPointModel inPoint = otherPoint.getType() == Type.IN ? otherPoint : null;

        if (outPoint == null && inPoint == null) {
            outPoint = otherPoint.type == Type.OUT ? otherPoint : null;
            inPoint = this.getType() == Type.IN ? this : null;
        }

        //check if same connection type
        if (outPoint == null || inPoint == null) {
            return false;
        }

        //check if same element
        if (outPoint.getElementModel().equals(inPoint.getElementModel())) {
            return false;
        }


        //Check for loop
        return outPoint.getElementModel().verifyNoLoop(inPoint);
    }
}
