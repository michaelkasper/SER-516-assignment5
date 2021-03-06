package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


public class Header extends JPanel {

    private final JButton addSpaceBtn;
    private final JButton importSpaceBtn;
    private final JButton saveSpaceBtn;
    private final JButton compileBtn;
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
        this.setBackground(Color.ORANGE);//TODO: set color

        this.addSpaceBtn = new JButton("add");
        this.importSpaceBtn = new JButton("import");
        this.saveSpaceBtn = new JButton("save");
        this.compileBtn = new JButton("compile");

        this.add(this.addSpaceBtn);
        this.add(this.importSpaceBtn);
        this.add(this.saveSpaceBtn);
        this.add(this.compileBtn);
    }


    /**
     * @param actionListener Event to run when addSpaceBtn is clicked
     */
    public void setAddSpaceBtnListener(ActionListener actionListener) {
        this.addSpaceBtn.addActionListener(actionListener);
    }


    /**
     * @param actionListener Event to run when importSpaceBtn is clicked
     */
    public void setImportBtnListener(ActionListener actionListener) {
        this.importSpaceBtn.addActionListener(actionListener);
    }

    /**
     * @param actionListener Event to run when saveSpaceBtn is clicked
     */
    public void setSaveBtnListener(ActionListener actionListener) {
        this.saveSpaceBtn.addActionListener(actionListener);
    }

    /**
     * @param actionListener Event to run when compileBtn is clicked
     */
    public void setCompileBtnListener(ActionListener actionListener) {
        this.compileBtn.addActionListener(actionListener);
    }

}
