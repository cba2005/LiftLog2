package edu.uscb.cs.cs185.LiftLog2.system;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class TestMain {
	
	public static final String TAG = "TEST_MAIN";
	public static final boolean DEBUG_MODE = true;
	
	public TestMain() {
		
	}
	
	public static void main(String[] args) {
		debug("test started...");	
	}
	
	public static void debug(String msg) {
		if (DEBUG_MODE)
			System.out.println(TAG+": "+msg);
	}
	
}
