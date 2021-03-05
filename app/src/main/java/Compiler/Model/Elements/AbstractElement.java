package Compiler.Model.Elements;

import Compiler.View.Components.Element;

import java.util.UUID;

public abstract class AbstractElement {
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

    public AbstractElement() {
        this.id = UUID.randomUUID();

        this.elementView = new Element();// TODO: not sure this is where we want this
    }
}
