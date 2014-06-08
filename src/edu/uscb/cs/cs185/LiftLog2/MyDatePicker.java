package edu.uscb.cs.cs185.LiftLog2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;



/**
 * Created by Caressa on 6/8/2014.
 */


public class MyDatePicker extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private TextView dateView;
    public static final String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    MyDatePicker(TextView dateView) {
        this.dateView = dateView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, 2013, 3, 17);
    }



    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String date = "";
        date = MONTHS[month];
        date += " " + String.valueOf(day);
        date += ", " + year;
        dateView.setText(date);
    }
}
