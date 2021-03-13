package Compiler.Service.DragAndDrop;

import java.awt.datatransfer.Transferable;

public interface DragInterface extends DragAndDropInterface, Transferable {
    Transferable getDragTransferable();

    void onDragComplete();

    void onDragStart();

    boolean canDrag();
}
