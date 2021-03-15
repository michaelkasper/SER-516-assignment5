package Compiler.View;

import Compiler.Controller.WorkspaceController;
import Compiler.Model.SpaceModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

import static Compiler.Config.*;

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
                int index = tabbedPane.getTabCount();
                tabbedPane.add("Space " + (index + 1), newSpace);
                tabbedPane.setBackgroundAt(index, Color.WHITE);
                tabbedPane.setSelectedIndex(index);

                Runnable renderErrors = () -> {
                    if (spaceModel.hasErrors()) {
                        tabbedPane.setBackgroundAt(index, TAB_ERROR_COLOR);
                        tabbedPane.setToolTipTextAt(index, spaceModel.getErrorsAsString());

                    } else {
                        tabbedPane.setBackgroundAt(index, Color.WHITE);
                        tabbedPane.setToolTipTextAt(index, null);

                    }
                };
                renderErrors.run();

                spaceModel.addPropertyChangeListener(SpaceModel.EVENT_UPDATE_ERRORS, event -> {
                    if (event.getNewValue() != null) {
                        renderErrors.run();
                    }
                });

                newSpace.rebuildMap(spaceModel);
                renderErrors.run();
            }
        });

        workspaceController.addPropertyChangeListener(WorkspaceController.EVENT_CLEAR_SPACES, e -> {
            tabbedPane.removeAll();
        });

        this.add(tabbedPane);
    }
}
