package Decorator;

import java.beans.PropertyChangeListener;

public class PropertyChangeDecorator {

    public final java.beans.PropertyChangeSupport support = new java.beans.PropertyChangeSupport(this);

    public PropertyChangeDecorator() {
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }

}
