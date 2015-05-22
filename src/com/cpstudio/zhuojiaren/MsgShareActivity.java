package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
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

public class MsgShareActivity extends Activity {

	private PopupWindows pwh = null;
	private String msgid = null;
	private String parentid = null;
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_share);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(MsgShareActivity.this);
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");
		parentid = i.getStringExtra("parentid");
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgShareActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				publish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj)) {
					OnClickListener listener = new OnClickListener() {
						@Override
						public void onClick(View paramView) {
							MsgShareActivity.this.finish();
						}
					};
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), listener,
							R.string.info62);
				} else {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error5);
				}
				break;
			}
			}
		}

	};

	private void publish() {
		EditText contentEditText = (EditText) findViewById(R.id.editTextContent);
		String content = contentEditText.getText().toString();
		if (content.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info65);
			contentEditText.requestFocus();
			return;
		}
		mConnHelper.pubCmt(msgid, parentid, content, "1", mUIHandler,
				MsgTagVO.PUB_INFO, MsgShareActivity.this, true, null,null);
	}

}
