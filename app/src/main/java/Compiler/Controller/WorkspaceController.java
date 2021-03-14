package Compiler.Controller;

import Compiler.Compiler;
import Compiler.Model.SpaceModel;
import Compiler.Model.ValidationError;
import Compiler.Service.PropertyChangeDecorator;

import java.awt.event.ActionEvent;
import java.util.ArrayList;


public class WorkspaceController extends PropertyChangeDecorator {

    public static final String EVENT_SPACE_ADDED = "event_space_added";
    private ArrayList<SpaceModel> spaces = new ArrayList<>();
    private Compiler frame;

    public WorkspaceController(Compiler frame) {
        this.frame = frame;
    }


    public void onAddSpace(ActionEvent e) {
        SpaceModel newSpace = new SpaceModel();
        this.spaces.add(newSpace);
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
        ArrayList<ValidationError> errors = new ArrayList<>();
        for (SpaceModel space : this.spaces) {
            errors.addAll(space.validate());
            space.support.firePropertyChange(SpaceModel.EVENT_UPDATE_ERRORS, null, true);
        }

        if (errors.size() == 0) {
            this.frame.showDialog("No errors found");
        } else if (errors.size() == 1) {
            this.frame.showDialog(errors.size() + " error found");
        } else {
            this.frame.showDialog(errors.size() + " errors found");
        }
    }

}
