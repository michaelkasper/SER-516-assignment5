package Compiler.Controller;

import Compiler.View.Header;
import Compiler.View.Sidebar;
import Compiler.View.Space;
import Compiler.View.Spaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The Game board controller handles all logic for the game board view
 */
public class WorkspaceController {

    public static final String PROPERTY_SPACES = "spaces";
    public static final String PROPERTY_ACTIVE_SPACE = "active_space";
    /**
     * TODO: Add tabs controller that changes the active spaces Model
     * TODO: Add ArrayList of spaces Models
     * TODO: Add current active space view linked to the space model
     */

    private final JFrame theFrame;
    private final Header headerView;
    private final Spaces spacesView;
    private final Sidebar sidebarView;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    private ArrayList<Space> spaces = new ArrayList<Space>();


    public WorkspaceController(JFrame theFrame) throws IOException {
        this.theFrame = theFrame;


        this.headerView = new Header(this);
        this.sidebarView = new Sidebar(this);
        this.spacesView = new Spaces(this);

        this.theFrame.add(this.headerView, BorderLayout.PAGE_START);
        this.theFrame.add(this.sidebarView, BorderLayout.LINE_START);
        this.theFrame.add(this.spacesView, BorderLayout.CENTER);
    }


    private void registerListeners() {
        this.headerView.setAddSpaceBtnListener(this::onAddSpace);
        this.headerView.setImportBtnListener(this::onImport);
        this.headerView.setSaveBtnListener(this::onSave);
        this.headerView.setCompileBtnListener(this::onCompile);
        this.spacesView.setTabSelectedListener(this::onTabSelected);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        support.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        support.removePropertyChangeListener(property, listener);
    }


    private void onAddSpace(ActionEvent e) {
        Space newSpace = new Space();
        this.spaces.add(newSpace);

        this.support.firePropertyChange(PROPERTY_SPACES, null, newSpace);// add to tabs
        this.support.firePropertyChange(PROPERTY_ACTIVE_SPACE, null, newSpace);// make active space
    }


    private void onSave(ActionEvent e) {
        // grab active space
    }


    private void onImport(ActionEvent e) {
        Space newSpace = new Space(); // need to pass in loaded info
        this.spaces.add(newSpace);

        this.support.firePropertyChange(PROPERTY_SPACES, null, newSpace);// add to tabs
        this.support.firePropertyChange(PROPERTY_ACTIVE_SPACE, null, newSpace);// make active space
    }

    private void onCompile(ActionEvent e) {
        // grab active space
    }

    private void onTabSelected(ActionEvent e) {
        // need to get selected space id from e
        // this.support.firePropertyChange(PROPERTY_ACTIVE_SPACE, null, newSpace);// make active space
    }


}
