package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.PropertyChangeDecorator;
import Compiler.View.Components.Element;
import Compiler.View.Space;

import javax.swing.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class SpaceController extends PropertyChangeDecorator {

    private SpaceModel spaceModel;
    private Space spaceView;

    public SpaceController(Space spaceView, SpaceModel spaceModel) {
        this.spaceView = spaceView;
        this.spaceModel = spaceModel;

        this.registerListeners();
    }

    public void onElementDrop(TransferHandler.TransferSupport support) {
        try {
            String[] data = (String[]) support.getTransferable().getTransferData(Element.DRAGGABLE_FLAG);

            String id = data[0];
            String className = data[1];

            if (!this.spaceModel.hasElementId(id)) {
                AbstractElement elementModel = AbstractElement.Factory(className);
                elementModel.setPosition(support.getDropLocation().getDropPoint());
                this.spaceModel.addElement(elementModel);

                elementModel.addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, e -> {
                    this.spaceView.rebuildMap(this.spaceModel);
                });
            } else {
                AbstractElement elementModel = this.spaceModel.getElementById(id);
                elementModel.setPosition(support.getDropLocation().getDropPoint());
            }

        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        this.spaceView.setOnElementDrop(this::onElementDrop);

        this.spaceModel.addPropertyChangeListener(SpaceModel.EVENT_ELEMENT_ADDED, e -> {
            if (e.getNewValue() != null) {
                this.spaceView.rebuildMap(this.spaceModel);
            }
        });
    }


}
