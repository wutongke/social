package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.TabContainerActivity;
import com.cpstudio.zhuojiaren.broadcastreceiver.ContentPoller;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.ImQuanVO;

public class NoticeSetting {
	private int mNum;
	private int mNum2;
	private NotificationManager nm;
	private TimerTask timertask;
	private Timer timer;
	private Context mContext;

	public NoticeSetting(Context context, String loginName) {
		mContext = context;
	}

	/**
	 * <i>public void setNotice(boolean vibrate,boolean sound,boolean
	 * led,Context context)</i>
	 * 
	 * @param vibrate
	 *            Set the vibrating alert
	 * @param sound
	 *            Set voice alert
	 * @param led
	 *            Set LED alert
	 * @param context
	 *            activity name
	 * @return Returns true if this is successful else return false
	 */
	private boolean setUserNotice(boolean vibrate, boolean sound, boolean led,
			Context context) {
		int all = 0;
		ImQuanFacade imGroupChatFacade = new ImQuanFacade(context);
		ArrayList<ImQuanVO> list = imGroupChatFacade.getAll();
		try {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIsread().equals("0")) {
					all++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (all > mNum) {
			startNotice(vibrate, sound, led, context, 1, all);
			mNum = all;
			return true;
		}
		return false;
	}

	private boolean setMsgNotice(boolean vibrate, boolean sound, boolean led,
			Context context) {
		int all = 0;
		ImChatFacade msgFacade = new ImChatFacade(context);
		ArrayList<ImMsgVO> list = msgFacade.getAll();
		try {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getIsread().equals("0")) {
					all++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (all > mNum2) {
			startNotice(vibrate, sound, led, context, 2, all);
			mNum2 = all;
			return true;
		}
		return false;
	}

	public void startNotification() {
		ResHelper pu = ResHelper.getInstance(mContext);
		final boolean vibrate = pu.getVibrate();
		final boolean sound = pu.getSound();
		final boolean led = pu.getLed();
		int runtime = pu.getRuntime();
		AlarmManager am = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		Intent ii = new Intent(mContext, ContentPoller.class);
		PendingIntent pii = PendingIntent.getBroadcast(mContext, 0, ii, 0);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime(), runtime, pii);
		if (timer != null) {
			timertask.cancel();
			timer.cancel();
		}
		timer = new Timer();
		timertask = new TimerTask() {
			@Override
			public void run() {
				setUserNotice(vibrate, sound, led, mContext);
				setMsgNotice(vibrate, sound, led, mContext);
			}
		};
		timer.schedule(timertask, 10, runtime);
	}

	@SuppressWarnings("deprecation")
	private void startNotice(boolean vibrate, boolean sound, boolean led,
			Context context, int id, int num) {
		Intent intent = null;
		Notification n = null;
		PendingIntent contentIntent;
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (id == 1) {
			intent = new Intent(context, TabContainerActivity.class);
			intent.putExtra("type", "userlist");
			contentIntent = PendingIntent.getActivity(context, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			n = new Notification(R.drawable.newmsg, num
					+ context.getString(R.string.info3),
					System.currentTimeMillis());
			n.setLatestEventInfo(
					context,
					context.getString(R.string.info4),
					context.getString(R.string.info5) + num
							+ context.getString(R.string.info6), contentIntent);
		}else if(id == 2){
			intent = new Intent(context, TabContainerActivity.class);
			intent.putExtra("type", "msglist");
			contentIntent = PendingIntent.getActivity(context, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			n = new Notification(R.drawable.newmsg, num
					+ context.getString(R.string.info3),
					System.currentTimeMillis());
			n.setLatestEventInfo(
					context,
					context.getString(R.string.info4),
					context.getString(R.string.info5) + num
							+ context.getString(R.string.info6), contentIntent);
		}
		if (vibrate)
			n.vibrate = new long[] { 100, 250, 100, 500 };
		if (sound)
			n.defaults |= Notification.DEFAULT_SOUND;
		if (led) {
			n.ledARGB = 0xff00ff00;
			n.ledOnMS = 300;
			n.ledOffMS = 1000;
			n.flags |= Notification.FLAG_SHOW_LIGHTS;
		}
		n.flags |= Notification.FLAG_AUTO_CANCEL;
		nm.cancel(id);
		nm.notify(id, n);
	}
}
