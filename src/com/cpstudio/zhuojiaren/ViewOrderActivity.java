package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.adapter.OrderListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.OrderVO;
import com.cpstudio.zhuojiaren.ui.BaseActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
/**
 * ¶©µ¥ÁÐ±í
 * @author lz
 *
 */
public class ViewOrderActivity extends BaseActivity implements
		OnPullDownListener {
	@InjectView(R.id.lvOrders)
	com.cpstudio.zhuojiaren.widget.PullDownView mPullDownView;
	private ListView mListView;
	OrderListAdapter mAdapter;
	private ConnHelper mConnHelper = null;
	ArrayList<OrderVO> mList = new ArrayList<OrderVO>();
	private int mPage = 0;
	final int pageSize = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_order);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.title_activity_view_order_list);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();
		mListView = mPullDownView.getListView();
		mAdapter = new OrderListAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		loadData();
	}

	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ArrayList<OrderVO> list = new ArrayList<OrderVO>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<OrderVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseOrderList();
						if (!list.isEmpty()) {
							// infoFacade.update(list);
						}
					}
				}
				mPullDownView.finishLoadData(loadState);
				updateItemList(list, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				ArrayList<OrderVO> list = new ArrayList<OrderVO>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<OrderVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseOrderList();
					}
				}
				updateItemList(list, false, true);
				break;
			}

			}
		}
	};

	private void updateItemList(ArrayList<OrderVO> list, boolean refresh,
			boolean append) {
		if (!list.isEmpty()) {
			mPullDownView.hasData();
			if (!append) {
				mList.clear();
			}
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mPage++;
		} else {
			mPullDownView.noData(!refresh);
		}
	}

	private void loadData() {
		// TODO Auto-generated method stub
		if (mPullDownView.startLoadData()) {
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			} else {
				mConnHelper.getOrdersList(mUIHandler, MsgTagVO.DATA_LOAD,
						mPage, pageSize);
			}
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mPage = 0;
		loadData();
	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getOrdersList(mUIHandler, MsgTagVO.DATA_MORE, mPage,
					pageSize);
		}
	}

}
