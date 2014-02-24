package edu.uwec.cs.forstezt.gametree;

import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePlayer extends JFrame {

	// ***************************************************************************
	// INSTANCE VARIABLES
	// ***************************************************************************

	// Variables use in construction of the GUI
	private JPanel ivjJFrameContentPane = null;
	private JPanel ivjGamePlayerPane = null;
	private BoardPanel ivjBoardPanel1 = null;

	private JMenuBar ivjGamePlayerJMenuBar = null;

	private JMenu ivjGameMenu = null;
	private JMenuItem ivjTicTacToeMenuItem = null;
	private JMenuItem ivjConnectFourMenuItem = null;

	private JMenu ivjDifficultyMenu = null;
	private JRadioButtonMenuItem ivjEasyButton = null;
	private JRadioButtonMenuItem ivjHardButton = null;
	private JRadioButtonMenuItem ivjMediumButton = null;

	// Other instance variables you need to add to make the game work
	private TwoPlayerGameBoard board = null;
	private MiniMax minimax = new MiniMax(2);
	private boolean readyForNextMove = true;
	private ComputerThinkingThread computerMove = null;
	
	// ***************************************************************************
	// CONSTRUCTORS
	// ***************************************************************************

	public GamePlayer() {
		super();
		initialize();
	}

	public GamePlayer(String title) {
		super(title);
	}

	// ***************************************************************************
	// EXTRA METHODS YOU NEED TO ADD TO THIS CLASS
	// ***************************************************************************
	public void computerDone(TwoPlayerGameBoard computerMove) {
		this.board = computerMove;
		getBoardPanel1().setBoard(this.board);
		this.readyForNextMove = true;
		
	}
	
	public MiniMax getMiniMax() {
		return this.minimax;
	}
	
	public TwoPlayerGameBoard getBoard() {
		return this.board;
	}

	
	// ***************************************************************************
	// EVENT HANDLERS FOR THE GUI PIECES
	// YOU NEED TO FILL IN MOST THE IMPLEMENTATION
	// ***************************************************************************

	public void boardPanel1_MouseReleased(java.awt.event.MouseEvent mouseEvent) {
		if (this.readyForNextMove && !this.board.isDraw() && !this.board.isComputerWinner() && !this.board.isUserWinner()) {
			try {
				this.board.placeUserMove(mouseEvent.getPoint());
				getBoardPanel1().setBoard(this.board);
				if (!this.board.isDraw() && !this.board.isComputerWinner() && !this.board.isUserWinner()) {
					this.readyForNextMove = false;
					this.computerMove = new ComputerThinkingThread(this);
					(new Thread(this.computerMove)).start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	
	public void ticTacToeMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		//if a thread is running, cancel it
		if (!this.readyForNextMove) {
			this.computerMove.cancel();
		}
		
		this.board = new TicTacToeBoard();
		getBoardPanel1().setBoard(this.board);	
	}

	public void connectFourMenuItem_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		//if a thread is running, cancel it
		if (!this.readyForNextMove) {
			this.computerMove.cancel();
		}
		
		this.board = new ConnectFourBoard();
		getBoardPanel1().setBoard(this.board);
	
	}

	public void easyButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (this.readyForNextMove) {
			this.minimax.setMaxLevel(2);
		}
	}

	public void hardButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (this.readyForNextMove) {
			this.minimax.setMaxLevel(8);
		}
	}

	public void mediumButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
		if (this.readyForNextMove) {
			this.minimax.setMaxLevel(5);
		}
	}

	// ***************************************************************************
	// METHODS TO SETUP THE VARIOUS GUI PIECES
	// ***************************************************************************

	/**
		 * Return the JFrameContentPane property value.
		 * @return javax.swing.JPanel
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJFrameContentPane() {
		if (ivjJFrameContentPane == null) {
			try {
				ivjJFrameContentPane = new javax.swing.JPanel();
				ivjJFrameContentPane.setName("JFrameContentPane");
				ivjJFrameContentPane.setLayout(new java.awt.BorderLayout());
				getJFrameContentPane().add(getGamePlayerPane(), "Center");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJFrameContentPane;
	}

	/**
	 * Return the GamePlayerPane property value.
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getGamePlayerPane() {
		if (ivjGamePlayerPane == null) {
			try {
				ivjGamePlayerPane = new javax.swing.JPanel();
				ivjGamePlayerPane.setName("GamePlayerPane");
				ivjGamePlayerPane.setLayout(null);
				ivjGamePlayerPane.setBackground(java.awt.Color.black);
				getGamePlayerPane().add(getBoardPanel1(), getBoardPanel1().getName());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjGamePlayerPane;
	}

	/**
	 * Return the BoardPanel1 property value.
	 * @return BoardPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private BoardPanel getBoardPanel1() {
		if (ivjBoardPanel1 == null) {
			try {
				ivjBoardPanel1 = new BoardPanel();
				ivjBoardPanel1.setName("BoardPanel1");
				ivjBoardPanel1.setBackground(java.awt.Color.black);
				ivjBoardPanel1.setBounds(31, 23, 701, 601);
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjBoardPanel1;
	}

	/**
	 * Return the GamePlayerJMenuBar property value.
	 * @return javax.swing.JMenuBar
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuBar getGamePlayerJMenuBar() {
		if (ivjGamePlayerJMenuBar == null) {
			try {
				ivjGamePlayerJMenuBar = new javax.swing.JMenuBar();
				ivjGamePlayerJMenuBar.setName("GamePlayerJMenuBar");
				ivjGamePlayerJMenuBar.add(getGameMenu());
				ivjGamePlayerJMenuBar.add(getDifficultyMenu());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjGamePlayerJMenuBar;
	}

	/**
		 * Return the GameMenu property value.
		 * @return javax.swing.JMenu
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenu getGameMenu() {
		if (ivjGameMenu == null) {
			try {
				ivjGameMenu = new javax.swing.JMenu();
				ivjGameMenu.setName("GameMenu");
				ivjGameMenu.setText("Game");
				ivjGameMenu.add(getTicTacToeMenuItem());
				ivjGameMenu.add(getConnectFourMenuItem());
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjGameMenu;
	}

	/**
	 * Return the TicTacToeMenuItem property value.
	 * @return javax.swing.JMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuItem getTicTacToeMenuItem() {
		if (ivjTicTacToeMenuItem == null) {
			try {
				ivjTicTacToeMenuItem = new javax.swing.JMenuItem();
				ivjTicTacToeMenuItem.setName("TicTacToeMenuItem");
				ivjTicTacToeMenuItem.setText("TicTacToe");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTicTacToeMenuItem;
	}

	/**
		 * Return the ConnectFourMenuItem property value.
		 * @return javax.swing.JMenuItem
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenuItem getConnectFourMenuItem() {
		if (ivjConnectFourMenuItem == null) {
			try {
				ivjConnectFourMenuItem = new javax.swing.JMenuItem();
				ivjConnectFourMenuItem.setName("ConnectFourMenuItem");
				ivjConnectFourMenuItem.setText("ConnectFour");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjConnectFourMenuItem;
	}

	/**
		 * Return the DifficultyMenu property value.
		 * @return javax.swing.JMenu
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JMenu getDifficultyMenu() {
		if (ivjDifficultyMenu == null) {
			try {
				ivjDifficultyMenu = new javax.swing.JMenu();
				ivjDifficultyMenu.setName("DifficultyMenu");
				ivjDifficultyMenu.setText("Difficulty");
				ivjDifficultyMenu.add(getEasyButton());
				ivjDifficultyMenu.add(getMediumButton());
				ivjDifficultyMenu.add(getHardButton());
				// user code begin {1}
				ButtonGroup bg = new ButtonGroup();
				bg.add(getEasyButton());
				bg.add(getMediumButton());
				bg.add(getHardButton());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjDifficultyMenu;
	}

	/**
		 * Return the Easy property value.
		 * @return javax.swing.JRadioButtonMenuItem
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButtonMenuItem getEasyButton() {
		if (ivjEasyButton == null) {
			try {
				ivjEasyButton = new javax.swing.JRadioButtonMenuItem();
				ivjEasyButton.setName("EasyButton");
				ivjEasyButton.setSelected(true);
				ivjEasyButton.setText("Easy");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjEasyButton;
	}

	/**
	 * Return the JRadioButtonMenuItem3 property value.
	 * @return javax.swing.JRadioButtonMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButtonMenuItem getMediumButton() {
		if (ivjMediumButton == null) {
			try {
				ivjMediumButton = new javax.swing.JRadioButtonMenuItem();
				ivjMediumButton.setName("MediumButton");
				ivjMediumButton.setText("Medium");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjMediumButton;
	}

	/**
	 * Return the JRadioButtonMenuItem1 property value.
	 * @return javax.swing.JRadioButtonMenuItem
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButtonMenuItem getHardButton() {
		if (ivjHardButton == null) {
			try {
				ivjHardButton = new javax.swing.JRadioButtonMenuItem();
				ivjHardButton.setName("HardButton");
				ivjHardButton.setText("Hard");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjHardButton;
	}

	// ***************************************************************************
	// Main and init code
	// ***************************************************************************

	/**
	 * Starts the application.
	 * @param args an array of command-line arguments
	 */
	public static void main(java.lang.String[] args) {
		try {
			/* Set native look and feel */
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			/* Create the frame */
			GamePlayer aGamePlayer = new GamePlayer();
			/* Add a windowListener for the windowClosedEvent */
			aGamePlayer.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosed(java.awt.event.WindowEvent e) {
					System.exit(0);
				};
			});
			aGamePlayer.setVisible(true);
		} catch (Throwable exception) {
			System.err.println("Exception occurred in main() of GamePlayer");
			exception.printStackTrace(System.out);
		}
	}

	/**
		 * Initialize the class.
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("GamePlayer");
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setJMenuBar(getGamePlayerJMenuBar());
			setSize(760, 700);
			setTitle("GamePlayer");
			setContentPane(getJFrameContentPane());
			initConnections();
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}

		// YOUR INIT CODE GOES HERE
		// THAT IS, SET ANY VARIABLES THAT YOU WANT INITIALIZED AT THE START HERE		

		// user code end
	}

	/**
	 * Called whenever the part throws an exception.
	 * @param exception java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* Uncomment the following lines to print uncaught exceptions to stdout */
		System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		exception.printStackTrace(System.out);
		System.exit(1);
	}

	// ***************************************************************************
	// Connections
	// ***************************************************************************

	/**
		 * Initializes connections
		 * @exception java.lang.Exception The exception description.
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initConnections() throws java.lang.Exception {
		// user code begin {1}
		// user code end
		getBoardPanel1().addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent e) {
				connEtoC1(e);
			};
		});
		getTicTacToeMenuItem().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				connEtoC2(e);
			};
		});
		getConnectFourMenuItem().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				connEtoC3(e);
			};
		});
		getEasyButton().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				connEtoC4(e);
			};
		});
		getMediumButton().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				connEtoC5(e);
			};
		});
		getHardButton().addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				connEtoC6(e);
			};
		});
	}

	/**
		 * connEtoC1:  (BoardPanel1.mouse.mouseReleased(java.awt.event.MouseEvent) --> GamePlayer.boardPanel1_MouseReleased(Ljava.awt.event.MouseEvent;)V)
		 * @param arg1 java.awt.event.MouseEvent
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC1(java.awt.event.MouseEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.boardPanel1_MouseReleased(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC2:  (TicTacToeMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> GamePlayer.ticTacToeMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC2(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.ticTacToeMenuItem_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC3:  (ConnectFourMenuItem.action.actionPerformed(java.awt.event.ActionEvent) --> GamePlayer.connectFourMenuItem_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC3(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.connectFourMenuItem_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
		 * connEtoC4:  (EasyButton.action.actionPerformed(java.awt.event.ActionEvent) --> GamePlayer.easyButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
		 * @param arg1 java.awt.event.ActionEvent
		 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC4(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.easyButton_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC5:  (MediumButton.action.actionPerformed(java.awt.event.ActionEvent) --> GamePlayer.mediumButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC5(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.mediumButton_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

	/**
	 * connEtoC6:  (HardButton.action.actionPerformed(java.awt.event.ActionEvent) --> GamePlayer.hardButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void connEtoC6(java.awt.event.ActionEvent arg1) {
		try {
			// user code begin {1}
			// user code end
			this.hardButton_ActionPerformed(arg1);
			// user code begin {2}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}
			// user code end
			handleException(ivjExc);
		}
	}

}
