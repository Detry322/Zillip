package logic;

import game.GameContainer;
import game.GameScreen;
import game.GameSide;
import game.MenuSide;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Assets implements Runnable {

	public static BufferedImage digits;
	public static BufferedImage gameSideImage;
	public static BufferedImage hint;
	public static BufferedImage hintHover;
	public static BufferedImage hintPressed;
	public static BufferedImage menuSideImage;
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
	public static BufferedImage zillipPopped;
	public static BufferedImage arrow;
	public static BufferedImage mainBack;
	public static BufferedImage zillipLogo;
	public static BufferedImage soundOff;
	public static BufferedImage soundOn;
	public static BufferedImage soundOffHover;
	public static BufferedImage soundOffPressed;
	public static BufferedImage soundOnHover;
	public static BufferedImage soundOnPressed;
	public static BufferedImage resume;
	public static BufferedImage resumeHover;
	public static BufferedImage resumePressed;
	public static byte[] bigExplode;
	public static byte[] mediumExplode;
	public static byte[] smallExplode;
	public static byte[] sound;
	public static byte[] mainMusic;
	public static byte[] timeRunningOut;
	public static byte[] hintUsed;
	
	public static Game game;
	public static GameContainer gameContainer;
	public static GameScreen pauseScreen;
	public static GameScreen mainScreen;
	public static GameScreen loseScreen;
	public static GameScore displayScore;
	public static GameButton playButton;
	public static GameButton resumeButton;
	public static ToggleableGameButton soundButton = null;
	public static GameButton pauseButton;
	public static GameButton hintButton;
	public static GameTimer gameTimer;
	public static MenuSide menuSide;
	public static GameSide gameSide;
	
	public static void destroy() {
		soundOff = null;
		soundOn = null;
		soundOffHover = null;
		soundOffPressed = null;
		soundOnHover = null;
		soundOnPressed = null;
		digits = null;
		gameSideImage = null;
		hint = null;
		hintHover = null;
		hintPressed = null;
		menuSideImage = null;
		pause = null;
		pauseHover = null;
		pausePressed = null;
		play = null;
		playHover = null;
		playPressed = null;
		score = null;
		time = null;
		timeOverlay = null;
		zillip = null;
		zillipPopped = null;
		arrow = null;
		mainBack = null;
		zillipLogo = null;
		bigExplode = null;
		mediumExplode = null;
		smallExplode = null;
		soundOn = null;
		mainMusic = null;
		timeRunningOut = null;
		hintUsed = null;
		game = null;
		gameContainer = null;
		pauseScreen = null;
		mainScreen = null;
		playButton = null;
		pauseButton = null;
		hintButton = null;
		gameTimer = null;
		menuSide = null;
		gameSide = null;
		resume = null;
		resumeHover = null;
		resumePressed = null;
	}
	
	private static boolean gameIsPaused = false;
	
	private static boolean soundIsOn = true;
	
	public static final String fileBase = "http://localhost/zillip/";

	private static final int numberOfItems = 36;
	private static double percentDone;
	
	public void run() {
		Assets.loadImages();
		Assets.loadSounds();
	}
	
	public static byte[] loadSound(String area) {
		ByteArrayOutputStream returner = new ByteArrayOutputStream();
		InputStream is;
		try {
			URL url = new URL(fileBase + area);
			is = url.openStream ();
			byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			int n;
			while ( (n = is.read(byteChunk)) > 0 ) {
				returner.write(byteChunk, 0, n);
			}
			is.close();
		} catch (MalformedURLException e) {
			System.out.println("Urls wrong");
		} catch (IOException e) {
			System.out.println("IO Exception");
		} catch (Exception e) {
			System.out.println(e.getClass().getName());
		}

		return returner.toByteArray();
	}
	
	public static void loadSounds() {
		bigExplode = loadSound("sound/big.wav");
		assetLoaded();
		mediumExplode = loadSound("sound/medium.wav");
		assetLoaded();
		smallExplode = loadSound("sound/small.wav");
		assetLoaded();
		hintUsed = loadSound("sound/hint.wav");
		assetLoaded();
		mainMusic = loadSound("sound/main.wav");
		assetLoaded();
		timeRunningOut = loadSound("sound/time.wav");
		assetLoaded();
		sound = loadSound("sound/sound.wav");
		assetLoaded();
	}
	
	public static void loadImages() {
		
		try {
			digits = ImageIO.read(new URL(fileBase + "image/Digits.png"));
			assetLoaded();
			gameSideImage = ImageIO.read(new URL(fileBase + "image/GameSide.png"));
			assetLoaded();
			zillipLogo = ImageIO.read(new URL(fileBase + "image/ZillipLogo.png"));
			assetLoaded();
			mainBack = ImageIO.read(new URL(fileBase + "image/Main.png"));
			assetLoaded();
			hint = ImageIO.read(new URL(fileBase + "image/Hint.png"));
			assetLoaded();
			hintHover = ImageIO.read(new URL(fileBase + "image/HintHover.png"));
			assetLoaded();
			hintPressed = ImageIO.read(new URL(fileBase + "image/HintPressed.png"));
			assetLoaded();
			menuSideImage = ImageIO.read(new URL(fileBase + "image/MenuSide.png"));
			assetLoaded();
			pause = ImageIO.read(new URL(fileBase + "image/Pause.png"));
			assetLoaded();
			pauseHover = ImageIO.read(new URL(fileBase + "image/PauseHover.png"));
			assetLoaded();
			pausePressed = ImageIO.read(new URL(fileBase + "image/PausePressed.png"));
			assetLoaded();
			play = ImageIO.read(new URL(fileBase + "image/Play.png"));
			assetLoaded();
			playHover = ImageIO.read(new URL(fileBase + "image/PlayHover.png"));
			assetLoaded();
			playPressed = ImageIO.read(new URL(fileBase + "image/PlayPressed.png"));
			assetLoaded();
			score = ImageIO.read(new URL(fileBase + "image/Score.png"));
			assetLoaded();
			time = ImageIO.read(new URL(fileBase + "image/Time.png"));
			assetLoaded();
			timeOverlay = ImageIO.read(new URL(fileBase + "image/TimeOverlay.png"));
			assetLoaded();
			zillip = ImageIO.read(new URL(fileBase + "image/Zillip.png"));
			assetLoaded();
			zillipPopped = ImageIO.read(new URL(fileBase + "image/ZillipPopped.png"));
			assetLoaded();
			arrow = ImageIO.read(new URL(fileBase + "image/Arrow.png"));
			assetLoaded();
			soundOff = ImageIO.read(new URL(fileBase + "image/SoundOff.png"));
			assetLoaded();
			soundOn = ImageIO.read(new URL(fileBase + "image/SoundOn.png"));
			assetLoaded();
			soundOffHover = ImageIO.read(new URL(fileBase + "image/SoundOffHover.png"));
			assetLoaded();
			soundOffPressed = ImageIO.read(new URL(fileBase + "image/SoundOffPressed.png"));
			assetLoaded();
			soundOnHover = ImageIO.read(new URL(fileBase + "image/SoundOnHover.png"));
			assetLoaded();
			soundOnPressed = ImageIO.read(new URL(fileBase + "image/SoundOnPressed.png"));
			assetLoaded();
			resume = ImageIO.read(new URL(fileBase + "image/Resume.png"));
			assetLoaded();
			resumeHover = ImageIO.read(new URL(fileBase + "image/ResumeHover.png"));
			assetLoaded();
			resumePressed = ImageIO.read(new URL(fileBase + "image/ResumePressed.png"));
			assetLoaded();
		} catch (MalformedURLException e) {
			//fileBase is wrong
		} catch (IOException e) {
			//couldn't connect
		} 
		
	}
	

	public static synchronized boolean soundOn() {
		return soundIsOn;
	}

	public static synchronized void sound(boolean sound) {
		soundIsOn = sound;
	}
	
	public static synchronized int getPercent() {
		return (int) percentDone;
	}
	
	public synchronized static void setPause(boolean pause) {
		gameIsPaused = pause;
	}
	
	public synchronized static boolean isPaused() {
		return gameIsPaused;
	}
	
	public static synchronized void assetLoaded() {
		percentDone += 100.0/numberOfItems;
		System.out.println(percentDone + " percent loaded.");
		try {
			gameContainer.alert();
		} catch (Exception e) {
			System.out.println("GameContainer not yet loaded");
		}
	}
	
}
