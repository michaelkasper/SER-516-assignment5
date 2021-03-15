package Compiler.View.Components.ConnectionPoint;

import Compiler.Model.Connections.ConnectionPointModel;

import java.awt.datatransfer.DataFlavor;

/**
 * Individual pixel used in the grid
 */

public class ConnectionPointOut extends AbstractConnectionPoint {

    public ConnectionPointOut(ConnectionPointModel elementModel) {
        super(elementModel);
    }

    public DataFlavor[] getAllowedDraggableFlags() {
        return this.isDropZoneActive() ? new DataFlavor[]{AbstractConnectionPoint.DRAGGABLE_IN_FLAG} : new DataFlavor[]{};
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{AbstractConnectionPoint.DRAGGABLE_OUT_FLAG, DataFlavor.stringFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(AbstractConnectionPoint.DRAGGABLE_OUT_FLAG) || flavor.equals(DataFlavor.stringFlavor);
    }

}
