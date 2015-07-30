package com.cpstudio.zhuojiaren;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ConnectCallback;
import io.rong.imlib.RongIMClient.ErrorCode;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.androidpn.client.ServiceManager;

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
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.LoginRes;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.ui.ForgetPasswordActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class LoginActivity extends Activity {

	private ResHelper mResHelper = null;
	private String mUid;
	private String mPassword;
	private EditText mUidView;
	private EditText mPwdView;
	private ZhuoConnHelper connHelper = null;
	private PopupWindows pwh = null;
	private TextView mFrogetPasswordView;

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
		mFrogetPasswordView = (TextView) findViewById(R.id.froget_password);
		initClick();
	}

	private void initClick() {
		mFrogetPasswordView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						ForgetPasswordActivity.class));
			}
		});
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
					// 获取session
					LoginRes res = JsonHandler_Lef.parseLoginRes(
							LoginActivity.this, JsonHandler.parseResult(rs)
									.getData());
					boolean first = mResHelper.getFirstUse();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(ResHelper.USER_ID, mUid);
					map.put(ResHelper.PASSWORD, mPassword);
					map.put(ResHelper.LOGIN_STATE, 1);
					map.put(ResHelper.SESSION, res.getSession());
					map.put(ResHelper.UPLIOAD_TOKEN, res.getQiniuToken());
					map.put(ResHelper.IM_TOKEN, res.getRongyunToken());
					// 保存到本地
					mResHelper.setPreference(map);
					// 保存到两个单例中
					mResHelper.setPassword(mPassword);
					mResHelper.setSessionForAPP(res.getSession());
					mResHelper.setUpLoadTokenForQiniu(res.getQiniuToken());
					mResHelper.setImTokenForRongyun(res.getRongyunToken());
					mResHelper.setUserid(mUid);

					connHelper.setPassword(mPassword);
					connHelper.setSession(res.getSession());
					connHelper.setUploadFileToken(res.getQiniuToken());
					connHelper.setImToken(res.getRongyunToken());
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
				String token=mResHelper.getImTokenForRongyun();
				//之后需删除，暂测试用
//				token = "Py74UXPT8qhWh2FBRCIcMTFjiRWti9Q/V/JbvRGji8CEHe0b5wf8iw2NE/ATk8uhgGu1XTpqtsG7e1/c1dAylg==";
				token="1i0IMiO5dWjOuGb10l2INNGFPZgrVDszbwnCc2LVvviZzRX4y7mcfCOL7dMa+prc1m3BcXo7y7yZu7T7F6rXBg==";
				
				RongIM.connect(token, new ConnectCallback() {

					@Override
					public void onError(ErrorCode arg0) {
						Toast.makeText(LoginActivity.this, "connect onError",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(String arg0) {
						Toast.makeText(LoginActivity.this,
								"connect onSuccess", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onTokenIncorrect() {
						// TODO Auto-generated method stub
						Toast.makeText(LoginActivity.this,
								"onTokenIncorrect", Toast.LENGTH_SHORT).show();
					}
				});
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
