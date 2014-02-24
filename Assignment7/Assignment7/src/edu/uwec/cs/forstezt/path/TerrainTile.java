package edu.uwec.cs.forstezt.path;

import java.awt.*;

// This is an abstract class

public interface TerrainTile {

	// Draws the tile on the given Graphics
	// Do we need to take in the coordinates?
	public void draw(Graphics g, int x, int y, int width, int height);
	public abstract int getCost();
	
}
