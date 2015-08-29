package com.cpstudio.zhuojiaren.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GrouthVisit;

public class GrouthVisitDetailActivity extends BaseActivity {
	@InjectView(R.id.agvd_image)
	ImageView image;
	@InjectView(R.id.agvd_name_and_order)
	TextView name;
	@InjectView(R.id.agvd_content)
	TextView content;
	@InjectView(R.id.agvd_time)
	TextView time;
	LoadImage imageLoader;
	private GrouthVisit visit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_visit_detail);
		ButterKnife.inject(this);
		initTitle();
		visit= (GrouthVisit) getIntent().getSerializableExtra("visit");
		title.setText(R.string.visit_detail);
		imageLoader = LoadImage.getInstance();
		loadData();
	}
	private void loadData() {
		// TODO Auto-generated method stub
		if(visit.getImageAddr()!=null)
		imageLoader.beginLoad(visit.getImageAddr(), image) ;
		content.setText(visit.getContent());
		name.setText(visit.getTitle());
		time.setText(visit.getCalldate());
	}
	Handler uiHandler =new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
}
