package edu.uwec.cs.forstezt.gametree;

public class TestCase {
	
	public static void main(String[] args) {
//		char[] board = {'X', 'O', 'X',
//						'_', 'O', '_',
//				 		'O', 'X', '_'};
//		char[] board = {'X', 'O', '_',
//						'_', 'X', 'O',
//						'_', '_', 'O'};
		char[] board = {'X', 'O', 'O',
						'_', '_', '_',
						'X', 'X', 'O'};
		TicTacToeBoard state = new TicTacToeBoard(board, 3, true);

		System.out.println(state);
		System.out.println("Static Evaluation: " + state.staticEvaluation());
		System.out.println("----------------------------------------------------------------");
		
		//printTreeStates(state);
		MiniMax minimax = new MiniMax(8);
		System.out.println(minimax.generateNextMove(state));
		
	}
	
	private static void printTreeStates(TicTacToeBoard state) {
		while (state.hasMoreChildren()) {
			TicTacToeBoard child = (TicTacToeBoard)state.nextChild();
			System.out.println(child);
			System.out.println("Static Evaluation: " + child.staticEvaluation());
			System.out.println("----------------------------------------------------------------");
			printTreeStates(child);
		}
	}
	
}
