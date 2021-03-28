package Compiler.Model;

import Compiler.Service.Store;
import org.json.simple.JSONObject;

import java.beans.PropertyChangeSupport;
import java.util.UUID;

abstract public class AbstractModel {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final String id;

    public AbstractModel() {
        this.id = UUID.randomUUID().toString();
        Store.getInstance().register(this);
    }

    public AbstractModel(JSONObject data) {
        this.id = (String) data.get("id");
        Store.getInstance().register(this);
    }

    public PropertyChangeSupport getChangeSupport() {
        return propertyChangeSupport;
    }

    public String getId() {
        return id;
    }

    public boolean equals(AbstractModel other) {
        return this.id.equals(other.getId()) && this.getClass().equals(other.getClass());
    }

    public JSONObject export() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        return obj;
    }
}
