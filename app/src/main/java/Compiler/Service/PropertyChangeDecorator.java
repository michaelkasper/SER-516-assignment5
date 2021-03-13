package Compiler.Service;

import java.beans.PropertyChangeListener;

public class PropertyChangeDecorator {

    public final java.beans.PropertyChangeSupport support = new java.beans.PropertyChangeSupport(this);

    public PropertyChangeDecorator() {
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }
}
