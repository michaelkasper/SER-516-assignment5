package Compiler.View;

import Compiler.Model.Elements.AbstractElement;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;

public class ConnectionsLayer extends JPanel {

    private HashMap<String, Element> elementViewsMap;

    public ConnectionsLayer() {
        this.setBorder(null);
        this.setLayout(null);
        this.setOpaque(false);
    }


    protected void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();
        int ARR_SIZE = 10;

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[]{len, len - ARR_SIZE, len - ARR_SIZE, len}, new int[]{0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point spacePosition = this.getLocationOnScreen();
        for (Element fromView : this.elementViewsMap.values()) {
            AbstractElement elementModel = fromView.getController().getElementModel();
            for (AbstractElement toElement : elementModel.getToConnections()) {
                Element toView = this.elementViewsMap.get(toElement.getId());

                if (toView != null) {
                    if (fromView.isShowing() && toView.isShowing()) {
                        double fromX = fromView.getLocationOnScreen().getX() - spacePosition.getX() + fromView.getWidth();
                        double fromY = fromView.getLocationOnScreen().getY() - spacePosition.getY() + (fromView.getHeight() >> 1);

                        double toX = toView.getLocationOnScreen().getX() - spacePosition.getX();
                        double toY = toView.getLocationOnScreen().getY() - spacePosition.getY() + (toView.getHeight() >> 1);

                        drawArrow(g, (int) fromX, (int) fromY, (int) toX, (int) toY);
                    }
                }
            }
        }
    }

    public void rebuild(HashMap<String, Element> elementViewsMap) {
        this.elementViewsMap = elementViewsMap;
        this.repaint();
        this.revalidate();
    }
}
