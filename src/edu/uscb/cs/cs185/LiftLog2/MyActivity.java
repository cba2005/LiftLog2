package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.content.Context;
import edu.uscb.cs.cs185.LiftLog2.interfaces.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import edu.uscb.cs.cs185.LiftLog2.system.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

public class MyActivity extends ActionBarActivity {
	public static final String TAG = "MAIN_ACTIVITY";
	public static final boolean DEBUG_MODE = true;


   // private MyCalendarView calendar;
    private File saveFolder;
    private Uri outputFileUri;
    private ListView list;
    private MyAdapter adapter;
    private SharedPreferences pref;
    private int imgNum = 0;
    private boolean vicSelfie = true;
    private SharedPreferences.Editor editor;
    private String path;
    private TextView dateTV,dayTV;
	
	private EventManager eventManager;
	
    FragmentManager manager;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		//debug("starting main activity...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dateTV = (TextView) findViewById(R.id.date);
        dayTV = (TextView) findViewById(R.id.day);
        Drawable background = getResources().getDrawable(R.drawable.ucsbwave_144);
        getSupportActionBar().setBackgroundDrawable(background);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupSelfies();
		setupSystem();
        setupList();

        String delegate ="EEEE, MMMM dd, yyyy";
        java.util.Date noteTS = Calendar.getInstance().getTime();
        dateTV.setText(DateFormat.format(delegate, noteTS));
        delegate = "hh:mm:ss";
        dayTV.setText(DateFormat.format(delegate,noteTS));
        //ListView Stuff
        list = (ListView) findViewById(R.id.listView);






        Button calendarButton = (Button) findViewById(R.id.calendarButton);
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.addEvent:
                addEventDialog();
                return true;
            case R.id.settings:
                settingsDialog();
                return true;
            case R.id.help:
                helpDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void takePhoto()
    {
        if(vicSelfie)
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 100);
        }
    }

    public void openCalendar()
    {
        CalendarDialog newEvent = new CalendarDialog(this);
        newEvent.show(getSupportFragmentManager(), "newEvent");

    }

    public void settingsDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkbox);

        checkBox.setChecked(vicSelfie);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vicSelfie = !vicSelfie;
                editor.putBoolean("vicSelfie", vicSelfie);
                editor.commit();
            }
        });


       checkBox.setText("Victory Selfie");


        dialog.setTitle("Settings");
        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.textDialog);


        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        if(imgNum > 0)
        {
            String fileImgNum = new DecimalFormat("000").format(imgNum);
            Drawable d = Drawable.createFromPath(path + "/selfie_" + fileImgNum + ".jpg");
            image.setImageDrawable(d);
        }
        else
            image.setImageResource(R.drawable.doge);

        Button dialogButton = (Button) dialog.findViewById(R.id.button);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void helpDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        // final RelativeLayout dialogLayout = (RelativeLayout) findViewById(R.id.relLayout);


        dialog.setTitle("Help");
        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.textDialog);


        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(R.drawable.doge);

        Button dialogButton = (Button) dialog.findViewById(R.id.button);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            imgNum = pref.getInt("imgNum", imgNum) + 1;
            editor.putInt("imgNum", imgNum);
            editor.commit();
            String fileImgNum = new DecimalFormat("000").format(imgNum);
            super.onActivityResult(requestCode, resultCode, data);

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            getSupportActionBar().setIcon(new BitmapDrawable(photo));


            File file = new File(saveFolder, "selfie_" + fileImgNum + ".jpg");
            outputFileUri = null;
            outputFileUri = Uri.fromFile(file);
            data.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            try {
                FileOutputStream out = new FileOutputStream(file);
                photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	
	public void setupSystem() {
		debug("setting up system...");
		boolean sdCardExists = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExists) {
			saveFolder = new File(Environment.getExternalStorageDirectory(), "/YouCSMe");
		} else {
			saveFolder = getBaseContext().getDir("/YouCSMe", Context.MODE_PRIVATE);
		}
		String path = saveFolder.getAbsolutePath();
		
		eventManager = new EventManager(path);
		debug("system setup complete!");
	}

    public void setupSelfies()
    {
        boolean sdCardExists = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

        if (sdCardExists) {
            saveFolder = new File(Environment.getExternalStorageDirectory(), "/YouCSMe/VictorySelfies");
        } else {
            saveFolder = getBaseContext().getDir("/YouCSMe/VictorySelfies", Context.MODE_PRIVATE);
        }

        if (!saveFolder.exists())
            saveFolder.mkdirs();

        path = saveFolder.getAbsolutePath();


        pref =  this.getPreferences(Context.MODE_PRIVATE);
        editor = pref.edit();
        if (!pref.contains("imgNum"))
        {
            editor.putInt("imgNum", imgNum);
            editor.commit();
        }
        else
            imgNum = pref.getInt("imgNum", imgNum);
        if(!pref.contains("vicSelfie"))
        {
            editor.putBoolean("vicSelfie", vicSelfie);
            editor.commit();
        }
        else
            vicSelfie = pref.getBoolean("vicSelfie", vicSelfie);

        if(imgNum > 0)
        {
            String fileImgNum = new DecimalFormat("000").format(imgNum);
            Drawable d = Drawable.createFromPath(path + "/selfie_" + fileImgNum + ".jpg");
            getSupportActionBar().setIcon(d);
        }

    }

    public void setupList()
    {
        //ListView Stuff
        list = (ListView) findViewById(R.id.listView);

        // Getting adapter by passing xml data ArrayList
        adapter = new MyAdapter(this);//, songsList);
        list.setAdapter(adapter);
		
		list.setOnItemClickListener( new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				debug("YOU CLICKED: " + eventManager.getEvents().get(position).getName());
				Event e = eventManager.getEvents().get(position);
				editEventDialog(e);
			}
		});

        list.setOnTouchListener(new MyTouchView(MyActivity.this) {
            @Override
            public void onSwipeLeft() {
                Toast.makeText(MyActivity.this, "left", Toast.LENGTH_SHORT).show();
            }

/*            @Override
            public void singleTap()
            {
                editEventDialog();
            }*/
        });

    }


    public void addEventKnownDateDialog(Calendar c)
    {
        AddEventKnownDateDialog newEvent = new AddEventKnownDateDialog(this, c);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }


    public void editEventKnownDateDialog(Calendar c)
    {
        EditEventKnownDateDialog newEvent = new EditEventKnownDateDialog(this, c);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }

    public void addEventDialog()
    {
        AddEventDialog newEvent = new AddEventDialog(this);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }

    public void editEventDialog(Event e)
    {
        EditEventDialog newEvent = new EditEventDialog(this, e);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }

    public void openDayViewDialog(int month, int day, int year){
        DayViewDialog newEvent = new DayViewDialog(month, day, year, this);
        newEvent.show(getSupportFragmentManager(), "newEvent");

    }

    public void timePickerDialog(TextView t, IDialog d)
    {
        MyTimePicker newEvent = new MyTimePicker(t, d);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }

    public void datePickerDialog(TextView t, IDialog d)
    {
        MyDatePicker newEvent = new MyDatePicker(t, d);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }


    public void addEvent()
    {
        //call whatever to change text file
		adapter.notifyDataSetChanged();
		
    }

    public void editEvent()
    {
        //call whatever to change text file
		adapter.notifyDataSetChanged();
    }

    public void completedTask()
    {

    }



	
	public EventManager getEventManager() {
		return eventManager;
	}

	public static void debug(String msg) {
		if (DEBUG_MODE)
			Log.i("log", TAG + ": " + msg);
	}
}
