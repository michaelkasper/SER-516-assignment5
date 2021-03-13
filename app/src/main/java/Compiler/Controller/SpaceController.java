package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.PropertyChangeDecorator;
import Compiler.View.Components.Element;
import Compiler.View.Space;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;


public class SpaceController extends PropertyChangeDecorator implements MouseListener {

    public SpaceModel spaceModel;
    private Space spaceView;
    private Element fromElement;
    private Element toElement;
    private boolean connectionInProgress = false;

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

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getComponent() instanceof Element){
            Element element = (Element) e.getComponent();
            if(connectionInProgress) {
                this.toElement = element;
                this.fromElement.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                createConnection();
                connectionInProgress = false;
            }
            else {
                this.fromElement = element;
                Border border = BorderFactory.createLineBorder(Color.GREEN);
                element.setBorder(border);
                connectionInProgress = true;
            }
        }
    }

    private void createConnection() {
        this.fromElement.elementModel.addConnectionOut(this.toElement.elementModel);
        this.toElement.elementModel.addConnectionIn(this.fromElement.elementModel);
        this.spaceView.rebuildMap(this.spaceModel);
        this.fromElement = null;
        this.toElement = null;
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
