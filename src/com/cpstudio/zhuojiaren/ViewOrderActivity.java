package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.adapter.OrderListAdapter;
import com.cpstudio.zhuojiaren.model.OrderVO;

public class ViewOrderActivity extends BaseActivity {
	@InjectView(R.id.lvOrders)
	ListView lvOrders;
	OrderListAdapter mAdapter;

	ArrayList<OrderVO> mList = new ArrayList<OrderVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_order);
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.title_activity_view_order);
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 4; i++)
			mList.add(new OrderVO());
		mAdapter = new OrderListAdapter(this, mList);
		lvOrders.setAdapter(mAdapter);
	}

}
