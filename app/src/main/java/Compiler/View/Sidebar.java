package Compiler.View;

import Compiler.Controller.DragController;
import Compiler.Controller.WorkspaceController;
import Compiler.Model.Elements.*;
import Compiler.View.Components.Element;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class Sidebar extends JPanel {

    private final WorkspaceController workspaceController;

    public Sidebar(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));
        this.setPreferredSize(new Dimension(250, getHeight()));

        for (AbstractElement elementModel : Arrays.asList(
                new OpenIfElement(),
                new CloseIfElement(),
                new MethodStartElement(),
                new MethodEndElement(),
                new LoopElement(),
                new ThreadElement(),
                new CommandElement()
        )) {
            this.add(new Element(elementModel));
            this.add(Box.createRigidArea(new Dimension(getWidth(), 20)));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
