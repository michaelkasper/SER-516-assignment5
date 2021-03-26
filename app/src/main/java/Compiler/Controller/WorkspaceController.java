package Compiler.Controller;

import Compiler.Compiler;
import Compiler.Model.SpaceModel;
import Compiler.Service.ImportExport;
import Compiler.Service.Store;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;

import static Compiler.Model.SpaceModel.EVENT_ELEMENT_ADDED;
import static Compiler.Model.SpaceModel.EVENT_UPDATE_ERRORS;


public class WorkspaceController {

    public static final String EVENT_SPACE_ADDED = "event_space_added";
    public static final String EVENT_CLEAR_SPACES = "event_clear_spaces";
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final Compiler frame;

    public WorkspaceController(Compiler frame) {
        this.frame = frame;
    }

    public PropertyChangeSupport getChangeSupport() {
        return propertyChangeSupport;
    }

    public void onAddSpace(ActionEvent e) {
        this.getChangeSupport().firePropertyChange(EVENT_SPACE_ADDED, null, new SpaceModel());
    }


    public void onSave(ActionEvent e) {
        File saveLocation = this.frame.showSaveDialog();

        if (saveLocation != null) {
            if (ImportExport.saveTo(saveLocation)) {
                this.frame.showDialog("Saved!");
            } else {
                this.frame.showDialog("Save Unsuccessful");
            }
        }
    }


    public void onImport(ActionEvent e) {
        JFileChooser fileChooser = this.frame.showOpenFileDialog();
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileLocation = fileChooser.getSelectedFile();

            ArrayList<SpaceModel> oldSpaces = Store.getInstance().getAllSpaces();
            ArrayList<SpaceModel> newSpaces = ImportExport.loadFrom(fileLocation);

            if (newSpaces != null) {
                for (SpaceModel space : oldSpaces) {
                    Store.getInstance().remove(space);
                }

                this.getChangeSupport().firePropertyChange(EVENT_CLEAR_SPACES, null, true);
                for (SpaceModel space : newSpaces) {
                    this.getChangeSupport().firePropertyChange(EVENT_SPACE_ADDED, null, space);
                    space.getChangeSupport().firePropertyChange(EVENT_ELEMENT_ADDED, null, true);
                    space.getChangeSupport().firePropertyChange(EVENT_UPDATE_ERRORS, null, true);
                }
                return;
            }

            this.frame.showDialog("Import Unsuccessful");
        }
    }

    public void onCompile(ActionEvent e) {
        // grab active space
        ArrayList<String> errors = new ArrayList<>();
        for (SpaceModel space : Store.getInstance().getAllSpaces()) {
            errors.addAll(space.validate());
            space.getChangeSupport().firePropertyChange(EVENT_UPDATE_ERRORS, null, true);
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
