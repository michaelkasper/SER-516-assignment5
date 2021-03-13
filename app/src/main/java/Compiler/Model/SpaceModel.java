package Compiler.Model;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.Timer;

import java.util.ArrayList;
import java.util.HashMap;

public class SpaceModel extends AbstractModel {

    public final static String EVENT_ELEMENT_ADDED = "event_element_added";
    public final static String EVENT_CONNECTION_CREATED = "event_connection_created";
    public final static String EVENT_CONNECTION_STARTED = "event_connection_started";


    private ArrayList<AbstractElement> elements = new ArrayList<>();
    private HashMap<ConnectionPointModel.Type, ConnectionPointModel> futureConnection = new HashMap<ConnectionPointModel.Type, ConnectionPointModel>();

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

    public ConnectionPointModel getElementConnectionPointById(String id) {
        for (AbstractElement element : this.elements) {
            for (ConnectionPointModel point : element.getAllConnectionPoints()) {
                if (point.getId().equals(id)) {
                    return point;
                }
            }
        }
        return null;
    }

    public ArrayList<AbstractElement> getElements() {
        return this.elements;
    }

    public void createConnection(ConnectionPointModel inConnection, String outConnectionId) {
        this.createConnection(inConnection, this.getElementConnectionPointById(outConnectionId));
    }

    public void createConnection(String inConnectionId, ConnectionPointModel outConnection) {
        this.createConnection(this.getElementConnectionPointById(inConnectionId), outConnection);
    }

    public void createConnection(ConnectionPointModel inConnection, ConnectionPointModel outConnection) {

        ConnectionModel connection = new ConnectionModel(inConnection, outConnection);

        inConnection.getElementModel().addConnection(connection);
        outConnection.getElementModel().addConnection(connection);

        inConnection.setCurrentConnection(connection);
        outConnection.setCurrentConnection(connection);

        futureConnection.replace(ConnectionPointModel.Type.IN, null);
        futureConnection.replace(ConnectionPointModel.Type.OUT, null);

        this.support.firePropertyChange(EVENT_CONNECTION_CREATED, null, true);
    }

    public void startConnection(ConnectionPointModel newConnectionPointModel) {
        ConnectionPointModel currentPoint = futureConnection.get(newConnectionPointModel.getType());
        if (currentPoint != null && currentPoint.getId().equals(newConnectionPointModel.getId())) {
            futureConnection.put(newConnectionPointModel.getType(), null);
            this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);
            return;
        }

        futureConnection.put(newConnectionPointModel.getType(), newConnectionPointModel);

        ConnectionPointModel inPoint = futureConnection.get(ConnectionPointModel.Type.IN);
        ConnectionPointModel outPoint = futureConnection.get(ConnectionPointModel.Type.OUT);

        this.support.firePropertyChange(EVENT_CONNECTION_STARTED, null, true);

        if (inPoint != null && outPoint != null) {
            Timer.setTimeout(() -> {
                this.createConnection(inPoint, outPoint);
            }, 150);
        }
    }

    public boolean isFutureConnection(ConnectionPointModel pointModel) {
        ConnectionPointModel inPoint = futureConnection.get(ConnectionPointModel.Type.IN);
        ConnectionPointModel outPoint = futureConnection.get(ConnectionPointModel.Type.OUT);

        return (inPoint != null && inPoint.getId().equals(pointModel.getId())) || (outPoint != null && outPoint.getId().equals(pointModel.getId()));
    }
}
