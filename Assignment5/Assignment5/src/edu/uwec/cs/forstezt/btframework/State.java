package edu.uwec.cs.forstezt.btframework;

public interface State {

	public boolean hasMoreChildren();
	public State nextChild();
	public boolean isFeasible();
	public boolean isSolved();
	public int getBound();
	public int getLevel();
	
}
