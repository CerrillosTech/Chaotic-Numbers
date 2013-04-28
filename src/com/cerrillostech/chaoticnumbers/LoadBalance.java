/** 
 * Exponential Load Balancer
 * Made by Carlos Cerrillos
 * GitHub: CerrillosTech
 * 
 * The following class and methods will take a range of numbers that will be
 * used in calculations that are exponentially proportional to the time it takes to complete said calculations.
 * 
 * USAGE:
 * Create the object.
 * 		Valid arguments are as follows
 * 		({2,4,8,16,32,64,128,256}, BigInteger x, BigInteger y)
 * 		WHERE y is larger, normally much larger, than x
 * 
 * Invoke the ExponentialLoad method to get a BigInteger array that contains the start and end ranges for each core.
 * 
 * RETURNS:
 * 		BigIntegerArray that is a multiple of 2
 * 		{core_1_start, core_1_end, core_2_start, core_2_end, ... core_n_start, core_n_end}
 * 
 * EXCEPTIONS:
 * 		IllegalArgumentException
 * 
 * KNOWN ISSUES/BUGS:
 * 		If you wish to split up a small load (ie 0-100) for many cores (ie 16),
 * 		you may get issues where a core will calculate to & from the same number (ie 57-57)
 * 		OR WORSE, it may give a core a calculation that starts at a bigger number than the number it ends at (ie 57-54).
 * 		This may cause major problems if your program cannot correctly handle a range
 * 		of numbers where the start is larger than the end!
 * */
package com.cerrillostech.chaoticnumbers;
import java.math.BigInteger;

public class LoadBalance {
	private final int cores;
	private final BigInteger start, end;
	private boolean valid, done;
	public LoadBalance(int cores, BigInteger start, BigInteger end) {
		this.cores = cores;
		this.start = start;
		this.end = end;
		if(isValid(cores)){
			valid = true;
		} else {
			valid = false;
		}
		this.done = false;
	}
	public BigInteger[] ExponentialLoad() throws Exception {
		if(valid){
			return InitialBalance(start, end);
		} else {
			throw new IllegalArgumentException("Must be 2^n");
		}
	}
	private BigInteger[] InitialBalance(BigInteger startin, BigInteger endin){
		BigInteger list[] = new BigInteger[4];
		list[0] = startin;
		list[3] = endin;
		list[2] = ELBR(startin,endin);
		list[1] = list[2].subtract(new BigInteger("1"));
		if(this.cores==2){
			this.done = true;
			return list;
		} else {
			BigInteger RangesOut[] = list;
			while(!this.done){
				RangesOut = RecursiveBalance(RangesOut);
			}
			return RangesOut;
		}
	}
	
	private BigInteger[] RecursiveBalance(BigInteger[] RangesIn){
		int prevCores = (RangesIn.length)/2;
		if(prevCores==cores){
			this.done = true;
			return RangesIn;
		} else {
			BigInteger[] RangesOut = new BigInteger[RangesIn.length*2];
			for(int x = 0; x < RangesIn.length*2; x += 4){
				int[] prevpair = { (x/2), ((x/2)+1)};
				if(x==0){
					RangesOut[x] = RangesIn[prevpair[0]];
				} else {
					RangesOut[x] = RangesOut[x-1].add(BigInteger.ONE);
				}
				RangesOut[x+3] = RangesIn[prevpair[1]];
				
				RangesOut[x+2] = ELBR(RangesOut[x], RangesOut[x+3]);;
				RangesOut[x+1] = RangesOut[x+2].subtract(BigInteger.ONE);
			}
			return RangesOut;
		}
	}
	/* Behind the scenes methods... */
	private static BigInteger ELBR(BigInteger startin, BigInteger endin){		
		BigInteger output = BIsqrt(((endin.pow(2)).add(startin.pow(2))).divide(new BigInteger("2")));
		return output;
	}
	private static BigInteger BIsqrt(BigInteger n) {
		  BigInteger a = BigInteger.ONE;
		  BigInteger b = new BigInteger(n.shiftRight(5).add(new BigInteger("8")).toString());
		  while(b.compareTo(a) >= 0) {
		    BigInteger mid = new BigInteger(a.add(b).shiftRight(1).toString());
		    if(mid.multiply(mid).compareTo(n) > 0) b = mid.subtract(BigInteger.ONE);
		    else a = mid.add(BigInteger.ONE);
		  }
		  return a.subtract(BigInteger.ONE);
	}
	private boolean isValid(int cores) {
		int[] valids = { 2,4,8,16,32,64,128,256};
		for(int x = 0; x < valids.length; x++){
			if(cores==valids[x]){
				return true;
			}
		}
		return false;
	}
}
