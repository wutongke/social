package com.cpstudio.zhuojiaren.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.MsgListActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZMCDCount;
import com.cpstudio.zhuojiaren.ui.MyFriendActivity;
import com.cpstudio.zhuojiaren.ui.UserSameActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudui.zhuojiaren.lz.MyActiveActivity;

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

	// Ö÷View
	View layout;
	private String uid = null;
	ZMCDCount dataUnit;

	public interface functionListener {
		//
		public void onTypeChange(int type);
	}

	Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj, getActivity()
						.getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getActivity().getApplicationContext());
					dataUnit = nljh.parseZmCDCount();
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}
				updateInfo();
				break;
			}
			}
		}
	};

	private void updateInfo() {
		// TODO Auto-generated method stub
		if (dataUnit == null)
			return;
		tvViewedNum.setText(String.valueOf(dataUnit.getViewCount()));
		tvAimedNum.setText(String.valueOf(dataUnit.getFollowCount()));
		tvRankNum.setText(String.valueOf(dataUnit.getTop()));
		tvZanedNum.setText(String.valueOf(dataUnit.getPraiseCount()));
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
		uid = getArguments().getString("userid");
		initclick();
		loadData();
		return layout;
	}

	private void initclick() {
		// TODO Auto-generated method stub

		llMyRenmai.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext,
						MyFriendActivity.class);
				i.putExtra("type", 1);
				startActivity(i);
//				Intent i = new Intent(mContext, UserSameActivity.class);
//				i.putExtra("type", 12);
//				startActivity(i);
			}
		});
		llMyActive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, MyActiveActivity.class);
				startActivity(i);
			} 
		});
		llMyQuanzi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, ZhuoQuanActivity.class);
				i.putExtra("userid", uid);
				i.putExtra("selected", 0);
				startActivity(i);
			}
		});
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getActivity().getApplicationContext()) == 2) {
		} else {
			mConnHelper.getZMDTCount(mUIHandler, MsgTagVO.DATA_LOAD, uid);
		}
	}

}
