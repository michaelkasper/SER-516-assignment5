package Compiler.View;

import Compiler.Controller.ElementController;
import Compiler.Controller.SpaceController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DropInterface;
import Compiler.Service.Store;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;

public class Space extends JPanel implements DropInterface {

    private final SpaceController spaceController;
    private final HashMap<String, Element> elementViewsMap = new HashMap<>();
    private final ConnectionsLayer spaceConnectionsLayer = new ConnectionsLayer();

    public Space(SpaceController spaceController) {
        this.spaceController = spaceController;
        this.getDragAndDropInterface().registerDropComponent(this);
        this.setBorder(null);
        this.setLayout(null);
        this.addMouseListener(this.spaceController);

        this.spaceController.getSpaceModel().getChangeSupport().addPropertyChangeListener(SpaceModel.EVENT_ELEMENT_ADDED, this::onElementAdded);
        this.spaceController.getSpaceModel().getChangeSupport().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_CREATED, this::onRebuildConnections);

        this.add(this.spaceConnectionsLayer);


        for (AbstractElement elementModel : this.spaceController.getSpaceModel().getElements()) {
            this.onElementAdded(new PropertyChangeEvent(elementModel, SpaceModel.EVENT_ELEMENT_ADDED, null, elementModel.getId()));
        }
    }

    private void onElementAdded(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            String elementModelId = (String) e.getNewValue();

            if (!this.elementViewsMap.containsKey(elementModelId)) {
                AbstractElement elementModel = Store.getInstance().getElementById(elementModelId);
                Element elementView = new Element(new ElementController(elementModel));
                this.elementViewsMap.put(elementModel.getId(), elementView);
                elementModel.getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, this::onRebuildConnections);
                this.add(elementView);
                this.onRebuildConnections(null);
            }
        }
    }

    private void onRebuildConnections(PropertyChangeEvent e) {
        this.repaint();
        this.revalidate();
        this.setComponentZOrder(this.spaceConnectionsLayer, 0);
        this.spaceConnectionsLayer.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.spaceConnectionsLayer.rebuild(this.elementViewsMap);
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
