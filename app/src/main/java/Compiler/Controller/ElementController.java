package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeSupport;

public class ElementController implements MouseListener {

    public final static String EVENT_CONNECTION_ERROR = "event_connection_error";
    public final static String EVENT_SHOW_INPUT_POPUP = "event_show_input_popup";
    private final static int CLICK_INTERVAL = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final AbstractElement elementModel;
    private Timer timer;

    public ElementController(AbstractElement elementModel) {
        this.elementModel = elementModel;
    }

    public PropertyChangeSupport getChangeSupport() {
        return propertyChangeSupport;
    }

    public AbstractElement getElementModel() {
        return elementModel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 2) return;

        if (e.getButton() == MouseEvent.BUTTON1) {
            if (this.timer == null) {
                this.timer = new Timer(CLICK_INTERVAL, event -> {
                    timer.stop();
                    this.onSingleClick();
                });
            }

            if (this.timer.isRunning()) {
                timer.stop();
                this.onDoubleClick();
            } else {
                timer.restart();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void saveValue(String value) {
        this.elementModel.setValue(value);
    }

    private void onDoubleClick() {
        this.getChangeSupport().firePropertyChange(EVENT_SHOW_INPUT_POPUP, null,
                this.elementModel.getValue() == null
                        ? "Enter a Value"
                        : this.elementModel.getValue());
    }


    private void onSingleClick() {
        AbstractElement selectedFromElement = this.getElementModel().getSpaceModel().getSelectedFromElement();

        if (selectedFromElement == null) {
            if (!this.getElementModel().hasOpenOutConnections()) {
                this.getChangeSupport().firePropertyChange(EVENT_CONNECTION_ERROR, null,
                        this.getElementModel().getOutCount() > 0
                                ? "The element can't create any more out connections"
                                : "The element doesn't allow out connections");
                return;
            }

            this.startConnection(this.getElementModel());
            return;
        }


        if (this.getElementModel().equals(selectedFromElement)) {
            this.getElementModel().getSpaceModel().clearSelected();
            return;
        }


        if (!this.getElementModel().hasOpenInConnections()) {
            this.getChangeSupport().firePropertyChange(EVENT_CONNECTION_ERROR, null,
                    this.getElementModel().getInCount() > 0
                            ? "The element can't create any more in connections"
                            : "The element doesn't allow in connections");
            return;
        }

        try {
            this.finishConnection(this.getElementModel());
        } catch (Exception err) {
            this.getChangeSupport().firePropertyChange(EVENT_CONNECTION_ERROR, null, err.getMessage());
        }
    }

    private void startConnection(AbstractElement fromElement) {
        if (this.getElementModel().getSpaceModel().getSelectedFromElement() != null) {
            return;
        }

        this.getElementModel().getSpaceModel().setSelectedFromElement(fromElement);
    }


    private void finishConnection(AbstractElement toElement) throws Exception {
        if (this.getElementModel().getSpaceModel().getSelectedFromElement() == null) {
            return;
        }

        if (this.getElementModel().getSpaceModel().getSelectedFromElement().isAllowedToConnectTo(toElement)) {
            this.getElementModel().getSpaceModel().setSelectedToElement(toElement);

            Timer timer = new Timer(100, event -> {
                this.getElementModel().getSpaceModel().getSelectedFromElement().addToConnections(this.getElementModel().getSpaceModel().getSelectedToElement());
                this.getElementModel().getSpaceModel().getSelectedToElement().addFromConnections(this.getElementModel().getSpaceModel().getSelectedFromElement());
                this.getElementModel().getSpaceModel().clearSelected();
            });
            timer.setRepeats(false);
            timer.start();

        } else {
            throw new Exception("The elements can't connect");
        }
    }

}
