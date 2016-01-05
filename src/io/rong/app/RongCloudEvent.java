package io.rong.app;

import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.app.model.User;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.notification.PushNotificationMessage;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

import com.cpstudio.zhuojiaren.facade.GroupFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.ui.TabContainerActivity;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;
import com.sea_monster.exception.BaseException;
import com.sea_monster.network.AbstractHttpRequest;
import com.sea_monster.network.ApiCallback;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有： 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。 5、群组信息提供者：GetGroupInfoProvider。 蓉c
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent implements
		RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
		RongIM.UserInfoProvider, RongIM.GroupInfoProvider,
		RongIM.ConversationBehaviorListener,
		RongIMClient.ConnectionStatusListener, RongIM.LocationProvider,
		RongIMClient.OnReceivePushMessageListener,
		RongIM.ConversationListBehaviorListener, ApiCallback {

	private static final String TAG = "RongCloudEvent";

	private static RongCloudEvent mRongCloudInstance;

	private Context mContext;
	private AbstractHttpRequest<User> getUserInfoByUserIdHttpRequest;

	/**
	 * 初始化 RongCloud.
	 * 
	 * @param context
	 *            上下文。
	 */
	public static void init(Context context) {

		if (mRongCloudInstance == null) {

			synchronized (RongCloudEvent.class) {

				if (mRongCloudInstance == null) {
					mRongCloudInstance = new RongCloudEvent(context);
				}
			}
		}
	}

	/**
	 * 构造方法。
	 * 
	 * @param context
	 *            上下文。
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		initDefaultListener();
	}

	/**
	 * RongIM.init(this) 后直接可注册的Listener。
	 */
	private void initDefaultListener() {
		RongIM.setUserInfoProvider(this, true);// 设置用户信息提供者。
		RongIM.setGroupInfoProvider(this, true);// 设置群组信息提供者。
		RongIM.setConversationBehaviorListener(this);// 设置会话界面操作的监听器。
		RongIM.setLocationProvider(this);// 设置地理位置提供者,不用位置的同学可以注掉此行代码
		RongIM.setOnReceivePushMessageListener(this);
		RongIM.setOnReceiveMessageListener(this);
	}

	/*
	 * 连接成功注册。 <p/> 在RongIM-connect-onSuccess后调用。
	 */
	public void setOtherListener() {
		RongIM.getInstance().getRongIMClient()
				.setOnReceiveMessageListener(this);// 设置消息接收监听器。
		RongIM.getInstance().setSendMessageListener(this);// 设置发出消息接收监听器.
		RongIM.getInstance().getRongIMClient()
				.setConnectionStatusListener(this);// 设置连接状态监听器。

		// 扩展功能自定义
		InputProvider.ExtendProvider[] provider = {
				new ImageInputProvider(RongContext.getInstance()),// 图片
				new CameraInputProvider(RongContext.getInstance()),// 相机
		// new LocationInputProvider(RongContext.getInstance()),// 地理位置
		// new VoIPInputProvider(RongContext.getInstance()),// 语音通话
		// new LZExtendProvider(RongContext.getInstance()) // 自定义通讯录
		};
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.PRIVATE, provider);

	}

	/**
	 * 自定义 push 通知。 如 onReceivePushMessage 返回 true 融云不会在通知栏弹出通知，需要开发者自已处理。
	 * 
	 * @param msg
	 * @return
	 */
	@Override
	public boolean onReceivePushMessage(PushNotificationMessage msg) {
		Log.d(TAG, "onReceived-onPushMessageArrive:" + msg.getContent());

		// PushNotificationManager.getInstance().onReceivePush(msg);
		// Intent intent = new Intent();
		// Uri uri;
		//
		// intent.setAction(Intent.ACTION_VIEW);
		//
		// Conversation.ConversationType conversationType = msg
		// .getConversationType();
		//
		// uri = Uri.parse("rong://" +
		// RongContext.getInstance().getPackageName())
		// .buildUpon().appendPath("conversationlist").build();
		// intent.setData(uri);
		// Log.d(TAG, "onPushMessageArrive-url:" + uri.toString());
		//
		// Notification notification = null;
		//
		// PendingIntent pendingIntent = PendingIntent.getActivity(
		// RongContext.getInstance(), 0, intent,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		//
		// if (android.os.Build.VERSION.SDK_INT < 11) {
		// notification = new Notification(RongContext.getInstance()
		// .getApplicationInfo().icon, "自定义 notification",
		// System.currentTimeMillis());
		//
		// notification.setLatestEventInfo(RongContext.getInstance(),
		// "自定义 title", "这是 Content:" + msg.getObjectName(),
		// pendingIntent);
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// notification.defaults = Notification.DEFAULT_SOUND;
		// } else {
		//
		// notification = new Notification.Builder(RongContext.getInstance())
		// .setLargeIcon(getAppIcon())
		// .setSmallIcon(R.drawable.ic_launcher)
		// .setTicker("自定义 notification").setContentTitle("自定义 title")
		// .setContentText("这是 Content:" + msg.getObjectName())
		// .setContentIntent(pendingIntent).setAutoCancel(true)
		// .setDefaults(Notification.DEFAULT_ALL).build();
		//
		// }
		//
		// NotificationManager nm = (NotificationManager) RongContext
		// .getInstance().getSystemService(
		// RongContext.getInstance().NOTIFICATION_SERVICE);
		//
		// nm.notify(0, notification);
		return false;
	}

	private Bitmap getAppIcon() {
		BitmapDrawable bitmapDrawable;
		Bitmap appIcon;
		bitmapDrawable = (BitmapDrawable) RongContext.getInstance()
				.getApplicationInfo()
				.loadIcon(RongContext.getInstance().getPackageManager());
		appIcon = bitmapDrawable.getBitmap();
		return appIcon;
	}

	/**
	 * 获取RongCloud 实例。
	 * 
	 * @return RongCloud。
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}

	/**
	 * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
	 * 接收消息监听器的实现，所有接收到的消息、通知、状态都经由此处设置的监听器处理。包括私聊消息、讨论组消息、群组消息、聊天室消息以及各种状态。
	 * 
	 * @param message
	 *            接收到的消息的实体信息。
	 * @param left
	 *            剩余未拉取消息数目。 // 收到消息是否处理完成，true 表示走自已的处理方式，false 走融云默认处理方式。
	 */
	@Override
	public boolean onReceived(Message message, int left) {

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			if (((TextMessage) messageContent).getExtra() != null) {
				handleCustomMessage((TextMessage) messageContent);
				return true;
			}
			Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onReceived-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onReceived-RichContentMessage:"
							+ richContentMessage.getContent());
		} else if (messageContent instanceof InformationNotificationMessage) {// 小灰条消息
			InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-informationNotificationMessage:"
					+ informationNotificationMessage.getMessage());
		} else if (messageContent instanceof DeAgreedFriendRequestMessage) {// 好友添加成功消息
			DeAgreedFriendRequestMessage deAgreedFriendRequestMessage = (DeAgreedFriendRequestMessage) messageContent;
			Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:"
					+ deAgreedFriendRequestMessage.getMessage());
			receiveAgreeSuccess(deAgreedFriendRequestMessage);
		} else if (messageContent instanceof ContactNotificationMessage) {// 好友添加消息
			ContactNotificationMessage contactContentMessage = (ContactNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-ContactNotificationMessage:getExtra;"
					+ contactContentMessage.getExtra());
			Log.d(TAG, "onReceived-ContactNotificationMessage:+getmessage:"
					+ contactContentMessage.getMessage().toString());
			// RongIM.getInstance().getRongIMClient().deleteMessages(new
			// int[]{message.getMessageId()});
			// if(DemoContext.getInstance()!=null) {
			// RongIM.getInstance().getRongIMClient().removeConversation(Conversation.ConversationType.SYSTEM,
			// "10000");
			// String targetname =
			// DemoContext.getInstance().getUserNameByUserId(contactContentMessage.getSourceUserId());
			// RongIM.getInstance().getRongIMClient().insertMessage(Conversation.ConversationType.SYSTEM,
			// "10000", contactContentMessage.getSourceUserId(),
			// contactContentMessage, null);
			//
			// }
			Intent in = new Intent();
			in.setAction(TabContainerActivity.ACTION_DMEO_RECEIVE_MESSAGE);
			in.putExtra("rongCloud", contactContentMessage);
			in.putExtra("has_message", true);
			mContext.sendBroadcast(in);
			return true;
		} else {
			Log.d(TAG, "onReceived-其他消息，自己来判断处理");
		}
		return false;
	}

	// 文本消息的extra字段部位空则为自定义的消息：接收到点赞，评论等的推送消息(是否能实现？)
	private void handleCustomMessage(TextMessage messageContent) {
		// TODO Auto-generated method stub
		String typeStr = messageContent.getExtra();
		if (typeStr == null)
			return;
		int type = 1;
		try {
			type = Integer.parseInt(typeStr);
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}

		Intent in = new Intent();
		in.setAction(TabContainerActivity.ACTION_SYS_MSG);
		in.putExtra("json", messageContent);
		in.putExtra("type", type);
		mContext.sendBroadcast(in);
	}

	/**
	 * @param deAgreedFriendRequestMessage
	 */
	private void receiveAgreeSuccess(
			DeAgreedFriendRequestMessage deAgreedFriendRequestMessage) {
		ArrayList<UserInfo> friendreslist = new ArrayList<UserInfo>();
		// 暂时注释 if (DemoContext.getInstance() != null) {
		// friendreslist = DemoContext.getInstance().getFriendList();
		// friendreslist.add(deAgreedFriendRequestMessage.getUserInfo());
		//
		// // DemoContext.getInstance().setFriends(friendreslist);
		// }
		Intent in = new Intent();
		in.setAction(TabContainerActivity.ACTION_DMEO_AGREE_REQUEST);
		in.putExtra("userid", deAgreedFriendRequestMessage.getFriendId());
		mContext.sendBroadcast(in);
	}

	@Override
	public Message onSend(Message message) {
		return message;
	}

	/**
	 * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
	 * 
	 * @param message
	 *            消息。
	 */
	@Override
	public boolean onSent(Message message,
			RongIM.SentMessageErrorCode sentMessageErrorCode) {

		if (message.getSentStatus() == Message.SentStatus.FAILED) {

			if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {// 不在聊天室

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {// 不在讨论组

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {// 不在群组

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {// 你在他的黑名单中

			}
		}

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// 文本消息
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// 图片消息
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// 语音消息
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onSent-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// 图文消息
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onSent-RichContentMessage:"
							+ richContentMessage.getContent());
		} else {
			Log.d(TAG, "onSent-其他消息，自己来判断处理");
		}
		return false;
	}

	/**
	 * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
	 * 
	 * @param userId
	 *            用户 Id。
	 * @return 用户信息，（注：由开发者提供用户信息）。
	 */
	@Override
	public UserInfo getUserInfo(String userId) {
		UserFacade mUserInfosDao = DemoContext.getInstance(mContext)
				.getmUserInfosDao();
		if (mUserInfosDao == null
				|| mUserInfosDao.getSimpleInfoById(userId) == null) {
			// 数据库中不存在，网络请求
			Log.i("cloudevent", (mUserInfosDao == null) + "," + "好友不存在");
		}
		UserInfo info = DemoContext.getInstance(mContext).getUserInfoById(
				userId);
		if (info != null)
			Log.i("cloudevent",
					"user:" + info.getUserId() + "," + info.getName() + ","
							+ info.getPortraitUri());
		return info;
	}

	/**
	 * 群组信息的提供者：GetGroupInfoProvider 的回调方法， 获取群组信息。
	 * 
	 * @param groupId
	 *            群组 Id.
	 * @return 群组信息，（注：由开发者提供群组信息）。
	 */
	@Override
	public Group getGroupInfo(String groupId) {
		GroupFacade mGroupInfosDao = DemoContext.getInstance(mContext)
				.getmGroupInfoDao();
		if (mGroupInfosDao == null
				|| mGroupInfosDao.getSimpleInfoById(groupId) == null) {
			// 数据库中不存在，网络请求
			return null;
		}
		Group info = DemoContext.getInstance(mContext)
				.getGroupInfoById(groupId);
		if (info != null)
			Log.i("cloudevent", "group" + info.getId() + "," + info.getName()
					+ "," + info.getPortraitUri());
		return info;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param conversationType
	 *            会话类型。
	 * @param user
	 *            被点击的用户的信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onUserPortraitClick(Context context,
			Conversation.ConversationType conversationType, UserInfo user) {
		Log.d(TAG, "onUserPortraitClick");

		Intent i = new Intent(context, ZhuoMaiCardActivity.class);
		i.putExtra("userid", user.getUserId());
		context.startActivity(i);
		//
		// /**
		// * demo 代码 开发者需替换成自己的代码。
		// */
		// if (user != null) {
		// Log.d("Begavior", conversationType.getName() + ":" + user.getName());
		// Intent in = new Intent(context, DePersonalDetailActivity.class);
		// in.putExtra("USER", user);
		// in.putExtra("SEARCH_USERID", user.getUserId());
		// context.startActivity(in);
		// }
		return false;
	}

	@Override
	public boolean onUserPortraitLongClick(Context context,
			Conversation.ConversationType conversationType, UserInfo userInfo) {
		return false;
	}

	/**
	 * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
	 * 
	 * @param context
	 *            应用当前上下文。
	 * @param message
	 *            被点击的消息的实体信息。
	 * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
	 */
	@Override
	public boolean onMessageClick(Context context, View view, Message message) {
		Log.d(TAG, "onMessageClick");

		// /**
		// * demo 代码 开发者需替换成自己的代码。
		// */
		// if (message.getContent() instanceof LocationMessage) {
		// Intent intent = new Intent(context, SOSOLocationActivity.class);
		// intent.putExtra("location", message.getContent());
		// context.startActivity(intent);
		// } else if (message.getContent() instanceof RichContentMessage) {
		// RichContentMessage mRichContentMessage = (RichContentMessage)
		// message.getContent();
		// Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());
		//
		// } else if (message.getContent() instanceof ImageMessage) {
		// ImageMessage imageMessage = (ImageMessage) message.getContent();
		// Intent intent = new Intent(context, PhotoActivity.class);
		//
		// intent.putExtra("photo", imageMessage.getLocalUri() == null ?
		// imageMessage.getRemoteUri() : imageMessage.getLocalUri());
		// if (imageMessage.getThumUri() != null)
		// intent.putExtra("thumbnail", imageMessage.getThumUri());
		//
		// context.startActivity(intent);
		// }
		//
		// Log.d("Begavior", message.getObjectName() + ":" +
		// message.getMessageId());

		return false;
	}

	@Override
	public boolean onMessageLongClick(Context context, View view,
			Message message) {
		return false;
	}

	/**
	 * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
	 * 
	 * @param status
	 *            网络状态。
	 */
	@Override
	public void onChanged(ConnectionStatus status) {
		Log.d(TAG, "onChanged:" + status);
		if (status.getMessage().equals(
				ConnectionStatus.DISCONNECTED.getMessage())) {
		}
	}

	/**
	 * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
	 * 
	 * @param context
	 *            上下文
	 * @param callback
	 *            回调
	 */
	@Override
	public void onStartLocation(Context context, LocationCallback callback) {
		// /**
		// * demo 代码 开发者需替换成自己的代码。
		// */
		// DemoContext.getInstance().setLastLocationCallback(callback);
		// context.startActivity(new Intent(context,
		// SOSOLocationActivity.class));//SOSO地图
	}

	/**
	 * 点击会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationClick(Context context, View view,
			UIConversation conversation) {
		return false;
	}

	/**
	 * 长按会话列表 item 后执行。
	 * 
	 * @param context
	 *            上下文。
	 * @param view
	 *            触发点击的 View。
	 * @param conversation
	 *            长按会话条目。
	 * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
	 */
	@Override
	public boolean onConversationLongClick(Context context, View view,
			UIConversation conversation) {
		return false;
	}

	@Override
	public void onComplete(AbstractHttpRequest abstractHttpRequest, Object obj) {
		if (getUserInfoByUserIdHttpRequest != null
				&& getUserInfoByUserIdHttpRequest.equals(abstractHttpRequest)) {
			if (obj instanceof User) {
				final User user = (User) obj;
				if (user.getCode() == 200) {
					// 更新用户信息
					// UserInfos addFriend = new UserInfos();
					// addFriend.setUsername(user.getResult().getUsername());
					// addFriend.setUserid(user.getResult().getId());
					// addFriend.setPortrait(user.getResult().getPortrait());
					// addFriend.setStatus("0");
					// mUserInfosDao.insertOrReplace(addFriend);
				}
			}
		}
	}

	@Override
	public void onFailure(AbstractHttpRequest abstractHttpRequest,
			BaseException e) {

	}
}
