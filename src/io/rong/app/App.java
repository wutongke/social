package io.rong.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;


import io.rong.app.message.DeAgreedFriendRequestMessage;
import io.rong.app.message.DeContactNotificationMessageProvider;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Bob on 2015/1/30.
 */
public class App extends Application {

    @Override
    public void onCreate() {

        super.onCreate();
        /**
         * IMKit SDK调用第一步 初始化
         * context上下文
         */
        RongIM.init(this);
        /**d
         * 融云SDK事件监听处理
         */
        RongCloudEvent.init(this);

//        DemoContext.init(this);

        //注册消息类型的时候判断当前的进程是否在主进程
        if ("io.rong.app".equals(getCurProcessName(getApplicationContext()))) {
            try {
                //注册自定义消息,注册完消息后可以收到自定义消息
                RongIM.registerMessageType(DeAgreedFriendRequestMessage.class);
//                //注册消息模板，注册完消息模板可以在会话列表上展示
//                RongIM.registerMessageTemplate(new DeContactNotificationMessageProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Crash 日志
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));


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
