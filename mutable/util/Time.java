/** Ben F Rayfield offers this software opensource MIT license */
package mutable.util;

import java.text.DecimalFormat;

public class Time{
	private Time(){}
	
	private static long lastNanoId = 1;
	
	public static final long nanoTimeOfY1970;
	
	public static final int secondsPerDay = 24*60*60;
	
	protected static final DecimalFormat secondsFormat  = new DecimalFormat("0000000000.0000000");
	
	protected static final DecimalFormat stardateFormat = new DecimalFormat("00000.000000000000");
	
	static{
		nanoTimeOfY1970 = System.nanoTime()-System.currentTimeMillis()*1000000;
	}
	
	/** Seconds since year 1970
	with relative nanosecond precision (System.nanoTime)
	and absolute few milliseconds precision (System.currentTimeMillis).
	<br><br>
	Practically, at least in normal computers in year 2011, this has about microsecond precision
	because you can only run it a few million times per second.
	TODO test it again on newer computers.
	*/
	public static double now(){
		return nowNano()*1e-9;
		
		//TODO optimize by caching the 2 start numbers into 1 double */
		//long nanoDiff = System.nanoTime()-startNano;
		//return .001*startMillis + 1e-9*nanoDiff; 
	}
	
	public static long nowNano(){
		return System.nanoTime()-nanoTimeOfY1970;
	}
	
	/** nanoseconds since year 1970 or 1 nanosecond since last call, whichever is later. Useful for 64 bit ids.
	FIXME throw if stored id on harddrive is bigger than this since clock can be reset wrongly such as
	my dual boot of ubuntu and win7 keeps setting windows time ahead a few hours after I come back fron ubuntu,
	probably the time offset between GMT and my local timezone, and when I click to sync with internet time in windows
	it goes back those few hours. If I modify a file before resetting clock by internet, file modified time is few hours
	in future, and after setting time by internet, those file modified times are still in future.
	Similarly, these ids ccould get set in the future by a wrong clock then when the clock is adjusted
	it would try to create an earlier time, which violates the order of ids which is used to
	represent merkle forest so would break that. Therefore, if id on harddrive is bigger than this,
	should throw here, but only check harddrive once at JVM start and make sure ids generated here
	are only ascending during run of JVM. 
	*/
	public static synchronized long nowNanoAsId(){
		return lastNanoId = Math.max(nowNano(),lastNanoId+1);
	}
	
	public static String timeStr(){
		return timeStr(now());
	}
	
	public static String timeStr(double time){
		return secondsFormat.format(time);
	}
	
	public static String stardateStr(){
		return stardateStr(now());
	}
	
	/** number of 24 hour blocks since year 1970 */
	public static String stardateStr(double time){
		return stardateFormat.format(time/secondsPerDay);
	}

	/** Uses Thread.sleep(milliseconds,nanoseconds) for extra accuracy,
	but don't count on Java running threads often enough to use the extra accuracy on all computers.
	*/
	public static void sleep(double seconds) throws InterruptedException{
		if(seconds <= 0) return;
		double millis = seconds*1e3;
		long millisL = (long)millis;
		millis -= millisL;
		double nanos = millis*1e6;
		int nanosI = (int)Math.round(nanos);
		Thread.sleep(millisL, nanosI);
	}
	
	public static void sleepNoThrow(double seconds){
		try{
			sleep(seconds);
		}catch(InterruptedException e){}
	}

}