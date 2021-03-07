package Compiler.Controller;

import Compiler.Model.SpaceModel;
import Compiler.View.Components.Element;
import Compiler.View.MainFrame;
import Compiler.View.Sidebar;
import Compiler.View.Space;
import Compiler.View.Spaces;
import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DragController extends MouseAdapter {

    public DragController(Sidebar sidebar) {
        sidebar.addMouseMotionListener(this);
        sidebar.setTransferHandler(new ElementTransferHandler());
    }

    public void mouseDragged(MouseEvent e) {
        Sidebar sidebar = (Sidebar) e.getComponent();
        Component component = sidebar.getComponentAt(e.getPoint());
        if (component != null ) {
            if ( component instanceof Element) {
                System.out.println("MouseDragged");
                Element element = (Element) component;
                element.setMoving(true);
                sidebar.getParent();
                TransferHandler th = sidebar.getTransferHandler();
                th.exportAsDrag(sidebar, e, TransferHandler.MOVE);
            }
        }
    }
}
