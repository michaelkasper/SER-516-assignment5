package Compiler.Controller;

import Compiler.Model.SpaceModel;
import Compiler.Service.PropertyChangeDecorator;

import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class WorkspaceController extends PropertyChangeDecorator {

    public static final String EVENT_SPACE_ADDED = "event_space_added";
    private ArrayList<SpaceModel> spaces = new ArrayList<>();

    public WorkspaceController() {
    }


    public void onAddSpace(ActionEvent e) {
        SpaceModel newSpace = new SpaceModel();
        this.support.firePropertyChange(EVENT_SPACE_ADDED, null, newSpace);// add to tabs
    }


    public void onSave(ActionEvent e) {
        // grab active space
    }


    public void onImport(ActionEvent e) {
        SpaceModel newSpace = new SpaceModel(); // need to pass in loaded info
        this.spaces.add(newSpace);

        this.support.firePropertyChange(EVENT_SPACE_ADDED, null, newSpace);// add to tabs
    }

    public void onCompile(ActionEvent e) {
        // grab active space
    }

}
