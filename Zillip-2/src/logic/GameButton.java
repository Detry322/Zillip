package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameButton extends JButton implements ActionListener {
	
	Runnable x;
	
	public GameButton(BufferedImage icon, BufferedImage hover, BufferedImage pressed, Runnable runnable) {
	    super(new ImageIcon(icon));
	    setFocusPainted(false);
	    setPressedIcon(new ImageIcon(pressed));
	    setRolloverEnabled(true);
	    setRolloverIcon(new ImageIcon(hover));
	    setBorderPainted(false);
	    setContentAreaFilled(false);
	    setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
	    x = runnable;
	    addActionListener(this);
	  }
	public GameButton() {
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		x.run();
	}
}
