package Compiler.Model.Elements;

import Compiler.Model.AbstractModel;
import Compiler.Model.ConnectionModel;
import Compiler.Model.SpaceModel;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class AbstractElement extends AbstractModel {

    public static final String EVENT_CONNECTION_MADE = "event_connection_made";
    public static final String EVENT_POSITION_UPDATED = "event_position_updated";


    private ArrayList<AbstractElement> connectionsIn = new ArrayList<>();
    private ArrayList<AbstractElement> connectionsOut = new ArrayList<>();
    private SpaceModel spaceModel;
    public final String symbol;
    public final int inputs;
    public final int outputs;
    private Point position = new Point(-1, -1);

    public AbstractElement(String symbol, int inputs, int outputs) {
        super();
        this.symbol = symbol;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public void addConnectionIn(AbstractElement element) {
        this.connectionsIn.add(element);
        this.support.firePropertyChange(EVENT_CONNECTION_MADE, null, new ConnectionModel(this, element));
    }

    public void addConnectionOut(AbstractElement element) {
        this.connectionsOut.add(element);
        this.support.firePropertyChange(EVENT_CONNECTION_MADE, null, new ConnectionModel(element, this));
    }

    public AbstractElement rest() {
        this.connectionsIn.clear();
        this.connectionsOut.clear();

        return this;
    }

    public void setPosition(Point dropPoint) {
        this.position = dropPoint;
        this.support.firePropertyChange(AbstractElement.EVENT_POSITION_UPDATED, null, this);
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

    public static AbstractElement Factory(String className) {
        switch (className) {
            case "OpenIfElement":
                return new OpenIfElement();
            case "CloseIfElement":
                return new CloseIfElement();
            case "CommandElement":
                return new CommandElement();
            case "LoopElement":
                return new LoopElement();
            case "MethodEndElement":
                return new MethodEndElement();
            case "MethodStartElement":
                return new MethodStartElement();
            case "ThreadElement":
                return new ThreadElement();
        }
        return null;
    }

}
