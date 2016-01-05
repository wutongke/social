package com.cpstudio.zhuojiaren.fragment;

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

public class ZhuomaiMoreInfoFra extends Fragment {
	@InjectView(R.id.tvMoreResource)
	TextView tvMoreResource;
	@InjectView(R.id.tvMoreNeed)
	TextView tvMoreNeed;

	View layout;
	String groupId = null;

	public interface functionListener {
		public void onTypeChange(int type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.fragment_zhuomai_commercy_info,
				container, false);
		ButterKnife.inject(this, layout);

		initclick();
		return layout;
	}

	private void initclick() {
		// TODO Auto-generated method stub
		tvMoreResource.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//跳转到我的资源列表
			}
		});
		tvMoreNeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//跳转到我的需求列表
			}
		});
	}
}
