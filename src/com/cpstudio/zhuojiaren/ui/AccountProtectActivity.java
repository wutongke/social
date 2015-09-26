package com.cpstudio.zhuojiaren.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;

public class AccountProtectActivity extends BaseActivity {
	@InjectView(R.id.swit)
	ImageView swit;
	private String open=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_protect);
		ButterKnife.inject(this);
		open = getIntent().getStringExtra("protect");
		initTitle();
		TextView goBack = (TextView)findViewById(R.id.activity_back);
		goBack.setText(R.string.account_and_security);
		initClick();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		swit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ÍøÂç²¿·Ö
				if(open!=null&&open.equals("1")){
					open="0";
					swit.setBackgroundResource(R.drawable.closecheck);
				}
				else{
					swit.setBackgroundResource(R.drawable.opencheck);
					open="1";
				} 
					
			}
		});
	}


}
