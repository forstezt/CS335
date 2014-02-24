package edu.uwec.cs.forstezt.gametree;

import java.awt.Graphics;
import java.awt.geom.Point2D;

public interface TwoPlayerGameBoard {
	// Methods used in MiniMax 
	public boolean hasMoreChildren(); // returns true if the board has more children 
	public TwoPlayerGameBoard nextChild(); // returns the next feasible child board (note that we have 
	 // combined nextChild and isFeasible into this method) 
	public double staticEvaluation(); // returns a static evaluation of the current board (-1 loss, +1 win, 0 draw) 
	 
	 
	// Methods used for drawing 
	public void draw(Graphics g); // draws the game board on the given graphics object 
	 
	 
	// Methods used for game play 
	public boolean isComputerWinner(); // returns true if the computer is a winner 
	public boolean isDraw(); // returns true if the game is a draw 
	public boolean isUserWinner(); // returns true if the user is a winner 
	 
	 
	// Methods used for handling user input 
	public void placeUserMove(Point2D mouseLocation) throws Exception; // mutates the current problem to 
	// place a user's piece on a particular location on the board indicated by the user clicking with their 
	// mouse. If the user's click was an invalid position, the method will throw an Exception (this 
	// should probably be a specific type of exception, but a generic Exception will do for now). 
	// Note that this method will also need to change any instance variables in the class to indicate that 
	// it is now the computer's turn. 
	
}
