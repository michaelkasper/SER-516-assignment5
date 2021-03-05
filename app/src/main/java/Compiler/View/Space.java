package Compiler.View;

import javax.swing.*;
import java.awt.*;

public class Space extends JPanel {

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
}
