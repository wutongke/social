package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.util.CommonUtil;
/**
 * Í¼Æ¬½éÉÜAPP½çÃæ
 * @author lz
 *
 */
public class PosterActivity extends Activity implements OnGestureListener {
	private ViewFlipper mViewFlipper;
	private GestureDetector mDetector;
	private LinearLayout layout;
	private int now = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poster);
		init();
	}

	private void init() {
		mDetector = new GestureDetector(this, this);
		mViewFlipper = (ViewFlipper) findViewById(R.id.VFPoster);
		layout = (LinearLayout) findViewById(R.id.linearLayoutPointers);
		findViewById(R.id.imageViewStart).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						goLogin();
					}
				});
	}

	private void goLogin() {
		Intent intent0 = new Intent(PosterActivity.this, LoginActivity.class);
		startActivity(intent0);
		PosterActivity.this.finish();
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 60) {
			int prev = now;
			if (mViewFlipper.getChildCount() != now + 1) {
				now++;

				mViewFlipper.setInAnimation(getApplicationContext(),
						R.anim.push_left_in);
				mViewFlipper.setOutAnimation(getApplicationContext(),
						R.anim.push_left_out);
				mViewFlipper.showNext();
				((ImageView) layout.getChildAt(now))
						.setImageResource(R.drawable.ico_pointer_on);
				((ImageView) layout.getChildAt(prev))
						.setImageResource(R.drawable.ico_pointer_off);
				return true;
			}
		} else if (e1.getX() - e2.getX() < -60) {
			int prev = now;
			if (now != 0) {
				now--;
				mViewFlipper.setInAnimation(getApplicationContext(),
						R.anim.push_right_in);
				mViewFlipper.setOutAnimation(getApplicationContext(),
						R.anim.push_right_out);
				mViewFlipper.showPrevious();
				((ImageView) layout.getChildAt(now))
						.setImageResource(R.drawable.ico_pointer_on);
				((ImageView) layout.getChildAt(prev))
						.setImageResource(R.drawable.ico_pointer_off);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onDown(MotionEvent arg0) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.mDetector.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		recycleBitmap((ViewGroup) findViewById(R.id.rootLayout));
		super.onDestroy();
	}

	public static void recycleBitmap(ViewGroup viewGroup) {
		if (viewGroup != null) {
			CommonUtil.recycleDrawable(viewGroup.getBackground());
			int count = viewGroup.getChildCount();
			for (int i = 0; i < count; i++) {
				View view = viewGroup.getChildAt(i);
				CommonUtil.recycleDrawable(view.getBackground());
				if (view instanceof ImageView) {
					CommonUtil
							.recycleDrawable(((ImageView) view).getDrawable());
				} else if (view instanceof ViewGroup) {
					recycleBitmap((ViewGroup) view);
				}
			}
		}
	}
}
