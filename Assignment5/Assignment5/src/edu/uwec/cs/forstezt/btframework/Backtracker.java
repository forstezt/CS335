package edu.uwec.cs.forstezt.btframework;

import java.util.ArrayList;
import java.util.List;

public class Backtracker {
	
	public List<State> backtrack(State s) {
		
		List<State> result = new ArrayList<State>();
		
		if (s.isSolved()) {
			
			//System.out.println("Solved: " + s);
			result.add(s);
			
		} else {
			
			while(s.hasMoreChildren()) { //&& result.isEmpty()) {
				
				State child = s.nextChild();
				
				if (child.isFeasible()) {
					result.addAll(backtrack(child));
				}	
			}
		}
		return result;
	}
}
