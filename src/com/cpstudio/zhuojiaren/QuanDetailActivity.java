package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.ui.QuanCreateActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
/**
 * Ȧ������
 * @author lz
 *
 */
public class QuanDetailActivity extends Activity {
	private final static int USER_SELECT = 0;
	private final static int EDIT_GROUP = 1;
	private LoadImage mLoadImage = new LoadImage();
	//��ͬ��ݣ����ܲ�ͬ
	private String memberType = "";
	private PopupWindows pwh = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;
	private QuanFacade mFacade = null;
	private ArrayList<String> tempids = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_detail);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new QuanFacade(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		pwh = new PopupWindows(QuanDetailActivity.this);
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
					QuanVO detail = null;
					if (msg.obj instanceof QuanVO) {
						detail = (QuanVO) msg.obj;
					} else {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						detail = nljh.parseQuan();
						if (null != detail) {
							mFacade.saveOrUpdate(detail);
						}
					}
					if (null != detail) {
						String id = detail.getGroupid();
						((TextView) findViewById(R.id.textViewId)).setText(id);
						String name = detail.getGname();
						((TextView) findViewById(R.id.textViewName))
								.setText(name);
						String headUrl = detail.getGheader();
						ImageView headIv = (ImageView) findViewById(R.id.imageViewGroupHeader);
						headIv.setTag(headUrl);
						mLoadImage.addTask(headUrl, headIv);
						String jj = detail.getGintro();
						((TextView) findViewById(R.id.textViewJJ)).setText(jj);
					}
				}
				break;
			}
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					Button buttonMsgState = (Button) findViewById(R.id.buttonMsgState);
					String alertState = (String) buttonMsgState.getTag();
					if (alertState.equals("1")) {
						buttonMsgState
								.setBackgroundResource(R.drawable.button_switch_off);
						buttonMsgState.setTag("0");
					} else {
						buttonMsgState
								.setBackgroundResource(R.drawable.button_switch_on);
						buttonMsgState.setTag("1");
					}
				}
				break;
			}
			case MsgTagVO.FOLLOW_QUAN: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (isfollow) {
						isfollow = false;
						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
								null, R.string.label_exitSuccess);
						loadData();
					} else {
						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
								null, R.string.label_applysuccess);
					}
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
							null, R.string.label_recommandSuccess);
				}
				break;
			}
			}
		}
	};


	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			QuanVO quan = mFacade.getById(groupid);
			if (quan == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = quan;
				msg.sendToTarget();
			}
		} else {
			String params = ZhuoCommHelper.getUrlGroupDetail() + "?groupid="
					+ groupid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
					QuanDetailActivity.this, true, new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							QuanDetailActivity.this.finish();
						}
					});
		}
	}

//	private OnClickListener chatListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent i = new Intent(QuanDetailActivity.this,
//					QuanBoardChatActivity.class);
//			i.putExtra("groupid", groupid);
//			i.putExtra("type", "chat");
//			startActivity(i);
//		}
//	};

//	private OnClickListener boardListener = new OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			Intent i = new Intent(QuanDetailActivity.this,
//					QuanBoardChatActivity.class);
//			i.putExtra("groupid", groupid);
//			i.putExtra("type", "board");
//			startActivity(i);
//		}
//	};

	private OnClickListener memberListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(QuanDetailActivity.this,
					QuanUsersActivity.class);
			i.putExtra("groupid", groupid);
			startActivity(i);
		}
	};

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuanDetailActivity.this.finish();
			}
		});

		findViewById(R.id.buttonSetting).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						final View parent = findViewById(R.id.scrollViewGroupInfo);
						if (memberType.equals("0")) {
							OnClickListener onClickListener = new OnClickListener() {
								@Override
								public void onClick(View paramView) {
									Intent intent = new Intent(
											QuanDetailActivity.this,
											QuanCreateActivity.class);
									intent.putExtra("groupid", groupid);
									QuanDetailActivity.this
											.startActivity(intent);
								}
							};
							pwh.showBottomPop(parent,
									new OnClickListener[] { onClickListener },
									new int[] { R.string.label_edit }, "manage");
						} else if (memberType.equals("1")
								|| memberType.equals("2")) {
							OnClickListener onClickListener = new OnClickListener() {
								@Override
								public void onClick(View paramView) {
									pwh.showPopDlg(parent,
											new OnClickListener() {

												@Override
												public void onClick(View v) {
													mConnHelper
															.followGroup(
																	groupid,
																	"0",
																	mUIHandler,
																	MsgTagVO.FOLLOW_QUAN,
																	null, true,
																	null, null);
												}
											}, null, R.string.info13);
								}
							};
							pwh.showBottomPop(parent,
									new OnClickListener[] { onClickListener },
									new int[] { R.string.label_exit }, "exit");
						}
					}
				});

//		findViewById(R.id.tabButtonGb).setOnClickListener(boardListener);
//
//		findViewById(R.id.tabButtonQl).setOnClickListener(chatListener);

		findViewById(R.id.tabButtonCy).setOnClickListener(memberListener);

		findViewById(R.id.buttonApply).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						mConnHelper.followGroup(groupid, "1", mUIHandler,
								MsgTagVO.FOLLOW_QUAN, null, true, null, null);
					}
				});
		/**
		 * �Ƽ�������
		 */
		findViewById(R.id.buttonRecommand).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						Intent i = new Intent(QuanDetailActivity.this,
								UserSelectActivity.class);
						i.putStringArrayListExtra("otherids", tempids);
						startActivityForResult(i, USER_SELECT);
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case USER_SELECT:
				ArrayList<String> mSelectlist = data
						.getStringArrayListExtra("ids");
				if (mSelectlist != null && mSelectlist.size() > 0) {
					String useridlist = "";
					for (String id : mSelectlist) {
						useridlist += id + ";";
					}
					useridlist = ZhuoCommHelper.subLast(useridlist);
					mConnHelper.recommandGroup(groupid, useridlist, mUIHandler,
							MsgTagVO.MSG_FOWARD, null, true, null, null);
				}
				break;
			case EDIT_GROUP:
				pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo), null,
						R.string.info60);
				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
