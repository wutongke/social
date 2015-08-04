package com.cpstudio.zhuojiaren;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class MsgCmtActivity extends Activity {

	private PopupWindows pwh = null;
	private String msgid = null;
	private String parentid = null;
	private String after = null;
	private ZhuoConnHelper mConnHelper = null;
	private String forward = "0";
	private String content = null;
	private String toId = null;
	private String toUserid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_cmt);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(MsgCmtActivity.this);
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");
		toId = i.getStringExtra("toId");
		toUserid = i.getStringExtra("toUserid");
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgCmtActivity.this.finish();
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
		public void handleMessage(final Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj)) {
					Intent intent = new Intent();
					// intent.putExtra("forward", forward);
					// intent.putExtra("msgid", msgid);
					// intent.putExtra("userid", userid);
					intent.putExtra("data", (String) msg.obj);
					setResult(RESULT_OK, intent);
					MsgCmtActivity.this.finish();
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
		content = contentEditText.getText().toString();
		if (content.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info65);
			contentEditText.requestFocus();
			return;
		}
		if (after != null) {
			content += after;
		}
		CheckBox cb = (CheckBox) findViewById(R.id.checkBoxZf);
		if (cb.isChecked()) {
			forward = "1";
		}
		// mConnHelper.pubCmt(msgid, parentid, content, forward, mUIHandler,
		// , MsgCmtActivity.this, true, null, null);
		mConnHelper.CommentTopic(mUIHandler, MsgTagVO.PUB_INFO, msgid, content,
				toId, toUserid);
	}

}
