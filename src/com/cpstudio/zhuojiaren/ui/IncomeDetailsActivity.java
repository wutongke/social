package com.cpstudio.zhuojiaren.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.IncomeVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class IncomeDetailsActivity extends BaseActivity {
	@InjectView(R.id.aid_deal_number)
	TextView dealNumber;
	@InjectView(R.id.aid_left_broken_money)
	TextView leftBrokenMoney;
	@InjectView(R.id.aid_money)
	TextView moneyTV;
	@InjectView(R.id.aid_money_type)
	TextView moneyType;
	@InjectView(R.id.aid_remark)
	TextView remark;
	@InjectView(R.id.aid_time)
	TextView time;
	@InjectView(R.id.aid_type)
	TextView type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income_details);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.income_expenditure_details);
		loadData(getIntent().getStringExtra(getResources().getString(R.id.tag_id)));
	}
	private void loadData(String stringExtra) {
		// TODO Auto-generated method stub
		IncomeVO test = new IncomeVO();
		test.setId("123");
		test.setName("¹ºÖÃÍ¼Êé");
		test.setTime(System.currentTimeMillis()-System.currentTimeMillis()/99+"");
		test.setLeftMoney("3000");
		test.setMoney("30.00");
		test.setOthers("²»´í²»´í");
		test.setDealNumber("123453463454");
		Message msg = uihandler.obtainMessage();
		msg.obj = test;
		msg.sendToTarget();
	}
	Handler uihandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			IncomeVO res = (IncomeVO) msg.obj;
			moneyTV.setText(res.getMoney());
			type.setText(res.getName());
			time.setText(CommonUtil.calcTime(res.getTime()));
			dealNumber.setText(res.getDealNumber());
			leftBrokenMoney.setText(res.getLeftMoney());
			remark.setText(res.getOthers());
			Float money = Float.parseFloat(res.getMoney());
			if(money>0){
				moneyType.setText(R.string.income_money);
				moneyTV.setTextColor(getResources().getColor(R.color.lightgreen));
			}else{
				moneyTV.setTextColor(getResources().getColor(R.color.black));
			}
		};
	};
}
