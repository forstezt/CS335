package edu.uwec.cs.forstezt.gametree;

public class ComputerThinkingThread implements Runnable{

	private GamePlayer player;
	private boolean proceed;
	
	/**
	 * Constructor
	 * @param player - The GamePlayer being updated by this thread
	 */
	public ComputerThinkingThread(GamePlayer player) {
		this.player = player;
		this.proceed = true;
	}
	
	/**
	 * Cancels the progress of this thread
	 * The next board will still be generated, but the GamePlayer will not be told about it
	 */
	public void cancel() {
		this.proceed = false;
	}
	
	@Override
	public void run() {
		TwoPlayerGameBoard nextBoard = this.player.getMiniMax().generateNextMove(this.player.getBoard());
		
		if (this.proceed) {
			player.computerDone(nextBoard);
		}
	}

}
