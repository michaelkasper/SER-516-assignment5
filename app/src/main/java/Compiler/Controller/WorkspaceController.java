package Compiler.Controller;

import Compiler.View.Workspace;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;

/**
 * The Game board controller handles all logic for the game board view
 */
public class WorkspaceController extends AbstractController {


    private final Workspace workspaceView;


    public WorkspaceController() throws IOException {
        this.workspaceView = new Workspace();

        this.linkEventsToView();
    }


    private void linkEventsToView() {
        //todo: SAMPLE ---> this.workspaceView.explanationBtnAction(e -> this.router.apply("explanation"));


        this.workspaceView.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent evt) {

            }

            @Override
            public void componentShown(ComponentEvent evt) {

            }
        });
    }

    public JPanel getView() {
        return this.workspaceView;
    }
}
