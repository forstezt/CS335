package edu.uwec.cs.forstezt.hull;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeHull implements ConvexHullFinder {
	
	private int RIGHT_LIST_INDEX = 1;
	private int LEFT_LIST_INDEX = 0;
	
	public List<Point2D> computeHull(List<Point2D> points) {
		
		List<Point2D> hull = new ArrayList<Point2D>();
		
		if (points.size() > 0) { //the list contains points
			//sort the list of points by x-value from left to right (and by y-value as well)
			//this algorithm is O(nlogn), so it doesn't increase the big O time
			Point2DComparator comparator = new Point2DComparator();
			Collections.sort(points, comparator);
			
			//removes any duplicate points in the list
			//this algorithm is O(n), so it doesn't increase the big O time
			//NOTE: this noticeably slows the run time with tens of thousands of points, but also dramatically reduces the frequency of errors (I observed none after many runs)
			for (int i = 0; i < points.size() - 1; i++) {
				if (points.get(i + 1).equals(points.get(i))) {
					points.remove(i + 1);
					i--;
				}
			}
			
			//call recursive algorithm
			hull = recursiveMergeHull(points);
		}
		
		return hull;
	}
	
	/*
	 * Recursively finds the convex hull around a bunch of points
	 * points - the list of points
	 */
	private List<Point2D> recursiveMergeHull(List<Point2D> points) {
		
		//base case
		if (points.size() == 1) {
			return points;
		} else {			
			//divide the points in half
			int mid = points.size() / 2;
			
			//recursively calculate both the left and right hulls
			List<Point2D> leftHull = recursiveMergeHull(points.subList(0, mid));
			List<Point2D> rightHull = recursiveMergeHull(points.subList(mid, points.size()));
			
			//merge the 2 hulls together
			return mergeHulls(leftHull, rightHull);
		}
			
	}
	
	/*
	 * Merges 2 convex hulls together
	 * leftHull - the left convex hull to be merged
	 * rightHull - the right convex hull to be merged
	 */
	private List<Point2D> mergeHulls(List<Point2D> leftHull, List<Point2D> rightHull) {
		List<Point2D> hull = new ArrayList<Point2D>();
		
		//generate the indices for the upper and lower tangents
		//NOTE: the index variables RIGHT_LIST_INDEX and LEFT_LIST_INDEX can be found at the top of the file
		int[] upperTangentIndices = findUpperTangent(leftHull, rightHull);
		int[] lowerTangentIndices = findLowerTangent(leftHull, rightHull);
	
		//add the point on the upper tangent line that's in the right hull
		hull.add(rightHull.get(upperTangentIndices[RIGHT_LIST_INDEX]));

		//trace around the edge of the left hull from the index of the upper tangent until you reach the lower tangent, adding all points along the way to the hull
		int index = upperTangentIndices[LEFT_LIST_INDEX];
		while(index != lowerTangentIndices[LEFT_LIST_INDEX]) {
			hull.add(leftHull.get(index));
			index = (index + 1) % leftHull.size();
		}
		
		//add the points from the lower tangent line
		hull.add(leftHull.get(lowerTangentIndices[LEFT_LIST_INDEX]));					
		
		//trace around the edge of the right hull from the index of the lower tangent until you reach the upper tangent, adding all points along the way to the hull
		index = lowerTangentIndices[RIGHT_LIST_INDEX];
		while (index != upperTangentIndices[RIGHT_LIST_INDEX]) {
			hull.add(rightHull.get(index));
			index = (index + 1) % rightHull.size();
		}
		
		
		return hull;
	}	
	
	/*
	 * Finds the lower tangent line connecting 2 convex hulls
	 * leftHull - the left convex hull
	 * rightHull - the right convex hull
	 */
	private int[] findLowerTangent(List<Point2D> leftHull, List<Point2D> rightHull){
		//find the leftmost and rightmost points of the right and left hulls respectively and connect them with a line
		int leftHullIndex = GetRightmostPointIndex(leftHull);
		int rightHullIndex = GetLeftmostPointIndex(rightHull);
		Line2D tangentLine = new Line2D.Double(leftHull.get(leftHullIndex), rightHull.get(rightHullIndex));
				
		//while you haven't found the lower tangent, keep walking down on both sides
		while(!(isLowerTangent(tangentLine, leftHull, leftHullIndex) && isLowerTangent(tangentLine, rightHull, rightHullIndex))) {
			while(!isLowerTangent(tangentLine, leftHull, leftHullIndex)) {
				leftHullIndex--;
				tangentLine.setLine(leftHull.get((leftHullIndex % leftHull.size() + leftHull.size()) % leftHull.size()), tangentLine.getP2());
			}
			while(!isLowerTangent(tangentLine, rightHull, rightHullIndex)) {
				rightHullIndex++;
				tangentLine.setLine(tangentLine.getP1(), rightHull.get(rightHullIndex % rightHull.size()));
			}
		}
		
		int[] tangentIndices = new int[2];
		tangentIndices[LEFT_LIST_INDEX] = (leftHullIndex % leftHull.size() + leftHull.size()) % leftHull.size();
		tangentIndices[RIGHT_LIST_INDEX] = rightHullIndex % rightHull.size();
		
		return tangentIndices;
	}
	
	/*
	 * Finds the upper tangent line connecting 2 convex hulls
	 * leftHull - the left convex hull
	 * rightHull - the right convex hull
	 */
	private int[] findUpperTangent(List<Point2D> leftHull, List<Point2D> rightHull){
		//find the leftmost and rightmost points of the right and left hulls respectively and connect them with a line		
		int leftHullIndex = GetRightmostPointIndex(leftHull);
		int rightHullIndex = GetLeftmostPointIndex(rightHull);
		Line2D tangentLine = new Line2D.Double(leftHull.get(leftHullIndex), rightHull.get(rightHullIndex));
			
		//while you haven't found the upper tangent, keep walking up on both sides
		while(!(isUpperTangent(tangentLine, leftHull, leftHullIndex) && isUpperTangent(tangentLine, rightHull, rightHullIndex))) {
			while(!isUpperTangent(tangentLine, leftHull, leftHullIndex)) {
				leftHullIndex++;
				tangentLine.setLine(leftHull.get(leftHullIndex % leftHull.size()), tangentLine.getP2());
			}
			while(!isUpperTangent(tangentLine, rightHull, rightHullIndex)) {
				rightHullIndex--;
				tangentLine.setLine(tangentLine.getP1(), rightHull.get((rightHullIndex % rightHull.size() + rightHull.size()) % rightHull.size()));
			}
		}
		
		int[] tangentIndices = new int[2];
		tangentIndices[LEFT_LIST_INDEX] = leftHullIndex % leftHull.size();
		tangentIndices[RIGHT_LIST_INDEX] = (rightHullIndex % rightHull.size() + rightHull.size()) % rightHull.size();
		
		return tangentIndices;
	}
	
	/*
	 * Returns true if the line is a lower tangent line to the given points
	 * tangentLine - the potential tangent line
	 * points - the points being compared to the line
	 * index - the index of the point in the list which is a part of the potential tangent line
	 */
	private boolean isLowerTangent(Line2D tangentLine, List<Point2D> points, int index) {
		boolean isLowerTangent = true;

		//get the points on either side of the point at the given index (the point that is contained in the line)
		Point2D testPoint1 = points.get(((index + 1) % points.size() + points.size()) % points.size());
		Point2D testPoint2 = points.get(((index - 1) % points.size() + points.size()) % points.size());

		if (tangentLine.relativeCCW(testPoint1) == -1) { //there is a point that is clockwise to the line, so it isn't a lower tangent
			isLowerTangent = false;
		}
		
		if (tangentLine.relativeCCW(testPoint2) == -1) { //there is a point that is clockwise to the line, so it isn't a lower tangent
			isLowerTangent = false;
		}
		
		return isLowerTangent;
	}
	
	/*
	 * Returns true if the line is an upper tangent line to the given points
	 * tangentLine - the potential tangent line
	 * points - the points being compared to the line
	 * index - the index of the point in the list which is a part of the potential tangent line
	*/
	private boolean isUpperTangent(Line2D tangentLine, List<Point2D> points, int index) {
		boolean isUpperTangent = true;
		
		//get the points on either side of the point at the given index (the point that is contained in the line)
		Point2D testPoint1 = points.get(((index + 1) % points.size() + points.size()) % points.size());
		Point2D testPoint2 = points.get(((index - 1) % points.size() + points.size()) % points.size());

		if (tangentLine.relativeCCW(testPoint1) == 1) { //there is a point that is counterclockwise to the line, so it isn't a lower tangent
			isUpperTangent = false;
		}
		
		if (tangentLine.relativeCCW(testPoint2) == 1) { //there is a point that is counterclockwise to the line, so it isn't a lower tangent
			isUpperTangent = false;
		}
		
		return isUpperTangent;
	}
	
	/*
	 * Returns the index of the rightmost point in a list of points
	 * points - the list of points
	 */
	private int GetRightmostPointIndex(List<Point2D> points){
		int index = -1;
		
		//find the rightmost point
		double maxXValue = Double.MIN_VALUE;
		double maxYValue = Double.MIN_VALUE;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getX() > maxXValue) {
				maxXValue = points.get(i).getX();
				maxYValue = points.get(i).getY();
				index = i;
			} else if (points.get(i).getX() == maxXValue) { //the points have the same x-value, so sort by y-value
				if (points.get(i).getY() > maxYValue){
					maxYValue = points.get(i).getY();
					index = i;
				}
			}
		}
		
		return index;
	}
	
	/*
	 * Returns the index of the leftmost point in a list of points
	 * points - the list of points
	 */
	private int GetLeftmostPointIndex(List<Point2D> points){
		int index = -1;
		
		//find the rightmost point
		double minXValue = Double.MAX_VALUE;
		double minYValue = Double.MAX_VALUE;
		for (int i = 0; i < points.size(); i++) {
			if (points.get(i).getX() < minXValue) {
				minXValue = points.get(i).getX();
				minYValue = points.get(i).getY();
				index = i;
			} else if (points.get(i).getX() == minYValue) { //the points have the same x-value, so sort by y-value
				if (points.get(i).getY() < minYValue) {
					minYValue = points.get(i).getY();
					index = i;
				}
			}
		}
		
		return index;
	}
	
}
