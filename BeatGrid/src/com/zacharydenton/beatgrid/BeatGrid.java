package com.zacharydenton.beatgrid;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class BeatGrid extends Activity {
	
	private GridView gridView;
	private ArrayList<Beat> beats;
	private boolean recording = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeGrid(savedInstanceState);
    }

    @Override
    protected void onResume() {
    	super.onResume();
    	if (beats.size() != Prefs.getWidth(this) * Prefs.getHeight(this))
    		initializeGrid(null);    
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	for (Beat beat : beats) {
    		beat.deselect();
    	}
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {
        return beats;
    }

    @SuppressWarnings("unchecked")
	private void initializeGrid(Bundle savedInstanceState) {
    	if (savedInstanceState != null) {
        	beats = (ArrayList<Beat>) getLastNonConfigurationInstance();
    	} else {
	    	beats = new ArrayList<Beat>();
	    	for (int i=0; i < (Prefs.getWidth(this) * Prefs.getHeight(this)); i++) {
	    		beats.add(new Beat(this));
	    	}
    	}
    	gridView = new GridView(this);
        setContentView(gridView);
        gridView.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.settings:
    		startActivity(new Intent(this, Prefs.class));
    		return true;
    	}
    	return false;
    }

	public ArrayList<Beat> getBeats() {
		return beats;
	}

	public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}
}