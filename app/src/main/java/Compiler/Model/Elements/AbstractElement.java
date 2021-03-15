package Compiler.Model.Elements;

import Compiler.Model.AbstractModel;
import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Connections.LoopConnectionPointModel;
import Compiler.Model.SpaceModel;
import Compiler.Model.ValidationError;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractElement extends AbstractModel implements Serializable {

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

    public String symbol;
    public int inputs;
    public int outputs;
    protected SpaceModel spaceModel;
    protected ArrayList<ConnectionPointModel> inConnectionPoints;
    protected ArrayList<ConnectionPointModel> outConnectionPoints;
    protected ArrayList<ValidationError> errors = new ArrayList<>();
    protected Point position = new Point(-1, -1);

    public AbstractElement() {
        super();
    }

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


    public JSONObject export() {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("class", this.getClass().getSimpleName());
        obj.put("symbol", symbol);
        obj.put("inputs", inputs);
        obj.put("outputs", outputs);
        obj.put("spaceModelId", spaceModel.getId());

        JSONArray inConnectionPoints = new JSONArray();
        for (ConnectionPointModel point : this.inConnectionPoints) {
            inConnectionPoints.add(point.getId());
        }
        obj.put("inConnectionPoints", inConnectionPoints);


        JSONArray outConnectionPoints = new JSONArray();
        for (ConnectionPointModel point : this.outConnectionPoints) {
            outConnectionPoints.add(point.getId());
        }
        obj.put("outConnectionPoints", outConnectionPoints);


        JSONArray errors = new JSONArray();
        for (ValidationError error : this.errors) {
            errors.add(error.getErrMessage());
        }
        obj.put("errors", errors);


        JSONObject position = new JSONObject();
        position.put("x", this.position.x);
        position.put("y", this.position.y);
        obj.put("position", position);

        return obj;
    }

    public void importJson(JSONObject json) {
        this.id = (String) json.get("id");
        this.symbol = (String) json.get("symbol");

        this.inputs = ((Long) json.get("inputs")).intValue();
        this.outputs = ((Long) json.get("outputs")).intValue();

        this.inConnectionPoints.clear();
        this.outConnectionPoints.clear();

        JSONObject position = (JSONObject) json.get("position");
        this.position = new Point(((Long) position.get("x")).intValue(), ((Long) position.get("y")).intValue());

        JSONArray errorsJson = (JSONArray) json.get("errors");
        errorsJson.forEach(errorJson -> {
            this.errors.add(new ValidationError(this, (String) errorJson));
        });
    }

    public void importRelationships(JSONObject json, HashMap<String, SpaceModel> spacesMap, HashMap<String, ConnectionPointModel> pointsMap) {
        String spaceModelId = (String) json.get("spaceModelId");
        if (spaceModelId != null && !spaceModelId.isEmpty()) {
            this.spaceModel = spacesMap.get(spaceModelId);
        }

        JSONArray inConnectionPointsJson = (JSONArray) json.get("inConnectionPoints");
        inConnectionPointsJson.forEach(inConnectionPointId -> {
            this.inConnectionPoints.add(pointsMap.get(inConnectionPointId));
        });


        JSONArray outConnectionPointsJson = (JSONArray) json.get("outConnectionPoints");
        outConnectionPointsJson.forEach(outConnectionPointId -> {
            this.outConnectionPoints.add(pointsMap.get(outConnectionPointId));
        });


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

}
