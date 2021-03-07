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

    public DragController(Sidebar sidebar, Space space) {
        sidebar.addMouseMotionListener(this);
        //spaces.addMouseMotionListener(this);
        ElementTransferHandler transferHandler = new ElementTransferHandler();
        space.setTransferHandler(transferHandler);
        sidebar.setTransferHandler(transferHandler);
        //this.space = spaces.activeSpace;
    }

    public void mouseDragged(MouseEvent e) {
        Sidebar sidebar = (Sidebar) e.getComponent();
        Component component = sidebar.getComponentAt(e.getPoint());
        if (component != null ) {
            if ( component instanceof Element) {
                Element element = (Element) component;
                element.setMoving(true);
                sidebar.getParent();
                TransferHandler th = sidebar.getTransferHandler();
                th.exportAsDrag(sidebar, e, TransferHandler.MOVE);
            }
        }
    }
}
