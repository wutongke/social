package com.cpstudio.zhuojiaren;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.adapter.OrderListItemGoodsListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.OrderVO;
import com.cpstudio.zhuojiaren.ui.GoodsDetailLActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.NestedListView;
/**
 * ¶©µ¥ÏêÇé
 * @author lz
 *
 */
public class OrderDetailActivity extends BaseActivity {
	@InjectView(R.id.tvOrderStatus)
	TextView tvStatus;
	@InjectView(R.id.tvZhuobi)
	TextView tvZhuobi;
	@InjectView(R.id.tvReceiver)
	TextView tvReceiver;
	@InjectView(R.id.tvPhoneNum)
	TextView tvPhoneNum;
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
	OrderListItemGoodsListAdapter mAdapter;
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_activity_order_detail);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		orderId = getIntent().getStringExtra("orderId");

		loadData();
	}

	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				OrderVO item = null;
				if (msg.obj instanceof OrderVO) {
					item = (OrderVO) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						item = nljh.parseOrderVO();
						if (item != null) {
							// infoFacade.update(list);
							updateData(item);
						}
					}
				}
				break;
			}
			}
		}

	};

	private void updateData(OrderVO item) {
		// TODO Auto-generated method stub
		if (item == null)
			return;
		String[] statusArray = getResources().getStringArray(
				R.array.order_status);
		if (item.getStatus() >= 0 && item.getStatus() < statusArray.length)
			setText(tvStatus, statusArray[item.getStatus()]);
		setText(tvZhuobi, String.valueOf(item.getTotalZhuobi()));

		setText(tvAddress, item.getReceiverAddr());
		setText(tvReceiver, item.getReceiver());
		setText(tvDealTime, item.getReceipttime());
		setText(tvOrderNum, item.getBillNo());
		setText(tvPhoneNum, item.getPhone());

		final List<GoodsVO> goodsList = item.getBuyGoods();
		if (goodsList != null) {
			mAdapter = new OrderListItemGoodsListAdapter(this, goodsList);
			lvGoods.setAdapter(mAdapter);
			lvGoods.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(OrderDetailActivity.this,
							GoodsDetailLActivity.class);
					intent.putExtra("goodsId", goodsList.get(position)
							.getGoodsId());
					OrderDetailActivity.this.startActivity(intent);
				}
			});
		}
	}

	private void setText(TextView tv, String value) {
		if (tv == null || value == null)
			return;
		tv.setText(value);
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getOrderDetail(mUIHandler, MsgTagVO.DATA_LOAD, orderId);
		}
	}

}
