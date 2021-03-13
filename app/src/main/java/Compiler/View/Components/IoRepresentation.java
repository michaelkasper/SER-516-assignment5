package Compiler.View.Components;

import Compiler.View.Components.ConnectionPoint.AbstractConnectionPoint;

import javax.swing.*;
import java.awt.*;

public class IoRepresentation extends JComponent {
    private final int WIDTH = 15;
    private final int HEIGHT = 15;
    private int count;
    private AbstractConnectionPoint[] points;

    public IoRepresentation(int count, AbstractConnectionPoint[] points) {
        this.count = count;
        this.points = points;
        this.setLayout(null);

        for (AbstractConnectionPoint point : this.points) {
            this.add(point);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        int xAlignment = (this.getWidth() / 2) - (WIDTH / 2);
        int midY = this.getHeight() / 2;
        switch (count) {
            case -1:
                int height = HEIGHT * 4;
                this.points[0].setBounds(new Rectangle(xAlignment, midY - (height / 2), WIDTH, height));
                break;
            case 1:
                this.points[0].setBounds(new Rectangle(xAlignment, midY - (HEIGHT / 2), WIDTH, HEIGHT));
                break;
            case 2:
                this.points[0].setBounds(new Rectangle(xAlignment, midY - (HEIGHT / 2) - HEIGHT, WIDTH, HEIGHT));
                this.points[1].setBounds(new Rectangle(xAlignment, midY - (HEIGHT / 2) + HEIGHT, WIDTH, HEIGHT));
                break;
        }
    }
}
