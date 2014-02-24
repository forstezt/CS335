package edu.uwec.cs.forstezt.path;

// This is a concrete class that extends the Tile class

import java.awt.*;

public class Mountain implements TerrainTile {

	public Mountain() {
	}
	
	public int getCost() {
		return 45;
	}
	
	public void draw(Graphics g, int x, int y, int width, int height) {

		g.setColor(Color.darkGray);
		g.fillRect(x, y, width, height);
	}
}
