package edu.uwec.cs.forstezt.shortestpath;

import java.util.ArrayList;
import java.util.List;

public class ShortestPath {

	private int[][] adj;
	private int[][] backtrackMatrix;
	
	public ShortestPath(int[][] adj) {
		this.adj = adj;
		
		//initialize all cells in the backtracking matrix to -1
		backtrackMatrix = new int[adj.length][adj.length];
		for (int row = 0; row < adj.length; row++) {
			for (int col = 0; col < adj.length; col++) {
				backtrackMatrix[row][col] = -1;
			}
		}
		
		completeAdjMatrix();
	}	
	
	/**
	 * Fills out adjacency matrix and builds up a second matrix used for backtracking
	 */
	private void completeAdjMatrix() {
		for (int k = 0; k < adj.length; k++) {
			for (int row = 0; row < adj.length; row++) {
				for (int col = 0; col < adj.length; col++) {
					//calculate the length of the path including the new intermediate node
					int intermediatePath = (adj[row][k] + adj[k][col] > 0) ? adj[row][k] + adj[k][col] : Integer.MAX_VALUE;
					
					if (intermediatePath < adj[row][col]) { //the intermediate node allows us to take a shorter path than the current one
						//update the backtracking matrix and the adjacency matrix
						backtrackMatrix[row][col] = k;
						adj[row][col] = intermediatePath;
					}				
				}
			}
		}
	}
	
	/**
	 * Creates a string representing the adjacency matrix
	 */
	public String toString() {
		String s = "";
		for (int row = 0; row < adj.length; row++) {
			for (int col = 0; col < adj.length; col++) {
				s += adj[row][col] + "\t";
			}
			s += "\n";
		}
		return s;
	}
	
	/**
	 * Returns the shortest distance from one node to another
	 * @param firstNode - the starting node
	 * @param lastNode - the ending node
	 * @return the distance
	 */
	public int shortestDist(int firstNode, int lastNode) {
		return adj[firstNode][lastNode];
	}
	
	/**
	 * Returns each node touched when taking the shortest path from one node to another
	 * @param firstNode - the starting node
	 * @param lastNode - the ending node
	 * @return the intermediate nodes
	 */
	public List<Integer> shortestPath(int firstNode, int lastNode) {
		 List<Integer> shortestPath = new ArrayList<Integer>();

		 int intermediateRow;
		 int row = firstNode;
		 
		 //add all of the intermediate nodes touched when traveling the shortest path from firstNode to lastNode
		 do {
			 shortestPath.add(row);
			 intermediateRow = backtrackMatrix[row][lastNode];
			 row = intermediateRow;
		 } while(intermediateRow != -1);
		 
		 //add the final node
		 shortestPath.add(lastNode);
		 
		 return shortestPath;
	}
	
	public static void main(String[] args) { 
		// i<-->i has cost of 0 
		// if no path then cost of infinity 
		int x = Integer.MAX_VALUE; 
		int[][] adj = {
				 {0, 3, 8, x, 4}, 
				 {x, 0, x, 1, 7}, 
				 {x, 4, 0, x, x}, 
				 {2, x, 5, 0, x}, 
				 {x, x, x, 6, 0}};
		 
		 ShortestPath sp = new ShortestPath(adj); 
		 System.out.println(sp); 
		 /* Should produce: 
			0	3	8	4	4 
		 	3 	0 	6 	1 	7 
		 	7 	4 	0 	5 	11 
		 	2 	5 	5 	0 	6 
		 	8 	11	11 	6 	0 
		 */ 
		 
		 int s = sp.shortestDist(4, 1); 
		 System.out.println(s); 
		 // Should produce: 11 
		 
		 List<Integer> p = sp.shortestPath(4, 1); 
		 System.out.println(p); 
		 // Should produce: [4, 3, 0, 1] 
	} 
}