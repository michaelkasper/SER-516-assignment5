package Compiler.View.Components;

import Compiler.Controller.DragController;
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
	private boolean moving = false;

    /**
     * TODO: Make draggable
     */
    public Element(AbstractElement elementModel) {
        this.elementController = new ElementController(this, elementModel);
        this.elementModel = elementModel;

        IoRepresentation inputsRepresentation = new IoRepresentation(this.elementModel.inputs);
        IoRepresentation outputRepresentation = new IoRepresentation(this.elementModel.outputs);

        JLabel symbolLabel = new JLabel(this.elementModel.symbol, SwingConstants.CENTER);
        symbolLabel.setFont(symbolLabel.getFont().deriveFont(25f));

        this.setLayout(new GridLayout(0, 3));
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        this.add(inputsRepresentation);
        this.add(symbolLabel);
        this.add(outputRepresentation);
    }

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		if (moving)
			setBackground(Color.LIGHT_GRAY);
		this.moving = moving;
	}

	public AbstractElement getElementModel() {
        return this.elementModel;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
