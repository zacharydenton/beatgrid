package com.zacharydenton.beatgrid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class Grid implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1171137890223175599L;
	private ArrayList<Beat> beats;
	private int width, height;
	private String name;

	public Grid(Context context) {
		BeatGrid beatGrid = (BeatGrid) context;
		beats = new ArrayList<Beat>();
		width = Prefs.getWidth(beatGrid);
		height = Prefs.getHeight(beatGrid);
		for (int i = 0; i < (width * height); i++) {
			beats.add(new Beat(beatGrid));
		}
	}

	public ArrayList<Beat> getBeats() {
		return beats;
	}

	public Beat get(int x, int y) {
		return (Beat) getBeats().get(y * width + x);
	}

	public void resize(int w, int h, float cellWidth, float cellHeight) {
		width = w;
		height = h;
		int i = 0;
		for (Beat beat : getBeats()) {
			beat.setRect(i % width, i / width, cellWidth, cellHeight);
			i++;
		}
	}

	public String save(Context context, String gridname) {
		name = gridname;
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = context.openFileOutput(name, Context.MODE_PRIVATE);
			out = new ObjectOutputStream(fos);
			ArrayList<String> beatfiles = new ArrayList<String>();
			for (Beat beat : getBeats()) {
				beatfiles.add(beat.getPath());
			}
			out.writeObject(beatfiles);
			out.close();
			return name;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}

	public static Grid load(Context context, String gridname) throws IOException,
			ClassNotFoundException {
		FileInputStream FIS = context.openFileInput(gridname);
		ObjectInputStream OIS = new ObjectInputStream(FIS);
		Grid temp = new Grid(context);
		ArrayList<String> beatfiles = (ArrayList<String>) OIS.readObject();
		OIS.close();
		FIS.close();
		
		for (int i = 0; i < Math.min(beatfiles.size(), temp.getBeats().size()); i++) {
			temp.getBeats().get(i).loadSound(beatfiles.get(i));
		}
		
		return temp;
	}
}
