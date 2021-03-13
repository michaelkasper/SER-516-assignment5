package Compiler.View;

import Compiler.Controller.SpaceController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DropInterface;
import Compiler.View.Components.SpaceConnections;
import Compiler.View.Components.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class Space extends JPanel implements DropInterface {

    private final SpaceController spaceController;

    /**
     * TODO: Listen for connection changes form AbstractElements
     * TODO: Draw Connections
     */
    public Space(SpaceModel spaceModel) {
        this.spaceController = new SpaceController(spaceModel);
        this.getDragAndDropInterface().registerDropComponent(this);
        this.setBorder(null);
        this.setLayout(null);

        this.spaceController.addPropertyChangeListener(SpaceController.EVENT_REBUILD_MAP, e -> {
            if (e.getNewValue() != null) {
                this.rebuildMap(this.spaceController.getSpaceModel());
            }
        });
    }

    public void rebuildMap(SpaceModel spaceModel) {
        this.removeAll();
        SpaceConnections spaceConnectionsLayer = new SpaceConnections(this.spaceController);

        this.add(spaceConnectionsLayer);

        for (AbstractElement elementModel : spaceModel.getElements()) {
            Element elementView = new Element(elementModel);
            this.add(elementView);
        }

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
    public void draggingStart() {
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
    }

    @Override
    public void draggingEnd() {
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
