package edu.uwec.cs.forstezt.btframework;

import java.util.ArrayList;
import java.util.List;

public class GraphColoringState implements State {

	private boolean[][] graph;
	private int[] nodeColors;
	private List<ColorWeightPair> colorWeights;
	private int nextNode;
	private int nextColor;
	private int leastWeight;
	
	/**
	 * Constructor for a GraphColoringState
	 * @param graph - the adjacency graph
	 * @param colorWeights - a list of viable colors, paired with their respective weights
	 */
	public GraphColoringState(boolean[][] graph, List<ColorWeightPair> colorWeights) {
		this.graph = graph;
		
		this.nodeColors = new int[graph.length];
		for (int i = 0; i < this.nodeColors.length; i++) {
			this.nodeColors[i] = -1;
		}
		
		this.colorWeights = colorWeights;
		this.leastWeight = Integer.MAX_VALUE;
		for (ColorWeightPair colorWeightPair : colorWeights) {
			if (colorWeightPair.weight < leastWeight) {
				leastWeight = colorWeightPair.weight;
			}
		}
				
		this.nextNode = 0;
		this.nextColor = 0;
	}
	
	/**
	 * A copy constructor for the GraphColoringState class
	 * Deep copies are made of all objects and arrays
	 * @param orig - the original GraphColoringState, from which this copy is being made
	 */
	public GraphColoringState(GraphColoringState orig) {
		this.graph = new boolean[orig.graph.length][orig.graph.length];
		for (int i = 0; i < orig.graph.length; i++) {
			for (int j = 0; j < orig.graph.length; j++) {
				this.graph[i][j] = orig.graph[i][j];
			}
		}
		
		this.nodeColors = new int[orig.nodeColors.length];
		for (int i = 0; i < orig.nodeColors.length; i++) {
			this.nodeColors[i] = orig.nodeColors[i];
		}
		
		this.colorWeights = new ArrayList<ColorWeightPair>();
		for (ColorWeightPair colorWeightPair : orig.colorWeights) {
			this.colorWeights.add(colorWeightPair);
		}
		
		this.leastWeight = orig.leastWeight;
		this.nextNode = orig.nextNode;
		this.nextColor = orig.nextColor;
	}
	
	/**
	 * Returns true if this state has any unchecked children, false otherwise
	 */
	@Override
	public boolean hasMoreChildren() {
		return (this.nextColor < this.colorWeights.size());
	}

	/**
	 * Returns the next unchecked child of this state
	 */
	@Override
	public State nextChild() {
		GraphColoringState child = new GraphColoringState(this);
		
		//set the color of the child
		child.nodeColors[this.nextNode] = this.nextColor;
		
		//set up to produce another child
		this.nextColor++;
		
		//set up the child
		child.nextNode++;
		child.nextColor = 0;
		
		return child;
	}

	/**
	 * Returns true if the current state is feasible, false otherwise
	 */
	@Override
	public boolean isFeasible() {
		boolean isFeasible = true;
		int currNode = this.nextNode - 1;
		for(int node = 0; node < this.graph.length; node++) {
			//the current node is connected to another with the same color (not itself)
			if (node != currNode && this.graph[currNode][node] == true && this.nodeColors[currNode] == this.nodeColors[node]) {
				isFeasible =  false;
			}
		}
		return isFeasible;
	}

	/**
	 * Returns true if the current state is solved, false otherwise
	 */
	@Override
	public boolean isSolved() {
		return (this.nextNode == this.graph.length);
	}

	/**
	 * Returns the bound for this state
	 */
	@Override
	public int getBound() {
		int bound = 0;
		int currNode = this.nextNode - 1;
		
		//add up the weights of every node whose color has been determined
		for (int node = currNode; node >= 0; node--) {
			bound += this.colorWeights.get(this.nodeColors[node]).weight;
		}
		
		//add a lower bound on the sum of the weights of every node whose color
		//has not been determined (assume every one has the color with the least weight)
		bound += (this.nodeColors.length - this.nextNode) * this.leastWeight;
		
		return bound;
	}
	
	/**
	 * Returns the level of the state space tree at which this state is located
	 * Used for ordering states correctly with best first search
	 */
	public int getLevel() {
		return this.nextNode;
	}

	/**
	 * Returns a string representation of the current state
	 */
	public String toString() {	
		String result = "<";
		
		for (int nodeColorIndex : nodeColors) {
			if (nodeColorIndex == -1) { //if no color has yet been assigned to a particular node, mark it with "null"
				result += "null ";
			} else { //add the current node's assigned color to the string
				result += this.colorWeights.get(nodeColorIndex).color + " ";
			}
		}
		result += ">";
		
		return result;
	}
	
}
