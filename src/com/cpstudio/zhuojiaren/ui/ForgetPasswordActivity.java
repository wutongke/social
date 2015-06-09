package com.cpstudio.zhuojiaren.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.Util;

public class ForgetPasswordActivity extends BaseActivity {
	@InjectView(R.id.afp_submitPassword)
	Button submit;
	RadioButton sexWoman;
	@InjectView(R.id.afp_phoneNoInput)
	EditText phone;
	@InjectView(R.id.afp_get_message)
	Button getMessage;
	@InjectView(R.id.afp_message)
	TextView messageText;
	CountDownTimer timer;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		ButterKnife.inject(this);
		initTitle();
		title.setText("找回密码");
		initView();
	}

	private void initView() {
		

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (phone.getText().toString().equals("")
						|| messageText.getText().toString().isEmpty()) {
					Toast.makeText(ForgetPasswordActivity.this, "请填写完整",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (Util.isMobileNum(phone.getText().toString()) == false) {
					Toast.makeText(ForgetPasswordActivity.this, "手机号不合法",
							Toast.LENGTH_SHORT).show();
					return;
				}
				progressDialog = new ProgressDialog(
						ForgetPasswordActivity.this,
						ProgressDialog.THEME_HOLO_DARK);
				progressDialog.setMessage("密码提交");
				progressDialog.show();
				// join(nickName.getText().toString(),
				// MD5.getMessageDigest(password.getText().toString().getBytes()),
				// sex,
				// phone.getText().toString(),messageText.getText().toString());
				// submit(phone.getText().toString(),MD5.getMessageDigest(password.getText().toString().getBytes()),messageText.getText().toString());
			}
		});
		getMessage.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (phone.getText().toString().isEmpty()) {
					Toast.makeText(ForgetPasswordActivity.this, "请填写手机号",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (Util.isMobileNum(phone.getText().toString()) == false) {
					Toast.makeText(ForgetPasswordActivity.this, "手机号不合法",
							Toast.LENGTH_SHORT).show();
					return;
				}
				timer = new CountDownTimer(60000, 1000) {

					@SuppressLint("ResourceAsColor")
					@Override
					public void onTick(long millisUntilFinished) {
						getMessage.setText((millisUntilFinished / 1000)
								+ "秒后可重新发送");
						getMessage.setEnabled(false);
						getMessage
								.setBackgroundColor(ForgetPasswordActivity.this
										.getResources().getColor(
												R.color.graywhitem));
					}

					@Override
					public void onFinish() {
						getMessage.setEnabled(true);
						getMessage
								.setBackgroundColor(ForgetPasswordActivity.this
										.getResources().getColor(R.color.gold));
						getMessage.setText("获取验证码");
					}
				}.start();
				getMeeage();
			}
		});
	}

	/**
	 * 获取验证码
	 */
	protected void getMeeage() {
		// TODO Auto-generated method stub
		connHelper.getVerificationcode(phone.getText().toString(), uiHandler, MsgTagVO.GET_VERIFICATIONCODE,
				ForgetPasswordActivity.this, true);
	}

	// 注册验证
	private void submit(final String phone, final String pwd,
			final String message) {
		
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.GET_VERIFICATIONCODE:
				
				break;
			case MsgTagVO.SUBMIT_VERIFICATIONCODE:
				String rs = (String) msg.obj;
				if (JsonHandler.checkResult(rs, getApplicationContext())) {
					//验证码正确
					
				} else {
					//验证码错误
				}
				break;
			default:
				break;
			}
		};
	};
}
