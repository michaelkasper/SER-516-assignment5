package Compiler.View.Components.ConnectionPoint;

import Compiler.Model.Connections.ConnectionPointModel;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;

/**
 * Individual pixel used in the grid
 */

public class ConnectionPointIn extends AbstractConnectionPoint {

    public ConnectionPointIn(ConnectionPointModel elementModel) {
        super(elementModel);
    }

    public DataFlavor[] getAllowedDraggableFlags() {
        return new DataFlavor[]{AbstractConnectionPoint.DRAGGABLE_OUT_FLAG};
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{AbstractConnectionPoint.DRAGGABLE_IN_FLAG, DataFlavor.stringFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(AbstractConnectionPoint.DRAGGABLE_IN_FLAG) || flavor.equals(DataFlavor.stringFlavor);
    }
}
