package edu.uscb.cs.cs185.LiftLog2.system;


import java.sql.*;
import java.text.*;
import java.util.*;
import java.util.Date;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class Event {
	
	public static final String TAG = "EVENT";
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("hh:mm");
	
	private int type;
	private Class class_;
	private String className;
	private String name;
	private String description;
	private int status;
	private String dateDue;
	private String timeDue;
	
	private Calendar calendar;
	
	public Event(int type, String className, String name, String description, Calendar c) {
		status = EventManager.STAT_INCOMPLETE;
		
		this.type = type;
		this.className = className;
		this.name = name;
		this.dateDue = DATE_FORMAT.format(c.getTime());
		this.timeDue = TIME_FORMAT.format(c.getTime());
		this.description = description;
		this.calendar = c;

		debug("CREATING EVENT...");
		debug("class: "+getClassName());
		debug("name: "+getName());
		debug("desc: "+getDescription());
		debug("type: "+EventManager.typeToString(getType()));
		debug("date: "+getDateDue());
		debug("time: "+getTimeDue());
		debug("calendar: "+getCalendar().getTime());
		debug("EVENT CREATION COMPLETE\n");
	}	
	
	public void setType(int t) {
		type = t;
	}

	public void setName(String n) {
		name = n;
	}
	
	public void setDescription(String d) {
		description = d;
	}
	
	public void setStatus(int s) {
		status = s;
	}
	
	private void setDateDue(String d) {
		dateDue = d;
	}
	
	private void setTimeDue(String t) {
		timeDue = t;
	}
	
	public void setCalendar(Calendar c){
		calendar = c;
		dateDue = DATE_FORMAT.format(calendar.getTime());
		timeDue = DATE_FORMAT.format(calendar.getTime());
	}
	
	public void setClassName(String n) {
		className = n;
	}
	
	public void setClass_(Class c) {
		class_ = c;
	}
	
	public int getType() {
		return type;
	}
	
	public String getClassName() {
		return className;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String getDateDue() {
		return dateDue;
	}
	
	public String getTimeDue() {
		return timeDue;
	}
	
	public Calendar getCalendar() {
		return calendar;
	}
	
	public int getYear() {
		return Integer.parseInt(dateDue.substring(0, 4));
	}
	
	public int getMonth() {
		return Integer.parseInt(dateDue.substring(5, 7));
	}
	
	public int getDay() {
		return Integer.parseInt(dateDue.substring(8));
	}
	
	public int getHour() {
		return Integer.parseInt(timeDue.substring(0,2));
	}
	
	public int getMinutes() {
		return Integer.parseInt(timeDue.substring(3));
	}
	
	public Class getClass_() {
		return class_;
	}

	public void debug(String msg) {
		TestMain.debug(TAG+": "+msg);
	}
	
}
