package edu.uwec.cs.forstezt.hull;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.*;

public class ConvexHullGUI {
	
	List<Point2D> points;
	ConvexHullFinder theConvexHullFinder;

	public ConvexHullGUI() {
		
		points = new ArrayList<Point2D>();
		
		JFrame f = new JFrame("Convex Hull Finder");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		f.setSize(new Dimension(600, 500));
		
		// Add the components
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(Color.LIGHT_GRAY);
		mainPanel.setLayout(new BorderLayout());
		
		
		
		
		// On the center we add the convex hull panel and add the even listener to it
		final ConvexHullPanel chp = new ConvexHullPanel();
		chp.setPreferredSize(new Dimension(400, 400));
		chp.setBackground(Color.WHITE);
		chp.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseReleased(e);
				
				points.add(e.getPoint());
				chp.setPoints(points);
			}
		});
		
		mainPanel.add(chp, BorderLayout.CENTER);
		
		
		
		
		// On the left we add another panel that contains the controls
		JPanel controlPanel = new JPanel();
		controlPanel.setBackground(Color.LIGHT_GRAY);
		controlPanel.setSize(new Dimension(100, 500));
		controlPanel.setLayout(new GridLayout(6, 1));
		//controlPanel.setLayout(new FlowLayout());
		
		JLabel numPointsLab = new JLabel("     # of points     ");
		controlPanel.add(numPointsLab);
		
		final JTextField numPoints = new JTextField(10);
		controlPanel.add(numPoints);
		
		
		JButton genPoints = new JButton("Generate Points");
		controlPanel.add(genPoints);
		genPoints.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Assume the text entered is an actual number
				int numberOfPoints = Integer.parseInt(numPoints.getText());
				
				// Make the List of Point2Ds
				points = new ArrayList<Point2D>();

				int width = chp.getWidth();
				int height = chp.getHeight();

				for (int i = 0; i < numberOfPoints; i++) {
					// Generate 2 random numbers in the range set by the hull panel
					// and not too close to the edges so it looks good
					int x = (int) (Math.random() * (width - 50)) + 25;
					int y = (int) (Math.random() * (height - 50)) + 25;

					points.add(new Point2D.Double(x, y));
				}

				// Display them on the panel
				chp.setPoints(points);
				chp.setHull(new ArrayList<Point2D>()); // Zero out any hull that was there
			}
		
		});
		
		
		JRadioButton quickButton = new JRadioButton("QuickHull");
		quickButton.setSelected(true);
		theConvexHullFinder = new QuickHull();
		quickButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				theConvexHullFinder = new QuickHull();			
			}	
		});
		controlPanel.add(quickButton);
		
		JRadioButton mergeButton = new JRadioButton("MergeHull");
		mergeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				theConvexHullFinder = new MergeHull();			
			}	
		});
		controlPanel.add(mergeButton);
		
		ButtonGroup hullChoice = new ButtonGroup();
		hullChoice.add(quickButton);
		hullChoice.add(mergeButton);
		
		
		JButton genHull = new JButton("Generate Hull");
		genHull.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
								
				// Generate the hull
				List<Point2D> hullPoints = theConvexHullFinder.computeHull(points);
				
				// Show the results on the screen
				chp.setHull(hullPoints);
			}	
		});
		controlPanel.add(genHull);
		
		
		mainPanel.add(controlPanel, BorderLayout.WEST);
		
		
		
	
		// Replace the content pane of the frame with the main panel we built
		f.setContentPane(mainPanel);
		f.setVisible(true);
	}
	
	public static void main(String[] args) {
		new ConvexHullGUI();
	}

}
