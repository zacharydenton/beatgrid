package com.zacharydenton.beatgrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class GridView extends View {
	
	private int maxX;
	private int maxY;
	private float width;
	private float height;
	private int selX;
	private int selY;
	private final Rect selRect = new Rect();
	private final BeatGrid beatGrid;

	public GridView(Context context) {
		super(context);
		
		beatGrid = (BeatGrid) context;
		maxX = beatGrid.getWidth();
		maxY = beatGrid.getHeight();
		
		setFocusable(true);
		setFocusableInTouchMode(true);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.grid_background));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.grid_dark));
		
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.grid_hilite));
		
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.grid_light));
		
		for (int i = 0; i < maxX; i++) {
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(), hilite);	
		}
		for (int i = 0; i < maxY; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1, hilite);	
		}		
		
		Paint selected = new Paint();
		selected.setColor(getResources().getColor(R.color.grid_selected));
		canvas.drawRect(selRect, selected);
					
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			select(selX, selY-1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			select(selX, selY+1);
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			select(selX-1, selY);
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			select(selX+1, selY);
			break;
		default:
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			select((int) (event.getX() / width), (int) (event.getY() / height));
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / (float) maxX;
		height = h / (float) maxY;
		getRect(selX, selY, selRect);
		super.onSizeChanged(w, h, oldw, oldh);
		
	}
	
	private void getRect(int x, int y, Rect rect) { 
		rect.set((int) (x * width), (int) (y * height),
				(int) (x * width + width), (int) (y * height + height));
	}
	
	private void select(int x, int y) {
		invalidate(selRect);
		selX = Math.min(Math.max(x, 0), maxX-1);
		selY = Math.min(Math.max(y,0), maxY-1);
		getRect(selX, selY, selRect);
		invalidate(selRect);
	}
}
