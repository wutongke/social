


















































/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.TabContainerActivity;
import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.facade.CmtRcmdFacade;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.facade.RecordChatFacade;
import com.cpstudio.zhuojiaren.facade.SysMsgFacade;
import com.cpstudio.zhuojiaren.helper.DownloadHelper;
import com.cpstudio.zhuojiaren.helper.DownloadHelper.FinishCallback;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.model.CmtRcmdVO;
import com.cpstudio.zhuojiaren.model.DownloadVO;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.ImQuanVO;
import com.cpstudio.zhuojiaren.model.PushMsgVO;
import com.cpstudio.zhuojiaren.model.SysMsgVO;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml.
 *用于对广播的notification消息的处理
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class NotificationReceiver extends BroadcastReceiver {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationReceiver.class);

	public NotificationReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
		String action = intent.getAction();
		Log.d(LOGTAG, "action=" + action);

		if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
			String notificationId = intent
					.getStringExtra(Constants.NOTIFICATION_ID);
			String notificationApiKey = intent
					.getStringExtra(Constants.NOTIFICATION_API_KEY);
			String notificationTitle = intent
					.getStringExtra(Constants.NOTIFICATION_TITLE);
			String notificationMessage = intent
					.getStringExtra(Constants.NOTIFICATION_MESSAGE);
			String notificationUri = intent
					.getStringExtra(Constants.NOTIFICATION_URI);

			Log.d(LOGTAG, "notificationId=" + notificationId);
			Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
			Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
			Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
			Log.d(LOGTAG, "notificationUri=" + notificationUri);
			boolean hasNew = false;

			if (null != notificationMessage && !notificationMessage.equals("")) {
				try {
					PushMsgVO pushMsg = JsonHandler
							.parsePushMsg(notificationMessage);
					List<CardMsgVO> cards = pushMsg.getExchangecard_msg();
					List<ImMsgVO> chatMsgs = pushMsg.getUserchats();
					List<ImQuanVO> quanChatMsgs = pushMsg.getGroupchat();
					List<SysMsgVO> sysMsgs = pushMsg.getSys_msg();
					List<CmtRcmdVO> cmtRcmds = pushMsg.getComment_msg();
					List<ImMsgVO> recordChatMsgs = pushMsg.getCloudchat();

					CardMsgFacade cardMsgFacade = new CardMsgFacade(context);
					ImChatFacade imChatFacade = new ImChatFacade(context);
					ImQuanFacade imQuanFacade = new ImQuanFacade(context);
					SysMsgFacade sysMsgFacade = new SysMsgFacade(context);
					CmtRcmdFacade cmtRcmdFacade = new CmtRcmdFacade(context);
					RecordChatFacade recordChatFacade = new RecordChatFacade(
							context);

					String message = "";
//dealXX这一步只是将消息保存到数据库中并设置未读标志，之后发送HTTP消息告诉服务器客户端已经缓存此未读消息
					//名片消息
					if (cards != null && cards.size() > 0) {
						message = dealCardMg(context, cardMsgFacade, cards);
						hasNew = true;
					}
					//系统消息
					if (sysMsgs != null && sysMsgs.size() > 0) {
						message = dealSysMg(context, sysMsgFacade, sysMsgs);
						hasNew = true;
					}
					//评论消息
					if (cmtRcmds != null && cmtRcmds.size() > 0) {
						message = dealCmtMg(context, cmtRcmdFacade, cmtRcmds);
						hasNew = true;
					}
					//圈子聊天消息
					if (quanChatMsgs != null && quanChatMsgs.size() > 0) {
						message = dealQuanMsg(context, imQuanFacade,
								quanChatMsgs);
						if (!message.equals("")) {
							hasNew = true;
						}
					}
					//个人聊天消息
					if (chatMsgs != null && chatMsgs.size() > 0) {
						message = dealChatMsg(context, imChatFacade, chatMsgs);
						if (!message.equals("")) {
							hasNew = true;
						}
					}
					//聊天记录的消息
					if (recordChatMsgs != null && recordChatMsgs.size() > 0) {
						dealRecordChatMsg(context, recordChatFacade,
								recordChatMsgs);
					}
					if (hasNew) {
	//这一步读取未读消息并提示到UI
						showMsg(context, message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}
	}

	private void showMsg(Context context, String message) {
		CardMsgFacade cardMsgFacade = new CardMsgFacade(context);
		ImChatFacade imChatFacade = new ImChatFacade(context);
		ImQuanFacade imQuanFacade = new ImQuanFacade(context);
		SysMsgFacade sysMsgFacade = new SysMsgFacade(context);
		CmtRcmdFacade cmtRcmdFacade = new CmtRcmdFacade(context);
		RecordChatFacade recordChatFacade = new RecordChatFacade(context);
		List<CardMsgVO> unreadCards = cardMsgFacade.getUnread();
		List<ImMsgVO> unreadChatMsgs = imChatFacade.getUnread();
		List<ImQuanVO> unreadQuanChatMsgs = imQuanFacade.getUnread();
//云录音消息
		List<ImMsgVO> unreadReocrdChatMsgs = recordChatFacade.getUnread();
		List<SysMsgVO> unreadSysMsgs = sysMsgFacade.getUnread();
		List<CmtRcmdVO> unreadCmtRcmds = cmtRcmdFacade.getUnread();
		int all = unreadCards.size() + unreadChatMsgs.size()
				+ unreadQuanChatMsgs.size() + unreadSysMsgs.size()
				+ unreadCmtRcmds.size();
		String intentId = null;
		String title = context.getString(R.string.app_name);
		int type = TabContainerActivity.MSG_LIST;
		String content = context.getString(R.string.info5) + all
				+ context.getString(R.string.info6);
		int alluser = 0;
		if (all > 0) {//所有推送消息的数目
			Intent chatIntent = new Intent("com.cpstudio.chatlist");
			context.sendBroadcast(chatIntent);
		}

		int ex = unreadReocrdChatMsgs.size();
		if (ex > 0) {
			Intent cloudIntent = new Intent("com.cpstudio.cloudchat");
			context.sendBroadcast(cloudIntent);
		}
		all += ex;
		if (all > 0) {
			if (all == unreadCards.size()) {
				type = TabContainerActivity.MSG_CARD;
				title = context.getString(R.string.label_cardname);
			} else if (all == unreadChatMsgs.size()) {
				for (ImMsgVO item : unreadChatMsgs) {
					if (intentId == null) {//只有一个人的消息，则显示姓名
						type = TabContainerActivity.MSG_IM;
						title = item.getSender().getUsername();
						intentId = item.getSender().getUserid();
					} else if (!title.equals(item.getSender().getUsername())) {
						type = TabContainerActivity.MSG_LIST;//多个人的消息则显示“倬家人”
						title = context.getString(R.string.app_name);
						intentId = null;
						break;
					}
				}
			} else if (all == unreadQuanChatMsgs.size()) {
				for (ImQuanVO item : unreadQuanChatMsgs) {
					if (intentId == null) {
						type = TabContainerActivity.MSG_QUAN;
						title = item.getGroup().getGname();
						intentId = item.getGroup().getGroupid();
					} else if (!title.equals(item.getGroup().getGname())) {
						type = TabContainerActivity.MSG_LIST_QUAN;
						title = context.getString(R.string.app_name);
						intentId = null;
						break;
					}
				}
			} else if (all == unreadSysMsgs.size()) {
				type = TabContainerActivity.MSG_SYS;
				title = context.getString(R.string.label_sysmsg);
			} else if (all == unreadCmtRcmds.size()) {
				type = TabContainerActivity.MSG_CMT;
				title = context.getString(R.string.label_cmtmsg);
			} else if (all == unreadReocrdChatMsgs.size()) {
				type = TabContainerActivity.MSG_CLOUD;
				title = context.getString(R.string.label_cloudvoice);
			}
			if (all == 1) {//只有某一类型的消息
				content = message;
			} else if (type == TabContainerActivity.MSG_LIST) {//多种类型的消息
				alluser = unreadCards.size() > 0 ? alluser + 1 : alluser;
				alluser = unreadChatMsgs.size() > 0 ? alluser + 1 : alluser;
				alluser = unreadQuanChatMsgs.size() > 0 ? alluser + 1 : alluser;
				alluser = unreadSysMsgs.size() > 0 ? alluser + 1 : alluser;
				alluser = unreadCmtRcmds.size() > 0 ? alluser + 1 : alluser;
				alluser = unreadReocrdChatMsgs.size() > 0 ? alluser + 1
						: alluser;
				content = alluser + context.getString(R.string.info82)
						+ content;
			}
//是否需要推送
			if (ResHelper.getInstance(context).isMsgList()) {
				return;
			}
			Notifier notifier = new Notifier(context);
			notifier.notify(title, content, message, type, intentId);
		}
	}
//每接收到一个推送消息都会HTTP告诉服务器
	private String dealCardMg(Context context, CardMsgFacade cardMsgFacade,
			List<CardMsgVO> cards) {
		String message = "";
		Set<String> ids = new HashSet<String>();
		boolean exist = false;
		for (CardMsgVO item : cards) {
			item.setIsread("0");
			if (cardMsgFacade.saveOrUpdate(item) > 0) {
				ids.add(item.getId());
			}
			if (!exist) {
				
				message = item.getSender().getUsername()
						+ context.getString(R.string.message_exchange_card);
				exist = true;
			}
		}
		if (ids.size() > 0) {
			String msgid = "";
			for (String id : ids) {
				msgid += id + ";";
			}
			msgid = ZhuoCommHelper.subLast(msgid);
			ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
			connHelper.msgRead(null, 0, null, msgid, "exchangecard_msg", true,
					null, null);
		}
		return message;
	}

	private String dealSysMg(Context context, SysMsgFacade sysMsgFacade,
			List<SysMsgVO> sysMsgs) {
		String message = "";
		Set<String> ids = new HashSet<String>();
		boolean exist = false;
		for (SysMsgVO item : sysMsgs) {
			if (sysMsgFacade.saveOrUpdate(item) > 0) {
				ids.add(item.getId());
			}
			if (!exist) {
				message = item.getSender().getUsername()
						+ context.getString(R.string.message_sys_message);
				exist = true;
			}
		}
		if (ids.size() > 0) {
			String msgid = "";
			for (String id : ids) {
				msgid += id + ";";
			}
			msgid = ZhuoCommHelper.subLast(msgid);
			ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
			connHelper.msgRead(null, 0, null, msgid, "sys_msg", true, null,
					null);
		}
		return message;
	}

	private String dealCmtMg(Context context, CmtRcmdFacade cmtRcmdFacade,
			List<CmtRcmdVO> cmtRcmds) {
		String message = "";
		Set<String> ids = new HashSet<String>();
		boolean exist = false;
		for (CmtRcmdVO item : cmtRcmds) {
			if (cmtRcmdFacade.saveOrUpdate(item) > 0) {
				ids.add(item.getId());
			}
			if (!exist) {
				message = item.getSender().getUsername() + ":"
						+ item.getContent();
				exist = true;
			}
		}
		if (ids.size() > 0) {
			String msgid = "";
			for (String id : ids) {
				msgid += id + ";";
			}
			msgid = ZhuoCommHelper.subLast(msgid);
			ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
			connHelper.msgRead(null, 0, null, msgid, "comment_msg", true, null,
					null);
//评论消息还进行了广播到评论Activity，使得其可以实时更新
			Intent intent = new Intent("com.cpstudio.rcmdcmt");
			context.sendBroadcast(intent);
		}
		return message;
	}

	private String dealQuanMsg(Context context, ImQuanFacade imQuanFacade,
			List<ImQuanVO> quanChatMsgs) {
		ResHelper resHelper = ResHelper.getInstance(context);
		String imagePath = resHelper.getChatImagePath();
		String voicePath = resHelper.getVoicePath();
		String message = "";
		Set<String> ids = new HashSet<String>();
		boolean exist = false;
		for (ImQuanVO item : quanChatMsgs) {
			if (item.getSender() == null) {
				ids.add(item.getId());
			}
			String msgTemp = "";
			if (item.getFile() != null && !item.getFile().equals("")) {
				item.setIsread("3");
			}
			if (imQuanFacade.saveOrUpdate(item) > 0) {
				if (item.getFile() != null && !item.getFile().equals("")) {
					String url = null;
					String name = null;
					String savePath = null;
					if (item.getType().equals("voice")) {
						url = item.getFile();
						if (url == null) {
							continue;
						}
						url += ".amr";
						name = System.currentTimeMillis() + ".amr";
						savePath = voicePath;
					} else if (item.getType().equals("image")) {
						url = item.getFile();
						name = System.currentTimeMillis() + ".jpg";
						savePath = imagePath;
					}
					FinishCallback callback = getQuanChatCallback(context);
					DownloadHelper helper = new DownloadHelper(url, savePath,
							name, callback, item.getId());
					helper.execute();
				} else {
					if (item.getGroup()
							.getGroupid()
							.equals(ResHelper.getInstance(context)
									.getChatgroup())) {
						Intent chatIntent = new Intent(
								"com.cpstudio.groupchat."
										+ item.getGroup().getGroupid());
						context.sendBroadcast(chatIntent);
					}
					Intent intent = new Intent("com.cpstudio.groupchat");
					context.sendBroadcast(intent);
					ids.add(item.getId());
					if (!exist) {
						if (item.getType().equals("card")) {
							msgTemp = item.getSender().getUsername()
									+ "("
									+ item.getGroup().getGname()
									+ ")"
									+ context.getString(R.string.label_sended)
									+ item.getContent().split("____")[1]
									+ context
											.getString(R.string.label_sendedcardtoall);
						} else if (item.getType().equals("emotion")) {
							msgTemp = item.getSender().getUsername() + "("
									+ item.getGroup().getGname() + ")"
									+ context.getString(R.string.label_sended)
									+ context.getString(R.string.label_emotion);
						} else {
							msgTemp = item.getSender().getUsername() + "("
									+ item.getGroup().getGname() + "):"
									+ item.getContent();
						}
						exist = true;
					}
				}
			}
			if (!msgTemp.equals("")) {
				message = msgTemp;
			}
		}
		if (ids.size() > 0) {
			String msgid = "";
			for (String id : ids) {
				msgid += id + ";";
			}
			msgid = ZhuoCommHelper.subLast(msgid);
			ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
			connHelper.msgRead(null, 0, null, msgid, "groupchat", true, null,
					null);
		}
		return message;

	}

	private String dealChatMsg(Context context, ImChatFacade imChatFacade,
			List<ImMsgVO> chatMsgs) {
		ResHelper resHelper = ResHelper.getInstance(context);
		String imagePath = resHelper.getChatImagePath();
		String voicePath = resHelper.getVoicePath();
		String message = "";
		Set<String> ids = new HashSet<String>();
		boolean exist = false;
		for (ImMsgVO item : chatMsgs) {
			if (item.getSender() == null) {
				ids.add(item.getId());
			}
			String msgTemp = "";
			if (item.getFile() != null && !item.getFile().equals("")) {
				item.setIsread("3");
			}
			if (imChatFacade.saveOrUpdate(item) > 0) {
				if (item.getFile() != null && !item.getFile().equals("")) {
					String url = null;
					String name = null;
					String savePath = null;
					if (item.getType().equals("voice")) {
						url = item.getFile();
						if (url == null) {
							continue;
						}
						url += ".amr";
						name = System.currentTimeMillis() + ".amr";
						savePath = voicePath;
					} else if (item.getType().equals("image")) {
						url = item.getFile();
						name = System.currentTimeMillis() + ".jpg";
						savePath = imagePath;
					}
					FinishCallback callback = getUserChatCallback(context);
					DownloadHelper helper = new DownloadHelper(url, savePath,
							name, callback, item.getId());
					helper.execute();
				} else {
					ids.add(item.getId());
					if (!exist) {
						if (item.getType().equals("card")) {
							msgTemp = item.getSender().getUsername()
									+ context.getString(R.string.label_sended)
									+ item.getContent().split("____")[1]
									+ context
											.getString(R.string.label_sendedcardtome);
						} else if (item.getType().equals("emotion")) {
							msgTemp = item.getSender().getUsername()
									+ context.getString(R.string.label_sended)
									+ context.getString(R.string.label_emotion);
						} else {
							msgTemp = item.getSender().getUsername() + ":"
									+ item.getContent();

						}
						exist = true;
					}
					if (item.getSender()
							.getUserid()
							.equals(ResHelper.getInstance(context)
									.getChatuser())) {
						Intent chatIntent = new Intent("com.cpstudio.userchat."
								+ item.getSender().getUserid());
						context.sendBroadcast(chatIntent);
						msgTemp = "";
					}
				}
			}
			if (!msgTemp.equals("")) {
				message = msgTemp;
			}
		}
		if (ids.size() > 0) {
			String msgid = "";
			for (String id : ids) {
				msgid += id + ";";
			}
			msgid = ZhuoCommHelper.subLast(msgid);
			ZhuoConnHelper.getInstance(context).msgRead(null, 0, null, msgid,
					"userchat", true, null, null);
		}
		return message;
	}

	private void dealRecordChatMsg(Context context,
			RecordChatFacade recordChatFacade, List<ImMsgVO> recordChatMsgs) {
		ResHelper resHelper = ResHelper.getInstance(context);
		String voicePath = resHelper.getVoicePath();
		for (ImMsgVO item : recordChatMsgs) {
			if (item.getFile() != null && !item.getFile().equals("")) {
				item.setIsread("3");
			}
			if (recordChatFacade.saveOrUpdate(item) > 0) {
				if (item.getFile() != null && !item.getFile().equals("")) {
					String url = null;
					String name = null;
					String savePath = null;
					if (item.getType().equals("cloudvoice")) {
						url = item.getFile();
						if (url == null) {
							continue;
						}
						url += ".amr";
						name = System.currentTimeMillis() + ".amr";
						savePath = voicePath;
					}
					FinishCallback callback = getCloudChatCallback(context);
					DownloadHelper helper = new DownloadHelper(url, savePath,
							name, callback, item.getId());
					helper.execute();
				}
			}
		}
	}

	private FinishCallback getCloudChatCallback(final Context context) {
		return new FinishCallback() {

			@Override
			public boolean onReturn(String rs) {
				System.out.println(JsonHandler.checkResult(rs));
				String message = "";
				JsonHandler jsonHandler = new JsonHandler(rs);
				DownloadVO downloadVO = jsonHandler.parseDownload();
				String msgid = downloadVO.getId();
				String savepath = downloadVO.getSavepath();
				ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
				connHelper.msgRead(null, 0, null, msgid, "cloudchat", true,
						null, null);
				RecordChatFacade facade = new RecordChatFacade(context);
				ImMsgVO item = facade.getById(msgid);
				item.setSavepath(savepath);
				item.setIsread("0");
				facade.update(item);
				String userid = item.getSender().getUserid();
				if (userid.equals(ResHelper.getInstance(context).getChatuser())) {
					Intent chatIntent = new Intent("com.cpstudio.cloudchat."
							+ userid);
					context.sendBroadcast(chatIntent);
				}
				message = item.getSender().getUsername() + ":"
						+ context.getString(R.string.message_cloudvoice);
				showMsg(context, message);
				return false;
			}
		};
	}

	private FinishCallback getUserChatCallback(final Context context) {
		return new FinishCallback() {

			@Override
			public boolean onReturn(String rs) {
				System.out.println(JsonHandler.checkResult(rs));
				String message = "";
				JsonHandler jsonHandler = new JsonHandler(rs);
				DownloadVO downloadVO = jsonHandler.parseDownload();
				String msgid = downloadVO.getId();
				String savepath = downloadVO.getSavepath();
				ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
				connHelper.msgRead(null, 0, null, msgid, "userchat", true,
						null, null);
				ImChatFacade facade = new ImChatFacade(context);
				ImMsgVO item = facade.getById(msgid);
				item.setSavepath(savepath);
				item.setIsread("0");
				facade.update(item);
				String userid = item.getSender().getUserid();
				if (userid.equals(ResHelper.getInstance(context).getChatuser())) {
					Intent chatIntent = new Intent("com.cpstudio.userchat."
							+ userid);
					context.sendBroadcast(chatIntent);
				} else {
					if (item.getType().equals("voice")) {
						message = item.getSender().getUsername() + ":"
								+ context.getString(R.string.message_voice);
					} else if (item.getType().equals("image")) {
						message = item.getSender().getUsername() + ":"
								+ context.getString(R.string.message_photo);
					}
				}
				if (!message.equals("")) {
					showMsg(context, message);
				}
				return false;
			}
		};
	}

	private FinishCallback getQuanChatCallback(final Context context) {
		return new FinishCallback() {

			@Override
			public boolean onReturn(String rs) {
				System.out.println(JsonHandler.checkResult(rs));
				String message = "";
				JsonHandler jsonHandler = new JsonHandler(rs);
				DownloadVO downloadVO = jsonHandler.parseDownload();
				String msgid = downloadVO.getId();
				String savepath = downloadVO.getSavepath();
				ZhuoConnHelper connHelper = ZhuoConnHelper.getInstance(context);
				connHelper.msgRead(null, 0, null, msgid, "groupchat", true,
						null, null);
				ImQuanFacade facade = new ImQuanFacade(context);
				ImQuanVO item = facade.getById(msgid);
				item.setSavepath(savepath);
				item.setIsread("0");
				facade.update(item);
				String groupid = item.getGroup().getGroupid();
				if (groupid.equals(ResHelper.getInstance(context)
						.getChatgroup())) {
					Intent chatIntent = new Intent("com.cpstudio.groupchat."
							+ groupid);
					context.sendBroadcast(chatIntent);
				} else {
					if (item.getType().equals("voice")) {
						message = item.getSender().getUsername() + "("
								+ item.getGroup().getGname() + "):"
								+ context.getString(R.string.message_voice);
					} else if (item.getType().equals("image")) {
						message = item.getSender().getUsername() + "("
								+ item.getGroup().getGname() + "):"
								+ context.getString(R.string.message_photo);
					}
				}
				Intent intent = new Intent("com.cpstudio.groupchat");
				context.sendBroadcast(intent);
				if (!message.equals("")) {
					showMsg(context, message);
				}
				return false;
			}
		};
	}

}