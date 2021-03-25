package Compiler.View.Components;

import Compiler.Controller.ElementController;
import Compiler.Model.Connections.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DragInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;

public class Element extends JPanel implements DragInterface {

    public static DataFlavor DRAGGABLE_FLAG = new DataFlavor(Element.class, "Draggable Element");
    public static final Color ELEMENT_COLOR = new Color(238, 238, 238);
    public static final Color ELEMENT_ERROR_COLOR = new Color(240, 204, 204);

    private final ElementController elementController;
    private boolean moving = false;

    public Element(ElementController elementController) {
        this.elementController = elementController;
        this.getDragAndDropInterface().registerDragComponent(this);

        this.setLayout(new GridLayout(1, 1));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(ELEMENT_COLOR);

        JLabel symbolLabel = new JLabel(this.getElementModel().symbol, SwingConstants.CENTER);
        symbolLabel.setFont(symbolLabel.getFont().deriveFont(25f));

        this.add(symbolLabel);

        if (this.elementController.getElementModel().getSpaceModel() != null) {
            if (this.elementController.getElementModel().getPosition().x > -1 && this.elementController.getElementModel().getPosition().y > -1) {
                this.setBounds(new Rectangle(this.elementController.getElementModel().getPosition(), new Dimension(150, 75)));
            }

            this.addMouseListener(this.elementController);
            this.renderErrors(null);
            this.elementController.getElementModel().getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_UPDATE_ERRORS, this::renderErrors);
            this.elementController.getElementModel().getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_CREATED, this::highlight);
            this.elementController.getElementModel().getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_CONNECTION_STARTED, this::highlight);
            this.elementController.addPropertyChangeListener(ElementController.EVENT_CONNECTION_ERROR, this::showConnectionError);
        }
    }

    @Override
    public boolean canDrag() {
        return !this.moving;
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
        this.moving = false;
        this.setVisible(true);
        this.revalidate();
    }

    @Override
    public void onDragStart() {
        this.moving = true;
        if (this.getElementModel().getSpaceModel() != null) {
            this.setVisible(false);
            this.revalidate();
        }
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{Element.DRAGGABLE_FLAG, DataFlavor.stringFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(Element.DRAGGABLE_FLAG) || flavor.equals(DataFlavor.stringFlavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) {
        if (flavor.equals(Element.DRAGGABLE_FLAG) || flavor.equals(DataFlavor.stringFlavor)) {
            return this.getElementModel().getId();
        }

        return null;
    }


    public AbstractElement getElementModel() {
        return this.elementController.getElementModel();
    }


    private void renderErrors(PropertyChangeEvent e) {
        if (this.elementController.getElementModel().hasErrors()) {
            this.setBackground(ELEMENT_ERROR_COLOR);
            this.setToolTipText(this.elementController.getElementModel().getErrorsAsString());

        } else {
            this.setBackground(ELEMENT_COLOR);
            this.setToolTipText(null);
        }
    }

    private void showConnectionError(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            JOptionPane.showMessageDialog(this, e.getNewValue());
        }
    }


    private void showInputPopup(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            String value = JOptionPane.showInputDialog(this, "Input", e.getNewValue());
            this.elementController.saveValue(value);
        }
    }

    private void highlight(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {

            ConnectionPointModel futureConnectionPoint1 = this.elementController.getSpaceModel().getFutureConnection();

            if (futureConnectionPoint1 != null) {
                ConnectionPointModel futureConnectionPoint2 = this.elementController.getSpaceModel().getFutureConnection().getConnectsTo();
                if (futureConnectionPoint1.getElementModel().equals(this.getElementModel())) {
                    this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                    return;
                }

                if (futureConnectionPoint2 != null && futureConnectionPoint2.getElementModel().equals(this.getElementModel())) {
                    this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
                    return;
                }


                ConnectionPointModel potentialConnectionPoint = this.getElementModel().getOpenInConnectionPoints();
                if (potentialConnectionPoint != null && futureConnectionPoint1.isAllowedToConnectTo(potentialConnectionPoint)) {
                    this.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
                    return;
                }
            }

            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }
}
