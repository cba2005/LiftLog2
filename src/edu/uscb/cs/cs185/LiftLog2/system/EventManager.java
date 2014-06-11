package edu.uscb.cs.cs185.LiftLog2.system;

import java.io.*;
import java.util.*;

/**
 * Created by ilovekpop on 6/8/2014.
 */
public class EventManager {
	
	public static final String TAG = "EVENT_MANAGER";
	
	public static final int STAT_INCOMPLETE = 0;
	public static final int STAT_COMPLETE = 1;
	
	public static final int TYP_HOMEWORK = 0;
	public static final int TYP_PRESENTATION = 1;
	public static final int TYP_PROJECT = 2;
	public static final int TYP_EXAM = 3;
	public static final String[] EVENT_TYPES = {"Homework", "Presentation", "Project", "Exam"};
	
	public static String EVENT_FILE_PATH;
	public static final String ACTIVE_EVENT_FILE_NAME = "activeEvents.txt";
	public static final String INACTIVE_EVENT_FILE_NAME = "inactiveEvents.txt";
	
	private int numActiveEvents;
	private int numInactiveEvents;
	private ArrayList<Event> activeEvents;
	private ArrayList<Event> inactiveEvents;
	private File activeEventFile;
	private File inactiveEventFile;
	private ArrayList<Event> events;
	private ClassManager classManager;
	
	public EventManager(String path) {
		classManager = new ClassManager(path);
		activeEvents = new ArrayList<Event>();
		inactiveEvents = new ArrayList<Event>();
		events = new ArrayList<Event>();
		EVENT_FILE_PATH = path;
		activeEventFile = new File(EVENT_FILE_PATH+"/"+ ACTIVE_EVENT_FILE_NAME);
		inactiveEventFile = new File(EVENT_FILE_PATH+"/"+INACTIVE_EVENT_FILE_NAME);
		numActiveEvents = 0;
		numInactiveEvents = 0;
		loadActiveEvents();
		loadInactiveEvents();
	}
	
