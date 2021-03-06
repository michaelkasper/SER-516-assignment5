package Compiler.Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.UUID;

abstract public class AbstractModel {


    protected final UUID id;
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public AbstractModel() {
        this.id = UUID.randomUUID();
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }
}
