package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class CardAddUserImageActivity extends Activity {

	private PopupWindows pwh = null;
	private ImageSelectHelper mIsh = null;
	private LoadImage mLoadImage = new LoadImage();
	private Map<String, View> toDelView = new HashMap<String, View>();
	private ArrayList<String> local = new ArrayList<String>();
	boolean isEditable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_image);
		Intent intent = getIntent();
		ArrayList<String> images = intent
				.getStringArrayListExtra(CardEditActivity.EDIT_IMAGE_STR1);
		isEditable = intent.getBooleanExtra(CardEditActivity.EDITABLE, false);
		for (int i = 0; i < images.size(); i++) {
			local.add(images.get(i));
		}

		pwh = new PopupWindows(CardAddUserImageActivity.this);
		mIsh = ImageSelectHelper.getIntance(CardAddUserImageActivity.this,
				R.id.linearLayoutPicContainer);
		initSelecter();
		initClick();
	}

	private void initSelecter() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (mIsh.isInit()) {
						mIsh.initParams();
						Thread.sleep(20);
					}
					Message msg = mUIHandler
							.obtainMessage(MsgTagVO.INIT_SELECT);
					msg.sendToTarget();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_success);
				} else
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_failed);
				break;
			}
			case MsgTagVO.INIT_SELECT:
				mIsh.insertNetworkImage(local, local, mLoadImage,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								String id = (String) v.getTag();
								toDelView.put(id, v);
								mIsh.removeFromContainer(toDelView.get(id));
							}
						}, null);
				break;
			default:
				break;
			}
		}
	};

	boolean isLocal(String item) {
		for (int i = 0; i < local.size(); i++) {
			if (local.get(i).equals(item))
				return true;
		}
		return false;
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CardAddUserImageActivity.this.finish();
			}
		});
		if (!isEditable)
			findViewById(R.id.buttonSubmit).setVisibility(View.GONE);
		else {
			findViewById(R.id.buttonSubmit).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							ArrayList<String> images = new ArrayList<String>();
							StringBuilder sb = new StringBuilder();
							boolean flag = false;
							for (String tag : mIsh.getTags()) {
								if (isLocal(tag) == false) {
									images.add(tag);
								} else {
									if (flag)
										sb.append(",");
									sb.append(tag.subSequence(tag.lastIndexOf("/")+1, tag.length()));
									flag = true;
								}
							}
							ConnHelper.getInstance(getApplicationContext())
									.pubPhoto(CardAddUserImageActivity.this,
											mUIHandler, MsgTagVO.DATA_LOAD,
											sb.toString(), images);
						}
					});

			mIsh.getmAddButton().setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mIsh.initParams();
					if (mIsh.getTags() != null && mIsh.getTags().size() < 9) {
						pwh.showPop(findViewById(R.id.rootLayout));
					} else {
						mIsh.getmAddButton().setVisibility(View.GONE);
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error24);
					}
				}
			});
		}
	}

	String getPhotoStr(ArrayList<String> pics) {
		if (pics == null || pics.size() < 1) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		boolean flag = false;
		Set<String> set = toDelView.keySet();
		for (String item : pics) {
			boolean ff = false;
			for (Iterator iter = set.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				if (item.equals(key)) {
					ff = true;
					break;
				}
			}
			if (ff)
				continue;
			if (flag)
				sb.append(",");
			flag = true;
			sb.append(item);
		}

		return sb.toString();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String filePath = pwh.dealPhotoReturn(requestCode, resultCode, data,
				false);
		if (filePath != null) {
			try {
				mIsh.insertLocalImage(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	
}
