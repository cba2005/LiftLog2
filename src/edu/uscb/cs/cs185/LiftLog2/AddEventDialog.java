package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;


/**
 * Created by Caressa on 6/6/2014.
 */
public class AddEventDialog extends DialogFragment{
    private MyActivity activity;
    private Button timeButton, dateButton;
    private TextView timeTextView, dateTextView;


        public AddEventDialog(MyActivity activity)
        {
            this.activity = activity;
        }

    private static String[] classesArray;
    @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);
            classesArray = getResources().getStringArray(R.array.classList);

            // creating the fullscreen dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_item);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            timeButton = (Button) dialog.findViewById(R.id.timeButton);
            dateButton = (Button) dialog.findViewById(R.id.dateButton);
            timeTextView = (TextView) dialog.findViewById(R.id.eventTime);
            dateTextView = (TextView) dialog.findViewById(R.id.eventDate);

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