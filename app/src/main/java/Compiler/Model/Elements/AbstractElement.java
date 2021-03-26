package Compiler.Model.Elements;

import Compiler.Model.AbstractModel;
import Compiler.Model.SpaceModel;
import Compiler.Service.Store;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractElement extends AbstractModel implements Serializable {

    public static final String EVENT_POSITION_UPDATED = "event_position_updated";
    private String symbol;
    private int inCount;
    private int outCount;
    private String value;
    private SpaceModel spaceModel;
    private Point position = new Point(-1, -1);
    private final ArrayList<AbstractElement> fromConnections = new ArrayList<>();
    private final ArrayList<AbstractElement> toConnections = new ArrayList<>();
    private final ArrayList<String> errors = new ArrayList<>();


    public AbstractElement() {
        super();
    }

    public AbstractElement(JSONObject data) {
        super(data);
        this.symbol = (String) data.get("symbol");
        this.value = (String) data.get("value");

        this.inCount = ((Long) data.get("inCount")).intValue();
        this.outCount = ((Long) data.get("outCount")).intValue();

        JSONObject position = (JSONObject) data.get("position");
        this.position = new Point(((Long) position.get("x")).intValue(), ((Long) position.get("y")).intValue());

        JSONArray errorsJson = (JSONArray) data.get("errors");
        errorsJson.forEach(errorJson -> {
            this.errors.add((String) errorJson);
        });
    }

    public AbstractElement(String symbol, int inCount, int outCount) {
        super();
        this.symbol = symbol;
        this.inCount = inCount;
        this.outCount = outCount;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setPosition(Point dropPoint) {
        this.position = dropPoint;
        this.getChangeSupport().firePropertyChange(AbstractElement.EVENT_POSITION_UPDATED, null, this);
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

    public ArrayList<String> validate() {
        this.errors.clear();
        this.errors.addAll(this.validateConnections());
        return this.errors;
    }

    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

    public String getErrorsAsString() {
        String errorMsg = "Errors:";
        for (String error : this.errors) {
            errorMsg += " " + error + ";";
        }

        return errorMsg;
    }

    public int getInCount() {
        return inCount;
    }

    public int getOutCount() {
        return outCount;
    }

    public ArrayList<AbstractElement> getFromConnections() {
        return fromConnections;
    }

    public ArrayList<AbstractElement> getToConnections() {
        return toConnections;
    }


    public void addFromConnections(AbstractElement fromElement) {
        fromConnections.add(fromElement);
    }

    public void addToConnections(AbstractElement toElement) {
        toConnections.add(toElement);
    }


    public boolean hasOpenInConnections() {
        return this.fromConnections.size() < this.inCount;
    }

    public boolean hasOpenOutConnections() {
        return this.toConnections.size() < this.outCount;
    }


    public boolean isAllowedToConnectTo(AbstractElement toElement) {
        //check if same element
        if (this.equals(toElement)) {
            return false;
        }

        //check if same connection type
        if (!this.hasOpenOutConnections() || !toElement.hasOpenInConnections()) {
            return false;
        }

        //Check for loop
        return this.verifyNoLoop(toElement);
    }


    public boolean verifyNoLoop(AbstractElement toElement) {
        for (AbstractElement parentElement : this.fromConnections) {
            if (parentElement.equals(toElement)) {
                //TODO: LOOP LOGIC HERE??
                return false;
            } else {
                return parentElement.verifyNoLoop(toElement);
            }
        }
        return true;
    }


    public JSONObject export() {
        JSONObject obj = super.export();

        obj.put("class", this.getClass().getSimpleName());
        obj.put("symbol", symbol);
        obj.put("value", value);
        obj.put("inCount", inCount);
        obj.put("outCount", outCount);
        obj.put("spaceModelId", spaceModel.getId());

        JSONArray fromConnections = new JSONArray();
        for (AbstractElement element : this.fromConnections) {
            fromConnections.add(element.getId());
        }
        obj.put("fromConnections", fromConnections);


        JSONArray toConnections = new JSONArray();
        for (AbstractElement element : this.toConnections) {
            toConnections.add(element.getId());
        }
        obj.put("toConnections", toConnections);


        JSONArray errors = new JSONArray();
        for (String error : this.errors) {
            errors.add(error);
        }
        obj.put("errors", errors);


        JSONObject position = new JSONObject();
        position.put("x", this.position.x);
        position.put("y", this.position.y);
        obj.put("position", position);

        return obj;
    }

    public void importRelationships(JSONObject json) {
        String spaceModelId = (String) json.get("spaceModelId");
        if (spaceModelId != null && !spaceModelId.isEmpty()) {
            this.spaceModel = Store.getInstance().getSpaceById(spaceModelId);
        }

        JSONArray fromConnectionsJson = (JSONArray) json.get("fromConnections");
        fromConnectionsJson.forEach(elementId -> {
            this.fromConnections.add(Store.getInstance().getElementById((String) elementId));
        });


        JSONArray toConnectionsJson = (JSONArray) json.get("toConnections");
        toConnectionsJson.forEach(elementId -> {
            this.toConnections.add(Store.getInstance().getElementById((String) elementId));
        });


    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


    protected ArrayList<String> validateConnections() {
        ArrayList<String> errors = new ArrayList<>();

        if (this.hasOpenInConnections()) {
            errors.add("Missing In Connections");
        }

        if (this.hasOpenOutConnections()) {
            errors.add("Missing Out Connections");
        }
        return errors;
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
}
