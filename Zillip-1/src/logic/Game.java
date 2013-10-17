package logic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Game extends JLabel implements MouseListener, MouseMotionListener, ActionListener {

	private BufferedImage zillip;
	private BufferedImage redZillip;
	private BufferedImage orangeZillip;
	private BufferedImage yellowZillip;
	private BufferedImage greenZillip;
	private BufferedImage blueZillip;
	private BufferedImage lightBlueZillip;
	private BufferedImage purpleZillip;
	private BufferedImage zillipPopped;
	private BufferedImage canvas;
	private BufferedImage verticalArrow;
	private BufferedImage horizontalArrow;
	public SoundPlayer mainTheme;
	private Moves hint;
	private boolean lock = false;
	private boolean isMouseDown = false;
	private Timer timer = new Timer(2, this);
	static enum colors {RED, YELLOW, GREEN, BLUE, PURPLE, ORANGE, LIGHT_BLUE, DESTROYED};
	static final colors[] colorArray = {colors.RED,colors.ORANGE,colors.YELLOW,colors.GREEN,colors.BLUE,colors.PURPLE, colors.LIGHT_BLUE};
	static enum direction {VERTICAL, HORIZONTAL, NONE, RELEASED};
	Vector<Moves> previousMoves = new Vector<Moves>(5);
	Moves previousMove;
	private direction movement = direction.RELEASED;
	private colors[][] gamefield = new colors[6][6];
	private Point currentPosition;
	private Point initialPosition;
	private ArrayList<ArrayList<Point>> chains = null;
	public Game(BufferedImage zillip, BufferedImage popped, BufferedImage arrow) {
		super(new ImageIcon(new BufferedImage(zillip.getWidth()*6,zillip.getHeight()*6,Transparency.BITMASK)));
		canvas = new BufferedImage(zillip.getWidth()*6,zillip.getHeight()*6,Transparency.BITMASK);
		verticalArrow = arrow;
		AffineTransform at = new AffineTransform();
		at.rotate(Math.toRadians(-90), arrow.getWidth() / 2, arrow.getHeight() / 2);
		AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		horizontalArrow = ato.filter(arrow, null);
		this.zillip = zillip;
		zillipPopped = popped;
		setSize(zillip.getWidth()*6,zillip.getHeight()*6);
		createGameField();
		addMouseListener(this);
		addMouseMotionListener(this);
		loadZillips();
		update();
		setLock(true);
	}
	
	public void play() {
		setLock(false);
		mainTheme = new SoundPlayer(Assets.mainMusic,true);
	}
	
	public void hint() {
		colors[][] x = new colors[6][6];
		for (int i = 0;i<6;i++)
			for (int j = 0;j<6;j++)
				x[i][j] = gamefield[i][j];
		setHint(GameHelper.findMoves(x));
		update();
	}

	private void addToChain(Point p, ArrayList<Point> array) {
		boolean hasPoint = false;
		for (Point x : array) 
			if (x.equals(p))
				hasPoint = true;
		if (!hasPoint)
			array.add(p);
	}
	
	private ArrayList<Point> getNeighbors(Point p) {
		ArrayList<Point> returner = new ArrayList<Point>();
		int x,y;
		x = (int) p.getX();
		y = (int) p.getY();
		try {
			if (gamefield[x][y] == gamefield[x+1][y])
				returner.add(new Point(x+1,y));
		} catch (Exception e) {}
		try {
			if (gamefield[x][y] == gamefield[x-1][y])
				returner.add(new Point(x-1,y));
		} catch (Exception e) {}
		try {
			if (gamefield[x][y] == gamefield[x][y+1])
				returner.add(new Point(x,y+1));
		} catch (Exception e) {}
		try {
			if (gamefield[x][y] == gamefield[x][y-1])
				returner.add(new Point(x,y-1));
		} catch (Exception e) {}
		
		return returner;
	}
	
	public ArrayList<ArrayList<Point>> getChains() {
		ArrayList<ArrayList<Point>> returner = new ArrayList<ArrayList<Point>>();
		for (int i = 0;i<6;i++)
			for (int j = 0;j<6;j++) {
				ArrayList<Point> x = getChain(new Point(i,j));
				if (x.size() > 2)
					returner.add(x);
			}
				
		return returner;
	}
	
	public ArrayList<Point> getChain(Point p) {
		ArrayList<Point> returner = new ArrayList<Point>();
		
		for (Point point : getNeighbors(p)) {
			getChain(point, 0, returner);
			addToChain(point, returner);
		}
		return returner;
	}
	
	public void getChain(Point p, int value, ArrayList<Point> returner) {
		if (value > 10) 
			return;
		for (Point point : getNeighbors(p)) {
			getChain(point, ++value, returner);
			addToChain(point, returner);
		}
	}
	
	public void createGameField() {
		ArrayList<colors> x = new ArrayList<colors>(6);
		Random random = new Random();
		for (int j = 0;j < 6; j++) {
			x.add(colors.RED);
			x.add(colors.ORANGE);
			x.add(colors.YELLOW);
			x.add(colors.GREEN);
			x.add(colors.BLUE);
			x.add(colors.PURPLE);
			x.add(colors.LIGHT_BLUE);
			for (int i = 5;i > 0;i--) {
				gamefield[i][j] = x.remove(random.nextInt(i+1));
			}
			gamefield[0][j] = x.remove(0);
		}
		for (int i = 0;i<6;i++) {
			for (int j = 0;j<5;j++) {
				if (gamefield[i][j] == gamefield[i][j+1]) {
					if (random.nextInt(4) >= 1)
						gamefield[i][j+1] = colorArray[random.nextInt(7)];
				}
			}
		}
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
		lightBlueZillip = colorImage(zillip,colors.LIGHT_BLUE);
	}
	
	public void update() {
		paint();
	}
	
	public void destroyChains() {
		if (chains != null) {
			setLock(true);
			final ArrayList<ArrayList<Point>> tempChains = chains;
			for (ArrayList<Point> chain : chains) {
				if (chain.size() == 3) {
					new SoundPlayer(Assets.smallExplode, false);
					try {
						Assets.displayScore.setScore(Assets.displayScore.getScore()+3);
						Assets.gameTimer.setPercent(Assets.gameTimer.getPercent()-2);
					} catch (Exception e) {
						
					}
				}
				if (chain.size() == 4) {
					new SoundPlayer(Assets.mediumExplode, false);
					try {
						Assets.displayScore.setScore(Assets.displayScore.getScore()+5);
						Assets.gameTimer.setPercent(Assets.gameTimer.getPercent()-4);
					} catch (Exception e) {
						
					}
				}
				if (chain.size() >= 5) {
					new SoundPlayer(Assets.bigExplode, false);
					try {
						Assets.displayScore.setScore(Assets.displayScore.getScore()+chain.size()*2+(chain.size()-5)*5);
						Assets.gameTimer.setPercent(Assets.gameTimer.getPercent()-chain.size()*1.3);
					} catch (Exception e) {
						
					}
				}
				for (Point point : chain)
					gamefield[(int) point.getX()][(int) point.getY()] = colors.DESTROYED;
			}
			ActionListener listener = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Random x = new Random();
					for (ArrayList<Point> chain : tempChains)
						for (Point point : chain)
							gamefield[(int) point.getX()][(int) point.getY()] = colorArray[x.nextInt(7)];
					setLock(false);
					update();
					ArrayList<ArrayList<Point>> newChains = getChains();
					if (newChains.size() <= 0)
						chains = null;
					else
						chains = newChains;
					Timer notX = new Timer(300,new ActionListener(){public void actionPerformed(ActionEvent e){destroyChains();}});
					notX.setRepeats(false);
					notX.start();
				}
			};
			Timer x = new Timer(80,listener);
			x.setRepeats(false);
			x.start();
			update();
		}
	}
	
	public void paint() {
		
		Graphics g = canvas.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, zillip.getWidth()*6, zillip.getHeight()*6);
		if (!isMouseDown() || movement == direction.NONE) {
			for (int i = 0;i<6;i++)
				for (int j = 0;j<6;j++) {
					if (gamefield[i][j] == colors.RED) {
				    	g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
				    }
					if (gamefield[i][j] == colors.LIGHT_BLUE) {
				    	g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
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
				    if (gamefield[i][j] == colors.DESTROYED) {
				    	g.drawImage(zillipPopped,i*zillip.getWidth(),j*zillip.getWidth(),null);
				    }
				}
			if (hint != null) {
		    	if (hint.vertical) {
		    		g.drawImage(verticalArrow, hint.row*zillip.getHeight(), 0, null);
		    	} else {
		    		g.drawImage(horizontalArrow, 0, hint.row*zillip.getHeight(), null);
		    	}
		    	hint = null;
		    }
		} else {
			if (movement == direction.VERTICAL) {
				for (int i = 0;i<6;i++) {
					for (int j = 0;j<6;j++) {
						if (getCoordinates(initialPosition).getX() == i) {
							int moveAmount = (int) (getCurrentPosition().getY()-initialPosition.getY());
							if (gamefield[i][j] == colors.RED) {
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(redZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
						    }
							if (gamefield[i][j] == colors.LIGHT_BLUE) {
								g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount,null);
								g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+6*zillip.getWidth(),null);
								g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-6*zillip.getWidth(),null);
								g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount+12*zillip.getWidth(),null);
								g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth()+moveAmount-12*zillip.getWidth(),null);
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
							if (gamefield[i][j] == colors.LIGHT_BLUE) {
						    	g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						}
					}
				}
			} else if (movement == direction.HORIZONTAL) {
				for (int i = 0;i<6;i++) {
					for (int j = 0;j<6;j++) {
						if (getCoordinates(initialPosition).getY() == j) {
							int moveAmount = (int) (getCurrentPosition().getX()-initialPosition.getX());
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
						    if (gamefield[i][j] == colors.LIGHT_BLUE) {
						    	g.drawImage(lightBlueZillip,i*zillip.getWidth()+moveAmount,j*zillip.getWidth(),null);
						    	g.drawImage(lightBlueZillip,i*zillip.getWidth()+moveAmount+6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(lightBlueZillip,i*zillip.getWidth()+moveAmount+12*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(lightBlueZillip,i*zillip.getWidth()+moveAmount-6*zillip.getWidth(),j*zillip.getWidth(),null);
						    	g.drawImage(lightBlueZillip,i*zillip.getWidth()+moveAmount-12*zillip.getWidth(),j*zillip.getWidth(),null);
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
							if (gamefield[i][j] == colors.LIGHT_BLUE) {
						    	g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
						    }
						}
					}
				}
			} else {
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
						if (gamefield[i][j] == colors.LIGHT_BLUE) {
					    	g.drawImage(lightBlueZillip,i*zillip.getWidth(),j*zillip.getWidth(),null);
					    }
					}
			}
		}
		g.dispose();
		setIcon(new ImageIcon(canvas));
		
	}
	public BufferedImage colorImage(BufferedImage image,  colors x) {
	    Color color = Color.BLACK;
	    if (x == colors.RED) {
	    	color = new Color(Color.RED.getRed(),Color.RED.getGreen(),Color.red.getBlue(),0);
	    }
	    if (x == colors.ORANGE) {
	    	color = new Color(255,111,0,0);
	    }
	    if (x == colors.LIGHT_BLUE) {
	    	color = new Color(150,255,255,0);
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
	    	color = new Color(150,0,150,0);
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
		if (!isLocked()) {
			setCurrentPosition(arg0.getPoint());
			getCoordinates(arg0.getPoint());
			if (Math.abs(initialPosition.getX()-getCurrentPosition().getX()) > Math.abs(initialPosition.getY()-getCurrentPosition().getY()))
				setDirection(direction.HORIZONTAL);
			else if (Math.abs(initialPosition.getX()-getCurrentPosition().getX()) < Math.abs(initialPosition.getY()-getCurrentPosition().getY()))
				setDirection(direction.VERTICAL);
			else if (getCoordinates(initialPosition).equals(getCoordinates(currentPosition)))
				setDirection(direction.NONE);
			update();
		}
	}

	public void shift(boolean vertical, double rowdouble, double amountdouble) {
		
		colors[] rowArray = new colors[6];
		colors[] rowNew = new colors[6];
		
		int row = (int) Math.round(rowdouble);
		int amount = (int) Math.round(amountdouble);
		amount %= 6;
		Moves x = new Moves();
		x.row = row;
		x.amount = amount;
		x.vertical = vertical;
		if (previousMoves.size() == 5)
			previousMoves.remove(4);
		previousMoves.add(0, x);
		
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
		update();
	}
	
	private boolean unShiftDisplay(boolean vertical) {
		double positionX = getCurrentPosition().getX();
		double positionY = getCurrentPosition().getY();
		if (vertical) {
			setDirection(direction.VERTICAL);
			if (getCurrentPosition().getY()-initialPosition.getY() > 0) {
				positionY -= 1;
				setCurrentPosition(positionX, positionY);
				update();
			} else if (getCurrentPosition().getY()-initialPosition.getY() < 0){
				positionY += 1;
				setCurrentPosition(positionX, positionY);
				update();
			} else {
				return false;
			}
		} else if (!vertical) {
			setDirection(direction.HORIZONTAL);
			if (getCurrentPosition().getX()-initialPosition.getX() > 0) {
				positionX -= 1;
				setCurrentPosition(positionX, positionY-10);
				update();
			} else if (getCurrentPosition().getX()-initialPosition.getX() < 0){
				positionX += 1;
				setCurrentPosition(positionX, positionY);
				update();
			} else {
				return false;
			}
		}
		return true;
	}
	
	public void unShift() {
		if (previousMoves.size()>0 && !isLocked()) {
			setLock(true);
			setMouseDown(true);
			previousMove = previousMoves.get(0);
			double initialX,initialY,finalX,finalY;
			if (previousMove.vertical) {
				initialX = finalX = ((double) previousMove.row + 0.5) * ((double) zillip.getWidth());
				if (previousMove.amount > 0) {
					initialY = 0.5 * zillip.getWidth();
				} else {
					initialY = 5.5 * zillip.getWidth();
				}
				finalY = initialY + (previousMove.amount * zillip.getWidth());
			} else {
				initialY = finalY = ((double) previousMove.row + 0.5)*zillip.getWidth();
				if (previousMove.amount > 0) {
					initialX = 0.5 * zillip.getWidth();
				} else {
					initialX = 5.5 * zillip.getWidth();
				}
				finalX = initialX + (previousMove.amount * zillip.getWidth());
			}
			initialPosition.setLocation(initialX, initialY);
			setCurrentPosition(finalX,finalY);
			shift(previousMove.vertical,previousMove.row,-1*previousMove.amount);
			timer.start();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!unShiftDisplay(previousMove.vertical)) {
			setMouseDown(false);
			setLock(false);
			timer.stop();
		}
		
	}
	
	@Override
	public void mouseMoved(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) {
		if (!isLocked()) {
			setMouseDown(true);
			setInitialPosition(arg0.getPoint());
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (!isLocked()) {
			setMouseDown(false);
			Point initialpos = getCoordinates(initialPosition);
			Point finalpos = getCoordinates(arg0.getPoint());
			if (movement == direction.HORIZONTAL) {
				shift(false, initialpos.getY(), finalpos.getX()-initialpos.getX());
				chains = getChains();
				if (chains.size() <= 0) {
					unShift();
					chains = null;
				}
			} else if (movement == direction.VERTICAL) {
				shift(true, initialpos.getX(), finalpos.getY()-initialpos.getY());
				chains = getChains();
				if (chains.size() <= 0) {
					unShift();
					chains = null;
				}
			}
			destroyChains();
			setDirection(direction.RELEASED);
		}
	}
	
	public void pause() {
		setLock(true);
		mainTheme.setSoundOff(true);
	}
	
	public void resume() {
		setLock(false);
		mainTheme.setSoundOff(false);
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

	public synchronized void setCurrentPosition(double x,double y) {
		currentPosition.setLocation(x, y);
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

	public synchronized boolean isLocked() {
		return lock;
	}
	
	public synchronized void setLock(boolean lock) {
		this.lock = lock;
	}

	public synchronized Moves getHint() {
		return hint;
	}

	public synchronized void setHint(Moves hint) {
		this.hint = hint;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
