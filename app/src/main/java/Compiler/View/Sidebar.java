package Compiler.View;

import Compiler.Controller.WorkspaceController;
import Compiler.Model.Elements.*;
import Compiler.View.Components.Element;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Arrays;

public class Sidebar extends JPanel {

    public Sidebar(WorkspaceController workspaceController) {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(new Dimension(250, getHeight()));

        this.setBackground(new Color(229, 225, 230));
        this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(10, 20, 20, 10, new Color(219, 225, 243)), new CompoundBorder(new LineBorder(Color.gray), new EmptyBorder(20, 20, 0, 20))));

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
}
