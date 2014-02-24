package edu.uwec.cs.forstezt.gametree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;



public class TicTacToeBoard implements TwoPlayerGameBoard{

	private static final int boardSize = 9;
	
	private char[] board;
	private int nextOpenPosition;
	private boolean isComputersTurn;
	
	/**
	 * Constructor for a TicTacToeBoard object
	 */
	public TicTacToeBoard() {
		//initialize the game board and fill it with spaces (which indicate that no one has made a move yet)
		this.board = new char[boardSize];
		for (int i = 0; i < boardSize; i++) {
			this.board[i] = '_';
		}
		
		this.nextOpenPosition = 0;
		this.isComputersTurn = false;
	}
	
	/**
	 * Testing constructor for a TicTacToeBoard object
	 */
	public TicTacToeBoard(char[] board, int nextOpenPosition, boolean isComputersTurn) {
		this.board = board;
		this.nextOpenPosition = nextOpenPosition;
		this.isComputersTurn = isComputersTurn;
	}

	/**
	 * Copy constructor for a TicTacToeBoard object
	 * @param orig
	 */
	public TicTacToeBoard(TicTacToeBoard orig) {
		this.board = new char[boardSize];
		for (int i = 0; i < boardSize; i++) {
			this.board[i] = orig.board[i];
		}
		this.nextOpenPosition = orig.nextOpenPosition;
		this.isComputersTurn = orig.isComputersTurn;
	}
	
	/**
	 * Returns true if this state has any unchecked children, false otherwise
	 */
	@Override
	public boolean hasMoreChildren() {
		return (nextOpenPosition < boardSize);
	}

	/**
	 * Returns the next unchecked child of this state
	 */
	@Override
	public TwoPlayerGameBoard nextChild() {
		TicTacToeBoard child = new TicTacToeBoard(this);
		
		//if this is the computer's move, put an 'O' on the board
		//if this is the player's move, put an 'X' on the board
		child.board[nextOpenPosition] = (isComputersTurn) ? 'X' : 'O';
		
		//set up to produce another child
		do {
			this.nextOpenPosition++;
		} while (this.nextOpenPosition < boardSize && this.board[this.nextOpenPosition] != '_');
		
		//set up the child
		child.isComputersTurn = !child.isComputersTurn;
		child.nextOpenPosition = 0;
		while (child.nextOpenPosition < boardSize && child.board[child.nextOpenPosition] != '_') {
			child.nextOpenPosition++;
		} 
		
		return child;
	}

	/**
	 * Evaluates the board at the current state, returning -1 if the computer has lost, 1 if the computer has won, and 0 if there is a draw 
	 */
	@Override
	public double staticEvaluation() {
		boolean hasSomeoneWon = hasVerticalWin() || hasHorizontalWin() || hasDiagonalWin();
		if (isComputersTurn && hasSomeoneWon) {
			return -1.0;
		} else if (!isComputersTurn && hasSomeoneWon) {
			return 1.0;
		} else {
			return 0.0;
		}
	}

	/**
	 * Draws the tic-tac-toe board on the screen
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		
		g.drawLine(100, 0, 100, 300);
		g.drawLine(200, 0, 200, 300);
		g.drawLine(0, 100, 300, 100);
		g.drawLine(0, 200, 300, 200);
		

		try {
			File xImageFile = new File("X.png");
			BufferedImage xImage = ImageIO.read(xImageFile);

			File oImageFile = new File("O.png");
			BufferedImage oImage = ImageIO.read(oImageFile);
			
			//runs through every square on the board, drawing x's and o's on the screen when necessary
			for (int i = 0; i < board.length; i++) {
				if (board[i] == 'X') {
					g.drawImage(xImage, (i % 3) * 100 + 5, (i / 3) * 100 + 5, 90, 90, null);					
				} else if (board[i] == 'O') {
					g.drawImage(oImage, (i % 3) * 100 + 5, (i / 3) * 100 + 5, 90, 90, null);
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Makes the user's move, determined by the position at which the user clicks
	 */
	@Override
	public void placeUserMove(Point2D mouseLocation) throws Exception {
		int row = (int)mouseLocation.getY() / 100;
		int col = (int)mouseLocation.getX() / 100;
		
		int boardIndex = row * 3 + col;
		
		//throw an exception if row or col are not between 0 and 2, or if a move has already been made at the chosen square
		if (row > 2 || col > 2 || row < 0 || col < 0 || board[boardIndex] == 'O' || board[boardIndex] == 'X') {
			throw new Exception();
		}
		
		this.board[boardIndex] = 'O';
		
		this.isComputersTurn = !this.isComputersTurn;
		
		this.nextOpenPosition = 0;
		while (this.nextOpenPosition < boardSize && this.board[this.nextOpenPosition] != '_') {
			this.nextOpenPosition++;
		} 
	}

