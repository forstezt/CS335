package edu.uwec.cs.forstezt.path;

import java.awt.*;

public class Road implements TerrainTile {
	
	public Road() {
	}
	
	public int getCost() {
		return 2;
	}

	public void draw(Graphics g, int x, int y, int width, int height) {

		g.setColor(Color.yellow);
		g.fillRect(x, y, width, height);
	}
}
