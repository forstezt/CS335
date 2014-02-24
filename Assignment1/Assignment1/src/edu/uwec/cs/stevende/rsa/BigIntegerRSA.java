package edu.uwec.cs.stevende.rsa;

import java.security.SecureRandom;
import java.math.*;

public class BigIntegerRSA {

	public static void main(String[] args) {

		// Here is an all BigInteger implementation of RSA where it does all the work import java.math.BigInteger;

		// Find P and Q
		final int BITLEN = 8;
		SecureRandom r = new SecureRandom();
		BigInteger p = BigInteger.ZERO;
		BigInteger q = BigInteger.ZERO;
		while (p.equals(q)) {
			p = new BigInteger(BITLEN / 2, 100, r);
			q = new BigInteger(BITLEN / 2, 100, r);
		}
		
		// Find PQ and phiPQ
		BigInteger pq = p.multiply(q);
		BigInteger phiPQ = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		// Find E
		BigInteger e = new BigInteger("2");
		while (phiPQ.gcd(e).compareTo(BigInteger.ONE) > 0) {
			e = e.add(BigInteger.ONE);
		}
		
		// Find D
		BigInteger d = e.modInverse(phiPQ);
	
		// Print out some information
		System.out.println("p: " + p);
		System.out.println("q: " + q);
		System.out.println("pq: " + pq);
		System.out.println("e: " + e);
		System.out.println("d: " + d);
		
	
		// Encyrpt with:  message.modPow(e, n);
		String plainTextMessage = "Hello World";
		BigInteger[] cipherMessage = EncodeMessage(e, pq, plainTextMessage);
		
		// Write out the message to the screen
		System.out.println("The cipher message is:");
		for (int i=0; i<cipherMessage.length; i++) {
			System.out.println(cipherMessage[i]);
		}
	
	    // decrypt with: message.modPow(d, n);
	    String decodedMessage = DecodeMessage(d, pq, cipherMessage);
	    
	    System.out.println(decodedMessage);
	}
	
	public static BigInteger[] GeneratePQAndE(int BITLEN) {
		BigInteger[] PQAndE = new BigInteger[2];
		
		SecureRandom r = new SecureRandom();
		
		BigInteger p = BigInteger.ZERO;
		BigInteger q = BigInteger.ZERO;
		while (p.equals(q)) {
			p = new BigInteger(BITLEN / 2, 100, r);
			q = new BigInteger(BITLEN / 2, 100, r);
		}
		
		// Find PQ and phiPQ
		BigInteger pq = p.multiply(q);
		BigInteger phiPQ = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		
		// Find E
		BigInteger e = new BigInteger("2");
		while (phiPQ.gcd(e).compareTo(BigInteger.ONE) > 0) {
			e = e.add(BigInteger.ONE);
		}
		
		PQAndE[0] = pq;
		PQAndE[1] = e;
		
		return PQAndE;
	}
	
	public static BigInteger[] EncodeMessage(BigInteger e, BigInteger pq, String plainTextMessage) {
		// Encyrpt with:  message.modPow(e, n);
		BigInteger[] cipherMessage = new BigInteger[plainTextMessage.length()];
		
		for (int i=0; i<plainTextMessage.length(); i++) {
			BigInteger m = new BigInteger(((int)plainTextMessage.charAt(i)) + "");
			BigInteger c = m.modPow(e, pq);
			cipherMessage[i] = c;
		}
		
		return cipherMessage;
	}
	
	public static String DecodeMessage(BigInteger d, BigInteger pq, BigInteger[] cipherMessage) {
	    // decrypt with: message.modPow(d, n);
	    String decodedMessage = "";
	    
	    for (int i=0; i<cipherMessage.length; i++) {
	    	BigInteger c = cipherMessage[i];
	    	BigInteger m = c.modPow(d, pq);
	    	decodedMessage += (char)(m.intValue());
	    }
	    
	    return decodedMessage;
	}
	
}

