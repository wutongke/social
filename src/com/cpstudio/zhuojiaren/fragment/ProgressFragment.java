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
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ProgressAdapter;
import com.cpstudio.zhuojiaren.model.ProgressVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.OverScrollableScrollView.OverScrollController;

public class ProgressFragment extends Fragment implements OverScrollController{

	@InjectView(R.id.payback_listview)
	ListView mListView;
	
	View view;
	ProgressAdapter mAdapter;
	ArrayList<ProgressVO> mDataList = new ArrayList<ProgressVO>();
	private Context mContext;
	private boolean mCanScrollUp = false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_progress, null);
		ButterKnife.inject(this, view);
		mContext = getActivity();
		mAdapter = new ProgressAdapter(mContext, mDataList,
				R.layout.item_progress);
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
		ProgressVO test = new ProgressVO();
		test.setContent("д�Ĳ�������");
		test.setTime("�ո�");
		UserVO user = new UserVO();
		user.setUsername("������");
		user.setPost("php����");
		user.setCompany("php902����");
		test.setUser(user);
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
