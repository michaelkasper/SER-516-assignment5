package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class SpaceModel extends AbstractModel {

    public final static String EVENT_ELEMENT_ADDED = "event_element_added";

    private ArrayList<AbstractElement> elements = new ArrayList<>();

    /**
     * TODO: Add ArrayList of AbstractElements
     * TODO: Add way to add to list
     * TODO: Broadcast view update when list updated
     */
    public SpaceModel() {
        super();
    }


    public void addElement(AbstractElement element) {
        this.elements.add(element);
        this.support.firePropertyChange(EVENT_ELEMENT_ADDED, null, element);// add to tabs
    }

    public boolean hasElement(AbstractElement element) {
        //TODO: check if element is in the ArrayList
        return false;
    }

}
