package edu.uwec.cs.forstezt.path;

import java.awt.Point;
import java.util.*;

public class PathFinder {

	private TerrainMap tm;
	private TerrainPanel tp;

	public PathFinder(TerrainMap tm, TerrainPanel tp) {
		this.tm = tm;
		this.tp = tp;
	}


	// The A* algorithm
	public List<Point> findPath(Point start, Point end) {
				
		PathState soln = null;
		
		int bestSolnCost = Integer.MAX_VALUE;
		boolean hasPrunedYet = false;
		
		List<PathState> statesToProcess = new ArrayList<PathState>();
		statesToProcess.add(0, new PathState(start, end, tm));
		
		while (!statesToProcess.isEmpty() && !hasPrunedYet) {
						
			//pop a feasible state from beginning of list
			PathState currentState = statesToProcess.remove(0);
			
			//draw the state that's being expanded on the screen
			tp.addAstarState(currentState);
			tp.repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
						
			if (currentState.getBound() < bestSolnCost) {
				if (currentState.isGoal()) {
					bestSolnCost = currentState.getBound();
					soln = currentState;					
				} else {
					PathState child;
					//add every feasible child of the current state to the list in order
					while((child = currentState.getNextChild()) != null) {
						//add a feasible state to the list in order by bound
						addInOrder(statesToProcess, child);
					}
				}		
			} else {
				hasPrunedYet = true;
			}
		}
		return soln.getPath();
	}
	
	/**
	 * Adds a state in order into a list of states, ordered primarily by bound 
	 * and secondarily by level in the state space tree
	 * @param stateList - the list of states
	 * @param state - the state to be added into the list
	 */
	public static void addInOrder(List<PathState> stateList, PathState state) {
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

