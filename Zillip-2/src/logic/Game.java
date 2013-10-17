package logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Game extends JLabel implements MouseListener, MouseMotionListener {
	
	private BufferedImage zillip;
	private BufferedImage redZillip;
	private BufferedImage orangeZillip;
	private BufferedImage yellowZillip;
	private BufferedImage greenZillip;
	private BufferedImage blueZillip;
	private BufferedImage purpleZillip;
	private BufferedImage canvas;
	private boolean isMouseDown = false;
	static enum colors {RED, YELLOW, GREEN, BLUE, PURPLE, ORANGE};
	static enum direction {VERTICAL, HORIZONTAL, NONE, RELEASED};
	private direction movement = direction.RELEASED;
	private colors[][] gamefield = new colors[6][6];
	private Point currentPosition;
	private Point initialPosition;
	
	public Game() {
		
	}
	
	public Game(BufferedImage zillip) {
		super(new ImageIcon(new BufferedImage(zillip.getWidth()*6,zillip.getHeight()*6,Transparency.BITMASK)));
		canvas = new BufferedImage(zillip.getWidth()*6,zillip.getHeight()*6,Transparency.BITMASK);
		this.zillip = zillip;
		setSize(zillip.getWidth()*6,zillip.getHeight()*6);
		Random x = new Random();
		for (int i = 0;i<6;i++) {
			for (int j = 0;j<6;j++) {
				int color = x.nextInt(6);
				switch (color) {
				case 0:
					gamefield[i][j] = colors.RED;
					break;
				case 1:
					gamefield[i][j] = colors.ORANGE;
					break;
				case 2:
					gamefield[i][j] = colors.YELLOW;
					break;
				case 3:
					gamefield[i][j] = colors.GREEN;
					break;
				case 4:
					gamefield[i][j] = colors.BLUE;
					break;
				case 5:
					gamefield[i][j] = colors.PURPLE;
					break;
				default:
					gamefield[i][j] = colors.RED;
					break;
				}
			}
		}
		addMouseListener(this);
		addMouseMotionListener(this);
		loadZillips();
		update();
	}
	
	public static String printPoint(Point x) {
		return ("("+x.getX()+","+x.getY()+")");
	}

	public void loadZillips() {
		redZillip = colorImage(zillip,colors.RED);
		orangeZillip = colorImage(zillip,colors.ORANGE);
		yellowZillip = colorImage(zillip,colors.YELLOW);
		greenZillip = colorImage(zillip,colors.GREEN);
		blueZillip = colorImage(zillip,colors.BLUE);
		purpleZillip = colorImage(zillip,colors.PURPLE);
	}
	
	public void update() {
		Graphics g = canvas.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, zillip.getWidth()*6, zillip.getHeight()*6);
		if (!isMouseDown() || movement == direction.NONE)
			for (int i = 0;i<6;i++)
				for (int j = 0;j<6;j++) {
					if (gamefield[i][j] == colors.RED) {
				    	g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
				    }
				    if (gamefield[i][j] == colors.ORANGE) {
				    	g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
				    }
				    if (gamefield[i][j] == colors.YELLOW) {
				    	g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
				    }
				    if (gamefield[i][j] == colors.GREEN) {
				    	g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
				    }
				    if (gamefield[i][j] == colors.BLUE) {
				    	g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
				    }
				    if (gamefield[i][j] == colors.PURPLE) {
				    	g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
				    }
				}
		else {
			if (movement == direction.VERTICAL)
				for (int i = 0;i<6;i++)
					for (int j = 0;j<6;j++) {
						if (getCoordinates(initialPosition).getX() == i) {
							int moveAmount = (int) (currentPosition.getY()-initialPosition.getY());
							if (gamefield[i][j] == colors.RED) {
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.ORANGE) {
						    	g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
						    	g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.YELLOW) {
						    	g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
						    	g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.GREEN) {
						    	g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
						    	g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.BLUE) {
						    	g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
						    	g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.PURPLE) {
						    	g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
						    	g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
						    }
						} else {
							if (gamefield[i][j] == colors.RED) {
						    	g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.ORANGE) {
						    	g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.YELLOW) {
						    	g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.GREEN) {
						    	g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.BLUE) {
						    	g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.PURPLE) {
						    	g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						}
					}
			else if (movement == direction.HORIZONTAL)
				for (int i = 0;i<6;i++)
					for (int j = 0;j<6;j++) {
						if (getCoordinates(initialPosition).getY() == j) {
							int moveAmount = (int) (currentPosition.getX()-initialPosition.getX());
							if (gamefield[i][j] == colors.RED) {
						    	g.drawImage(redZillip,i*zillip.getWidth()+moveAmount,j*zillip.getWidth(),null);
						    	g.drawImage(redZillip,i*zillip.getWidth()+moveAmount+6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(redZillip,i*zillip.getWidth()+moveAmount+12*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(redZillip,i*zillip.getWidth()+moveAmount-6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(redZillip,i*zillip.getWidth()+moveAmount-12*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.ORANGE) {
						    	g.drawImage(orangeZillip,i*zillip.getWidth()+moveAmount,j*zillip.getWidth(),null);
						    	g.drawImage(orangeZillip,i*zillip.getWidth()+moveAmount+6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(orangeZillip,i*zillip.getWidth()+moveAmount+12*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(orangeZillip,i*zillip.getWidth()+moveAmount-6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(orangeZillip,i*zillip.getWidth()+moveAmount-12*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.YELLOW) {
						    	g.drawImage(yellowZillip,i*zillip.getWidth()+moveAmount,j*zillip.getWidth(),null);	
						    	g.drawImage(yellowZillip,i*zillip.getWidth()+moveAmount+6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(yellowZillip,i*zillip.getWidth()+moveAmount+12*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(yellowZillip,i*zillip.getWidth()+moveAmount-6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(yellowZillip,i*zillip.getWidth()+moveAmount-12*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.GREEN) {
						    	g.drawImage(greenZillip,i*zillip.getWidth()+moveAmount,j*zillip.getWidth(),null);
						    	g.drawImage(greenZillip,i*zillip.getWidth()+moveAmount+6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(greenZillip,i*zillip.getWidth()+moveAmount+12*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(greenZillip,i*zillip.getWidth()+moveAmount-6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(greenZillip,i*zillip.getWidth()+moveAmount-12*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.BLUE) {
						    	g.drawImage(blueZillip,i*zillip.getWidth()+moveAmount,j*zillip.getWidth(),null);
						    	g.drawImage(blueZillip,i*zillip.getWidth()+moveAmount+6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(blueZillip,i*zillip.getWidth()+moveAmount+12*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(blueZillip,i*zillip.getWidth()+moveAmount-6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(blueZillip,i*zillip.getWidth()+moveAmount-12*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.PURPLE) {
						    	g.drawImage(purpleZillip,i*zillip.getWidth()+moveAmount,j*zillip.getWidth(),null);
						    	g.drawImage(purpleZillip,i*zillip.getWidth()+moveAmount+6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(purpleZillip,i*zillip.getWidth()+moveAmount+12*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(purpleZillip,i*zillip.getWidth()+moveAmount-6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(purpleZillip,i*zillip.getWidth()+moveAmount-12*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						} else {
							if (gamefield[i][j] == colors.RED) {
						    	g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						    if (gamefield[i][j] == colors.ORANGE) {
						    	g.drawImage(orangeZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.YELLOW) {
						    	g.drawImage(yellowZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.GREEN) {
						    	g.drawImage(greenZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.BLUE) {
						    	g.drawImage(blueZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);				    
						    }
						    if (gamefield[i][j] == colors.PURPLE) {
						    	g.drawImage(purpleZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						}
					}
		}
		g.dispose();
		setIcon(new ImageIcon(canvas));
	}
	public BufferedImage colorImage(BufferedImage image,  colors x) {
	    Color color = Color.RED;
	    if (x == colors.RED) {
	    	color = new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.red.getBlue(),0);
	    }
	    if (x == colors.ORANGE) {
	    	color = new Color(255,111,0,0);
	    }
	    if (x == colors.YELLOW) {
	    	color = new Color(Color.YELLOW.getRed(),Color.YELLOW.getGreen(),Color.YELLOW.getBlue(),0);
	    }
	    if (x == colors.GREEN) {
	    	color = new Color(Color.GREEN.getRed(),Color.GREEN.getGreen(),Color.GREEN.getBlue(),0);
	    }
	    if (x == colors.BLUE) {
	    	color = new Color(Color.BLUE.getRed(),Color.BLUE.getGreen(),Color.BLUE.getBlue(),0);
	    }
	    if (x == colors.PURPLE) {
	    	color = new Color(Color.MAGENTA.getRed(),Color.MAGENTA.getGreen(),Color.MAGENTA.getBlue(),0);
	    }
	    BufferedImage destImage = new BufferedImage(image.getWidth(),
                image.getHeight(), BufferedImage.TYPE_INT_RGB);

        Graphics2D g = destImage.createGraphics();
        g.drawImage(image, null, 0, 0);
        g.dispose();
        for (int i = 0; i < destImage.getWidth(); i++) {
            for (int j = 0; j < destImage.getHeight(); j++) {
                int destRGB = destImage.getRGB(i, j);
                if (destRGB == Color.BLACK.getRGB())
                    destImage.setRGB(i, j, color.getRGB());
                else
                	destImage.setRGB(i, j, (new Color(0,0,0,0).getRGB()));
            }
        }
        return destImage;
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		setCurrentPosition(arg0.getPoint());
		getCoordinates(arg0.getPoint());
		if (Math.abs(initialPosition.getX()-currentPosition.getX()) > Math.abs(initialPosition.getY()-currentPosition.getY()))
			setDirection(direction.HORIZONTAL);
		else if (Math.abs(initialPosition.getX()-currentPosition.getX()) < Math.abs(initialPosition.getY()-currentPosition.getY()))
			setDirection(direction.VERTICAL);
		else if (getCoordinates(initialPosition).equals(getCoordinates(currentPosition)))
			setDirection(direction.NONE);
		update();
	}

	public void shift(boolean vertical, double rowdouble, double amountdouble) {
		
		colors[] rowArray = new colors[6];
		colors[] rowNew = new colors[6];
		
		int row = (int) Math.round(rowdouble);
		int amount = (int) Math.round(amountdouble);
		
		if (vertical) {
			for (int i = 0;i<rowArray.length;i++)
				rowArray[i] = gamefield[row][i];
			for (int i = 0;i<rowNew.length;i++)
				rowNew[i] = rowArray[(i-amount+18)%6];
			for (int i = 0;i<rowNew.length;i++)
				gamefield[row][i] = rowNew[i];
		} else {
			for (int i = 0;i<rowArray.length;i++)
				rowArray[i] = gamefield[i][row];
			for (int i = 0;i<rowNew.length;i++)
				rowNew[i] = rowArray[(i-amount+18)%6];
			for (int i = 0;i<rowNew.length;i++)
				gamefield[i][row] = rowNew[i];
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {	
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		setMouseDown(true);
		setInitialPosition(arg0.getPoint());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		setMouseDown(false);
		Point initialpos = getCoordinates(initialPosition);
		Point finalpos = getCoordinates(arg0.getPoint());
		if (movement == direction.HORIZONTAL)
			shift(false, initialpos.getY(), finalpos.getX()-initialpos.getX());
		else if (movement == direction.VERTICAL)
			shift(true, initialpos.getX(), finalpos.getY()-initialpos.getY());
		setDirection(direction.RELEASED);
		update();
	}
	
	// Synced methods
	
	public synchronized Point getCoordinates(Point point) {
		int x,y;
		x = (int) (point.getX()/zillip.getWidth());
		y = (int) (point.getY()/zillip.getHeight());
		return new Point(x,y);
	}
	
	public synchronized boolean isMouseDown() {
		return isMouseDown;
	}

	public synchronized void setMouseDown(boolean isMouseDown) {
		this.isMouseDown = isMouseDown;
	}

	public synchronized Point getInitialPosition() {
		return initialPosition;
	}

	public synchronized void setInitialPosition(Point initialPosition) {
		this.initialPosition = initialPosition;
	}

	public synchronized Point getCurrentPosition() {
		return currentPosition;
	}

	public synchronized void setCurrentPosition(Point currentPosition) {
		this.currentPosition = currentPosition;
	}
	
	public synchronized void setDirection(direction x) {
		movement = x;
	}

}
