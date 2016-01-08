package io.rong.app;

import io.rong.imkit.RongIM;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by Bob on 2015/1/30.
 */
public class App extends Application {
	@Override
	public void onCreate() {

		super.onCreate();
		/**
		 * 百度地图初始化
		 */
		SDKInitializer.initialize(this);

		initRong();

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
