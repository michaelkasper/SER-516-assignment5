package Compiler.View;

import Compiler.Controller.ElementController;
import Compiler.Controller.SpaceController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DropInterface;
import Compiler.View.Components.Element;
import Compiler.View.Components.SpaceConnections;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.HashMap;

public class Space extends JPanel implements DropInterface {

    private final SpaceController spaceController;

    public Space(SpaceController spaceController) {
        this.spaceController = spaceController;
        this.getDragAndDropInterface().registerDropComponent(this);
        this.setBorder(null);
        this.setLayout(null);

        this.spaceController.addPropertyChangeListener(SpaceController.EVENT_REBUILD_MAP, e -> {
            if (e.getNewValue() != null) {
                this.rebuildMap();
            }
        });
    }

    public void rebuildMap() {
        this.removeAll();
        HashMap<String, Element> elementViewsMap = new HashMap<>();

        for (AbstractElement elementModel : this.spaceController.spaceModel.getElements()) {
            Element elementView = new Element(new ElementController(elementModel));
            elementViewsMap.put(elementModel.getId(), elementView);
            this.add(elementView);
        }

        SpaceConnections spaceConnectionsLayer = new SpaceConnections(elementViewsMap);
        this.add(spaceConnectionsLayer);
        this.setComponentZOrder(spaceConnectionsLayer, 0);

        this.repaint();
        this.revalidate();

        spaceConnectionsLayer.setBounds(0, 0, this.getWidth(), this.getHeight());
        spaceConnectionsLayer.repaint();
        spaceConnectionsLayer.revalidate();
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
