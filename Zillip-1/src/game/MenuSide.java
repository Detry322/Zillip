package game;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MenuSide extends JPanel{
	
	BufferedImage border;
	public MenuSide(BufferedImage border) {
		super();
		setBorder(new EmptyBorder(16,16,16,16));
		this.border = border;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(border,0,0,this);
	}

}
