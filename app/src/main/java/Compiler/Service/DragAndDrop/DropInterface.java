package Compiler.Service.DragAndDrop;

import java.awt.datatransfer.DataFlavor;

public interface DropInterface extends DragAndDropInterface {
    boolean onDrop(TransferHandler.TransferSupport support);

    DataFlavor[] getAllowedDraggableFlags();

    boolean canDropDragComponent(String dragId);
    
    void dropZoneDraggingStart();

    void dropZoneDraggingEnd();
}
