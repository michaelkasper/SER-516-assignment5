package Compiler.View.Components.ConnectionPoint;

import Compiler.Controller.ConnectionPointController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DragInterface;
import Compiler.Service.DragAndDrop.DropInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Individual pixel used in the grid
 */

abstract public class AbstractConnectionPoint extends JPanel implements DragInterface, DropInterface {

    public static DataFlavor DRAGGABLE_IN_FLAG = new DataFlavor(ConnectionPointIn.class, "Draggable In Connection Point");
    public static DataFlavor DRAGGABLE_OUT_FLAG = new DataFlavor(ConnectionPointOut.class, "Draggable Out Connection Point");

    private final ConnectionPointController connectionPointController;
    private boolean moving = false;

    public AbstractConnectionPoint(AbstractElement elementModel) {
        this.connectionPointController = new ConnectionPointController(elementModel);
        if (elementModel.getSpaceModel() != null) {
            this.getDragAndDropInterface().registerDragComponent(this);
            this.getDragAndDropInterface().registerDropComponent(this);
        }
        this.setBackground(new Color(112, 112, 112));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    public boolean canDrag() {
        return !this.moving;
    }

    @Override
    public boolean onDrop(TransferHandler.TransferSupport support) {
        return this.connectionPointController.onConnectionDrop(support);
    }

    abstract public DataFlavor[] getAllowedDraggableFlags();

    @Override
    public void draggingStart() {
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
    }

    @Override
    public void draggingEnd() {
        this.setBorder(BorderFactory.createEmptyBorder());
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
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        if (flavor.equals(DataFlavor.stringFlavor) || flavor.equals(AbstractConnectionPoint.DRAGGABLE_IN_FLAG) || flavor.equals(AbstractConnectionPoint.DRAGGABLE_OUT_FLAG)) {
            return this.getElementModel().getId();
        }

        return null;
    }

    public AbstractElement getElementModel() {
        return this.connectionPointController.getElementModel();
    }
}
