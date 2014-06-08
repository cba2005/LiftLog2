package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Caressa on 6/6/2014.
 */
public class AddEventDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //Button button = (Button) findViewById(R.id.);
        //button.setOnClickListener(new View.OnClickListener() {
        //  public void onClick(View v) {
        //    showDialog();
        //}
        //});
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    void showDialog() {
/*        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        DialogFragment newFragment = MyDialogFragment.newInstance();
        newFragment.show(ft, "dialog");*/
    }

    public void show(FragmentManager supportFragmentManager, String newEvent) {
    }

    public static class MyDialogFragment extends DialogFragment {

        static MyDialogFragment newInstance() {
            MyDialogFragment f = new MyDialogFragment();
            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.add_item, container, false);
            return v;
        }

    }
}