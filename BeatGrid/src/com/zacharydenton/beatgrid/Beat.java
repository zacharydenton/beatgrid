package com.zacharydenton.beatgrid;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;

public class Beat extends View {

	private final Rect rect = new Rect();
	private final Paint color = new Paint();
	private final BeatGrid beatGrid;
	private final AudioRecorder recorder;
	private final MediaPlayer player = new MediaPlayer();
	private String path;
	private boolean active;
	private boolean recording;

	public Beat(Context context) {
		super(context);
		
		beatGrid = (BeatGrid) context;		
		active = false;
		setColor(getResources().getColor(R.color.beat_inactive));
		
		recorder = new AudioRecorder(generatePath());
		recording = false;
		
	}
	
	public boolean deselect() {
		if (active) {
			active = false;
			if (recording) {
				stopRecording();
			} else {
				stop();
			}
			if (path != null) {
				setColor(getResources().getColor(R.color.beat_initialized));
			} else {
				setColor(getResources().getColor(R.color.beat_inactive));
			}
			return true;
		} else {
			return false;
		}
	}

	private String generatePath() {
		File soundDir = new File("/sdcard/BeatGrid/beats");
		soundDir.mkdirs();
		File soundFile;
		try {
			soundFile = File.createTempFile("beat", ".3gp", soundDir);
			return soundFile.getAbsolutePath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}
	
	public Paint getColor() {
		return color;
	}
		
	public Rect getRect() {
		return rect;
	}

	private void play() {
		try {
			player.prepare();
			player.start();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean select() {
		if (!active) {
			active = true;
			if ((path == null) && (!recording)) {
				startRecording();
			} else {
				play();
				setColor(getResources().getColor(R.color.beat_active));
			}
			return true;
		} else {
			return false;
		}
		
	}
	
	public void setColor(int c) {
		color.setColor(c);
	}

	public void setRect(int x, int y, float width, float height) {
		rect.set((int) (x * width), (int) (y * height),
				(int) (x * width + width), (int) (y * height + height));
	}
	
	private void startRecording() {
		try {
			recording = true;
			recorder.start();
			setColor(getResources().getColor(R.color.beat_recording));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void stop() {
		player.stop();
	}
	
	private void stopRecording() {
		try {
			Log.d("beatgrid", "here");
			recorder.stop();
			path = recorder.path;
			player.setDataSource(path);
			recording = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void toggle() {
		if (active)
			deselect();
		else
			select();
	}

}
