package Compiler.Controller;

import Compiler.View.Header;
import Compiler.View.Sidebar;
import Compiler.View.Spaces;

import javax.swing.*;
import java.io.IOException;

/**
 * The Game board controller handles all logic for the game board view
 */
public class WorkspaceController {

    /**
     * TODO: Add tabs controller that changes the active spaces Model
     * TODO: Add ArrayList of spaces Models
     * TODO: Add current active space view linked to the space model
     */

    private final JFrame theFrame;
    private final Header headerView;
    private final Spaces spacesView;
    private final Sidebar sidebarView;


    public WorkspaceController(JFrame theFrame) throws IOException {
        this.theFrame = theFrame;

        this.headerView = new Header();
        this.sidebarView = new Sidebar();
        this.spacesView = new Spaces();

        /**
         * Dont think this is what we want, just spit balling right now
         */
        this.theFrame.add(this.headerView);
        this.theFrame.add(this.sidebarView);
        this.theFrame.add(this.spacesView);

        this.theFrame.setVisible(true);


        //todo: SAMPLE ---> this.headerView.registerSaveAction(e -> this.router.apply("explanation"));
    }

}
