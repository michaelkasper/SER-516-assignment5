package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

import java.util.ArrayList;

public class SpaceModel extends AbstractModel {

    public final static String EVENT_ELEMENT_ADDED = "event_element_added";
    public final static String EVENT_CONNECTION_CREATED = "event_connection_created";

    private ArrayList<AbstractElement> elements = new ArrayList<>();

    public SpaceModel() {
        super();
    }

    public void addElement(AbstractElement element) {
        element.setSpaceModel(this);
        this.elements.add(element);
        this.support.firePropertyChange(EVENT_ELEMENT_ADDED, null, element);// add to tabs
    }

    public boolean hasElementId(String id) {
        return this.getElementById(id) != null;
    }


    public AbstractElement getElementById(String id) {
        return this.elements.stream()
                .filter(element -> id.equals(element.getId()))
                .findFirst()
                .orElse(null);
    }

    public ArrayList<AbstractElement> getElements() {
        return this.elements;
    }

    public void createConnection(String inConnectionId, String outConnectionId) {
        AbstractElement inElement = this.getElementById(inConnectionId);
        AbstractElement outElement = this.getElementById(outConnectionId);

        inElement.addConnectionOut(outElement);
        outElement.addConnectionIn(inElement);
        this.support.firePropertyChange(EVENT_CONNECTION_CREATED, null, inElement);// add to tabs
    }
}
