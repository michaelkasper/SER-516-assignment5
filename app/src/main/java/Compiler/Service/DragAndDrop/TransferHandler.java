package Compiler.Service.DragAndDrop;


import Compiler.Service.Image;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class TransferHandler extends javax.swing.TransferHandler {
    private ArrayList<DropInterface> dropZones;

    @Override
    public int getSourceActions(JComponent sourceComponent) {
        return javax.swing.TransferHandler.MOVE;
    }


    @Override
    protected Transferable createTransferable(JComponent dragComponent) {
        if (dragComponent instanceof DragInterface) {
            return ((DragInterface) dragComponent).getDragTransferable();
        }
        return null;
    }


    /**
     * after drop
     */
    @Override
    protected void exportDone(JComponent component, Transferable data, int action) {
        if (component instanceof DragInterface) {
            DragInterface dragComponent = (DragInterface) component;

            this.loopRelatedDropZones(dragComponent, dragComponent.getTransferDataFlavors()[0], DropInterface::dropZoneDraggingEnd);
            dragComponent.onDragComplete();
        }
    }


    /**
     * on drop
     */
    @Override
    public boolean importData(javax.swing.TransferHandler.TransferSupport support) {
        if (support.getComponent() instanceof DropInterface) {
            if (!canImport(support))
                return false;

            return ((DropInterface) support.getComponent()).onDrop(support);
        }
        return false;
    }

    /**
     * CAN DROP
     */
    @Override
    public boolean canImport(javax.swing.TransferHandler.TransferSupport support) {
        try {
            if (support.getComponent() instanceof DropInterface) {
                DropInterface component = (DropInterface) support.getComponent();

                for (DataFlavor flavor : component.getAllowedDraggableFlags()) {
                    if (support.isDataFlavorSupported(flavor)) {
                        Object transferData = support.getTransferable().getTransferData(flavor);
                        if (transferData != null) {
                            String dragId = transferData.toString();
                            if (component.canDropDragComponent(dragId)) {
                                return true;
                            }
                        }
                    }

                }
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void loopRelatedDropZones(DragInterface dragComponent, DataFlavor allowedFlavor, Consumer<DropInterface> callback) {
        try {
            for (DropInterface dropzone : this.dropZones) {
                for (DataFlavor flavor : dropzone.getAllowedDraggableFlags()) {
                    if (allowedFlavor.equals(flavor)) {
                        Object transferData = dragComponent.getTransferData(flavor);
                        if (transferData != null) {
                            String dragId = transferData.toString();
                            if (dropzone.canDropDragComponent(dragId)) {
                                callback.accept(dropzone);
                            }
                        }
                    }
                }
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Drag started
     */
    public <T extends JPanel & DragInterface> void exportAsDrag(T component, InputEvent e, int action, ArrayList<DropInterface> dropZones) {
        this.dropZones = dropZones;

        this.loopRelatedDropZones(component, component.getTransferDataFlavors()[0], DropInterface::dropZoneDraggingStart);
        this.setDragImage(Image.createImage(component));
        super.exportAsDrag(component, e, action);
    }

}
