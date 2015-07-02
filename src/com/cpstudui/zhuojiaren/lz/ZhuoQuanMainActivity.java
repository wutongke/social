package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.QuanBoardChatActivity;
import com.cpstudio.zhuojiaren.QuanDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserSelectActivity;
import com.cpstudio.zhuojiaren.adapter.ActiveListAdapter;
import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.QuanziActiveFra;
import com.cpstudio.zhuojiaren.fragment.QuanziMemberFra;
import com.cpstudio.zhuojiaren.fragment.QuanziTopicFra;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.EditEventActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;

/**
 * 倬圈主页 acitivity与fragment通信,当fragment选中我圈子时，设置管理，点击管理后，操作fragment
 * 
 * @author lz
 * 
 */
public class ZhuoQuanMainActivity extends BaseFragmentActivity {
	@InjectView(R.id.azq_tab)
	TabButton tabButton;
	@InjectView(R.id.azq_viewpager)
	ViewPager viewPager;
	@InjectView(R.id.imageViewGroupHeader)
	ImageView ivGroupHeader;
	@InjectView(R.id.textViewName)
	TextView tvName;
	@InjectView(R.id.textViewCy)
	TextView tvMemNum;
	@InjectView(R.id.textViewTopic)
	TextView tvTopicNum;
	@InjectView(R.id.lt_chengyuan_menue)
	View ltMember;// 成员操作菜单
	@InjectView(R.id.lt_youke_menue)
	View ltYouke;// 非成员操作菜单

	@InjectView(R.id.btnPubActive)
	Button btnPubActive;

	@InjectView(R.id.btnPubTopic)
	Button btnPubTopic;
	@InjectView(R.id.btnJoinQuan)
	Button btnJoinQuan;

	@InjectView(R.id.btnQuanChat)
	Button btnQuanChat;
	private final static int USER_SELECT = 0;
	private Context mContext;
	// 四个fragment 方便通信
	List<Fragment> fragments;

	private PopupWindows phw = null;
	private LoadImage mLoadImage = new LoadImage();
	// 不同身份，功能不同
	private String memberType = "";
	private PopupWindows pwh = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;// 是否已经加入该圈子
	private QuanFacade mFacade = null;
	private ArrayList<String> tempids = new ArrayList<String>();

