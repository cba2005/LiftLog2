package edu.uscb.cs.cs185.LiftLog2.system;

import edu.uscb.cs.cs185.LiftLog2.*;

import java.util.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class TestMain {
	
	public static final String TAG = "TEST_MAIN";
	public static final boolean DEBUG_MODE = true;
	
	private String testPath = "test-res";
	
	public TestMain() {
		EventManager eventManager = new EventManager(testPath);
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(2014, 10, 11, 5, 22); // year, month, day, hour, min, sec
		
		eventManager.addActiveEvent(new Event(EventManager.TYP_HOMEWORK, "cs185", "hw4", "some shitty app", calendar));
		
		Calendar cal = Calendar.getInstance();
		cal.set(2014, 5, 8, 8, 19);
		
		eventManager.addActiveEvent(new Event(EventManager.TYP_EXAM, "cs154", "midterm1", "single cycle, washing machines, etc.", cal));
		
		eventManager.addActiveEvent(new Event(EventManager.TYP_PROJECT, "cs162", "assign6", "prolog and scala bullshit", EventManager.NEW_CALENDAR(2014, 3, 20, 11, 59)));
		eventManager.addActiveEvent(new Event(EventManager.TYP_PROJECT, "cs162", "assign7", "more prolog and scala bullshit smh", EventManager.NEW_CALENDAR(2014, 4, 17, 11, 59)));
		
		//eventManager.removeActiveEvent("hw4", "cs185");
		
		eventManager.completeEvent("midterm1", "cs154");
		
		debug("removed event and got here");
	}
	
	public static void main(String[] args) {
		debug("test started...yes this is dog");
		new TestMain();
	}
	
	public static void debug(String msg) {
		if (DEBUG_MODE)
			MyActivity.debug(TAG + ": " + msg);
	}
}
