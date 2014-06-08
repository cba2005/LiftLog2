package edu.uscb.cs.cs185.LiftLog2.system;

import java.util.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class ClassManager {
	
	// minor change
	public static final String TAG = "CLASS_MANAGER";
	
	private ArrayList<Class> classes;
	private int numClasses;
	
	public ClassManager() {
		debug("initiated class manager...YEAH");
		classes = new ArrayList<Class>();
		numClasses = 0;	
	}
	
	public void addClass(String name){
		classes.add(new Class(name));
		numClasses++;
	}
	
	public void removeClass(String name) {
		if (classExists(name)) {
			classes.remove(getClass(name));
			numClasses--;
		}
	}
	
	public Class getClass(String name) {
		for (Class c : classes) {
			if (c.getClassName() == name) {
				debug("found class "+ c.getClassName());
				return c;
			}
		}
		return null;
	}
	
	public boolean classExists(String name) {
		for (Class c : classes) {
			if (c.getClassName() == name)
				return true;
		}
		return false;
	}
	
	public void debug(String msg) {
		TestMain.debug(TAG+": "+msg);
	}
	
}
