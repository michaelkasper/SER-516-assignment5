package Compiler.View;

import Compiler.Controller.SpaceController;
import Compiler.Model.SpaceModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Space extends JPanel {


    private final SpaceController spaceController;

    /**
     * TODO: Make drop zone
     * TODO: Make arrayList of AbstractElements
     * TODO: Notify Space View of changes
     * TODO: Listen for connection changes form AbstractElements
     */
    public Space(SpaceModel spaceModel) {
        this.spaceController = new SpaceController(this, spaceModel);
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.registerListeners();
    }


    private void registerListeners() {
        // listen for changes on controller only
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void setDropListener(ActionListener actionListener) {
        //todo: link actionListener to drop zone view
    }
}
