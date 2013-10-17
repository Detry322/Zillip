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
	
	public GameTimer() {
		original = new BufferedImage(100,100, Transparency.BITMASK);
		overlay = new BufferedImage(100,100, Transparency.BITMASK);
	}

	public GameTimer(BufferedImage image, BufferedImage changeTo, int speedOfDecrease) {
		super(new ImageIcon(image));
		speed = speedOfDecrease;
		original = image;
		overlay = changeTo;
		bufimage = new BufferedImage(original.getWidth(null),original.getHeight(null),Transparency.BITMASK);
		percent = 0;
		updateImage();
		isGoing = true;
		(new Thread(this)).start();
	}

	@Override
	public void run() {
		while (true) {
			if (percent >= 100d) 
				break;
			if (isGoing()) {
				try {
					if (speed > 0)
						Thread.sleep(speed);
					else
						break;
				} catch (Exception e) {}
				percent += 0.1;
				updateImage();
			}
		}
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

	public void setPercent(double percent) {
		if (this.percent <= 100d) {
			this.percent = percent;
			updateImage();
		}
	}
	
}
