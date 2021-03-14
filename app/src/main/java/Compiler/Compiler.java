package Compiler;

import Compiler.Controller.WorkspaceController;
import Compiler.View.Header;
import Compiler.View.Sidebar;
import Compiler.View.Spaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static Compiler.Config.FRAME_HEIGHT;
import static Compiler.Config.FRAME_WIDTH;

/**
 * Compiler is the main entity to the app.
 * <p>
 *
 * @author Ankit Vutukuri (avutuku1@asu.edu)
 * @author Disha Suresh (dsuresh5@asu.edu)
 * @author Dragan Bogoevski (dbogoevs@asu.edu)
 * @author Kamal Penmetcha (kpenmetc@asu.edu)
 * @author Michael Kasper (mkasper@asu.edu)
 * @version 1.0
 */
public class Compiler extends JFrame {

    public Compiler() {
        super("Compiler");
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        WorkspaceController workspaceController = new WorkspaceController(this);

        Header headerView = new Header(workspaceController);
        Sidebar sidebarView = new Sidebar(workspaceController);
        Spaces spacesView = new Spaces(workspaceController);

        this.add(headerView, BorderLayout.PAGE_START);
        this.add(sidebarView, BorderLayout.LINE_START);
        this.add(spacesView, BorderLayout.CENTER);

        this.setVisible(true);

        workspaceController.onAddSpace(new ActionEvent(this, 0, ""));
    }


    public void showDialog(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Compiler::new);
    }
}
