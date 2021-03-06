package Compiler.View.Components;

import Compiler.Controller.ElementController;
import Compiler.Model.Elements.AbstractElement;

import javax.swing.*;
import javax.swing.border.*;

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
        this.elementModel = elementModel;    }

    @Override
    protected void paintComponent(Graphics g) {
    	Border blackline = BorderFactory.createLineBorder(Color.black);
    	this.setBorder(blackline);
    	this.setLayout(new GridLayout(0, 3));
    	
    	IoRepresentation inputsRepresentation = new IoRepresentation(this.elementModel.inputs);
    	this.add(inputsRepresentation);    	
    	
    	JLabel symbolLabel = new JLabel(this.elementModel.symbol, SwingConstants.CENTER);
    	symbolLabel.setFont(symbolLabel.getFont().deriveFont(25f));
    	this.add(symbolLabel);
        
    	IoRepresentation outputRepresentation = new IoRepresentation(this.elementModel.outputs);
    	this.add(outputRepresentation);   
    	
    	super.paintComponent(g);
    }
}
