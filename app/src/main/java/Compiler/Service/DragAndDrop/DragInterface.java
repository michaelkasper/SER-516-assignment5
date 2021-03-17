package Compiler.Service.DragAndDrop;

import java.awt.datatransfer.Transferable;

public interface DragInterface extends Transferable {
    DragAndDrop getDragAndDropInterface();

    Transferable getDragTransferable();

    void onDragComplete();

    void onDragStart();

    boolean canDrag();
}
