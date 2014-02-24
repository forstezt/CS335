package edu.uwec.cs.forstezt.path;

import java.awt.Graphics;
import java.awt.Point;
import java.util.*;
import java.io.*;

// A Board consists of a 2D grid of Tiles
// A Board can display itself among other things

public class TerrainMap {
	private TerrainTile[][] theTiles;
	
	
	// Constructor to read from file
	public TerrainMap(String filename) {
		
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			
			// Read in the width
			String widthHeight = br.readLine();
			StringTokenizer st = new StringTokenizer(widthHeight);
			int width = Integer.parseInt(st.nextToken());
			int height = Integer.parseInt(st.nextToken());
					
			theTiles = new TerrainTile[width][height];
			
			// Now read the info (row by row)
			for (int y=0; y<height; y++) {
				String currentRow = br.readLine();
				st = new StringTokenizer(currentRow);
				for (int x=0; x<width; x++) {
						
					String currentTile = st.nextToken();	
					
					if (currentTile.equals("g")) {
						theTiles[x][y] = new Grass();
					}
					else if (currentTile.equals("r")) {
						theTiles[x][y] = new Road();
					}
					else if (currentTile.equals("m")) {
						theTiles[x][y] = new Mountain();
					}
					else if (currentTile.equals("s")) {
						theTiles[x][y] = new Swamp();
					}
						
						
				}
			}
			
			br.close();
				
		}
		catch (Exception e) {
			System.out.println("Problem reading terrain info from file");
			System.exit(1);
		}
		
		
	}
	
	// To enable the Board to draw itself on the given Graphics
	public void draw(Graphics g) {
		for (int x=0; x<theTiles.length; x++) {
			for (int y=0; y<theTiles[0].length; y++) {
				theTiles[x][y].draw(g, x * 25, y * 25, 25, 25);
			}
		}

	}
	
	// To hand back the cost of a given tile
	public int getCost(Point position) {
		return (theTiles[position.x][position.y]).getCost();		
	}
	
	public int getTerrainHeight() {
		return theTiles.length;
	}
	
	public int getTerrainWidth() {
		return theTiles[0].length;
	}
}
