package edu.uwec.cs.forstezt.path;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TerrainPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private TerrainMap theBoard;
	private Avatar theAvatar;
	private List<PathState> aStar;
	
	public TerrainPanel(TerrainMap b) {
		this.theBoard = b;
		this.setSize(500, 500);

		this.aStar = new ArrayList<PathState>();
		
		// Setup the frame that I belong in
		JFrame frame = new JFrame("Terrain");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(1);
			}
		});
		frame.getContentPane().add(this);
		frame.setSize(500 + 10, 500 + 28);
		
		this.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent e) {
				
				// Get the current mouse x,y
				Point location = e.getPoint();
				
				// Convert it to the grid we are using
				location.x /= 25;
				location.y /= 25;
								
				// Move the avatar along that path
				clearAstarStates();
				theAvatar.moveAlongPath(theBoard, location);
			}
		});
		
		frame.setVisible(true);
	}
	
	// Can't be done in the constructor since the panel and the avatar need to know about each other
	public void addAvatar(Avatar a) {
		this.theAvatar = a;
	}
	
	// These are for A* path generation display
	public void clearAstarStates() {
		aStar.clear();
	}
	public void addAstarState(PathState p) {
		aStar.add(p);
	}

	// What gets called when repaint is called
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Draw the board
		theBoard.draw(g);
		
		// Draw the AstarPoints
		for (int i=0; i<aStar.size(); i++) {
			PathState position = aStar.get(i);
			
			position.draw(g, 25, 25);
		}
		
		// Draw the avatar
				theAvatar.draw(g, 25, 25);
	}

}