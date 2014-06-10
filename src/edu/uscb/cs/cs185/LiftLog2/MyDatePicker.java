package edu.uscb.cs.cs185.LiftLog2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import edu.uscb.cs.cs185.LiftLog2.interfaces.*;

import java.util.Calendar;


/**
 * Created by Caressa on 6/8/2014.
 */


public class MyDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TextView dateView;
	private IDialog iDialog;
	
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    MyDatePicker(TextView dateView, IDialog iDialog) {
        
		this.dateView = dateView;
		this.iDialog = iDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
		String date = "";
		date = MONTHS[month];
		date += " " + String.valueOf(day);
		date += ", " + year;
		dateView.setText(date);
		
		if (iDialog != null)
			iDialog.setDate(year, month, day);
	}
	
}
