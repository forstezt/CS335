package edu.uwec.cs.forstezt.btframework;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BacktrackerIterative {
	
	private int numberExpanded = 0;
	private boolean hasPrunedYet = false;
	
	/**
	 * Solves the graph coloring problem using a breadth-first search
	 * @param s - initial state
	 * @return the solution state
	 */
	public State backtrackBreadth(State s) {		
		State soln = null;		
		int bestSolnCost = Integer.MAX_VALUE;
		
		List<State> statesToProcess = new LinkedList<State>();
		statesToProcess.add(0, s);  // push
		
		while (!statesToProcess.isEmpty()) {
						
			//pop a feasible state from beginning of list
			State currentState = statesToProcess.remove(0);
			
			numberExpanded++;

			if (currentState.getBound() < bestSolnCost) {
				if (currentState.isSolved()) {
					bestSolnCost = currentState.getBound();
					soln = currentState;
				} else {				
					while(currentState.hasMoreChildren()) {
						State child = currentState.nextChild();			
						if (child.isFeasible()) {
							//add a feasible state to the end of the list
							statesToProcess.add(child);
						}
					}
				}
			}
		}
		System.out.println("Nodes Expanded: " + numberExpanded);
		System.out.println(soln);
		return soln;
	}
	
	/**
	 * Solves the graph coloring problem using a depth-first search
	 * @param s - initial state
	 * @return the solution state
	 */
	public State backtrackDepth(State s) {		
		State soln = null;		
		int bestSolnCost = Integer.MAX_VALUE;
		
		List<State> statesToProcess = new LinkedList<State>();
		statesToProcess.add(0, s);  // push
		
		while (!statesToProcess.isEmpty()) {
						
			//pop a feasible state from beginning of list
			State currentState = statesToProcess.remove(0);
			
			numberExpanded++;
			
			if (currentState.getBound() < bestSolnCost) {
				if (currentState.isSolved()) {				
					bestSolnCost = currentState.getBound();
					soln = currentState;
				} else {
					while(currentState.hasMoreChildren()) {
						State child = currentState.nextChild();			
						if (child.isFeasible()) {
							//add a feasible state to the beginning of the list
							statesToProcess.add(0, child);
						}
					}
				}	
			}
		}
		System.out.println("Nodes Expanded: " + numberExpanded);
		System.out.println(soln);
		return soln;
	}
	
	/**
	 * Solves the graph coloring problem using a best-first search
	 * @param s - initial state
	 * @return the solution state
	 */
	public State backtrackBest(State s) {
		
		State soln = null;
		
		int bestSolnCost = Integer.MAX_VALUE;
		
		List<State> statesToProcess = new LinkedList<State>();
		statesToProcess.add(0, s);  // push
		
		while (!statesToProcess.isEmpty() && hasPrunedYet == false) {
						
			//pop a feasible state from beginning of list
			State currentState = statesToProcess.remove(0);
			
			numberExpanded++;
			
			if (currentState.getBound() < bestSolnCost) {
				if (currentState.isSolved()) {
					bestSolnCost = currentState.getBound();
					soln = currentState;					
				} else {
					while(currentState.hasMoreChildren()) {
						State child = currentState.nextChild();					
						if (child.isFeasible()) {							
							//add a feasible state to the list in order by bound
							addInOrder(statesToProcess, child);
						}
					}
				}		//System.out.println(breadthResult); 
				//System.out.println(depthResult); 
			} else {
				hasPrunedYet = true;
			}
		}
		System.out.println("Nodes Expanded: " + numberExpanded);
		System.out.println(soln);
		return soln;
	}
	
	/**
	 * Adds a state in order into a list of states, ordered primarily by bound 
	 * and secondarily by level in the state space tree
	 * @param stateList - the list of states
	 * @param state - the state to be added into the list
	 */
	public static void addInOrder(List<State> stateList, State state) {
		StateComparator comparator = new StateComparator();
		
		//return the index at which the new state should be added in the list
		int index = Collections.binarySearch(stateList, state, comparator);
		
		//if the index is less than 0, then the state was not found in the list,
		//and you use the unary operator to procure the index at which the state
		//should be added in the list
		if (index < 0) {
			index = ~index;
		}
		
		stateList.add(index, state);
		
	}
}
