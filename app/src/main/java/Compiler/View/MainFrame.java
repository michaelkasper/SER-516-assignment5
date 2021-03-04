package Compiler.View;

import Compiler.Controller.WorkspaceController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static Compiler.Config.FRAME_HEIGHT;
import static Compiler.Config.FRAME_WIDTH;

/**
 * Main view of the app. Initiates all controllers and links all view models
 */
public class MainFrame extends JFrame {

    private CardLayout cardLayout;


    public MainFrame() {
        super("Compiler");
        this.cardLayout = new CardLayout();
        this.setLayout(cardLayout);
        this.setResizable(false);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        try {
            WorkspaceController workspaceController = new WorkspaceController(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

        this.setVisible(true);
    }
}
