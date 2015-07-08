package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.ApplicationErrorReport.CrashInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.OrderAdapter;
import com.cpstudio.zhuojiaren.adapter.OrderAdapter.SelectGoodsChangeListener;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class CartActivity extends BaseActivity {
	@InjectView(R.id.acart_listview)
	ListView listView;
	@InjectView(R.id.acart_add_all)
	TextView addAll;
	@InjectView(R.id.acart_cash)
	TextView cash;
	@InjectView(R.id.acart_check)
	CheckBox cheak;
	@InjectView(R.id.acart_to_pay)
	TextView toPay;
	OrderAdapter mAdapter ;
	ArrayList<GoodsVO> mDataList = new ArrayList<GoodsVO>();
	private float sumPrice;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.cart2);
		//1管理2删除 比较重要
		function.setText(R.string.label_manage);
		function.setTag(1);
		function.setBackgroundResource(R.drawable.button_bg);
		mAdapter = new OrderAdapter(this, mDataList, R.layout.item_cart_goods);
		//监听选择商品数量变化
		mAdapter.setGoodsChangeListenter(new SelectGoodsChangeListener() {
			
			@Override
			public void onGoodsChange(float sum,int count) {
				// TODO Auto-generated method stub
				sumPrice = sum;
				addAll.setText(getResources().getString(R.string.add)+sum);
				toPay.setText(String.format(getResources().getString(R.string.to_pay), count));
			}
		});
		
		listView.setAdapter(mAdapter);
		//初始化
		addAll.setText(getResources().getString(R.string.add)+mAdapter.addAllGoodsPrice());
		sumPrice = mAdapter.addAllGoodsPrice();
		toPay.setText(String.format(getResources().getString(R.string.to_pay), 0));
		initClick();
		loadData();
	}
	private void loadData() {
		// TODO Auto-generated method stub
		GoodsVO goods = new GoodsVO();
		goods.setGid("1");
		goods.setName("吉大小天鹅");
		goods.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods.setCompanyName("北京");
		goods.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods.setIsCollection("0");
		PicVO pic = new PicVO();
		pic.setOrgurl("http://img13.360buyimg.com/vclist/jfs/t931/269/1375027638/11748/d0421ed8/559a312fN059bda44.jpg");
		goods.setCompanyPic(pic);
		ArrayList<PicVO> list = new ArrayList<PicVO>();
		list.add(pic);
		goods.setPic(list);
		goods.setMoney("152");
		goods.setPrice("152");
		goods.setZhuobi("120");
		
		GoodsVO goods2 = new GoodsVO();
		goods2.setGid("2");
		goods2.setName("吉大小天鹅");
		goods2.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods2.setCompanyName("北京");
		goods2.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods2.setIsCollection("0");
		goods2.setPic(list);
		goods2.setMoney("152");
		goods2.setPrice("152");
		goods2.setZhuobi("120");
		
		GoodsVO goods4 = new GoodsVO();
		goods4.setName("吉大小天鹅");
		goods4.setGid("4");
		goods4.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods4.setCompanyName("北京");
		goods4.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods4.setIsCollection("0");
		goods4.setPic(list);
		goods4.setMoney("152");
		goods4.setPrice("152");
		goods4.setZhuobi("120");
		
		GoodsVO goods5 = new GoodsVO();
		goods5.setGid("5");
		goods5.setName("吉大小天鹅");
		goods5.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods5.setCompanyName("北京");
		goods5.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods5.setIsCollection("0");
		goods5.setPic(list);
		goods5.setMoney("152");
		goods5.setPrice("152");
		goods5.setZhuobi("120");
		
		GoodsVO goods6 = new GoodsVO();
		goods6.setGid("6");
		goods6.setName("吉大小天鹅");
		goods6.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods6.setCompanyName("北京");
		goods6.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods6.setIsCollection("0");
		goods6.setPic(list);
		goods6.setMoney("152");
		goods6.setPrice("152");
		goods6.setZhuobi("120");
		GoodsVO goods7 = new GoodsVO();
		goods7.setGid("7");
		goods7.setName("吉大小天鹅");
		goods7.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods7.setCompanyName("北京");
		goods7.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods7.setIsCollection("0");
		goods7.setPic(list);
		goods7.setMoney("152");
		goods7.setPrice("152");
		goods7.setZhuobi("120");
		GoodsVO goods8 = new GoodsVO();
		goods8.setGid("8");
		goods8.setName("吉大小天鹅");
		goods8.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods8.setCompanyName("北京");
		goods8.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods8.setIsCollection("0");
		goods8.setPic(list);
		goods8.setMoney("152");
		goods8.setPrice("152");
		goods8.setZhuobi("120");
		GoodsVO goods9 = new GoodsVO();
		goods9.setGid("9");
		goods9.setName("吉大小天鹅");
		goods9.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods9.setCompanyName("北京");
		goods9.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods9.setIsCollection("0");
		goods9.setPic(list);
		goods9.setMoney("152");
		goods9.setPrice("152");
		goods9.setZhuobi("120");
		GoodsVO goods3 = new GoodsVO();
		goods3.setGid("3");
		goods3.setName("吉大小天鹅");
		goods3.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods3.setCompanyName("北京");
		goods3.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods3.setIsCollection("0");
		goods3.setPic(list);
		goods3.setMoney("152");
		goods3.setPrice("152");
		goods3.setZhuobi("120");
//		mDataList.add((GoodsVO)(getIntent().getSerializableExtra("goods")));
		mDataList.add(goods);
		mDataList.add(goods2);
		mDataList.add(goods3);
		mDataList.add(goods4);
		mDataList.add(goods5);
		mDataList.add(goods6);
		mDataList.add(goods7);
		mDataList.add(goods8);
		mDataList.add(goods9);
		mAdapter.notifyDataSetChanged();
		
	}
	private void initClick() {
		// TODO Auto-generated method stub
		toPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mAdapter.getSelectList().size()<1){
					CommonUtil.displayToast(CartActivity.this, R.string.no_order);
					return;
				}
				Intent intent = new Intent(CartActivity.this,OrderSubmitActivity.class);
				intent.putExtra("goods", mAdapter.getSelectList());
				intent.putExtra("goodsprice",String.valueOf(sumPrice));
				startActivity(intent);
			}
		});
		//
		cheak.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					mAdapter.getSelectList().clear();
					mAdapter.getSelectList().addAll(mDataList);
					mAdapter.notifyDataSetChanged();
				}else{
					mAdapter.getSelectList().clear();
					mAdapter.notifyDataSetChanged();
				}
				addAll.setText(getResources().getString(R.string.add)+mAdapter.addAllGoodsPrice());
				sumPrice = mAdapter.addAllGoodsPrice();
				toPay.setText(String.format(getResources().getString(R.string.to_pay), mAdapter.getSelectList().size()));
			}
		});
		
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Integer tag = (Integer) function.getTag();
				if(tag==1){
					function.setTag(2);
					function.setText(R.string.DELETE);
				}else{
					function.setTag(1);
					function.setText(R.string.label_manage);
					mDataList.removeAll(mAdapter.getSelectList());
					mAdapter.getSelectList().clear();
					//设置
					addAll.setText(getResources().getString(R.string.add)+mAdapter.addAllGoodsPrice());
					sumPrice = mAdapter.addAllGoodsPrice();
					toPay.setText(String.format(getResources().getString(R.string.to_pay), 0));
					mAdapter.notifyDataSetChanged();
					//增加网络部分
				}
			}
		});
	}
}
