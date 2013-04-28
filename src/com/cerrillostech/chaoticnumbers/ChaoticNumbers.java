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
 * 
 * shuttleshuttle
 */

package com.cerrillostech.chaoticnumbers;
import java.math.BigInteger;

public class ChaoticNumbers {
	public static void main(String args[]){
		QuantaNetTest();
	}
	
	public static void QuantaNetTest(){
		QuantaNet qn = new QuantaNet(true);
		if(qn.checkUUID()){
			System.out.println("has uuid");
		} else {
			System.out.println("doenst have uuid");
		}
		try {
			qn.clientMode();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void LoadBalancerTest(){
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
