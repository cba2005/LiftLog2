package edu.uscb.cs.cs185.LiftLog2.system;

import android.graphics.*;

import java.util.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class Class {
	
	private String className;
	private long classColor;
	private ArrayList<Event> events;
	
	public Class(String name, long colorVal) {
		className = name;
		classColor = colorVal;
		events = new ArrayList<Event>();
	}
	
	public void addEvent(Event e) {
		events.add(e);
	}

	public boolean contains(String name) {
		for (Event e: events) {
			if (e.getName().compareTo(name) == 0) {
				return true;
			}
		}
		return false;
	}

	public Event getEvent(String name) {
		for (Event e : events) {
			if (e.getName().compareTo(name) == 0) {
				return e;
			}
		}
		return null;
	}
	
	public void setClassName(String name){
		className = name;
	}
	
	public void setClassColor(int color){
		classColor = color;
	}
	
	public String getClassName() {
		return className;
	}
	
	public long getClassColor() {
		return classColor;
	}
}
