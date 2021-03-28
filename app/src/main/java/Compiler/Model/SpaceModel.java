package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.Elements.CloseIfElement;
import Compiler.Model.Elements.OpenIfElement;
import Compiler.Service.Store;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.ArrayList;

public class SpaceModel extends AbstractModel {

    public final static String EVENT_ELEMENT_ADDED = "event_element_added";
    public final static String EVENT_CONNECTION_CREATED = "event_connection_created";
    public final static String EVENT_CONNECTION_STARTED = "event_connection_started";
    public final static String EVENT_UPDATE_ERRORS = "event_update_errors";

    private final ArrayList<AbstractElement> elements = new ArrayList<>();
    private final ArrayList<String> errors = new ArrayList<>();
    private AbstractElement selectedFromElement = null;
    private AbstractElement selectedToElement = null;
    private int index = 0;
    private boolean tabSelected = false;

    public SpaceModel() {
        super();
        this.addElement(new OpenIfElement(new Point(20, 20)));
        this.addElement(new CloseIfElement(new Point(750, 550)));
    }

    public SpaceModel(JSONObject data) {
        super(data);
        this.index = ((Long) data.get("index")).intValue();
        this.tabSelected = (boolean) data.get("tabSelected");
        
        JSONArray errorsJson = (JSONArray) data.get("errors");
        errorsJson.forEach(errorJson -> {
            this.errors.add((String) errorJson);
        });
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTabSelected(boolean tabSelected) {
        this.tabSelected = tabSelected;
    }

    public boolean isTabSelected() {
        return this.tabSelected;
    }

    public void addElement(AbstractElement element) {
        element.setSpaceModel(this);
        this.elements.add(element);
        this.getChangeSupport().firePropertyChange(EVENT_ELEMENT_ADDED, null, element.getId());
    }

    public ArrayList<AbstractElement> getElements() {
        return this.elements;
    }

    public AbstractElement getSelectedFromElement() {
        return selectedFromElement;
    }

    public AbstractElement getSelectedToElement() {
        return selectedToElement;
    }

    public void clearSelected() {
        this.setSelectedFromElement(null);
        this.setSelectedToElement(null);
        this.getChangeSupport().firePropertyChange(EVENT_CONNECTION_CREATED, null, true);
    }

    public void setSelectedFromElement(AbstractElement selectedFromElement) {
        this.selectedFromElement = selectedFromElement;
        this.getChangeSupport().firePropertyChange(EVENT_CONNECTION_STARTED, null, true);

    }

    public void setSelectedToElement(AbstractElement selectedToElement) {
        this.selectedToElement = selectedToElement;
        this.getChangeSupport().firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
    }

    public ArrayList<String> validate() {
        this.errors.clear();
        ArrayList<String> childErrors = new ArrayList<>();

        for (AbstractElement element : this.elements) {
            childErrors.addAll(element.validateCompile());
        }

        if (childErrors.size() > 0) {
            this.errors.add("Elements have errors");
            this.getChangeSupport().firePropertyChange(EVENT_UPDATE_ERRORS, null, true);
        }

        return childErrors;
    }

    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

    public String getErrorsAsString() {
        StringBuilder errorMsg = new StringBuilder("Errors:");
        for (String error : this.errors) {
            errorMsg.append(" ").append(error).append(";");
        }

        return errorMsg.toString();
    }


    public JSONObject export() {
        JSONObject obj = super.export();

        obj.put("index", this.index);
        obj.put("tabSelected", this.tabSelected);
        obj.put("selectedFromElement", selectedFromElement != null ? selectedFromElement.getId() : null);
        obj.put("selectedToElement", selectedToElement != null ? selectedToElement.getId() : null);

        JSONArray elements = new JSONArray();
        for (AbstractElement element : this.elements) {
            elements.add(element.getId());
        }
        obj.put("elements", elements);

        JSONArray errors = new JSONArray();
        for (String error : this.errors) {
            errors.add(error);
        }
        obj.put("errors", errors);

        return obj;
    }

    public void importRelationships(JSONObject json) {
        Store store = Store.getInstance();
        String selectedFromElementId = (String) json.get("selectedFromElement");
        if (selectedFromElementId != null && !selectedFromElementId.isEmpty()) {
            this.selectedFromElement = store.getElementById(selectedFromElementId);
        }

        String selectedToElementId = (String) json.get("selectedToElement");
        if (selectedToElementId != null && !selectedToElementId.isEmpty()) {
            this.selectedToElement = store.getElementById(selectedToElementId);
        }

        JSONArray elementsJson = (JSONArray) json.get("elements");
        elementsJson.forEach(elementId -> {
            this.elements.add(store.getElementById((String) elementId));
        });
    }
}
