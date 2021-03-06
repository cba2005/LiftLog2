package edu.uscb.cs.cs185.LiftLog2.system;


import android.graphics.drawable.*;
import edu.uscb.cs.cs185.LiftLog2.*;

import java.text.*;
import java.util.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class Event {
	
	public static final String TAG = "EVENT";
	
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");
	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	
	private int type;
	private Class class_;
	private String className;
	private String name;
	private String description;
	private int status;
	private String dateDue;
	private String timeDue;
	private String formattedDate;
	private String formattedTime;
	
	private Calendar calendar;
	
	public Event(int type, int status, String className, String name, String description, Calendar c) {
		
		debug("ADDING CALENDAR: "+c.getTime());
		
		this.type = type;
		this.status = status;
		this.className = className;
		this.name = name;
		this.dateDue = DATE_FORMAT.format(c.getTime());
		this.timeDue = TIME_FORMAT.format(c.getTime());
		this.description = description;
		this.calendar = c;
		this.formattedDate = FORMAT_DATE(getYear(), getMonth(), getDay());
		this.formattedTime = FORMAT_TIME(getHour(), getMinutes());
		debug("DATE: "+MONTHS[getMonth()-1]+" "+getDay()+", "+getYear());
		debug("TIME: "+ FORMAT_TIME(getHour(), getMinutes()));
		

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
		timeDue = TIME_FORMAT.format(calendar.getTime());
		formattedDate = FORMAT_DATE(getYear(), getMonth(), getDay());
		formattedTime = FORMAT_TIME(getHour(), getMinutes());
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
		debug("GETTING HOUR: "+timeDue.substring(0, 2));
		return Integer.parseInt(timeDue.substring(0,2));
	}
	
	public int getMinutes() {
		debug("GETTING MINUTES: "+timeDue.substring(3));
		return Integer.parseInt(timeDue.substring(3));
	}
	
	public Class getClass_() {
		return class_;
	}
	
	public String getFormattedDate() {
		return formattedDate;
	}
	
	public String getFormattedTime() { 
		DateFormat dFormat = new SimpleDateFormat("h:mm a");
		return dFormat.format(calendar.getTime());
	}
	
	// should've used calendar and dateformat i fucked up
	public static String FORMAT_DATE(int year, int monthIndex, int day) {
		return MONTHS[monthIndex-1]+" "+day+", "+year; 
	}
	
	// pretty retarded man o well, will fix in future iterations maybe
	public static String FORMAT_TIME(int hour, int minute) {
		String min;
		if (minute < 10)
			min = "0"+minute;
		else
			min = ""+minute;
		String ampm = "AM";
		if (hour > 12) {
			hour = hour % 12;
			ampm = "PM";		
		}
		else if (hour == 0) {
			hour = 12;
			ampm = "AM";
		}
		else if (hour == 12)
			ampm = "PM";
		return hour+":"+min+" "+ampm;
	}

	public void debug(String msg) {
		TestMain.debug(TAG+": "+msg);
	}
	
}
