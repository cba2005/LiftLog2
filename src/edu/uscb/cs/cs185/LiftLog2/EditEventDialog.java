package edu.uscb.cs.cs185.LiftLog2;

import android.app.*;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import edu.uscb.cs.cs185.LiftLog2.interfaces.*;
import edu.uscb.cs.cs185.LiftLog2.system.*;

import java.util.Calendar;


/**
 * Created by Caressa on 6/6/2014.
 */
public class EditEventDialog extends DialogFragment implements IDialog{
    private MyActivity activity;
    private Button timeButton, dateButton, cancelButton, editEventButton;
    private TextView timeTextView, dateTextView,eNameTextView;
    private AutoCompleteTextView className;
    private ImageView eventIcon;
	private Spinner dropdown;
	public static final String[] MONTHS = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    private int year, month, day, hour, minute;
    private EditEventDialog my_dialog;
	private Event event;

    public EditEventDialog(MyActivity activity, Event e)
    {
        this.activity = activity;
        this.my_dialog = this;
		this.event = e;
		MyActivity.debug("DIALOG GOT EVENT ---------- "+e.getName());
    }

    private static String[] classesArray;
    TextView eventDate;
    TextView eventTime;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        // classesArray = getResources().getStringArray(R.array.classList);
		
        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_item);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        timeButton = (Button) dialog.findViewById(R.id.timeButton);
        dateButton = (Button) dialog.findViewById(R.id.dateButton);
        cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        editEventButton = (Button) dialog.findViewById(R.id.editEventButton);
        timeTextView = (TextView) dialog.findViewById(R.id.eventTime);
        dateTextView = (TextView) dialog.findViewById(R.id.eventDate);
        eNameTextView = (TextView) dialog.findViewById(R.id.eventEditText);
        className = (AutoCompleteTextView) dialog.findViewById(R.id.acCourseName);
        eventIcon = (ImageView) dialog.findViewById(R.id.eventIcon);
        className.setThreshold(1);
        dropdown = (Spinner) dialog.findViewById(R.id.spinner1);
		Drawable[] pics = new Drawable[]{getResources().getDrawable(R.drawable.homework),getResources().getDrawable(R.drawable.presentation), getResources().getDrawable(R.drawable.project),getResources().getDrawable(R.drawable.exam)};
		SpinnerAdapter myAdapter = new SpinnerAdapter(activity, android.R.layout.simple_spinner_item, EventManager.EVENT_TYPES,pics);
		dropdown.setAdapter(myAdapter);

		// beep boop
		eNameTextView.setText(event.getName());
		className.setText(event.getClassName());
		dropdown.setSelection(event.getType());

        setDateTime();
        setListeners(dialog);
        startAutoComplete();

        return dialog;
    }

    public void startAutoComplete() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.classList));
        className.setAdapter(adapter);
    }

    public void editEvent() {
		event.setName(eNameTextView.getText().toString());
		ClassManager classManager = activity.getEventManager().getClassManager();
		String cName = className.getText().toString();
		event.setClassName(cName);
		if(!classManager.contains(cName)) {
			classManager.addClass(cName, ClassManager.COLORS[classManager.getNumClasses()%ClassManager.NUM_COLORS]);
			event.setClass_(classManager.getClass(cName));
		}
		else {
			event.setClass_(classManager.getClass(cName));
		}
		
		event.setCalendar(EventManager.NEW_CALENDAR(year, month, day, hour, minute));
		event.setDescription(DEF_DESC);
		event.setType(dropdown.getSelectedItemPosition());
		event.setStatus(EventManager.STAT_INCOMPLETE);
		activity.getEventManager().sortEvents();
		activity.getEventManager().saveActiveEvents();
		activity.getEventManager().saveInactiveEvents();
    }

    public void setDateTime()
    {
        String date = Event.FORMAT_DATE(event.getYear(), event.getMonth(), event.getDay());
        dateTextView.setText(date);
		String time = event.getFormattedTime(); //Event.FORMAT_TIME(event.getHour(), event.getMinutes());
        timeTextView.setText(time);
		
		year = event.getYear();
		month = event.getMonth()-1;
		day = event.getDay();
		hour = event.getHour();
		minute = event.getMinutes();
    }

    public void setListeners(final Dialog dialog)
    {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.datePickerDialog(dateTextView, my_dialog, year, month, day);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.timePickerDialog(timeTextView, my_dialog, hour, minute);
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                switch (position){
                    case 0: eventIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.homework));
                        break;
                    case 1: eventIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.presentation));
                        break;
                    case 2: eventIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.project));
                        break;
                    case 3: eventIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.exam));
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

				final Calendar c = Calendar.getInstance();
				int y = c.get(Calendar.YEAR);
				int m = c.get(Calendar.MONTH); // want index
				int d = c.get(Calendar.DAY_OF_MONTH);

				MyActivity.debug("CURR YEAR: "+y);
				MyActivity.debug("CURR MONTH: "+m);
				MyActivity.debug("CURR DAY: "+d);

				MyActivity.debug("CHOSEN YEAR: "+year);
				MyActivity.debug("CHOSEN MONTH: "+month);
				MyActivity.debug("CHOSEN DAY: "+day);
				
				if (year < y || (year == y && month < m) || (year == y && month == m && day < d))
				{
					if (year < y)
						MyActivity.debug("YOUR YEAR");
					else if ((year >= y && month < m))
						MyActivity.debug("YOUR MONTH");
					else
						MyActivity.debug("YOUR DAY");

                    final Dialog dialog2 = new Dialog(dialog.getContext());
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog2.setContentView(R.layout.custom_dialog);
                    CheckBox checkBox = (CheckBox) dialog2.findViewById(R.id.checkbox);
                    TextView title = (TextView) dialog2.findViewById(R.id.titleName);
                    TextView textytext = (TextView) dialog2.findViewById(R.id.textDialog);
                    Button cancelButton  = (Button) dialog2.findViewById(R.id.cancelButton);
                    Button done = (Button) dialog2.findViewById(R.id.button);
                    title.setText("Invalid Date");
                    textytext.setText("\n\n Inputted Date passed! \n Please select new date");
                    done.setText("Ok");
                    checkBox.setVisibility(View.INVISIBLE);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });
                    ImageView image = (ImageView) dialog2.findViewById(R.id.imageDialog);
                    image.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                    // if button is clicked, close the custom dialog
                    done.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog2.dismiss();
                        }
                    });

                    dialog2.show();


				}
				else {
					if (eNameTextView.getText().toString().length() != 0 && className.getText().toString().length() != 0) {
						dialog.dismiss();
						editEvent();
						activity.editEvent();
					}
					else {
                        final Dialog dialog2 = new Dialog(dialog.getContext());
                        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog2.setContentView(R.layout.custom_dialog);
                        CheckBox checkBox = (CheckBox) dialog2.findViewById(R.id.checkbox);
                        TextView title = (TextView) dialog2.findViewById(R.id.titleName);
                        TextView textytext = (TextView) dialog2.findViewById(R.id.textDialog);
                        Button cancelButton  = (Button) dialog2.findViewById(R.id.cancelButton);
                        Button done = (Button) dialog2.findViewById(R.id.button);
                        title.setText("Missing Fields");
                        textytext.setText("\n\n Please input course name and event name!");
                        done.setText("Ok");
                        checkBox.setVisibility(View.INVISIBLE);
                        cancelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });
                        ImageView image = (ImageView) dialog2.findViewById(R.id.imageDialog);
                        image.setBackgroundDrawable(getResources().getDrawable(R.drawable.error));
                        // if button is clicked, close the custom dialog
                        done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog2.dismiss();
                            }
                        });

                        dialog2.show();


                    }
				}	
				}
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void setDate(int y, int m, int d) {
        year = y;
        month = m;
        day = d;
    }

    @Override
    public void setTime(int h, int min) {
        hour = h;
        minute = min;
    }
}

