package edu.uscb.cs.cs185.LiftLog2.system;

import android.graphics.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class Class {
	
	private String className;
	private long classColor;
	
	public Class(String name, long colorVal) {
		className = name;
		classColor = colorVal;
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
