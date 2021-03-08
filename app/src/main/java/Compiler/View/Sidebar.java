package Compiler.View;

import Compiler.Controller.WorkspaceController;
import Compiler.Model.Elements.*;
import Compiler.View.Components.Element;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Arrays;


public class Sidebar extends JPanel {

    private final WorkspaceController workspaceController;

    public Sidebar(WorkspaceController workspaceController) {
        this.workspaceController = workspaceController;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(new Dimension(250, getHeight()));

        this.setBackground(new Color(219, 225, 243));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 10));

        JPanel innerBox = new JPanel();
        innerBox.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.gray), new EmptyBorder(20, 20, 0, 20)));
        innerBox.setLayout(new BoxLayout(innerBox, BoxLayout.PAGE_AXIS));

        for (AbstractElement elementModel : Arrays.asList(
                new OpenIfElement(),
                new CloseIfElement(),
                new MethodStartElement(),
                new MethodEndElement(),
                new LoopElement(),
                new ThreadElement(),
                new CommandElement()
        )) {
            innerBox.add(new Element(elementModel));
            innerBox.add(Box.createRigidArea(new Dimension(getWidth(), 20)));
        }

        this.add(innerBox);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
