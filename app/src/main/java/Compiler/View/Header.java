package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static Compiler.Config.BLUE_BG_COLOR;


public class Header extends JPanel {

    private final JButton addSpaceBtn;
    private final WorkspaceController workspaceController;

    /**
     * TODO: Added buttons here
     * TODO: Create event listener for each button
     *
     * @param workspaceController
     */
    public Header(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
        this.setLayout(new FlowLayout());
        this.setPreferredSize(new Dimension(getWidth(), 50));

        this.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.white));
        this.setBackground(BLUE_BG_COLOR);
        
        this.addSpaceBtn = new JButton("New Space");
        this.add(this.addSpaceBtn);
    }


    /**
     * @param actionListener Event to run when addSpaceBtn is clicked
     */
    public void setAddSpaceBtnListener(ActionListener actionListener) {
        this.addSpaceBtn.addActionListener(actionListener);
    }


}
