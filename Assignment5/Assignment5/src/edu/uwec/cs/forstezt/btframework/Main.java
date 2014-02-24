package edu.uwec.cs.forstezt.btframework;

import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		
//		BacktrackerIterative bt = new BacktrackerIterative();
//		
//		State s = new NQueensState(4);
//		
//		State result = bt.backtrack(s);
//		
//		System.out.println(result);
		
		BacktrackerIterative bt = new BacktrackerIterative(); 
		
		// Define the graph from the ppt 
		boolean[][] graph = 
			 	{{false, true, false, false, false, true}, 
				 { true, false, true, false, false, true}, 
				 {false, true, false, true, true, false}, 
				 {false, false, true, false, true, false}, 
				 {false, false, true, true, false, true}, 
				 {true, true, false, false, true, false}}; 
		
		//Define a full-connected 3-node graph
		boolean[][] graph2 = 
			 	{{true, true, true}, 
				 {true, true, true}, 
				 {true, true, true}}; 
		 
		// Define the colors used in the ppt 
		List<ColorWeightPair> colors = new ArrayList<ColorWeightPair>(); 
		colors.add(new ColorWeightPair("Red", 2)); 
		colors.add(new ColorWeightPair("Green", 3)); 
		colors.add(new ColorWeightPair("Blue", 5));
		colors.add(new ColorWeightPair("Yellow", 2));
		State s = new GraphColoringState(graph2, colors); 
		//State breadthResult = bt.backtrackBreadth(s);
		//State depthResult = bt.backtrackDepth(s);
		State bestResult = bt.backtrackBest(s);
	}

}
