package com.cpstudio.zhuojiaren;

import java.io.File;
import java.util.ArrayList;

import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.imageloader.LoadImage.Callback;
import com.cpstudio.zhuojiaren.model.LoadImageResultVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PhotoViewActivity extends Activity implements OnGestureListener {
	ArrayList<String> pics = new ArrayList<String>();
	private int now = 0;
	private ImageView imageViewPhoto = null;
	private LoadImage mLoadImage = new LoadImage();
	private GestureDetector mDetector;
	private LinearLayout layout;
	private String mType = null;
	private View processbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_view);
		Intent intent = getIntent();
		pics = intent.getStringArrayListExtra("pics");
		String pic = intent.getStringExtra("pic");
		mType = intent.getStringExtra("type");
		imageViewPhoto = (ImageView) findViewById(R.id.imageViewPhoto);
		mDetector = new GestureDetector(this, this);
		layout = (LinearLayout) findViewById(R.id.linearLayoutPointers);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		layoutParams.leftMargin = 5;
		layoutParams.rightMargin = 5;
		processbar = findViewById(R.id.progressBar);
		mLoadImage.setOnReturnListener(onReturnListener);
		for (int i = 0; i < pics.size(); i++) {
			ImageView iv = new ImageView(PhotoViewActivity.this);
			iv.setLayoutParams(layoutParams);
			if (pics.get(i).equals(pic)) {
				now = i;
				iv.setImageResource(R.drawable.ico_pointer_on);
			} else {
				iv.setImageResource(R.drawable.ico_pointer_off);
			}
			layout.addView(iv);
		}
		if (mType == null || mType.equals("network")) {
			imageViewPhoto.setTag(pic);
			mLoadImage.addTask(pic, imageViewPhoto);
			mLoadImage.doTask();
		} else {
			findViewById(R.id.progressBar).setVisibility(View.GONE);
			// try {
			// imageViewPhoto.setImageBitmap(ImageRectUtil.memoryMaxImage(pic));
			// } catch (IOException e) {
			// CommonUtil.displayToast(getApplicationContext(),
			// R.string.error1);
			// e.printStackTrace();
			// }
			imageViewPhoto.setImageURI(Uri.fromFile(new File(pic)));
		}
		initClick();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.UPDATE: {
				processbar.setVisibility(View.GONE);
				LoadImageResultVO resultVO = (LoadImageResultVO) msg.obj;
				boolean result = resultVO.isLoadSuccess();
				if (!result) {
					if (((String) imageViewPhoto.getTag()).equals(resultVO
							.getUrl())) {
						imageViewPhoto
								.setImageResource(R.drawable.default_image);
					}
				}
				break;
			}

			}
		}
	};

	private Callback onReturnListener = new Callback() {

		@Override
		public boolean onReturn(boolean result, String url) {
			LoadImageResultVO resultVO = new LoadImageResultVO();
			resultVO.setLoadSuccess(result);
			resultVO.setUrl(url);
			Message msg = mUIHandler.obtainMessage(MsgTagVO.UPDATE);
			msg.obj = resultVO;
			msg.sendToTarget();
			return false;
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.mDetector.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent paramMotionEvent) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent paramMotionEvent) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent paramMotionEvent) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float paramFloat1,
			float paramFloat2) {
		if (pics != null && pics.size() > 1) {
			if (e1.getX() - e2.getX() > 100) {
				int prev = now;
				if (pics.size() == now + 1) {
					now = 0;
				} else {
					now++;
				}
				String url = pics.get(now);
				if (mType == null || mType.equals("network")) {
					imageViewPhoto.setTag(url);
					mLoadImage.addTask(url, imageViewPhoto);
					mLoadImage.doTask();
				} else {
					// try {
					// imageViewPhoto.setImageBitmap(ImageRectUtil.memoryMaxImage(url));
					// } catch (IOException e) {
					// CommonUtil.displayToast(getApplicationContext(),
					// R.string.error1);
					// e.printStackTrace();
					// }
					imageViewPhoto.setImageURI(Uri.fromFile(new File(url)));
				}
				((ImageView) layout.getChildAt(now))
						.setImageResource(R.drawable.ico_pointer_on);
				((ImageView) layout.getChildAt(prev))
						.setImageResource(R.drawable.ico_pointer_off);
				return true;
			} else if (e1.getX() - e2.getX() < -100) {
				int prev = now;
				if (now == 0) {
					now = pics.size() - 1;
				} else {
					now--;
				}
				String url = pics.get(now);
				if (mType == null || mType.equals("network")) {
					imageViewPhoto.setTag(url);
					mLoadImage.addTask(url, imageViewPhoto);
					mLoadImage.doTask();
				} else {
					// try {
					// imageViewPhoto.setImageBitmap(ImageRectUtil.memoryMaxImage(url));
					// } catch (IOException e) {
					// CommonUtil.displayToast(getApplicationContext(),
					// R.string.error1);
					// e.printStackTrace();
					// }
					imageViewPhoto.setImageURI(Uri.fromFile(new File(url)));
				}
				((ImageView) layout.getChildAt(now))
						.setImageResource(R.drawable.ico_pointer_on);
				((ImageView) layout.getChildAt(prev))
						.setImageResource(R.drawable.ico_pointer_off);
				return true;
			}
		}
		return false;
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PhotoViewActivity.this.finish();
			}
		});
	}
	
}
