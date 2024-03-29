package Compiler.Controller;

import Compiler.Compiler;
import Compiler.Model.SpaceModel;
import Compiler.Service.CodeGenerator;
import Compiler.Service.ImportExport;
import Compiler.Service.Store;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class WorkspaceController {

    public static final String EVENT_SPACE_ADDED = "event_space_added";
    public static final String EVENT_CLEAR_SPACES = "event_clear_spaces";
    public static final String EVENT_SELECT_TAB = "event_select_tab";

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
            HashMap<String, SpaceModel> newSpaces = ImportExport.loadFrom(fileLocation);

            if (newSpaces != null) {
                for (SpaceModel space : oldSpaces) {
                    if (!newSpaces.containsKey(space.getId())) {
                        Store.getInstance().remove(space);
                    }
                }

                List<SpaceModel> spaces = new ArrayList<>(newSpaces.values());
                spaces.sort(Comparator.comparingInt(SpaceModel::getIndex));
                int selectedTab = spaces.stream().filter(SpaceModel::isTabSelected).findFirst().map(SpaceModel::getIndex).orElse(0);

                this.getChangeSupport().firePropertyChange(EVENT_CLEAR_SPACES, null, true);
                for (SpaceModel space : spaces) {
                    this.getChangeSupport().firePropertyChange(EVENT_SPACE_ADDED, null, space);
                }

                this.getChangeSupport().firePropertyChange(EVENT_SELECT_TAB, null, selectedTab);
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
        }

        if (errors.size() == 0) {
            JOptionPane.showMessageDialog(this.frame, "No errors found", "Compile", JOptionPane.PLAIN_MESSAGE);

            try {
                // create a JTextArea
                JTextArea textArea = new JTextArea(50, 40);
                textArea.setText(CodeGenerator.Generate());
                textArea.setEditable(false);

                // wrap a scrollpane around it
                JScrollPane scrollPane = new JScrollPane(textArea);

                // display them in a message dialog
                JOptionPane.showMessageDialog(this.frame, scrollPane, "Generated Code", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }


        } else if (errors.size() == 1) {
            JOptionPane.showMessageDialog(this.frame, errors.size() + " errors found", "Compile", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this.frame, errors.size() + " errors found", "Compile", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void onTabSelected(ChangeEvent e) {
        if (e.getSource() instanceof JTabbedPane) {
            JTabbedPane pane = (JTabbedPane) e.getSource();
            int selectedIndex = pane.getSelectedIndex();

            for (SpaceModel space : Store.getInstance().getAllSpaces()) {
                space.setTabSelected(selectedIndex == space.getIndex());
            }

        }
    }

}
