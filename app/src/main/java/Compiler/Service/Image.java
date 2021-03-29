package Compiler.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Image {
    public static BufferedImage createImage(JPanel panel) {
        int w = panel.getWidth();
        int h = panel.getHeight();
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.BITMASK);
        Graphics2D g = bi.createGraphics();
        panel.paint(g);
        g.dispose();
        return bi;
    }
}
