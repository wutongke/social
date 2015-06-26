package com.cpstudio.zhuojiaren.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;

public class ZhuomaiActiveInfoFra extends Fragment {

	@InjectView(R.id.tvViewedNum)
	TextView tvViewedNum;
	@InjectView(R.id.tvAimedNum)
	TextView tvAimedNum;
	@InjectView(R.id.tvZanedNum)
	TextView tvZanedNum;
	@InjectView(R.id.tvRankNum)
	TextView tvRankNum;
	@InjectView(R.id.llMyRenmai)
	View llMyRenmai;
	@InjectView(R.id.llMyActive)
	View llMyActive;
	@InjectView(R.id.llMyQuanzi)
	View llMyQuanzi;
	
	private ZhuoConnHelper mConnHelper = null;

	private Context mContext;
	private String mLastId = null;
	// private PopupWindows pupWindow;

	// 主View
	View layout;
	private String uid = null;

	public interface functionListener {
		//
		public void onTypeChange(int type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.fragment_zhuomai_active_info,
				container, false);
		ButterKnife.inject(this, layout);

		mContext = getActivity();
		mConnHelper = ZhuoConnHelper.getInstance(getActivity()
				.getApplicationContext());

		uid = ResHelper.getInstance(getActivity().getApplicationContext())
				.getUserid();
		initclick();
		return layout;
	}

	private void initclick() {
		// TODO Auto-generated method stub
		
		
		
		
		llMyRenmai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		llMyActive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		llMyQuanzi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void loadData() {
		// String url = ZhuoCommHelper.getUrlUserInfo()
		// + "?uid="
		// + ResHelper.getInstance(getActivity().getApplicationContext())
		// .getUserid();
		//
		// // 加载刷新个人信息
		// mConnHelper.getFromServer(url, mUIHandler, MsgTagVO.UPDATE);
		//
		// if (mListViewFooter.startLoading()) {
		// mList.clear();
		// mAdapter.notifyDataSetChanged();
		// mPage = 1;
		// String params = ZhuoCommHelper.getUrlMsgList();
		// params += "?pageflag=" + "0";
		// params += "&reqnum=" + "10";
		// params += "&lastid=" + "0";
		// params += "&type=" + "0";
		//
		// params += "&gongxutype=" + "0";
		// params += "&from=" + "0";
		// params += "&uid=" + uid;
		// mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		// }
	}

}
