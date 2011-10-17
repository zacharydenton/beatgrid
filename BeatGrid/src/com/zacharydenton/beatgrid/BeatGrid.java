package com.zacharydenton.beatgrid;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class BeatGrid extends Activity {

	private GridView gridView;
	private Grid grid;
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
		if (getGrid().getBeats().size() != Prefs.getWidth(this)
				* Prefs.getHeight(this))
			initializeGrid(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		for (Beat beat : getGrid().getBeats()) {
			beat.deselect();
		}
	}

	private void initializeGrid(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			setGrid(new Grid(this));
		} else {
			setGrid(new Grid(this));
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
		case R.id.savegrid:
			saveGrid();
			return true;
		case R.id.loadgrid:
			loadGrid();
			return true;
		}
		return false;
	}

	public boolean isRecording() {
		return recording;
	}

	public void setRecording(boolean recording) {
		this.recording = recording;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public void saveGrid() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Save grid");
		alert.setMessage("Please enter a name for the grid");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String name = input.getText().toString();
				grid.save(BeatGrid.this, name);
		        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});

		alert.show();
	}
	

	public void loadGrid() {
		
		final String[] items = listGrids();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select a grid");
		builder.setItems(items, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
				try {
					setGrid(Grid.load(BeatGrid.this, items[item]));
					gridView.setGrid(getGrid());
			        Toast.makeText(getApplicationContext(), "Loaded", Toast.LENGTH_SHORT).show();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("beatgrid", "file not found");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("beatgrid", "I/O error");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("beatgrid", "class not found");
				}
		    }
		});

		builder.show();
	}
	
	public String[] listGrids() {
		return fileList();
	}
}