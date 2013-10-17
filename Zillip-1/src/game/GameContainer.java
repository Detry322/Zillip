package game;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logic.Assets;
import logic.GameButton;
import logic.GameScore;
import logic.GameTimer;
import logic.ToggleableGameButton;

public class GameContainer extends JApplet {
	
	public JLabel loadingLabel;
	
	public GameContainer() {
		Assets.gameContainer = this;
	}
	
	public void createLoading() {
		loadingLabel = new JLabel("Loading Assets...");
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		getContentPane().add(loadingLabel, BorderLayout.CENTER);
		validate();
	}
	
	public void createGUI() {
		Container container = new Container();
		container.setLayout(new BorderLayout());
		Assets.mainScreen = new GameScreen(Assets.mainBack);
		Assets.mainScreen.setSize(640,480);
		Container y = new Container();
		y.setLayout(new BoxLayout(y,BoxLayout.Y_AXIS));
		Assets.mainScreen.setLayout(new BorderLayout());
		JLabel zillipLogo = new JLabel(new ImageIcon(Assets.zillipLogo));
		y.add(Box.createVerticalStrut(64));
		y.add(zillipLogo);
		y.add(Box.createVerticalStrut(48));
		Runnable run = new Runnable() {public void run(){
			createGame();
		}};
		Assets.playButton = new GameButton(Assets.play,Assets.playHover,Assets.playPressed,run);
		y.add(Assets.playButton);
		Assets.mainScreen.add(Box.createHorizontalStrut(192),BorderLayout.WEST);
		Assets.mainScreen.add(y,BorderLayout.CENTER);
		container.add(Assets.mainScreen, BorderLayout.CENTER);
		setContentPane(container);
		validate();
	}
	
	public void createGame() {
		Container c = new Container();
		c.setLayout(new BorderLayout());
		Assets.menuSide = new MenuSide(Assets.menuSideImage);
		Assets.menuSide.setLayout(new BoxLayout(Assets.menuSide,BoxLayout.Y_AXIS));
		Assets.displayScore = new GameScore(Assets.digits,Assets.score,0000);
		Runnable hintRunnable = new Runnable() {
			public void run() {
				Assets.displayScore.setScore(Assets.displayScore.getScore()-30);
				Assets.gameTimer.setPercent(Assets.gameTimer.getPercent()+10);
				Assets.game.hint();
			}
		};
		Assets.hintButton = new GameButton(Assets.hint,Assets.hintHover,Assets.hintPressed,hintRunnable);
		Assets.gameTimer = new GameTimer(Assets.time,Assets.timeOverlay,200,false);
		Runnable pauseRunnable = new Runnable() {
			public void run() {
				Assets.game.pause();
				Assets.gameTimer.setGoing(false);
				createPause();
			}
		};
		Assets.pauseButton = new GameButton(Assets.pause,Assets.pauseHover,Assets.pausePressed,pauseRunnable);
		Assets.menuSide.add(Assets.displayScore);
		Assets.menuSide.add(Assets.hintButton);
		Assets.menuSide.add(Assets.gameTimer);
		Assets.menuSide.add(Assets.pauseButton);
		Assets.gameSide = new GameSide(Assets.gameSideImage);
		c.setLayout(new BoxLayout(c,BoxLayout.X_AXIS));
		c.add(Assets.menuSide);
		c.add(Assets.gameSide);
		Assets.gameTimer.setGoing(true);
		Assets.game.play();
		repaint();
		Assets.menuSide.repaint();
		Assets.gameSide.repaint();
		setContentPane(c);
		validate();
	}
	
	public void resumeGame() {
		Container c = new JPanel();
		c.setLayout(new BoxLayout(c,BoxLayout.X_AXIS));
		c.add(Assets.menuSide);
		c.add(Assets.gameSide);
		Assets.game.resume();
		Assets.gameTimer.setGoing(true);
		setContentPane(c);
		validate();
	}
	
	public void createPause() {
		Container c = new JPanel();
		Assets.pauseScreen = new GameScreen(Assets.mainBack);
		Assets.pauseScreen.setLayout(new BoxLayout(Assets.pauseScreen,BoxLayout.Y_AXIS));
		Assets.pauseScreen.add(Box.createVerticalStrut(64));
		JLabel zillipLogo = new JLabel(new ImageIcon(Assets.zillipLogo));
		Assets.pauseScreen.add(zillipLogo);
		zillipLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		Assets.pauseScreen.add(Box.createVerticalStrut(16));
		if (Assets.soundButton == null) {
			Assets.soundButton = new ToggleableGameButton(Assets.soundOn,Assets.soundOnHover,Assets.soundOnPressed,Assets.soundOff,Assets.soundOffHover,Assets.soundOffPressed);
		}
		Assets.soundButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		Assets.pauseScreen.add(Assets.soundButton);
		Assets.pauseScreen.add(Box.createVerticalStrut(16));
		Runnable resume = new Runnable() {
			public void run() {
				resumeGame();
			}
		};
		Assets.resumeButton = new GameButton(Assets.resume,Assets.resumeHover,Assets.resumePressed, resume);
		Assets.resumeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		Assets.pauseScreen.add(Assets.resumeButton);
		c.add(Assets.pauseScreen,BorderLayout.NORTH);
		setContentPane(c);
		validate();
	}
	
	public void createLose() {
		Container c = new JPanel();
		Assets.loseScreen = new GameScreen(Assets.mainBack);
		Assets.loseScreen.setLayout(new BoxLayout(Assets.loseScreen,BoxLayout.Y_AXIS));
		Assets.loseScreen.add(Box.createVerticalStrut(32));
		JLabel zillipLogo = new JLabel(new ImageIcon(Assets.zillipLogo));
		Assets.loseScreen.add(zillipLogo);
		zillipLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
		Assets.loseScreen.add(Box.createVerticalStrut(4));
		JLabel label = new JLabel("You lost! and you only got...");
		label.setFont(new Font("Courier",Font.PLAIN,32));
		label.setAlignmentX(CENTER_ALIGNMENT);
		Assets.loseScreen.add(label);
		Assets.loseScreen.add(Box.createVerticalStrut(4));
		Assets.loseScreen.add(Assets.displayScore);
		Assets.displayScore.setAlignmentX(CENTER_ALIGNMENT);
		Assets.playButton.setAlignmentX(CENTER_ALIGNMENT);
		Assets.loseScreen.add(Box.createVerticalStrut(4));
		Assets.loseScreen.add(Assets.playButton);
		c.add(Assets.loseScreen);
		setContentPane(c);
		validate();
	}
	
	public void init() {
		setSize(640,480);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					createLoading();
				}
			});
		} catch (Exception e) {
			System.out.println("Could not load. Exiting...");
			System.exit(1);
		}
		(new Thread(new Assets())).start();
	}
	public void alert() {
		int assetPercent = Assets.getPercent();
		if (assetPercent < 99) {
			loadingLabel.setText("Loading Assets... " + ((int) assetPercent));
		} else {
			loadingLabel.setText("Loading Assets... " + ((int) assetPercent));
			System.out.println("Data done loading. Loading GUI");
			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						createGUI();
					}
				});
			} catch (Exception e) {
				System.out.println("Could not load. Exiting...");
				System.exit(1);
			}
		}	
	}
	public void stop() {
		Assets.destroy();
	}
}
