package edu.uwec.cs.stevende.rsa;

// See associated word document on assignment for details of this algorithm
// This is the version without any BigIntegers

public class RSA {

	public static void main(String[] args) {
		
		//******************************************************************************
		// Find P and Q
		//******************************************************************************
		// Generate 2 random numbers, P and Q with the following properties:
		//   1.  P and Q can be at most bitLength/2 bits long
		//   2.  P and Q cannot be 0 or 1
		//     --> The above 2 constraints imply that P and Q are in the range [2..2^(bitLength/2)) 
		//   3.  P and Q must be primes
		//   4.  P and Q cannot be the same number
		//   5.  (PQ) must be at least 256

		// Init P and Q so that (P * Q) is < 256 so the loop will run at least once
		long p = 0;
		long q = 0;

		// The number of bits we want P and Q to contain
		long bitLength = 15;
		long power = bitLength / 2;

		// Keep picking random P and Q until they are different and their product is >= 256
		while ((p == q) || ((p * q) < 256)) {

			// Pick P

			double rand = Math.random(); // Get a random double in the range [0.0 ... 1.0)
			p = (long) (rand * Math.pow(2, power)); // Change the range to [0..2^power)

			boolean foundP = false; // Init the loop control so the loop will run at least once
			while (!foundP) {

				// Find out if the number is prime
				// This code considers 0 and 1 to not be prime which is perfect
				//   since we don't want to include these anyway
				long i = 2; // Run from 2 up to, but not including, sqrt(P)
				while ((i <= Math.sqrt(p)) && ((p % i) != 0)) {
					i++;
				}
				// If we made it all the way to P then it is prime because nothing divided it evenly
				if ((i > Math.sqrt(p))) {
					foundP = true;
				} else { // We stoped early since something divided P evenly OR P was 0 or 1
					// Generate a new canidate P
					rand = Math.random();
					p = (long) (rand * Math.pow(2, power));
				}
			}
			
			// Repeat the process for Q

			rand = Math.random(); // Get a random double in the range [0.0 ... 1.0)
			q = (long) (rand * Math.pow(2, power)); // Change the range to [0..128)

			boolean foundQ = false;
			while (!foundQ) {

				// Find out if the number is prime
				// This code considers 0 and 1 to not be prime which is perfect
				//   since we don't want to include these anyway
				long i = 2; // Run from 2 up to, but not including, sqrt(Q)
				while ((i <= Math.sqrt(q)) && ((q % i) != 0)) {
					i++;
				}
				// If we made it all the way to Q then it is prime because nothing divided it evenly
				if ((i > Math.sqrt(q))) {
					foundQ = true;
				} else { // We stoped early since something divided Q evenly OR Q was 0 or 1
					// Generate a new canidate Q
					rand = Math.random();
					q = (long) (rand * Math.pow(2, power));
				}
			}

		} // End of while loop testing to make sure P!=Q and (P*Q) >= 256

		//******************************************************************************
		// Find PQ and phiPQ
		//******************************************************************************
		long pq = p * q;
		long phiPQ = (p - 1) * (q - 1);

		//******************************************************************************
		// Find E
		//******************************************************************************
		// Generate an E with the following properties:
		// 1.  1 < E < phiPQ
		// 2.  E and phiPQ are relatively prime
		//   --> the above constraint implies that gcd(E, phiPQ) == 1
		// 3.  There may be several candidates for E, so pick the smallest one (for consistency
		//       with the rest of the class -- there is no theoretical reason for this constraint)

		long e = 2;  // We will start at 2 and work our way up until we find an E that meets the constraints

		boolean foundE = false;
		while (!foundE) {

			// The following perform a standard GCD calculation -- Euclid's algorithm

			// Set the initial values of a and b to phiPQ and our current candidate for E
			long a = phiPQ;
			long b = e;

			long r = 1; // Initial starting value so we go through the loop at least once
			while (r != 0) { // When r reaches 0 then the gcd is the b value

				// Put the bigger of a and b into a and the smaller into b
				if (a < b) { // Swap them if they are in the wrong order
					long temp = a;
					a = b;
					b = temp;
				}

				r = a % b;  // Compute the remainder of the bigger / smaller
				a = r; // Replace a with the remainder -- b stays the same
			}
			// Recall the gcd is stored in b at this point
			if (b == 1) { // We are done if the gcd was 1
				foundE = true;
			} else { // Keep looking for an E value that works
				e++;
			}
		}

		//******************************************************************************
		// Find D
		//******************************************************************************
		// Generate D with the following properties:
		// 1.  0 < D <= PQ
		// 2.  (DE-1) is evenly divisible by phiPQ
		//    --> That is count up for D until we find a (DE-1)%phiPQ that equals 0

		long d = 1;  // Init to the starting range of possible D values
		
		// Keep looking until we find a D value that fits the constraints above
		while ((((d * e) - 1) % phiPQ) != 0) {
			d++;
		}

		//******************************************************************************
		// Display results of P, Q, E, D to screen
		//******************************************************************************
		System.out.println("P:" + p);
		System.out.println("Q:" + q);
		System.out.println("PQ:" + pq);
		System.out.println("phiPQ:" + phiPQ);
		System.out.println("E:" + e);
		System.out.println("D:" + d);


		//******************************************************************************
		// Encrypt the messsage using only the public key (E, PQ)
		//******************************************************************************
		
		// This part needs a plainTextMessage as well as the public key, (E, PQ)
		String plainTextMessage = "Hello World";
		long[] cipherTextMessage = new long[plainTextMessage.length()];

		// Encrypt each character of the message, one at a time
		for (int i = 0; i < plainTextMessage.length(); i++) {
			// Get the current character to encode and obtain its unicode (int) value
			// Note that the assumption we made above was that this unicode value was really
			//   just an ASCII value (i.e. in the range 0..255).
			char currentCharacter = plainTextMessage.charAt(i);
			int unicodeValue = (int) currentCharacter;
			
			// Compute (unicodeValue ^ E) mod (PQ)
			// This causes us some problems because if we perform ^E first it will blow the
			//   top off the int range (the long range as well) even for small E.  So we either
			//   need to use BigIntegers (we don't use this in this version) or we need to
			//   use another method that combines the power and mod together so we don't blow
			//   the top off the range.

			long tempE = e;			
			long cipherValue = 1;
			while (tempE > 0) {
				
				// If E is odd
				if ( (tempE % 2) == 1 ) {
					cipherValue *= unicodeValue;
					cipherValue %= pq;
				}
				tempE /= 2;  // Integer division
				unicodeValue *= unicodeValue;
				unicodeValue %= pq;
			}
	
			// Now save the cipher value -- we can't stick this into a String because the numbers
			//  may be larger than what String chars can hold (unless we only used 7 bits for P and Q)
			cipherTextMessage[i] = cipherValue;
		}

		// At this point cipherTextMessage contains the encoded message
		

		//******************************************************************************
		// Decrypt the cipher using only the private key (D, PQ)
		//******************************************************************************

		// To decode we need this cipherTextMessage as well as the private key, (d, n)
		String decodedTextMessage = "";

		// Encrypt each character of the message, one at a time
		for (int i = 0; i < cipherTextMessage.length; i++) {
			// Get the current character to encode and obtain its unicode (int) value
			long cipherValue = cipherTextMessage[i];
			
			// Compute (cipherValue ^ D) mod (PQ)
			// Again this is a problem with blowing the top off the range so we do the same
			//   thing as above.

			long tempD = d;
			long decodedValue = 1;
			while (tempD > 0) {
				
				// If D is odd
				if ( (tempD % 2) == 1 ) {
					decodedValue *= cipherValue;
					decodedValue %= pq;
				}
				tempD /= 2;  // Integer division
				cipherValue *= cipherValue;
				cipherValue %= pq;
			}
		

			// And then pretend the int is a unicode character and produce a char from it to add to our cipher string
			char decodedCharacter = (char) decodedValue;
			decodedTextMessage += decodedCharacter;
		}

		// At this point the decoded message is in decodedTextMessage
		System.out.println(decodedTextMessage);

		System.exit(0); // To make sure we shut down
	}

}