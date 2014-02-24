package edu.uwec.cs.forstezt.path;

//This is the object that will go in the open and closed lists of the path finder

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.*;

public class PathState  {
	
	private static final int MIN_TILE_COST = 2;
	
	private Point position;
	private Point goal;
	private boolean[][] validPositions;
	private List<Point> path;
	private List<Point> children;
	private int currentCost;
	private TerrainMap tm;
	
	/**
	 * Constructor for a PathState instance located at position (0,0)
	 * @param position - A Point representing the position of the current state on the board
	 * @param goal - A Point representing the position of the current state's goal on the board
	 * @param terrainWidth - The integer width of the board/terrain
	 * @param terrainHeight - The integer height of the board/terrain
	 */
	public PathState(Point position, Point goal, TerrainMap tm) {		
		this.position = position;
		this.goal = goal;
		this.currentCost = 0;
		this.tm = tm;
		this.path = new ArrayList<Point>();
		this.path.add(this.position);
		
		this.children = new ArrayList<Point>();
		this.children.add(new Point(this.position.x, this.position.y - 1));
		this.children.add(new Point(this.position.x + 1, this.position.y));			
		this.children.add(new Point(this.position.x, this.position.y + 1));
		this.children.add(new Point(this.position.x - 1, this.position.y));
		
		this.validPositions = new boolean[tm.getTerrainHeight()][tm.getTerrainWidth()];
		for (int row = 0; row < tm.getTerrainHeight(); row++) {
			for (int col = 0; col < tm.getTerrainWidth(); col++) {
				this.validPositions[row][col] = true;
			}
		}
		this.validPositions[this.position.y][this.position.x] = false;
	}
	
	/**
	 * Copy constructor for a PathState instance
	 * @param orig - The PathState instance being copied
	 */
	public PathState(PathState orig) {
		this.position = new Point(orig.position.x, orig.position.y);
		this.goal = new Point(orig.goal.x, orig.goal.y);
		this.currentCost = orig.currentCost;
		this.tm = orig.tm;
		
		this.children = new ArrayList<Point>();
		for (Point point : orig.children) {
			this.children.add(new Point(point.x, point.y));
		}
		
		this.path = new ArrayList<Point>();
		for (Point point : orig.path) {
			this.path.add(new Point(point.x, point.y));
		}
		
		this.validPositions = orig.validPositions;
	}
	
	/**
	 * Returns the next child of this state or null if no viable children are left
	 * @return the next child of this state
	 */
	public PathState getNextChild() {		
		//generate child
		PathState child = null;
		
		//find the next viable child, if one exists
		boolean validPositionFound = false;
		Point childPosition;
		while (!this.children.isEmpty() && !validPositionFound) {
			//try the next position in the list of child positions
			childPosition = this.children.remove(0);
			
			//the position is a valid one
			if (childPosition.y >= 0 &&
				childPosition.x >= 0 &&
				childPosition.y < this.tm.getTerrainHeight() &&
				childPosition.x < this.tm.getTerrainWidth() &&
				this.validPositions[childPosition.y][childPosition.x]) {
				
				//create a new child with the chosen position
				child = new PathState(this);
				child.position = childPosition;
				validPositionFound = true;
			}
		}
		
		//a viable child was found
		if (child != null) {
			//update the global list of which positions have already been checked
			this.validPositions[child.position.y][child.position.x] = false;
					
			//create the child's list of children
			child.children = new ArrayList<Point>();
			child.children.add(new Point(child.position.x, child.position.y - 1));
			child.children.add(new Point(child.position.x + 1, child.position.y));			
			child.children.add(new Point(child.position.x, child.position.y + 1));
			child.children.add(new Point(child.position.x - 1, child.position.y));
			
			//add the parent to the child's path
			child.path.add(child.position);
			
			//modify the child's currentCost
			child.currentCost += child.tm.getCost(child.position);
		}
		
		return child;
	}
	
	/**
	 * Returns true if this state has reached its goal, false otherwise
	 * @return whether or not the state has reached its goal
	 */
	public boolean isGoal() {
		return this.position.equals(goal);
	}
	
	/**
	 * Returns a list of the path from the start to the current position
	 * @return the path
	 */
	public List<Point> getPath() {
		return this.path;
	}
	
	/**
	 * Returns the estimated bound on the current state
	 * @return the current state's estimated bound
	 */
	public int getBound() {		
		int manhattanDist = Math.abs(this.position.x - this.goal.x) + Math.abs(this.position.y - this.goal.y); 
		return this.currentCost + manhattanDist * MIN_TILE_COST;
	}
	
	// You may need equals/compareto/hashcode/tostring depend on the data structures you are using
	
	
	// This assumes that you have a Point called position as an instance variable, but feel free to change this method as well
	public void draw(Graphics g, int width, int height) {
		
		// Shade based on the estimator
		int colorShade = (int)((getBound()) * 1.5);
		if (colorShade > 255) {
			colorShade = 255;
		}
		
		g.setColor(new Color(colorShade, colorShade, colorShade));

		g.fillOval((this.position.x * width) + (width/4), (this.position.y * height) + (width/4), width/2, height/2);
		
		g.setColor(Color.black);
		g.drawOval((this.position.x * width) + (width/4), (this.position.y * height) + (width/4), width/2, height/2);
	
	}
	
	public String toString() {
		return "(" + this.position.x + ", " + this.position.y + ")" /*+ " Bound: " + this.getBound()*/;
	}
	
	
}

