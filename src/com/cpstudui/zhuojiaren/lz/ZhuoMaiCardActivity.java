package com.cpstudui.zhuojiaren.lz;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.message.ContactNotificationMessage;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.CardEditActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.fragment.ActivePagerAdapter;
import com.cpstudio.zhuojiaren.fragment.ZhuomaiActiveInfoFra;
import com.cpstudio.zhuojiaren.fragment.ZhuomaiCardCommercyInfoFra;
import com.cpstudio.zhuojiaren.fragment.ZhuomaiMoreInfoFra;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.Praise;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.ui.PhoneActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.cpstudio.zhuojiaren.widget.TabButton.TabsButtonOnClickListener;

/**
 * 倬脉名片
 * 
 * @author lz
 * 
 */
public class ZhuoMaiCardActivity extends FragmentActivity {
	@InjectView(R.id.activity_back)
	TextView tvBack;
	@InjectView(R.id.activity_title)
	TextView tvTitle;
	@InjectView(R.id.activity_function)
	ImageView ivShare;
	@InjectView(R.id.activity_function2)
	ImageView ivZan;

	@InjectView(R.id.azq_tab)
	TabButton tabButton;
	@InjectView(R.id.azq_viewpager)
	ViewPager viewPager;
	@InjectView(R.id.imageViewHeader)
	ImageView ivHeader;
	@InjectView(R.id.textViewName)
	TextView tvName;
	@InjectView(R.id.textViewMemType)
	TextView tvMemType;

	@InjectView(R.id.textViewPosition)
	TextView tvPosition;
	@InjectView(R.id.textViewPhone)
	TextView tvPhone;
	@InjectView(R.id.textViewPurse)
	TextView tvZBNum;// 倬币数
	@InjectView(R.id.textViewht)
	TextView tvCompany;// 倬币数

	@InjectView(R.id.lt_myself_menue)
	View ltNyselfMenue;// 个人资料编辑菜单
	@InjectView(R.id.lt_other_menue)
	View ltOtherMenue;// 个人资料编辑菜单

	@InjectView(R.id.btnEditBG)
	Button btnEditBG;//
	@InjectView(R.id.btnEditCard)
	Button btnEditCard;

	@InjectView(R.id.btnSendCard)
	Button btnSendCard;//
	@InjectView(R.id.btnChat)
	Button btnChat;
	@InjectView(R.id.rootmain)
	View rootMainBG;//

	private final static int USER_SELECT = 0;
	private Context mContext;
	List<Fragment> fragments;

	private PopupWindows phw = null;
	private LoadImage mLoadImage = new LoadImage();
	// 不同身份，功能不同
	private String memberType = "";
	private PopupWindows pwh = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;// 是否已经加入该圈子

	private ArrayList<String> tempids = new ArrayList<String>();

	String userid, myid, ismy;
	private UserFacade userFacade = null;
	private CardMsgFacade mFacade = null;
	UserNewVO userInfo;
	BaseCodeData baseDataSet;

	// 用于在fragment中获得groupid
	public String getGroupid() {
		return groupid;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_card_main);
		ButterKnife.inject(this);
		mContext = this;
		tvTitle.setText(R.string.title_zhuomai_card);

		// 初始化tab和viewpager
		viewPager.setAdapter(getPagerAdapter());

		tabButton.setViewPager(viewPager);

