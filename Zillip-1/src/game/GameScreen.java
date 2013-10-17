package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GameScreen extends JPanel {
	
	final BufferedImage backgroundImage;
	
	public GameScreen(BufferedImage image) {
		super();
		backgroundImage = image;
		setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
	}
	
}
