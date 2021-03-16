package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.PropertyChangeDecorator;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ElementController extends PropertyChangeDecorator implements MouseListener {

    private AbstractElement elementModel;

    //TODO: Listen for double click, trigger popup for edit
    //TODO: Listen for popup buttons and save results
    public ElementController(AbstractElement elementModel) {
        this.elementModel = elementModel;

    }
    
    public AbstractElement getElementModel() {
        return elementModel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
            String value = JOptionPane.showInputDialog(e.getComponent()
                    , "Input"
                    , "Enter a Value");
        }
        // TODO: Add this value to element model.
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
}
