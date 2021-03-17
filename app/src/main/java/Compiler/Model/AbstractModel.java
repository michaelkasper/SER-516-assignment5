package Compiler.Model;

import Compiler.Service.PropertyChangeDecorator;
import Compiler.Service.Store;
import org.json.simple.JSONObject;

import java.util.UUID;

abstract public class AbstractModel extends PropertyChangeDecorator {

    protected String id;

    public AbstractModel() {
        this.id = UUID.randomUUID().toString();
        Store.getInstance().register(this);
    }

    public AbstractModel(JSONObject data) {
        super();
        this.id = (String) data.get("id");
        Store.getInstance().register(this);
    }

    public String getId() {
        return id;
    }


    public boolean equals(AbstractModel other) {
        return this.id.equals(other.getId()) && this.getClass().equals(other.getClass());
    }
}
