package com.zacharydenton.beatgrid;

import android.app.Activity;
import android.os.Bundle;

public class BeatGrid extends Activity {
	
	private GridView gridView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        gridView = new GridView(this);
        setContentView(gridView);
        gridView.requestFocus();

    }
}