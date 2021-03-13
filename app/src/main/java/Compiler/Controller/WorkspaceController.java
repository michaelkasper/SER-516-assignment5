package Compiler.Controller;

import Compiler.Model.SpaceModel;
import Compiler.Service.PropertyChangeDecorator;
import Compiler.View.Header;
import Compiler.View.Sidebar;
import Compiler.View.Spaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class WorkspaceController extends PropertyChangeDecorator {

    public static final String EVENT_SPACE_ADDED = "event_space_added";
    public static final String EVENT_ACTIVE_SPACE_CHANGED = "event_active_space_changed";

    private final JFrame theFrame;
    private final Header headerView;
    private final Spaces spacesView;
    public final Sidebar sidebarView;

    private final ArrayList<SpaceModel> spaces = new ArrayList<SpaceModel>();

    public WorkspaceController(JFrame theFrame) {
        this.theFrame = theFrame;

        this.headerView = new Header(this);
        this.sidebarView = new Sidebar(this);
        this.spacesView = new Spaces(this);

        this.theFrame.add(this.headerView, BorderLayout.PAGE_START);
        this.theFrame.add(this.sidebarView, BorderLayout.LINE_START);
        this.theFrame.add(this.spacesView, BorderLayout.CENTER);
        this.registerListeners();


        // Create default tab
        this.onAddSpace(new ActionEvent(this, 0, ""));
    }


    private void registerListeners() {
        this.headerView.setAddSpaceBtnListener(this::onAddSpace);
        this.headerView.setImportBtnListener(this::onImport);
        this.headerView.setSaveBtnListener(this::onSave);
        this.headerView.setCompileBtnListener(this::onCompile);
        this.spacesView.setTabSelectedListener(this::onTabSelected);
    }


    private void onAddSpace(ActionEvent e) {
        SpaceModel newSpace = new SpaceModel();
        this.spaces.add(newSpace);
        this.support.firePropertyChange(EVENT_SPACE_ADDED, null, newSpace);// add to tabs
        this.support.firePropertyChange(EVENT_ACTIVE_SPACE_CHANGED, null, this.spaces.size() - 1);// make active space
    }


    private void onSave(ActionEvent e) {
        // grab active space
    }


    private void onImport(ActionEvent e) {
        SpaceModel newSpace = new SpaceModel(); // need to pass in loaded info
        this.spaces.add(newSpace);

        this.support.firePropertyChange(EVENT_SPACE_ADDED, null, newSpace);// add to tabs
        this.support.firePropertyChange(EVENT_ACTIVE_SPACE_CHANGED, null, newSpace);// make active space
    }

    private void onCompile(ActionEvent e) {
        // grab active space
    }

    private void onTabSelected(ActionEvent e) {
        // need to get selected space id from e
        // this.support.firePropertyChange(PROPERTY_ACTIVE_SPACE, null, newSpace);// make active space
    }


}
