package com.cpstudio.zhuojiaren.widget;

import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class XYListView extends ListView {
	private float x = 0;
	private float y = 0;
	private float times = 2;

	public XYListView(Context context) {
		super(context);
		times = DeviceInfoUtil.getDeviceCsd(context);
	}

	public XYListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		times = DeviceInfoUtil.getDeviceCsd(context);
	}

	public XYListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		times = DeviceInfoUtil.getDeviceCsd(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = ev.getX();
			y = ev.getY();
			return false;
		}
		if (Math.abs((ev.getX() - x) / (ev.getY() - y)) < 0.5 && super.onInterceptTouchEvent(ev)){
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (Math.abs((ev.getX() - x)) < 30 * times || x == 0) {
			onTouchEvent(ev);
			
		}
		return super.dispatchTouchEvent(ev);
	}

}
