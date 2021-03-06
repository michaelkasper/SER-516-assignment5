package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;

import java.util.ArrayList;
import java.util.Observable;
import java.util.UUID;

public class Space extends Observable {

    private final UUID id;
    private ArrayList<AbstractElement> elements = new ArrayList<>();

    /**
     * TODO: Add ArrayList of AbstractElements
     * TODO: Add way to add to list
     * TODO: Broadcast view update when list updated
     */
    public Space() {
        this.id = UUID.randomUUID();
    }

    // Should be called when an element is dropped in a space.
    public void addElement(AbstractElement element) {
        this.elements.add(element);
        setChanged();
        notifyObservers();
    }

}
