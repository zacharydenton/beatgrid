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

	private int maxX;
	private int maxY;
	private float width;
	private float height;
	private int selX;
	private int selY;
	private final BeatGrid beatGrid;

	public GridView(Context context) {
		super(context);

		beatGrid = (BeatGrid) context;
		maxX = Prefs.getWidth(getContext());
		maxY = Prefs.getHeight(getContext());

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

		for (int i = 0; i < maxX; i++) {
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}
		for (int i = 0; i < maxY; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			toggle((int) (event.getX() / width), (int) (event.getY() / height));
			return true;
		default:
			return super.onTouchEvent(event);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / (float) maxX;
		height = h / (float) maxY;
		updateBeats(maxX, maxY, width, height);
		super.onSizeChanged(w, h, oldw, oldh);

	}

	private void updateBeats(int x, int y, float width, float height) {
		int i = 0;
		for (Beat beat : beatGrid.getBeats()) {
			beat.setRect(i % x, i / x, width, height);
			i++;
		}
	}

	private void toggleBeat(int x, int y) {
		Beat beat = beatGrid.getBeats().get(selY * maxX + selX);
		invalidate(beat.getRect());
		beat.toggle();
		invalidate(beat.getRect());
	}

	private void toggle(int x, int y) {
		selX = Math.min(Math.max(x, 0), maxX - 1);
		selY = Math.min(Math.max(y, 0), maxY - 1);
		toggleBeat(selX, selY);
	}
}
