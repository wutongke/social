package com.cpstudio.zhuojiaren;

import java.io.File;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.imageloader.LoadImage.Callback;
import com.cpstudio.zhuojiaren.model.LoadImageResultVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class PhotoSingleActivity extends Activity {

	private LoadImage mLoadImage = new LoadImage();
	private View mProcessBar;
	private ImageView mPhotoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_single);
		mPhotoView = (ImageView) findViewById(R.id.iv_photo);
		Intent intent = getIntent();
		String mType = intent.getStringExtra("type");
		String pic = intent.getStringExtra("pic");
		mProcessBar = findViewById(R.id.progressBar);
		mLoadImage.setOnReturnListener(onReturnListener);
		if (mType == null || mType.equals("network")) {
			mProcessBar.setVisibility(View.VISIBLE);
			mPhotoView.setTag(pic);
			mLoadImage.addTask(pic, mPhotoView);
			mLoadImage.doTask();
		} else {
			mPhotoView.setImageURI(Uri.fromFile(new File(pic)));
//			try {
//				mPhotoView.setImageBitmap(ImageRectUtil.memoryMaxImage(pic));
//			} catch (IOException e) {
//				CommonUtil.displayToast(getApplicationContext(),
//						R.string.error1);
//				e.printStackTrace();
//			}
		}
		initClick();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.UPDATE: {
				mProcessBar.setVisibility(View.GONE);
				LoadImageResultVO resultVO = (LoadImageResultVO) msg.obj;
				boolean result = resultVO.isLoadSuccess();
				if (!result) {
					if (((String) mPhotoView.getTag())
							.equals(resultVO.getUrl())) {
						mPhotoView.setImageResource(R.drawable.default_image);
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

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PhotoSingleActivity.this.finish();
			}
		});
	}
	
}
