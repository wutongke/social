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

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.cpstudio.zhuojiaren.MsgQuanListActivity;
import com.cpstudio.zhuojiaren.MsgRcmdCmtActivity;
import com.cpstudio.zhuojiaren.MsgSysListActivity;
import com.cpstudio.zhuojiaren.QuanBoardChatActivity;
import com.cpstudio.zhuojiaren.TabContainerActivity;

/**
 * This class is to notify the user of messages with NotificationManager.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class Notifier {

	private static final String LOGTAG = LogUtil.makeLogTag(Notifier.class);

	private static final Random random = new Random(System.currentTimeMillis());

	private Context context;

	private SharedPreferences sharedPrefs;

	private NotificationManager notificationManager;

	public Notifier(Context context) {
		this.context = context;
		this.sharedPrefs = context.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		this.notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@SuppressWarnings("deprecation")
	public void notify(String title, String content, String ticker, int type,
			String intentId) {
		Log.d(LOGTAG, "notify()...");

		if (isNotificationEnabled()) {
			if (isNotificationToastEnabled()) {

			}
			Notification notification = new Notification();
			notification.icon = getNotificationIcon();
			notification.defaults = Notification.DEFAULT_LIGHTS;
			if (isNotificationSoundEnabled()) {
				notification.defaults |= Notification.DEFAULT_SOUND;
			}
			if (isNotificationVibrateEnabled()) {
				notification.defaults |= Notification.DEFAULT_VIBRATE;
			}
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			notification.when = System.currentTimeMillis();
			notification.tickerText = ticker;
			Intent intent = initIntent(type, intentId, context);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, title, content,
					pendingIntent);
			clearNotify();
			notificationManager.notify(random.nextInt(), notification);
		} else {
			Log.w(LOGTAG, "Notificaitons disabled.");
		}
	}

	private Intent initIntent(int type, String intentId, Context context) {
		Intent intent = new Intent();
		intent.putExtra("toTab", true);
		switch (type) {
		case TabContainerActivity.MSG_CARD:
//			intent.setClass(context, MsgCardListActivity.class);
			break;
		case TabContainerActivity.MSG_CMT:
			intent.setClass(context, MsgRcmdCmtActivity.class);
			break;
		case TabContainerActivity.MSG_IM:
//			intent.setClass(context, ChatActivity.class);
			intent.putExtra("userid", intentId);
			break;
		case TabContainerActivity.MSG_LIST_QUAN:
			intent.setClass(context, MsgQuanListActivity.class);
			break;
		case TabContainerActivity.MSG_QUAN:
			intent.setClass(context, QuanBoardChatActivity.class);
			intent.putExtra("groupid", intentId);
			intent.putExtra("type", "chat");
			break;
		case TabContainerActivity.MSG_SYS:
			intent.setClass(context, MsgSysListActivity.class);
			break;
		case TabContainerActivity.MSG_LIST:
			intent.setClass(context, TabContainerActivity.class);
			intent.putExtra(TabContainerActivity.SHOW_PAGE,
					TabContainerActivity.MSG_PAGE);
			break;
		case TabContainerActivity.MSG_CLOUD:
//			intent.setClass(context, RecordListActivity.class);
			break;
		}
		return intent;
	}

	public void clearNotify() {
		notificationManager.cancelAll();
	}

	private int getNotificationIcon() {
		return sharedPrefs.getInt(Constants.NOTIFICATION_ICON, 0);
	}

	private boolean isNotificationEnabled() {
		return sharedPrefs.getBoolean(Constants.SETTINGS_NOTIFICATION_ENABLED,
				true);
	}

	private boolean isNotificationSoundEnabled() {
		return sharedPrefs.getBoolean(Constants.SETTINGS_SOUND_ENABLED, true);
	}

	private boolean isNotificationVibrateEnabled() {
		return sharedPrefs.getBoolean(Constants.SETTINGS_VIBRATE_ENABLED, true);
	}

	private boolean isNotificationToastEnabled() {
		return sharedPrefs.getBoolean(Constants.SETTINGS_TOAST_ENABLED, false);
	}

}
