package Compiler.Service;

import Compiler.Model.AbstractModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;

import java.util.ArrayList;
import java.util.HashMap;

public class Store {

    private static Store instance = null;
    private final HashMap<String, SpaceModel> spaces = new HashMap<>();
    private final HashMap<String, AbstractElement> elements = new HashMap<>();

    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    public ArrayList<SpaceModel> getAllSpaces() {
        return new ArrayList<>(spaces.values());
    }

    public ArrayList<AbstractElement> getAllElements() {
        return new ArrayList<>(elements.values());
    }

    public SpaceModel getSpaceById(String id) {
        return spaces.get(id);
    }

    public AbstractElement getElementById(String id) {
        return elements.get(id);
    }

    public void register(SpaceModel space) {
        spaces.put(space.getId(), space);
    }

    public void register(AbstractElement element) {
        elements.put(element.getId(), element);
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
    }

    public void remove(SpaceModel space) {
        for (AbstractElement element : space.getElements()) {
            this.remove(element);
        }

        this.spaces.remove(space.getId());
    }

    public void remove(AbstractElement element) {
        this.elements.remove(element.getId());
    }
}