	// 用于在fragment中获得groupid
	public String getGroupid() {
		return groupid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_quan_main);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.title_activity_zhuojiaquan_main);
		function.setTag(0);
		function.setBackgroundResource(R.drawable.ico_about);
		// 初始化tab和viewpager
		viewPager.setAdapter(getPagerAdapter());

		tabButton.setViewPager(viewPager);

		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new QuanFacade(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		pwh = new PopupWindows(ZhuoQuanMainActivity.this);
		loadData();
		initOnClick();

		// 圈子数据在之前版本是所有的数据都在一个请求“getUrlGroupDetail”中，新版本在主界面只获得成员数，话题数等基本信息，
		// 之后的圈话题，圈活动，圈成员在单独的请求中获得。是否需要分页？
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
						// String id = detail.getGroupid();
						memberType = detail.getMembertype();
						// ((TextView)
						// findViewById(R.id.textViewId)).setText(id);
						String name = detail.getGname();
						tvName.setText(name);
						String headUrl = detail.getGheader();
						ivGroupHeader.setTag(headUrl);
						mLoadImage.addTask(headUrl, ivGroupHeader);
						// 圈子简介内容
						// String jj = detail.getGintro();
						// ((TextView)
						// findViewById(R.id.textViewJJ)).setText(jj);
						String memberNum = detail.getMembersnum();
						String memberAll = detail.getMembersmax();
						if (memberNum == null || memberNum.equals("")) {
							memberNum = "0";
						}
						if (memberAll == null || memberAll.equals("")) {
							memberAll = "0";
						}
						tvMemNum.setText(memberNum + "人");
						// ((TextView) findViewById(R.id.textViewCy))
						// .setText(memberNum + "/" + memberAll);
						if (memberType != null && !memberType.equals("3")) {
							isfollow = true;
							// ((TextView) findViewById(R.id.textViewDate))
							// .setText(detail.getLastmsgtime());
							// Button buttonMsgState = (Button)
							// findViewById(R.id.buttonMsgState);
							// String alertState = detail.getAlert();
							// buttonMsgState.setTag(alertState);
							// //是否圈聊消息提醒
							// if (alertState.equals("1")) {
							// buttonMsgState
							// .setBackgroundResource(R.drawable.button_switch_on);
							// } else {
							// buttonMsgState
							// .setBackgroundResource(R.drawable.button_switch_off);
							// }
							// buttonMsgState
							// .setOnClickListener(new OnClickListener() {
							// @Override
							// public void onClick(View v) {
							// if (v.getTag().equals("1")) {
							// mConnHelper.groupAlert(groupid,
							// "0", mUIHandler,
							// MsgTagVO.PUB_INFO,
							// null, true, null, null);
							// } else {
							// mConnHelper.groupAlert(groupid,
							// "1", mUIHandler,
							// MsgTagVO.PUB_INFO,
							// null, true, null, null);
							// }
							// }
							// });
							// ((TextView) findViewById(R.id.textViewGb))
							// .setText("        "
							// + detail.getLastbroadcast());
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
							// ImageView cjIV = (ImageView)
							// findViewById(R.id.imageViewCj);
							// cjIV.setTag(createrUrl);
							// mLoadImage.addTask(createrUrl, cjIV);
							// cjIV.setOnClickListener(new OnClickListener() {
							// @Override
							// public void onClick(View v) {
							// Intent i = new Intent(
							// QuanDetailActivity.this,
							// UserCardActivity.class);
							// i.putExtra("userid", createrId);
							// startActivity(i);
							// }
							// });
							// }
							List<UserVO> managers = detail.getManagers();
							int num = managers.size();
							// ((TextView)
							// findViewById(R.id.textViewGl)).setText(num
							// + "");
							// LinearLayout ll = (LinearLayout)
							// findViewById(R.id.linearLayoutGl);
							// int height = ll.getLayoutParams().height;
							// LayoutParams llp = new LayoutParams(height,
							// height);
							// llp.rightMargin = 5;
							// RelativeLayout.LayoutParams rlp = new
							// RelativeLayout.LayoutParams(
							// LayoutParams.MATCH_PARENT,
							// LayoutParams.MATCH_PARENT);
							for (int i = 0; i < num; i++) {
								// String managerUrl =
								// managers.get(i).getUheader();
								final String managerId = managers.get(i)
										.getUserid();
								tempids.add(managerId);
								// RelativeLayout rl = new RelativeLayout(
								// QuanDetailActivity.this);
								// rl.setLayoutParams(llp);
								// ImageView iv = new ImageView(
								// QuanDetailActivity.this);
								// iv.setLayoutParams(rlp);
								// rl.addView(iv);
								// ll.addView(rl);
								// iv.setTag(managerUrl);
								// mLoadImage.addTask(managerUrl, iv);
								// iv.setOnClickListener(new OnClickListener() {
								// @Override
								// public void onClick(View v) {
								// Intent i = new Intent(
								// QuanDetailActivity.this,
								// UserCardActivity.class);
								// i.putExtra("userid", managerId);
								// startActivity(i);
								// }
								// });
								// }
								// mLoadImage.doTask();
							}
						}
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
					ZhuoQuanMainActivity.this, true, new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							ZhuoQuanMainActivity.this.finish();
						}
					});
		}
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		// 选择不同的fragment，function按键不同
		tabButton.setPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				setFunctionText(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (phw == null)
					phw = new PopupWindows(ZhuoQuanMainActivity.this);

				OnClickListener briefListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(ZhuoQuanMainActivity.this,
								QuanBriefActivity.class);
						i.putExtra("groupid", groupid);
						startActivity(i);
					}
				};

				OnClickListener shareListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 通过第三方软件分享，QQ，微信等
					}
				};

				OnClickListener inviteListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(ZhuoQuanMainActivity.this,
								UserSelectActivity.class);
						i.putStringArrayListExtra("otherids", tempids);
						startActivityForResult(i, USER_SELECT);
					}
				};
				// 需要另外设置菜单选项布局及响应事件
				phw.showQuanOptionsMenue(v, 2, briefListener, shareListener,
						inviteListener);
			}
		});
		btnPubTopic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ZhuoQuanMainActivity.this,
						PubTopicActicvity.class);
				startActivity(i);
			}
		});
		btnPubActive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ZhuoQuanMainActivity.this,
						EditEventActivity.class);
				startActivity(i);
			}
		});
		btnQuanChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 进入圈聊界面
				 Intent i = new Intent(ZhuoQuanMainActivity.this,
				 QuanBoardChatActivity.class);
				 i.putExtra("groupid", groupid);
					
				 startActivity(i);
			}
		});
		btnJoinQuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				mConnHelper.followGroup(groupid, "1", mUIHandler,
						MsgTagVO.FOLLOW_QUAN, null, true, null, null);

			}
		});
	}

	protected void onPause() {
		super.onPause();
		viewPager.setCurrentItem(0, false);
		tabButton.setTabBackgroundByIndex(0);
	};

	PagerAdapter getPagerAdapter() {
		fragments = new ArrayList<Fragment>();
		List<CharSequence> titles = new ArrayList<CharSequence>();
		String quanTopicTitle = getString(R.string.quanzi_topic);
		String quanEventTitle = getString(R.string.quanzi_event);
		String quanMemberTitle = getString(R.string.quanzi_member);

		Fragment quanTopic = addBundle(new QuanziTopicFra(), QuanVO.QUANZITOPIC);
		fragments.add(quanTopic);
		titles.add(quanTopicTitle);

		Fragment quanEvent = addBundle(new QuanziActiveFra(),
				QuanVO.QUANZIEVENT);
		fragments.add(quanEvent);
		titles.add(quanEventTitle);

		Fragment quanMember = addBundle(new QuanziMemberFra(),
				QuanVO.QUANZIMEMBER);
		fragments.add(quanMember);
		titles.add(quanMemberTitle);
		return new ActivePagerAdapter(getSupportFragmentManager(), fragments,
				titles);
	}

	protected Fragment addBundle(Fragment fragment, int catlog) {
		Bundle bundle = new Bundle();
		bundle.putInt(QuanVO.QUANZIMAINTYPE, catlog);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void setFunctionText(int arg0) {
		switch (arg0) {
		// case 0:
		// function.setText("管理");
		// function.setTag(0);
		// break;
		// case 1:
		// function.setTag(1);
		// ImageSpan span = new ImageSpan(mContext, R.drawable.tab_good);
		// SpannableString spanStr = new SpannableString(" ");
		// spanStr.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		// function.setText(spanStr);
		// break;
		// case 2:
		// function.setText("筛选");
		// function.setTag(2);
		// break;
		// case 3:
		// function.setText("");
		// function.setTag(3);
		// break;
		}
	}

	private void changeType(boolean isMember) {

		if (isMember) {
			ltMember.setVisibility(View.VISIBLE);
			ltYouke.setVisibility(View.GONE);
		} else {
			ltMember.setVisibility(View.GONE);
			ltYouke.setVisibility(View.VISIBLE);
		}
	}
			
}
