package Compiler.Model;

import Compiler.Service.PropertyChangeDecorator;

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


    public boolean equals(AbstractModel other) {
        return this.id.equals(other.getId()) && this.getClass().equals(other.getClass());
    }
}
