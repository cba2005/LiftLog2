package edu.uscb.cs.cs185.LiftLog2.system;

import edu.uscb.cs.cs185.LiftLog2.*;

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
	public static final int TYP_PROJECT = 3;
	public static final int TYP_EXAM = 4;
	
	public static String EVENT_FILE_PATH;
	public static String EVENT_FILE_NAME;
	
	private int numEvents;
	private ArrayList<Event> events;
	private File eventFile;
	private ClassManager classManager;
	
	public EventManager(String path) {
		classManager = new ClassManager(path);
		events = new ArrayList<Event>();
		EVENT_FILE_PATH = path;
		EVENT_FILE_NAME = "events.txt";
		eventFile = new File(EVENT_FILE_PATH+"/"+EVENT_FILE_NAME);
		numEvents = 0;	
		loadEvents();
	}
	
	public void loadEvents() {
		if (!eventFile.exists())
			return;
		try {
			debug("trying to load events...");
			FileInputStream fileInputStream = new FileInputStream(eventFile);
			//DataInputStream buffer = new DataInputStream(fileInputStream);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = buffer.readLine();
			numEvents = Integer.parseInt(line);
			line = buffer.readLine();
			while (line != null)
			{
				String className = line;
				line = buffer.readLine();
				String name = line;
				line = buffer.readLine();
				String desc = line;
				line = buffer.readLine();
				String status = line;
				line = buffer.readLine();
				String type = line;
				line = buffer.readLine();
				String date = line;
				line = buffer.readLine();
				String time = line;
				line = buffer.readLine();
				
				debug("loaded event:\n"+className+"\n"+name+"\n"+desc+"\n"+status+"\n"+type+"\n"+date+"\n"+time+"\n");
				
				int year = Integer.parseInt(date.substring(0, 4));
				int month = Integer.parseInt(date.substring(5, 7));
				int day = Integer.parseInt(date.substring(8));
				debug("YEAR FOUND: "+year);
				debug("MONTH FOUND: "+month);
				debug("DAY FOUND: "+day);
				
				int hour = Integer.parseInt(time.substring(0,2));
				int minute = Integer.parseInt(time.substring(3));
				debug("HOUR FOUND: "+hour);
				debug("MINUTE FOUND: "+minute);
				
				Calendar c = Calendar.getInstance();
				c.set(year, month, day, hour, minute);
				
				events.add(new Event(Integer.parseInt(type), className, name, desc, c));
			}
		}
		catch(Exception e) {
			
		}
	}
	
	public void saveEvents() {
		try {
			if (!eventFile.exists())
				new File(EVENT_FILE_PATH).mkdirs();

			debug("saving events...");
			FileOutputStream fileOutputStream = new FileOutputStream(eventFile);
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			//DataOutputStream buffer = new DataOutputStream(fileOutputStream);
			buffer.write(numEvents+"\n");
			for (Event event : events) {
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
	
	public void addEvent(Event e) {
		debug("adding event...");
		events.add(e);
		String className = e.getClassName();
		if(!classManager.contains(className)) {
			classManager.addClass(className, ClassManager.COLORS[classManager.getNumClasses()%ClassManager.NUM_COLORS]);
			e.setClass_(classManager.getClass(className));
		}
		else {
			e.setClass_(classManager.getClass(className));	
		}
		numEvents++;
		saveEvents();
	}
	
	public void removeEvent(String name) {
		events.remove(getEvent(name));
		numEvents--;
	}
	
	public void completeEvent(Event e) {
		
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
			if (e.getName() == name) {
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
	
	public void debug(String msg) {
		TestMain.debug(TAG+": "+msg);
	}
	
}
