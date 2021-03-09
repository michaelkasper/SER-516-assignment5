package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

import java.util.ArrayList;

public class SpaceModel extends AbstractModel {

    public final static String EVENT_ELEMENT_ADDED = "event_element_added";

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
}
