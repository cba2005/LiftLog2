package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


/**
 * Created by Caressa on 6/6/2014.
 */
public class AddEventDialog extends DialogFragment{
        public AddEventDialog() {}

        @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
            super.onCreateDialog(savedInstanceState);

            // creating the fullscreen dialog
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_item);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            return dialog;
        }

}