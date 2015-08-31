package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;
import com.utils.ImageRectUtil;

public class ImageSelectHelper implements OnClickListener {
	private Activity mActivity;
	private View mAddButton;
	private int mLineNum = 5;
	private int mHeight = 90;
	private int mMarginRight = 0;
	private LinearLayout.LayoutParams mTlp;
	private LinearLayout mContainer;
	private LinearLayout.LayoutParams mTll;
	private boolean init = true;
	private ArrayList<String> tags = new ArrayList<String>();
	private LayoutInflater inflater = null;

	private ImageSelectHelper(Activity activity, int container, String filePath) {
		this.mActivity = activity;
		this.mContainer = (LinearLayout) activity.findViewById(container);
		init();
		threadInit(filePath);
	}

	void init() {
		mTll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		LinearLayout ll = new LinearLayout(mActivity);
		ll.setLayoutParams(mTll);
		mContainer.addView(ll);
		inflater = LayoutInflater.from(mActivity);
		mAddButton = inflater.inflate(R.layout.item_image_add, null);
		((ImageView) (mAddButton.findViewById(R.id.imageViewPic)))
				.setImageResource(R.drawable.bg_head_add);
		ll.addView(mAddButton);
	}

	public static ImageSelectHelper getIntance(Activity activity, int container) {
		return new ImageSelectHelper(activity, container, null);
	}

