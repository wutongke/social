package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.google.gson.Gson;

public class OrderSubmitActivity extends BaseActivity {
	@InjectView(R.id.aod_person_info)
	TextView personInfo;
	@InjectView(R.id.aod_locate_info)
	TextView locateInfo;
	@InjectView(R.id.aod_pic1)
	ImageView pic1;
	@InjectView(R.id.aod_pic2)
	ImageView pic2;
	@InjectView(R.id.aod_pic3)
	ImageView pic3;
	@InjectView(R.id.aod_goods_count)
	TextView goodsCount;
	@InjectView(R.id.aod_invoice_info)
	EditText invoice;
	@InjectView(R.id.aod_left_message)
	EditText leftMessage;
	@InjectView(R.id.aod_left)
	TextView leftMoney;
	@InjectView(R.id.aod_sum)
	TextView goodsPrice;
	@InjectView(R.id.aod_submit_order)
	Button submitOrder;
	@InjectView(R.id.aod_goods_info)
	View goodsInfoLayout;
	@InjectView(R.id.aod_receipt_info)
	View receiveInfo;
	//
	private int requestForLocate = 1;
	// 保存默认地址
	SharedPreferences sp;
	SharedPreferences.Editor editer;
	ArrayList<GoodsVO> goodsList;
	LoadImage loadImage = new LoadImage();
	private static int GET_MONEY = 100;
	private static int GET_ORDER_NUMBER = 101;
	private static int PAYFOR = 102;
	private static int PAYMESSAGE = 103;
	String orderNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_submit);
		ButterKnife.inject(this);
		initTitle();
		initView();
		title.setText(R.string.write_order);
		initClick();
		getleftMoney();
	}

	private void getleftMoney() {
		// TODO Auto-generated method stub
		AppClientLef.getInstance(this.getApplicationContext()).getMyZhuoBi(
				OrderSubmitActivity.this, uiHandler, GET_MONEY);
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(final Message msg) {
			ResultVO res = null;
			res = JsonHandler.parseResult((String) msg.obj);
			if (JsonHandler.checkResult((String) msg.obj,
					OrderSubmitActivity.this)) {
			} else {
				CommonUtil.displayToast(OrderSubmitActivity.this, res.getMsg());
				return;
			}
			String data = res.getData();
			if (msg.what == GET_MONEY) {

				leftMoney.setText(res.getData());
			} else if (msg.what == GET_ORDER_NUMBER) {
				int price = (int) Float.parseFloat(goodsPrice.getText()
						.toString());
				AppClientLef.getInstance(
						OrderSubmitActivity.this.getApplicationContext())
						.payWithZhuobi(OrderSubmitActivity.this, uiHandler,
								PAYFOR, price + "", res.getData());
				orderNumber = res.getData();
			} else if (msg.what == PAYFOR) {
				CommonUtil.displayToast(OrderSubmitActivity.this, "支付成功");
				OrderSubmitActivity.this.finish();
				// leftMoney.setText(res.getData());
				// AppClientLef.getInstance(OrderSubmitActivity.this.getApplicationContext())
				// .postPayStatus(OrderSubmitActivity.this, uiHandler,
				// PAYMESSAGE, orderNumber, "1");//1表示成功
			} else {
				CommonUtil.displayToast(OrderSubmitActivity.this,
						R.string.error0);
			}
		};
	};

	private void initView() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences("receive_goods", MODE_PRIVATE);
		editer = sp.edit();
		if (sp.getString("name", null) != null) {
			String name = sp.getString("name", "");
			String phone = sp.getString("phone", "");
			String locate = sp.getString("locate", "");
			personInfo.setText(name + "  " + phone);
			locateInfo.setText(locate);
		} else {
			personInfo.setText(R.string.set_loate);
		}

		Intent intent = getIntent();
		goodsList = (ArrayList<GoodsVO>) intent.getSerializableExtra("goods");
		if (goodsList == null) {
			CommonUtil.displayToast(this, R.string.error_intent);
			this.finish();
		}
		goodsCount.setText(String.format(
				getResources().getString(R.string.goods_count),
				goodsList.size()));
		int count = goodsList.size();
		if (count >= 1) {
			loadImage.beginLoad(goodsList.get(0).getGoodsImg(), pic1);
			if (count >= 2)
				loadImage.beginLoad(goodsList.get(1).getGoodsImg(), pic2);
			if (count >= 3)
				loadImage.beginLoad(goodsList.get(2).getGoodsImg(), pic3);
		}
		goodsPrice.setText(intent.getStringExtra("goodsprice"));
	}

	private void initClick() {
		// TODO Auto-generated method stub
		goodsInfoLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSubmitActivity.this,
						CartActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		receiveInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSubmitActivity.this,
						LocateActivity.class);
				startActivityForResult(intent, requestForLocate);
			}
		});
		submitOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// submit to get tradeid and then fay
				// get number
				final int price = (int) Float.parseFloat(goodsPrice.getText()
						.toString());
				
				LayoutInflater inflater = LayoutInflater
						.from(OrderSubmitActivity.this);
				final View view = inflater.inflate(R.layout.textview_dialot,
						null);
				final TextView title = (TextView)view.findViewById(R.id.texttitle);
				final EditText password = (EditText) view.findViewById(R.id.money);
				title.setVisibility(View.GONE);
				password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
				new AlertDialog.Builder(OrderSubmitActivity.this,
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle("请输入支付密码（登录密码）")
						.setView(view)
						.setNegativeButton("取消", null)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method
										// stub
										if (!password.getText().toString()
												.isEmpty()
												&& CommonUtil.getMD5String(password.getText()
														.toString())
														.equals(ResHelper
																.getInstance(
																		OrderSubmitActivity.this)
																.getPassword())) {
											AppClientLef.getInstance(
													OrderSubmitActivity.this.getApplicationContext())
													.getOrderNumber(invoice.getText().toString(),
															leftMessage.getText().toString(),
															price + "".toString(),
															new Gson().toJson(goodsList), uiHandler,
															GET_ORDER_NUMBER, OrderSubmitActivity.this);
										}else{
											CommonUtil.displayToast(OrderSubmitActivity.this, R.string.error_password);
										}
									}
								}).create().show();
				
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == requestForLocate) {
			String name = sp.getString("name", "");
			String phone = sp.getString("phone", "");
			String locate = sp.getString("locate", "");
			editer.commit();
			personInfo.setText(name + "  " + phone);
			locateInfo.setText(locate);
		}
	}
}
