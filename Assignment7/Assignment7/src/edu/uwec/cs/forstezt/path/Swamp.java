package edu.uwec.cs.forstezt.path;

import java.awt.*;

public class Swamp implements TerrainTile {

	public int getCost() {
		return 50;
	}
	
	public void draw(Graphics g, int x, int y, int width, int height) {

		g.setColor(Color.cyan);
		g.fillRect(x, y, width, height);
	}
}
