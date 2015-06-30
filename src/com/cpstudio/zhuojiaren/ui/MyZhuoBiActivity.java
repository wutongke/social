package com.cpstudio.zhuojiaren.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;

public class MyZhuoBiActivity extends BaseActivity {
	@InjectView(R.id.amzb_rmb)
	TextView rmb;
	@InjectView(R.id.amzb_give_money)
	Button giveMoney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_zhuo_bi);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.my_zhuo_money);
		function.setText(R.string.income_expenditure_details);
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyZhuoBiActivity.this,IncomeActivity.class));
			}
		});
	}

}
