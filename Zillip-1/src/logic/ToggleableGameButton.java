package logic;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class ToggleableGameButton extends GameButton {
	
	BufferedImage a,b,c,d,e,f;
	public boolean isOn = true;
	
	public ToggleableGameButton(BufferedImage q,BufferedImage r,BufferedImage s,BufferedImage t,BufferedImage u,BufferedImage v) {
		super(q,r,s,null);
		a=q;
		b=r;
		c=s;
		d=t;
		e=u;
		f=v;
	}
	
	public void actionPerformed(ActionEvent hjf) {
		isOn = !isOn;
		if (isOn) {
			Assets.sound(true);
			this.setIcon(new ImageIcon(a));
			this.setRolloverIcon(new ImageIcon(b));
			this.setPressedIcon(new ImageIcon(c));
			new SoundPlayer(Assets.sound,false);
		} else {
			Assets.sound(false);
			this.setIcon(new ImageIcon(d));
			this.setRolloverIcon(new ImageIcon(e));
			this.setPressedIcon(new ImageIcon(f));
		}
	}

}