	public static ImageSelectHelper getIntance(Activity activity,
			int container, String filePath) {
		return new ImageSelectHelper(activity, container, filePath);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			insertLocalImage((String) msg.obj);
		}
	};

	public View getmAddButton() {
		return mAddButton;
	}

	public boolean isInit() {
		return init;
	}

	public void threadInit(final String filePath) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (init) {
						initParams();
						Thread.sleep(20);
					}
					if (filePath != null && !filePath.equals("")) {
						Message msg = mHandler.obtainMessage();
						msg.obj = filePath;
						msg.sendToTarget();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void initParams() {
		if (init) {
			int allWidths = mContainer.getMeasuredWidth();
			if (allWidths == 0) {
				return;
			}
			int paddingLeft = mContainer.getPaddingLeft();
			int paddingRight = mContainer.getPaddingRight() == 0 ? paddingLeft
					: mContainer.getPaddingRight();
			int allWidth = allWidths - paddingLeft - paddingRight;
			mHeight = allWidth / mLineNum;
			mMarginRight = (allWidth - mLineNum * mHeight) / (mLineNum - 1);
			mTlp = new LinearLayout.LayoutParams(mHeight, mHeight);
			mTlp.rightMargin = mMarginRight;
			mAddButton.setLayoutParams(mTlp);
			init = false;
		}
	}

	private OnClickListener getViewListener(final String type) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = (String) v.getTag();
				Intent intent = new Intent(mActivity,
						PhotoViewMultiActivity.class);
				ArrayList<String> orgs = new ArrayList<String>();
				orgs.add(path);
				intent.putStringArrayListExtra("pics", orgs);
				intent.putExtra("pic", path);
				intent.putExtra("type", type);
				mActivity.startActivity(intent);
			}
		};
	}

	private OnClickListener getCardListener(final String uid) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mActivity, ZhuoMaiCardActivity.class);
				i.putExtra("userid", uid);
				mActivity.startActivity(i);
			}
		};
	}

	public boolean insertNetworkImage(Map<String, String> map,
			LoadImage loadImage, OnClickListener deletelistener, String type) {
		if (null != map) {
			for (String tag : map.keySet()) {
				String url = map.get(tag);
				if (tags.contains(tag)) {
					continue;
				}
				tags.add(tag);
				View rl = inflater.inflate(R.layout.item_image_add, null);
				rl.setLayoutParams(mTlp);
				ImageView iv = (ImageView) rl.findViewById(R.id.imageViewPic);
				iv.setImageResource(0);
				iv.setBackgroundColor(android.graphics.Color.GRAY);
				iv.setTag(url);
				loadImage.addTask(url, iv);
				rl.setTag(tag);
				if (null != type && type.equals("card")) {
					iv.setOnClickListener(getCardListener(tag));
				} else {
					iv.setOnClickListener(getViewListener("network"));
				}
				View del = (ImageView) rl.findViewById(R.id.imageViewDel);
				del.setTag(tag);
				del.setVisibility(View.VISIBLE);
				if (null != deletelistener) {
					del.setOnClickListener(deletelistener);
				} else {
					del.setOnClickListener(this);
				}
				resetContainer(rl);
			}
			loadImage.doTask();
			return true;
		}
		return false;
	}

	public boolean insertNetworkImage(ArrayList<String> ids,
			ArrayList<String> urls, LoadImage loadImage,
			OnClickListener deletelistener, String type) {
		if (null != ids && ids.size() == urls.size()) {
			for (int i = 0; i < ids.size(); i++) {
				String tag = ids.get(i);
				String url = urls.get(i);
				if (tags.contains(tag)) {
					continue;
				}
				tags.add(tag);
				View rl = inflater.inflate(R.layout.item_image_add, null);
				rl.setLayoutParams(mTlp);
				ImageView iv = (ImageView) rl.findViewById(R.id.imageViewPic);
				iv.setImageResource(0);
				iv.setBackgroundColor(android.graphics.Color.GRAY);
				iv.setTag(url);
				loadImage.addTask(url, iv);
				rl.setTag(tag);
				if (null != type && type.equals("card")) {
					iv.setOnClickListener(getCardListener(tag));
				} else {
					iv.setOnClickListener(getViewListener("network"));
				}
				View del = (ImageView) rl.findViewById(R.id.imageViewDel);
				del.setTag(tag);
				del.setVisibility(View.VISIBLE);
				if (null != deletelistener) {
					del.setOnClickListener(deletelistener);
				} else {
					del.setOnClickListener(this);
				}
				resetContainer(rl);
			}
			loadImage.doTask();
			return true;
		}
		return false;
	}

	public boolean insertDefaultImage(Map<String, Integer> map,
			OnClickListener deletelistener, String type) {
		if (null != map) {
			for (String tag : map.keySet()) {
				int resId = map.get(tag);
				if (tags.contains(tag)) {
					continue;
				}
				tags.add(tag);
				View rl = inflater.inflate(R.layout.item_image_add, null);
				rl.setLayoutParams(mTlp);
				ImageView iv = (ImageView) rl.findViewById(R.id.imageViewPic);
				iv.setImageResource(resId);
				rl.setTag(tag);
				if (null != type && type.equals("card")) {
					iv.setOnClickListener(getCardListener(tag));
				}
				View del = (ImageView) rl.findViewById(R.id.imageViewDel);
				del.setTag(tag);
				del.setVisibility(View.VISIBLE);
				if (null != deletelistener) {
					del.setOnClickListener(deletelistener);
				} else {
					del.setOnClickListener(this);
				}
				resetContainer(rl);
			}
			return true;
		}
		return false;
	}

	public boolean updateNetworkImage(String url, LoadImage loadImage,
			String type) {
		if (null != url) {
			ImageView iv = (ImageView) mAddButton
					.findViewById(R.id.imageViewPic);
			iv.setImageResource(0);
			iv.setBackgroundColor(Color.GRAY);
			iv.setTag(url);
			if (null != type) {
				iv.setOnClickListener(getViewListener("type"));
			} else {
				iv.setOnClickListener(getViewListener("network"));
			}
			loadImage.addTask(url, iv);
			tags.clear();
			tags.add(url);
			return true;
		}
		return false;
	}

	public boolean updateImage(String filePath) {
		if (null != filePath) {
			ImageView iv = (ImageView) mAddButton
					.findViewById(R.id.imageViewPic);
			iv.setImageResource(0);
			iv.setBackgroundColor(Color.GRAY);
			iv.setImageBitmap(ImageRectUtil.imageThumbCrop(filePath, mHeight,
					mHeight));
			iv.setTag(filePath);
			iv.setOnClickListener(getViewListener("local"));
			tags.clear();
			tags.add(filePath);
			return true;
		}
		return false;
	}

	public boolean insertLocalImage(ArrayList<String> locals) {
		if (locals != null) {
			for (String filePath : locals) {
				insertLocalImage(filePath);
			}
			return true;
		}
		return false;
	}

	public boolean insertLocalImage(String filePath) {
		if (filePath != null && !filePath.trim().equals("")) {
			if (init) {
				threadInit(filePath);
				return false;
			}
			if (tags.contains(filePath)) {
				return true;
			}
			tags.add(filePath);
			View rl = inflater.inflate(R.layout.item_image_add, null);
			rl.setLayoutParams(mTlp);
			ImageView iv = (ImageView) rl.findViewById(R.id.imageViewPic);
			iv.setImageResource(0);
			iv.setImageBitmap(ImageRectUtil.imageThumbCrop(filePath, mHeight,
					mHeight));
			rl.setTag(filePath);
			iv.setTag(filePath);
			iv.setOnClickListener(getViewListener("local"));
			View del = (ImageView) rl.findViewById(R.id.imageViewDel);
			del.setTag(filePath);
			del.setVisibility(View.VISIBLE);
			del.setOnClickListener(this);
			resetContainer(rl);
			return true;
		}
		return false;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	@Override
	public void onClick(View v) {
		removeFromContainer(v);
	}

	public void removeFromContainer(View view) {
		mAddButton.setVisibility(View.VISIBLE);
		View v = (View) view.getParent();
		tags.remove(v.getTag());
		LinearLayout parentView = (LinearLayout) v.getParent();
		if (parentView.getChildCount() == 1) {
			((LinearLayout) parentView.getParent()).removeView(parentView);
		} else {
			parentView.removeView(v);
		}
		resetContainer();
	}

	public void clear() {
		tags.clear();
		if (mContainer != null)
			mContainer.removeAllViews();

		init();
	}

	public void removeAll() {
		tags.clear();
		mContainer.removeAllViews();
	}

	private void resetContainer(View v) {
		int allParent = mContainer.getChildCount();
		for (int i = allParent - 1; i >= 0; i--) {
			LinearLayout nowParent = (LinearLayout) mContainer.getChildAt(i);
			if (nowParent != null) {
				if (nowParent.getChildCount() == mLineNum) {
					LinearLayout ll = new LinearLayout(mActivity);
					ll.setLayoutParams(mTll);
					mContainer.addView(ll);
					View now = nowParent.getChildAt(mLineNum - 1);
					nowParent.removeView(now);
					ll.addView(now);
					i++;
				} else if (nowParent.getChildCount() < mLineNum) {
					if (i > 0) {
						LinearLayout prevParent = (LinearLayout) mContainer
								.getChildAt(i - 1);
						if (null != prevParent
								&& prevParent.getChildCount() == mLineNum) {
							View prev = prevParent.getChildAt(mLineNum - 1);
							prevParent.removeView(prev);
							nowParent.addView(prev, 0);
						}
					} else {
						nowParent.addView(v, 0);
					}
				}
			}
		}
	}

	private void resetContainer() {
		int allParent = mContainer.getChildCount();
		if (allParent > 1) {
			for (int i = 0; i < allParent; i++) {
				LinearLayout nowParent = (LinearLayout) mContainer
						.getChildAt(i);
				if (nowParent != null && nowParent.getChildCount() < mLineNum
						&& i < allParent - 1) {
					LinearLayout nextParent = (LinearLayout) mContainer
							.getChildAt(i + 1);
					if (nextParent != null && nextParent.getChildCount() > 0) {
						View next = nextParent.getChildAt(0);
						nextParent.removeView(next);
						nowParent.addView(next);
						if (nextParent.getChildCount() == 0) {
							mContainer.removeView(nextParent);
						}
					}
				}
			}
		}
	}
}
