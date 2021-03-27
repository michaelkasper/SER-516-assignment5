package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.Store;
import Compiler.View.Element;

import javax.swing.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;


public class SpaceController implements MouseListener {

    public final static String EVENT_REBUILD_MAP = "event_rebuild_map";
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final SpaceModel spaceModel;

    public SpaceController(SpaceModel spaceModel) {
        this.spaceModel = spaceModel;

        this.spaceModel.getChangeSupport().addPropertyChangeListener(SpaceModel.EVENT_ELEMENT_ADDED, this::onUpdateSpace);
        this.spaceModel.getChangeSupport().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_CREATED, this::onUpdateSpace);

        for (AbstractElement element : this.spaceModel.getElements()) {
            element.getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, this::onUpdateSpace);
        }
    }

    public PropertyChangeSupport getChangeSupport() {
        return propertyChangeSupport;
    }


    public boolean onElementDrop(TransferHandler.TransferSupport support) {
        try {
            String id = (String) support.getTransferable().getTransferData(Element.DRAGGABLE_FLAG);
            AbstractElement element = Store.getInstance().getElementById(id);

            if (element != null) {
                if (element.getSpaceModel() == null) {
                    AbstractElement newElement = AbstractElement.Factory(element.getClass().getSimpleName());
                    newElement.setPosition(support.getDropLocation().getDropPoint());
                    this.spaceModel.addElement(newElement);
                    newElement.getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, this::onUpdateSpace);
                } else {
                    element.setPosition(support.getDropLocation().getDropPoint());
                }
                return true;
            }
            return false;

        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public SpaceModel getSpaceModel() {
        return this.spaceModel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 2) return;

        if (e.getButton() == MouseEvent.BUTTON1) {
            this.spaceModel.clearSelected();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private void onUpdateSpace(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            this.getChangeSupport().firePropertyChange(EVENT_REBUILD_MAP, null, this.spaceModel);
        }
    }

}
