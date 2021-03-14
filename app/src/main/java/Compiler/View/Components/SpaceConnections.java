package Compiler.View.Components;

import Compiler.Controller.SpaceController;
import Compiler.Model.ConnectionModel;
import Compiler.Model.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class SpaceConnections extends JPanel {

    private final SpaceController spaceController;

    /**
     * TODO: Listen for connection changes form AbstractElements
     * TODO: Draw Connections
     */
    public SpaceConnections(SpaceController spaceController) {
        this.spaceController = spaceController;
        this.setBorder(null);
        this.setLayout(null);
        this.setOpaque(false);
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
        Point spacePosition = this.getLocationOnScreen();
        for (AbstractElement elementModel : this.spaceController.getSpaceModel().getElements()) {
            for (ConnectionModel connection : elementModel.getOutConnections()) {
                ConnectionPointModel inConnectionPoint = connection.inPoint;
                ConnectionPointModel outConnectionPoint = connection.outPoint;
                if (outConnectionPoint.getConnectionPointView().isShowing() && inConnectionPoint.getConnectionPointView().isShowing()) {

                    double fromX = outConnectionPoint.getConnectionPointView().getLocationOnScreen().getX() - spacePosition.getX();
                    double fromY = outConnectionPoint.getConnectionPointView().getLocationOnScreen().getY() - spacePosition.getY();

                    double toX = inConnectionPoint.getConnectionPointView().getLocationOnScreen().getX() - spacePosition.getX();
                    double toY = inConnectionPoint.getConnectionPointView().getLocationOnScreen().getY() - spacePosition.getY();

                    drawArrow(g, (int) toX, (int) toY, (int) fromX, (int) fromY);
                }

            }
        }
    }

}
