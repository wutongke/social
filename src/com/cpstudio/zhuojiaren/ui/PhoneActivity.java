package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.util.Util;

public class PhoneActivity extends BaseActivity {
	@InjectView(R.id.ap_phone_bang_image)
	ImageView phoneBang;
	@InjectView(R.id.ap_phone_number)
	TextView phoneNumber;
	@InjectView(R.id.ap_phone_box)
	Button phoneBox;
	@InjectView(R.id.ap_update_phone)
	Button updatePhone;
	@InjectView(R.id.ap_phone_des)
	TextView des;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.bang_phone);
		TextView goBack = (TextView)findViewById(R.id.activity_back);
		goBack.setText(R.string.account_and_security);
		initClick();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		phoneNumber.setVisibility(View.VISIBLE);
		ResHelper resHelper = ResHelper.getInstance(mContext);
		phoneNumber.setText(resHelper.getUserid());
		updatePhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent (mContext,TextEditActivity.class);
				intent.putExtra("edtiText", "");
				startActivityForResult(intent, 1);
			}
		});
		phoneBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivity(intent);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1&&requestCode==RESULT_OK){
			if(Util.isMobileNum(data.getStringExtra("data")))
			phoneNumber.setText(getResources().getString(R.string.your_phone)+data.getStringExtra("data"));
			else{
				Util.toastMessage((Activity)mContext, getResources().getString(R.string.invalid_phone));
			}
		}
	}
}
