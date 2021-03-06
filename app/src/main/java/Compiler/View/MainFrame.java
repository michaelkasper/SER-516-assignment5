package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import java.awt.*;

import static Compiler.Config.FRAME_HEIGHT;
import static Compiler.Config.FRAME_WIDTH;

/**
 * Main view of the app. Initiates all controllers and links all view models
 */
public class MainFrame extends JFrame {
    private final WorkspaceController workspaceController;

    public MainFrame() {
        super("Compiler");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.workspaceController = new WorkspaceController(this);

        this.setVisible(true);
    }
}
