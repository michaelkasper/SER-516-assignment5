package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.Store;
import Compiler.View.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.IOException;


public class SpaceController {

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

    private void onUpdateSpace(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            this.getChangeSupport().firePropertyChange(EVENT_REBUILD_MAP, null, this.spaceModel);
        }
    }

    public void initializeIfElements() {
        AbstractElement newOpenIfElement = AbstractElement.Factory("OpenIfElement");
        newOpenIfElement.setPosition(new Point(20, 20));
        this.spaceModel.addElement(newOpenIfElement);
        newOpenIfElement.getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, this::onUpdateSpace);

        AbstractElement newCloseIfElement = AbstractElement.Factory("CloseIfElement");
        newCloseIfElement.setPosition(new Point(750, 550));
        this.spaceModel.addElement(newCloseIfElement);
        newCloseIfElement.getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, this::onUpdateSpace);

        this.getChangeSupport().firePropertyChange(EVENT_REBUILD_MAP, null, this.spaceModel);
    }
}
