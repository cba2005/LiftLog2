package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;
import android.view.Window;


import java.util.ArrayList;
import java.util.Calendar;
import android.os.Handler;

/**
 * Created by bronhuston on 6/8/14.
 */
public class CalendarDialog extends DialogFragment {


    public CalendarDialog() {}


    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calendar);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



        return dialog;
    }



}