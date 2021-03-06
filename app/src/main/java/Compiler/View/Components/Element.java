package Compiler.View.Components;

import Compiler.Controller.ElementController;
import Compiler.Model.Elements.AbstractElement;

import javax.swing.*;
import java.awt.*;

/**
 * Individual pixel used in the grid
 */

public class Element extends JPanel {

    private final ElementController elementController;

    /**
     * TODO: Make draggable
     */
    public Element(AbstractElement elementModel) {
        this.elementController = new ElementController(this, elementModel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
