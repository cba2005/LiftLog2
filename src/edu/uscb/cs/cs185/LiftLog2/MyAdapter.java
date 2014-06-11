package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import edu.uscb.cs.cs185.LiftLog2.system.*;

import java.util.*;

/**
 * Created by Caressa on 6/8/2014.
 */
public class MyAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater = null;
	private MyActivity myActivity;
	private EventManager eventManager;

	private ArrayList<Event> events;
	
	static class MyViewHolder {
		TextView date;
		TextView name;

		TextView className;
	}

    public MyAdapter(Activity activity)
    {
        this.activity = activity;
		myActivity = (MyActivity) activity;
		eventManager = myActivity.getEventManager();
        inflater = activity.getLayoutInflater();

    }
    @Override
    public int getCount() {
        // have it return the number of items we want displayed
        return eventManager.getNumTotalEvents();
    }

    @Override
    public Object getItem(int position) {      
		return eventManager.getEvents().get(position);
    }

    @Override
    public long getItemId(int position) { 
		return position;
    }
	
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
		MyViewHolder viewHolder;
	
        if(view == null) {

			    view = inflater.inflate(R.layout.row_layout, viewGroup, false);

			viewHolder = new MyViewHolder();

			//set textViews here
			viewHolder.date = (TextView) view.findViewById(R.id.dateView);
			viewHolder.name = (TextView) view.findViewById(R.id.itemView);

			view.setTag(viewHolder);
		}
		else {
			viewHolder = (MyViewHolder) view.getTag();
		}

		View rect = view.findViewById(R.id.myRectangleView);
		final Event event = (Event) getItem(position);
		if (event.getClass_() == null)
			MyActivity.debug("EVENT CLASS IS NULL");
		else {
			MyActivity.debug("attempting to color squares");
			((GradientDrawable) rect.getBackground()).setColor(event.getClass_().getClassColor());
		}

        final View finalView = view;
        final boolean[] longClicked = {false};

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                finalView.setPressed(true);
                Event e = eventManager.getEvents().get(position);
                myActivity.debug("HEY U R LONGPRESSING ME LOL: " + e.getName());
                Toast.makeText(myActivity, "You want delete " + e.getName(), Toast.LENGTH_SHORT).show();
                myActivity.deleteEvent(e);
                longClicked[0] = true;
                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!longClicked[0]) {
                    finalView.setPressed(true);
                    myActivity.debug("YOU CLICKED: " + eventManager.getEvents().get(position).getName() + " AT POSITION " + position);
                    Event e = eventManager.getEvents().get(position);
                    myActivity.editEventDialog(e);
                }
                longClicked[0] = false;


            }
        });

        view.setOnTouchListener(new MyTouchView(myActivity) {
            @Override
            public void onSwipeLeft() {
                //View view = getCurrentFocus();
                //int position = (Integer) view.getTag();
                Event e = eventManager.getEvents().get(position);
                Toast.makeText(myActivity, "left on " + e.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void singleTap()
            {
                    finalView.performClick();
            }

            @Override
            public void longPress()
            {
                finalView.performLongClick();

            }


        });


		viewHolder.date.setText(event.getDateDue());
		viewHolder.name.setText(event.getName());





        return view;
    }


}
