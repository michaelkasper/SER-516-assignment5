package Compiler.View;

import Compiler.Controller.ElementController;
import Compiler.Model.Elements.*;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.util.Arrays;

public class Sidebar extends JPanel {

    public Sidebar() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(new Dimension(250, getHeight()));

        this.setBackground(new Color(229, 225, 230));
        this.setBorder(BorderFactory.createCompoundBorder(new MatteBorder(10, 20, 20, 10, new Color(219, 225, 243)), new CompoundBorder(new LineBorder(Color.gray), new EmptyBorder(20, 20, 0, 20))));

        for (AbstractElement elementModel : Arrays.asList(
                new IfEndElement(),
                new IfStartElement(),
                new LoopElement(),
                new ThreadOpenElement(),
                new ThreadCloseElement(),
                new CommandElement()
        )) {
            this.add(new Element(new ElementController(elementModel)));
            this.add(Box.createRigidArea(new Dimension(getWidth(), 20)));
        }
    }
}
