package Compiler.Service.DragAndDrop;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DragAndDrop extends MouseAdapter {

    private static DragAndDrop instance = null;
    private final TransferHandler transferHandler;
    private ArrayList<AbstractDropJPanel> dropZones = new ArrayList<>();

    public static DragAndDrop getInstance() {
        if (instance == null) {
            instance = new DragAndDrop();
        }
        return instance;
    }

    public DragAndDrop() {
        this.transferHandler = new TransferHandler();
    }


    public void registerDropComponent(AbstractDropJPanel component) {
        dropZones.add(component);
        component.setTransferHandler(this.transferHandler);

    }


    public void registerDragComponent(JComponent component) {
        component.addMouseMotionListener(this);
        component.setTransferHandler(this.transferHandler);
    }


    public void mouseDragged(MouseEvent e) {
        if (e.getComponent() instanceof AbstractDragJPanel) {
            AbstractDragJPanel component = (AbstractDragJPanel) e.getComponent();
            if (component.canDrag()) {
                this.transferHandler.exportAsDrag(component, e, javax.swing.TransferHandler.MOVE, this.dropZones);
            }
        }
    }
}
