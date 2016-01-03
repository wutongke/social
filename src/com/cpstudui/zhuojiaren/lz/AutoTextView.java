package com.cpstudui.zhuojiaren.lz;

import java.util.List;

import com.cpstudio.zhuojiaren.model.MessagePubVO;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;
/**
 * 滚动公告控件
 * @author lz
 *
 */
public class AutoTextView extends TextSwitcher implements
		ViewSwitcher.ViewFactory {
	private float mHeight;
	private Context mContext;
	private Rotate3dAnimation mInUp;
	private Rotate3dAnimation mOutUp;

	private Rotate3dAnimation mInDown;
	private Rotate3dAnimation mOutDown;
	//
	private List<MessagePubVO> list;
	public int index = 0;
	private Thread updateThread;
	private volatile boolean stopFlag = false;

	public String getCurrentId()
	{
		String id = null;
		if (index >= 0 && list != null && index < list.size())
			id = list.get(index).getId();
		return id;
	}

	public AutoTextView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	public AutoTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mHeight = 12;
		mContext = context;
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		setFactory(this);
		mInUp = createAnim(-90, 0, true, true);
		mOutUp = createAnim(0, 90, false, true);
		mInDown = createAnim(90, 0, true, false);
		mOutDown = createAnim(0, -90, false, false);
		setInAnimation(mInUp);
		setOutAnimation(mOutUp);
	}

	private Rotate3dAnimation createAnim(float start, float end,
			boolean turnIn, boolean turnUp) {
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				turnIn, turnUp);
		rotation.setDuration(800);
		rotation.setFillAfter(false);
		rotation.setInterpolator(new AccelerateInterpolator());
		return rotation;
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		TextView t = new TextView(mContext);
		t.setGravity(Gravity.CENTER);
		t.setTextSize(mHeight);
		t.setMaxLines(1);
		return t;
	}

	public void previous() {
		if (getInAnimation() != mInDown) {
			setInAnimation(mInDown);
		}
		if (getOutAnimation() != mOutDown) {
			setOutAnimation(mOutDown);
		}
	}

	public void next() {
		if (getInAnimation() != mInUp) {
			setInAnimation(mInUp);
		}
		if (getOutAnimation() != mOutUp) {
			setOutAnimation(mOutUp);
		}
	}

	class Rotate3dAnimation extends Animation {
		private final float mFromDegrees;
		private final float mToDegrees;
		private float mCenterX;
		private float mCenterY;
		private final boolean mTurnIn;
		private final boolean mTurnUp;
		private Camera mCamera;

		public Rotate3dAnimation(float fromDegrees, float toDegrees,
				boolean turnIn, boolean turnUp) {
			mFromDegrees = fromDegrees;
			mToDegrees = toDegrees;
			mTurnIn = turnIn;
			mTurnUp = turnUp;
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			mCamera = new Camera();
			mCenterY = getHeight() / 2;
			mCenterX = getWidth() / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			final float fromDegrees = mFromDegrees;
			float degrees = fromDegrees
					+ ((mToDegrees - fromDegrees) * interpolatedTime);
			final float centerX = mCenterX;
			final float centerY = mCenterY;
			final Camera camera = mCamera;
			final int derection = mTurnUp ? 1 : -1;
			final Matrix matrix = t.getMatrix();
			camera.save();
			if (mTurnIn) {
				camera.translate(0.0f, derection * mCenterY
						* (interpolatedTime - 1.0f), 0.0f);
			} else {
				camera.translate(0.0f, derection * mCenterY
						* (interpolatedTime), 0.0f);
			}
			camera.rotateX(degrees);
			camera.getMatrix(matrix);
			camera.restore();
			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}

	public void updateUI() {
		AutoTextView.this.setText(getList().get(0).getPublish());
		invalidate();
		stopFlag=false;
		if (updateThread == null)
			updateThread = new Thread(new updateThread());
		updateThread.start();
	}

	public void stopAutoText() {
		stopFlag = true;
	}

	class updateThread implements Runnable {
		long time = 2000; 
		int i = 0;

		public void run() {
			while (!stopFlag) {
				long sleeptime = updateIndex(i);
				time += sleeptime;
				mHandler.post(mUpdateResults);
				if (sleeptime == -1)
					return;
				try {
					Thread.sleep(time);
					i++;
					if (i == getList().size())
						i = 0;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	Handler mHandler = new Handler();
	Runnable mUpdateResults = new Runnable() {
		public void run() {
			AutoTextView.this.setText(getList().get(index).getPublish());
			invalidate();
		}
	};

	public long updateIndex(int index) {
		if (index == -1)
			return -1;
		this.index = index;
		return index;
	}

	public List<MessagePubVO> getList() {
		return list;
	}

	public void setList(List<MessagePubVO> list) {
		this.list = list;
		updateThread=null;
		this.index=0;
	}
}
