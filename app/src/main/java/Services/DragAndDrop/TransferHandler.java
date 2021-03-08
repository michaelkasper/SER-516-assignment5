package Services.DragAndDrop;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class TransferHandler extends javax.swing.TransferHandler {
    private ArrayList<AbstractDropJPanel> dropZones;

    public int getSourceActions(JComponent sourceComponent) {
        return javax.swing.TransferHandler.MOVE;
    }

    /**
     * Drag started
     */
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
        if (action == javax.swing.TransferHandler.MOVE) {
            if (component instanceof AbstractDragJPanel) {
                AbstractDragJPanel dragComponent = (AbstractDragJPanel) component;

                for (AbstractDropJPanel dropzone : this.dropZones) {
                    for (DataFlavor flavor : dropzone.getAllowedDraggableFlags()) {
                        if (dragComponent.getTransferDataFlavors()[0].equals(flavor)) {
                            dropzone.draggingEnd();
                        }
                    }
                }

                dragComponent.onDragComplete();
            }
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


    @Override
    public void setDragImage(Image img) {
        super.setDragImage(img);
    }

    public void exportAsDrag(AbstractDragJPanel component, InputEvent e, int action, ArrayList<AbstractDropJPanel> dropZones) {
        this.dropZones = dropZones;

        for (AbstractDropJPanel dropzone : this.dropZones) {
            for (DataFlavor flavor : dropzone.getAllowedDraggableFlags()) {
                if (component.getTransferDataFlavors()[0].equals(flavor)) {
                    dropzone.draggingStart();
                }
            }
        }
        this.setDragImage(this.createImage(component));
        component.onDragStart();

        super.exportAsDrag(component, e, action);
    }


    public BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        g.dispose();
        return bi;
    }
}
