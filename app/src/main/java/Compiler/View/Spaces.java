package Compiler.View;

import Compiler.Controller.WorkspaceController;
import Compiler.Model.SpaceModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Spaces extends JPanel {

    private final JTabbedPane tabbedPane = new JTabbedPane();
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

        tabbedPane.setBounds(0, 0, getWidth(), getHeight());
        tabbedPane.setTabPlacement(JTabbedPane.TOP);


//        tabbedPane.setBackground(Color.orange);
//        tabbedPane.setForeground(Color.white);

        this.add(tabbedPane);

        this.registerListeners();
    }


    private void registerListeners() {
        // listen for changes on controller only
        this.workspaceController.addPropertyChangeListener(WorkspaceController.EVENT_SPACE_ADDED, e -> {
            if (e.getNewValue() != null) {
                SpaceModel spaceModel = (SpaceModel) e.getNewValue();
                this.tabbedPane.add(String.valueOf(this.tabbedPane.getTabCount() + 1), new Space(spaceModel));
            }
        }); // add tab


        this.workspaceController.addPropertyChangeListener(WorkspaceController.EVENT_ACTIVE_SPACE_CHANGED, e -> {
            if (e.getNewValue() != null) {
                int spaceModelIndex = (int) e.getNewValue();
                tabbedPane.setSelectedIndex(spaceModelIndex);
            }
        }); // select tab
    }

    public void setTabSelectedListener(ActionListener actionListener) {

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        //TODO: REMOVE
//        g.setColor(Color.BLUE);
//        g.fillRect(0, 0, getWidth(), getHeight());
        //TODO: REMOVE

    }
}
