package Compiler.Controller;

import Compiler.View.Components.Element;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ElementTransferable implements Transferable {

    private DataFlavor widgetFlavor = new DataFlavor(Element.class,"Draggable Element");
    private DataFlavor[] flavorArray = { widgetFlavor, DataFlavor.stringFlavor };
    private Rectangle bounds = null;

    public ElementTransferable(Element element) {
        bounds = element.getBounds();
    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavorArray;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        System.out.println(flavor.equals(widgetFlavor));
        return flavor.equals(widgetFlavor) ||
                flavor.equals(DataFlavor.stringFlavor);
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        System.out.println(flavor.equals(widgetFlavor));
        if (flavor.equals(DataFlavor.stringFlavor))
            return this.bounds.toString();
        if (flavor.equals(widgetFlavor))
            return new Rectangle(this.bounds);
        return null;
    }
}
