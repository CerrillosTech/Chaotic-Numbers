/** 
 * Chaotic Numbers
 * Friendly Name: Prime Number Generator
 * Made by Carlos Cerrillos
 * GitHub: CerrillosTech
 * 
 * When completed, this program will be able to compute large ranges of prime numbers.
 * It will also include networking features that will allow results
 * to be stored in databases and clustering features that will allow the calculations to be split
 * across multiple cores/nodes.
 * 
 * 
 * CURRENT FUNCTIONS:
 * 		Exponential Load Balancing.
 */

package com.cerrillostech.chaoticnumbers;
import java.math.BigInteger;

public class ChaoticNumbers {
	public static void main(String args[]){
		//Testing Load Balancer
		LoadBalance load = new LoadBalance(8, BigInteger.ZERO, new BigInteger("10000"));
		try {
			BigInteger[] ranges = load.ExponentialLoad();
			for(int x = 0; x < ranges.length; x++){
				System.out.println("" + ranges[x]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
