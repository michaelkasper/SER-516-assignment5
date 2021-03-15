package Compiler.Controller;

import Compiler.Compiler;
import Compiler.Model.SpaceModel;
import Compiler.Model.ValidationError;
import Compiler.Service.ImportExport;
import Compiler.Service.PropertyChangeDecorator;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;


public class WorkspaceController extends PropertyChangeDecorator {
    private static FileWriter file;

    public static final String EVENT_SPACE_ADDED = "event_space_added";
    public static final String EVENT_CLEAR_SPACES = "event_clear_spaces";

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


    public void onSave(ActionEvent event) {
        File saveLocation = this.frame.showSaveDialog();

        if (saveLocation != null) {
            if (ImportExport.saveTo(this.spaces, saveLocation)) {
                this.frame.showDialog("Saved!");
            } else {
                this.frame.showDialog("Save Unsuccessful");
            }
        }
    }


    public void onImport(ActionEvent event) {
        File fileLocation = this.frame.showOpenFileDialog();

        if (fileLocation != null) {
            ArrayList<SpaceModel> newSpaces = ImportExport.loadFrom(fileLocation);

            if (newSpaces != null) {
                this.spaces.clear();
                this.support.firePropertyChange(EVENT_CLEAR_SPACES, null, true);// add to tabs

                for (SpaceModel space : newSpaces) {
                    this.spaces.add(space);
                    this.support.firePropertyChange(EVENT_SPACE_ADDED, null, space);// add to tabs
                }
                return;
            }
        }

        this.frame.showDialog("Import Unsuccessful");
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
