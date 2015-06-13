package com.cpstudio.zhuojiaren.fragment;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class CrowdFundingFragment extends Fragment{
	@InjectView(R.id.fcd_pull_down_view)
	PullDownView pullDownView;
	private View view;
	private ListView mListView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 view  = inflater.inflate(R.layout.fragment_crowdfunding, null);
		ButterKnife.inject(this,view);
		initPullDownView();
		return view;
	}
	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(getActivity(), R.layout.head_crowdfunding);
		pullDownView.setOnPullDownListener(new OnPullDownListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
		mListView = pullDownView.getListView();
	}
}
