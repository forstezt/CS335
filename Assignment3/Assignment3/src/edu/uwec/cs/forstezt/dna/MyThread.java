package edu.uwec.cs.forstezt.dna;

import java.util.Map;
import java.util.concurrent.Semaphore;

public class MyThread implements Runnable{

	public Thread thread;
	private Semaphore[][] semaphores;
	private int[][] table;
	private Map<String, Integer> matchCost;
	private String string1;
	private String string2;
	private int row;
	private int col;
	
		
	public MyThread(Semaphore[][] semaphores, int[][] table, Map<String, Integer> matchCost, String string1, String string2, int row, int col) {
		
		this.semaphores = semaphores;
		this.table = table;
		this.matchCost = matchCost;
		this.string1 = string1;
		this.string2 = string2;
		this.row = row;
		this.col = col;
		
		thread = new Thread(this);
		thread.start();
		
	}
	
	@Override
	public void run() {		
		
		//wait for values to the left, above, and to the upper left of this cell
		//to be filled in before using them to calculate this cell's value
		//then, when each required cell's value has been calculated, read them and
		//store the alignment weights for the cells to the left, above, and to the 
		//upper left of this cell
		
		//get the weight value to the upper-left, if possible
		try {
			semaphores[row - 1][col - 1].acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int diagonal = table[row - 1][col - 1];

		//get the weight value from above, if possible
		try {
			semaphores[row - 1][col].acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int top = table[row - 1][col];
		
		//get the weight value to the left, if possible
		try {
			semaphores[row][col - 1].acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int left = table[row][col - 1];
		
		//calculate the possible cumulative weight values following from the following 3 scenarios:
		//			1) matching the current character from string 1 with the current character from string 2
		//			2) matching the current character from string 1 with a blank space
		//			3) matching a blank space with the current character from string 2
		int endToEndCost = matchCost.get("" + string1.charAt(col - 1) + string2.charAt(row - 1)) + diagonal;
		int endToBlankCost = matchCost.get(string1.charAt(col - 1) + "-") + left;
		int blankToEndCost = matchCost.get(string2.charAt(row - 1) + "-") + top;
		
		//take the highest possible cumulative weight value
		table[row][col] = Math.max(endToEndCost, Math.max(blankToEndCost, Math.max(endToBlankCost, 0)));
		
		//release 3 tickets (for the cells to the right, below, and to the lower right of this one to acquire)
		semaphores[row][col].release(3);
		
	}

}

