package com.cpstudui.zhuojiaren.lz;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.SendMessageCallback;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.CustomShareBoard;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.cpstudio.zhuojiaren.widget.TabButton.TabsButtonOnClickListener;

/**
 * 閸婎剝鍓﹂崥宥囧
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
	TextView tvZBNum;// 閸婎剙绔甸弫锟�
	@InjectView(R.id.textViewNote)
	TextView tvSignature;
	@InjectView(R.id.textViewht)
	TextView tvCompany;

	@InjectView(R.id.lt_myself_menue)
	View ltNyselfMenue;
	@InjectView(R.id.lt_other_menue)
	View ltOtherMenue;

	@InjectView(R.id.btnEditBG)
	Button btnEditBG;
	@InjectView(R.id.btnEditCard)
	Button btnEditCard;

	@InjectView(R.id.btnSendCard)
	Button btnSendCard;//
	@InjectView(R.id.btnChat)
	Button btnChat;
	@InjectView(R.id.rootmain)
	View rootMainBG;//
	@InjectView(R.id.rlSendCard)
	View rlSendCard;//

	private final static int USER_SELECT = 0;
	private Context mContext;
	List<Fragment> fragments;

	private PopupWindows phw = null;
	private LoadImage mLoadImage = new LoadImage();
	// 娑撳秴鎮撻煬顐″敜閿涘苯濮涢懗鎴掔瑝閸氾拷
	private String memberType = "";
	private PopupWindows pwh = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;

	private ArrayList<String> tempids = new ArrayList<String>();

	String userid, myid, ismy;
	private UserFacade userFacade = null;
	UserNewVO userInfo;
	BaseCodeData baseDataSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_card_main);
		ButterKnife.inject(this);
		mContext = this;
		tvTitle.setText(R.string.title_zhuomai_card);

		viewPager.setAdapter(getPagerAdapter());

		tabButton.setViewPager(viewPager);

		userFacade = new UserFacade(ZhuoMaiCardActivity.this);
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

		rootMainBG.setBackgroundResource(R.drawable.manbg_zmmp_1);
		baseDataSet = mConnHelper.getBaseDataSet();
		initOnClick();
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			if (mConnHelper != null)
				mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD, userid);
		}
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
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
					viewPager.setCurrentItem(1);
					Intent intent = new Intent(mContext, CardEditActivity.class);
					intent.putExtra("id", userid);
					mContext.startActivity(intent);
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
				int item = (Integer) (v.getTag());
				if (item == 2) {
					Intent intent = new Intent(mContext, CardEditActivity.class);
					intent.putExtra("id", userid);
					mContext.startActivity(intent);
				} else {
					viewPager.setCurrentItem(item);
				}
			}
		});
		ivZan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mConnHelper.praiseCard(mUIHandler, MsgTagVO.MSG_LIKE, userid, 1);
			}
		});
		ivShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CustomShareBoard cb = new CustomShareBoard(
						ZhuoMaiCardActivity.this);
				cb.showCustomShareContent();
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
							getString(R.string.noneed_send_card));
				else if (r == UserNewVO.USER_RELATION.RELATION_FRIENDS
						.ordinal()) {
					new AlertDialog.Builder(ZhuoMaiCardActivity.this,
							AlertDialog.THEME_HOLO_LIGHT)
							.setTitle(getString(R.string.alert))
							.setMessage(getString(R.string.sure_remove_friend))
							.setPositiveButton(getString(R.string.sure),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											//閸掝亪娅庢總钘夊几
											mConnHelper.makeFriends(mUIHandler, MsgTagVO.MSG_DEL,
													userInfo.getUserid(), 0);
										}
									}).setNegativeButton(getString(R.string.label_cancel), null).create()
							.show();
					
				}
				else
				{
					mConnHelper.makeFriends(mUIHandler, MsgTagVO.MSG_FOWARD,
							userInfo.getUserid(), 1);
				}
			}
		});
		btnChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				int r = userInfo.getRelation();
				if (r == UserNewVO.USER_RELATION.RELATION_STRANGER.ordinal()) {
				}
				RongIM.getInstance().startPrivateChat(ZhuoMaiCardActivity.this,
						userInfo.getUserid(), userInfo.getName());
			}
		});
	}

	void sendCard() {
		// 闁帡锟介崥宥囧閿涘牐顕Ч鍌涘潑閸旂姴銈介崣瀣剁礆
		if (RongIM.getInstance().getRongIMClient() == null)
			return;
		if (userInfo.getUserid() == null)
			return;
		ContactNotificationMessage msg = ContactNotificationMessage.obtain(
				"Request", myid, userInfo.getUserid(), userInfo.getName());

		io.rong.imlib.model.UserInfo info = new UserInfo(userInfo.getUserid(),
				userInfo.getName(), Uri.parse(userInfo.getUheader()));
		info.setName(userInfo.getName());
		info.setUserId(userInfo.getUserid());
		msg.setUserInfo(info);// 鐎佃鏌熼幒銉ュ綀閸掓壆娈戞禒宥囧姧娑撶皠ull
		RongIM.getInstance()
				.getRongIMClient()
				// 閹恒儱褰堥崚鎵畱content閸ュ搫鐣炬稉锟界拠閿嬬湴閸旂姳璐熸總钘夊几閿涘本婀侀悽銊ф畱閸欘亝婀乮d"
				.sendMessage(ConversationType.PRIVATE, userid,
						msg, myid, new SendMessageCallback() {
							@Override
							public void onSuccess(Integer arg0) {
								// TODO Auto-generated method stub
								pwh.showPopTip(findViewById(R.id.zhuomai_card),
										null, R.string.label_sendcardsuccess);
							}

							@Override
							public void onError(Integer arg0, ErrorCode arg1) {
								// TODO Auto-generated method stub
								Toast.makeText(ZhuoMaiCardActivity.this,
										getString(R.string.rongyun_failed),
										1000).show();
							}
						});
	}

	protected void onPause() {
		super.onPause();
		// viewPager.setCurrentItem(0, false);
		// tabButton.setTabBackgroundByIndex(0);
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
		Intent i = getIntent();
		userid = i.getStringExtra("userid");
		Bundle bundle = new Bundle();
		bundle.putInt(QuanVO.QUANZIMAINTYPE, catlog);
		bundle.putString("userid", userid);
		fragment.setArguments(bundle);

		return fragment;
	}

	/**
	 * 婵夘偄鍘栭崐瀣╂眽閸╃儤婀版穱鈩冧紖
	 */
	void fillHeadInfo() {
		if (userInfo == null)
			return;
		tvSignature.setText(userInfo.getSignature());
		mLoadImage.beginLoad(userInfo.getUheader(), ivHeader);
		tvName.setText(userInfo.getName());
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
		tvPhone.setText(userInfo.getPhone());
<<<<<<< .mine
		// tvZBNum.setText("閺嗗倹妫ら崐顒�閺侊拷);
=======
		
>>>>>>> .theirs
		if (userInfo.getRelation() == UserNewVO.USER_RELATION.RELATION_MYSELF
				.ordinal() || userInfo.getUserid().equals(myid)) {
			ltNyselfMenue.setVisibility(View.VISIBLE);
			ltOtherMenue.setVisibility(View.GONE);
			int count = 1;
			try {
				count = Integer.parseInt(userInfo.getZhuobi());
			} catch (Exception e) {

			}
			if (count != -1)
				tvZBNum.setText(String
						.format(getResources().getString(R.string.num), count));
		} else {
			ltNyselfMenue.setVisibility(View.GONE);
			ltOtherMenue.setVisibility(View.VISIBLE);
			tvZBNum.setVisibility(View.GONE);
			if (userInfo.getRelation() == UserNewVO.USER_RELATION.RELATION_FRIENDS
					.ordinal()) {
				btnSendCard.setText(R.string.remove_friend);
			}
			else
				btnSendCard.setText(R.string.label_cardsend);

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
			case MsgTagVO.MSG_LIKE: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_zanSuccess);
				} else {
					// CommonUtil.displayToast(getApplicationContext(),
					// R.string.FAILED);
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
//ios鏆傛椂娌″仛锛屾秷鎭繕闇�鑷畾涔�	sendCard();
				} else {
				}
				break;
			}
			case MsgTagVO.MSG_DEL: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					changeStatus();
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.FAILED);
				}
				break;
			}
			}
		}
	};

	/**
	 * 閺�懓褰夋總钘夊几閸忓磭閮�
	 */
	void changeStatus()
	{
		loadData();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		loadData();
		super.onResume();
	}
}
