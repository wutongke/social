package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

public class UplevelDetailActivity extends Activity {
	private LoadImage mLoadImage = new LoadImage();
	private PopupWindows pwh = null;
	private String msgid = null;
	private ZhuoConnHelper mConnHelper = null;
	private String cmtnum = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uplevel_detail);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");
		pwh = new PopupWindows(UplevelDetailActivity.this);
		loadData();
		initClick();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					ZhuoInfoVO item = nljh.parseZhuoInfo();
					if (item != null) {
						((TextView) findViewById(R.id.textViewTitle))
								.setText(item.getTitle());
						((TextView) findViewById(R.id.textViewDetail))
								.setText(item.getText());
						((TextView) findViewById(R.id.textViewTime))
								.setText(CommonUtil.calcTime(item.getAddtime()));
						cmtnum = item.getCmtnum();
						((TextView) findViewById(R.id.textViewUser))
								.setText(cmtnum + getString(R.string.label_cmt));
						((TextView) findViewById(R.id.buttonCmt))
								.setText(cmtnum);
						LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutPicContainer);
						LayoutParams lllp = new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
						RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						rllp.addRule(RelativeLayout.CENTER_HORIZONTAL);
						final List<PicVO> pics = item.getPic();
						if (pics != null && pics.size() > 0) {
							for (PicVO pic : pics) {
								RelativeLayout rl = new RelativeLayout(
										UplevelDetailActivity.this);
								rl.setLayoutParams(lllp);
								rl.setTag(pic.getOrgurl());
								ll.addView(rl);
								ImageView iv = new ImageView(
										UplevelDetailActivity.this);
								iv.setLayoutParams(rllp);
								rl.addView(iv);
								String url = pic.getUrl();
								iv.setTag(url);
								rl.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												UplevelDetailActivity.this,
												PhotoViewMultiActivity.class);
										ArrayList<String> orgs = new ArrayList<String>();
										for (int j = 0; j < pics.size(); j++) {
											orgs.add(pics.get(j).getOrgurl());
										}
										intent.putStringArrayListExtra("pics",
												orgs);
										intent.putExtra("pic",
												(String) v.getTag());
										startActivity(intent);
									}
								});
								mLoadImage.addTask(url, iv);
							}
							mLoadImage.doTask();
						}
					}
				}
				break;
			}
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj)) {
					((EditText) findViewById(R.id.editTextCmt)).setText("");
					pwh.showPopDlgOne(findViewById(R.id.scrollView), null,
							R.string.info62);
					try {
						int cmts = Integer.valueOf(cmtnum) + 1;
						((TextView) findViewById(R.id.textViewUser))
								.setText(cmts + getString(R.string.label_cmt));
						((TextView) findViewById(R.id.buttonCmt)).setText(cmts
								+ "");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					pwh.showPopDlgOne(findViewById(R.id.scrollView), null,
							R.string.error5);
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		String params = ZhuoCommHelper.getUrlMsgDetail();
		params += "?msgid=" + msgid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
				UplevelDetailActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						UplevelDetailActivity.this.finish();
					}
				});
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UplevelDetailActivity.this.finish();
				// overridePendingTransition(R.anim.activity_left_in,
				// R.anim.activity_right_out);
			}
		});

		findViewById(R.id.editTextCmt).setOnFocusChangeListener(
				new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (hasFocus) {
							findViewById(R.id.buttonCmt).setVisibility(
									View.INVISIBLE);
							findViewById(R.id.buttonPub).setVisibility(
									View.VISIBLE);
							findViewById(R.id.viewHidden).setVisibility(
									View.VISIBLE);
						} else {
							findViewById(R.id.buttonCmt).setVisibility(
									View.VISIBLE);
							findViewById(R.id.buttonPub).setVisibility(
									View.INVISIBLE);
							findViewById(R.id.viewHidden).setVisibility(
									View.INVISIBLE);
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						}
					}
				});

		findViewById(R.id.buttonCmt).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UplevelDetailActivity.this,
						CmtListActivity.class);
				intent.putExtra("msgid", msgid);
				startActivity(intent);
				// overridePendingTransition(R.anim.activity_right_in,
				// R.anim.activity_left_out);
			}
		});

		findViewById(R.id.buttonPub).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText edit = (EditText) findViewById(R.id.editTextCmt);
				String content = edit.getText().toString();
				if (content.trim().equals("")) {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.info65);
					edit.requestFocus();
					return;
				}
				String forward = "0";
				edit.clearFocus();
				mConnHelper.pubCmt(msgid, msgid, content, forward, mUIHandler,
						MsgTagVO.PUB_INFO, UplevelDetailActivity.this, true,
						null, null);

			}
		});
		findViewById(R.id.viewHidden).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditText edit = (EditText) findViewById(R.id.editTextCmt);
				edit.clearFocus();
			}
		});

	}

}
