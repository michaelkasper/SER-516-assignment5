package Compiler.Controller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


abstract public class AbstractController {
    protected final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }


}
