package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.cart2);
		function.setText(R.string.label_manage);
		mAdapter = new OrderAdapter(this, mDataList, R.layout.item_cart_goods);
		//监听选择商品数量变化
		mAdapter.setGoodsChangeListenter(new SelectGoodsChangeListener() {
			
			@Override
			public void onGoodsChange(int sum,int count) {
				// TODO Auto-generated method stub
				addAll.setText(getResources().getString(R.string.add)+sum);
				toPay.setText(String.format(getResources().getString(R.string.to_pay), count));
			}
		});
		listView.setAdapter(mAdapter);
		initClick();
		loadData();
	}
	private void loadData() {
		// TODO Auto-generated method stub
		GoodsVO goods = new GoodsVO();
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
		mDataList.add(goods);
		mDataList.add(goods);
		mDataList.add(goods);
		mDataList.add(goods);
		mDataList.add(goods);
		mDataList.add(goods);
		mDataList.add(goods);
		mDataList.add(goods);
		mAdapter.notifyDataSetChanged();
		
	}
	private void initClick() {
		// TODO Auto-generated method stub
		toPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
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
			}
		});
	}
}
