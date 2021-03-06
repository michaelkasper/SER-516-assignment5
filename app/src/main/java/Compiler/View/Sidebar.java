package Compiler.View;

import Compiler.Controller.WorkspaceController;
import Compiler.Model.Elements.*;
import Compiler.View.Components.Element;

import javax.swing.*;
import java.awt.*;


public class Sidebar extends JPanel {

    private final WorkspaceController workspaceController;

    public Sidebar(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.setPreferredSize(new Dimension(250, getHeight()));

        this.add(new Element(new OpenIfElement()));
        this.add(new Element(new CloseIfElement()));
        this.add(new Element(new MethodStartElement()));
        this.add(new Element(new MethodEndElement()));
        this.add(new Element(new LoopElement()));
        this.add(new Element(new ThreadElement()));
        this.add(new Element(new CommandElement()));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
