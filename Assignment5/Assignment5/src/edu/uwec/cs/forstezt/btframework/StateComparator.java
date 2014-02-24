package edu.uwec.cs.forstezt.btframework;

import java.util.Comparator;

public class StateComparator implements Comparator<State>{

	@Override
	public int compare(State o1, State o2) {
		if (o1.getBound() > o2.getBound()) {
			return 1;
		} else if (o2.getBound() > o1.getBound()) {
			return -1;
		} else {
			if (o1.getLevel() < o2.getLevel()) {
				return 1;
			} else if (o1.getLevel() > o2.getLevel()) {
				return -1;
			} else {
				return 0;				
			}
		}
	}
	
}
