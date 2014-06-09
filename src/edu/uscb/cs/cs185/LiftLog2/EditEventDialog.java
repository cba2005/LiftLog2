package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;


/**
 * Created by Caressa on 6/6/2014.
 */
public class EditEventDialog extends DialogFragment{

    private MyActivity activity;
    private Button timeButton, dateButton;
    private TextView timeTextView, dateTextView;
    public static final String[] MONTHS ={"January","February","March","April","May","June","July","August","September","October","November","December"};

    public EditEventDialog(MyActivity activity)
    {
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_item);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        timeButton = (Button) dialog.findViewById(R.id.timeButton);
        dateButton = (Button) dialog.findViewById(R.id.dateButton);
        timeTextView = (TextView) dialog.findViewById(R.id.eventTime);
        dateTextView = (TextView) dialog.findViewById(R.id.eventDate);


        final Calendar c = Calendar.getInstance();
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

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.datePickerDialog(dateTextView);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.timePickerDialog(timeTextView);
            }
        });


        return dialog;
    }

}