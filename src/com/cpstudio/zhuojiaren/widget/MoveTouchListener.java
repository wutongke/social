package com.cpstudio.zhuojiaren.widget;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MoveTouchListener implements android.view.View.OnTouchListener {
	private int acceptMove = 0;
	private int resultMove = 0;
	private int minMove = 15;
	private float x = 0;
	private float startx = 0;
	private float y = 0;
	private float top = 0;
	private int height = 0;
	private boolean up = false;
	private float times = 0;
	private float scroll = 0;
	private int scrollTemp = 0;
	private boolean returnValue = false;

	public MoveTouchListener(int resultMove, boolean itemClick) {
		this.resultMove = resultMove;
		this.acceptMove = (int) (resultMove * 5 / 7.0);
		this.returnValue = itemClick;
	}

	public MoveTouchListener(int resultMove) {
		this.resultMove = resultMove;
		this.acceptMove = (int) (resultMove * 5 / 7.0);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		System.out.println(event.getAction());
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = event.getRawX();
			startx = x;
			y = event.getRawY();
			minMove = 15;
			height = v.getMeasuredHeight();
			int[] location = new int[2];
			v.getLocationOnScreen(location);
			top = location[1];
			DisplayMetrics localDisplayMetrics = v.getContext().getResources()
					.getDisplayMetrics();
			times = localDisplayMetrics.density;
			break;
		case MotionEvent.ACTION_MOVE:
			float distinctx = event.getRawX() - x;
			if (event.getRawY() > top && event.getRawY() < (top + height)) {
				if (up) {
					startx = event.getRawX();
					up = false;
				} else {
					float distincty = event.getRawY() - y;
					if (Math.abs(distinctx / distincty) > 3) {
						if (scroll == 0) {
							if (distinctx < -resultMove * times) {
								scrollTemp = (int) (resultMove * times);
								v.scrollTo(scrollTemp, 0);
							} else if (distinctx < -minMove * times) {
								scrollTemp = -(int) distinctx;
								v.scrollTo(scrollTemp, 0);
							}
						} else {
							if (distinctx > resultMove * times) {
								scrollTemp = 0;
								v.scrollTo(scrollTemp, 0);
							} else if (distinctx > minMove * times) {
								scrollTemp = (int) (scroll - distinctx);
								v.scrollTo(scrollTemp, 0);
							}
						}
					}
					if ((event.getRawX() - startx) * distinctx < 0) {
						x = event.getRawX();
						minMove = 0;
						scroll = scrollTemp;
					}
					startx = event.getRawX();
					if (distinctx < -minMove * times
							|| distinctx > minMove * times) {
						return true;
					}
				}
			} else {
				actionup(v, event);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (actionup(v, event)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_CANCEL:
			v.scrollTo(0, 0);
			scroll = 0;
			break;
		}
		return returnValue;
	}

	private boolean actionup(View v, MotionEvent event) {
		if (!up) {
			float distinctx = event.getRawX() - x;
			if (scroll == 0) {
				if (distinctx < -acceptMove * times && scrollTemp > 0) {
					scroll = resultMove * times;
					v.scrollTo((int) scroll, 0);
				} else {
					v.scrollTo(0, 0);
				}
			} else {
				if (distinctx > acceptMove * times && scrollTemp < scroll) {
					scroll = 0;
					v.scrollTo(0, 0);
				} else {
					v.scrollTo((int) scroll, 0);
				}
			}
			up = true;
			if (distinctx < -minMove * times || distinctx > minMove * times) {
				return true;
			}
		}
		return false;
	}
}
