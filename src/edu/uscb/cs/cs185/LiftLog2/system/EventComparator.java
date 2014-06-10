package edu.uscb.cs.cs185.LiftLog2.system;

import java.util.*;

/**
 * Created by ilovekpop on 6/9/2014.
 */
public class EventComparator implements Comparator<Event> {
	
	@Override
	public int compare(Event lhs, Event rhs) {
		if (lhs.getYear() < rhs.getYear())
			return -1;
		else if (lhs.getYear() > rhs.getYear())
			return 1;
		else {
			if (lhs.getMonth() < rhs.getMonth())
				return -1;
			else if (lhs.getMonth() > rhs.getMonth())
				return 1;
			else {
				if (lhs.getDay() < rhs.getDay())
					return -1;
				else if (lhs.getDay() > rhs.getDay())
					return 1;
				else
					return 0;
			}
		}
	}
}
