package io.rong.app;

import io.rong.imkit.common.RongConst;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Group;

import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.ui.ZhuoMaiCardActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanMainActivity;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class ConversationActivity extends FragmentActivity {
	@InjectView(R.id.buttonManage)
	Button btnManage;
	@InjectView(R.id.userNameShow)
	TextView tvTitle;
	int type = 0;// 0:私聊 1：群聊
	private PopupWindows pwh = null;
	String myid;
	String targetId;
	UserNewVO guest = null;
	private Button followButton;
	private ConnHelper mConnHelper = null;
	private Conversation.ConversationType mConversationType;
	// 需要增加一个接口判断两人是否为好友关系
	String tofollow = "1";// 0:已经互相关注为好友，1：还不是好友关系
	Group group = null;
	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {// 获得聊天对象得信息
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					guest = nljh.parseNewUser();
					if (guest != null
							&& (guest.getRelation() == UserNewVO.USER_RELATION.RELATION_STRANGER
									.ordinal())) {
						tofollow = "1";
						tvTitle.setText(guest.getName());
					} else
						tofollow = "0";
					UserFacade facade = new UserFacade(ConversationActivity.this
							.getApplicationContext());
					facade.saveOrUpdate(guest);
				}
				break;
			}
			case MsgTagVO.ADD_BACK: {// 拉入黑名单
				// if (JsonHandler.checkResult((String) msg.obj,
				// getApplicationContext())) {
				// if (toBlack.equals("0")) {
				// blackButton.setText(R.string.label_unblack);
				// toBlack = "1";
				// } else {
				// blackButton.setText(R.string.label_black);
				// toBlack = "0";
				// }
				// CommonUtil.displayToast(getApplicationContext(),
				// R.string.label_success);
				// }
				// break;
			}
			case MsgTagVO.MSG_FOWARD: {// 递送名片，加好友
				// if (JsonHandler.checkResult((String) msg.obj,
				// getApplicationContext())) {
				// if (tofollow.equals("0")) {
				// followButton.setText(R.string.label_follow);
				// tofollow = "1";
				// } else {
				// tofollow = "0";
				// followButton.setText(R.string.label_unfollow);
				// }
				// CommonUtil.displayToast(getApplicationContext(),
				// R.string.label_success);
				// }
				break;
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		ButterKnife.inject(this);
		// getIntent().getData()
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(ConversationActivity.this);

		Intent intent = getIntent();

		// // push或通知过来
		// if (intent != null && intent.getData() != null
		// && intent.getData().getScheme().equals("rong")
		// && intent.getData().getQueryParameter("push") != null) {
		// // 通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
		// if (ZhuoConnHelper.getInstance(getApplicationContext()) != null
		// && intent.getData().getQueryParameter("push")
		// .equals("true")) {
		// // enterActivity(intent);
		// } else {
		// enterFragment(intent);
		// }
		// }

		// push或通知过来
		if (intent != null && intent.getData() != null
				&& intent.getData().getScheme().equals("rong")) {
			// 通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
			if (intent.getData().getQueryParameter("push") == null
					|| intent.getData().getQueryParameter("push")
							.equals("true")) {
				String title = intent.getData().getQueryParameter("title");

				if (title != null)
					tvTitle.setText(title);
				enterFragment(intent);
			}
			else
			{
				//push消息的处理
			}
		}

		// else if (intent != null) {
		// // 程序切到后台，收到消息后点击进入,会执行这里
		// if (RongIM.getInstance() == null
		// || RongIM.getInstance().getRongIMClient() == null) {
		// if (DemoContext.getInstance() != null) {
		// String token = DemoContext.getInstance()
		// .getSharedPreferences()
		// .getString("DEMO_TOKEN", "defult");
		// reconnect(token);
		// }
		// } else {
		// enterFragment(intent);
		// }
		// }
		initClick();
	}

	/**
	 * 消息分发，选择跳转到哪个fragment
	 * 
	 * @param intent
	 */
	private void enterFragment(Intent intent) {
		String tag = null;
		if (intent != null) {
			// Fragment fragment = null;
			if (intent.getExtras() != null
					&& intent.getExtras().containsKey(RongConst.EXTRA.CONTENT)) {
				String fragmentName = intent.getExtras().getString(
						RongConst.EXTRA.CONTENT);
				// fragment = Fragment.instantiate(this, fragmentName);
			} else if (intent.getData() != null) {
				// if
				// (intent.getData().getPathSegments().get(0).equals("conversation"))
				// {
				// tag = "conversation";
				// if (intent.getData().getLastPathSegment().equals("system")) {
				// //注释掉的代码为不加输入框的聊天页面（此处作为示例）
				// // String fragmentName =
				// MessageListFragment.class.getCanonicalName();
				// // fragment = Fragment.instantiate(this, fragmentName);
				// // lz startActivity(new Intent(DemoActivity.this,
				// NewFriendListActivity.class));
				// // finish();
				// // List<Conversation> conversations =
				// RongIM.getInstance().getRongIMClient().getConversationList(Conversation.ConversationType.SYSTEM);
				// // for (int i = 0; i < conversations.size(); i++) {
				// //
				// RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(Conversation.ConversationType.SYSTEM,
				// conversations.get(i).getSenderUserId());
				// // }
				// } else {
				// String fragmentName =
				// ConversationFragment.class.getCanonicalName();
				// // fragment = Fragment.instantiate(this, fragmentName);
				// }
				// } else if
				// (intent.getData().getLastPathSegment().equals("conversationlist"))
				// {
				// tag = "conversationlist";
				// String fragmentName =
				// ConversationListFragment.class.getCanonicalName();
				// fragment = Fragment.instantiate(this, fragmentName);
				// } else if
				// (intent.getData().getLastPathSegment().equals("subconversationlist"))
				// {
				// tag = "subconversationlist";
				// String fragmentName =
				// SubConversationListFragment.class.getCanonicalName();
				// fragment = Fragment.instantiate(this, fragmentName);
				// } else if
				// (intent.getData().getPathSegments().get(0).equals("friend"))
				// {
				// tag = "friend";
				// String fragmentName =
				// FriendMultiChoiceFragment.class.getCanonicalName();
				// fragment = Fragment.instantiate(this, fragmentName);
				// ActionBar actionBar = getSupportActionBar();
				// actionBar.hide();//隐藏ActionBar
				// }

				targetId = intent.getData().getQueryParameter("targetId");
				mConversationType = Conversation.ConversationType
						.valueOf(intent.getData().getLastPathSegment()
								.toUpperCase(Locale.getDefault()));
				if (mConversationType == ConversationType.PRIVATE)
					mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
							targetId);
				// else {
				// group = mConnHelper.getGroupMap().get("targetId");
				// if (group != null) {
				// tvTitle.setText(group.getName());
				// }
				// }
			}
		}
	}

	//
	// /**
	// * 收到 push 消息后，选择进入哪个 Activity 如果程序缓存未被清理，进入 MainActivity 程序缓存被清理，进入
	// * LoginActivity，重新获取token
	// * <p/>
	// * 作用：由于在 manifest 中 intent-filter 是配置在 DemoActivity
	// * 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。 以跳到 MainActivity 为例： 在
	// * DemoActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击
	// * 返回键 时，程序会退到 MainActivity 页面，而不是直接退回到 桌面。
	// */
	// private void enterActivity(Intent intent) {
	//
	// if (ZhuoConnHelper.getInstance(getApplicationContext()) != null) {
	// String userid = ZhuoConnHelper.getInstance(getApplicationContext())
	// .getUserid();
	// Intent in = new Intent();
	// if (userid != null) {
	// in.setClass(ConversationActivity.this,
	// TabContainerActivity.class);
	// // in.putExtra("PUSH_TOKEN", token);
	// // in.putExtra("PUSH_INTENT", intent.getData());
	// } else {
	// in.setClass(ConversationActivity.this, LoginActivity.class);
	// // in.putExtra("PUSH_CONTEXT", "push");
	// }
	// startActivity(in);
	// finish();
	// }
	// }
	// if (intent.getExtras() != null &&
	// intent.getExtras().containsKey(RongConst.EXTRA.CONTENT)) {
	// String fragmentName =
	// intent.getExtras().getString(RongConst.EXTRA.CONTENT);
	// fragment = Fragment.instantiate(this, fragmentName);
	// } else if (intent.getData() != null) {
	// if (intent.getData().getPathSegments().get(0).equals("conversation")) {
	// tag = "conversation";
	// if (intent.getData().getLastPathSegment().equals("system")) {
	// //注释掉的代码为不加输入框的聊天页面（此处作为示例）
	// // String fragmentName = MessageListFragment.class.getCanonicalName();
	// // fragment = Fragment.instantiate(this, fragmentName);
	// startActivity(new Intent(DemoActivity.this,
	// NewFriendListActivity.class));
	// finish();
	// List<Conversation> conversations =
	// RongIM.getInstance().getRongIMClient().getConversationList(Conversation.ConversationType.SYSTEM);
	// for (int i = 0; i < conversations.size(); i++) {
	// RongIM.getInstance().getRongIMClient().clearMessagesUnreadStatus(Conversation.ConversationType.SYSTEM,
	// conversations.get(i).getSenderUserId());
	// }
	// } else {
	// String fragmentName = ConversationFragment.class.getCanonicalName();
	// fragment = Fragment.instantiate(this, fragmentName);
	// }
	// } else if
	// (intent.getData().getLastPathSegment().equals("conversationlist")) {
	// tag = "conversationlist";
	// String fragmentName = ConversationListFragment.class.getCanonicalName();
	// fragment = Fragment.instantiate(this, fragmentName);
	// } else if
	// (intent.getData().getLastPathSegment().equals("subconversationlist")) {
	// tag = "subconversationlist";
	// String fragmentName =
	// SubConversationListFragment.class.getCanonicalName();
	// fragment = Fragment.instantiate(this, fragmentName);
	// } else if (intent.getData().getPathSegments().get(0).equals("friend")) {
	// tag = "friend";
	// String fragmentName = FriendMultiChoiceFragment.class.getCanonicalName();
	// fragment = Fragment.instantiate(this, fragmentName);
	// ActionBar actionBar = getSupportActionBar();
	// actionBar.hide();//隐藏ActionBar
	// }
	// targetId = intent.getData().getQueryParameter("targetId");
	// targetIds = intent.getData().getQueryParameter("targetIds");
	// mDiscussionId = intent.getData().getQueryParameter("discussionId");
	// if (targetId != null) {
	// // intent.getData().getLastPathSegment();//获得当前会话类型
	// mConversationType =
	// Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
	// } else if (targetIds != null)
	// mConversationType =
	// Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
	// }
	// }

	private void initClick() {
		// TODO Auto-generated method stub
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConversationActivity.this.finish();
			}
		});
		btnManage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = findViewById(R.id.root_layout);
				OnClickListener onClickListenerViewCard = new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						Intent i = new Intent(ConversationActivity.this,
								ZhuoMaiCardActivity.class);
						i.putExtra("userid", targetId);
						startActivity(i);
					}
				};
				OnClickListener onClickListenerViewQuan = new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						Intent i = new Intent(ConversationActivity.this,
								ZhuoQuanMainActivity.class);
						i.putExtra("groupid", targetId);
						startActivity(i);
					}
				};
				int followInt = R.string.label_unfollow;
				if (tofollow.equals("1")) {
					followInt = R.string.label_follow;
				}
				if (mConversationType == ConversationType.PRIVATE) {
					pwh.showBottomPop(view,
							new OnClickListener[] { onClickListenerViewCard },
							new int[] { R.string.label_viewcard }, 7, "manage");
				} else
					pwh.showBottomPop(
							view,
							new OnClickListener[] { onClickListenerViewQuan },
							new int[] { R.string.title_activity_zhuojiaquan_detail },
							7, "manage");
			}
		});
	}
}
