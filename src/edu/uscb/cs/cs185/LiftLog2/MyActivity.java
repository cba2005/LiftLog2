package edu.uscb.cs.cs185.LiftLog2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

public class MyActivity extends ActionBarActivity {
	public static final String TAG = "MAIN_ACTIVITY";
	public static final boolean DEBUG_MODE = true;
	
	// omg caressa use private and public variables
	// yeah learn how to code omg
    private MyCalendarView calendar;
    private File saveFolder;
    private Uri outputFileUri;
    private ListView list;
    private MyAdapter adapter;
    private SharedPreferences pref;
    private int imgNum = 0;
    private boolean vicSelfie = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		//debug("starting main activity...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Drawable background = getResources().getDrawable(R.drawable.ucsbwave_144);
        getSupportActionBar().setBackgroundDrawable(background);
        setupSelfies();

        calendar = (MyCalendarView) findViewById(R.id.calendarView);


        //ListView Stuff
        list=(ListView)findViewById(R.id.listView);

        // Getting adapter by passing xml data ArrayList
        adapter = new MyAdapter(this);//, songsList);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });




        Button photoButton = (Button) findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });


        /*calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                // TODO Auto-generated method stub


            }
        });*/
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
                addEvent();
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

    public void settingsDialog()
    {
        View checkBoxView = View.inflate(this, R.layout.checkbox_layout, null);
        CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);

        checkBox.setChecked(vicSelfie);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                vicSelfie = !vicSelfie;
                pref.edit().putBoolean("vicSelfie", vicSelfie);
                pref.edit().commit();
            }
        });

        checkBox.setText("Victory Selfie");

        new AlertDialog.Builder(this)
                .setTitle("Much Setting. Such wow")
                .setView(checkBoxView)
                //.setMessage("Poop....No Settings")
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				})
                .show();
    }

    public void helpDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("Need Help??")
                .setMessage("TOO BAD!")
                .setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public void addEvent()
    {
        AddEventDialog newEvent = new AddEventDialog();
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imgNum = pref.getInt("imgNum",imgNum) + 1;
        pref.edit().putInt("imgNum", imgNum);
        pref.edit().commit();
        String fileImgNum = new DecimalFormat("000").format(imgNum);
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap photo = (Bitmap) data.getExtras().get("data");
        getSupportActionBar().setIcon(new BitmapDrawable(photo));


        File file = new File(saveFolder, "victoryPhoto-" + fileImgNum + ".jpg");
        outputFileUri = null;
        outputFileUri = Uri.fromFile(file);
        data.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        try {
            FileOutputStream out = new FileOutputStream(file);
            photo.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

        pref = getPreferences(MODE_PRIVATE);
        if (!pref.contains("imgNum"))
        {
            pref.edit().putInt("imgNum", imgNum);
            pref.edit().commit();
        }
        else
            imgNum = pref.getInt("imgNum",imgNum);
        if(!pref.contains("vicSelfie"))
        {
            pref.edit().putBoolean("vicSelfie", vicSelfie);
            pref.edit().commit();
        }
        else
            vicSelfie = pref.getBoolean("vicSelfie", vicSelfie);
    }
	/*
	public static void debug(String msg) {
		if (DEBUG_MODE)
			Log.i("log", TAG+": "+msg);
	}*/
}
