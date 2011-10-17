package com.zacharydenton.beatgrid;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.view.View;

public class Beat extends View implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4477957099798092027L;
	private final Rect rect = new Rect();
	private final Paint color = new Paint();
	private final BeatGrid beatGrid;
	private AudioRecorder recorder;
	private MediaPlayer player;
	private String path;
	private boolean active;
	private boolean recording;

	public Beat(Context context) {
		super(context);

		player = new MediaPlayer();
		beatGrid = (BeatGrid) context;
		clear();
	}

	public boolean deselect() {
		if (active) {
			invalidate(getRect());

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
			invalidate(getRect());

			return true;
		} else {
			return false;
		}
	}

	private String generatePath() {
		File soundDir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/BeatGrid/beats");
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

	private void play() throws IOException {
		try {
			if (!player.isPlaying()) {
				AudioManager mgr = (AudioManager) beatGrid
						.getSystemService(Context.AUDIO_SERVICE);
				float streamVolumeCurrent = mgr
						.getStreamVolume(AudioManager.STREAM_MUSIC);
				float streamVolumeMax = mgr
						.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
				float volume = streamVolumeCurrent / streamVolumeMax;
				player.setVolume(volume, volume);
				if (Prefs.getLooping(beatGrid)) {
					player.setLooping(true);
				} else {
					player.setLooping(false);
					player.setOnCompletionListener(new OnCompletionListener() {
						public void onCompletion(MediaPlayer player) {
							Beat.this.deselect();
						}
					});
				}
				player.start();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean select() {
		if (!active) {
			active = true;
			if ((path == null) && (!recording)) {
				if (!beatGrid.isRecording())
					startRecording();
				else
					return false;
			} else {
				try {
					play();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					deselect();
					e.printStackTrace();
				}
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
			beatGrid.setRecording(true);
			recorder.start();
			setColor(getResources().getColor(R.color.beat_recording));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void stop() {
		if (player.isPlaying()) {
			player.stop();
			try {
				player.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void loadSound(String filename) throws IllegalArgumentException,
			IllegalStateException, IOException {
		if (filename != null) {
			path = filename;
			player.setDataSource(path);
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.prepare();
			active = false;
			setColor(getResources().getColor(R.color.beat_initialized));
		}
	}

	private void stopRecording() {
		try {
			recorder.stop();
			loadSound(recorder.path);
			recording = false;
			beatGrid.setRecording(false);
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

	public void clear() {
		if (recording)
			stopRecording();
		if (active)
			stop();
		setColor(getResources().getColor(R.color.beat_inactive));

		player.reset();
		recorder = new AudioRecorder(generatePath());
		recording = false;
		active = false;
		path = null;
	}

	public String getPath() {
		return path;
	}

}
