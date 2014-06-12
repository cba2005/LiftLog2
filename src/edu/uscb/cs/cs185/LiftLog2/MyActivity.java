package edu.uscb.cs.cs185.LiftLog2;

import android.annotation.*;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.view.*;
import edu.uscb.cs.cs185.LiftLog2.interfaces.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.*;
import android.widget.*;
import edu.uscb.cs.cs185.LiftLog2.system.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

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
    private TextView dateTV,dayTV;
    private Event compEvent;
	
	private File completionMessagesFile;
	private String[] defaultMessages = {"Congratulation. Lettuce celebrate.", "Nice one, friend.", "u r cool maybe", "wow so hot, so cool, so doge.",
	"maybe ur barn with it. maybe its neighbelline.", "nice gains bro, lookin' big", "hot. like something hot", "wow, so model."};
	private String[] smileMessages = {"smile!", "sorriso!", "Улыбочку!", "senyum!", "sourire!", "sonrisa!", "Řekni!", "lächeln", "웃다", "χαμόγελο!", "微笑!", "微笑む!"};
    private ArrayList<String> completionMessages; //= {"Yay! Good job.", "Hey dere"};
	private int numCompletionMessages;
	
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
        Drawable background = getResources().getDrawable(R.drawable.logo);
        getSupportActionBar().setBackgroundDrawable(background);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupSelfies();
		setupSystem();
        setupList();
		loadCompletionMessages();
		//setupCompletionMessages();

        String delegate ="EEEE, MMMM dd";//, yyyy";
        java.util.Date noteTS = Calendar.getInstance().getTime();
        dateTV.setText(DateFormat.format(delegate, noteTS));
        delegate = "hh:mm:ss";
        dayTV.setText(DateFormat.format(delegate,noteTS));

        Button calendarButton = (Button) findViewById(R.id.calendarButton);


        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        if (currentapiVersion < Build.VERSION_CODES.HONEYCOMB)
            calendarButton.setVisibility(View.INVISIBLE);
        else
        {
            calendarButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openCalendar();
                }
            });        }

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
            case android.R.id.home:
                vicSelfieDialog();
                return true;
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

    public void completeEvent(final Event event)
    {
        if(vicSelfie)
        {
            int ranNum = (int)(Math.random() * ((smileMessages.length)));
            final Toast toast = Toast.makeText(this, smileMessages[ranNum], Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            new CountDownTimer(250, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    toast.cancel();
                    takePhoto(event);
                }
            }.start();
        }
        else
            setEventComplete(event);

    }
    public void takePhoto(Event event)
    {
        if(vicSelfie)
        {
            compEvent = event;
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkbox);
        TextView title = (TextView) dialog.findViewById(R.id.titleName);
        TextView textytext = (TextView) dialog.findViewById(R.id.textDialog);
        Button cancelButton  = (Button) dialog.findViewById(R.id.cancelButton);
        title.setText("Settings");
        textytext.setVisibility(View.INVISIBLE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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


        //dialog.setTitle("Settings");
        // set the custom dialog components - text, image and button
        //TextView text = (TextView) dialog.findViewById(R.id.textDialog);



        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        String path = saveFolder.getAbsolutePath();
        imgNum = pref.getInt("imgNum", imgNum);

        if(imgNum > 0) {
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
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkbox);
        TextView title = (TextView) dialog.findViewById(R.id.titleName);
        TextView textytext = (TextView) dialog.findViewById(R.id.textDialog);
        Button cancelButton  = (Button) dialog.findViewById(R.id.cancelButton);
        String version = "\t\tYouCSMe 1.0\n\n";
        String authors = "\tCARESSA, ARLENE, \n BRONWYN, IRIS-ELENI\n";



        title.setText("Help");
        textytext.setText(version + authors);
        cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
        checkBox.setVisibility(View.INVISIBLE);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        image.setImageResource(R.drawable.doge);
        Button dialogButton = (Button) dialog.findViewById(R.id.button);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void vicSelfieDialog()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selfie_dialog);
        ImageView image = (ImageView) dialog.findViewById(R.id.selfie);
        Button cancelButton  = (Button) dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //dialog.setTitle("Settings");
        String path = saveFolder.getAbsolutePath();
        imgNum = pref.getInt("imgNum", imgNum);

        if(imgNum > 0) {
            String fileImgNum = new DecimalFormat("000").format(imgNum);
            Drawable d = Drawable.createFromPath(path + "/selfie_" + fileImgNum + ".jpg");
            image.setImageDrawable(d);
        }
        else
            image.setImageResource(R.drawable.doge);

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
        setEventComplete(compEvent);
        compEvent = null;
    }
	
	public void setupSystem() {
		debug("setting up system...");
        File saveFolder1;
		boolean sdCardExists = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExists) {
			saveFolder1 = new File(Environment.getExternalStorageDirectory(), "/YouCSMe");
		} else {
			saveFolder1 = getBaseContext().getDir("/YouCSMe", Context.MODE_PRIVATE);
		}
		String path = saveFolder1.getAbsolutePath();
		
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

        String path = saveFolder.getAbsolutePath();


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

        if(imgNum > 0) {
            String fileImgNum = new DecimalFormat("000").format(imgNum);
            Drawable d = Drawable.createFromPath(path + "/selfie_" + fileImgNum + ".jpg");
            getSupportActionBar().setIcon(d);
        }

    }

    @SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void setupList()
    {
        //ListView Stuff
        list = (ListView) findViewById(R.id.listView);
        ColorDrawable cd = new ColorDrawable(0x00007fff);
        list.setOverscrollFooter(cd);
        list.setOverscrollHeader(cd);
        // Getting adapter by passing xml data ArrayList
        adapter = new MyAdapter(this, eventManager.getEvents());//, songsList);
        list.setAdapter(adapter);
    }


    public void addEventKnownDateDialog(Calendar c)
    {
        AddEventDialog newEvent = new AddEventDialog(this);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }


    public void editEventKnownDateDialog(Calendar c, Event e)
    {
        EditEventDialog newEvent = new EditEventDialog(this, e);
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

    public void timePickerDialog(TextView t, IDialog d, int hour, int minute)
    {
        MyTimePicker newEvent = new MyTimePicker(t, d, hour, minute);
        newEvent.show(getSupportFragmentManager(), "newEvent");
    }

    public void datePickerDialog(TextView t, IDialog d, int year, int month, int day)
    {
        MyDatePicker newEvent = new MyDatePicker(t, d, year, month, day);
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

    public void setEventComplete(Event e)
    {
        int ranNum = (int)(Math.random() * ((completionMessages.size())));
        Toast toast = Toast.makeText(MyActivity.this, completionMessages.get(ranNum), Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        debug("\nsetting event completion " + e.getName());
		eventManager.setEventComplete(e.getName(), e.getClassName());
		//adapter.notifyDataSetChanged();
		debug("EVENT "+e.getName()+" set to status: "+e.getStatus());
    }
	
	public void setEventIncomplete(Event e, MyAdapter adapter) {
		debug("\nsetting event incompletion "+e.getName());
		eventManager.setEventIncomplete(e.getName(), e.getClassName());
		//adapter.notifyDataSetChanged();
		debug("EVENT "+e.getName() + " set to status: "+e.getStatus());
	}

    public void deleteEvent(final Event e, final MyAdapter adapter)
    {
        final Dialog dialog2 = new Dialog(this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.completed_dialog);
        CheckBox checkBox = (CheckBox) dialog2.findViewById(R.id.checkbox);
        TextView title = (TextView) dialog2.findViewById(R.id.titleName);
        TextView textytext = (TextView) dialog2.findViewById(R.id.textDialog);
        Button cancelButton  = (Button) dialog2.findViewById(R.id.cancelButton);
        Button done = (Button) dialog2.findViewById(R.id.button);
        Button no = (Button) dialog2.findViewById(R.id.buttonNo);
        title.setText("Trash");
        textytext.setText("\n\n Are you sure you want to delete this item?");
        done.setText("Yes");
        no.setText("Cancel");
        checkBox.setVisibility(View.INVISIBLE);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        ImageView image = (ImageView) dialog2.findViewById(R.id.imageDialog);
        image.setBackgroundDrawable(getResources().getDrawable(R.drawable.trash));
        // if button is clicked, close the custom dialog
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				eventManager.removeEvent(e.getName(), e.getClassName());
				adapter.notifyDataSetChanged();
                dialog2.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });
        dialog2.show();
    }

	public Drawable getEventDrawable(Event e) {
		switch(e.getType()) {
			case EventManager.TYP_HOMEWORK:
				return getResources().getDrawable(R.drawable.homework);
			case EventManager.TYP_PRESENTATION:
				return getResources().getDrawable(R.drawable.presentation);
			case EventManager.TYP_PROJECT:
				return getResources().getDrawable(R.drawable.project);
			case EventManager.TYP_EXAM:
				return getResources().getDrawable(R.drawable.exam);
			default:
				return getResources().getDrawable(R.drawable.homework);
		}
	}
	
	private void loadCompletionMessages() {
		debug("loading up messages...");
		File saveFolder1;
		boolean sdCardExists = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExists) {
			saveFolder1 = new File(Environment.getExternalStorageDirectory(), "/YouCSMe");
		} else {
			saveFolder1 = getBaseContext().getDir("/YouCSMe", Context.MODE_PRIVATE);
		}
		String path = saveFolder1.getAbsolutePath();

		completionMessages = new ArrayList<String>();
		for (String m : defaultMessages) {
			completionMessages.add(m);
		}

		completionMessagesFile = new File(path+"/"+"messages.txt");
		
		if (!completionMessagesFile.exists())
			return;
		
		try {
			FileInputStream fileInputStream = new FileInputStream(completionMessagesFile);
			BufferedReader buffer = new BufferedReader(new InputStreamReader(fileInputStream));
			String line = buffer.readLine();
			numCompletionMessages = Integer.parseInt(line);
			line = buffer.readLine();
			while (line != null) {
				completionMessages.add(line);
				debug("read the line: "+line);
				line = buffer.readLine();
			}
		}
		catch (Exception e) {
			debug("exception loading messages: "+e);
		}		
		debug("message setup complete!");
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}

	public static void debug(String msg) {
		if (DEBUG_MODE)
			Log.i("log", TAG + ": " + msg);
	}
}
