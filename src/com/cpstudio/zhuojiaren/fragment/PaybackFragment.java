package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.PayBackAdapter;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PayBackVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.OverScrollableScrollView.OverScrollController;

public class PaybackFragment extends Fragment implements OverScrollController{
	@InjectView(R.id.payback_listview)
	ListView mListView;
	View view;
	PayBackAdapter mAdapter;
	ArrayList<PayBackVO> mDataList = new ArrayList<PayBackVO>();
	private Context mContext;
	private boolean mCanScrollUp = false;
	String crowdFundingId;
	AppClientLef appClient;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_payback, null);
		ButterKnife.inject(this, view);
		crowdFundingId = getArguments().getString("id");
		appClient = AppClientLef.getInstance(getActivity());
		mContext = getActivity();
		mAdapter = new PayBackAdapter(mContext, mDataList,
				R.layout.item_payback);
		mListView.setAdapter(mAdapter);
		mListView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                
            }
            
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int childTop = 0;
                if (view.getChildCount() > 0) {
                    childTop = view.getChildAt(0).getTop();
                }
                if (firstVisibleItem == 0 && childTop == 0) {
                    mCanScrollUp = true;
                } else {
                    mCanScrollUp = false;
                }
            }
        });
		init();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		// test
		appClient.getPayback(getActivity(),handler , MsgTagVO.DATA_LOAD, crowdFundingId);
	}

	@Override
	public boolean canScrollUp() {
		// TODO Auto-generated method stub
		return mCanScrollUp;
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					CommonUtil.displayToast(getActivity(),
							R.string.label_success);
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}

				break;
			case MsgTagVO.DATA_LOAD:
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					res = JsonHandler.parseResult((String) msg.obj);

					mDataList.clear();
					mDataList.addAll(JsonHandler_Lef.parsePayBackVOList(res
							.getData()));
					mAdapter.notifyDataSetChanged();
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}
			default:
				break;
			}
		};
	};
}
