package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel {

    /**
     * @param workspaceController
     */
    public Header(WorkspaceController workspaceController) {
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(getWidth(), 50));

        this.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.white));
        this.setBackground(new Color(219, 225, 243));

        JButton addSpaceBtn = new JButton("New Space");
        JButton importSpaceBtn = new JButton("Load");
        JButton saveSpaceBtn = new JButton("Save");
        JButton compileBtn = new JButton("Compile");

        this.add(addSpaceBtn);
        this.add(importSpaceBtn);
        this.add(saveSpaceBtn);
        this.add(compileBtn);

        addSpaceBtn.addActionListener(workspaceController::onAddSpace);
        importSpaceBtn.addActionListener(workspaceController::onImport);
        saveSpaceBtn.addActionListener(workspaceController::onSave);
        compileBtn.addActionListener(workspaceController::onCompile);
    }
}
