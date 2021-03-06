package Compiler.View;

import Compiler.Model.Elements.AbstractElement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Space extends JPanel implements Observer {

    /**
     * TODO: Make drop zone
     * TODO: Make arrayList of AbstractElements
     * TODO: Notify Space View of changes
     * TODO: Listen for connection changes form AbstractElements
     *
     */
    public Space() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o.getClass() == AbstractElement.class) {
            // Connection change from AbstractElements.
        }
        else {
            // New element dropped in space.
        }
    }
}
