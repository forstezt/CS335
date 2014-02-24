package edu.uwec.cs.forstezt.btframework;

public class NQueensState implements State {

	private int n;
	private int[] board;
	private int nextCol;
	private int nextRow;
	
	public NQueensState(int n) {
		this.n = n;
		this.board = new int[n];
		this.nextCol = 0;
		this.nextRow = 0;
	}
	
	// Copy constructor
	public NQueensState(NQueensState nqs) {
		this.n = nqs.n;
		// make a deep copy
		this.board = new int[n];
		for (int i=0; i<n; i++) {
			this.board[i] = nqs.board[i];
		}
		this.nextCol = nqs.nextCol;
		this.nextRow = nqs.nextRow;
	}
	
	@Override
	public boolean hasMoreChildren() {
		
		return (nextRow < n);
	}

	@Override
	public State nextChild() {
		
		// Copy myself
		NQueensState child = new NQueensState(this);
		
		// Modify the child's board
		child.board[this.nextCol] = this.nextRow;
		
		// Set myself up to produce more children
		this.nextRow++;
		
		// Setup the child so it can produce the correct children
		child.nextCol++;
		child.nextRow = 0;
		
		return child;
	}

	@Override
	public boolean isFeasible() {
		boolean feasible = true;
		
		int col = this.nextCol - 1;  // the position that I just filled in
		
		// Check to make sure state is feasible
		for (int j=0; j<col; j++) {
			
			// Check row
			if (board[j] == board[col]) {
				//System.out.println("not feasible - same row");
				feasible = false;
			}
			
			// Check diag
			if (((board[j] + (col-j)) == board[col]) ||
				((board[j] - (col-j)) == board[col])) {
				//System.out.println("not feasible - same diag");
				feasible = false;
			}	
		}
		
		return feasible;
	}

	@Override
	// Assuming it is feasible
	public boolean isSolved() {
		
		return (nextCol == n);
	}
	
	public int getBound() {
		return 0;
	}
	
	public int getLevel() {
		return 0;
	}
	
	public String toString() {
		
		String result = "<";
		
		for (int i=0; i<n; i++) {
			result += board[i] + " ";
		}
		result += ">";
		
		return result;
	}

}
