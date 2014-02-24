package edu.uwec.cs.forstezt.rsa;

import java.security.SecureRandom;
import java.io.*;
import java.math.*;

import edu.uwec.cs.stevende.*;
import edu.uwec.cs.stevende.rsa.BigIntegerRSA;


public class RSACracker {

	public static void main(String[] args) {
		
//		BigInteger PQ = BigInteger.valueOf(608485549753L);
//		BigInteger E = BigInteger.valueOf(7L);
//				
//		BigInteger[] cipherMessage = {
//			BigInteger.valueOf(576322461849L),
//			BigInteger.valueOf(122442824098L),
//			BigInteger.valueOf(34359738368L),
//			BigInteger.valueOf(29647771149L),
//			BigInteger.valueOf(140835578744L),
//			BigInteger.valueOf(546448062804L),
//			BigInteger.valueOf(120078454173L),
//			BigInteger.valueOf(42618442977L)
//		};
		
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("H:/Classes/CS335/timing_results.xlsx"), "utf-8"));
		
			for (int BITLEN = 14; BITLEN < 64; BITLEN++) {
				BigInteger[] PQAndE = BigIntegerRSA.GeneratePQAndE(BITLEN);
				BigInteger PQ = PQAndE[0];
				BigInteger E = PQAndE[1];
				
				BigInteger P = BigInteger.ZERO;
				BigInteger Q = BigInteger.ZERO;
				
				BigInteger D = BigInteger.ZERO;
				
				// Encyrpt with:  message.modPow(e, n);
				String plainTextMessage = "Go Duke!";
				BigInteger[] cipherMessage = BigIntegerRSA.EncodeMessage(E, PQ, plainTextMessage);		
				
				long startTime = System.currentTimeMillis();
				
				//check to see if 2 divides PQ
				if (PQ.mod(BigInteger.valueOf(2L)).compareTo(BigInteger.ZERO) == 0) {
					P = BigInteger.valueOf(2L);
					Q = PQ.divide(P);
				}
		
				//check to see if 3 divides PQ
				if (PQ.mod(BigInteger.valueOf(3L)).compareTo(BigInteger.ZERO) == 0) {
					P = BigInteger.valueOf(3L);
					Q = PQ.divide(P);
				}
				
				//All primes greater than 3 are of the form 6n + 1 or 6n - 1
				BigInteger primeCandidate;
				boolean primesFound = false;
				BigInteger multipleOfSix = BigInteger.valueOf(6L);
		
				while (multipleOfSix.subtract(BigInteger.ONE).compareTo(PQ) < 0 && !primesFound) {			
					//check the 6n - 1 case
					primeCandidate = multipleOfSix.subtract(BigInteger.ONE);
					
					//The product of 2 primes is only divisible by those 2 primes, itself, and 1,
					//so if something other than PQ and 1 divides PQ, it is a prime factor
					if (PQ.mod(primeCandidate).compareTo(BigInteger.ZERO) == 0) {
						P = primeCandidate;
						Q = PQ.divide(P);
						primesFound = true;
					}
					
					if (!primesFound) {
						//check the 6n + 1 case
						primeCandidate = multipleOfSix.add(BigInteger.ONE);
						
						//The product of 2 primes is only divisible by those 2 primes, itself, and 1,
						//so if something other than PQ and 1 divides PQ, it is a prime factor
						if (PQ.mod(primeCandidate).compareTo(BigInteger.ZERO) == 0) {
							P = primeCandidate;
							Q = PQ.divide(P);
							primesFound = true;
						}			
					}
					
					//check the next multiple of six
					multipleOfSix = multipleOfSix.add(BigInteger.valueOf(6L));
				}
				
				//calculate D
				D = E.modInverse(P.subtract(BigInteger.ONE).multiply(Q.subtract(BigInteger.ONE)));
				
				long endTime = System.currentTimeMillis();
				
				long elapsedTime = endTime - startTime;
				
				writer.write(BITLEN + "\t" + elapsedTime + "\n");
				
				//decrypt message
				String decryptedMessage = BigIntegerRSA.DecodeMessage(D, PQ, cipherMessage);
				
//				System.out.println("PQ: " + PQ);
//				System.out.println("P: " + P);
//				System.out.println("Q: " + Q);
//				System.out.println("D: " + D);
//				System.out.println("Decrypted Message: " + decryptedMessage);
			}
			
			writer.close();
		
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
