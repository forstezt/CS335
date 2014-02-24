package edu.uwec.cs.forstezt.rsa;

import java.math.BigInteger;

public class TimeEstimator {

	public static void main(String[] args) {
		int bitLength = 1024;
		//double timeEstimate = 0.0081 * Math.pow(Math.E, 0.2448 * bitLength);
		double timeEstimate = 0.0023 * Math.pow(Math.E, 0.2554 * bitLength);

		System.out.println("Time Estimate in ms: " + timeEstimate);
		System.out.println("Time Estimate in centuries: " + timeEstimate / (1000 * 60 * 60 * 24 * 365.242 * 100));
	}

}
