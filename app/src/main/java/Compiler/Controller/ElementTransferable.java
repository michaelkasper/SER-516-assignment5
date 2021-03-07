package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.View.Components.Element;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ElementTransferable implements Transferable {

    private DataFlavor widgetFlavor = new DataFlavor(Element.class,"Draggable Element");
    private DataFlavor[] flavorArray = { widgetFlavor, DataFlavor.stringFlavor };
    private Class elementModel;

    public ElementTransferable(Element element) {
        elementModel = element.getElementModel().getClass();
    }

    public DataFlavor[] getTransferDataFlavors() {
        return flavorArray;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(widgetFlavor) ||
                flavor.equals(DataFlavor.stringFlavor);
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (flavor.equals(widgetFlavor))
            return this.elementModel;
        return null;
    }
}
