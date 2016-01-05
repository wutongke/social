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
 * ����SDK�¼��������� ���¼�ͳһ���������߿�ֱ�Ӹ��Ƶ��Լ�����Ŀ��ȥʹ�á�
 * <p/>
 * ��������ļ����¼��У� 1����Ϣ��������OnReceiveMessageListener��
 * 2��������Ϣ��������OnSendMessageListener�� 3���û���Ϣ�ṩ�ߣ�GetUserInfoProvider��
 * 4��������Ϣ�ṩ�ߣ�GetFriendsProvider�� 5��Ⱥ����Ϣ�ṩ�ߣ�GetGroupInfoProvider�� ��c
 * 7������״̬���������Ի�ȡ�������״̬��ConnectionStatusListener�� 8������λ���ṩ�ߣ�LocationProvider��
 * 9���Զ��� push ֪ͨ�� OnReceivePushMessageListener��
 * 10���Ự�б��������ļ�������ConversationListBehaviorListener��
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
	 * ��ʼ�� RongCloud.
	 * 
	 * @param context
	 *            �����ġ�
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
	 * ���췽����
	 * 
	 * @param context
	 *            �����ġ�
	 */
	private RongCloudEvent(Context context) {
		mContext = context;
		initDefaultListener();
	}

	/**
	 * RongIM.init(this) ��ֱ�ӿ�ע���Listener��
	 */
	private void initDefaultListener() {
		RongIM.setUserInfoProvider(this, true);// �����û���Ϣ�ṩ�ߡ�
		RongIM.setGroupInfoProvider(this, true);// ����Ⱥ����Ϣ�ṩ�ߡ�
		RongIM.setConversationBehaviorListener(this);// ���ûỰ��������ļ�������
		RongIM.setLocationProvider(this);// ���õ���λ���ṩ��,����λ�õ�ͬѧ����ע�����д���
		RongIM.setOnReceivePushMessageListener(this);
		RongIM.setOnReceiveMessageListener(this);
	}

	/*
	 * ���ӳɹ�ע�ᡣ <p/> ��RongIM-connect-onSuccess����á�
	 */
	public void setOtherListener() {
		RongIM.getInstance().getRongIMClient()
				.setOnReceiveMessageListener(this);// ������Ϣ���ռ�������
		RongIM.getInstance().setSendMessageListener(this);// ���÷�����Ϣ���ռ�����.
		RongIM.getInstance().getRongIMClient()
				.setConnectionStatusListener(this);// ��������״̬��������

		// ��չ�����Զ���
		InputProvider.ExtendProvider[] provider = {
				new ImageInputProvider(RongContext.getInstance()),// ͼƬ
				new CameraInputProvider(RongContext.getInstance()),// ���
		// new LocationInputProvider(RongContext.getInstance()),// ����λ��
		// new VoIPInputProvider(RongContext.getInstance()),// ����ͨ��
		// new LZExtendProvider(RongContext.getInstance()) // �Զ���ͨѶ¼
		};
		RongIM.getInstance().resetInputExtensionProvider(
				Conversation.ConversationType.PRIVATE, provider);

	}

	/**
	 * �Զ��� push ֪ͨ�� �� onReceivePushMessage ���� true ���Ʋ�����֪ͨ������֪ͨ����Ҫ���������Ѵ���
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
		// .getApplicationInfo().icon, "�Զ��� notification",
		// System.currentTimeMillis());
		//
		// notification.setLatestEventInfo(RongContext.getInstance(),
		// "�Զ��� title", "���� Content:" + msg.getObjectName(),
		// pendingIntent);
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		// notification.defaults = Notification.DEFAULT_SOUND;
		// } else {
		//
		// notification = new Notification.Builder(RongContext.getInstance())
		// .setLargeIcon(getAppIcon())
		// .setSmallIcon(R.drawable.ic_launcher)
		// .setTicker("�Զ��� notification").setContentTitle("�Զ��� title")
		// .setContentText("���� Content:" + msg.getObjectName())
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
	 * ��ȡRongCloud ʵ����
	 * 
	 * @return RongCloud��
	 */
	public static RongCloudEvent getInstance() {
		return mRongCloudInstance;
	}

	/**
	 * ������Ϣ�ļ�������OnReceiveMessageListener �Ļص����������յ���Ϣ��ִ�С�
	 * ������Ϣ��������ʵ�֣����н��յ�����Ϣ��֪ͨ��״̬�����ɴ˴����õļ�������������˽����Ϣ����������Ϣ��Ⱥ����Ϣ����������Ϣ�Լ�����״̬��
	 * 
	 * @param message
	 *            ���յ�����Ϣ��ʵ����Ϣ��
	 * @param left
	 *            ʣ��δ��ȡ��Ϣ��Ŀ�� // �յ���Ϣ�Ƿ�����ɣ�true ��ʾ�����ѵĴ���ʽ��false ������Ĭ�ϴ���ʽ��
	 */
	@Override
	public boolean onReceived(Message message, int left) {

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// �ı���Ϣ
			TextMessage textMessage = (TextMessage) messageContent;
			if (((TextMessage) messageContent).getExtra() != null) {
				handleCustomMessage((TextMessage) messageContent);
				return true;
			}
			Log.d(TAG, "onReceived-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// ͼƬ��Ϣ
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// ������Ϣ
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onReceived-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// ͼ����Ϣ
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onReceived-RichContentMessage:"
							+ richContentMessage.getContent());
		} else if (messageContent instanceof InformationNotificationMessage) {// С������Ϣ
			InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) messageContent;
			Log.d(TAG, "onReceived-informationNotificationMessage:"
					+ informationNotificationMessage.getMessage());
		} else if (messageContent instanceof DeAgreedFriendRequestMessage) {// ������ӳɹ���Ϣ
			DeAgreedFriendRequestMessage deAgreedFriendRequestMessage = (DeAgreedFriendRequestMessage) messageContent;
			Log.d(TAG, "onReceived-deAgreedFriendRequestMessage:"
					+ deAgreedFriendRequestMessage.getMessage());
			receiveAgreeSuccess(deAgreedFriendRequestMessage);
		} else if (messageContent instanceof ContactNotificationMessage) {// ���������Ϣ
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
			Log.d(TAG, "onReceived-������Ϣ���Լ����жϴ���");
		}
		return false;
	}

	// �ı���Ϣ��extra�ֶβ�λ����Ϊ�Զ������Ϣ�����յ����ޣ����۵ȵ�������Ϣ(�Ƿ���ʵ�֣�)
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
		// ��ʱע�� if (DemoContext.getInstance() != null) {
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
	 * ��Ϣ��UIչʾ��ִ��/�Լ�����Ϣ������ִ��,���۳ɹ���ʧ�ܡ�
	 * 
	 * @param message
	 *            ��Ϣ��
	 */
	@Override
	public boolean onSent(Message message,
			RongIM.SentMessageErrorCode sentMessageErrorCode) {

		if (message.getSentStatus() == Message.SentStatus.FAILED) {

			if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {// ����������

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {// ����������

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {// ����Ⱥ��

			} else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {// �������ĺ�������

			}
		}

		MessageContent messageContent = message.getContent();

		if (messageContent instanceof TextMessage) {// �ı���Ϣ
			TextMessage textMessage = (TextMessage) messageContent;
			Log.d(TAG, "onSent-TextMessage:" + textMessage.getContent());
		} else if (messageContent instanceof ImageMessage) {// ͼƬ��Ϣ
			ImageMessage imageMessage = (ImageMessage) messageContent;
			Log.d(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
		} else if (messageContent instanceof VoiceMessage) {// ������Ϣ
			VoiceMessage voiceMessage = (VoiceMessage) messageContent;
			Log.d(TAG, "onSent-voiceMessage:"
					+ voiceMessage.getUri().toString());
		} else if (messageContent instanceof RichContentMessage) {// ͼ����Ϣ
			RichContentMessage richContentMessage = (RichContentMessage) messageContent;
			Log.d(TAG,
					"onSent-RichContentMessage:"
							+ richContentMessage.getContent());
		} else {
			Log.d(TAG, "onSent-������Ϣ���Լ����жϴ���");
		}
		return false;
	}

	/**
	 * �û���Ϣ���ṩ�ߣ�GetUserInfoProvider �Ļص���������ȡ�û���Ϣ��
	 * 
	 * @param userId
	 *            �û� Id��
	 * @return �û���Ϣ����ע���ɿ������ṩ�û���Ϣ����
	 */
	@Override
	public UserInfo getUserInfo(String userId) {
		UserFacade mUserInfosDao = DemoContext.getInstance(mContext)
				.getmUserInfosDao();
		if (mUserInfosDao == null
				|| mUserInfosDao.getSimpleInfoById(userId) == null) {
			// ���ݿ��в����ڣ���������
			Log.i("cloudevent", (mUserInfosDao == null) + "," + "���Ѳ�����");
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
	 * Ⱥ����Ϣ���ṩ�ߣ�GetGroupInfoProvider �Ļص������� ��ȡȺ����Ϣ��
	 * 
	 * @param groupId
	 *            Ⱥ�� Id.
	 * @return Ⱥ����Ϣ����ע���ɿ������ṩȺ����Ϣ����
	 */
	@Override
	public Group getGroupInfo(String groupId) {
		GroupFacade mGroupInfosDao = DemoContext.getInstance(mContext)
				.getmGroupInfoDao();
		if (mGroupInfosDao == null
				|| mGroupInfosDao.getSimpleInfoById(groupId) == null) {
			// ���ݿ��в����ڣ���������
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
	 * �Ự��������ļ�������ConversationBehaviorListener �Ļص�������������û�ͷ���ִ�С�
	 * 
	 * @param context
	 *            Ӧ�õ�ǰ�����ġ�
	 * @param conversationType
	 *            �Ự���͡�
	 * @param user
	 *            ��������û�����Ϣ��
	 * @return ����True��ִ�к���SDK����������False����ִ��SDK������
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
		// * demo ���� ���������滻���Լ��Ĵ��롣
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
	 * �Ự��������ļ�������ConversationBehaviorListener �Ļص��������������Ϣʱִ�С�
	 * 
	 * @param context
	 *            Ӧ�õ�ǰ�����ġ�
	 * @param message
	 *            ���������Ϣ��ʵ����Ϣ��
	 * @return ����True��ִ�к���SDK����������False����ִ��SDK������
	 */
	@Override
	public boolean onMessageClick(Context context, View view, Message message) {
		Log.d(TAG, "onMessageClick");

		// /**
		// * demo ���� ���������滻���Լ��Ĵ��롣
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
	 * ����״̬���������Ի�ȡ�������״̬:ConnectionStatusListener �Ļص�����������״̬�仯ʱִ�С�
	 * 
	 * @param status
	 *            ����״̬��
	 */
	@Override
	public void onChanged(ConnectionStatus status) {
		Log.d(TAG, "onChanged:" + status);
		if (status.getMessage().equals(
				ConnectionStatus.DISCONNECTED.getMessage())) {
		}
	}

	/**
	 * λ����Ϣ�ṩ��:LocationProvider �Ļص��������򿪵�������ͼҳ�档
	 * 
	 * @param context
	 *            ������
	 * @param callback
	 *            �ص�
	 */
	@Override
	public void onStartLocation(Context context, LocationCallback callback) {
		// /**
		// * demo ���� ���������滻���Լ��Ĵ��롣
		// */
		// DemoContext.getInstance().setLastLocationCallback(callback);
		// context.startActivity(new Intent(context,
		// SOSOLocationActivity.class));//SOSO��ͼ
	}

	/**
	 * ����Ự�б� item ��ִ�С�
	 * 
	 * @param context
	 *            �����ġ�
	 * @param view
	 *            ��������� View��
	 * @param conversation
	 *            �Ự��Ŀ��
	 * @return ���� true ����ִ������ SDK �߼������� false ��ִ������ SDK �߼���ִ�и÷�����
	 */
	@Override
	public boolean onConversationClick(Context context, View view,
			UIConversation conversation) {
		return false;
	}

	/**
	 * �����Ự�б� item ��ִ�С�
	 * 
	 * @param context
	 *            �����ġ�
	 * @param view
	 *            ��������� View��
	 * @param conversation
	 *            �����Ự��Ŀ��
	 * @return ���� true ����ִ������ SDK �߼������� false ��ִ������ SDK �߼���ִ�и÷�����
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
					// �����û���Ϣ
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
