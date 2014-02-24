package edu.uwec.cs.forstezt.hull;

import java.awt.geom.*;
import java.util.*;
import java.awt.Color; // Can't just import java.awt.* b/c of the conflict with List
import java.awt.Graphics;

public class ConvexHullPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;

	private List<Point2D> thePoints;

	private List<Point2D> theHull;

	// Default constructor

	public ConvexHullPanel() {
		thePoints = new ArrayList<Point2D>();
		theHull = new ArrayList<Point2D>();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g); // To draw the background color, if set

		// Draw the list of points in blue
		g.setColor(Color.blue);
		for (int i = 0; i < thePoints.size(); i++) {
			Point2D currentPoint = thePoints.get(i);
			g.fillOval((int) currentPoint.getX() - 2, (int) currentPoint.getY() - 2, 4, 4);
		}

		// Draw the convex hull in red
		g.setColor(Color.red);

		for (int i = 0; i < theHull.size(); i++) {
			Point2D firstPoint = theHull.get(i);
			Point2D secondPoint = theHull.get((i + 1) % theHull.size());

			g.drawLine((int) firstPoint.getX(), (int) firstPoint.getY(), (int) secondPoint.getX(), (int) secondPoint.getY());
			g.fillOval((int) firstPoint.getX() - 2, (int) firstPoint.getY() - 2, 4, 4);
			g.fillOval((int) secondPoint.getX() - 2, (int) secondPoint.getY() - 2, 4, 4);
		}

	}

	public void setHull(List<Point2D> theHull) {
		this.theHull = theHull;
		repaint();
	}

	public void setPoints(List<Point2D> thePoints) {
		this.thePoints = thePoints;
		repaint();
	}
}
