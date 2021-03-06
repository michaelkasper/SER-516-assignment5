package Compiler.Controller;

import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.SpaceModel;
import Compiler.Service.PropertyChangeDecorator;
import Compiler.View.Components.ConnectionPoint.AbstractConnectionPoint;

import javax.swing.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;


public class ConnectionPointController extends PropertyChangeDecorator implements MouseListener {
    private ConnectionPointModel connectionPointModel;

    public ConnectionPointController(ConnectionPointModel connectionPointModel) {
        this.connectionPointModel = connectionPointModel;
    }

    public boolean onConnectionDrop(TransferHandler.TransferSupport support) {
        try {
            if (support.isDataFlavorSupported(AbstractConnectionPoint.DRAGGABLE_IN_FLAG)) {
                String inId = (String) support.getTransferable().getTransferData(AbstractConnectionPoint.DRAGGABLE_IN_FLAG);
                return this.getSpaceModel().createConnection(inId, this.connectionPointModel);
            }

            if (support.isDataFlavorSupported(AbstractConnectionPoint.DRAGGABLE_OUT_FLAG)) {
                String outId = (String) support.getTransferable().getTransferData(AbstractConnectionPoint.DRAGGABLE_OUT_FLAG);
                return this.getSpaceModel().createConnection(this.connectionPointModel, outId);
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ConnectionPointModel getConnectionPoint() {
        return connectionPointModel;
    }

    public SpaceModel getSpaceModel() {
        return this.connectionPointModel.getElementModel().getSpaceModel();
    }

    public void setIsDragging(boolean isDragging) {
        this.connectionPointModel.setDragging(isDragging);
    }

    public boolean isDragging() {
        return this.connectionPointModel.isDragging();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(this.getConnectionPoint().getConnectsTo() == null){
            this.getSpaceModel().startConnection(this.connectionPointModel);
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
}