	/**
	 * Returns true if the computer has won, false otherwise
	 */
	@Override
	public boolean isComputerWinner() {
		return (!this.isComputersTurn && (hasVerticalWin() || hasHorizontalWin() || hasDiagonalWin()));
	}

	/**
	 * Returns true if the computer and the player have tied, false otherwise
	 */
	@Override
	public boolean isDraw() {
		boolean isGameFinished = true;
		for (int i = 0; i < this.board.length; i++) {
			if (this.board[i] == '_') {
				isGameFinished = false;
			}
		}
		return (isGameFinished && !hasVerticalWin() && !hasHorizontalWin() && !hasDiagonalWin());
	}

	/**
	 * Returns true if the user has won, false otherwise
	 */
	@Override
	public boolean isUserWinner() {
		return (this.isComputersTurn && (hasVerticalWin() || hasHorizontalWin() || hasDiagonalWin()));	
	}

	/**
	 * Creates a string representation of the TicTacToeBoard object
	 */
	public String toString() {
		String boardString = "";
		int rowLen = (int)Math.sqrt(boardSize);
		
		for (int i = 0; i < boardSize; i++) {
			boardString += board[i] + "\t";
			
			//we have reached the end of a row, go to a new line
			if ((i + 1) % rowLen == 0) {
				boardString += "\n";
			}
		}
		return boardString;
	}
	
	/**
	 * Returns true if one of the players has a vertical tic-tac-toe
	 * @return
	 */
	private boolean hasVerticalWin() {
		boolean hasWon = false;
		int rowLen = (int)Math.sqrt(boardSize);
		char top, middle, bottom;

		//march through each column of the board and check to see if a player has all three squares
		for (int i = 0; i < rowLen; i++) {
			top = this.board[i];
			middle = this.board[i + rowLen];
			bottom = this.board[i + 2 * rowLen];
			if (top != '_' && middle == top && bottom == top) {
				hasWon = true;
			}
		}
		return hasWon;
	}
	
	/**
	 * Returns true if one of the players has a horizontal tic-tac-toe
	 * @return
	 */
	private boolean hasHorizontalWin() {
		boolean hasWon = false;
		int rowLen = (int)Math.sqrt(boardSize);
		char left, middle, right;
		
		//march through each row of the board and check to see if a player has all three squares
		for (int i = 0; i < rowLen; i++) {
			left = this.board[i * rowLen];
			middle = this.board[i * rowLen + 1];
			right = this.board[i * rowLen + 2];
			if (left != '_' && middle == left && right == left) {
				hasWon = true;
			}
		}
		return hasWon;		
	}
	
	/**
	 * Returns true if one of the players has a diagonal tic-tac-toe
	 * @return
	 */
	private boolean hasDiagonalWin() {
		boolean hasWon = false;

		char upperLeft = this.board[0];
		char upperRight = this.board[2];
		char center = this.board[4];
		char lowerLeft = this.board[6];
		char lowerRight = this.board[8];
		
		//check to see if a player has either diagonal
		if ((upperLeft != '_' && upperLeft == center && upperLeft == lowerRight) || 
			(upperRight != '_' && upperRight == center && upperRight == lowerLeft)) {
			hasWon = true;
		}
		
		return hasWon;
	}

}
