package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Window;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.os.Handler;
import android.widget.CalendarView;
import android.widget.Toast;

/**
 * Created by bronhuston on 6/8/14.
 */
public class CalendarDialog extends DialogFragment {
    private MyActivity activity;

    private long date;
    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    int done = 0;
    public CalendarDialog(MyActivity activity) {
        this.activity = activity;
    }

    public int getmYear()
    {
        return mYear;
    }

    public int getmMonth()
    {
        return mMonth;
    }

    public int getmDay()
    {
        return mDay;
    }

    public int getDone()
    {
        return done;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calendar);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final CalendarView calendar = (CalendarView) dialog.findViewById(R.id.calendarView);
        date = calendar.getDate();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth)
            {
                if(calendar.getDate() != date)
                {
                    done = 1;
                    mMonth = month;
                    mYear = year;
                    mDay = dayOfMonth;
                    dialog.dismiss();
                    activity.openDayViewDialog(mMonth, mDay, mYear);
                }

            }
        });


        return dialog;
    }


}