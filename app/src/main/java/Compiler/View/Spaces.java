package Compiler.View;

import Compiler.Compiler;
import Compiler.Controller.WorkspaceController;
import Compiler.Model.SpaceModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static Compiler.Config.BLUE_BG_COLOR;
import static Compiler.Config.GRAY_BG_COLOR;

public class Spaces extends JPanel {

    public Spaces(WorkspaceController workspaceController) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setSize(new Dimension(getWidth(), getHeight()));
        this.setBackground(GRAY_BG_COLOR);
        this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(10, 10, 20, 10, BLUE_BG_COLOR), new LineBorder(Color.gray)));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(0, 0, getWidth(), getHeight());
        tabbedPane.setTabPlacement(JTabbedPane.TOP);

        workspaceController.addPropertyChangeListener(WorkspaceController.EVENT_SPACE_ADDED, e -> {
            if (e.getNewValue() != null) {
                SpaceModel spaceModel = (SpaceModel) e.getNewValue();
                Space newSpace = new Space(spaceModel);
                tabbedPane.add("Space " + (tabbedPane.getTabCount() + 1), newSpace);
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);

            }
        }); // add tab

        this.add(tabbedPane);
    }
}
