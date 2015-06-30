package com.cpstudio.zhuojiaren.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;

public class MyMoneyActivity extends BaseActivity {
	
	@InjectView(R.id.rmb_layout)
	LinearLayout rmbLayout;
	@InjectView(R.id.zhuo_bi_layout)
	LinearLayout zhuoBiLayout;
	@InjectView(R.id.rmb)
	TextView rmb;
	@InjectView(R.id.zhuo_bi)
	TextView zhuoBi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_money);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.my_wallet);
		initOnclick();
		loadData();
		
	}
	private void initOnclick() {
		// TODO Auto-generated method stub
		rmbLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyMoneyActivity.this,MyBrokenMoneyActivity.class);
				intent.putExtra(getResources().getString(R.id.tag_id),rmb.getText().toString() );
				startActivity(intent);
			}
		});
		zhuoBiLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyMoneyActivity.this,MyZhuoBiActivity.class);
				intent.putExtra(getResources().getString(R.id.tag_id),zhuoBi.getText().toString() );
				startActivity(intent);
			}
		});
	}
	private void loadData() {
		// TODO Auto-generated method stub
		uiHandler.obtainMessage().sendToTarget();
	}
	Handler uiHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			rmb.setText(getResources().getString(R.string.crowdfunding_price_label2)+"150");
			zhuoBi.setText(getResources().getString(R.string.crowdfunding_price_label2)+"150");
		};
	};

}
