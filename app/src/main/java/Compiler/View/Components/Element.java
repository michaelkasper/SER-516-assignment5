package Compiler.View.Components;

import Compiler.Controller.ElementController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.DragAndDrop.AbstractDragJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Individual pixel used in the grid
 */

public class Element extends AbstractDragJPanel {

    public static DataFlavor DRAGGABLE_FLAG = new DataFlavor(Element.class, "Draggable Element");

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

        this.setLayout(new GridLayout(1, 3));
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        this.add(inputsRepresentation);
        this.add(symbolLabel);
        this.add(outputRepresentation);
    }

    @Override
    public boolean canDrag() {
        return !this.moving;
    }

    @Override
    public Transferable getDragTransferable() {
        return this;
    }

    @Override
    public void onDragComplete() {
        this.moving = false;
        this.setVisible(true);
        this.revalidate();
    }

    @Override
    public void onDragStart() {
        this.moving = true;
        if (this.elementModel.getSpaceModel() != null) {
            this.setVisible(false);
            this.revalidate();
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{Element.DRAGGABLE_FLAG, DataFlavor.stringFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(Element.DRAGGABLE_FLAG) || flavor.equals(DataFlavor.stringFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {

        if (flavor.equals(DataFlavor.stringFlavor)) {
            return this.elementModel.getId();
        }

        if (flavor.equals(Element.DRAGGABLE_FLAG)) {
            return new String[]{this.elementModel.getId(), this.elementModel.getClass().getSimpleName()};
        }

        return null;
    }


}
