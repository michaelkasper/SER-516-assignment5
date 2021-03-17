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

import static Compiler.Config.ELEMENT_COLOR;
import static Compiler.Config.ELEMENT_ERROR_COLOR;

public class Element extends JPanel implements DragInterface {

    public static DataFlavor DRAGGABLE_FLAG = new DataFlavor(Element.class, "Draggable Element");

    private final ElementController elementController;
    private boolean moving = false;

    public Element(AbstractElement elementModel) {
        this.elementController = new ElementController(elementModel);
        this.getDragAndDropInterface().registerDragComponent(this);

        this.setLayout(new GridLayout(1, 3));
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBackground(ELEMENT_COLOR);

        IoRepresentation inputsRepresentation = new IoRepresentation(ConnectionPointModel.Type.IN, elementModel);
        IoRepresentation outputRepresentation = new IoRepresentation(ConnectionPointModel.Type.OUT, elementModel);

        JLabel symbolLabel = new JLabel(this.getElementModel().symbol, SwingConstants.CENTER);
        symbolLabel.setFont(symbolLabel.getFont().deriveFont(25f));

        this.setComponentZOrder(inputsRepresentation, 0);
        this.setComponentZOrder(outputRepresentation, 0);

        this.add(inputsRepresentation);
        this.add(symbolLabel);
        this.add(outputRepresentation);


        if (elementModel.getSpaceModel() != null) {
            if (elementModel.getPosition().x > -1 && elementModel.getPosition().y > -1) {
                this.setBounds(new Rectangle(elementModel.getPosition(), new Dimension(150, 75)));
            }

            this.renderErrors();
            this.addMouseListener(this.elementController);
            elementModel.getSpaceModel().addPropertyChangeListener(SpaceModel.EVENT_UPDATE_ERRORS, event -> {
                if (event.getNewValue() != null) {
                    this.renderErrors();
                }
            });
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


    private void renderErrors() {
        if (this.elementController.getElementModel().hasErrors()) {
            this.setBackground(ELEMENT_ERROR_COLOR);
            this.setToolTipText(this.elementController.getElementModel().getErrorsAsString());

        } else {
            this.setBackground(ELEMENT_COLOR);
            this.setToolTipText(null);
        }
    }
}
