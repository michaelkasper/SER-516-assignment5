package Compiler.Controller;

import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.Store;
import Compiler.View.Element;

import javax.swing.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;


public class SpaceController implements MouseListener {
    private final SpaceModel spaceModel;

    public SpaceController(SpaceModel spaceModel) {
        this.spaceModel = spaceModel;
    }

    public boolean onElementDrop(TransferHandler.TransferSupport support) {
        try {
            String id = (String) support.getTransferable().getTransferData(Element.DRAGGABLE_FLAG);
            AbstractElement element = Store.getInstance().getElementById(id);

            if (element != null) {
                if (element.getSpaceModel() == null) {
                    AbstractElement newElement = AbstractElement.Factory(element.getClass().getSimpleName());
                    newElement.setPosition(support.getDropLocation().getDropPoint());
                    this.spaceModel.addElement(newElement);
                } else {
                    element.setPosition(support.getDropLocation().getDropPoint());
                }
                return true;
            }
            return false;

        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public SpaceModel getSpaceModel() {
        return this.spaceModel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() > 2) return;

        if (e.getButton() == MouseEvent.BUTTON1) {
            this.spaceModel.clearSelected();
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
