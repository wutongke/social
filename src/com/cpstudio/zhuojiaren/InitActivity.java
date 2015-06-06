package com.cpstudio.zhuojiaren;

import java.util.HashMap;

import org.androidpn.client.ServiceManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.facade.ImQuanFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;

public class InitActivity extends Activity {

	private String mUserid = null;
	private ZhuoConnHelper connHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		DeviceInfoUtil.setWidth(metric.widthPixels);
		init();
	}

	private void init() {
		// 单人聊
		ImChatFacade imChatFacade = new ImChatFacade(getApplicationContext());
		imChatFacade.updateSendState();
		// 群聊
		ImQuanFacade imQuanFacade = new ImQuanFacade(getApplicationContext());
		imQuanFacade.updateSendState();
		start();
	}

	private void start() {
		ResHelper resHelper = ResHelper.getInstance(getApplicationContext());
		connHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mUserid = resHelper.getUserid();
		String password = resHelper.getLoginPwd();
		// 未登录判断是否是第一次启动，第一次启动则加载欢迎界面，否则到登录界面
		int state = resHelper.getLoginState();
		if (state == 1 && mUserid.length() > 0 && password.length() == 32) {
			if (CommonUtil.getNetworkState(getApplicationContext()) == 0) {
				connHelper.login(mUserid, password, mUIHandler,
						MsgTagVO.PUB_INFO, null, true, null, null);
			} else {
				startService();
				goActivity(TabContainerActivity.class);
			}
		} else {
			if (state == 0 && mUserid.equals("") && password.equals("")
					&& resHelper.getFirstUse()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(ResHelper.FIRSTUSER, false);
				resHelper.setPreference(map);
				goActivity(PosterActivity.class);
			} else {
				goActivity(LoginActivity.class);
			}
		}

	}

	@SuppressWarnings("rawtypes")
	private void goActivity(Class go) {
		Intent intent = new Intent(InitActivity.this, go);
		startActivity(intent);
		InitActivity.this.finish();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO:
				// 登陆是否成功
				if (JsonHandler.checkResult((String) msg.obj)) {
					startService();
					goActivity(TabContainerActivity.class);
				} else {
					goActivity(LoginActivity.class);
				}
				break;
			// 登陆成功后会启动聊天后台服务(NotificationService)以接收推送消息
			case MsgTagVO.START_SEND:
				ServiceManager serviceManager = new ServiceManager(
						getApplicationContext());
				serviceManager.setNotificationIcon(R.drawable.newmsg);
				serviceManager.startService();
				break;
			default:
				break;
			}
		}
	};

	private void startService() {
		Message msg = mUIHandler.obtainMessage(MsgTagVO.START_SEND);
		msg.sendToTarget();
	}

}