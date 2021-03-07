package Compiler.Controller;

import Compiler.Model.Elements.OpenIfElement;
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
            if ( comp instanceof Element) {
                Element element = (Element) comp;
                if ( element.isMoving() ) {
                    System.out.println("TransferableCreated");
                    return new ElementTransferable(element);
                }
            }
        }
        return null;
    }

    protected void exportDone(JComponent source, Transferable data,
                              int action) {

        if ( action == TransferHandler.MOVE ) {

            Sidebar sidebar = (Sidebar) source;
            System.out.println(source instanceof Element);
            for ( Component comp : source.getComponents() ) {
                System.out.println(comp.getClass());
                if ( comp instanceof Element ) {
                    Element element = (Element) comp;
                    if ( element.isMoving() ) {
                        System.out.println("ExportDone");
                        //canvas.remove(canvasWidget);
                        //space.repaint();
                    }
                }
            }
        }
    }

    public boolean importData(TransferHandler.TransferSupport support) {
        System.out.println("TEST1");
        if ( !canImport(support) )
            return false;

        Sidebar sidebar = (Sidebar) support.getComponent();
        Rectangle bounds = new Rectangle(0,0,10,10);
        try {
            bounds = (Rectangle) support.getTransferable().getTransferData(widgetFlavor);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Element element = new Element(new OpenIfElement());
        bounds.setLocation(support.getDropLocation().getDropPoint());
        element.setBounds(bounds);
        System.out.println("TEST");
        sidebar.add(element);
        sidebar.add(Box.createRigidArea(new Dimension(sidebar.getWidth(), 20)));

        sidebar.repaint();
        return true;
    }

    public boolean canImport(TransferHandler.TransferSupport support) {
        if ( support.isDataFlavorSupported(widgetFlavor) )
            return true;
        return false;
    }
}
