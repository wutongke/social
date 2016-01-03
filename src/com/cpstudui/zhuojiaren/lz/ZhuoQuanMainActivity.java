package com.cpstudui.zhuojiaren.lz;

import io.rong.app.DemoContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Group;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseFragmentActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.GroupFacade;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.QuanziActiveFra;
import com.cpstudio.zhuojiaren.fragment.QuanziMemberFra;
import com.cpstudio.zhuojiaren.fragment.QuanziTopicFra;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MessagePubVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.gtype;
import com.cpstudio.zhuojiaren.ui.EditEventActivity;
import com.cpstudio.zhuojiaren.ui.MyFriendActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.CustomShareBoard;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;

/**
 * 倬家圈子主页 acitivity与fragment通信,当fragment选中我圈子时，设置管理，点击管理后，操作fragment
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
	@InjectView(R.id.mtextViewCy)
	TextView tvMemNum;
	@InjectView(R.id.mtextViewTopic)
	TextView tvTopicNum;
	@InjectView(R.id.mtextViewGType)
	TextView tvTopicType;
	@InjectView(R.id.lt_chengyuan_menue)
	View ltMember;// 成员操作菜单
	@InjectView(R.id.lt_youke_menue)
	View ltYouke;// 非成员操作菜单
	@InjectView(R.id.at_pub)
	AutoTextView antoPubText;// 圈公告
	@InjectView(R.id.btnPubActive)
	View btnPubActive;

	@InjectView(R.id.btnPubTopic)
	View btnPubTopic;
	@InjectView(R.id.btnJoinQuan)
	View btnJoinQuan;
	@InjectView(R.id.activity_function_image)
	ImageView ivFunSimply;
	@InjectView(R.id.ivClose)
	ImageView ivClose;

	@InjectView(R.id.btnQuanChat)
	View btnQuanChat;
	private int currentPager = 0;
	// 四个fragment 方便通信
	List<Fragment> fragments;
	QuanVO detail;
	private PopupWindows phw = null;
	private LoadImage mLoadImage = LoadImage.getInstance();
	// 不同身份，功能不同
	private int role = QuanVO.QUAN_ROLE_NOTMEMBER;
	private PopupWindows pwh = null;
	private String groupid = null, groupName = null, gheadurl = null;
	private String owerName = null, owerId = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;// 是否已经加入该圈子

	// 用于在fragment中获得groupid
	public String getGroupid() {
		return groupid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_quan_main);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_activity_zhuojiaquan_main);
		ivFunSimply.setTag(0);
		ivFunSimply.setImageResource(R.drawable.menu_qht1);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		// mFacade = new QuanFacade(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		pwh = new PopupWindows(ZhuoQuanMainActivity.this);
		initOnClick();
		loadData();
		// 圈子数据在之前版本是所有的数据都在一个请求“getUrlGroupDetail”中，新版本在主界面只获得成员数，话题数等基本信息，
		// 之后的圈话题，圈活动，圈成员在单独的请求中获得。是否需要分页？
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 发布话题、活动成功后刷新
		if (viewPager.getAdapter() != null) {
			fragments.clear();
			viewPager.setAdapter(getPagerAdapter());
			tabButton.setViewPager(null);
			tabButton.setViewPager(viewPager);
			tabButton.setVisibility(View.VISIBLE);
			viewPager.setCurrentItem(currentPager);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (msg.obj != null && !msg.obj.equals("")) {
					detail = null;
					if (msg.obj instanceof QuanVO) {
						detail = (QuanVO) msg.obj;
					} else {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						detail = nljh.parseQuan();
						if (null != detail) {
							GroupFacade mgfcade = DemoContext.getInstance(
									getApplicationContext()).getmGroupInfoDao();
							mgfcade.saveOrUpdate(detail);
						}
					}
					if (null != detail) {
						role = detail.getRole();
						String name = detail.getGname();
						groupName = name;
						tvName.setText(name);
						gheadurl = detail.getGheader();
						ivGroupHeader.setTag(gheadurl);
						owerId = detail.getUserid();
						owerName = detail.getName();
						mLoadImage.addTask(gheadurl, ivGroupHeader);
						String gType = "未知";
						int type = detail.getGtype();
						List<gtype> types = mConnHelper.getBaseDataSet()
								.getGtype();
						if (type >= 1 && types != null && type <= types.size())
							gType = types.get(type - 1).getContent();
						// 圈子类型，之后要转换为编号对应的名称

						tvTopicType.setText(gType);
						tvMemNum.setText(detail.getMemberCount() + "");
						tvTopicNum.setText(detail.getTopicCount() + "");
						if (role >= QuanVO.QUAN_ROLE_MEMBER) {
							isfollow = true;
						} else {
							isfollow = false;
						}
						changeType(isfollow);
						mLoadImage
								.beginLoad(detail.getGheader(), ivGroupHeader);
						if (!TextUtils.isEmpty(detail.getGpub())) {
							List<MessagePubVO> noticesListData = new ArrayList<MessagePubVO>();
							MessagePubVO pub = new MessagePubVO();
							pub.setPublish(detail.getGpub());
							noticesListData.add(pub);
							antoPubText.setList(noticesListData);
							antoPubText.updateUI();
							findViewById(R.id.linearLayoutBroadcast)
									.setVisibility(View.VISIBLE);
						}
						if(fragments!=null)
							fragments.clear();
						viewPager.setAdapter(getPagerAdapter());
						tabButton.setViewPager(null);
						tabButton.setViewPager(viewPager);
						tabButton.setVisibility(View.VISIBLE);
						viewPager.setCurrentItem(currentPager);
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
					int type = 0;
					if (isfollow) {
						pwh.showPopTip(findViewById(R.id.root), null,
								R.string.label_exitSuccess);
						type = 0;
					} else {
						pwh.showPopTip(findViewById(R.id.root), null,
								R.string.label_applysuccess);
						type = 1;
					}
					isfollow = !isfollow;
					mConnHelper.followQuan(
							ZhuoQuanMainActivity.this,
							owerId,
							new Group(groupid, groupName, android.net.Uri
									.parse(gheadurl)), type);
					loadData();
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					pwh.showPopTip(findViewById(R.id.root), null,
							R.string.label_recommandSuccess);
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getQuanInfo(mUIHandler, MsgTagVO.DATA_LOAD, groupid);
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
				currentPager = arg0;
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
		ivFunSimply.setOnClickListener(new OnClickListener() {

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
						CustomShareBoard cb = new CustomShareBoard(
								ZhuoQuanMainActivity.this);
						cb.showCustomShareContent();
					}
				};

				OnClickListener inviteListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(ZhuoQuanMainActivity.this,
								MyFriendActivity.class);
						startActivity(i);
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
				i.putExtra("groupid", groupid);
				startActivity(i);
			}
		});
		btnPubActive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ZhuoQuanMainActivity.this,
						EditEventActivity.class);
				i.putExtra("groupid", groupid);
				startActivity(i);
			}
		});
		btnQuanChat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//进入圈聊界面
				RongIM.getInstance().startGroupChat(ZhuoQuanMainActivity.this,
						groupid, groupName);
			}
		});
		btnJoinQuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConnHelper.followGroup(mUIHandler, MsgTagVO.FOLLOW_QUAN,
						groupid, QuanVO.QUAN_JOIN, null, "");
			}
		});
		ivClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				findViewById(R.id.linearLayoutBroadcast).setVisibility(
						View.GONE);
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
		bundle.putInt(QuanVO.QUANROLE, role);
		fragment.setArguments(bundle);
		return fragment;
	}

	private void setFunctionText(int arg0) {
		switch (arg0) {
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
