package game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import logic.Assets;
import logic.GameButton;
import logic.GameScore;
import logic.GameTimer;

public class MenuSide extends JPanel{
	
	 BufferedImage border;
	 GameButton hint;
	 GameButton pause;
	 GameTimer timer;
	 GameScore score;
	
	public MenuSide(BufferedImage border) {
		super();
		setBorder(new EmptyBorder(16,16,16,16));
		this.border = border;
		try {
		score = new GameScore(Assets.digits,Assets.score, 98765);
		timer = new GameTimer(Assets.time, Assets.timeOverlay, 10);
		Runnable startstop = new Runnable() {
			  public void run() {
				  boolean x = timer.isGoing();
				  timer.setGoing(!x);
			  }
		};
		pause = new GameButton(Assets.pause,Assets.pauseHover,Assets.pausePressed,startstop);
		Runnable runnable = new Runnable() {
			  public void run() {
				  timer.setPercent(timer.getPercent() + 3);
			  }
		};
		hint = new GameButton(Assets.hint,Assets.hintHover,Assets.hintPressed,runnable);
		} catch (Exception e) {
			score = new GameScore();
			timer = new GameTimer();
			pause = new GameButton();
			hint = new GameButton();
		} finally {
			this.add(score);
			this.add(hint);
			this.add(timer);
			this.add(pause);
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(border,0,0,this);
	}

}
