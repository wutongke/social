package com.cpstudio.zhuojiaren.adapter;

import java.io.File;
import java.util.ArrayList;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.imageloader.LoadImage.Callback;
import com.cpstudio.zhuojiaren.model.LoadImageResultVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import uk.co.senab.photoview.PhotoView;
import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class PhotoPagerAdapter extends PagerAdapter {
	private String mType;
	private PhotoView mPhotoView;
	private LoadImage mLoadImage = new LoadImage();
	private ArrayList<String> mPics = new ArrayList<String>();
	private View mProcessBar;

	public PhotoPagerAdapter(ArrayList<String> pics, View processbar,
			String type) {
		this.mPics = pics;
		this.mType = type;
		this.mProcessBar = processbar;
		this.mLoadImage.setOnReturnListener(onReturnListener);
	}

	@Override
	public int getCount() {
		return mPics.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		mPhotoView = new PhotoView(container.getContext());
		if (mType == null || mType.equals("network")) {
			mProcessBar.setVisibility(View.VISIBLE);
			mPhotoView.setTag(mPics.get(position));
			mLoadImage.addTask(mPics.get(position), mPhotoView);
			mLoadImage.doTask();
		} else {
			mPhotoView.setImageURI(Uri.fromFile(new File(mPics.get(position))));
//			try {
//				mPhotoView.setImageBitmap(ImageRectUtil.revitionImageSize(
//						mPics.get(position), 640, 960));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		container.addView(mPhotoView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		return mPhotoView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
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

}
