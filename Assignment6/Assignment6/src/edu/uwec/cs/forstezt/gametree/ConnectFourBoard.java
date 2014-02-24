package edu.uwec.cs.forstezt.gametree;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ConnectFourBoard implements TwoPlayerGameBoard {

	private static final int boardSize = 42;
	private static final int rowLength = 7;
	
	private char[] board;
	private int[] nextOpenValuesPerColumn;
	private int nextOpenPosition;
	private boolean isComputersTurn;
	
	//I precalculated every possible winning scenario
	//each number is an index into the board, and each set
	//of four numbers indicates a win for some player, if that
	//player has a piece at all four corresponding spots
	//on the board.  The following is the code that I used
	//to generate the scenarios:
	
	//for (int i = 0; i < 42; i++) {
	//	if (i % 7 < 4) {
	//		System.out.println("{" + i + ", " + (i + 1) + ", " + (i + 2) + ", " + (i + 3) + "},");	
	//	}
	//	if (i / 7 < 3) {
	//		System.out.println("{" + i + ", " + (i + 7) + ", " + (i + 14) + ", " + (i + 21) + "},");
	//	}
	//	if (i / 7 < 3 && i % 7 < 4) {
	//		System.out.println("{" + i + ", " + (i + 7 + 1) + ", " + (i + 14 + 2) + ", " + (i + 21 + 3) + "},");
	//	}
	//	if (i / 7 > 2 && i % 7 < 4) {
	//		System.out.println("{" + i + ", " + (i - 7 + 1) + ", " + (i - 14 + 2) + ", " + (i - 21 + 3) + "},");
	//	}
	//}
	private int[][] precomputedWinScenarios = {
			{0, 1, 2, 3},
			{0, 7, 14, 21},
			{0, 8, 16, 24},
			{1, 2, 3, 4},
			{1, 8, 15, 22},
			{1, 9, 17, 25},
			{2, 3, 4, 5},
			{2, 9, 16, 23},
			{2, 10, 18, 26},
			{3, 4, 5, 6},
			{3, 10, 17, 24},
			{3, 11, 19, 27},
			{4, 11, 18, 25},
			{5, 12, 19, 26},
			{6, 13, 20, 27},
			{7, 8, 9, 10},
			{7, 14, 21, 28},
			{7, 15, 23, 31},
			{8, 9, 10, 11},
			{8, 15, 22, 29},
			{8, 16, 24, 32},
			{9, 10, 11, 12},
			{9, 16, 23, 30},
			{9, 17, 25, 33},
			{10, 11, 12, 13},
			{10, 17, 24, 31},
			{10, 18, 26, 34},
			{11, 18, 25, 32},
			{12, 19, 26, 33},
			{13, 20, 27, 34},
			{14, 15, 16, 17},
			{14, 21, 28, 35},
			{14, 22, 30, 38},
			{15, 16, 17, 18},
			{15, 22, 29, 36},
			{15, 23, 31, 39},
			{16, 17, 18, 19},
			{16, 23, 30, 37},
			{16, 24, 32, 40},
			{17, 18, 19, 20},
			{17, 24, 31, 38},
			{17, 25, 33, 41},
			{18, 25, 32, 39},
			{19, 26, 33, 40},
			{20, 27, 34, 41},
			{21, 22, 23, 24},
			{21, 15, 9, 3},
			{22, 23, 24, 25},
			{22, 16, 10, 4},
			{23, 24, 25, 26},
			{23, 17, 11, 5},
			{24, 25, 26, 27},
			{24, 18, 12, 6},
			{28, 29, 30, 31},
			{28, 22, 16, 10},
			{29, 30, 31, 32},
			{29, 23, 17, 11},
			{30, 31, 32, 33},
			{30, 24, 18, 12},
			{31, 32, 33, 34},
			{31, 25, 19, 13},
			{35, 36, 37, 38},
			{35, 29, 23, 17},
			{36, 37, 38, 39},
			{36, 30, 24, 18},
			{37, 38, 39, 40},
			{37, 31, 25, 19},
			{38, 39, 40, 41},
			{38, 32, 26, 20}
	};
	
	public ConnectFourBoard() {
		this.board = new char[boardSize];

		for (int i = 0; i < boardSize; i++) {
			this.board[i] = '_';
		}
		
		this.nextOpenValuesPerColumn = new int[rowLength];
		for (int i = 0; i < rowLength; i++) {
			this.nextOpenValuesPerColumn[i] = boardSize / rowLength - 1;
		}
		
		this.nextOpenPosition = boardSize - rowLength;
		this.isComputersTurn = false;
	}
	
	/**
	 * Copy constructor for a TicTacToeBoard object
	 * @param orig
	 */
	public ConnectFourBoard(ConnectFourBoard orig) {
		this.board = new char[boardSize];
		for (int i = 0; i < boardSize; i++) {
			this.board[i] = orig.board[i];
		}
		
		this.nextOpenValuesPerColumn = new int[rowLength];
		for (int i = 0; i < rowLength; i++) {
			this.nextOpenValuesPerColumn[i] = orig.nextOpenValuesPerColumn[i];
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
		ConnectFourBoard child = new ConnectFourBoard(this);
		
		//if this is the computer's move, put an 'O' on the board
		//if this is the player's move, put an 'X' on the board
		child.board[this.nextOpenPosition] = (this.isComputersTurn) ? 'R' : 'B';
		int nextOpenCol = this.nextOpenPosition % rowLength;
		child.nextOpenValuesPerColumn[nextOpenCol]--;
		
		//set up to produce another child
		do {
			nextOpenCol++;
		} while (nextOpenCol < rowLength && this.nextOpenValuesPerColumn[nextOpenCol] == -1);		
		this.nextOpenPosition = (nextOpenCol != rowLength) ? this.nextOpenValuesPerColumn[nextOpenCol] * rowLength + nextOpenCol : boardSize;
		
		//set up the child
		child.isComputersTurn = !child.isComputersTurn;
		child.nextOpenPosition = boardSize - rowLength;
		findNextOpenPosition(child);
		
		return child;
	}
	
	private static int findNextOpenPosition(ConnectFourBoard state) {		
		int col = state.nextOpenPosition % rowLength;
		
		//find the column with the next open spot on the board
		while (col < rowLength && state.nextOpenValuesPerColumn[col] == -1) {
			col++;
		}
		
		//there are no open spots on the board
		if (col == rowLength) {
			state.nextOpenPosition = boardSize;
			
		//there is an open spot in column col	
		} else {
			state.nextOpenPosition = state.nextOpenValuesPerColumn[col] * rowLength + col;
		}
		
		return col;
	}

	/**
	 * Evaluates the board at the current state, returning -1 if the computer has lost, 1 if the computer has won, and 0 if there is a draw 
	 */
	@Override
	public double staticEvaluation() {
		boolean hasSomeoneWon = hasSomeoneWon();
		if (isComputersTurn && hasSomeoneWon) {
			return -1.0;
		} else if (!isComputersTurn && hasSomeoneWon) {
			return 1.0;
		} else {
			return 0.0;
		}
		//return 0.0;
	}

	/**
	 * Draws the connect four board on the screen
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.white);
		
		//draw the vertical lines
		for (int i = 0; i < rowLength - 1; i++) {
			g.drawLine(50 * i + 50, 0, 50 * i + 50, 300);
		}
		
		//draw the horizontal lines
		for (int i = 0; i < 5; i++) {
			g.drawLine(0, 50 * i + 50, 350, 50 * i + 50);
		}
		
		try {
			File blueImageFile = new File("blue.png");
			BufferedImage blueImage = ImageIO.read(blueImageFile);

			File redImageFile = new File("red.png");
			BufferedImage redImage = ImageIO.read(redImageFile);
			
			//runs through every square on the board, drawing red and blue discs on the screen when necessary
			for (int i = 0; i < board.length; i++) {
				if (board[i] == 'R') {
					g.drawImage(redImage, (i % rowLength) * 50 + 5, (i / rowLength) * 50 + 5, 40, 40, null);					
				} else if (board[i] == 'B') {
					g.drawImage(blueImage, (i % rowLength) * 50 + 5, (i / rowLength) * 50 + 5, 40, 40, null);
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
		int col = (int)mouseLocation.getX() / 50;
		
		//given the column in which the user clicked, find the spot into which the piece will drop
		int row = -1;
		int boardIndex = (row + 1) * rowLength + col;
		while (row < 5 && this.board[boardIndex] == '_') {
			row++;
			boardIndex = (row + 1) * rowLength + col;
		}
		boardIndex = row * rowLength + col;
				
		//throw an exception if row or col are not between 0 and 2, or if a move has already been made at the chosen square
		if (row > 5 || col >= rowLength || row < 0 || col < 0 || board[boardIndex] == 'B' || board[boardIndex] == 'R') {
			throw new Exception();
		}
		
		this.board[boardIndex] = 'B';
		
		this.isComputersTurn = !this.isComputersTurn;
		
		//indicate that this spot has been taken
		this.nextOpenValuesPerColumn[col]--;

		findNextOpenPosition(this);
	}
	
	/**
	 * Returns true if the computer has won, false otherwise
	 */
	@Override
	public boolean isComputerWinner() {
		return (!this.isComputersTurn && hasSomeoneWon());
	}
	
	/**
	 * Returns true if the computer and the player have tied, false otherwise
	 */
	@Override
	public boolean isDraw() {
		boolean isGameFinished = true;
		for (int i = 0; i < this.board.length && isGameFinished; i++) {
			if (this.board[i] == '_') {
				isGameFinished = false;
			}
		}
		return (isGameFinished && !hasSomeoneWon());
	}

	/**
	 * Returns true if the user has won, false otherwise
	 */
	@Override
	public boolean isUserWinner() {
		return (this.isComputersTurn && hasSomeoneWon());	
	}
	
	/**
	 * Creates a string representation of the TicTacToeBoard object
	 */
	public String toString() {
		String boardString = "";
		
		for (int i = 0; i < boardSize; i++) {
			boardString += board[i] + "\t";
			
			//we have reached the end of a row, go to a new line
			if ((i + 1) % rowLength == 0) {
				boardString += "\n";
			}
		}
		return boardString;
	}
	
	/**
	 * Returns true if one of the players has won the game, false otherwise
	 * @return whether or not the game has been won
	 */
	private boolean hasSomeoneWon() {
		boolean hasSomeoneWon = false;
		for (int scenario = 0; scenario < this.precomputedWinScenarios.length && !hasSomeoneWon; scenario++) {
			if (this.board[this.precomputedWinScenarios[scenario][0]] != '_' && 
				this.board[this.precomputedWinScenarios[scenario][0]] == this.board[this.precomputedWinScenarios[scenario][1]] && 
				this.board[this.precomputedWinScenarios[scenario][0]] == this.board[this.precomputedWinScenarios[scenario][2]] && 
				this.board[this.precomputedWinScenarios[scenario][0]] == this.board[this.precomputedWinScenarios[scenario][3]]) {
				hasSomeoneWon = true;
			}
		}
		return hasSomeoneWon;
	}

}
