package com.cpstudio.zhuojiaren;

import java.util.HashMap;

import org.androidpn.client.ServiceManager;

import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.SysApplication;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MySettingActivity extends Activity {
	private PopupWindows pwh = null;
	private ZhuoConnHelper mConnHelper = null;
	private String isAlert = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_setting);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(MySettingActivity.this);
		initClick();
		loadData();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MySettingActivity.this.finish();
			}
		});

		findViewById(R.id.buttonLogout).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						String userid = ResHelper.getInstance(
								getApplicationContext()).getUserid();
						mConnHelper.androidName(null, 0, null, "", false, null,
								null);
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put(ResHelper.PASSWORD, "");
						map.put(ResHelper.LOGIN_STATE, 0);
						ResHelper.getInstance(getApplicationContext())
								.setPreference(map);
						ServiceManager serviceManager = new ServiceManager(
								MySettingActivity.this);
						serviceManager.stopService();
						Intent i = new Intent(MySettingActivity.this,
								LoginActivity.class);
						startActivity(i);
						DatabaseHelper dbHelper = new DatabaseHelper(
								getApplicationContext(), userid);
						dbHelper.close();
						SysApplication.getInstance().exit(false,
								getApplicationContext());
						MySettingActivity.this.finish();
					}
				});

		findViewById(R.id.textViewClearMsg).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						pwh.showPopDlg(findViewById(R.id.rootLayout),
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										ImChatFacade imChatFacade = new ImChatFacade(
												getApplicationContext());
										imChatFacade.deleteAll();
										pwh.showPopTip(
												findViewById(R.id.rootLayout),
												null, R.string.info12);
									}
								}, null, R.string.my_clearmsgalert);
					}
				});
		findViewById(R.id.relativeLayoutChangePwd).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(MySettingActivity.this,
								MyChangePwdActivity.class);
						startActivity(i);
					}
				});
		((ToggleButton) findViewById(R.id.toggleButtonAlert))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton v,
							boolean isChecked) {
						if (isChecked) {
							isAlert = "1";
						} else {
							isAlert = "0";
						}
						mConnHelper.updateConfig(mUIHandler, MsgTagVO.PUB_INFO,
								null, isAlert, false, null, null);
					}
				});

	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (null != msg.obj && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					UserVO user = nljh.parseUser();
					if (user != null && user.getIsalert().equals("0")) {
						isAlert = "0";
						((ToggleButton) findViewById(R.id.toggleButtonAlert))
								.setChecked(false);
					} else {
						isAlert = "1";
						((ToggleButton) findViewById(R.id.toggleButtonAlert))
								.setChecked(true);
					}
				}
				break;
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {

				}
				break;
			default:
				break;
			}
		}
	};

	private void loadData() {
		String params = ZhuoCommHelper.getUrlUserConfig();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

}
