package com.cpstudio.zhuojiaren.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelperLz;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GrouthVisit;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class GrouthVisitDetailActivity extends BaseActivity {
	@InjectView(R.id.agvd_image)
	ImageView image;
	@InjectView(R.id.agvd_name_and_order)
	TextView name;
	@InjectView(R.id.agvd_content)
	TextView content;
	@InjectView(R.id.agvd_time)
	TextView time;
	@InjectView(R.id.agvd_share_inspiration)
	TextView share;
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
		final InputMethodManager iMM = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		findViewById(R.id.thought_post).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (share.getText().toString().isEmpty()) {
							CommonUtil.displayToast(GrouthVisitDetailActivity.this,
									R.string.please_finish_share);
							return;
						}
						AppClientLef.getInstance(
								GrouthVisitDetailActivity.this.getApplicationContext())
								.shareThought( ZhuoCommHelperLz.getVisitthought(),"videoid",visit.getId(),
										share.getText().toString(),
										uiHandler, MsgTagVO.PUB_INFO,
										GrouthVisitDetailActivity.this, true, null, null);
						share.setText("");
						iMM.hideSoftInputFromInputMethod(share.getWindowToken(), 0);
					}
				});
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
			if (msg.what == MsgTagVO.PUB_INFO) {
				if (JsonHandler.checkResult((String) msg.obj,
						GrouthVisitDetailActivity.this)) {
					CommonUtil.displayToast(GrouthVisitDetailActivity.this,
							R.string.success);
				} else {
					return;
				}
			}
		};
	};
}
