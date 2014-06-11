package edu.uscb.cs.cs185.LiftLog2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import edu.uscb.cs.cs185.LiftLog2.interfaces.*;
import edu.uscb.cs.cs185.LiftLog2.system.*;

import java.util.Calendar;


/**
 * Created by Caressa on 6/8/2014.
 */


public class MyDatePicker extends DialogFragment
   	implements DatePickerDialog.OnDateSetListener {

    private TextView dateView;
	private IDialog iDialog;
	private int year, month, day;

    MyDatePicker(TextView dateView, IDialog iDialog, int year, int month, int day) {
        
		this.dateView = dateView;
		this.iDialog = iDialog;
		this.year = year;
		this.month = month;
		this.day = day;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of DatePickerDialog and return it
		MyActivity.debug("PICKER YEAR, MONTH, DAY: "+year+", "+month+", "+day);
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
		String date = Event.FORMAT_DATE(year, month+1, day);
		dateView.setText(date);
		
		if (iDialog != null)
			iDialog.setDate(year, month, day);
	}
	
}
