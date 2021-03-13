package Compiler.View.Components.ConnectionPoint;

import Compiler.Controller.ConnectionPointController;
import Compiler.Model.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DragInterface;
import Compiler.Service.DragAndDrop.DropInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Individual pixel used in the grid
 */

abstract public class AbstractConnectionPoint extends JPanel implements DragInterface, DropInterface {

    public static DataFlavor DRAGGABLE_IN_FLAG = new DataFlavor(ConnectionPointIn.class, "Draggable In Connection Point");
    public static DataFlavor DRAGGABLE_OUT_FLAG = new DataFlavor(ConnectionPointOut.class, "Draggable Out Connection Point");

    private final ConnectionPointController connectionPointController;

    public AbstractConnectionPoint(ConnectionPointModel connectionPointModel) {
        connectionPointModel.setView(this);
        this.connectionPointController = new ConnectionPointController(connectionPointModel);
        if (connectionPointModel.isInSpace()) {
            this.getDragAndDropInterface().registerDragComponent(this);
            this.getDragAndDropInterface().registerDropComponent(this);
            this.addMouseListener(this.connectionPointController);

            this.connectionPointController.getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_CREATED, e -> {
                if (e.getNewValue() != null) {
                    this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    if (this.getConnectionPoint().getCurrentConnection() != null) {
                        this.setBackground(Color.BLACK);
                    }

                }
            });


            this.connectionPointController.getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_STARTED, e -> {
                if (e.getNewValue() != null) {
                    if (this.connectionPointController.getSpaceModel().isFutureConnection(this.getConnectionPoint())) {
                        this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                    } else {
                        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    }
                }
            });
        }
        this.setBackground(new Color(112, 112, 112));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    @Override
    public boolean canDrag() {
        return !this.connectionPointController.isDragging() && this.getConnectionPoint().getCurrentConnection() == null;
    }

    @Override
    public boolean onDrop(TransferHandler.TransferSupport support) {
        return this.connectionPointController.onConnectionDrop(support);
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
    public Transferable getDragTransferable() {
        return this;
    }

    @Override
    public void onDragComplete() {
        this.connectionPointController.setIsDragging(false);
    }

    @Override
    public void onDragStart() {
        this.connectionPointController.setIsDragging(true);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        if (flavor.equals(DataFlavor.stringFlavor) || flavor.equals(AbstractConnectionPoint.DRAGGABLE_IN_FLAG) || flavor.equals(AbstractConnectionPoint.DRAGGABLE_OUT_FLAG)) {
            return this.getConnectionPoint().getId();
        }

        return null;
    }

    protected ConnectionPointModel getConnectionPoint() {
        return this.connectionPointController.getConnectionPoint();
    }

    protected boolean isDropZoneActive() {
        ArrayList<ConnectionPointModel> points = this.getConnectionPoint().getElementModel().getAllConnectionPoints();

        return points.stream().filter(ConnectionPointModel::isDragging).findFirst().orElse(null) == null
                && this.getConnectionPoint().getCurrentConnection() == null;
    }

    abstract public DataFlavor[] getAllowedDraggableFlags();

}
