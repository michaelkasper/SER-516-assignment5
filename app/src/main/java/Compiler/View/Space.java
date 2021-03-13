package Compiler.View;

import Compiler.Controller.SpaceController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.DragAndDrop;
import Compiler.Service.DragAndDrop.DropInterface;
import Compiler.View.Components.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.geom.AffineTransform;

public class Space extends JPanel implements DropInterface {

    private final SpaceController spaceController;

    /**
     * TODO: Listen for connection changes form AbstractElements
     * TODO: Draw Connections
     */
    public Space(SpaceModel spaceModel) {
        this.getDragAndDropInterface().registerDropComponent(this);
        this.spaceController = new SpaceController(spaceModel);
        this.setBorder(null);
        this.setLayout(null);

        this.spaceController.addPropertyChangeListener(SpaceController.EVENT_REBUILD_MAP, e -> {
            if (e.getNewValue() != null) {
                this.rebuildMap(this.spaceController.getSpaceModel());
            }
        });
    }

    public void rebuildMap(SpaceModel spaceModel) {
        this.removeAll();
        for (AbstractElement elementModel : spaceModel.getElements()) {
            Element elementView = new Element(elementModel);
            this.add(elementView);
        }

        this.repaint();
        this.revalidate();
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
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public DragAndDrop getDragAndDropInterface() {
        return DragAndDrop.getInstance();
    }

    @Override
    public boolean onDrop(TransferHandler.TransferSupport support) {
        return this.spaceController.onElementDrop(support);
    }

    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        int ARR_SIZE = 10;

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len},
                new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (AbstractElement elementModel : this.spaceController.getSpaceModel().getElements()) {
            Point from = elementModel.getPosition();
            for (AbstractElement toElement : elementModel.getConnectionsOut()) {
                Point to = toElement.getPosition();
                //g.drawLine(from.x, from.y, to.x, to.y);
                drawArrow(g, from.x, from.y, to.x, to.y);
            }
        }
    }

}
