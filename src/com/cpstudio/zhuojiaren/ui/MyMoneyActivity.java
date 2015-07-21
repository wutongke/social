package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BankCard;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class MyMoneyActivity extends BaseActivity {
	
	@InjectView(R.id.rmb_layout)
	LinearLayout rmbLayout;
	@InjectView(R.id.zhuo_bi_layout)
	LinearLayout zhuoBiLayout;
	@InjectView(R.id.rmb)
	TextView rmb;
	@InjectView(R.id.zhuo_bi)
	TextView zhuoBi;
	@InjectView(R.id.band_card)
	ListView bankCard;
	@InjectView(R.id.add_bank_card)
	Button addBankCard;
	List mDataList = new ArrayList<BankCard>();
	CommonAdapter<BankCard> mAdapter;
	LoadImage loader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_money);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.my_wallet);
		initOnclick();
		mAdapter = new CommonAdapter<BankCard>(MyMoneyActivity.this,mDataList,R.layout.item_bank_card) {
			
			@Override
			public void convert(ViewHolder helper, BankCard item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.ibc_bank_name, item.getBankName());
				helper.setText(R.id.ibc_bank_type, item.getCardType());
				helper.setText(R.id.ibc_bank_number, item.getBankNumber());
				helper.setImageResource(R.id.ibc_bank_image, R.drawable.money_qbmoney);
				loader.beginLoad(item.getBankImage(), (ImageView)helper.getView(R.id.ibc_bank_image));
			}
		};
		bankCard.setAdapter(mAdapter);
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
		for (int i = 0; i < 2; i++) {
			BankCard card = new BankCard();
			card.setId(i+"");
			card.setCardType("储蓄卡");
			card.setBankName("工商银行");
			card.setBankNumber("12312331313");
			mDataList.add(card);
		}
		mAdapter.notifyDataSetChanged();
	}
	Handler uiHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			rmb.setText(getResources().getString(R.string.crowdfunding_price_label2)+"150");
			zhuoBi.setText(getResources().getString(R.string.crowdfunding_price_label2)+"150");
		};
	};

}
