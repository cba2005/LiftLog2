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
public class EditEventDialog extends DialogFragment implements IDialog{
    private MyActivity activity;
    private Button timeButton, dateButton, cancelButton, editEventButton;
    private TextView timeTextView, dateTextView,eNameTextView;
    private AutoCompleteTextView className;
    public static final String[] MONTHS ={"January","February","March","April","May","June","July","August","September","October","November","December"};

    private int year, month, day, hour, minute;
    private EditEventDialog my_dialog;
	private Event event;

    public EditEventDialog(MyActivity activity, Event e)
    {
        this.activity = activity;
        this.my_dialog = this;
		this.event = e;
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
        className.setThreshold(1);
	

        Spinner dropdown = (Spinner) dialog.findViewById(R.id.spinner1);
        String[] items = new String[]{"Event Type","Homework","Presentation","Project","Exam"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialog.getContext(), android.R.layout.simple_spinner_item, items);

        dropdown.setAdapter(adapter);

		// beep boop
		eNameTextView.setText(event.getName());
		className.setText(event.getClassName());

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
		event.setType(DEF_TYPE);
		event.setStatus(EventManager.STAT_INCOMPLETE);
    }

    public void setDateTime()
    {
        final Calendar c = Calendar.getInstance();
        year = event.getYear();
        month = event.getMonth();
        day = event.getDay();
        String date = "";
        date = MONTHS[month];
        date+= " " + String.valueOf(day);
        date += " " + year;
        dateTextView.setText(date);

        hour = event.getHour();
		minute = event.getMinutes();
        String time = hour + ":"+minute;
        timeTextView.setText(time);
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

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                editEvent();
                activity.editEvent();
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