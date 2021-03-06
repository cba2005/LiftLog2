package edu.uscb.cs.cs185.LiftLog2.system;

import android.graphics.*;
import edu.uscb.cs.cs185.LiftLog2.R;

import java.io.*;
import java.util.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class ClassManager {
    Color c;
	static int red = 0xFFFF0000;
    static int darkBlue = 0xFF000080;
    static int fuschia = 0xFFFF00FF;
    static int purple = 0xFF660066;
    static int teal = 0xFF008080;
    static int blue = 0xFF0000FF;
   static int violet = 0xFF800080;
    static int lavender = 0xFF9999ff;
    static int salmon = 0xFFff9999;
    static int darkTurq = 0xFF024234;
    static int grey = 0xFF808080;
    static int maroon = 0xFF800000;
    public static final int[] COLORS = {red,blue ,darkTurq, purple,grey, teal, fuschia,violet,lavender,salmon,darkBlue,maroon};
	public static final int NUM_COLORS = 11;
	
	// minor change
	public static final String TAG = "CLASS_MANAGER";
	
	private ArrayList<Class> classes;
	private int numClasses;
	
	private String filePath;
	private String fileName;
	private File classFile;
	
	public ClassManager(String path) {
		debug("initiated class manager...");
		filePath = path;
		fileName = "classes.txt";
		classFile = new File(filePath+"/"+fileName);

		classes = new ArrayList<Class>();
		numClasses = 0;	
		
		loadClasses();
	}
	
	public void addClass(String name, int color){
		classes.add(new Class(name, color));
		debug("ADDING CLASS...");
		debug("class: "+name);
		debug("color: "+color);
		debug("CLASS ADDITION COMPLETE\n");
		numClasses++;
		saveClasses();
	}
	
	public void saveClasses() {
		try {
			if (!classFile.exists())
				new File(filePath).mkdirs();

			debug("saving classes...");
			FileOutputStream fileOutputStream = new FileOutputStream(classFile);
			BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
			debug("saving numClasses: "+numClasses);
			buffer.write(numClasses+"\n");
			for (Class c : classes) {
				buffer.write(c.getClassName()+"\n");
				buffer.write(c.getClassColor()+"\n");
			}

			buffer.close();
		}
		catch (Exception e) {
			debug("Exception caught trying to save: "+e);
		}
	}
	
	public void loadClasses() {
		if (!classFile.exists())
			return;
		try {
			debug("trying to load classes...");
			FileInputStream fileInputStream = new FileInputStream(classFile);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = buffer.readLine();
			numClasses = Integer.parseInt(line);
			debug("loaded numClasses: "+numClasses);
			line = buffer.readLine();
			while (line != null)
			{
				String className = line;
				line = buffer.readLine();
				String color = line;
				line = buffer.readLine();

				debug("loaded class:\n"+className+"\n"+color);
				classes.add(new Class(className, Integer.parseInt(color)));

			}
		}
		catch(Exception e) {
			debug("exception thrown loading classes: "+e);
		}	
	}
	
	public void addEventTo(String className) {
		
	}
	
	public void removeClass(String name) {
		if (contains(name)) {
			classes.remove(getClass(name));
			numClasses--;
		}
	}
	
	public int getNumClasses() {
		return numClasses;
	}
	
	public Class getClass(String name) {
		for (Class c : classes) {
			if (c.getClassName().compareTo(name) == 0) {
				debug("found class "+ c.getClassName());
				return c;
			}
		}
		return null;
	}
	
	public boolean contains(String name) {
		for (Class c : classes) {
			if (c.getClassName().compareTo(name) == 0)
				return true;
		}
		return false;
	}
	
	public void debug(String msg) {
		TestMain.debug(TAG+": "+msg);
	}
	
}
