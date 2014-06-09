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
import android.os.Handler;
import android.widget.CalendarView;
import android.widget.Toast;

/**
 * Created by bronhuston on 6/8/14.
 */
public class CalendarDialog extends DialogFragment {

    private int mYear;
    private int mMonth;
    private int mDay;
    public CalendarDialog() {}


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());




        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calendar);
        CalendarView calendar = (CalendarView) dialog.findViewById(R.id.calendarView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub

                Toast.makeText(dialog.getContext(), "Selected Date is\n\n"
                                + dayOfMonth + " : " + month + " : " + year,
                        Toast.LENGTH_LONG).show();
                        dialog.dismiss();
            }
        });


        return dialog;
    }


}