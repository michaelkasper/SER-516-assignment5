package Compiler.View.Components;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class IoRepresentation extends JComponent {
	private final int value;
	private final int WIDTH = 15;
	private final int HEIGHT = 15;
	
	public IoRepresentation(int value) {
		this.value = value;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		int xAlignment = (this.getWidth() / 2) - (WIDTH/2);
		int midY = this.getHeight() / 2;
		
		switch(value) {
		case -1:
			//drawing a rectangle
			int height = HEIGHT * 4;
			drawRectangle(g, xAlignment, midY - (height/2), WIDTH, height);
			break;
		case 1:
			drawRectangle(g, xAlignment, midY - (HEIGHT/2), WIDTH, HEIGHT);
			break;
		case 2:			
			drawRectangle(g, xAlignment, midY - (HEIGHT/2) - HEIGHT, WIDTH, HEIGHT);
			drawRectangle(g, xAlignment, midY - (HEIGHT/2) + HEIGHT, WIDTH, HEIGHT);
			break;
		}
	}
	
	private void drawRectangle(Graphics g, int x, int y, int width, int height) {
		g.setColor(Color.decode("#707070"));
		g.fillRect(x, y, width, height);			
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		super.paintComponent(g);
	}
}
