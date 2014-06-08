package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Caressa on 6/8/2014.
 */
public class MyAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater = null;

    public MyAdapter(Activity activity)
    {
        this.activity = activity;
        inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
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
        return newView;
    }
}
