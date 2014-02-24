package edu.uwec.cs.forstezt.hull;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class QuickHull implements ConvexHullFinder {

	public List<Point2D> computeHull(List<Point2D> points) {

		List<Point2D> hull = new ArrayList<Point2D>();

		// find the rightmost point
		Point2D rightmostPoint = GetRightmostPoint(points);
		// fing the leftmost point
		Point2D leftmostPoint = GetLeftmostPoint(points);

		// generate the initial arguments to find the top hull
		Line2D AB = new Line2D.Double(leftmostPoint, rightmostPoint);
		List<Point2D> topPoints = GetCCWPoints(points, AB);

		// generate the initial arguments to find the bottom hull
		Line2D BA = new Line2D.Double(rightmostPoint, leftmostPoint);
		List<Point2D> bottomPoints = GetCCWPoints(points, BA);
		
		
		if (topPoints.size() == 0 && bottomPoints.size() == 0) { //every point is on a single line
			//sort the points by x-value
			Comparator<Point2D> comparator = (ArePointsVertical(points)) ? new Point2DYComparator() : new Point2DXComparator();			
			Collections.sort(points, comparator);
			
			//add every point to the list from right to left (counterclockwise-ish)
			for (int i = points.size() - 1; i >= 0; i--) {
				hull.add(points.get(i));
			}
		} else { //not every point is on a line
			//
			List<Point2D> pointsOnLine = GetPointsOnLine(points, AB);
			if (pointsOnLine.size() > 0) {
				Comparator<Point2D> comparator = (ArePointsVertical(pointsOnLine)) ? new Point2DYComparator() : new Point2DXComparator();			
				Collections.sort(pointsOnLine, comparator);	
			}
			
			//add the rightmost point, then the top hull, then the leftmost point, and finally the bottom hull
			hull.add(rightmostPoint);
			hull.addAll(FindHullRecursively(pointsOnLine, topPoints, AB));
			hull.add(leftmostPoint);
			hull.addAll(FindHullRecursively(pointsOnLine, bottomPoints, BA));	
		}
		
		return hull;

	}

	/*
	 * Recursively builds the convex hull for a bunch of points
	 * pointsOnLine - the list of points that fall on the line AB
	 * points - the list of points that fall counterclockwise to the line AB
	 * AB - the line to which the points are compared
	 */
	private List<Point2D> FindHullRecursively(List<Point2D> pointsOnLine, List<Point2D> points, Line2D AB) {

		List<Point2D> hull = new ArrayList<Point2D>();
		
		//find the farthest point from the line AB
		Point2D C = FindC(pointsOnLine, points, AB);
		
		if (C != null) {

			//draw 2 lines connecting C with A and B
			//the lines must be oriented the way that they are so that we can use the relativeCCW function to find points that are above/below them
			Line2D CB = new Line2D.Double(C, AB.getP2());
			Line2D AC = new Line2D.Double(AB.getP1(), C);

			//find the points that are counterclockwise to our 2 lines
			List<Point2D> rightList = GetCCWPoints(points, CB);
			List<Point2D> leftList = GetCCWPoints(points, AC);
			
			//create a list comprised of the counterclockwise points and the points on the line AB
			List<Point2D> newPoints = new ArrayList<Point2D>();
			newPoints.addAll(points);
			newPoints.addAll(pointsOnLine);
			
			//find the points that lie on the lines AB and AC
			List<Point2D> pointsOnCB = GetPointsOnLine(newPoints, CB);
			List<Point2D> pointsOnAC = GetPointsOnLine(newPoints, AC);
		
			if (pointsOnCB.size() > 0) { //there are points on the line CB
				Comparator<Point2D> comparator = (ArePointsVertical(pointsOnCB)) ? new Point2DYComparator() : new Point2DXComparator();			
				Collections.sort(pointsOnCB, comparator);
			}
			if (pointsOnAC.size() > 0) { //there are points on the line AC
				Comparator<Point2D> comparator = (ArePointsVertical(pointsOnAC)) ? new Point2DYComparator() : new Point2DXComparator();
				Collections.sort(pointsOnAC, comparator);	
			}

			//add the C values to the hull from right to left (counterclockwise)
			hull.addAll(FindHullRecursively(pointsOnCB, rightList, CB));
			hull.add(C);
			hull.addAll(FindHullRecursively(pointsOnAC, leftList, AC));
		}

		return hull;
	}
	
	/*
	 * Returns true if every point in a list of points falls on the same vertical line
	 * points - the list of points
	 */
	private boolean ArePointsVertical(List<Point2D> points) {
		boolean arePointsVertical = true;
		double xValue = points.get(0).getX();
		for (Point2D point : points) {
			if (xValue - point.getX() < -0.00001 || xValue - point.getX() > 0.00001) {
				arePointsVertical = false;
			}
		}
		
		return arePointsVertical;
	}

	/*
	 * Returns every point in a list of points that falls on a given line
	 * points - the list of points
	 * line - the line that the points are compared against
	 */
	private List<Point2D> GetPointsOnLine(List<Point2D> points, Line2D line) {
		List<Point2D> pointsOnLine = new ArrayList<Point2D>();
		for (Point2D point : points) {
			if (line.intersects(new Rectangle2D.Double(point.getX(), point.getY(), 0.01, 0.01)) && !point.equals(line.getP1()) && !point.equals(line.getP2())) {
				pointsOnLine.add(point);
			}
		}

		return pointsOnLine;
	}
	
	/*
	 * Returns every point in a list of points that is counterclockwise to a given line
	 * points - the list of points
	 * line - the line that the points are compared against
	 */
	private List<Point2D> GetCCWPoints(List<Point2D> points, Line2D line) {
		List<Point2D> CCWPoints = new ArrayList<Point2D>();
		for (Point2D point : points) {
			if (line.relativeCCW(point) == 1) {
				CCWPoints.add(point);
			}
		}

		return CCWPoints;
	}

	/*
	 * Find C - the farthest point in a list of points from a given line
	 * pointsOnLine - the list of points that are on the line AB
	 * points - the list of points that are counterclockwise to the line AB
	 * AB - a line from point A to point B (points A and B determined in recursive method)
	 */
	private Point2D FindC(List<Point2D> pointsOnLine, List<Point2D> points, Line2D AB) {

		Point2D C = null;
		
		if (points.size() > 0) { //there are points that are counterclockwise to the line AB
			double maxDistanceFromLine = 0.0;
			for (Point2D point : points) {
				double distanceFromLine = AB.ptLineDist(point);
				if (distanceFromLine > maxDistanceFromLine) {
					C = point;
					maxDistanceFromLine = distanceFromLine;
				}
			}
		} else { //there aren't any points that are counterclockwise to the line AB
			if (pointsOnLine.size() > 0) { //there are points on the line AB
				C = pointsOnLine.get(pointsOnLine.size() / 2);
			}
		}

		return C;
	}

	/*
	 * Returns the rightmost point of a list of points
	 * points - the list of points
	 */
	private Point2D GetRightmostPoint(List<Point2D> points) {
		Point2D rightmostPoint = new Point2D.Double(Double.MIN_VALUE, 0.0);
		for (Point2D point : points) {
			if (point.getX() > rightmostPoint.getX()) { //this point is farther right than the current rightmost point
				rightmostPoint = point;
			}
		}

		return rightmostPoint;
	}

	/*
	 * Returns the leftmost point of a list of points
	 * points - the list of points
	 */
	private Point2D GetLeftmostPoint(List<Point2D> points) {
		Point2D leftmostPoint = new Point2D.Double(Double.MAX_VALUE, 0.0);
		for (Point2D point : points) {
			if (point.getX() < leftmostPoint.getX()) { //this point is farther left than the current rightmost point
				leftmostPoint = point;
			}
		}

		return leftmostPoint;
	}
}
