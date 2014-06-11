package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by bronhuston on 6/9/14.
 */
public class EditEventKnownDateDialog extends DialogFragment {

    private MyActivity activity;
    private Button timeButton, dateButton, cancelButton, editEventButton;
    private TextView timeTextView, dateTextView;
    private AutoCompleteTextView className;
    private Calendar c;
    public static final String[] MONTHS ={"January","February","March","April","May","June","July","August","September","October","November","December"};
	private int year, month, day, hour, minute;

    public EditEventKnownDateDialog(MyActivity activity, Calendar c)
    {
        this.activity = activity;
        this.c = c;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_known_date_item);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        timeButton = (Button) dialog.findViewById(R.id.timeButton);
        cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        editEventButton = (Button) dialog.findViewById(R.id.editEventButton);
        timeTextView = (TextView) dialog.findViewById(R.id.eventTime);
        dateTextView = (TextView) dialog.findViewById(R.id.eventDateKnownDate);
        className = (AutoCompleteTextView) dialog.findViewById(R.id.acCourseName);
        className.setThreshold(1);

        setDateTime();
        setListeners(dialog);
        startAutoComplete();
        return dialog;
    }


    public void startAutoComplete() {
        //make autocomplere adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.classList));
        className.setAdapter(adapter);

    }


    public void setDateTime()
    {
        //pull from text file

        /*String date = "";
        date = MONTHS[month];
        date+= " " + String.valueOf(day);
        date += " " + year;
        dateTextView.setText(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        String time = hour + ":"+"00";
        timeTextView.setText(time);*/

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = "";
        date = MONTHS[month];
        date+= " " + String.valueOf(day);
        date += " " + year;
        dateTextView.setText(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        String time = hour + ":"+"00";
        timeTextView.setText(time);

    }

    public void setListeners(final Dialog dialog)
    {
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.timePickerDialog(timeTextView, null, hour, minute);
            }
        });

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.editEvent();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
