package Compiler.View;

import Compiler.Controller.SpaceController;
import Compiler.Controller.WorkspaceController;
import Compiler.Model.SpaceModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.beans.PropertyChangeEvent;

import static Compiler.Model.SpaceModel.EVENT_CONNECTION_CREATED;

public class SpacesContainer extends JPanel {

    public static final Color TAB_ERROR_COLOR = new Color(255, 146, 146);
    private final JTabbedPane tabbedPane = new JTabbedPane();

    public SpacesContainer(WorkspaceController workspaceController) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setSize(new Dimension(getWidth(), getHeight()));
        this.setBackground(new Color(229, 225, 230));
        this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(10, 10, 20, 10, new Color(219, 225, 243)), new LineBorder(Color.gray)));

        tabbedPane.setBounds(0, 0, getWidth(), getHeight());
        tabbedPane.setTabPlacement(JTabbedPane.TOP);
        tabbedPane.addChangeListener(workspaceController::onTabSelected);

        workspaceController.getChangeSupport().addPropertyChangeListener(WorkspaceController.EVENT_SELECT_TAB, this::onSelectTab);
        workspaceController.getChangeSupport().addPropertyChangeListener(WorkspaceController.EVENT_SPACE_ADDED, this::onSpaceAdded);
        workspaceController.getChangeSupport().addPropertyChangeListener(WorkspaceController.EVENT_CLEAR_SPACES, this::onResetTabs);

        this.add(tabbedPane);
    }

    private void onSpaceAdded(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            int index = tabbedPane.getTabCount();

            SpaceModel spaceModel = (SpaceModel) e.getNewValue();
            spaceModel.setIndex(index);

            SpaceController newController = new SpaceController(spaceModel);
            Space newSpace = new Space(newController);
            tabbedPane.add("Space " + (index + 1), newSpace);
            tabbedPane.setBackgroundAt(index, Color.WHITE);
            tabbedPane.setSelectedIndex(index);

            spaceModel.getChangeSupport().addPropertyChangeListener(SpaceModel.EVENT_UPDATE_ERRORS, event -> this.onUpdateErrors(spaceModel, index, event));
            spaceModel.getChangeSupport().firePropertyChange(EVENT_CONNECTION_CREATED, null, true);
        }
    }

    private void onSelectTab(PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            int selectedTab = (int) e.getNewValue();
            tabbedPane.setSelectedIndex(selectedTab);
        }
    }

    private void onResetTabs(PropertyChangeEvent e) {
        tabbedPane.removeAll();
    }


    private void onUpdateErrors(SpaceModel spaceModel, int index, PropertyChangeEvent e) {
        if (e.getNewValue() != null) {
            if (spaceModel.hasErrors()) {
                tabbedPane.setBackgroundAt(index, TAB_ERROR_COLOR);
                tabbedPane.setToolTipTextAt(index, spaceModel.getErrorsAsString());
            } else {
                tabbedPane.setBackgroundAt(index, Color.WHITE);
                tabbedPane.setToolTipTextAt(index, null);
            }
        }
    }
}
