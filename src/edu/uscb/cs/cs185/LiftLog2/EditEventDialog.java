package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    private TextView timeTextView, dateTextView, eNameTextView;
    private AutoCompleteTextView className;
    public static final String[] MONTHS ={"January","February","March","April","May","June","July","August","September","October","November","December"};
	
	private EditEventDialog my_dialog;
	private int year, month, day, hour, minute;

    public EditEventDialog(MyActivity activity)
    {
        this.activity = activity;
		my_dialog = this;
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

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
		int type = DEF_TYPE;
		Calendar cal = EventManager.NEW_CALENDAR(year, month, day, hour, minute);
		activity.getEventManager().addActiveEvent(new Event(type, cName, name, desc, cal));
	}

    public void setDateTime()
    {
        //pull from text file

        /*String date = "";
        date = MONTHS[month];
        date+= " " + String.valueOf(day);
        date += " " + year;
        dateTextView.setText(date);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        String time = hour + ":"+"00";
        timeTextView.setText(time);*/

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