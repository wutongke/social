package com.cpstudio.zhuojiaren.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;

public class AccountActivity extends BaseActivity {
	@InjectView(R.id.ac_zhuo_account)
	LinearLayout zhuoAccount;
	@InjectView(R.id.ac_zhuo_account_text)
	TextView zhuoAccountText;
	@InjectView(R.id.ac_phone_number)
	LinearLayout phoneNumber;
	@InjectView(R.id.ac_phone_number_text)
	TextView phoneNumberText;
	@InjectView(R.id.ac_qq_number)
	LinearLayout qqNumber;
	@InjectView(R.id.ac_qq_number_text)
	TextView qqNumberText;
	@InjectView(R.id.ac_email)
	LinearLayout email;
	@InjectView(R.id.ac_email_text)
	TextView emailText;
	@InjectView(R.id.ac_zhuo_password)
	LinearLayout zhuoPassword;
	@InjectView(R.id.ac_zhuo_password_text)
	TextView zhuoPasswordText;
	@InjectView(R.id.ac_account_security)
	LinearLayout accountSecurity;
	@InjectView(R.id.ac_account_security_text)
	TextView accountSecurityText;
	private Context mContext;
	private final int zhuoR=1;
	private final int phoneR=2;
	private final int qqR = 3;
	private final int emailR= 4;
	private final int zhuoPwdR = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.account_and_security);
		initClick();
	}
	private void initClick() {
		
		// TODO Auto-generated method stub
		OnClickListener click = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (mContext,ZhuoNameEditActivity.class);
				intent.putExtra("edtiText", "±à¼­Ù¾ÂöºÅ");
				startActivityForResult(intent, zhuoR);
			}
		};
		zhuoAccount.setOnClickListener(click);
		phoneNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(mContext,PhoneActivity.class));
			}
		});
		qqNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (mContext,QQActivity.class);
				intent.putExtra("qq", "456123789");
				startActivityForResult(intent,qqR);
			}
		});
		email.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (mContext,EmailActivity.class);
				intent.putExtra("email", "456123789@qq.com");
				startActivityForResult(intent,qqR);
			}
		});
		accountSecurity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (mContext,AccountProtectActivity.class);
				intent.putExtra("protect", "0");
				startActivity(new Intent(new Intent (mContext,AccountProtectActivity.class)));
			}
		});
		zhuoPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(new Intent (mContext,MyChangePwdActivity.class)));
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK)
		switch(requestCode){
		
		case zhuoR:
			zhuoAccountText.setText(data.getStringExtra("data"));
			break;
		case phoneR:
			phoneNumberText.setText(data.getStringExtra("data"));
			break;
		case qqR:
			qqNumberText.setText(data.getStringExtra("data"));
			break;
		case emailR:
			emailText.setText(data.getStringExtra("data"));
			break;
		}
	}

}
