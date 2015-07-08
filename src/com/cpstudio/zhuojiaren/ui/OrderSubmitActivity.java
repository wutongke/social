package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

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
	private int requestForLocate= 1;
	//����Ĭ�ϵ�ַ
	SharedPreferences sp;
	SharedPreferences.Editor editer;
	ArrayList<GoodsVO> goodsList;
	LoadImage loadImage = new LoadImage();
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
		Message msg = uiHandler.obtainMessage();
		msg.what=1;
		msg.obj = 500;
		msg.sendToTarget();
	}
	private Handler uiHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				leftMoney.setText(((Integer)msg.obj).toString());
			}else{
				CommonUtil.displayToast(OrderSubmitActivity.this, R.string.error0);
			}
		};
	};
	private void initView() {
		// TODO Auto-generated method stub
		sp = getSharedPreferences("receive_goods", MODE_PRIVATE);
		editer=sp.edit();
		if(sp.getString("name", null)!=null){
			String name = sp.getString("name","");
			String phone = sp.getString("phone","");
			String locate = sp.getString("locate","");
			personInfo.setText(name+"  "+phone);
			locateInfo.setText(locate);
		}else{
			personInfo.setText(R.string.set_loate);
		}
		
		Intent intent  = getIntent();
		goodsList = (ArrayList<GoodsVO>) intent.getSerializableExtra("goods");
		if(goodsList==null){
			CommonUtil.displayToast(this, R.string.error_intent);
			this.finish();
		}
		goodsCount.setText(String.format(getResources().getString(R.string.goods_count), goodsList.size()));
		int count = goodsList.size();
		if(count>=1){
			loadImage.beginLoad(goodsList.get(0).getFirstPic().getOrgurl(), pic1);
			if(count>=2)
				loadImage.beginLoad(goodsList.get(1).getFirstPic().getOrgurl(), pic2);
			if(count>=3)
				loadImage.beginLoad(goodsList.get(2).getFirstPic().getOrgurl(), pic3);
		}
		goodsPrice.setText(intent.getStringExtra("goodsprice"));
	}
	private void initClick() {
		// TODO Auto-generated method stub
		goodsInfoLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSubmitActivity.this,CartActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		receiveInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(OrderSubmitActivity.this,LocateActivity.class);
				startActivityForResult(intent, requestForLocate);
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK&&requestCode==requestForLocate){
			String name = sp.getString("name","");
			String phone = sp.getString("phone","");
			String locate = sp.getString("locate","");
			editer.commit();
			personInfo.setText(name+"  "+phone);
			locateInfo.setText(locate);
		}
	}
}