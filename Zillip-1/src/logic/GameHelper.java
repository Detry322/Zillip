package logic;

import java.awt.Point;
import java.util.ArrayList;

import logic.Game.colors;

public class GameHelper {
	
	private static Game.colors[][] gamefield;

	public static Moves findMoves(Game.colors[][] x) {
		gamefield = x;
		boolean vertical = true;
		for (int k = 0;k<2;k++) {
			for (int i = 0;i<6;i++) {
				for (int j = 1;j<7;j++) {
					shift(vertical,i);
					ArrayList<ArrayList<Point>> chain = getChains();
					if (chain.size() > 0) {
						Moves lol = new Moves();
						lol.amount = j;
						lol.vertical = vertical;
						lol.row = i;
						return lol;
					}
				}
			}
			vertical = !vertical;
		}
		return null;
	}
	
	public static void shift(boolean vertical, int row) {
		
		colors[] rowArray = new colors[6];
		colors[] rowNew = new colors[6];
		
		int amount = 1;
		
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
	
	private static void addToChain(Point p, ArrayList<Point> array) {
		boolean hasPoint = false;
		for (Point x : array) 
			if (x.equals(p))
				hasPoint = true;
		if (!hasPoint)
			array.add(p);
	}
	private static ArrayList<Point> getNeighbors(Point p) {
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
	
	public static ArrayList<ArrayList<Point>> getChains() {
		ArrayList<ArrayList<Point>> returner = new ArrayList<ArrayList<Point>>();
		for (int i = 0;i<6;i++)
			for (int j = 0;j<6;j++) {
				ArrayList<Point> x = getChain(new Point(i,j));
				if (x.size() > 2)
					returner.add(x);
			}
				
		return returner;
	}
	
	public static ArrayList<Point> getChain(Point p) {
		ArrayList<Point> returner = new ArrayList<Point>();
		
		for (Point point : getNeighbors(p)) {
			getChain(point, 0, returner);
			addToChain(point, returner);
		}
		return returner;
	}
	
	public static void getChain(Point p, int value, ArrayList<Point> returner) {
		if (value > 10) 
			return;
		for (Point point : getNeighbors(p)) {
			getChain(point, ++value, returner);
			addToChain(point, returner);
		}
	}
	
}
