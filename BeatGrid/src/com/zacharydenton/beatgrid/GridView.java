package com.zacharydenton.beatgrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class GridView extends View {

	private int width;
	private int height;
	private float cellWidth;
	private float cellHeight;
	private int selX;
	private int selY;
	private final BeatGrid beatGrid;

	public GridView(Context context) {
		super(context);

		beatGrid = (BeatGrid) context;
		width = Prefs.getWidth(getContext());
		height = Prefs.getHeight(getContext());

		setFocusable(true);
		setFocusableInTouchMode(true);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Paint background = new Paint();
		// background.setColor(getResources().getColor(R.color.grid_background));
		// canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		for (Beat beat : beatGrid.getBeats()) {
			canvas.drawRect(beat.getRect(), beat.getColor());
		}

		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.grid_dark));

		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.grid_hilite));

		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.grid_light));

		for (int i = 0; i < width; i++) {
			canvas.drawLine(i * cellWidth, 0, i * cellWidth, getHeight(), light);
			canvas.drawLine(i * cellWidth + 1, 0, i * cellWidth + 1, getHeight(),
					hilite);
		}
		for (int i = 0; i < height; i++) {
			canvas.drawLine(0, i * cellHeight, getWidth(), i * cellHeight, light);
			canvas.drawLine(0, i * cellHeight + 1, getWidth(), i * cellHeight + 1,
					hilite);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			toggle((int) (event.getX() / cellWidth), (int) (event.getY() / cellHeight));
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		cellWidth = w / (float) width;
		cellHeight = h / (float) height;
		updateBeats(width, height, cellWidth, cellHeight);
		super.onSizeChanged(w, h, oldw, oldh);

	}

	private void updateBeats(int width, int height, float cellWidth, float cellHeight) {
		int i = 0;
		for (Beat beat : beatGrid.getBeats()) {
			beat.setRect(i % width, i / width, cellWidth, cellHeight);
			i++;
		}
	}

	private void toggleBeat(int x, int y) {
		Beat beat = beatGrid.getBeats().get(y * width + x);
		invalidate(beat.getRect());
		beat.toggle();
		invalidate(beat.getRect());
	}

	private void toggle(int x, int y) {
		selX = Math.min(Math.max(x, 0), width - 1);
		selY = Math.min(Math.max(y, 0), height - 1);
		toggleBeat(selX, selY);
	}
}
