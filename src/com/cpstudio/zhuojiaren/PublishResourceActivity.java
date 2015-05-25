package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.BaiduLocationHelper;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PopupWindows.WheelOKClick;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class PublishResourceActivity extends Activity {

	private PopupWindows pwh = null;
	private ImageSelectHelper mIsh = null;
	private String mType = "demand";
	private String mCategory = "1";
	private String mLocation = "";
	private ZhuoConnHelper mConnHelper = null;
	private BaiduLocationHelper locationHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_gong_xu);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(PublishResourceActivity.this);
		mIsh = ImageSelectHelper.getIntance(PublishResourceActivity.this,
				R.id.linearLayoutPicContainer);
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

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwh.showPopDlg(findViewById(R.id.rootLayout),
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								PublishResourceActivity.this.finish();
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

		findViewById(R.id.relativeLayoutchoiceType).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String[] type1 = new String[] {
								getString(R.string.type_gong),
								getString(R.string.type_xu) };
						String[] temp = getResources().getStringArray(
								R.array.array_res_type);
						String[] type2 = new String[temp.length - 1];
						for (int i = 1; i < temp.length; i++) {
							type2[i - 1] = temp[i];
						}
						pwh.showWheelPop(
								findViewById(R.id.imageViewchoiceType), type1,
								type2, new WheelOKClick() {

									@Override
									public String onClick(String[] selected,
											int[] selectedId) {
										((TextView) findViewById(R.id.editTextchoiceType))
												.setText(selected[0]
														+ selected[1]);
										mType = ZhuoCommHelper
												.transferMsgStringToType(
														selected[0],
														PublishResourceActivity.this);
										mCategory = ZhuoCommHelper
												.transferMsgStringToCategory(
														selected[1],
														PublishResourceActivity.this);
										return null;
									}
								});
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
		findViewById(R.id.textViewPosInfo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						((TextView) v).setText("¼ÓÔØÎ»ÖÃÖÐ...");
						if (locationHelper != null) {
							locationHelper.stopLocation();
							locationHelper.startLocation();
						}
					}
				});
	}

	private void publish() {
		EditText titleEditText = (EditText) findViewById(R.id.editTextchoiceTitle);
		String title = titleEditText.getText().toString();
		EditText typeEditText = (EditText) findViewById(R.id.editTextchoiceType);
		String type = typeEditText.getText().toString();
		EditText contentEditText = (EditText) findViewById(R.id.editTextchoiceContent);
		String content = contentEditText.getText().toString();
		String tag = ((TextView) findViewById(R.id.editTextchoiceTag))
				.getText().toString();
		if (title.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info22);
			titleEditText.requestFocus();
			return;
		}
		if (type.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info23);
			return;
		}
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
		mConnHelper.pubZhuoInfo(mIsh.getTags(), mUIHandler, MsgTagVO.PUB_INFO,
				PublishResourceActivity.this, content, tag, mLocation, imgCnt,
				mType, mCategory, title, true, null, null);
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
							public void onClick(View paramView) {
								setResult(RESULT_OK);
								PublishResourceActivity.this.finish();
							}
						};
						pwh.showPopDlgOne(findViewById(R.id.rootLayout),
								listener, R.string.info62);
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
					PublishResourceActivity.this.finish();
				}
			};
			pwh.showPopDlg(findViewById(R.id.rootLayout), listener, null,
					R.string.info69);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
