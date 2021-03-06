package Compiler.Model.Elements;

import Compiler.View.Components.Element;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

public abstract class AbstractElement extends Observable {
    protected final Element elementView;
    private final UUID id;

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

    private ArrayList<AbstractElement> connectionsIn = new ArrayList<>();
    private ArrayList<AbstractElement> connectionsOut = new ArrayList<>();

    public AbstractElement() {
        this.id = UUID.randomUUID();

        this.elementView = new Element();// TODO: not sure this is where we want this
    }

    public void addConnectionIn(AbstractElement element) {
        this.connectionsIn.add(element);
        setChanged();
        notifyObservers();
    }

    public void addConnectionOut(AbstractElement element) {
        this.connectionsOut.add(element);
        setChanged();
        notifyObservers();
    }
}
