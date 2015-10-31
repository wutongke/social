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
import android.content.Context;
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
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.TabButton;
import com.cpstudio.zhuojiaren.widget.TabButton.PageChangeListener;
import com.cpstudio.zhuojiaren.widget.TabButton.TabsButtonOnClickListener;

/**
 * پ����Ƭ
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
	TextView tvZBNum;// پ����
	@InjectView(R.id.textViewNote)
	TextView tvSignature;// پ����
	@InjectView(R.id.textViewht)
	TextView tvCompany;

	@InjectView(R.id.lt_myself_menue)
	View ltNyselfMenue;// �������ϱ༭�˵�
	@InjectView(R.id.lt_other_menue)
	View ltOtherMenue;// �������ϱ༭�˵�

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
	@InjectView(R.id.rlSendCard)
	View rlSendCard;//
	
	private final static int USER_SELECT = 0;
	private Context mContext;
	List<Fragment> fragments;

	private PopupWindows phw = null;
	private LoadImage mLoadImage = new LoadImage();
	// ��ͬ��ݣ����ܲ�ͬ
	private String memberType = "";
	private PopupWindows pwh = null;
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;// �Ƿ��Ѿ������Ȧ��

	private ArrayList<String> tempids = new ArrayList<String>();

	String userid, myid, ismy;
	private UserFacade userFacade = null;
	UserNewVO userInfo;
	BaseCodeData baseDataSet;
	// ������fragment�л��groupid
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

		// ��ʼ��tab��viewpager
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

		// ���ø��Ա���ͼƬ���ڸ�����Ϣ����˿���ѡ���
		rootMainBG.setBackgroundResource(R.drawable.manbg_zmmp_1);
		baseDataSet = mConnHelper.getBaseDataSet();
		initOnClick();
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
			if (mConnHelper != null)
				mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD, userid);
		}
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		// ѡ��ͬ��fragment��function����ͬ
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
					// ��չʾ�����page
					viewPager.setCurrentItem(1);
					Intent intent = new Intent(mContext, CardEditActivity.class);
					intent.putExtra("id", userid);
					mContext.startActivity(intent);
					// ��ת�����跢��ҳ��
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
				int item=(Integer) (v.getTag());
				if ( item== 2) {
					Intent intent = new Intent(mContext, CardEditActivity.class);
					intent.putExtra("id", userid);
					mContext.startActivity(intent);
				}
				else {
					viewPager.setCurrentItem(item);
				}
			}
		});
		ivZan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// ����Ƭ

				mConnHelper.praiseCard(mUIHandler, MsgTagVO.MSG_LIKE, myid, 1);
				// MsgTagVO.MSG_LIKE, null, true, null, null);
			}
		});
		ivShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ���?�����أ�
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
				// // ͨ�����������?QQ��΢�ŵ�
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
				// // ��Ҫ�������ò˵�ѡ��ּ���Ӧ�¼�
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
					// Intent i = new Intent(ZhuoMaiCardActivity.this,
					// ChangeBackgroundActivity.class);
					// startActivity(i);
					CommonUtil.displayToast(ZhuoMaiCardActivity.this, "����");
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
							"���Ǻ��ѣ�����Ҫ�ٷ�����Ƭ");
				else if(r != UserNewVO.USER_RELATION.RELATION_FRIENDS.ordinal()){
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
					// ���İ��������������ʱ��Ҫ��ʾ�����ǲ��Ǻ��ѣ��Ƿ������Ƭ��ʹ���Ϊ��ĺ���
				}
				RongIM.getInstance().startPrivateChat(ZhuoMaiCardActivity.this,
						userInfo.getUserid(), userInfo.getName());
			}
		});
	}

	void sendCard() {
		// ������Ƭ(����Ӻ���)
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
		msg.setUserInfo(info);// �Է����ܵ�����ȻΪnull
		RongIM.getInstance()
				.getRongIMClient()
				// ���ܵ���content�̶�Ϊ"�����Ϊ���ѣ����õ�ֻ��id"
				.sendMessage(ConversationType.PRIVATE, userInfo.getUserid(),
						msg, userInfo.getName(), new SendMessageCallback() {
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
										"���뷢��ʧ��ErrorCode��" + arg1, 1000).show();
							}
						});
	}

	protected void onPause() {
		super.onPause();
//		viewPager.setCurrentItem(0, false);
//		tabButton.setTabBackgroundByIndex(0);
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
	 * ���ͷ��������Ϣ
	 */
	void fillHeadInfo() {
		if (userInfo == null)
			return;
		tvSignature.setText(userInfo.getSignature());
		mLoadImage.beginLoad(userInfo.getUheader(), ivHeader);
		tvName.setText(userInfo.getName());
		// tvPosition/tvMemType��Ҫͨ������ö�Ӧ�����
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
		// tvZBNum.setText("����پ����");
		if (userInfo.getRelation() == UserNewVO.USER_RELATION.RELATION_MYSELF
				.ordinal() || userInfo.getUserid().equals(myid)) {
			ltNyselfMenue.setVisibility(View.VISIBLE);
			ltOtherMenue.setVisibility(View.GONE);
		} else {
			ltNyselfMenue.setVisibility(View.GONE);
			ltOtherMenue.setVisibility(View.VISIBLE);
			if(userInfo.getRelation()==UserNewVO.USER_RELATION.RELATION_FRIENDS.ordinal())
			{
				rlSendCard.setVisibility(View.GONE);
			}
				
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
			case MsgTagVO.MSG_LIKE:{
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_zanSuccess);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.FAILED);
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					sendCard();
				} else {
					sendCard();
					CommonUtil.displayToast(getApplicationContext(),
							R.string.FAILED);
				}
				break;
			}
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		loadData();
		super.onResume();
	}
}
