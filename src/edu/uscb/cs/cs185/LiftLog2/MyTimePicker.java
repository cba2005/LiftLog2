package edu.uscb.cs.cs185.LiftLog2;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.TextView;
import edu.uscb.cs.cs185.LiftLog2.interfaces.*;
import edu.uscb.cs.cs185.LiftLog2.system.*;

import java.util.Calendar;


/**
 * Created by Caressa on 6/8/2014.
 */


public class MyTimePicker extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {

    private TextView timeView;
	private IDialog iDialog;
	private int hour, minute;

    MyTimePicker(TextView timeView, IDialog iDialog, int hour, int minute) {
        this.timeView = timeView;
		this.iDialog = iDialog;
		this.hour = hour;
		this.minute = minute;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time = Event.FORMAT_TIME(hour, minute);
		timeView.setText(time);
		
		if (iDialog != null)
			iDialog.setTime(hour, minute);
    }
}
