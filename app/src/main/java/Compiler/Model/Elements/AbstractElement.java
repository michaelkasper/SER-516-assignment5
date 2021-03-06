package Compiler.Model.Elements;

import Compiler.Model.AbstractModel;
import Compiler.Model.ConnectionModel;
import Compiler.View.Components.Element;

import java.util.ArrayList;

public abstract class AbstractElement extends AbstractModel {
    protected final Element elementView;

    public static final String EVENT_CONNECTION_MADE = "event_connection_made";

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
    public String symbol;
    public int inputs;
    public int outputs;
    
    public AbstractElement(String symbol) {
        super();
        this.symbol = symbol;

        this.elementView = new Element(this);// TODO: not sure this is where we want this
    }

    public AbstractElement(String symbol, int inputs, int outputs) {
        super();
        this.symbol = symbol;
        this.inputs = inputs;
        this.outputs = outputs;
        this.elementView = new Element(this);// TODO: not sure this is where we want this
    }

    public void addConnectionIn(AbstractElement element) {
        this.connectionsIn.add(element);
        this.support.firePropertyChange(EVENT_CONNECTION_MADE, null, new ConnectionModel(this, element));
    }

    public void addConnectionOut(AbstractElement element) {
        this.connectionsOut.add(element);
        this.support.firePropertyChange(EVENT_CONNECTION_MADE, null, new ConnectionModel(element, this));
    }
}
