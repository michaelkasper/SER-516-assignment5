package Compiler.Model;

import Compiler.Controller.ConnectionPointController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.View.Components.ConnectionPoint.AbstractConnectionPoint;

public class ConnectionPointModel extends AbstractModel {

    private Type type;
    private AbstractElement abstractElement;
    private boolean isDragging = false;
    private boolean isCreatingConnection = false;
    private AbstractConnectionPoint connectionPointView;
    private ConnectionModel currentConnection;

    public enum Type {IN, OUT}

    public ConnectionPointModel(Type type, AbstractElement abstractElement) {
        this.type = type;
        this.abstractElement = abstractElement;
    }


    public boolean isInSpace() {
        return this.getElementModel().getSpaceModel() != null;
    }

    public AbstractElement getElementModel() {
        return abstractElement;
    }


    public void setIsCreatingConnection(boolean isCreatingConnection) {
        this.isCreatingConnection = isCreatingConnection;
        this.getElementModel().getSpaceModel().startConnection(this);
    }


    public boolean getIsCreatingConnection() {
        return this.isCreatingConnection;
    }


    public boolean isDragging() {
        return isDragging;
    }

    public void setDragging(boolean dragging) {
        isDragging = dragging;
        this.setIsCreatingConnection(dragging);
    }

    public void setView(AbstractConnectionPoint connectionPointView) {
        this.connectionPointView = connectionPointView;
    }

    public ConnectionPointModel.Type getType() {
        return type;
    }


    public AbstractConnectionPoint getConnectionPointView() {
        return connectionPointView;
    }


    public ConnectionModel getCurrentConnection() {
        return currentConnection;
    }

    public void setCurrentConnection(ConnectionModel currentConnection) {
        this.currentConnection = currentConnection;
    }

}
