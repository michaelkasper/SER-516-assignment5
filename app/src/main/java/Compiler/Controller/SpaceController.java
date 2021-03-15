package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.PropertyChangeDecorator;
import Compiler.View.Components.Element;

import javax.swing.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class SpaceController extends PropertyChangeDecorator {
    public final static String EVENT_REBUILD_MAP = "event_rebuild_map";

    public SpaceModel spaceModel;

    public SpaceController(SpaceModel spaceModel) {
        this.spaceModel = spaceModel;

        for (String property : new String[]{SpaceModel.EVENT_ELEMENT_ADDED, SpaceModel.EVENT_CONNECTION_CREATED}) {
            this.spaceModel.addPropertyChangeListener(property, e -> {
                if (e.getNewValue() != null) {
                    this.support.firePropertyChange(EVENT_REBUILD_MAP, null, this.spaceModel);
                }
            });
        }


        for (AbstractElement element : this.spaceModel.getElements()) {
            this.configureElement(element);
        }
    }

    public boolean onElementDrop(TransferHandler.TransferSupport support) {
        try {
            String[] data = (String[]) support.getTransferable().getTransferData(Element.DRAGGABLE_FLAG);

            String id = data[0];
            String className = data[1];

            if (!this.spaceModel.hasElementId(id)) {
                AbstractElement elementModel = AbstractElement.Factory(className);
                elementModel.setPosition(support.getDropLocation().getDropPoint());
                this.spaceModel.addElement(elementModel);

                this.configureElement(elementModel);
            } else {
                AbstractElement elementModel = this.spaceModel.getElementById(id);
                elementModel.setPosition(support.getDropLocation().getDropPoint());
            }
            return true;

        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public SpaceModel getSpaceModel() {
        return this.spaceModel;
    }


    private void configureElement(AbstractElement element) {
        element.addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, e -> {
            this.support.firePropertyChange(EVENT_REBUILD_MAP, null, this.spaceModel);
        });
    }
}
