package com.cpstudio.zhuojiaren.ui;

import java.util.HashMap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.LoginActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.DatabaseHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.SysApplication;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;

public class SettingActivity extends BaseActivity {
	@InjectView(R.id.as_account)
	TextView account;
	@InjectView(R.id.as_new_message)
	TextView newMessage;
	@InjectView(R.id.as_new_versions)
	TextView newVesions;
	@InjectView(R.id.as_out_login)
	Button outLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.label_my_set);
		initClick();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		account.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this,AccountActivity.class));
			}
		});
		outLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String userid = ResHelper.getInstance(
						getApplicationContext()).getUserid();
				ZhuoConnHelper mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
				mConnHelper.androidName(null, 0, null, "", false, null,
						null);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put(ResHelper.PASSWORD, "");
				map.put(ResHelper.LOGIN_STATE, 0);
				ResHelper.getInstance(getApplicationContext())
						.setPreference(map);
				Intent i = new Intent(SettingActivity.this,
						LoginActivity.class);
				startActivity(i);
				DatabaseHelper dbHelper = new DatabaseHelper(
						getApplicationContext(), userid);
				dbHelper.close();
				SysApplication.getInstance().exit(false,
						getApplicationContext());
				SettingActivity.this.finish();
			}
		});
		newVesions.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(SettingActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
//				UpdateManager.getUpdateManager().checkAppUpdate(SettingActivity.this,
//						true);
			}
		});
		newMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this,MessageRemindActivity.class));
			}
		});
	}
	
}
