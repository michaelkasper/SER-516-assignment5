package Compiler.Controller;

import Compiler.Model.Elements.*;
import Compiler.View.Components.Element;
import Compiler.View.Sidebar;
import Compiler.View.Space;
import Compiler.View.Spaces;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;


public class ElementTransferHandler extends TransferHandler {
    public int getSourceActions(JComponent c) {
        return TransferHandler.MOVE;
    }

    private DataFlavor widgetFlavor = new DataFlavor(Element.class,"Draggable Element");
    protected Transferable createTransferable(JComponent c) {
        for ( Component comp : c.getComponents() ) {
            if (comp instanceof Element) {
                Element element = (Element) comp;
                if (element.isMoving()) {
                    return new ElementTransferable(element);
                }
            }
        }
        return null;
    }

    protected void exportDone(JComponent source, Transferable data,
                              int action) {
        if (action == TransferHandler.MOVE) {
            for (Component component : source.getComponents()) {
                if ( component instanceof Element) {
                    Element element = (Element) component;
                    if (element.isMoving()) {
                        element.setMoving(false);
                    }
                }
            }
        }
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        if ( !canImport(support) )
            return false;

        Space space = (Space) support.getComponent();
        Rectangle bounds = new Rectangle(0,0,100,50);
        Class elementModelClass;
        try {
            elementModelClass = (Class) support.getTransferable().getTransferData(widgetFlavor);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Element element;
        if(elementModelClass == OpenIfElement.class) {
            element = new Element(new OpenIfElement());
        }

        else if(elementModelClass == CloseIfElement.class) {
            element = new Element(new CloseIfElement());
        }

        else if(elementModelClass == CommandElement.class) {
            element = new Element(new CommandElement());
        }

        else if(elementModelClass == LoopElement.class) {
            element = new Element(new LoopElement());
        }

        else if(elementModelClass == MethodEndElement.class) {
            element = new Element(new MethodEndElement());
        }

        else if(elementModelClass == MethodStartElement.class) {
            element = new Element(new MethodStartElement());
        }

        else if(elementModelClass == ThreadElement.class) {
            element = new Element(new ThreadElement());
        }

        else {
            // Default
            element = new Element(new OpenIfElement());
        }

        bounds.setLocation(support.getDropLocation().getDropPoint());
        element.setBounds(bounds);
        space.add(element);

        space.repaint();
        return true;
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        if ( support.isDataFlavorSupported(widgetFlavor) )
            return true;
        return false;
    }
}
