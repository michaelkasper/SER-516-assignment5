package Compiler.Service;

import Compiler.Model.AbstractModel;
import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;

import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class Store extends MouseAdapter {

    private static Store instance = null;
    private HashMap<String, SpaceModel> spaces = new HashMap<>();
    private HashMap<String, AbstractElement> elements = new HashMap<>();
    private HashMap<String, ConnectionPointModel> points = new HashMap<>();


    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    public Store() {

    }

    public ArrayList<SpaceModel> getAllSpaces() {
        return new ArrayList<SpaceModel>(spaces.values());
    }

    public ArrayList<AbstractElement> getAllElements() {
        return new ArrayList<AbstractElement>(elements.values());
    }

    public ArrayList<ConnectionPointModel> getAllConnectionPoints() {
        return new ArrayList<ConnectionPointModel>(points.values());
    }

    public SpaceModel getSpaceById(String id) {
        return spaces.get(id);
    }

    public AbstractElement getElementById(String id) {
        return elements.get(id);
    }

    public ConnectionPointModel getConnectionPointById(String id) {
        return points.get(id);
    }

    public void register(SpaceModel space) {
        spaces.put(space.getId(), space);
    }

    public void register(AbstractElement element) {
        elements.put(element.getId(), element);
    }

    public void register(ConnectionPointModel point) {
        points.put(point.getId(), point);
    }

    public void register(AbstractModel model) {
        if (model instanceof SpaceModel) {
            this.register((SpaceModel) model);
            return;
        }

        if (model instanceof AbstractElement) {
            this.register((AbstractElement) model);
            return;
        }

        if (model instanceof ConnectionPointModel) {
            this.register((ConnectionPointModel) model);
            return;
        }
    }

    public void clear() {
        spaces.clear();
        elements.clear();
        points.clear();
    }
}
