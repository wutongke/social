package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.ApplicationErrorReport.CrashInfo;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.model.CartVO;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.google.gson.Gson;

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
	OrderAdapter mAdapter;
	ArrayList<GoodsVO> mDatas = new ArrayList<GoodsVO>();
	// 分页
	private AppClientLef appClientLef;
	private float sumPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		ButterKnife.inject(this);
		initTitle();
		appClientLef = AppClientLef.getInstance(this);
		title.setText(R.string.cart2);
		// 1管理2删除 比较重要
		function.setText(R.string.label_manage);
		function.setTag(1);
		function.setBackgroundResource(R.drawable.button_bg);
		mAdapter = new OrderAdapter(this, mDatas, R.layout.item_cart_goods);
		// 监听选择商品数量变化
		mAdapter.setGoodsChangeListenter(new SelectGoodsChangeListener() {

			@Override
			public void onGoodsChange(float sum, int count) {
				// TODO Auto-generated method stub
				sumPrice = sum;
				addAll.setText(getResources().getString(R.string.add) + sum);
				toPay.setText(String.format(
						getResources().getString(R.string.to_pay), count));
			}
		});

		listView.setAdapter(mAdapter);
		// 初始化
		addAll.setText(getResources().getString(R.string.add)
				+ mAdapter.addAllGoodsPrice());
		sumPrice = mAdapter.addAllGoodsPrice();
		toPay.setText(String.format(getResources().getString(R.string.to_pay),
				0));
		initClick();
		loadData();
	}

	private void initClick() {
		// TODO Auto-generated method stub
		toPay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mAdapter.getSelectList().size() < 1) {
					CommonUtil.displayToast(CartActivity.this,
							R.string.no_order);
					return;
				}
				Intent intent = new Intent(CartActivity.this,
						OrderSubmitActivity.class);
				intent.putExtra("goods", mAdapter.getSelectList());
				intent.putExtra("goodsprice", String.valueOf(sumPrice));
				startActivity(intent);
			}
		});
		//
		cheak.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					mAdapter.getSelectList().clear();
					mAdapter.getSelectList().addAll(mDatas);
					mAdapter.notifyDataSetChanged();
				} else {
					mAdapter.getSelectList().clear();
					mAdapter.notifyDataSetChanged();
				}
				addAll.setText(getResources().getString(R.string.add)
						+ mAdapter.addAllGoodsPrice());
				sumPrice = mAdapter.addAllGoodsPrice();
				toPay.setText(String.format(
						getResources().getString(R.string.to_pay), mAdapter
								.getSelectList().size()));
			}
		});

		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Integer tag = (Integer) function.getTag();
				if (tag == 1) {
					function.setTag(2);
					function.setText(R.string.DELETE);
				} else {

					function.setTag(1);
					function.setText(R.string.label_manage);
					StringBuilder sb = new StringBuilder();
					for (GoodsVO goods :mAdapter.getSelectList()){
						sb.append(goods.getGoodsId()+";");
					}
					AppClientLef.getInstance(CartActivity.this).removeGoods(
							sb.toString(), null, 0, CartActivity.this);

					mDatas.removeAll(mAdapter.getSelectList());
					mAdapter.getSelectList().clear();
					// 设置
					addAll.setText(getResources().getString(R.string.add)
							+ mAdapter.addAllGoodsPrice());
					sumPrice = mAdapter.addAllGoodsPrice();
					toPay.setText(String.format(
							getResources().getString(R.string.to_pay), 0));
					mAdapter.notifyDataSetChanged();
					// 增加网络部分
				}
			}
		});
	}

	private void loadData() {
		appClientLef.getCartGoodsList(this, uiHandler, MsgTagVO.DATA_LOAD, 0,
				1000);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				ResultVO res;
				if (JsonHandler
						.checkResult((String) msg.obj, CartActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					return;
				}
				String data = res.getData();
				CartVO cartData = new Gson().fromJson(data, CartVO.class);
				mDatas.clear();
				mDatas.addAll(cartData.getGoodsCartList());
				mAdapter.notifyDataSetChanged();
				break;
			}
			;
		}

	};

}
