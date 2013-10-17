package logic;

import java.awt.Graphics;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GameScore extends JLabel{

	private BufferedImage[] raster = new BufferedImage[10];
	private int score;
	private int[] scoreDigits = new int[5];
	private int width;
	private BufferedImage bufimage;
	private BufferedImage background;
			
	public void updateImage() {
		Graphics g = bufimage.createGraphics();
		g.drawImage(background, 0, 0, this);
		int initx = (bufimage.getWidth() - 5*width)/2;
		int inity = (bufimage.getHeight() - raster[1].getHeight())/2;
		for (int i = 0;i<5;i++) {
			g.drawImage(raster[scoreDigits[i]], initx + i*width, inity, this);
		}
		g.dispose();
		setIcon(new ImageIcon(bufimage));
	}
		
	
	public GameScore(BufferedImage rasterSource, BufferedImage background, int initialScore) {
		super(new ImageIcon(background));
		int width = rasterSource.getWidth()/10;
		this.width = width;
		int height = rasterSource.getHeight();
		for (int i = 0;i<10;i++) {
			raster[i] = rasterSource.getSubimage(width*i, 0, width, height);
		}
		this.score = initialScore;
		String x = "" + score;
		while (true) {
			if (x.length()>=5) {
				break;
			}
			x = "0" + x;
		}
		for (int i = 0;i<5;i++) {
			scoreDigits[i] = Integer.parseInt(x.substring(i,i+1));
		}
		this.background = background;
		bufimage = new BufferedImage(background.getWidth(),background.getHeight(),Transparency.BITMASK);
		updateImage();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		if (score < 0) {
			score = 0;
		}
		this.score = score;
		String x = "" + score;
		while (true) {
			if (x.length()>=5) {
				break;
			}
			x = "0" + x;
		}
		for (int i = 0;i<5;i++) {
			scoreDigits[i] = Integer.parseInt(x.substring(i,i+1));
		}
		updateImage();
	}
	
}
