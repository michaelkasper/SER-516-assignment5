package Compiler.Model.Elements;

import Compiler.Model.AbstractModel;
import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Connections.LoopConnectionPointModel;
import Compiler.Model.SpaceModel;
import Compiler.Model.ValidationError;
import Compiler.Service.Store;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AbstractElement extends AbstractModel implements Serializable {

    public static final String EVENT_POSITION_UPDATED = "event_position_updated";
    public String symbol;
    public int inputs;
    public int outputs;
    protected SpaceModel spaceModel;
    protected ArrayList<ConnectionPointModel> inConnectionPoints = new ArrayList<>();
    protected ArrayList<ConnectionPointModel> outConnectionPoints = new ArrayList<>();
    protected ArrayList<ValidationError> errors = new ArrayList<>();
    protected Point position = new Point(-1, -1);
    private String value;

    public AbstractElement() {
        super();
    }

    public AbstractElement(JSONObject data) {
        super(data);
        this.id = (String) data.get("id");
        this.symbol = (String) data.get("symbol");
        this.value = (String) data.get("value");

        this.inputs = ((Long) data.get("inputs")).intValue();
        this.outputs = ((Long) data.get("outputs")).intValue();

        JSONObject position = (JSONObject) data.get("position");
        this.position = new Point(((Long) position.get("x")).intValue(), ((Long) position.get("y")).intValue());

        JSONArray errorsJson = (JSONArray) data.get("errors");
        errorsJson.forEach(errorJson -> {
            this.errors.add(new ValidationError(this, (String) errorJson));
        });
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
        for (int i = 0; i < Math.abs(inputs); i++) {
            inConnectionPoints.add(new ConnectionPointModel(ConnectionPointModel.Type.IN, this));
        }

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
        obj.put("value", value);
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

    public void importRelationships(JSONObject json) {
        Store store = Store.getInstance();
        String spaceModelId = (String) json.get("spaceModelId");
        if (spaceModelId != null && !spaceModelId.isEmpty()) {
            this.spaceModel = store.getSpaceById(spaceModelId);
        }

        JSONArray inConnectionPointsJson = (JSONArray) json.get("inConnectionPoints");
        inConnectionPointsJson.forEach(inConnectionPointId -> {
            this.inConnectionPoints.add(store.getConnectionPointById((String) inConnectionPointId));
        });


        JSONArray outConnectionPointsJson = (JSONArray) json.get("outConnectionPoints");
        outConnectionPointsJson.forEach(outConnectionPointId -> {
            this.outConnectionPoints.add(store.getConnectionPointById((String) outConnectionPointId));
        });


    }

    public static AbstractElement Factory(String className) {
        return AbstractElement.Factory(className, null);
    }

    public static AbstractElement Factory(String className, JSONObject data) {
        Boolean hasData = data != null;
        switch (className) {
            case "OpenIfElement":
                return hasData ? new OpenIfElement(data) : new OpenIfElement();
            case "CloseIfElement":
                return hasData ? new CloseIfElement(data) : new CloseIfElement();
            case "CommandElement":
                return hasData ? new CommandElement(data) : new CommandElement();
            case "LoopElement":
                return hasData ? new LoopElement(data) : new LoopElement();
            case "MethodEndElement":
                return hasData ? new MethodEndElement(data) : new MethodEndElement();
            case "MethodStartElement":
                return hasData ? new MethodStartElement(data) : new MethodStartElement();
            case "ThreadElement":
                return hasData ? new ThreadElement(data) : new ThreadElement();
        }
        return null;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
