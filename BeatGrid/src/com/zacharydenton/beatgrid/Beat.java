package com.zacharydenton.beatgrid;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class Beat extends View {

	private final Rect rect = new Rect();
	private final Paint color = new Paint();
	private final BeatGrid beatGrid;
	private boolean active;

	public Beat(Context context) {
		super(context);
		
		beatGrid = (BeatGrid) context;		
		active = false;
		setColor(getResources().getColor(R.color.beat_inactive));
		
	}
	
	public void setRect(int x, int y, float width, float height) {
		rect.set((int) (x * width), (int) (y * height),
				(int) (x * width + width), (int) (y * height + height));
	}
	
	public void setColor(int c) {
		color.setColor(c);
	}
		
	public Paint getColor() {
		return color;
	}

	public Rect getRect() {
		return rect;
	}

	public boolean select() {
		if (!active) {
			active = true;
			setColor(getResources().getColor(R.color.beat_active));
			return true;
		} else {
			return false;
		}
		
	}
	
	public boolean deselect() {
		if (active) {
			active = false;
			setColor(getResources().getColor(R.color.beat_inactive));
			return true;
		} else {
			return false;
		}
	}

}
