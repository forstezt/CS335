package edu.uwec.cs.forstezt.gametree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

public class BoardPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private TwoPlayerGameBoard board = null;
	
	/**
	 * Display a new game board
	 * @param newBoard
	 */
	public void setBoard(TwoPlayerGameBoard newBoard) {
		this.board = newBoard;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		if (this.board != null) {
			
			Font messageFont = new Font("Serif", Font.BOLD, 30);
			
			//the user and the computer have tied
			if (this.board.isDraw()) {
				g.setColor(Color.WHITE);
				g.setFont(messageFont);
				g.drawString("It's a draw!", 75, 350);
				this.board.draw(g);
			
			//the computer has won the game
			} else if (this.board.isComputerWinner()) {
				g.setColor(Color.white);
				g.setFont(messageFont);
				g.drawString("You have lost.", 75, 350);
				this.board.draw(g);
				
			//the user has won the game
			} else if (this.board.isUserWinner()) {
				g.setColor(Color.WHITE);
				g.setFont(messageFont);
				g.drawString("You have won!", 75, 350);
				this.board.draw(g);
				
			//the game is still in progress
			} else {
				g.setColor(Color.WHITE);
				g.setFont(messageFont);
				g.drawString("The game is still in progress...", 75, 350);
				this.board.draw(g);		
			}
		}
	}
}
