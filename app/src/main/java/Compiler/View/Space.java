package Compiler.View;

import Compiler.Controller.SpaceController;
import Compiler.Model.Elements.AbstractElement;
import Compiler.Model.SpaceModel;
import Compiler.Service.DragAndDrop.AbstractDropJPanel;
import Compiler.View.Components.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;

public class Space extends AbstractDropJPanel {


    private final SpaceController spaceController;
    private Consumer<TransferHandler.TransferSupport> onElementDrop;

    /**
     * TODO: Listen for connection changes form AbstractElements
     * TODO: Draw Connections
     */
    public Space(SpaceModel spaceModel) {
        this.spaceController = new SpaceController(this, spaceModel);
        this.setBorder(null);
        this.setLayout(null);
    }

    public void rebuildMap(SpaceModel spaceModel) {
        this.removeAll();
        for (AbstractElement elementModel : spaceModel.getElements()) {
            Element elementView = new Element(elementModel);
            elementView.setBounds(new Rectangle(elementModel.getPosition(), new Dimension(150, 75)));
            elementView.addMouseListener(this.spaceController);
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
        this.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public boolean onDrop(TransferHandler.TransferSupport support) {
        onElementDrop.accept(support);
        return true;
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
        for (AbstractElement elementModel : this.spaceController.spaceModel.getElements()) {
            Point from = elementModel.getPosition();
            for (AbstractElement toElement : elementModel.getConnectionsOut()) {
                Point to = toElement.getPosition();
                //g.drawLine(from.x, from.y, to.x, to.y);
                drawArrow(g, from.x, from.y, to.x, to.y);
            }
        }
    }

}
