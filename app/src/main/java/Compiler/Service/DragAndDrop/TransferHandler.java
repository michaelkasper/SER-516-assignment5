package Compiler.Service.DragAndDrop;


import Compiler.Service.Image;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TransferHandler extends javax.swing.TransferHandler {
    private ArrayList<AbstractDropJPanel> dropZones;

    @Override
    public int getSourceActions(JComponent sourceComponent) {
        return javax.swing.TransferHandler.MOVE;
    }


    @Override
    protected Transferable createTransferable(JComponent dragComponent) {
        if (dragComponent instanceof AbstractDragJPanel) {
            return ((AbstractDragJPanel) dragComponent).getDragTransferable();
        }
        return null;
    }


    /**
     * after drop
     */
    @Override
    protected void exportDone(JComponent component, Transferable data, int action) {
        if (component instanceof AbstractDragJPanel) {
            AbstractDragJPanel dragComponent = (AbstractDragJPanel) component;

            this.loopRelatedDropZones(dragComponent.getTransferDataFlavors()[0], AbstractDropJPanel::draggingEnd);
            dragComponent.onDragComplete();
        }
    }


    /**
     * on drop
     */
    @Override
    public boolean importData(javax.swing.TransferHandler.TransferSupport support) {
        if (support.getComponent() instanceof AbstractDropJPanel) {
            if (!canImport(support))
                return false;

            return ((AbstractDropJPanel) support.getComponent()).onDrop(support);
        }
        return false;
    }

    /**
     * CAN DROP
     */
    @Override
    public boolean canImport(javax.swing.TransferHandler.TransferSupport support) {
        if (support.getComponent() instanceof AbstractDropJPanel) {
            AbstractDropJPanel component = (AbstractDropJPanel) support.getComponent();
            for (DataFlavor flavor : component.getAllowedDraggableFlags()) {
                if (support.isDataFlavorSupported(flavor)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void loopRelatedDropZones(DataFlavor allowedFlavor, Consumer<AbstractDropJPanel> callback) {
        for (AbstractDropJPanel dropzone : this.dropZones) {
            for (DataFlavor flavor : dropzone.getAllowedDraggableFlags()) {
                if (allowedFlavor.equals(flavor)) {
                    callback.accept(dropzone);
                }
            }
        }
    }

    /**
     * Drag started
     */
    public void exportAsDrag(AbstractDragJPanel component, InputEvent e, int action, ArrayList<AbstractDropJPanel> dropZones) {
        this.dropZones = dropZones;

        this.loopRelatedDropZones(component.getTransferDataFlavors()[0], AbstractDropJPanel::draggingStart);
        this.setDragImage(Image.createImage(component));
        component.onDragStart();
        super.exportAsDrag(component, e, action);
    }

}
