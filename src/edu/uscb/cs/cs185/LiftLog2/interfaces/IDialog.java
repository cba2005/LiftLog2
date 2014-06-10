package edu.uscb.cs.cs185.LiftLog2.interfaces;

import edu.uscb.cs.cs185.LiftLog2.system.*;

/**
 * Created by ilovekpop on 6/9/2014.
 */
public interface IDialog {
	
	public static final String DEF_DESC = "no description";
	public static final int DEF_TYPE = EventManager.TYP_HOMEWORK;
	
	public void setDate(int y, int m, int d);
	public void setTime(int h, int min);
	
}
