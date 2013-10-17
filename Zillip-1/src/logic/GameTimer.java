package logic;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameTimer extends JLabel implements Runnable {
	
	private final Image original;
	private final Image overlay;
	private BufferedImage bufimage;
	private int speed;
	int i = 0;
	private int initialDelay;
	private double percent;
	private boolean isGoing;
	
	private void updateImage() {
		Graphics2D g = bufimage.createGraphics();
		double tempPer = (percent)/100.0;
		g.drawImage(original, 0, (int) (bufimage.getHeight()*tempPer), bufimage.getWidth(), bufimage.getHeight(), 0, (int) (bufimage.getHeight()*tempPer), original.getWidth(null), original.getHeight(null), this);
		g.drawImage(overlay, 0, 0, bufimage.getWidth(), (int) (bufimage.getHeight()*percent/100.0), 0, 0, bufimage.getWidth(null), (int) (bufimage.getHeight(null)*percent/100.0), this);
		g.dispose();
		setIcon(new ImageIcon(bufimage));
	}

	public GameTimer(BufferedImage image, BufferedImage changeTo, int speedOfDecrease, boolean started) {
		super(new ImageIcon(image));
		speed = speedOfDecrease;
		original = image;
		overlay = changeTo;
		bufimage = new BufferedImage(original.getWidth(null),original.getHeight(null),Transparency.BITMASK);
		percent = 0;
		updateImage();
		isGoing = started;
		(new Thread(this)).start();
	}

	@Override
	public void run() {
		try {Thread.sleep(initialDelay);}catch(Exception e){};
		while (true) {
			if (percent >= 100d) {
				timeIsUp();
				break;
			}
			if (isGoing()) {
				try {
					if (speed > 0)
						Thread.sleep(speed);
					else
						break;
				} catch (Exception e) {}
				Assets.displayScore.setScore(Assets.displayScore.getScore()+1);
				percent += 1;
				updateImage();
				if (percent > 75) {
					i++;
					i %= 4;
					if (i==0) new SoundPlayer(Assets.timeRunningOut,false);
				}
			}
		}
		Assets.gameContainer.createLose();
		Assets.game.mainTheme.stopPlaying();
		Assets.game.setLock(true);
	}

	public synchronized boolean isGoing() {
		return isGoing;
	}

	public synchronized void setGoing(boolean isGoing) {
		this.isGoing = isGoing;
	}

	public double getPercent() {
		return percent;
	}
	
	public void timeIsUp() {
		
	}

	public void setPercent(double percent) {
		if (this.percent <= 100d && percent >= 0d) {
			this.percent = percent;
			updateImage();
		}
	}
	
}
