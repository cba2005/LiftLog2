package edu.uscb.cs.cs185.LiftLog2.system;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.util.Log;

public class SelfieManager {

	public static final String XML_FILE_NAME = "PicListGPS.xml";
	public static final String COUNT_FILE_NAME = "photo_index.txt";
	private ArrayList<Selfie> selfies;
	private int numPhotos;
	private String filePath;
	private File xmlFile;
	private File indexFile;
	private Selfie lastPhoto;

	public SelfieManager(String path) {
		selfies = new ArrayList<Selfie>();
		numPhotos = 0;
		filePath = path;
		xmlFile = new File(filePath+"/"+XML_FILE_NAME);
		indexFile = new File(filePath+"/"+COUNT_FILE_NAME);
		lastPhoto = null;
		try {
			loadPhotos();
		}
		catch (Exception e) {
			Log.d("loadPhotos", "Exception caught" +e);
		}
	}

	public void addSelfie(String index, Assignment ass) {
		Selfie photo = new Selfie(index, ass);
		selfies.add(photo);
		lastPhoto = photo;
		numPhotos++;
	}

	public void savePhotos() {
		try {

			FileOutputStream fileOutputStream = new FileOutputStream(indexFile);
			DataOutputStream buffer = new DataOutputStream(fileOutputStream);

			for (Selfie photo : selfies) {
				String nameLine = "photo_"+photo.getIndexString()+".jpg\n";

				buffer.writeChars(nameLine);
			}

			buffer.close();
		}
		catch (Exception e) {

		}
	}

	public void loadPhotos() throws Exception {
		Log.d("APP_PHOTO_MANAGER", "trying to load old photos");
		if (!indexFile.exists())
			return;
		Log.d("APP_PHOTO_MANAGER", "loading old photos...");
		FileInputStream fileInputStream = new FileInputStream(indexFile);
		DataInputStream buffer = new DataInputStream(fileInputStream);
		int counter = 0;
		String name="", lat="", lon="";
		while (buffer.available() > 0)
		{
			char in;
			while((in = buffer.readChar()) != '\n')
			{
				switch(counter) {
					case 0:
						name += in;
						break;
					case 1:
						lat += in;
						break;
					case 2:
						lon += in;
						break;
				}
			}
			switch(counter) {
				case 0:
					counter++;
					break;
				case 1:
					counter++;
					break;
				case 2:
					counter = 0;
					addSelfie(getNextIndexString(), null);
					name = "";
					lat = "";
					lon = "";
					break;
			}
		}
	}

	public int getNextIndex() {
		return numPhotos;
	}

	public String getNextIndexString() {
		return Selfie.getPhotoIndex(numPhotos);
	}

	public boolean isEmpty() {
		return selfies.isEmpty();
	}

	public Selfie getLastPhoto() {
		return lastPhoto;
	}

	public File getLastPhotoFile() {
		return new File(filePath+"photo_"+lastPhoto.getIndexString()+".jpg");
	}
}
