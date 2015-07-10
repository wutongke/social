package com.cpstudio.zhuojiaren.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.CardAddUserResourceActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;

public class ZhuomaiCardCommercyInfoFra extends Fragment {
	@InjectView(R.id.tvMoreResource)
	TextView tvMoreResource;
	@InjectView(R.id.tvMoreNeed)
	TextView tvMoreNeed;
	@InjectView(R.id.textViewWeidian)
	ImageView ivWeidian;
	@InjectView(R.id.textViewVideo)
	ImageView ivVideo;
	public final static String EDIT_RES_STR1 = "type";
	public final static String EDIT_RES_STR2 = "userid";
	private ZhuoConnHelper mConnHelper = null;

	private Context mContext;
	private String mLastId = null;
	// private PopupWindows pupWindow;

	// 主View
	View layout;
	private String uid = null;
	String groupId = null;

	public interface functionListener {
		//
		public void onTypeChange(int type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.fragment_zhuomai_commercy_info,
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
		ivWeidian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		ivVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		tvMoreResource.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 跳转到我的资源列表
				Intent i = new Intent(mContext,
						CardAddUserResourceActivity.class);
				i.putExtra(EDIT_RES_STR1, 2);
				i.putExtra(EDIT_RES_STR2, uid);
				startActivity(i);
			}
		});
		tvMoreNeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 跳转到我的需求列表
				Intent i = new Intent(mContext,
						CardAddUserResourceActivity.class);
				i.putExtra(EDIT_RES_STR1, 1);
				i.putExtra(EDIT_RES_STR2, uid);
				startActivity(i);
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
