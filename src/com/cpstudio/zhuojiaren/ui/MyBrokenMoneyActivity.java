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

public class MyBrokenMoneyActivity extends BaseActivity {
	@InjectView(R.id.ambm_rmb)
	TextView rmb;
	@InjectView(R.id.ambm_recharge_money)
	Button rachargeMoney;
	@InjectView(R.id.ambm_take_money)
	Button takeMoney;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_broken_money);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.my_money);
		function.setText(R.string.income_expenditure_details);
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MyBrokenMoneyActivity.this,IncomeActivity.class));
			}
		});
		rmb.setText(getIntent().getStringExtra(getResources().getString(R.id.tag_id)));
	}
}
