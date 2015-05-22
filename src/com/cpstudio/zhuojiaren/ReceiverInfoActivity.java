package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class ReceiverInfoActivity extends Activity {
	private boolean isMy = true;
	private PopupWindows mPopHelper;
	private ZhuoConnHelper mConnHelper = null;
	private String gid = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver_info);
		mPopHelper = new PopupWindows(ReceiverInfoActivity.this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent intent = getIntent();
		isMy = intent.getBooleanExtra("ismy", true);
		gid = intent.getStringExtra("gid");
		if (isMy) {
			findViewById(R.id.linearLayoutMy).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.linearLayoutFriend).setVisibility(View.VISIBLE);
		}
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ReceiverInfoActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSave).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String myphone = ((EditText) findViewById(R.id.editTextMyPhone))
						.getText().toString();
				String friphone = ((EditText) findViewById(R.id.editTextFriPhone))
						.getText().toString();
				if (isMy) {
					String addr = ((EditText) findViewById(R.id.editTextMyAddress))
							.getText().toString();
					if (myphone.trim().equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error19);
						return;
					}
					if (addr.trim().equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error20);
						return;
					}
					mConnHelper.applyForExch(gid, "0", myphone, friphone, addr,
							mUIHandler, MsgTagVO.PUB_INFO,
							ReceiverInfoActivity.this, true, null, null);
				} else {
					String addr = ((EditText) findViewById(R.id.editTextFriAddress))
							.getText().toString();
					if (myphone.trim().equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error19);
						return;
					}
					if (friphone.trim().equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error21);
						return;
					}
					if (addr.trim().equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error22);
						return;
					}
					mConnHelper.applyForExch(gid, "1", myphone, friphone, addr,
							mUIHandler, MsgTagVO.PUB_INFO,
							ReceiverInfoActivity.this, true, null, null);
				}
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					View parent = findViewById(R.id.rootLayout);
					mPopHelper.showPopDlgOne(parent, new OnClickListener() {

						@Override
						public void onClick(View v) {
							ReceiverInfoActivity.this.finish();
						}
					}, R.string.info74);
				}
			}
			}
		}

	};

}
