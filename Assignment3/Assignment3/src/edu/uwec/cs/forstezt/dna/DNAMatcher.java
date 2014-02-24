package edu.uwec.cs.forstezt.dna;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class DNAMatcher {
	
	private Map<String, Integer> matchCost;
	
	public DNAMatcher(Map<String, Integer> matchCost){
		this.matchCost = matchCost;
	}
	
	/**
	 * Finds the optimal local character alignment of 2 strings
	 * @param string1 - one of the 2 strings being aligned
	 * @param string2 - one of the 2 strings being aligned
	 * @return a LocalAlignment object containing the character alignment information
	 */
	public LocalAlignment findLocalAlignment(String string1, String string2){
		int[][] table = computeOptimalAlignmentTable(string1, string2);
		return generateAlignmentFromTable(table, string1, string2);
	}
	
	/**
	 * Creates a table of optimal alignment weights for every possible alignment of substrings of 2 strings
	 * @param string1 - one of the 2 strings being aligned
	 * @param string2 - one of the 2 substrings being aligned
	 * @return the table
	 */
	private int[][] computeOptimalAlignmentTable(String string1, String string2){
		
		//create a list of threads
		List<MyThread> threads = new ArrayList<MyThread>();
		
		//create a table to hold the optimal alignment weights of every possible alignment of substrings of the 2 strings
		//since we include the empty character at the beginning of each string, the table's height and width are one more than the lengths of string 1 and string 2
		int[][] table = new int[string2.length() + 1][string1.length() + 1];
		
		//create a table of semaphores, each one corresponding to the same cell in the table of optimal alignment weights
		//none of the semaphores should release any tickets upon creation
		Semaphore[][] semaphores = new Semaphore[string2.length() + 1][string1.length() + 1];
		for (int row = 0; row < string2.length() + 1; row++){
			for (int col = 0; col < string1.length() + 1; col++){
				semaphores[row][col] = new Semaphore(0);
			}
		}
		
		//the cumulative weight of aligning the empty string and any substring of string 2 is 0
		//also, we should release 3 tickets from each of these semaphores, now that the corresponding weights are calculated
		for (int row = 0; row < string2.length() + 1; row++) {
			table[row][0] = 0;
			semaphores[row][0].release(3);
		}
		
		//the cumulative weight of aligning the empty string and any substring of string 1 is 0		
		//also, we should release 3 tickets from each of these semaphores, now that the corresponding weights are calculated
		for (int col = 0; col < string1.length() + 1; col++) {
			table[0][col] = 0;
			semaphores[0][col].release(3);
		}
		
		//start a new thread to calculate each weight in the table, excluding the first row and column, which we already calculated
		for (int row = 1; row < string2.length() + 1; row++) {
			for (int col = 1; col < string1.length() + 1; col++) {				
				MyThread thread = new MyThread(semaphores, table, matchCost, string1, string2, row, col);
				threads.add(thread);
				
			}
		}
	
		//wait until every thread is finished before returning the completed table
		for (int t = 0; t < threads.size(); t++) {	
			try {
				threads.get(t).thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//printTable(table, string1, string2);
		
		return table;
	}
	
	/**
	 * Prints the table of alignment weights to the console for debugging purposes
	 * @param table - the table to be printed
	 * @param string1 - the string corresponding to the table's columns
	 * @param string2 - the string corresponding to the table's rows
	 */
	private void printTable(int[][] table, String string1, String string2) {
		
		//label the top axis of the table
		System.out.print("\t*\t");
		for (int i = 0; i < string1.length(); i++) {
			System.out.print("" + string1.charAt(i) + "\t");
		}
		System.out.println();
		System.out.print("\t");
		for (int col = 0; col < string1.length() + 1; col++) {
			System.out.print("_______\t");
		}
		System.out.println();
		
		//print all of the weights
		for (int row = 0; row < string2.length() + 1; row++){
			if (row == 0) {
				System.out.print("*\t|");
			} else {
				System.out.print("" + string2.charAt(row - 1) + "\t|");	
			}
			for (int col = 0; col < string1.length() + 1; col++){
				System.out.print(table[row][col] + "\t");
			}
			System.out.println("");
		}
	}
	
	/**
	 * Generates a LocalAlignment object from the table of alignment weights
	 * @param table - the table of alignment weights
	 * @param string1 - the string corresponding to the table's columns
	 * @param string2 - the string corresponding to the table's rows
	 * @return the LocalAlignment object
	 */
	private LocalAlignment generateAlignmentFromTable(int[][] table, String string1, String string2){
		
		int string1MatchStartIndex;
		int string2MatchStartIndex;
		int matchLength = 0;
		
		int maxValue = Integer.MIN_VALUE;
		
		int currRow = 0;
		int currColumn = 0;
		
		//find one of the maximum values in the table of alignment weights (doesn't matter if there are duplicates)
		for (int row = 0; row < string2.length() + 1; row++){
			for (int col = 0; col < string1.length() + 1; col++){
				if (table[row][col] > maxValue) {
					maxValue = table[row][col];
					currRow = row;
					currColumn = col;
				}
			}
		}
		
		int currValue = table[currRow][currColumn];
		int leftValue;
		int diagonalValue;
		String stringA = "";
		String stringB = "";
		
		//add junk suffix to string2
		for (int row = currRow + 1; row < string2.length() + 1; row++) {
			stringB += string2.charAt(row - 1);
		}
		
		//add junk suffix to string1
		for (int col = currColumn + 1; col < string1.length() + 1; col++) {
			stringA += string1.charAt(col - 1);
		}
		
		//trace back to any cell with an alignment weight of 0 from the cell of highest weight in the table
		while(currValue != 0) { 
			
			leftValue = (currColumn > 0) ? table[currRow][currColumn - 1] : 0;
			diagonalValue = (currRow > 0 && currColumn > 0) ? table[currRow - 1][currColumn - 1] : 0;
						
			if (currValue == diagonalValue + matchCost.get("" + string1.charAt(currColumn - 1) + string2.charAt(currRow - 1))) { //match the current character in string 1 with the current character in string 2
				stringA = "" + string1.charAt(currColumn - 1) + stringA;
				stringB = "" + string2.charAt(currRow - 1) + stringB;
				currColumn--;
				currRow--;
			} else if (currValue == leftValue + matchCost.get("-" + string1.charAt(currColumn - 1))) { //match the current character in string 1 with a blank space
				stringA = "" + string1.charAt(currColumn - 1) + stringA;
				stringB = "-" + stringB;
				currColumn--;
			} else { //match a blank space with the current character in string 2
				stringA = "-" + stringA;
				stringB = "" + string2.charAt(currRow - 1) + stringB;
				currRow--;
			}
			
			matchLength++;
			
			currValue = table[currRow][currColumn];
		}
		
		string1MatchStartIndex = currColumn;
		string2MatchStartIndex = currRow;
		
		//add junk prefix to string2
		for (int row = currRow; row >= 1; row--) {
			stringB = string2.charAt(row - 1) + stringB;
		}
		
		//add junk prefix to string1
		for (int col = currColumn; col >= 1; col--) {
			stringA = string1.charAt(col - 1) + stringA;
		}
				
		return new LocalAlignment(stringA, stringB, string1MatchStartIndex, string2MatchStartIndex, matchLength);
	}
	
}
