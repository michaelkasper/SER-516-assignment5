package Compiler.View.Components;

import Compiler.Controller.ElementController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DragInterface;
import Compiler.View.Components.ConnectionPoint.ConnectionPointIn;
import Compiler.View.Components.ConnectionPoint.ConnectionPointOut;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Individual pixel used in the grid
 */

public class Element extends JPanel implements DragInterface {

    public static DataFlavor DRAGGABLE_FLAG = new DataFlavor(Element.class, "Draggable Element");

    private final ElementController elementController;
    private boolean moving = false;

    public Element(AbstractElement elementModel) {
        this.elementController = new ElementController(elementModel);
        this.getDragAndDropInterface().registerDragComponent(this);

        if (elementModel.getPosition().x > -1 && elementModel.getPosition().y > -1) {
            this.setBounds(new Rectangle(elementModel.getPosition(), new Dimension(150, 75)));
        }
        this.setLayout(new GridLayout(1, 3));
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        ConnectionPointIn[] pointsIn = new ConnectionPointIn[Math.abs(this.getElementModel().inputs)];
        for (int i = 0; i < pointsIn.length; i++) {
            pointsIn[i] = new ConnectionPointIn(elementModel);
        }

        ConnectionPointOut[] pointsOut = new ConnectionPointOut[Math.abs(this.getElementModel().outputs)];
        for (int i = 0; i < pointsOut.length; i++) {
            pointsOut[i] = new ConnectionPointOut(elementModel);
        }

        IoRepresentation inputsRepresentation = new IoRepresentation(this.getElementModel().inputs, pointsIn);
        IoRepresentation outputRepresentation = new IoRepresentation(this.getElementModel().outputs, pointsOut);

        this.setComponentZOrder(inputsRepresentation, 0);
        this.setComponentZOrder(outputRepresentation, 0);

        JLabel symbolLabel = new JLabel(this.getElementModel().symbol, SwingConstants.CENTER);
        symbolLabel.setFont(symbolLabel.getFont().deriveFont(25f));

        this.add(inputsRepresentation);
        this.add(symbolLabel);
        this.add(outputRepresentation);
    }

    @Override
    public boolean canDrag() {
        return !this.moving;
    }

    @Override
    public DragAndDrop getDragAndDropInterface() {
        return DragAndDrop.getInstance();
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
        if (this.getElementModel().getSpaceModel() != null) {
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
            return this.getElementModel().getId();
        }

        if (flavor.equals(Element.DRAGGABLE_FLAG)) {
            return new String[]{this.getElementModel().getId(), this.getElementModel().getClass().getSimpleName()};
        }

        return null;
    }


    public AbstractElement getElementModel() {
        return this.elementController.getElementModel();
    }
}