		userFacade = new UserFacade(ZhuoMaiCardActivity.this);
		mFacade = new CardMsgFacade(ZhuoMaiCardActivity.this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(ZhuoMaiCardActivity.this);
		Intent i = getIntent();
		userid = i.getStringExtra("userid");
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		if (userid.equals(myid)) {
			btnEditBG.setEnabled(true);
		} else
			btnEditBG.setEnabled(false);
		mLoadImage = new LoadImage();

		// 设置个性背景图片，在个人信息里。个人可以选择更换
		rootMainBG.setBackgroundResource(R.drawable.manbg_zmmp_1);
		baseDataSet = mConnHelper.getBaseDataSet();
		initOnClick();
		loadData();
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			// UserNewVO quan = userFacade.getById(userid);
			// if (quan == null) {
			// CommonUtil.displayToast(getApplicationContext(),
			// R.string.error0);
			// } else {
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			// msg.obj = quan;
			// msg.sendToTarget();
			// }
		} else {
			mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD, userid);
		}
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		// 选择不同的fragment，function按键不同
		tvTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ZhuoMaiCardActivity.this.finish();
			}
		});
		tabButton.setPageChangeListener(new PageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

				if (arg0 == 2) {
					// 不展示第三个page
					viewPager.setCurrentItem(1);
					mContext.startActivity(new Intent(mContext,
							CardEditActivity.class));
					// 跳转到供需发布页面
				}
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

		tabButton.setTabsButtonOnClickListener(new TabsButtonOnClickListener() {

			@Override
			public void tabsButtonOnClick(int id, View v) {
				// TODO Auto-generated method stub
				if ((Integer) (v.getTag()) == 2)
					mContext.startActivity(new Intent(mContext,
							CardEditActivity.class));
				else {
					viewPager.setCurrentItem((Integer) v.getTag());
				}
			}
		});
		ivZan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 赞名片

				mConnHelper.praiseCard(mUIHandler, MsgTagVO.MSG_LIKE, myid, 1);
				// MsgTagVO.MSG_LIKE, null, true, null, null);
			}
		});
		ivShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 分享，具体呢？
				// if (phw == null)
				// phw = new PopupWindows(ZhuoMaiCardActivity.this);
				//
				// OnClickListener briefListener = new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// Intent i = new Intent(ZhuoMaiCardActivity.this,
				// QuanBriefActivity.class);
				// i.putExtra("groupid", groupid);
				// startActivity(i);
				// }
				// };
				//
				// OnClickListener shareListener = new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// // 通过第三方软件分享，QQ，微信等
				// }
				// };
				//
				// OnClickListener inviteListener = new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// Intent i = new Intent(ZhuoMaiCardActivity.this,
				// UserSelectActivity.class);
				// i.putStringArrayListExtra("otherids", tempids);
				// startActivityForResult(i, USER_SELECT);
				// }
				// };
				// // 需要另外设置菜单选项布局及响应事件
				// phw.showQuanOptionsMenue(v, 2, briefListener, shareListener,
				// inviteListener);
			}
		});
		btnEditBG.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (userInfo.getRelation() == UserNewVO.USER_RELATION.RELATION_MYSELF
						.ordinal()) {
					Intent i = new Intent(ZhuoMaiCardActivity.this,
							ChangeBackgroundActivity.class);
					startActivity(i);
				}
			}
		});
		btnEditCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ZhuoMaiCardActivity.this,
						CardEditActivity.class);
				startActivity(i);
			}
		});
		btnSendCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int r = userInfo.getRelation();
				if (r == UserNewVO.USER_RELATION.RELATION_MYSELF.ordinal()
						|| userInfo.getUserid().equals(myid))
					CommonUtil.displayToast(ZhuoMaiCardActivity.this,
							"已是好友，不需要再发送名片");
				else {
					sendCard();
				}
			}
		});
		btnChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int r = userInfo.getRelation();
				if (r == UserNewVO.USER_RELATION.RELATION_STRANGER.ordinal()) {
					// 点击陌生人想与他聊天时，要提示，你们不是好友，是否递送名片，使他成为你的好友
				}
				RongIM.getInstance().startPrivateChat(ZhuoMaiCardActivity.this,
						userInfo.getUserid(), userInfo.getName());
			}
		});
	}

	void sendCard() {
		// 递送名片(即添加好友)
		if (RongIM.getInstance().getRongIMClient() == null)
			return;
		ContactNotificationMessage msg = ContactNotificationMessage.obtain(
				"Request", myid, userInfo.getUserid(), "请求添加好友");
		RongIM.getInstance()
				.getRongIMClient()
				.sendMessage(ConversationType.PRIVATE, userInfo.getUserid(),
						msg, "请求添加好友", new SendMessageCallback() {

							@Override
							public void onSuccess(Integer arg0) {
								// TODO Auto-generated method stub
								mConnHelper.followUser(mUIHandler,
										MsgTagVO.MSG_FOWARD,
										userInfo.getUserid(), 1);
								pwh.showPopTip(findViewById(R.id.zhuomai_card),
										null, R.string.label_sendcardsuccess);
							}

							@Override
							public void onError(Integer arg0, ErrorCode arg1) {
								// TODO Auto-generated method stub
								Toast.makeText(ZhuoMaiCardActivity.this,
										"申请发送失败ErrorCode：" + arg1, 1000).show();
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
		String commercyInfo = getString(R.string.label_commercy_info);
		String activeZM = getString(R.string.label_active_zhuomai);
		String moreInfo = getString(R.string.label_more_info);

		Fragment quanTopic = addBundle(new ZhuomaiCardCommercyInfoFra(),
				QuanVO.QUANZITOPIC);
		fragments.add(quanTopic);
		titles.add(commercyInfo);

		Fragment quanEvent = addBundle(new ZhuomaiActiveInfoFra(),
				QuanVO.QUANZIEVENT);
		fragments.add(quanEvent);
		titles.add(activeZM);

		Fragment quanMember = addBundle(new ZhuomaiMoreInfoFra(),
				QuanVO.QUANZIMEMBER);
		fragments.add(quanMember);
		titles.add(moreInfo);
		return new ActivePagerAdapter(getSupportFragmentManager(), fragments,
				titles);
	}

	protected Fragment addBundle(Fragment fragment, int catlog) {
		Bundle bundle = new Bundle();
		bundle.putInt(QuanVO.QUANZIMAINTYPE, catlog);
		bundle.putString("userid", userid);
		fragment.setArguments(bundle);
		
		return fragment;
	}

	/**
	 * 填充头部个人信息
	 */
	void fillHeadInfo() {
		if (userInfo == null)
			return;
		mLoadImage.beginLoad(userInfo.getUheader(), ivHeader);
		tvName.setText(userInfo.getName());
		// tvPosition/tvMemType需要通过编码获得对应的名称
		if (mConnHelper.getCitys() != null && userInfo.getCity() >= 1)
			tvPosition.setText(mConnHelper.getCitys()
					.get(userInfo.getCity() - 1).getCityName());

		String work = "";
		if (baseDataSet != null) {
			int pos = userInfo.getPosition();
			if (pos != 0)
				pos--;
			work = ((baseDataSet.getPosition()).get(pos)).getContent();
		}
		tvMemType.setText(work);

		tvCompany.setText(userInfo.getCompany());
		// 手机接口
		tvPhone.setText(userInfo.getPhone());
		tvZBNum.setText("暂无倬币数");
		if (userInfo.getRelation() == UserNewVO.USER_RELATION.RELATION_MYSELF
				.ordinal() || userInfo.getUserid().equals(myid)) {
			ltNyselfMenue.setVisibility(View.VISIBLE);
			ltOtherMenue.setVisibility(View.GONE);
		} else {
			ltNyselfMenue.setVisibility(View.GONE);
			ltOtherMenue.setVisibility(View.VISIBLE);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					userInfo = nljh.parseNewUser();
					fillHeadInfo();
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
			case MsgTagVO.MSG_LIKE:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_zanSuccess);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.FAILED);
				}
			case MsgTagVO.MSG_FOWARD: {
				// if (JsonHandler.checkResult((String) msg.obj,
				// getApplicationContext())) {
				// pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
				// null, R.string.label_recommandSuccess);
				// }
				break;
			}
			}
		}
	};
}
