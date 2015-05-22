package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;

public class EmotionPopHelper {
	private PopupWindow popupWindow;
	private Activity mActivity;
	private ViewFlipper mViewFlipper;
	private EmotionGestureListener mDetector;
	private static int[] emotionResIds = new int[] { R.drawable.bq01,
			R.drawable.bq02, R.drawable.bq03, R.drawable.bq04, R.drawable.bq05,
			R.drawable.bq06, R.drawable.bq07, R.drawable.bq08, R.drawable.bq09,
			R.drawable.bq10, R.drawable.bq11, R.drawable.bq12, R.drawable.bq13,
			R.drawable.bq14, R.drawable.bq15, R.drawable.bq16, R.drawable.bq17,
			R.drawable.bq18, R.drawable.bq19, R.drawable.bq20, R.drawable.bq21,
			R.drawable.bq22, R.drawable.bq23, R.drawable.bq24 };
	private static Map<String, Integer> emotionMap = new HashMap<String, Integer>();
	private OnItemClickListener mClickItem;
	private int lineNum = 8;
	private int now = 0;
	private int line = 0;
	private LinearLayout container;

	public EmotionPopHelper(Activity activity, OnItemClickListener click) {
		this.mActivity = activity;
		this.mClickItem = click;
		gentEmotionMap();
	}

	@SuppressWarnings("deprecation")
	public PopupWindow showBottomPop(View parent) {
		if (null == popupWindow) {
			LayoutInflater layoutInflater = (LayoutInflater) mActivity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout view = (RelativeLayout) layoutInflater.inflate(
					R.layout.widget_emotion, null);
			mViewFlipper = (ViewFlipper) view.findViewById(R.id.VFPoster);
			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return mDetector.getDector().onTouchEvent(event);
				}
			});
			mDetector = new EmotionGestureListener(mActivity, null,
					mViewFlipper);
			initPop(view);
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);

		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.AnimBottom);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(parent, 0, 0);
		return popupWindow;
	}

	public static Map<String, Integer> gentEmotionMap() {
		if (emotionMap == null || emotionMap.isEmpty()) {
			for (int i = 0; i < emotionResIds.length; i++) {
				emotionMap.put(getKey(i), emotionResIds[i]);
			}
		}
		return emotionMap;
	}

	private static String getKey(int i) {
		String key = "zjr" + i;
		if (i < 10) {
			key = "zjr0" + i;
		}
		return key;
	}

	private void initPop(View view) {
		line = (int) Math.ceil(emotionResIds.length / (double) lineNum);
		container = (LinearLayout) view
				.findViewById(R.id.linearLayoutContainer);
		LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lllp.leftMargin = (int) (20 * DeviceInfoUtil.getDeviceCsd(mActivity));
		for (int j = 0; j < line; j++) {
			GridView gv = new GridView(mActivity);
			gv.setNumColumns(lineNum / 2);
			gv.setGravity(Gravity.CENTER);
			gv.setSelector(R.drawable.indicator_list);
			LayoutParams layoutParams = new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			gv.setLayoutParams(layoutParams);
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			for (int i = j * lineNum; i < (j + 1) * lineNum
					&& i < emotionResIds.length; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("image", emotionResIds[i]);
				map.put("text", getKey(i));
				list.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(mActivity, list,
					R.layout.item_emotion, new String[] { "image", "text" },
					new int[] { R.id.image, R.id.text });
			gv.setAdapter(adapter);
			gv.setOnItemClickListener(mClickItem);
			gv.setOnTouchListener(mDetector);
			mViewFlipper.addView(gv);
			ImageView iv = new ImageView(mActivity);
			iv.setLayoutParams(lllp);
			if (j == 0) {
				iv.setImageResource(R.drawable.ico_pointer_on);
			} else {
				iv.setImageResource(R.drawable.ico_pointer_off);
			}
			container.addView(iv);
		}
	}

	class EmotionGestureListener extends SimpleOnGestureListener implements
			OnTouchListener {

		private GestureDetector gDetector;
		private ViewFlipper viewFlipper;
		private Context mContext;
		private float times = 0;

		public EmotionGestureListener(Context con, GestureDetector gDetector,
				ViewFlipper viewFlipper) {
			if (null == gDetector) {
				gDetector = new GestureDetector(con, this);
			}
			this.mContext = con;
			this.gDetector = gDetector;
			this.viewFlipper = viewFlipper;
			this.times = DeviceInfoUtil.getDeviceCsd(con);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return super.onSingleTapConfirmed(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (container != null) {
				((ImageView) (container.getChildAt(now)))
						.setImageResource(R.drawable.ico_pointer_off);
				if (e1.getX() - e2.getX() > 60 * times) {
					viewFlipper.setInAnimation(mContext, R.anim.push_left_in);
					viewFlipper.setOutAnimation(mContext, R.anim.push_left_out);
					viewFlipper.showNext();
					if (now + 1 < line) {
						now++;
					} else {
						now = 0;
					}
					((ImageView) (container.getChildAt(now)))
							.setImageResource(R.drawable.ico_pointer_on);
					return true;
				} else if (e1.getX() - e2.getX() < -60 * times) {
					viewFlipper.setInAnimation(mContext, R.anim.push_right_in);
					viewFlipper
							.setOutAnimation(mContext, R.anim.push_right_out);
					viewFlipper.showPrevious();
					if (now > 0) {
						now--;
					} else {
						now = line - 1;
					}
					((ImageView) (container.getChildAt(now)))
							.setImageResource(R.drawable.ico_pointer_on);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			return gDetector.onTouchEvent(event);
		}

		public GestureDetector getDector() {
			return this.gDetector;
		}

	}
}
