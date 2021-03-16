package Compiler.View.Components.ConnectionPoint;

import Compiler.Controller.ConnectionPointController;
import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Connections.LoopConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DragInterface;
import Compiler.Service.DragAndDrop.DropInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * Individual pixel used in the grid
 */

abstract public class  AbstractConnectionPoint extends JPanel implements DragInterface, DropInterface {

    public static DataFlavor DRAGGABLE_IN_FLAG = new DataFlavor(ConnectionPointIn.class, "Draggable In Connection Point");
    public static DataFlavor DRAGGABLE_OUT_FLAG = new DataFlavor(ConnectionPointOut.class, "Draggable Out Connection Point");

    private final ConnectionPointController connectionPointController;

    public AbstractConnectionPoint(ConnectionPointModel connectionPointModel) {
        connectionPointModel.setView(this);
        this.connectionPointController = new ConnectionPointController(connectionPointModel);
        this.setBackground(new Color(112, 112, 112));
        this.setBorder(BorderFactory.createLineBorder(Color.black));

        if (connectionPointModel.isInSpace()) {
            this.getDragAndDropInterface().registerDragComponent(this);
            this.getDragAndDropInterface().registerDropComponent(this);
            this.addMouseListener(this.connectionPointController);

            this.connectionPointController.getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_CREATED, e -> {
                if (e.getNewValue() != null) {
                    this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            });


            this.connectionPointController.getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_STARTED, e -> {
                if (e.getNewValue() != null) {
                    ConnectionPointModel futureConnectionPoint1 = this.connectionPointController.getSpaceModel().getFutureConnection();

                    if (futureConnectionPoint1 != null) {
                        ConnectionPointModel futureConnectionPoint2 = this.connectionPointController.getSpaceModel().getFutureConnection().getConnectsTo();
                        if (futureConnectionPoint1.equals(this.getConnectionPoint())) {
                            this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                            return;
                        }

                        if (futureConnectionPoint2 != null && futureConnectionPoint2.equals(this.getConnectionPoint())) {
                            this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                            return;
                        }
                    }

                    this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                }
            });

            if (this.getConnectionPoint().getClass() == LoopConnectionPointModel.class) {
                this.setBackground(Color.orange);
            }
        }
    }

    @Override
    public boolean canDrag() {
        return !this.connectionPointController.isDragging() && this.getConnectionPoint().getConnectsTo() == null;
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

    protected AbstractElement getElementModel() {
        return this.getConnectionPoint().getElementModel();
    }

    protected SpaceModel getSpaceModel() {
        return this.getElementModel().getSpaceModel();
    }


    public String toString() {
        return this.getConnectionPoint().getId();
    }

    @Override
    public boolean canDropDragComponent(String dragId) {
        if (dragId != null && this.getConnectionPoint().getConnectsTo() == null) {
            if (this.getElementModel().getConnectionPointById(dragId) == null) {
                ConnectionPointModel dragConnectionPoint = this.getSpaceModel().getElementConnectionPointById(dragId);
                if (dragConnectionPoint != null) {
                    return this.getConnectionPoint().isAllowedToConnectTo(dragConnectionPoint);
                }
            }
        }
        return false;
    }


    abstract public DataFlavor[] getAllowedDraggableFlags();

}
