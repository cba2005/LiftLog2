package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.Collection;

/**
 * Created by Caressa on 6/8/2014.
 */
public class MyAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater = null;

    public MyAdapter(Activity activity)
    {
        this.activity = activity;
        inflater = activity.getLayoutInflater();
    }
    @Override
    public int getCount() {
        //have it return the number of items we want displayed
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View newView = view;

        if(view == null)
            newView = inflater.inflate(R.layout.row_layout, null);

        //set textViews here
        TextView item = (TextView) newView.findViewById(R.id.itemView);
        item.setText("BOOM! HI");

        return newView;
    }

}
