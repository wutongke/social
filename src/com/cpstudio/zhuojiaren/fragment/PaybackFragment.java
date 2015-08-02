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
		test.setAmount("298");
		test.setLimit("200");
		test.setIntro("抄底价格，主动报警装置");
		test.setSupportCount("120");
		ArrayList<String> a = new ArrayList<String>();
		a.add("http://img0.imgtn.bdimg.com/it/u=3978527988,3847757746&fm=21&gp=0.jpg");
		a.add("http://img4.imgtn.bdimg.com/it/u=3355886690,291919142&fm=21&gp=0.jpg");
		a.add("http://img5.imgtn.bdimg.com/it/u=3541372963,3063415232&fm=21&gp=0.jpg");
		a.add("http://img3.imgtn.bdimg.com/it/u=1779627396,3712161577&fm=21&gp=0.jpg");
//		test.setImageUrl(a);
		PayBackVO test2 = new PayBackVO();
		test2.setAmount("298");
		test2.setLimit("200");
		test2.setIntro("抄底价格，主动报警装置");
		test2.setSupportCount("120");
		ArrayList<String> b = new ArrayList<String>();
		b.add("http://img3.imgtn.bdimg.com/it/u=2628293733,2370129064&fm=21&gp=0.jpg");
		b.add("http://img3.imgtn.bdimg.com/it/u=2863599132,929299683&fm=21&gp=0.jpg");
		b.add("http://img4.imgtn.bdimg.com/it/u=2297119962,3821452646&fm=21&gp=0.jpg");
//		test2.setImageUrl(b);
		mDataList.clear();
		mDataList.add(test);
		mDataList.add(test2);
		mDataList.add(test);
		mDataList.add(test2);
		mDataList.add(test);
		mDataList.add(test2);
		mDataList.add(test);
		mDataList.add(test2);
		mDataList.add(test);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public boolean canScrollUp() {
		// TODO Auto-generated method stub
		return mCanScrollUp;
	}
}
