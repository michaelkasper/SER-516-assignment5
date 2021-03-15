package Compiler.Model.Connections;

import Compiler.Model.AbstractModel;
import Compiler.Model.Elements.*;
import Compiler.Model.SpaceModel;
import Compiler.Model.ValidationError;
import Compiler.View.Components.ConnectionPoint.AbstractConnectionPoint;
import Compiler.View.Components.ConnectionPoint.ConnectionPointIn;
import Compiler.View.Components.ConnectionPoint.ConnectionPointOut;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.HashMap;

public class ConnectionPointModel extends AbstractModel {

    private Type type;
    private AbstractElement abstractElement;
    private boolean isDragging = false;
    private AbstractConnectionPoint connectionPointView;
    private ConnectionPointModel connectsToPoint;
    private boolean isHidden = false;

    public enum Type {IN, OUT}

    public ConnectionPointModel() {
    }

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


    public JSONObject export() {
        JSONObject obj = new JSONObject();

        obj.put("id", this.getId());
        obj.put("class", this.getClass().getSimpleName());
        obj.put("type", type.toString());
        obj.put("elementId", abstractElement.getId());
        obj.put("isDragging", isDragging);
        obj.put("isHidden", isHidden);
        obj.put("connectsToPointId", connectsToPoint != null ? connectsToPoint.getId() : null);

        return obj;
    }


    public void importJson(JSONObject json) {
        this.id = (String) json.get("id");
        this.isDragging = (boolean) json.get("isDragging");
        this.isHidden = (boolean) json.get("isHidden");

        switch ((String) json.get("type")) {
            case "IN" -> this.type = Type.IN;
            case "OUT" -> this.type = Type.OUT;
        }
    }

    public void importRelationships(JSONObject json, HashMap<String, AbstractElement> elementsMap, HashMap<String, ConnectionPointModel> pointsMap) {
        String elementId = (String) json.get("elementId");
        if (elementId != null && !elementId.isEmpty()) {
            this.abstractElement = elementsMap.get(elementId);
        }

        String connectsToPointId = (String) json.get("connectsToPointId");
        if (connectsToPointId != null && !connectsToPointId.isEmpty()) {
            this.connectsToPoint = pointsMap.get(connectsToPointId);
        }
    }


    public static ConnectionPointModel Factory(String className) {
        switch (className) {
            case "ConnectionPointModel":
                return new ConnectionPointModel();
            case "LoopConnectionPointModel":
                return new LoopConnectionPointModel();
        }
        return null;
    }
}
