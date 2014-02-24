package edu.uwec.cs.forstezt.path;

import java.awt.*;

public class Grass implements TerrainTile {
	
	public Grass() {
	}
	
	public int getCost() {
		return 10;
	}

	public void draw(Graphics g, int x, int y, int width, int height) {

		g.setColor(Color.green);
		g.fillRect(x, y, width, height);
	}
}
