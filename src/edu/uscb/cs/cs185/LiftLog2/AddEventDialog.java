package edu.uscb.cs.cs185.LiftLog2;

import android.app.*;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
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

import static android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by Caressa on 6/6/2014.
 */
public class AddEventDialog extends DialogFragment implements IDialog{
    public static MyActivity activity;
    private Button timeButton, dateButton, cancelButton, addEventButton;
    private TextView timeTextView, dateTextView,eNameTextView;
    private AutoCompleteTextView className;
    private ImageView eventIcon;
	private Spinner dropdown;

	private String[] items = new String[]{"Event Type","Homework","Presentation","Project","Exam"};
    public static final String[] MONTHS ={"January","February","March","April","May","June","July","August","September","October","November","December"};

	private int year, month, day, hour, minute;
	private AddEventDialog my_dialog;

        public AddEventDialog(MyActivity activity)
        {
            this.activity = activity;
			this.my_dialog = this;
        }

    private static String[] classesArray;
    TextView eventDate;
    TextView eventTime;

    @Override
        public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
		
        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_item);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        timeButton = (Button) dialog.findViewById(R.id.timeButton);
        dateButton = (Button) dialog.findViewById(R.id.dateButton);
        cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
        addEventButton = (Button) dialog.findViewById(R.id.addEventButton);
        timeTextView = (TextView) dialog.findViewById(R.id.eventTime);
        dateTextView = (TextView) dialog.findViewById(R.id.eventDate);
		eNameTextView = (TextView) dialog.findViewById(R.id.eventEditText);
        eventIcon = (ImageView) dialog.findViewById(R.id.eventIcon);
        className = (AutoCompleteTextView) dialog.findViewById(R.id.acCourseName);
        className.setThreshold(1);
		dropdown = (Spinner) dialog.findViewById(R.id.spinner1);
       //eventIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.homework));
		Drawable[] pics = new Drawable[]{getResources().getDrawable(R.drawable.homework),getResources().getDrawable(R.drawable.presentation), getResources().getDrawable(R.drawable.project),getResources().getDrawable(R.drawable.exam)};
		SpinnerAdapter myAdapter = new SpinnerAdapter(activity, android.R.layout.simple_spinner_item, EventManager.EVENT_TYPES, pics);
		dropdown.setAdapter(myAdapter);
		
		MyActivity.debug("ADAPTER SET");

        setDateTime();
        setListeners(dialog);
        startAutoComplete();

        return dialog;
    }

    public void startAutoComplete() {
    	//make autocomplere adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.classList));
        className.setAdapter(adapter);
    }
	
	public void addEvent() {
		String cName = className.getText().toString();
		String name = eNameTextView.getText().toString();
		String desc = DEF_DESC;
		int type = dropdown.getSelectedItemPosition();
		Calendar cal = EventManager.NEW_CALENDAR(year, month, day, hour, minute);
		Event e = new Event(type, cName, name, desc, cal);
		activity.getEventManager().addActiveEvent(e);
	}

    public void setDateTime()
    {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH); // want index
        day = c.get(Calendar.DAY_OF_MONTH);
		// hack fix until I can do real fix
		String date = Event.FORMAT_DATE(year, month+1, day);
		dateTextView.setText(date);

        hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		String time = Event.FORMAT_TIME(hour, minute);
		timeTextView.setText(time);
    }

    public void setListeners(final Dialog dialog)
    {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				MyActivity.debug("PASSING TO DATE PICKER "+year+", "+(month)+", "+day);
                activity.datePickerDialog(dateTextView, my_dialog, year, month, day);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.timePickerDialog(timeTextView, my_dialog, hour, minute);
            }
        });

        dropdown.setOnItemSelectedListener(new OnItemSelectedListener() {
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

        addEventButton.setOnClickListener(new View.OnClickListener() {
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
				
				//sweg sweg
				
				if (year < y || (year == y && month < m) || (year == y && month == m && day < d))
				{
					new AlertDialog.Builder(dialog.getContext())
							.setTitle("Invalid Date")
							.setMessage("That date already pass tho!!!")
							.setNeutralButton("oki doki", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// bye bye
								}
							})
							.show();	
				}
				else {
					if (eNameTextView.getText().toString().length() != 0 && className.getText().toString().length() != 0) {
						dialog.dismiss();
						addEvent();
						activity.addEvent();
					}
					else {
						new AlertDialog.Builder(dialog.getContext())
								.setTitle("Missing Fields")
								.setMessage("por favor setting event and course name\nthank")
								.setNeutralButton("oki doki", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// bye bye
									}
								})
								.show();
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
		MyActivity.debug("SET LISTENERS");
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

class SpinnerAdapter extends ArrayAdapter<String>
{
    private Activity context;
    String[] data = null;
    Drawable[] pics = null;
    public SpinnerAdapter(Activity context, int resource, String[] data, Drawable[] pics)
    {
        super(context, resource, data);
        this.context = context;
        this.data = data;
        this.pics = pics;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item
        return super.getView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {   // This view starts when we click the spinner.
        View row = convertView;
        if(row == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_options, parent, false);
        }

       String item = data[position];
        Drawable pic = pics[position];
        if(item != null)
        {   // Parse the data from each object and set it.
            ImageView myIcon = (ImageView) row.findViewById(R.id.imageIcon);
            TextView myEvent = (TextView) row.findViewById(R.id.hwText);
            if(myIcon != null)
            {
                myIcon.setBackgroundDrawable(pic);
               // myFlag.setBackgroundDrawable(getResources().getDrawable(item.getCountryFlag()));
            }
            if(myEvent != null)
                myEvent.setText(item);

        }

        return row;
    }

}