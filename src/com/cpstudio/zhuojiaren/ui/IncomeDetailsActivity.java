package com.cpstudio.zhuojiaren.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.IncomeVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
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
	private IncomeVO income;
	private String[] typelist = {"收入","支出"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_income_details);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.income_expenditure_details);
		income = (IncomeVO) getIntent().getSerializableExtra("income");
		dealNumber.setText(income.getBillNo());
		remark.setText(income.getRemark());
		time.setText(income.getAddtime());
		int typeNum = 0;
		try{
			typeNum = Integer.parseInt(income.getType());
		}catch(Exception e){
			
		}
		if(typeNum == 0 || typeNum == 1){
			type.setText(typelist[typeNum]);
		}
		getleftMoney();
	}

	private void getleftMoney() {
		// TODO Auto-generated method stub
		AppClientLef.getInstance(this.getApplicationContext()).getMyZhuoBi(
				IncomeDetailsActivity.this, uiHandler, GET_MONEY);
	}

	private static final int GET_MONEY = 101;
	private Handler uiHandler = new Handler() {
		public void handleMessage(final Message msg) {
			ResultVO res = null;
			res = JsonHandler.parseResult((String) msg.obj);
			if (JsonHandler.checkResult((String) msg.obj,
					IncomeDetailsActivity.this)) {
			} else {
				CommonUtil.displayToast(IncomeDetailsActivity.this,
						res.getMsg());
				return;
			}
			String data = res.getData();
			if (msg.what == GET_MONEY) {
				leftBrokenMoney.setText(res.getData());
			} else {
				CommonUtil.displayToast(IncomeDetailsActivity.this,
						R.string.error0);
			}
		};
	};
}
