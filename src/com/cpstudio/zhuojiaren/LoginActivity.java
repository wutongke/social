package com.cpstudio.zhuojiaren;

import java.util.HashMap;

import org.androidpn.client.ServiceManager;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Activity {

	private ResHelper mResHelper = null;
	private String mUid;
	private String mPassword;
	private EditText mUidView;
	private EditText mPwdView;
	private ZhuoConnHelper connHelper = null;
	private PopupWindows pwh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		connHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mResHelper = ResHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(LoginActivity.this);
		mUid = mResHelper.getUserid();
		mUidView = (EditText) findViewById(R.id.uid);
		mUidView.setText(mUid);
		mPwdView = (EditText) findViewById(R.id.password);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.rootLayout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
		});
		mPwdView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					login();
				}
				return false;
			}
		});

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						login();
					}
				});
	}

	private void login() {
		mUid = mUidView.getText().toString().trim();
		mPassword = CommonUtil.getMD5String(mPwdView.getText().toString());
		if (TextUtils.isEmpty(mPassword)) {
			CommonUtil.displayToast(getApplicationContext(), R.string.info15);
			return;
		}

		if (TextUtils.isEmpty(mUid)) {
			CommonUtil.displayToast(getApplicationContext(), R.string.info14);
			return;
		}
		connHelper.login(mUid, mPassword, mUIHandler, MsgTagVO.PUB_INFO,
				LoginActivity.this, true, null, null);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO:
				String rs = (String) msg.obj;
				if (JsonHandler.checkResult(rs, getApplicationContext())) {
					boolean first = mResHelper.getFirstUse();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(ResHelper.USER_ID, mUid);
					map.put(ResHelper.PASSWORD, mPassword);
					map.put(ResHelper.LOGIN_STATE, 1);
					mResHelper.setPreference(map);
					mResHelper.setPassword(mPassword);
					connHelper.setPassword(mPassword);
					mResHelper.setUserid(mUid);
					if (mPwdView.getText().toString().equals("000000") && first) {
						OnClickListener ok = new OnClickListener() {
							@Override
							public void onClick(View v) {
								showMainActivity();
							}
						};
						pwh.showPopDlgOne(findViewById(R.id.rootLayout), ok,
								R.string.info73);
					} else {
						showMainActivity();
					}
				} else {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(ResHelper.USER_ID, mUid);
					map.put(ResHelper.PASSWORD, "");
					map.put(ResHelper.LOGIN_STATE, 0);
					mResHelper.setPreference(map);
					mResHelper.setUserid("");
					mPwdView.requestFocus();
				}
				break;
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

	private void showMainActivity() {
		startService();
		Intent intent = new Intent(LoginActivity.this,
				TabContainerActivity.class);
		startActivity(intent);
		LoginActivity.this.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new ServiceManager(LoginActivity.this).stopService();
			LoginActivity.this.finish();
			System.exit(0);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
