package Compiler.Model;

import Services.PropertyChangeDecorator;

import java.util.UUID;

abstract public class AbstractModel extends PropertyChangeDecorator {

    protected String id;

    public AbstractModel() {
        super();
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }
}
