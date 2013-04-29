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
import java.util.Scanner;

import com.cerrillostech.chaoticnumbers.quantanet.QuantaPacket;
import com.cerrillostech.chaoticnumbers.quantanet.QuantaPacket.Header;
import com.cerrillostech.chaoticnumbers.quantanet.QuantaPacket.Type;

public class ChaoticNumbers {
	private static Scanner key;
	public static void main(String args[]){
	//	QuantaNetTest();
	//	menu();
		QuantaPacket packet = new QuantaPacket(Header.BROADCAST, Type.CLIENT);
		System.out.println(packet.toString());
		if(packet.isBroadcast()){
			System.out.println("Is a broadcast packet");
		}
	}
	
	public static void menu(){
		key = new Scanner(System.in);
		System.out.print("Server mode or Client mode (s,c): ");
		String mode = key.next();
		if(mode.equals("s")){
			QuantaNet qn = new QuantaNet(true);
			qn.serverMode();
			System.out.print("\n\nType q to stop\n\n");
			String com = key.next();
			if(com.equals("q")){
				qn.closeServer();
				System.exit(0);
			}
		} else if(mode.equals("c")){
			QuantaNet qn = new QuantaNet(true);
			qn.clientMode();
			System.out.print("\n\nType q to stop\n\n");
			String com = key.next();
			if(com.equals("q")){
				qn.closeClient();
				System.exit(0);
			}
		} else {
			menu();
		}
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
