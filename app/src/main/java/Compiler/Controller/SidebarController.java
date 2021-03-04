package Compiler.Controller;

import Compiler.View.Sidebar;

import javax.swing.*;
import java.io.IOException;

/**
 * The Explanation Controller handles all logic for the Explanation View
 */
public class SidebarController extends AbstractController {
    private final Sidebar explanationView;

    public SidebarController() throws IOException {
        this.explanationView = new Sidebar();
    }

    @Override
    public JPanel getView() {
        return this.explanationView;
    }
}
