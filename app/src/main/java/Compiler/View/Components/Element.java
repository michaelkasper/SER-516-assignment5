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
    private AbstractElement elementModel;

    /**
     * TODO: Make draggable
     */
    public Element(AbstractElement elementModel) {
        this.elementController = new ElementController(this, elementModel);
        this.elementModel = elementModel;

        this.add(new JLabel(this.elementModel.symbol));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // set center icon = this.elementModel.symbol

//        for (int i =0 ; i < this.elementModel.allowedIn ; i++){
//
//        }


    }
}
