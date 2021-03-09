package Compiler.View;

import Compiler.Controller.WorkspaceController;
import Compiler.Model.SpaceModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static Compiler.Config.BLUE_BG_COLOR;
import static Compiler.Config.GRAY_BG_COLOR;

public class Spaces extends JPanel {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private WorkspaceController workspaceController;
    
    public Spaces(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setSize(new Dimension(getWidth(), getHeight()));
        this.setBackground(GRAY_BG_COLOR);
        this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(10, 10, 20, 10, BLUE_BG_COLOR), new LineBorder(Color.gray)));

        tabbedPane.setBounds(0, 0, getWidth(), getHeight());
        tabbedPane.setTabPlacement(JTabbedPane.TOP);

        this.add(tabbedPane);
        this.registerListeners();
    }


    private void registerListeners() {
        // listen for changes on controller only
        this.workspaceController.addPropertyChangeListener(WorkspaceController.EVENT_SPACE_ADDED, e -> {
            if (e.getNewValue() != null) {
                SpaceModel spaceModel = (SpaceModel) e.getNewValue();
                Space newSpace = new Space(spaceModel);
                this.tabbedPane.add("Space " + (this.tabbedPane.getTabCount() + 1), newSpace);
            }
        }); // add tab


        this.workspaceController.addPropertyChangeListener(WorkspaceController.EVENT_ACTIVE_SPACE_CHANGED, e -> {
            if (e.getNewValue() != null) {
                int spaceModelIndex = (int) e.getNewValue();
                tabbedPane.setSelectedIndex(spaceModelIndex);
            }
        }); // select tab
    }
}
