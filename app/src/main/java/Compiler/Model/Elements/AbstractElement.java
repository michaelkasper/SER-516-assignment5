package Compiler.Model.Elements;

import Compiler.Model.AbstractModel;
import Compiler.Model.SpaceModel;
import Compiler.Service.ElementState;
import Compiler.Service.Store;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.function.Function;

public abstract class AbstractElement extends AbstractModel {

    public static final String EVENT_POSITION_UPDATED = "event_position_updated";
    public static final String EVENT_STATE_UPDATED = "event_state_updated";
    public final static String EVENT_UPDATE_ERRORS = "event_update_errors";

    private String symbol;
    private int inCount;
    private int outCount;
    private String value;
    private SpaceModel spaceModel;
    private Point position = new Point(-1, -1);
    private ElementState state = ElementState.DEFAULT;
    protected final ArrayList<AbstractElement> fromConnections = new ArrayList<>();
    protected final ArrayList<AbstractElement> toConnections = new ArrayList<>();
    protected final ArrayList<String> errors = new ArrayList<>();

    public AbstractElement() {
        super();
    }

    public AbstractElement(JSONObject data) {
        super(data);
        this.symbol = (String) data.get("symbol");
        this.value = (String) data.get("value");
        this.state = ElementState.valueOf((String) data.get("state"));

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
        return this.symbol;
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

        this.getSpaceModel().getChangeSupport().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_CREATED, this::updateState);
        this.getSpaceModel().getChangeSupport().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_STARTED, this::updateState);
        this.updateState(null);
    }

    public SpaceModel getSpaceModel() {
        return this.spaceModel;
    }

    public ArrayList<String> validateCompile() {
        this.errors.clear();
        this.errors.addAll(this.validateCompileConnections());
        this.getChangeSupport().firePropertyChange(EVENT_UPDATE_ERRORS, null, true);
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
        return this.inCount;
    }

    public int getOutCount() {
        return this.outCount;
    }

    public ElementState getState() {
        return state;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public ArrayList<AbstractElement> getFromConnections() {
        return this.fromConnections;
    }

    public ArrayList<AbstractElement> getToConnections() {
        return this.toConnections;
    }

    public void addFromConnections(AbstractElement fromElement) {
        this.fromConnections.add(fromElement);
        this.updateLoopFlag(null, AbstractElement::getFromConnections);
    }

    public void addToConnections(AbstractElement toElement) {
        this.toConnections.add(toElement);
        this.updateLoopFlag(null, AbstractElement::getToConnections);
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
        try {
            this.verifyNoLoop(toElement);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void verifyNoLoop(AbstractElement toElement) throws Exception {
        ArrayList<String> priorConnections = new ArrayList<>();
        priorConnections.add(toElement.getId());
        priorConnections.add(this.getId());

        this.verifyNoLoop(priorConnections, AbstractElement::getToConnections);
        this.verifyNoLoop(priorConnections, AbstractElement::getFromConnections);
    }

    protected void verifyNoLoop(ArrayList<String> priorConnections, Function<AbstractElement, ArrayList<AbstractElement>> getCollection) throws Exception {
        for (AbstractElement element : getCollection.apply(this)) {
            String flag = element.getId();
            if (priorConnections.contains(flag)) {
                if (!this.isLoopAllowed() && !element.isLoopAllowed()) {
                    throw new Exception("loop found");
                }
            } else {
                element.verifyNoLoop(new ArrayList<>(priorConnections) {{
                    add(flag);
                }}, getCollection);
            }
        }
    }

    protected boolean isLoopAllowed() {
        return false;
    }

    public void updateLoopFlag(String flag, Function<AbstractElement, ArrayList<AbstractElement>> getCollection) {
        for (AbstractElement fromElement : getCollection.apply(this)) {
            fromElement.updateLoopFlag(flag, getCollection);
        }
    }

    public JSONObject export() {
        JSONObject obj = super.export();

        obj.put("class", this.getClass().getSimpleName());
        obj.put("symbol", symbol);
        obj.put("value", value);
        obj.put("state", state.toString());
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
            this.setSpaceModel(Store.getInstance().getSpaceById(spaceModelId));
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


    protected ArrayList<String> validateCompileConnections() {
        ArrayList<String> errors = new ArrayList<>();

        if (this.hasOpenInConnections()) {
            errors.add("Missing In Connections");
        }

        if (this.hasOpenOutConnections()) {
            errors.add("Missing Out Connections");
        }
        return errors;
    }

    private void updateState(PropertyChangeEvent e) {
        ElementState currentState = this.state;
        this.state = ElementState.DEFAULT;

        AbstractElement selectedFromElement = this.getSpaceModel().getSelectedFromElement();
        AbstractElement selectedToElement = this.getSpaceModel().getSelectedToElement();

        if (selectedToElement != null && selectedToElement.equals(this)) {
            this.state = ElementState.SELECTED;

        } else if (selectedFromElement != null) {
            if (selectedFromElement.equals(this)) {
                this.state = ElementState.SELECTED;

            } else if (selectedToElement == null && this.hasOpenInConnections() && selectedFromElement.isAllowedToConnectTo(this)) {
                this.state = ElementState.HIGHLIGHTED;
            }
        }

        if (currentState != this.state) {
            this.getChangeSupport().firePropertyChange(EVENT_STATE_UPDATED, null, true);
        }
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
