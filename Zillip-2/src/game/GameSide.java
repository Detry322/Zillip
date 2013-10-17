package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Assets;
import logic.Game;

public class GameSide extends JPanel{
	
	BufferedImage border;
	Game game;
	
	public GameSide(BufferedImage border) {
		super();
		setBorder(new EmptyBorder(17,19,19,19));
		this.border = border;
		try {
		game = new Game(Assets.zillip);
		} catch (Exception e) {
			game = new Game();
		} finally {
			this.add(game);
		}
		
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(border,0,0,this);
	}

}
