package edu.uscb.cs.cs185.LiftLog2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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


/**
 * Created by Caressa on 6/6/2014.
 */
public class AddEventDialog extends DialogFragment implements IDialog{
    private MyActivity activity;
    private Button timeButton, dateButton, cancelButton, addEventButton;
    private TextView timeTextView, dateTextView,eNameTextView;
    private AutoCompleteTextView className;
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
        className = (AutoCompleteTextView) dialog.findViewById(R.id.acCourseName);
        className.setThreshold(1);
		dropdown = (Spinner) dialog.findViewById(R.id.spinner1);
		Drawable[] pics = new Drawable[]{getResources().getDrawable(R.drawable.homework),getResources().getDrawable(R.drawable.presentation), getResources().getDrawable(R.drawable.project),getResources().getDrawable(R.drawable.exam)};
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialog.getContext(), R.layout.support_simple_spinner_dropdown_item, items);
		SpinnerAdapter myAdapter = new SpinnerAdapter(activity, android.R.layout.simple_spinner_item, items,pics);

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
		activity.getEventManager().addActiveEvent(new Event(type, cName, name, desc, cal));
	}

    public void setDateTime()
    {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String date = "";
        date = MONTHS[month-1];
        date+= " " + String.valueOf(day);
        date += " " + year;
        dateTextView.setText(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        String time = hour + ":"+"00";
        timeTextView.setText(time);
		
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = 0;
    }

    public void setListeners(final Dialog dialog)
    {
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.datePickerDialog(dateTextView, my_dialog);
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.timePickerDialog(timeTextView, my_dialog);
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
				addEvent();
                activity.addEvent();
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
		MyActivity.debug("SET YEAR: "+year);
		MyActivity.debug("SET MONTH: "+month);
		MyActivity.debug("SET DAY: "+day);
	}

	@Override
	public void setTime(int h, int min) {
		hour = h;
		minute = min;
		MyActivity.debug("SET HOUR: "+hour);
		MyActivity.debug("SET MINUTE: "+minute);
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