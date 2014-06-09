package edu.uscb.cs.cs185.LiftLog2;

import android.app.Dialog;
import android.content.Context;
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
import android.util.Log;
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

       // calendar = (MyCalendarView) findViewById(R.id.calendarView);


        //ListView Stuff
        list = (ListView) findViewById(R.id.listView);

        // Getting adapter by passing xml data ArrayList
        adapter = new MyAdapter(this);//, songsList);
        list.setAdapter(adapter);

        // Click event for single list row
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                editEvent();
            }
        });




       /* Button photoButton = (Button) findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });*/


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

    public void openCalendar()
    {

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

    public void addEvent()
    {
        AddEventDialog newEvent = new AddEventDialog();
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }

    public void editEvent()
    {
        EditEventDialog newEvent = new EditEventDialog();
        newEvent.show(getSupportFragmentManager(), "newEvent");
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

	/*public static void debug(String msg) {
		if (DEBUG_MODE)
			Log.i("log", TAG + ": " + msg);
	}*/
}