	public void loadActiveEvents() {
		if (!activeEventFile.exists())
			return;
		try {
			debug("trying to load activeEvents...");
			FileInputStream fileInputStream = new FileInputStream(activeEventFile);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = buffer.readLine();
			numActiveEvents = Integer.parseInt(line);
			line = buffer.readLine();
			while (line != null)
			{
				String className = line;
				String name = buffer.readLine();
				String desc = buffer.readLine();
				String status = buffer.readLine();
				String type = buffer.readLine();
				String date = buffer.readLine();
				debug("LOADED DATE DUE: "+date);
				String time = buffer.readLine();
				line = buffer.readLine();
				
				debug("loaded event:\n"+className+"\n"+name+"\n"+desc+"\n"+status+"\n"+type+"\n"+date+"\n"+time+"\n");
				
				int year = Integer.parseInt(date.substring(0, 4));
				int month = Integer.parseInt(date.substring(5, 7));
				debug("LOADED MONTH: "+month);
				int day = Integer.parseInt(date.substring(8));
				
				int hour = Integer.parseInt(time.substring(0,2));
				int minute = Integer.parseInt(time.substring(3));

				Event e = new Event(Integer.parseInt(type), className, name, desc, NEW_CALENDAR(year, month-1, day, hour, minute));
				activeEvents.add(e);
				events.add(e);
				sortEvents();
				if(!classManager.contains(className)) {
					classManager.addClass(className, ClassManager.COLORS[classManager.getNumClasses()%ClassManager.NUM_COLORS]);
					e.setClass_(classManager.getClass(className));
				}
				else {
					e.setClass_(classManager.getClass(className));
				}
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public void loadInactiveEvents() {
		if (!inactiveEventFile.exists())
			return;
		try {
			debug("trying to load inactiveEvents...");
			FileInputStream fileInputStream = new FileInputStream(inactiveEventFile);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = buffer.readLine();
			numInactiveEvents = Integer.parseInt(line);
			line = buffer.readLine();
			while (line != null)
			{
				String className = line;
				String name = buffer.readLine();
				String desc = buffer.readLine();
				String status = buffer.readLine();
				String type = buffer.readLine();
				String date = buffer.readLine();
				String time = buffer.readLine();
				line = buffer.readLine();

				debug("loaded event:\n"+className+"\n"+name+"\n"+desc+"\n"+status+"\n"+type+"\n"+date+"\n"+time+"\n");

				int year = Integer.parseInt(date.substring(0, 4));
				int month = Integer.parseInt(date.substring(5, 7));
				int day = Integer.parseInt(date.substring(8));

				int hour = Integer.parseInt(time.substring(0,2));
				int minute = Integer.parseInt(time.substring(3));

				Event e = new Event(Integer.parseInt(type), className, name, desc, NEW_CALENDAR(year, month-1, day, hour, minute));
				inactiveEvents.add(e);
				events.add(e);
				sortEvents();
				if(!classManager.contains(className)) {
					classManager.addClass(className, ClassManager.COLORS[classManager.getNumClasses()%ClassManager.NUM_COLORS]);
					e.setClass_(classManager.getClass(className));
				}
				else {
					e.setClass_(classManager.getClass(className));
				}
			}
		}
		catch(Exception e) {

		}	
	}
	
	public void saveActiveEvents() {
		try {
			if (!activeEventFile.exists())
				new File(EVENT_FILE_PATH).mkdirs();

			debug("saving activeEvents...");
			FileOutputStream fileOutputStream = new FileOutputStream(activeEventFile);
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			buffer.write(numActiveEvents +"\n");
			for (Event event : activeEvents) {
				buffer.write(event.getClassName()+"\n");
				buffer.write(event.getName()+"\n");
				buffer.write(event.getDescription()+"\n");
				buffer.write(event.getStatus()+"\n");
				buffer.write(event.getType()+"\n");
				debug("WRITING DATE DUE: "+event.getDateDue());
				buffer.write(event.getDateDue()+"\n");
				buffer.write(event.getTimeDue()+"\n");
			}

			buffer.close();
		}
		catch (Exception e) {
			debug("Exception caught trying to save: "+e);
		}	
	}
	
	public void saveInactiveEvents() {
		try {
			if (!inactiveEventFile.exists())
				new File(EVENT_FILE_PATH).mkdirs();

			debug("saving inactiveEvents...");
			FileOutputStream fileOutputStream = new FileOutputStream(inactiveEventFile);
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			buffer.write(numInactiveEvents +"\n");
			for (Event event : inactiveEvents) {
				buffer.write(event.getClassName()+"\n");
				buffer.write(event.getName()+"\n");
				buffer.write(event.getDescription()+"\n");
				buffer.write(event.getStatus()+"\n");
				buffer.write(event.getType()+"\n");
				buffer.write(event.getDateDue()+"\n");
				buffer.write(event.getTimeDue()+"\n");
			}

			buffer.close();
		}
		catch (Exception e) {
			debug("Exception caught trying to save: "+e);
		}
	}
	
	public void addActiveEvent(Event e) {
		debug("adding active event...");
		if (activeEventsContains(e.getName(), e.getClassName())) {
			// throw exception maybe
			debug("event already exists: "+e.getName()+" : "+e.getClassName());
			return;
		}
		activeEvents.add(e);
		events.add(e);
		sortEvents();
		String className = e.getClassName();
		if(!classManager.contains(className)) {
			classManager.addClass(className, ClassManager.COLORS[classManager.getNumClasses()%ClassManager.NUM_COLORS]);
			e.setClass_(classManager.getClass(className));
		}
		else {
			e.setClass_(classManager.getClass(className));	
		}
		numActiveEvents++;
		saveActiveEvents();
	}

	public void addInactiveEvent(Event e) {
		debug("adding inactive event...");
		if (inactiveEventsContains(e.getName(), e.getClassName())) {
			// throw exception maybe
			debug("event already exists: "+e.getName()+" : "+e.getClassName());
			return;
		}
		inactiveEvents.add(e);
		if (!events.contains(e)) {
			events.add(e);
			sortEvents();
		}
		numInactiveEvents++;
		saveActiveEvents();
	}
	
	public void removeEvent(String name, String className) {
		Event e = getEvent(name, className);
		if (!events.contains(e))
			return;
		if (e.getStatus() != STAT_COMPLETE) 
			removeActiveEvent(name, className);
		else
			removeInactiveEvent(name, className);	
	}
	
	public void removeActiveEvent(String name, String className) {
		Event e = getActiveEvent(name, className);		
		if (!activeEvents.contains(e)) {
			debug("activeEvents doesn't contain: "+name);
			return;
		}
		activeEvents.remove(e);
		events.remove(e);
		sortEvents();
		numActiveEvents--;
		saveActiveEvents();
	}
	
	public void removeInactiveEvent(String name, String className) {
		Event e = getInactiveEvent(name, className);
		if (!inactiveEvents.contains(e)) {
			debug("inactiveEvents doesn't contain: "+name);
			return;
		}
		inactiveEvents.remove(e);
		events.remove(e);
		sortEvents();
		numInactiveEvents--;
		saveInactiveEvents();	
	}
	
	public void completeEvent(String name, String className) {
		Event e = getEvent(name, className);
		if (e == null) {
			// exception??? you fucked up, son.
			return;
		}
		e.setStatus(STAT_COMPLETE);
		removeActiveEvent(name, className);
		addInactiveEvent(e);
		saveActiveEvents();
		saveInactiveEvents();
	}
	
	/*
		looks up if an event exists in the ACTIVE event list
	 */
	public boolean activeEventsContains(String name, String className) {
		for (Event e: activeEvents) {
			if (e.getName().compareTo(name) == 0 && e.getClassName().compareTo(className) == 0) {
				return true;
			}
		}
		return false;
	}

	public boolean inactiveEventsContains(String name, String className) {
		for (Event e: inactiveEvents) {
			if (e.getName().compareTo(name) == 0 && e.getClassName().compareTo(className) == 0) {
				return true;
			}
		}
		return false;
	}
	
	public Event getActiveEvent(String name, String className) {
		for (Event e : activeEvents) {
			if (e.getName().compareTo(name) == 0 && e.getClassName().compareTo(className) == 0) {
				return e;
			}
		}
		return null;
	}

	public Event getInactiveEvent(String name, String className) {
		for (Event e : inactiveEvents) {
			if (e.getName().compareTo(name) == 0 && e.getClassName().compareTo(className) == 0) {
				return e;
			}
		}
		return null;
	}

	public Event getEvent(String name, String className) {
		for (Event e : events) {
			if (e.getName().compareTo(name) == 0 && e.getClassName().compareTo(className) == 0) {
				return e;
			}
		}
		return null;
	}
	
	public static String typeToString(int t) {
		switch(t) {
			case TYP_EXAM:
				return "EXAM";
			case TYP_HOMEWORK:
				return "HOMEWORK";
			case TYP_PRESENTATION:
				return "PRESENTATION";
			case TYP_PROJECT:
				return "PROJECT";
			default:
				return "WAT";
		}
	}
	
	/*
		will return null if empty, handle accordingly
	 */
	public ArrayList<Event> getEventsForDate(Calendar c) {
		ArrayList<Event> list = new ArrayList<Event>();
		String date = Event.DATE_FORMAT.format(c);
		debug("TARGET DATE: "+date);
		for (Event e : events) {
			debug("NO MATCH EVENT DATE: "+e.getDateDue());
			if (e.getDateDue().compareTo(date) == 0) {
				debug("MATCH EVENT DATE: "+e.getDateDue());
				list.add(e);
			}
		}
		if (list.isEmpty())
			debug("LIST IS EMPTY");
		return list;
	}

	/*
		will return null if empty, handle accordingly
	 */
	public ArrayList<Event> getInactiveEventsForDate(Calendar c) {
		ArrayList<Event> list = new ArrayList<Event>();
		String date = Event.DATE_FORMAT.format(c);
		for (Event e : inactiveEvents) {
			if (e.getDateDue().compareTo(date) == 0)
				list.add(e);
		}
		return list;
	}
	
	public ArrayList<Event> getActiveEvents() {
		return activeEvents;
	}
	
	public ArrayList<Event> getInactiveEvents() {
		return inactiveEvents;
	}
	
	public void sortEvents() {
		Collections.sort(events, new EventComparator());	
	}
	
	public ArrayList<Event> getEvents() { 
		return events; 
	}
	
	public int getNumActiveEvents() {
		return numActiveEvents;
	}
	
	public int getNumInactiveEvents() {
		return numInactiveEvents;
	}
	
	public int getNumTotalEvents() { return numActiveEvents + numInactiveEvents; }
	
	public ClassManager getClassManager() {
		return classManager;
	}
	
	public static Calendar NEW_CALENDAR(int year, int month, int day, int hour, int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, hour, min);
		return calendar;
	}
	
	public void debug(String msg) {
		TestMain.debug(TAG+": "+msg);
	}
	
}
