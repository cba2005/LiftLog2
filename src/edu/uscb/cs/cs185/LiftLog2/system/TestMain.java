package edu.uscb.cs.cs185.LiftLog2.system;


import android.graphics.*;

import java.util.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class TestMain {
	
	public static final String TAG = "TEST_MAIN";
	public static final boolean DEBUG_MODE = true;
	
	public static final int YEAR_OFF = 1900;
	public static final int MONTH_OFF = 4;
	public static final int DAY_OFF = 153;
	
	private String testPath = "test-res";
	
	public TestMain() {
		EventManager eventManager = new EventManager(testPath);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 10, 11, 5, 22); // year, month, day, hour, min, sec
		
		eventManager.addEvent(new Event(EventManager.TYP_HOMEWORK, "cs185", "fuckin event man", "wat a stupid thing omg", calendar));
		
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 5, 8, 8, 19);
		
		eventManager.addEvent(new Event(EventManager.TYP_EXAM, "cs154", "WOO HOO", "turn down 4 wat", cal));
	}
	
	public static void main(String[] args) {
		debug("test started...yes this is dog");
		new TestMain();
	}
	
	public static void debug(String msg) {
		if (DEBUG_MODE)
			System.out.println(TAG+": "+msg);
	}
}
