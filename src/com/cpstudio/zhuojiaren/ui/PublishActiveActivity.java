package com.cpstudio.zhuojiaren.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.BaiduLocationHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class PublishActiveActivity extends Activity {

	private PopupWindows pwh = null;
	private ImageSelectHelper mIsh = null;
	private String mLocation = "";
	private ConnHelper mConnHelper = null;
	private BaiduLocationHelper locationHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_active);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(PublishActiveActivity.this);
		Intent i = getIntent();
		String filePath = i.getStringExtra("filePath");
		mIsh = ImageSelectHelper.getIntance(PublishActiveActivity.this,
				R.id.linearLayoutPicContainer, filePath);
		initClick();
		locationHelper = new BaiduLocationHelper(getApplicationContext(),
				mUIHandler, MsgTagVO.UPDATE_LOCAL);
	}

	@Override
	protected void onDestroy() {
		if (locationHelper != null) {
			locationHelper.stopLocation();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (locationHelper != null) {
			locationHelper.stopLocation();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (locationHelper != null) {
			locationHelper.startLocation();
		}
		super.onResume();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (msg.obj != null && !msg.obj.equals("")) {
					if (JsonHandler.checkResult((String) msg.obj)) {
						OnClickListener listener = new OnClickListener() {
							@Override
							public void onClick(View v) {
								setResult(RESULT_OK);
								PublishActiveActivity.this.finish();
							}
						};
						try {
							pwh.showPopDlgOne(findViewById(R.id.rootLayout),
									listener, R.string.info62);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case MsgTagVO.UPDATE_LOCAL: {
				String locationinfo = (String) msg.obj;
				if (null != locationinfo && !locationinfo.trim().equals("")) {
					mLocation = locationinfo;
					((TextView) findViewById(R.id.textViewPosInfo))
							.setText(locationinfo);
				} else {
					((TextView) findViewById(R.id.textViewPosInfo))
							.setText(getString(R.string.error10));
				}
				break;
			}
			default: {

			}

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
								PublishActiveActivity.this.finish();
							}
						}, null, R.string.info69);
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						publish();
					}
				});

		findViewById(R.id.textViewPosInfo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						((TextView) v).setText("º”‘ÿŒª÷√÷–...");
						if (locationHelper != null) {
							locationHelper.stopLocation();
							locationHelper.startLocation();
						}
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

	private void publish() {
		EditText contentEditText = (EditText) findViewById(R.id.editTextchoiceContent);
		String content = contentEditText.getText().toString();
		String tag = ((TextView) findViewById(R.id.editTextchoiceTag))
				.getText().toString();
		if (content.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info24);
			contentEditText.requestFocus();
			return;
		}
		String imgCnt = "0";
		if (mIsh.getTags() != null) {
			imgCnt = mIsh.getTags().size() + "";
		}
		mConnHelper.pubDynamic(PublishActiveActivity.this, mUIHandler,
				MsgTagVO.PUB_INFO, content, mIsh.getTags());
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String filePath = pwh.dealPhotoReturn(requestCode, resultCode, data,
				false);
		if (filePath != null) {
			try {
				mIsh.insertLocalImage(filePath);
				if (mIsh.getTags() != null && mIsh.getTags().size() == 9) {
					mIsh.getmAddButton().setVisibility(View.GONE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					PublishActiveActivity.this.finish();
				}
			};
			pwh.showPopDlg(findViewById(R.id.rootLayout), listener, null,
					R.string.info69);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
