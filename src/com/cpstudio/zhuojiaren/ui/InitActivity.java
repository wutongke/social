package com.cpstudio.zhuojiaren.ui;

import io.rong.app.RongCloudEvent;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.LoginRes;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;

public class InitActivity extends Activity {

	private String mUserid = null;
	private ConnHelper connHelper = null;

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
		start();
	}

	private void start() {
		ResHelper resHelper = ResHelper.getInstance(getApplicationContext());
		connHelper = ConnHelper.getInstance(getApplicationContext());
		mUserid = resHelper.getUserid();
		String password = resHelper.getLoginPwd();
		// δ��¼�ж��Ƿ��ǵ�һ����������һ����������ػ�ӭ���棬���򵽵�¼����
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
				// ��½�Ƿ�ɹ�
				if (JsonHandler.checkResult((String) msg.obj)) {
					LoginRes res = JsonHandler_Lef
							.parseLoginRes(InitActivity.this, JsonHandler
									.parseResult((String) msg.obj).getData());
					AppClient.getInstance(InitActivity.this)
							.refreshUserInfo(res);
					startService();
					goActivity(TabContainerActivity.class);
				} else {
					goActivity(LoginActivity.class);
				}
				break;
			// ��½�ɹ�������������̨����(NotificationService)�Խ���������Ϣ
			case MsgTagVO.START_SEND:
				ResHelper mResHelper = ResHelper
						.getInstance(getApplicationContext());
				String token = mResHelper.getImTokenForRongyun();
				RongIM.connect(token, new ConnectCallback() {

					@Override
					public void onError(ErrorCode arg0) {
						Toast.makeText(InitActivity.this, "connect onError",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String arg0) {
						RongCloudEvent.getInstance().setOtherListener();

					}

					@Override
					public void onTokenIncorrect() {
						// TODO Auto-generated method stub
						Toast.makeText(InitActivity.this, "onTokenIncorrect",
								Toast.LENGTH_SHORT).show();
					}
				});

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