package Compiler.View;

import Compiler.Controller.ElementController;
import Compiler.Controller.SpaceController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DropInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;

public class Space extends JPanel implements DropInterface {

    private final SpaceController spaceController;

    public Space(SpaceController spaceController) {
        this.spaceController = spaceController;
        this.getDragAndDropInterface().registerDropComponent(this);
        this.setBorder(null);
        this.setLayout(null);
        this.spaceController.getChangeSupport().addPropertyChangeListener(SpaceController.EVENT_REBUILD_MAP, this::onRebuildMap);
        this.spaceController.initializeIfElements();
    }

    private void onRebuildMap(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            this.removeAll();
            HashMap<String, Element> elementViewsMap = new HashMap<>();

            for (AbstractElement elementModel : this.spaceController.getSpaceModel().getElements()) {
                Element elementView = new Element(new ElementController(elementModel));
                elementViewsMap.put(elementModel.getId(), elementView);
                this.add(elementView);
            }

            ConnectionsLayer spaceConnectionsLayer = new ConnectionsLayer(elementViewsMap);
            this.add(spaceConnectionsLayer);
            this.setComponentZOrder(spaceConnectionsLayer, 0);

            this.repaint();
            this.revalidate();

            spaceConnectionsLayer.setBounds(0, 0, this.getWidth(), this.getHeight());
            spaceConnectionsLayer.repaint();
            spaceConnectionsLayer.revalidate();
        }
    }

    public DataFlavor[] getAllowedDraggableFlags() {
        return new DataFlavor[]{Element.DRAGGABLE_FLAG};
    }

    @Override
    public boolean canDropDragComponent(String dragId) {
        return true;
    }

    @Override
    public void dropZoneDraggingStart() {
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
    }

    @Override
    public void dropZoneDraggingEnd() {
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public DragAndDrop getDragAndDropInterface() {
        return DragAndDrop.getInstance();
    }

    @Override
    public boolean onDrop(TransferHandler.TransferSupport support) {
        return this.spaceController.onElementDrop(support);
    }
}
