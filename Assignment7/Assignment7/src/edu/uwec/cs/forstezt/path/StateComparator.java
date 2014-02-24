package edu.uwec.cs.forstezt.path;

import java.util.Comparator;

public class StateComparator implements Comparator<PathState>{

	/**
	 * Compares two PathState instances based on their bound estimates
	 */
	@Override
	public int compare(PathState o1, PathState o2) {
		if (o1.getBound() > o2.getBound()) {
			return 1;
		} else if (o2.getBound() > o1.getBound()) {
			return -1;
		} else {
			return 0;
		}
	}
	
}