package edu.uscb.cs.cs185.LiftLog2.system;

/**
 * Created by ilovekpop on 6/8/2014.
 */
public class Selfie {

	private String latitude, longitude, indexString;
	private Assignment assignment;

	public Selfie(String index, Assignment ass) {
		indexString = index;
		assignment = ass;
	}

	public String getIndexString() {
		return indexString;
	}
	
	public Assignment getAssignment() {
		return assignment;
	}

	public static String getPhotoIndex(int index2) {
		if (index2 < 10) {
			return "00"+index2;
		}
		else if (index2 >= 10 && index2 < 100) {
			return "0"+index2;
		}
		else {
			return ""+index2;
		}
	}
	
}
