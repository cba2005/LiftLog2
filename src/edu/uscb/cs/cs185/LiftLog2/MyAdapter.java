package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    private ScaleAnimation animOpen, animClose, animStrikeComplete, animStrikeIncomplete, animStrike;
	private ImageView eventIcon;
    private LinearLayout linearLayout;
    private MyAdapter myAdapter = this;
    private TextView className;
    private ArrayList<Event> events;
	
	static class MyViewHolder {
		TextView date;
		TextView name;
		TextView className;
        Button button;
        View crossOut;
		ImageView eventIcon;
        LinearLayout linearLayout;

	}

    public MyAdapter(Activity activity, ArrayList<Event> events)
    {
        this.activity = activity;
		myActivity = (MyActivity) activity;
		eventManager = myActivity.getEventManager();
        inflater = activity.getLayoutInflater();
        animOpen = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        animOpen.setDuration(200);
        animClose = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        animClose.setDuration(200);
        animStrikeComplete = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        animStrikeComplete.setDuration(200);
		animStrikeIncomplete = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
		animStrikeIncomplete.setDuration(200);
		animStrike = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
		animStrike.setDuration(200);
		this.events = events;

    }
    @Override
    public int getCount() {
        // have it return the number of items we want displayed
        return events.size();
    }

    @Override
    public Object getItem(int position) {
		return events.get(position);//eventManager.getEvents().get(position);
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
            viewHolder.crossOut = view.findViewById(R.id.crossOut);
            viewHolder.linearLayout = (LinearLayout) view.findViewById(R.id.rowView);
            viewHolder.linearLayout.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.list_selector));
            viewHolder.className = (TextView) view.findViewById(R.id.cName);

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
                final Dialog dialog2 = new Dialog(view.getContext());
                dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog2.setContentView(R.layout.completed_dialog);
                CheckBox checkBox = (CheckBox) dialog2.findViewById(R.id.checkbox);
                TextView title = (TextView) dialog2.findViewById(R.id.titleName);
                TextView textytext = (TextView) dialog2.findViewById(R.id.textDialog);
                Button cancelButton  = (Button) dialog2.findViewById(R.id.cancelButton);
                Button done = (Button) dialog2.findViewById(R.id.button);
                Button no = (Button) dialog2.findViewById(R.id.buttonNo);
                title.setText("Completed!");
                textytext.setText("\n\n Did you really finish ?! ");
                done.setText(" YES! ");
                no.setText(" No ");
                checkBox.setVisibility(View.INVISIBLE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                ImageView image = (ImageView) dialog2.findViewById(R.id.imageDialog);
                image.setBackgroundDrawable(view.getResources().getDrawable(R.drawable.error));
                // if button is clicked, close the custom dialog
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
						closeButton(viewHolder.button);
						if (event.getStatus() == EventManager.STAT_INCOMPLETE) {
							strikeItem(viewHolder.crossOut);
							myActivity.setEventComplete(event, myAdapter);
						}
                        dialog2.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
						closeButton(viewHolder.button);
						if (event.getStatus() == EventManager.STAT_COMPLETE) {
							undoStrikeItem(viewHolder.crossOut);
							myActivity.setEventIncomplete(event, myAdapter);
						}
                        dialog2.dismiss();
                    }
                });
                dialog2.show();
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
        viewHolder.className.setText(event.getClassName());
		viewHolder.eventIcon.setBackgroundDrawable(myActivity.getEventDrawable(event));
        viewHolder.linearLayout.setBackgroundDrawable(myActivity.getResources().getDrawable(R.drawable.list_selector));
		if (event.getStatus() == EventManager.STAT_COMPLETE) {
			MyActivity.debug(event.getName() + " is complete");
			viewHolder.crossOut.setVisibility(View.VISIBLE);
		}
		else {
			MyActivity.debug(event.getName() + "is not complete. status: "+event.getStatus());
		}

        int dueDay = event.getDay();
        int dueMonth = event.getMonth();
        int dueYear = event.getYear();

        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH); // Note: zero based!
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR);

        if (((dueDay == day) && (dueMonth == (month+1))) && (dueYear == year)) {

                viewHolder.linearLayout.setBackgroundDrawable(myActivity.getResources().getDrawable(R.drawable.due_today));

        }
        else if ((dueMonth < (month+1)) && (dueYear == year))
            viewHolder.linearLayout.setBackgroundDrawable(myActivity.getResources().getDrawable(R.drawable.late));
        else if (dueYear < year)
            viewHolder.linearLayout.setBackgroundDrawable(myActivity.getResources().getDrawable(R.drawable.late));
        else
            viewHolder.linearLayout.setBackgroundDrawable(myActivity.getResources().getDrawable(R.drawable.list_selector));
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


    private void strikeItem(final View line)
    {

        animStrikeComplete.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				line.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});

        line.startAnimation(animStrikeComplete);
    }
	
	private void undoStrikeItem(final View line) {
		animStrikeIncomplete.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				line.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});

		line.startAnimation(animStrikeIncomplete);	
	}

	private void noAnimStrikeItem(final View line) {
		animStrike.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				line.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
			}
		});

		line.startAnimation(animStrike);
	}
}
