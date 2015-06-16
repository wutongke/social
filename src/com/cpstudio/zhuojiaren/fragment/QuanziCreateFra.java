package com.cpstudio.zhuojiaren.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.ui.QuanCreateActivity;

public class QuanziCreateFra extends Fragment{

	View layout;
	@InjectView(R.id.before_crete)
	LinearLayout beforeCreate;
	@InjectView(R.id.begin_crete)
	LinearLayout beginCreate;
	@InjectView(R.id.before_btn)
	Button beforeBtn;
	@InjectView(R.id.begin_btn)
	Button beginBtn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.activity_before_create, null);
		ButterKnife.inject(this, layout);
		beforeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				beforeCreate.setVisibility(View.GONE);
				beginCreate.setVisibility(View.VISIBLE);
			}
		});
		beginBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(),QuanCreateActivity.class));
			}
		});
		return layout;
	}
}
