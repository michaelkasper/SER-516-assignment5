package Compiler.Controller;

import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.PropertyChangeDecorator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ElementController extends PropertyChangeDecorator implements MouseListener {

    private final static int clickInterval = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
    private AbstractElement elementModel;
    private Timer timer;

    public ElementController(AbstractElement elementModel) {
        this.elementModel = elementModel;

        this.timer = new Timer(clickInterval, event -> {
            timer.stop();
            this.onSingleClick();
        });
    }

    public AbstractElement getElementModel() {
        return elementModel;
    }

    public SpaceModel getSpaceModel() {
        return this.getElementModel().getSpaceModel();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 2) return;

        if (e.getButton() == MouseEvent.BUTTON1) {
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


    private void onDoubleClick() {
        String value = JOptionPane.showInputDialog(this.getElementModel().getView(), "Input",
                this.elementModel.getValue() == null ? "Enter a Value" : this.elementModel.getValue()
        );
        elementModel.setValue(value);
    }


    private void onSingleClick() {
        ConnectionPointModel futureConnectionPoint1 = this.getSpaceModel().getFutureConnection();

        if (futureConnectionPoint1 == null) {
            ConnectionPointModel outPoint = this.getElementModel().getOpenOutConnectionPoints();
            if (outPoint != null) {
                this.getSpaceModel().startConnection(outPoint);
            } else {
                JOptionPane.showMessageDialog(this.getElementModel().getView(),
                        this.getElementModel().getOutConnectionPoints().size() > 0
                                ? "The element can't create any more out connections"
                                : "The element doesn't allow out connections");
            }
        } else {
            ConnectionPointModel inPoint = this.getElementModel().getOpenInConnectionPoints();
            if (inPoint != null) {
                try {
                    this.getSpaceModel().finishConnection(inPoint);
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(this.getElementModel().getView(), err.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this.getElementModel().getView(), this.getElementModel().getInConnectionPoints().size() > 0
                        ? "The element can't create any more in connections"
                        : "The element doesn't allow in connections");
            }
        }

    }
}
