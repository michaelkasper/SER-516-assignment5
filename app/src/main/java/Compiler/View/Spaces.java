package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Spaces extends JPanel {

    private WorkspaceController workspaceController;

    /**
     * TODO: Render tabs
     * TODO: Render Active Space Panel
     *
     * @param workspaceController
     */
    public Spaces(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setSize(new Dimension(getWidth(), getHeight()));

        //TODO: REMOVE
        this.add(new JButton("test"));
        //TODO: REMOVE


    }

    private void registerListeners() {
        this.workspaceController.addPropertyChangeListener(WorkspaceController.PROPERTY_SPACES, e -> e.getNewValue()); // add tab
        this.workspaceController.addPropertyChangeListener(WorkspaceController.PROPERTY_ACTIVE_SPACE, e -> e.getNewValue()); // select tab
    }

    public void setTabSelectedListener(ActionListener actionListener) {

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO: REMOVE
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, getWidth(), getHeight());
        //TODO: REMOVE

    }
}
