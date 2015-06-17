package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
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
import com.cpstudio.zhuojiaren.model.PayBackVO;
import com.cpstudio.zhuojiaren.widget.OverScrollableScrollView.OverScrollController;

public class PaybackFragment extends Fragment implements OverScrollController{
	@InjectView(R.id.payback_listview)
	ListView mListView;
	View view;
	PayBackAdapter mAdapter;
	ArrayList<PayBackVO> mDataList = new ArrayList<PayBackVO>();
	private Context mContext;
	private boolean mCanScrollUp = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_payback, null);
		ButterKnife.inject(this, view);
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
		PayBackVO test = new PayBackVO();
		test.setPrice("298");
		test.setMaxCount("200");
		test.setDes("抄底价格，主动报警装置");
		test.setSupportCount("120");
		ArrayList<String> a = new ArrayList<String>();
		a.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3001334328,759370792&fm=116&gp=0.jpg");
		a.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3001334328,759370792&fm=116&gp=0.jpg");
		a.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3001334328,759370792&fm=116&gp=0.jpg");
		a.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3001334328,759370792&fm=116&gp=0.jpg");
		a.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3001334328,759370792&fm=116&gp=0.jpg");
		test.setImageUrl(a);
		mDataList.clear();
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean canScrollUp() {
		// TODO Auto-generated method stub
		return mCanScrollUp;
	}
}
