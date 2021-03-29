package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public class HeaderMenu extends JMenuBar {

    public HeaderMenu(WorkspaceController workspaceController) {
        this.setMinimumSize(new Dimension(getWidth(), 60));
        this.setPreferredSize(new Dimension(getWidth(), 60));
        this.setMaximumSize(new Dimension(getWidth(), 60));

        this.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.white));
        this.setBackground(new Color(219, 225, 243));
        this.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(0, 0, 4, 0), new MatteBorder(4, 4, 4, 4, new Color(219, 225, 243))));

        JMenuItem load = new HeaderMenuItem("Load");
        JMenuItem save = new HeaderMenuItem("Save");
        JMenuItem newSpace = new HeaderMenuItem("New Space");
        JMenuItem compile = new HeaderMenuItem("Compile");

        load.addActionListener(workspaceController::onImport);
        save.addActionListener(workspaceController::onSave);
        newSpace.addActionListener(workspaceController::onAddSpace);
        compile.addActionListener(workspaceController::onCompile);

        JMenuItem spacer = new JMenuItem(" ");
        spacer.setBackground(this.getBackground());
        spacer.setPreferredSize(new Dimension(420, 50));

        this.add(load);
        this.add(save);
        this.add(newSpace);
        this.add(compile);
        this.add(spacer);
    }


    private static class HeaderMenuItem extends JMenuItem {

        HeaderMenuItem(String label) {
            super(label);
            this.setBackground(new Color(231, 182, 131));
            this.setMinimumSize(new Dimension(200, 50));
            this.setPreferredSize(new Dimension(200, 50));
            this.setMaximumSize(new Dimension(200, 50));
            this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(1, 1, 1, 1, new Color(255, 255, 255)), new CompoundBorder(new LineBorder(Color.gray), new EmptyBorder(1, 1, 1, 1))));
        }

    }
}
