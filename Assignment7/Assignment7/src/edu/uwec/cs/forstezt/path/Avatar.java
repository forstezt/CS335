package edu.uwec.cs.forstezt.path;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

public class Avatar  {

	// The current position of the avatar on the grid
	private Point position;
		
	// The panel that we are drawn on - so we can repaint it when we move
	private TerrainPanel tp;

	public Avatar(Point position, TerrainPanel tp) {
		this.position = position;
		this.tp = tp;
	}	
		
	// Draws the tile on the given Graphics
	// We already know our position
	// Draw as an oval
	// This method is not meant to be called directly - it is called through the panel's repaint
	public void draw(Graphics g, int width, int height) {
		
		g.setColor(Color.magenta);

		g.fillOval((position.x * width) + (width/4), (position.y * height) + (width/4), width/2, height/2);
		
		g.setColor(Color.black);
		g.drawOval((position.x * width) + (width/4), (position.y * height) + (width/4), width/2, height/2);
	}
		
	public void moveAlongPath(final TerrainMap theBoard, final Point endLocation) {
		
		
		// Do this in a thread so I can see it update since it will be called from inside an event handler
		new Thread(new Runnable() {

			@Override
			public void run() {

				// Generate a A* path
				PathFinder pf = new PathFinder(theBoard, tp);
				final List<Point> path = pf.findPath(position, endLocation);
				
				// Update my position, repaint the panel we are on, then sleep for a bit as a delay
				for (int i=0; i<path.size(); i++) {
					position = (Point)(path.get(i));
				
					tp.repaint();
					
					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				}
			}
			
		}).start();
	}

	
}

