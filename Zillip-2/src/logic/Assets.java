package logic;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Assets implements Runnable {

	public static BufferedImage digits;
	public static BufferedImage gameSide;
	public static BufferedImage hint;
	public static BufferedImage hintHover;
	public static BufferedImage hintPressed;
	public static BufferedImage menuSide;
	public static BufferedImage pause;
	public static BufferedImage pauseHover;
	public static BufferedImage pausePressed;
	public static BufferedImage play;
	public static BufferedImage playHover;
	public static BufferedImage playPressed;
	public static BufferedImage score;
	public static BufferedImage time;
	public static BufferedImage timeOverlay;
	public static BufferedImage zillip;
	public static final String fileBase = "http://localhost/zillip/";
	private static final int numberOfItems = 16;
	private static double percentDone;
	
	public void run() {
		Assets.loadImages();
	}
	
	public static void loadImages() {
		
		try {
			digits = ImageIO.read(new URL(fileBase + "Digits.png"));
			assetLoaded();
			gameSide = ImageIO.read(new URL(fileBase + "GameSide.png"));
			assetLoaded();
			hint = ImageIO.read(new URL(fileBase + "Hint.png"));
			assetLoaded();
			hintHover = ImageIO.read(new URL(fileBase + "HintHover.png"));
			assetLoaded();
			hintPressed = ImageIO.read(new URL(fileBase + "HintPressed.png"));
			assetLoaded();
			menuSide = ImageIO.read(new URL(fileBase + "MenuSide.png"));
			assetLoaded();
			pause = ImageIO.read(new URL(fileBase + "Pause.png"));
			assetLoaded();
			pauseHover = ImageIO.read(new URL(fileBase + "PauseHover.png"));
			assetLoaded();
			pausePressed = ImageIO.read(new URL(fileBase + "PausePressed.png"));
			assetLoaded();
			play = ImageIO.read(new URL(fileBase + "Play.png"));
			assetLoaded();
			playHover = ImageIO.read(new URL(fileBase + "PlayHover.png"));
			assetLoaded();
			playPressed = ImageIO.read(new URL(fileBase + "PlayPressed.png"));
			assetLoaded();
			score = ImageIO.read(new URL(fileBase + "Score.png"));
			assetLoaded();
			time = ImageIO.read(new URL(fileBase + "Time.png"));
			assetLoaded();
			timeOverlay = ImageIO.read(new URL(fileBase + "TimeOverlay.png"));
			assetLoaded();
			zillip = ImageIO.read(new URL(fileBase + "Zillip.png"));
			assetLoaded();
		} catch (MalformedURLException e) {
			//fileBase is wrong
		} catch (IOException e) {
			//couldn't connect
		} 
		
	}
	
	public static synchronized int getPercent() {
		return (int) percentDone;
	}
	
	public static synchronized void assetLoaded() {
		percentDone += 100.0/numberOfItems;
		System.out.println(percentDone + " percent loaded.");
	}
	
}
