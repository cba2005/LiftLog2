package edu.uscb.cs.cs185.LiftLog2.system;

import android.graphics.*;

/**
 * Created by ilovekpop on 6/7/2014.
 */
public class Class {
	
	private String className;
	private Color classColor;
	
	public Class(String name) {
		className = name;	
	}
	
	public void setClassName(String name){
		className = name;
	}
	
	public void setClassColor(Color color){
		classColor = color;
	}
	
	public String getClassName() {
		return className;
	}
	
	public Color getClassColor() {
		return classColor;
	}
}
