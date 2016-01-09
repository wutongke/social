package com.cpstudio.zhuojiaren.ui;

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

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;

/**
 * 申请加入圈子界面
 * 
 * @author lz
 * 
 */
public class ApplyToJoinQuanActicvity extends BaseActivity {
	private ConnHelper mConnHelper = null;
	String groupid;
	String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apply_quan);
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.title_active_join_quan);
		uid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
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
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					Toast.makeText(ApplyToJoinQuanActicvity.this,
							"申请已发送，请耐心等待", Toast.LENGTH_SHORT).show();
				}
				break;
			}

			}
		}
	};

	private void initClick() {

		findViewById(R.id.buttonSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = ((EditText) findViewById(R.id.editTextContent))
						.getText().toString().trim();
				mConnHelper.followGroup(mUIHandler, MsgTagVO.FOLLOW_QUAN,
						groupid, QuanVO.QUAN_JOIN, content, "");
			}
		});
	}
}
