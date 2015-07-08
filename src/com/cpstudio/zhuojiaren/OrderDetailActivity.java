package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.adapter.OrderListItemGoodsListAdapter;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.widget.NestedListView;

public class OrderDetailActivity extends BaseActivity {
	@InjectView(R.id.tvOrderStatus)
	TextView tvStatus;
	@InjectView(R.id.tvZhuobi)
	TextView tvZhuobi;
	@InjectView(R.id.tvReceiver)
	TextView tvReceiver;
	@InjectView(R.id.tvPhoneNum)
	TextView tvPhoneNum;
	// 设置内容和是需要格式化设置
	@InjectView(R.id.tvAddress)
	TextView tvAddress;
	@InjectView(R.id.ivGoods)
	NestedListView lvGoods;
	@InjectView(R.id.tvOrderNum)
	TextView tvOrderNum;
	@InjectView(R.id.tvDealTime)
	TextView tvDealTime;
	@InjectView(R.id.etCmt)
	EditText etCmt;
	@InjectView(R.id.rlCmt)
	View rlCmt;
	String orderId = null;
	List<GoodsVO> goodsList = new ArrayList<GoodsVO>();
	OrderListItemGoodsListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_activity_order_detail);

		orderId = getIntent().getStringExtra("orderId");

		mAdapter = new OrderListItemGoodsListAdapter(this, goodsList);

		lvGoods.setAdapter(mAdapter);

		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 3; i++)
			goodsList.add(new GoodsVO());
		mAdapter.notifyDataSetChanged();
	}

}
