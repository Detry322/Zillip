package game;

import java.awt.Container;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;

import logic.Assets;

public class FancyButton extends JButton {
  public FancyButton(Icon icon, Icon pressed, Icon rollover) {
    super(icon);
    setFocusPainted(false);
    setRolloverEnabled(true);
    setRolloverIcon(rollover);
    setPressedIcon(pressed);
    setBorderPainted(false);
    setContentAreaFilled(false);
  }

  // A simple test program 
  public static void main(String[] args) {

	  (new Thread(new Assets())).start();
	  while (Assets.getPercent() < 99);
	  try {
		  MenuSide menuSide = new MenuSide(Assets.menuSide);
		  menuSide.setLayout(new BoxLayout(menuSide,BoxLayout.Y_AXIS));
		  GameSide gameSide = new GameSide(Assets.gameSide);
		  final JFrame f = new JFrame();
		  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		  Container c = f.getContentPane();
		  c.setLayout(new BoxLayout(c,BoxLayout.X_AXIS));
		  c.add(menuSide);
		  c.add(gameSide);
		  f.pack();
		  f.setVisible(true);
	  } catch (Exception e) {
		  
	  }
  }
}