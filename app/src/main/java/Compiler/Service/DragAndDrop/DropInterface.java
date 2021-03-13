package Compiler.Service.DragAndDrop;

import java.awt.datatransfer.DataFlavor;

public interface DropInterface extends DragAndDropInterface {
    boolean onDrop(TransferHandler.TransferSupport support);

    DataFlavor[] getAllowedDraggableFlags();

    void dropZoneDraggingStart();

    void dropZoneDraggingEnd();
}
