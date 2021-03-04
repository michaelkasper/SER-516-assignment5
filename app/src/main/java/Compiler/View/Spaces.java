package Compiler.View;

import javax.swing.*;
import java.awt.*;

public class Spaces extends JPanel {

    /**
     * TODO: Render tabs
     * TODO: Render Active Space Panel
     */
    public Spaces() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
}
