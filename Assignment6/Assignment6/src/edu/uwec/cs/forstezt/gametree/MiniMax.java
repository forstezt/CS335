package edu.uwec.cs.forstezt.gametree;

public class MiniMax {
	
	private int maxLevel;
	
	public MiniMax(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	
	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}
	
	/**
	 * Return the next most feasible move, based upon all of the moves that have been made thus far
	 * @param currentProblem - The state which represents all of the previous moves that have been made so far
	 * @return the next most feasible move
	 */
	public TwoPlayerGameBoard generateNextMove(TwoPlayerGameBoard currentProblem) {
		TwoPlayerGameBoard nextMove = null;
		TwoPlayerGameBoard child;
		double bestChildEval = -Double.MAX_VALUE;
		double childEval;
		
		//find the child with the highest minimax evaluation
		while (currentProblem.hasMoreChildren()) {
			child = currentProblem.nextChild();
			childEval = recursiveMiniMaxAlphaBeta(child, 1, -Double.MAX_VALUE, Double.MAX_VALUE);
			
			if (childEval > bestChildEval) {
				nextMove = child;
				bestChildEval = childEval;
			}
		}
		
		return nextMove;
	}
	
	/**
	 * Uses the recursive MiniMax algorithm to generate a numeric evaluation of a particular move based on all of its possible results
	 * @param currentProblem - The current move being evaluated
	 * @param currentLevel - The current level in the computational tree, with respect to the original move
	 * @param alpha - The lower bound on the current move's numerical evaluation
	 * @param beta - The upper bound on the current move's numerical evaluation
	 * @return the numeric evaluation of the current move
	 */
	private double recursiveMiniMaxAlphaBeta(TwoPlayerGameBoard currentProblem, int currentLevel, double alpha, double beta) {
		
		//we have searched as many levels down in the tree as possible, stop and static evaluate the node that we are currently on
		if (currentLevel == maxLevel || currentProblem.isDraw() || currentProblem.isComputerWinner() || currentProblem.isUserWinner()) {
			return currentProblem.staticEvaluation();
		}
				
		TwoPlayerGameBoard child;
		
		//we are at a maximizing level
		if (currentLevel % 2 == 0) {
			
			do {
				child = currentProblem.nextChild();
				alpha = Math.max(alpha, recursiveMiniMaxAlphaBeta(child, currentLevel + 1, alpha, beta));
			} while (currentProblem.hasMoreChildren() && alpha < beta); 
			
			return alpha;
			
		//we are at a minimizing level
		} else {
			
			do {
				child = currentProblem.nextChild();
				beta = Math.min(beta, recursiveMiniMaxAlphaBeta(child, currentLevel + 1, alpha, beta));
			} while (currentProblem.hasMoreChildren() && alpha < beta);
			
			return beta;
		}
	}
}
