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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class ZenghuiActivity extends Activity {
	private final static int USER_SELECT = 0;
	private LoadImage mLoadImage = new LoadImage();
	private PopupWindows pwh = null;
	private String isCollect = "0";
	private String msgid = null;
	private ZhuoConnHelper mConnHelper = null;
	private TextView mCollectBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zenghui);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");
		pwh = new PopupWindows(ZenghuiActivity.this);
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
					((TextView) findViewById(R.id.textViewTitle)).setText(item
							.getTitle());
					((TextView) findViewById(R.id.textViewDetail)).setText(item
							.getText());
					((TextView) findViewById(R.id.textViewTime))
							.setText(CommonUtil.calcTime(item.getAddtime()));
					((TextView) findViewById(R.id.textViewUser)).setText(item
							.getUser().getUsername());
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
									ZenghuiActivity.this);
							rl.setLayoutParams(lllp);
							rl.setTag(pic.getOrgurl());
							ll.addView(rl);
							ImageView iv = new ImageView(ZenghuiActivity.this);
							iv.setLayoutParams(rllp);
							rl.addView(iv);
							String url = pic.getUrl();
							iv.setTag(url);
							rl.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									Intent intent = new Intent(
											ZenghuiActivity.this,
											PhotoViewMultiActivity.class);
									ArrayList<String> orgs = new ArrayList<String>();
									for (int j = 0; j < pics.size(); j++) {
										orgs.add(pics.get(j).getOrgurl());
									}
									intent.putStringArrayListExtra("pics", orgs);
									intent.putExtra("pic", (String) v.getTag());
									startActivity(intent);
								}
							});
							mLoadImage.addTask(url, iv);
						}
						mLoadImage.doTask();
					}
					isCollect = item.getIscollect();
				}
				break;
			}
			case MsgTagVO.MSG_LIKE: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_zanSuccess);
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {

				}
				break;
			}
			case MsgTagVO.MSG_COLLECT: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (isCollect != null && isCollect.equals("0")) {
						Drawable drawable = getResources().getDrawable(
								R.drawable.tab_collect_on);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						mCollectBtn.setCompoundDrawables(null, drawable, null,
								null);
						mCollectBtn.setText(R.string.label_collectCancel);
						isCollect = "1";
						pwh.showPopTip(findViewById(R.id.scrollView), null,
								R.string.label_collectSuccess);
					} else {
						Drawable drawable = getResources().getDrawable(
								R.drawable.tab_collect_off);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						mCollectBtn.setCompoundDrawables(null, drawable, null,
								null);
						mCollectBtn.setText(R.string.label_collect);
						isCollect = "0";
						pwh.showPopTip(findViewById(R.id.scrollView), null,
								R.string.label_cancelCollect);
					}

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
				ZenghuiActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						ZenghuiActivity.this.finish();
					}
				});
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ZenghuiActivity.this.finish();
				// overridePendingTransition(R.anim.activity_left_in,
				// R.anim.activity_right_out);
			}
		});

		findViewById(R.id.buttonSetting).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						OnClickListener zanClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								mConnHelper.goodMsg(msgid, mUIHandler,
										MsgTagVO.MSG_LIKE,
										ZenghuiActivity.this, true, null, null);
							}
						};
						OnClickListener cmtClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(ZenghuiActivity.this,
										MsgShareActivity.class);
								i.putExtra("msgid", msgid);
								i.putExtra("parentid", msgid);
								startActivity(i);
							}
						};
						OnClickListener recommandClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(ZenghuiActivity.this,
										UserSelectActivity.class);
								startActivityForResult(i, USER_SELECT);
							}
						};
						OnClickListener collectClickListener = new OnClickListener() {

							@Override
							public void onClick(View v) {
								mCollectBtn = (TextView) v;
								if (isCollect == null || isCollect.equals("0")) {
									mConnHelper.collectMsg(msgid, "1",
											mUIHandler, MsgTagVO.MSG_COLLECT,
											null, true, null, null);
								} else {
									mConnHelper.collectMsg(msgid, "0",
											mUIHandler, MsgTagVO.MSG_COLLECT,
											null, true, null, null);
								}
							}
						};
						if (isCollect == null || isCollect.equals("0")) {
							pwh.showBottomOption(findViewById(R.id.scrollView),
									zanClickListener, cmtClickListener,
									recommandClickListener,
									collectClickListener, false);
						} else {
							pwh.showBottomOption(findViewById(R.id.scrollView),
									zanClickListener, cmtClickListener,
									recommandClickListener,
									collectClickListener, true);
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == USER_SELECT) {
				ArrayList<String> ids = data.getStringArrayListExtra("ids");
				String useridlist = "";
				if (ids != null && ids.size() > 0) {
					for (String id : ids) {
						useridlist += id + ";";
					}
					useridlist = useridlist.substring(0,
							useridlist.length() - 1);
					mConnHelper.recommandMsg(msgid, useridlist, mUIHandler,
							MsgTagVO.MSG_FOWARD, null, true, null, null);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
