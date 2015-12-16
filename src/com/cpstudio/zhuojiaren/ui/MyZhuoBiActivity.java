package com.cpstudio.zhuojiaren.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.alipay.sdk.pay.ailiyue.AliPayActivity;
import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.MsgListActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;

public class MyZhuoBiActivity extends BaseActivity {
	@InjectView(R.id.amzb_rmb)
	TextView rmb;
	@InjectView(R.id.amzb_give_money)
	Button giveMoney;
	String price;
	private static int requestFriend = 2;

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
				startActivity(new Intent(MyZhuoBiActivity.this,
						IncomeActivity.class));
			}
		});
		findViewById(R.id.amzb_give_money).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MyZhuoBiActivity.this,
								MyFriendActivity.class);
						intent.putExtra("type", 3);
						startActivityForResult(intent, requestFriend);
					}

				});
		findViewById(R.id.amzb_add_money).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LayoutInflater inflater = LayoutInflater
								.from(MyZhuoBiActivity.this);
						final View view = inflater.inflate(
								R.layout.textview_dialot, null);
						final EditText money = (EditText) view
								.findViewById(R.id.money);
						new AlertDialog.Builder(MyZhuoBiActivity.this,
								AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
								.setTitle("充值")
								.setView(view)
								.setNegativeButton("取消", null)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												if (money.getText() == null)
													return;
												price = money.getText()
														.toString();
												AppClientLef
														.getInstance(
																MyZhuoBiActivity.this)
														.getOrderNumber(
																MyZhuoBiActivity.this,
																uiHandler,
																MsgTagVO.DATA_LOAD,
																"1", price);
											}
										}).create().show();
					}
				});
		loadMyZhuobi();
	}

	private static final  int GET_MONEY = 222;
	private void loadMyZhuobi() {
		// TODO Auto-generated method stub
		AppClientLef.getInstance(this.getApplicationContext()).getMyZhuoBi(
				MyZhuoBiActivity.this, uiHandler, GET_MONEY);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				ResultVO res = null;
				if (JsonHandler.checkResult((String) msg.obj,
						MyZhuoBiActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					return;
				}
				String data = res.getData();
				Intent payIntent = new Intent(MyZhuoBiActivity.this,ShoppingCartActivity.class);
				payIntent.putExtra("money",  (int) (Float
										.parseFloat(price)*100));
				payIntent.putExtra("number", data);
				startActivity(payIntent);
//				final Intent i = new Intent();
//				i.putExtra("tradeNum", data);
//				View view = getLayoutInflater().inflate(
//						R.layout.pay_wey_choose, null);
//				final AlertDialog alert = new AlertDialog.Builder(
//						MyZhuoBiActivity.this, AlertDialog.THEME_HOLO_LIGHT)
//						.setTitle("选择支付方式").setView(view).create();
//				view.findViewById(R.id.pay_weixin).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								i.setClass(MyZhuoBiActivity.this,
//										PayActivity.class);
//								// i.putExtra("money", "5");
//								i.putExtra("money", String.valueOf((int) (Float
//										.parseFloat(price) * 100)));
//								startActivity(i);
//								alert.dismiss();
//							}
//						});
//				view.findViewById(R.id.pay_ali).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								// TODO Auto-generated method stub
//								i.setClass(MyZhuoBiActivity.this,
//										AliPayActivity.class);
//								// i.putExtra("money", "0.5");
//								i.putExtra("money", String.valueOf((int) (Float
//										.parseFloat(price))));
//								startActivity(i);
//								alert.dismiss();
//							}
//						});
//				alert.show();
				break;
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj,
						MyZhuoBiActivity.this)) {
					ResultVO result = JsonHandler.parseResult((String) msg.obj);
					rmb.setText("￥"+result.getData());
					Toast.makeText(MyZhuoBiActivity.this, "赠送成功",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MyZhuoBiActivity.this, "赠送失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case GET_MONEY:
				ResultVO result = JsonHandler.parseResult((String) msg.obj);
				if (JsonHandler.checkResult((String) msg.obj,
						MyZhuoBiActivity.this)) {
					rmb.setText("￥"+result.getData());
				}else{
					Toast.makeText(MyZhuoBiActivity.this, result.getMsg(),
							Toast.LENGTH_SHORT).show();
				} 
			}
			
				
		};
	};

	protected void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		if (requestCode == requestFriend && resultCode == RESULT_OK) {
			LayoutInflater inflater = LayoutInflater
					.from(MyZhuoBiActivity.this);

			final View view = inflater.inflate(R.layout.textview_dialot, null);
			final EditText money = (EditText) view.findViewById(R.id.money);
			final TextView textView = (TextView) view
					.findViewById(R.id.texttitle);
			textView.setText("");
			new AlertDialog.Builder(MyZhuoBiActivity.this,
					AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
					.setTitle("送倬币给朋友")
					.setView(view)
					.setNegativeButton("取消", null)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method
									// stub
									if (money.getText() == null)
										return;
									AppClientLef
											.getInstance(MyZhuoBiActivity.this)
											.giveZhuobiToFriend(
													uiHandler,
													MsgTagVO.PUB_INFO,
													MyZhuoBiActivity.this,
													true,
													null,
													data.getStringExtra("userid"),
													money.getText().toString());
								}
							}).create().show();
		}
	}
}
