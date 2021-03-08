package Compiler.View;

import Compiler.Controller.SpaceController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.View.Components.Element;
import Services.DragAndDrop.AbstractDropJPanel;
import Services.DragAndDrop.DragAndDrop;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.util.function.Consumer;

public class Space extends AbstractDropJPanel {


    private final SpaceController spaceController;
    private final DragAndDrop dragController;
    private Consumer<TransferHandler.TransferSupport> onElementDrop;

    /**
     * TODO: Make drop zone
     * TODO: Make arrayList of AbstractElements
     * TODO: Notify Space View of changes
     * TODO: Listen for connection changes form AbstractElements
     */
    public Space(SpaceModel spaceModel) {
        this.spaceController = new SpaceController(this, spaceModel);

        this.dragController = DragAndDrop.getInstance();
        this.dragController.registerDropComponent(this);

        this.setLayout(null);
        this.registerListeners();
    }


    private void registerListeners() {
        // listen for changes on controller only
    }


    public void rebuildMap(SpaceModel spaceModel) {
        this.removeAll();
        for (AbstractElement elementModel : spaceModel.getElements()) {
            Element elementView = new Element(elementModel);
            elementView.setBounds(new Rectangle(elementModel.getPosition(), new Dimension(150, 75)));
            this.add(elementView);
        }
        this.repaint();
        this.revalidate();
    }

    public void setOnElementDrop(Consumer<TransferHandler.TransferSupport> onElementDrop) {
        this.onElementDrop = onElementDrop;
    }

    public DataFlavor[] getAllowedDraggableFlags() {
        return new DataFlavor[]{Element.DRAGGABLE_FLAG};
    }

    @Override
    public void draggingStart() {
        this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4));
    }

    @Override
    public void draggingEnd() {
        this.setBorder(BorderFactory.createLineBorder(Color.gray));
    }

    @Override
    public boolean onDrop(TransferHandler.TransferSupport support) {
        onElementDrop.accept(support);
        return true;
    }

}
