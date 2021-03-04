package Compiler;

import Compiler.View.MainFrame;

import javax.swing.*;

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
public class Compiler {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
