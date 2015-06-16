package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.PayBackAdapter;
import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.model.PayBackVO;
import com.cpstudio.zhuojiaren.widget.ListViewForScrollView;

public class CommentFragment extends Fragment{/*
	
	@InjectView(R.id.payback_listview)
	ListViewForScrollView mListView;
	View view;
	PayBackAdapter mAdapter;
	ArrayList<CommentVO> mDataList = new ArrayList<CommentVO>();
	private Context mContext;

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
		test.setDes("���׼۸���������װ��");
		test.setSupportCount("120");
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
		*//***
		 * ���scroll��listview �ĳ�ͻ
		 *//*
		int listHeight = setListViewHeightBasedOnChildren1(mListView);
		if(viewPagerHeight!=null){
			viewPagerHeight.onHeightChange(listHeight);
		}
		ViewGroup.LayoutParams params = mListView.getLayoutParams();
		params.height = listHeight;
		mListView.setLayoutParams(params);
	}

	ViewPagerHeight viewPagerHeight;
	
	public void setViewPagerHeight(ViewPagerHeight viewPagerHeight) {
		this.viewPagerHeight = viewPagerHeight;
	}

	// �ı�viewpager�ĸ߶�
	public interface ViewPagerHeight {
		public void onHeightChange(int height);
	}

	*//**
	 * ��ȡListview�ĸ߶ȣ�Ȼ������ViewPager�ĸ߶�
	 * 
	 * @param listView
	 * @return
	 *//*
	public static int setListViewHeightBasedOnChildren1(ListView listView) {
		// ��ȡListView��Ӧ��Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()�������������Ŀ
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // ��������View �Ŀ��
			totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
		// params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
		listView.setLayoutParams(params);
		return params.height;
	}

*/}
