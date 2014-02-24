package edu.uwec.cs.forstezt.path;

import java.awt.Point;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TerrainMap tm = new TerrainMap("map1.txt");
		TerrainPanel tp = new TerrainPanel(tm);
		Avatar ava = new Avatar(new Point(0, 0), tp);
		tp.addAvatar(ava);
		
		
	}

}
