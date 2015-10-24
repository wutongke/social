package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class CardAddUserImageActivity extends Activity {

	private PopupWindows pwh = null;
	private ImageSelectHelper mIsh = null;
	private Map<String, String> network = new HashMap<String, String>();
	private ArrayList<String> netids = new ArrayList<String>();
	private ArrayList<String> neturls = new ArrayList<String>();
	private LoadImage mLoadImage = new LoadImage();
	private ZhuoConnHelper mConnHelper = null;
	private Map<String, View> toDelView = new HashMap<String, View>();
	private ArrayList<String> local = new ArrayList<String>();
	boolean isEditable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_image);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		ArrayList<String> images = intent
				.getStringArrayListExtra(CardEditActivity.EDIT_IMAGE_STR1);
		ArrayList<String> ids = intent
				.getStringArrayListExtra(CardEditActivity.EDIT_IMAGE_STR2);
		isEditable = intent.getBooleanExtra(CardEditActivity.EDITABLE, false);
		for (int i = 0; i < images.size(); i++) {
			if (ids.get(i).equals(CardEditActivity.LOCAL_IMAGE)) {
				local.add(images.get(i));
			} else {
				network.put(ids.get(i), images.get(i));
				netids.add(ids.get(i));
				neturls.add(images.get(i));
			}
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
			case MsgTagVO.INIT_SELECT:
				mIsh.insertNetworkImage(netids, neturls, mLoadImage,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								String id = (String) v.getTag();
								// mConnHelper.delheaderimg(id, mUIHandler,
								// MsgTagVO.PUB_INFO,
								// CardAddUserImageActivity.this, true,
								// null, id);
								toDelView.put(id, v);
								mIsh.removeFromContainer(toDelView.get(id));
							}
						}, null);
				mIsh.insertLocalImage(local);
				break;
			// case MsgTagVO.PUB_INFO:
			// if (JsonHandler.checkResult((String) msg.obj,
			// getApplicationContext())) {
			// Bundle bundle = msg.getData();
			// String id = bundle.getString("data");
			// mIsh.removeFromContainer(toDelView.get(id));
			// }
			// break;
			default:
				break;
			}
		}
	};

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwh.showPopDlg(findViewById(R.id.rootLayout),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								CardAddUserImageActivity.this.finish();
							}
						}, null, R.string.info27);
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
							ArrayList<String> ids = new ArrayList<String>();
							for (String tag : mIsh.getTags()) {
								if (network.containsKey(tag)) {
									ids.add(tag);
								} else {
									images.add(tag);
								}
							}
							Intent i = new Intent();
							i.putExtra(CardEditActivity.EDIT_IMAGE_STR1, images);
							i.putExtra(CardEditActivity.EDIT_IMAGE_STR2, ids);
							setResult(RESULT_OK, i);
							CardAddUserImageActivity.this.finish();
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			pwh.showPopDlg(findViewById(R.id.rootLayout),
					new OnClickListener() {

						@Override
						public void onClick(View paramView) {
							CardAddUserImageActivity.this.finish();

						}
					}, null, R.string.info27);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
