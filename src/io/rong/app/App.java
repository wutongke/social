package io.rong.app;

import java.util.concurrent.ExecutionException;

import cn.trinea.android.common.service.impl.ImageSDCardCache;
import cn.trinea.android.common.util.CacheManager;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.socialize.sso.CustomHandler;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.app.message.DeContactNotificationMessageProvider;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 2015/1/30.
 */
public class App extends Application {
	public static final ImageSDCardCache IMAGE_SD_CACHE = CacheManager
			.getImageSDCardCache();

	@Override
	public void onCreate() {

		super.onCreate();
		/**
		 * 百度地图初始化
		 */
		SDKInitializer.initialize(this);

		initRong();

		// /**
		// * IMKit SDK调用第一步 初始化 context上下文
		// */
		// RongIM.init(this);
		// /**
		// * d 融云SDK事件监听处理
		// */
		// RongCloudEvent.init(this);
		//
		// // DemoContext.init(this);
		//
		// // 注册消息类型的时候判断当前的进程是否在主进程
		// if ("com.cpstudio.zhuojiaren"
		// .equals(getCurProcessName(getApplicationContext()))) {
		// try {
		// // 注册自定义消息,注册完消息后可以收到自定义消息
		// RongIM.registerMessageType(DeAgreedFriendRequestMessage.class);
		// // //注册消息模板，注册完消息模板可以在会话列表上展示
		// // RongIM.registerMessageTemplate(new
		// // DeContactNotificationMessageProvider());
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// Crash 日志
		Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(
				this));

	}

	void initRong() {
		/**
		 * IMKit SDK调用第一步 初始化 context上下文
		 */
		RongIM.init(this);
		/**
		 * d 融云SDK事件监听处理
		 */
		RongCloudEvent.init(this);

		// DemoContext.init(this);
		if ("io.rong.app".equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurProcessName(getApplicationContext()))) {
			RongIM.init(this);
			/**
			 * 融云SDK事件监听处理，注册相关代码，只需要在主进程中做
			 */
			if ("io.rong.app"
					.equals(getCurProcessName(getApplicationContext()))) {
				RongCloudEvent.init(this);
				// DemoContext.init(this);
				// 可以使得程序不会异常终止，但不好调试
				// Thread.setDefaultUncaughtExceptionHandler(new
				// RongExceptionHandler(this));
				// try{
				// RongIM.registerMessageType(CustomMessage.class);
				// RongIM.registerMessageType(DeAgreeeFriendRequestMessage.class);
				// RongIM.registerMessageTemplate(new
				// DeContectNotificationMessageProvider());
				// }
				// catch(ExecutionException exception)
				// exception.printStackTrace;
			}
		}
	}

	/**
	 * 获得当前进程号
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurProcessName(Context context) {
		int pid = android.os.Process.myPid();
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {
			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}

}
