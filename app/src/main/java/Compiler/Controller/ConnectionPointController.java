package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.PropertyChangeDecorator;
import Compiler.View.Components.ConnectionPoint.AbstractConnectionPoint;

import javax.swing.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


public class ConnectionPointController extends PropertyChangeDecorator {
    public AbstractElement elementModel;
    
    public ConnectionPointController(AbstractElement elementModel) {
        this.elementModel = elementModel;

    }

    public boolean onConnectionDrop(TransferHandler.TransferSupport support) {
        try {
            if (support.isDataFlavorSupported(AbstractConnectionPoint.DRAGGABLE_IN_FLAG)) {
                this.elementModel.getSpaceModel().createConnection((String) support.getTransferable().getTransferData(AbstractConnectionPoint.DRAGGABLE_IN_FLAG), this.getElementModel().getId());
            }

            if (support.isDataFlavorSupported(AbstractConnectionPoint.DRAGGABLE_OUT_FLAG)) {
                this.elementModel.getSpaceModel().createConnection(this.getElementModel().getId(), (String) support.getTransferable().getTransferData(AbstractConnectionPoint.DRAGGABLE_OUT_FLAG));
            }
            return true;

        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }


    public AbstractElement getElementModel() {
        return this.elementModel;
    }
}
