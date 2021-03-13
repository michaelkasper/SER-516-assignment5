package Compiler.View.Components;

import Compiler.Model.ConnectionPointModel;
import Compiler.Model.Elements.AbstractElement;
import Compiler.View.Components.ConnectionPoint.AbstractConnectionPoint;
import Compiler.View.Components.ConnectionPoint.ConnectionPointIn;
import Compiler.View.Components.ConnectionPoint.ConnectionPointOut;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class IoRepresentation extends JComponent {
    private final int WIDTH = 15;
    private final int HEIGHT = 15;
    private ConnectionPointModel.Type type;
    private AbstractElement abstractElement;
    private ArrayList<AbstractConnectionPoint> points = new ArrayList<AbstractConnectionPoint>();
    private int count;

    public IoRepresentation(ConnectionPointModel.Type type, AbstractElement abstractElement) {
        this.type = type;
        this.abstractElement = abstractElement;
        this.setLayout(null);

        if (type == ConnectionPointModel.Type.IN) {
            this.count = this.abstractElement.inputs;
            for (ConnectionPointModel connectionPoint : this.abstractElement.getInConnectionPoints()) {
                AbstractConnectionPoint point = new ConnectionPointIn(connectionPoint);
                this.points.add(point);
                this.add(point);
            }
        }

        if (type == ConnectionPointModel.Type.OUT) {
            this.count = this.abstractElement.outputs;
            for (ConnectionPointModel connectionPoint : this.abstractElement.getOutConnectionPoints()) {
                AbstractConnectionPoint point = new ConnectionPointOut(connectionPoint);
                this.points.add(point);
                this.add(point);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        int xAlignment = (this.getWidth() / 2) - (WIDTH / 2);
        int midY = this.getHeight() / 2;
        switch (count) {
            case -1:
                int height = HEIGHT * 4;
                this.points.get(0).setBounds(new Rectangle(xAlignment, midY - (height / 2), WIDTH, height));
                break;
            case 1:
                this.points.get(0).setBounds(new Rectangle(xAlignment, midY - (HEIGHT / 2), WIDTH, HEIGHT));
                break;
            case 2:
                this.points.get(0).setBounds(new Rectangle(xAlignment, midY - (HEIGHT / 2) - HEIGHT, WIDTH, HEIGHT));
                this.points.get(1).setBounds(new Rectangle(xAlignment, midY - (HEIGHT / 2) + HEIGHT, WIDTH, HEIGHT));
                break;
        }
    }
}
