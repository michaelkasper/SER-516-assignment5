package Services.DragAndDrop;

import javax.swing.*;
import java.awt.datatransfer.Transferable;

abstract public class AbstractDragJPanel extends JPanel implements Transferable {
    public abstract Transferable getDragTransferable();

    public abstract void onDragComplete();

    public abstract void onDragStart();

    public abstract boolean canDrag();
}
