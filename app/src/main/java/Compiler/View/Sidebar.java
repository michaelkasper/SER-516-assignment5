package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import java.awt.*;


public class Sidebar extends JPanel {

    public Sidebar(WorkspaceController workspaceController) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setSize(new Dimension(getWidth(), getHeight()));

        //TODO: REMOVE
        this.add(new JButton("test"));
        //TODO: REMOVE
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO: REMOVE
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());
        //TODO: REMOVE
    }
}
