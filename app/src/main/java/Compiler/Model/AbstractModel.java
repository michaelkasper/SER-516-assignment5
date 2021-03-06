package Compiler.Model;

import Decorator.PropertyChangeDecorator;

import java.util.UUID;

abstract public class AbstractModel extends PropertyChangeDecorator {

    public final UUID id;

    public AbstractModel() {
        super();
        this.id = UUID.randomUUID();
    }
}
