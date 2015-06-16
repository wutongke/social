package com.cpstudui.zhuojiaren.lz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.QuanFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;

public class ApplyToJoinQuanActicvity extends BaseActivity {
	private ZhuoConnHelper mConnHelper = null;
	private boolean isfollow = false;// 是否已经加入该圈子
	private QuanFacade mFacade = null;
	String groupid;
String uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_quan);
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.title_active_join_quan);
		uid = ResHelper.getInstance(getApplicationContext())
				.getUserid();
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
//		mFacade = new QuanFacade(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		initClick();
	}
	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.FOLLOW_QUAN: {
//				if (JsonHandler.checkResult((String) msg.obj,
//						getApplicationContext())) {
//					if (isfollow) {
//						isfollow = false;
//						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
//								null, R.string.label_exitSuccess);
//						loadData();
//					} else {
//						pwh.showPopTip(findViewById(R.id.scrollViewGroupInfo),
//								null, R.string.label_applysuccess);
//					}
//				}
				break;
			}
			
			}
		}
	};
	private void initClick() {

		findViewById(R.id.buttonSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		//还要记入申请加入的内容，接口需要做调整
				String content=((EditText)findViewById(R.id.editTextContent)).getText().toString().trim();
				mConnHelper.followGroup(groupid, "1", mUIHandler,
						MsgTagVO.FOLLOW_QUAN, null, true, null, null);
				Toast.makeText(ApplyToJoinQuanActicvity.this, "申请已发送，请耐心等待", 1000).show();
			}
		});
	}
}
