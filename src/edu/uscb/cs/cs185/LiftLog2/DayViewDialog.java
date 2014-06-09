package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import java.util.Calendar;

/**
 * Created by bronhuston on 6/8/14.
 */
public class DayViewDialog extends DialogFragment {
    private int month;
    private int day;
    private int year;
    private ListView list;
    private MyAdapter adapter;
    private MyActivity activity;
    private Button addEventButton;




    public DayViewDialog(int Month, int Day, int Year, MyActivity activity){
        this.month = Month;
        this.day = Day;
        this.year = Year;
        this.activity = activity;

    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);


        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
      //  Toast.makeText(dialog.getContext(),"got here",Toast.LENGTH_LONG).show();

        String date = "" + (month+1) + "/" + day + "/" + year;

        dialog.setTitle(date);
        dialog.setContentView(R.layout.day_view);


        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        list = (ListView) dialog.findViewById(R.id.listViewD);
        addEventButton = (Button) dialog.findViewById(R.id.addEventKnownDateButton);

        // Getting adapter by passing xml data ArrayList
        adapter = new MyAdapter(activity);//, songsList);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.editEventDialog();
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                c.set(year,month,day);
                activity.addEventKnownDateDialog(c);
            }
        });


       // dialog.show();
        return dialog;
    }

}
