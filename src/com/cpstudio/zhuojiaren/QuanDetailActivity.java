package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;

public class QuanDetailActivity extends Activity {
	private final static int USER_SELECT = 0;
	private final static int EDIT_GROUP = 1;
	private LoadImage mLoadImage = new LoadImage();
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
						memberType = detail.getMembertype();
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
						String memberNum = detail.getMembersnum();
						String memberAll = detail.getMembersmax();
						if (memberNum == null || memberNum.equals("")) {
							memberNum = "0";
						}
						if (memberAll == null || memberAll.equals("")) {
							memberAll = "0";
						}
						((TextView) findViewById(R.id.textViewCy))
								.setText(memberNum + "/" + memberAll);
						if (memberType != null && !memberType.equals("3")) {
							isfollow = true;
							((TextView) findViewById(R.id.textViewDate))
									.setText(detail.getLastmsgtime());
							Button buttonMsgState = (Button) findViewById(R.id.buttonMsgState);
							String alertState = detail.getAlert();
							buttonMsgState.setTag(alertState);
							if (alertState.equals("1")) {
								buttonMsgState
										.setBackgroundResource(R.drawable.button_switch_on);
							} else {
								buttonMsgState
										.setBackgroundResource(R.drawable.button_switch_off);
							}
							buttonMsgState
									.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											if (v.getTag().equals("1")) {
												mConnHelper.groupAlert(groupid,
														"0", mUIHandler,
														MsgTagVO.PUB_INFO,
														null, true, null, null);
											} else {
												mConnHelper.groupAlert(groupid,
														"1", mUIHandler,
														MsgTagVO.PUB_INFO,
														null, true, null, null);
											}
										}
									});
							((TextView) findViewById(R.id.textViewGb))
									.setText("        "
											+ detail.getLastbroadcast());
						} else {
							isfollow = false;
						}
						changeType(isfollow);
						UserVO founder = detail.getFounder();
						if (founder != null) {
							String createrUrl = founder.getUheader();
							final String createrId = detail.getFounder()
									.getUserid();
							tempids.add(createrId);
							ImageView cjIV = (ImageView) findViewById(R.id.imageViewCj);
							cjIV.setTag(createrUrl);
							mLoadImage.addTask(createrUrl, cjIV);
							cjIV.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent i = new Intent(
											QuanDetailActivity.this,
											UserCardActivity.class);
									i.putExtra("userid", createrId);
									startActivity(i);
								}
							});
						}
						List<UserVO> managers = detail.getManagers();
						int num = managers.size();
						((TextView) findViewById(R.id.textViewGl)).setText(num
								+ "");
						LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutGl);
						int height = ll.getLayoutParams().height;
						LayoutParams llp = new LayoutParams(height, height);
						llp.rightMargin = 5;
						RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT);
						for (int i = 0; i < num; i++) {
							String managerUrl = managers.get(i).getUheader();
							final String managerId = managers.get(i)
									.getUserid();
							tempids.add(managerId);
							RelativeLayout rl = new RelativeLayout(
									QuanDetailActivity.this);
							rl.setLayoutParams(llp);
							ImageView iv = new ImageView(
									QuanDetailActivity.this);
							iv.setLayoutParams(rlp);
							rl.addView(iv);
							ll.addView(rl);
							iv.setTag(managerUrl);
							mLoadImage.addTask(managerUrl, iv);
							iv.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									Intent i = new Intent(
											QuanDetailActivity.this,
											UserCardActivity.class);
									i.putExtra("userid", managerId);
									startActivity(i);
								}
							});
						}
						mLoadImage.doTask();
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

	private void changeType(boolean type) {
		int viewType = View.GONE;
		int viewTypeNot = View.VISIBLE;
		View llgb = findViewById(R.id.linearLayoutGuangbo);
		View rlql = findViewById(R.id.relativeLayoutQuanLiao);
		View rlcy = findViewById(R.id.relativeLayoutMembers);
		if (type) {
			viewType = View.VISIBLE;
			viewTypeNot = View.GONE;
			llgb.setOnClickListener(boardListener);
			rlql.setOnClickListener(chatListener);
			rlcy.setOnClickListener(memberListener);
		} else {
			rlcy.setOnClickListener(null);
		}
		findViewById(R.id.borderQuanLiao).setVisibility(viewType);
		findViewById(R.id.linearLayoutGroupTab).setVisibility(viewType);
		findViewById(R.id.borderMsg).setVisibility(viewType);
		findViewById(R.id.buttonRecommand).setVisibility(viewType);
		findViewById(R.id.buttonSetting).setVisibility(viewType);
		findViewById(R.id.relativeLayoutMsg).setVisibility(viewType);
		findViewById(R.id.imageViewCyMore).setVisibility(viewType);
		findViewById(R.id.borderGuangbo).setVisibility(viewType);
		findViewById(R.id.buttonApply).setVisibility(viewTypeNot);
		llgb.setVisibility(viewType);
		rlql.setVisibility(viewType);
	}

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

	private OnClickListener chatListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(QuanDetailActivity.this,
					QuanBoardChatActivity.class);
			i.putExtra("groupid", groupid);
			i.putExtra("type", "chat");
			startActivity(i);
		}
	};

	private OnClickListener boardListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(QuanDetailActivity.this,
					QuanBoardChatActivity.class);
			i.putExtra("groupid", groupid);
			i.putExtra("type", "board");
			startActivity(i);
		}
	};

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

		findViewById(R.id.tabButtonGb).setOnClickListener(boardListener);

		findViewById(R.id.tabButtonQl).setOnClickListener(chatListener);

		findViewById(R.id.tabButtonCy).setOnClickListener(memberListener);

		findViewById(R.id.buttonApply).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						mConnHelper.followGroup(groupid, "1", mUIHandler,
								MsgTagVO.FOLLOW_QUAN, null, true, null, null);
					}
				});

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
