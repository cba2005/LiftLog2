package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.Toast;

/**
 * Created by bronhuston on 6/8/14.
 */
public class DayViewDialog extends DialogFragment {
    private int month;
    private int day;
    private int year;


    public DayViewDialog(int Month, int Day, int Year){
        this.month = Month;
        this.day = Day;
        this.year = Year;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
      //  Toast.makeText(dialog.getContext(),"got here",Toast.LENGTH_LONG).show();

        String date = "" + month + "/" + day + "/" + year;

        dialog.setTitle(date);
        dialog.setContentView(R.layout.day_view);
        //CalendarView calendar = (CalendarView) dialog.findViewById(R.id.calendarView);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


       // dialog.show();
        return dialog;
    }

}
