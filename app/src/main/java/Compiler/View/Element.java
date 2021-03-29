package Compiler.View;

import Compiler.Controller.ElementController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DragInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.beans.PropertyChangeEvent;

public class Element extends JPanel implements DragInterface {

    public static final DataFlavor DRAGGABLE_FLAG = new DataFlavor(Element.class, "Draggable Element");
    public static final Color ELEMENT_COLOR = new Color(238, 238, 238);
    public static final Color ELEMENT_ERROR_COLOR = new Color(240, 204, 204);

    private final ElementController elementController;
    private boolean moving = false;

    public Element(ElementController elementController) {
        this.elementController = elementController;
        this.getDragAndDropInterface().registerDragComponent(this);

        this.setLayout(new GridLayout(1, 1));
        this.setBackground(ELEMENT_COLOR);
        this.setMaximumSize(new Dimension(150, 75));
        JLabel symbolLabel = new JLabel(this.elementController.getElementModel().getSymbol(), SwingConstants.CENTER);
        symbolLabel.setFont(symbolLabel.getFont().deriveFont(25f));

        this.add(symbolLabel);

        if (this.elementController.getElementModel().getSpaceModel() != null) {
            this.addMouseListener(this.elementController);
            this.renderErrors(null);
            this.highlight(null);
            this.updatePosition(null);

            this.elementController.getElementModel().getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_POSITION_UPDATED, this::updatePosition);
            this.elementController.getElementModel().getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_STATE_UPDATED, this::highlight);
            this.elementController.getElementModel().getChangeSupport().addPropertyChangeListener(AbstractElement.EVENT_UPDATE_ERRORS, this::renderErrors);

            this.elementController.getChangeSupport().addPropertyChangeListener(ElementController.EVENT_CONNECTION_ERROR, this::showConnectionError);
            this.elementController.getChangeSupport().addPropertyChangeListener(ElementController.EVENT_SHOW_INPUT_POPUP, this::showInputPopup);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        Dimension arcs = new Dimension(120, 120);
    	Graphics2D graphics = (Graphics2D) g;
    	
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width, height, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.setStroke(new BasicStroke(3));
        graphics.drawRoundRect(0, 0, width, height, arcs.width, arcs.height);
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
        if (this.elementController.getElementModel().getSpaceModel() != null) {
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
            return this.elementController.getElementModel().getId();
        }

        return null;
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
        JOptionPane.showMessageDialog(this, e.getNewValue());
    }


    private void showInputPopup(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            String value = JOptionPane.showInputDialog(this, "Input", e.getNewValue());
            this.elementController.saveValue(value);
        }
    }

    private void updatePosition(PropertyChangeEvent e) {
        if (this.elementController.getElementModel().getPosition().x > -1 && this.elementController.getElementModel().getPosition().y > -1) {
            this.setBounds(new Rectangle(this.elementController.getElementModel().getPosition(), new Dimension(150, 75)));
        }
    }

    private void highlight(PropertyChangeEvent e) {
        switch (this.elementController.getElementModel().getState()) {
            case SELECTED -> this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            case HIGHLIGHTED -> this.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
            //default -> this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }

    public ElementController getController() {
        return this.elementController;
    }
}
