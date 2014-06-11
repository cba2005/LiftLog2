package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
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
    private ScaleAnimation animOpen, animClose;
	private ImageView eventIcon;
    private MyAdapter myAdapter = this;

    private ArrayList<Event> events;
	
	static class MyViewHolder {
		TextView date;
		TextView name;
		TextView className;
        Button button;
		ImageView eventIcon;
	}

    public MyAdapter(Activity activity)
    {
        this.activity = activity;
		myActivity = (MyActivity) activity;
		eventManager = myActivity.getEventManager();
        inflater = activity.getLayoutInflater();
        animOpen = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        animOpen.setDuration(200);
        animClose = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        animClose.setDuration(200);

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
    public View getView(final int position, View view, final ViewGroup viewGroup) {
        final MyViewHolder viewHolder;

        if(view == null) {

			view = inflater.inflate(R.layout.row_layout, viewGroup, false);
			viewHolder = new MyViewHolder();

			//set textViews here
			viewHolder.date = (TextView) view.findViewById(R.id.dateView);
			viewHolder.name = (TextView) view.findViewById(R.id.itemView);
            viewHolder.button = (Button) view.findViewById(R.id.completedButton);
			viewHolder.eventIcon = (ImageView) view.findViewById(R.id.eventIcon);
		

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
			((GradientDrawable) rect.getBackground()).setColor(event.getClass_().getClassColor());
		}

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open dialog
                new AlertDialog.Builder(myActivity)
                        .setTitle("You Finished?!?")
                        .setMessage("Pero like.... did you really?")
                        .setPositiveButton("Yaaaaas", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                closeButton(viewHolder.button);
                                myActivity.completedTask(event, myAdapter);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                closeButton(viewHolder.button);
                                //closeButton(viewGroup);

                            }
                        })
                        .show();

            }
        });


        final View finalView = view;
        final boolean[] longClicked = {false};

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                closeButton(viewHolder.button);
                //closeButton(viewGroup);

                finalView.setPressed(true);
                Event e = eventManager.getEvents().get(position);
                myActivity.debug("HEY U R LONGPRESSING ME LOL: " + e.getName());
                Toast.makeText(myActivity, "You want delete " + e.getName(), Toast.LENGTH_SHORT).show();
                myActivity.deleteEvent(e, myAdapter);
                longClicked[0] = true;
                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //closeButton(viewHolder.button);
                closeButton(viewGroup);

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
                openButton(viewHolder.button);
                closeButton(viewGroup);

            }

            @Override
            public void onSwipeRight() {
                closeButton(viewHolder.button);
                //closeButton(viewGroup);

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

		
		viewHolder.date.setText(event.getFormattedDate());
		viewHolder.name.setText(event.getName());
		viewHolder.eventIcon.setBackgroundDrawable(myActivity.getEventDrawable(event));

        return view;
    }


    private void openButton(final Button completedButton)
    {
        if(completedButton.getVisibility() == View.VISIBLE)
            return;

        animOpen.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation) {
                completedButton.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) { }
        });

        completedButton.startAnimation(animOpen);
    }
    private void closeButton(final Button completedButton)
    {
        if(completedButton.getVisibility() != View.VISIBLE)
            return;

            animClose.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    completedButton.setVisibility(View.INVISIBLE);
                }
            });
            completedButton.startAnimation(animClose);
    }

    private void closeButton(ViewGroup parent)
    {
        for(int i = 0; i < parent.getChildCount(); i++)
        {
            View child = parent.getChildAt(i);
            if(child instanceof ViewGroup)
            {
                closeButton((ViewGroup)child);
            }
            else if(child != null)
            {
                if(child.getClass() == Button.class)
                {
                    closeButton((Button) child);
                }
            }
        }
    }
}
