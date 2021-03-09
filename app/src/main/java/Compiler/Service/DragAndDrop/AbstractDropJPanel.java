package Compiler.Service.DragAndDrop;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

abstract public class AbstractDropJPanel extends JPanel {
    public abstract boolean onDrop(TransferHandler.TransferSupport support);

    public abstract DataFlavor[] getAllowedDraggableFlags();

    public abstract void draggingStart();

    public abstract void draggingEnd();

    public AbstractDropJPanel() {
        DragAndDrop.getInstance().registerDropComponent(this);
    }
}
