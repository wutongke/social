package com.cpstudio.zhuojiaren.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class ResCommentActivity extends Activity {

	private PopupWindows pwh = null;
	private String msgid = null;
	private String content = null;
	private String toId = null;
	private String toUserid = null;
	private String toUserName = null;
	int type = 1;// 1:评论圈话题，2：评论动态 3:评论和回复供需
	EditText contentEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg_cmt);
		contentEditText = (EditText) findViewById(R.id.editTextContent);
		pwh = new PopupWindows(ResCommentActivity.this);
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");
		toId = i.getStringExtra("toId");
		toUserid = i.getStringExtra("toUserid");
		toUserName = i.getStringExtra("toUserName");
		type=i.getIntExtra("type",1);
		if(toId!=null && !toId.equals("-1")){
			contentEditText.setHint("回复"+toUserName);
		}
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ResCommentActivity.this.finish();
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
					com.cpstudio.zhuojiaren.model.ResultVO res = JsonHandler.parseResult((String) msg.obj);
					intent.putExtra("data", res.getData());
					setResult(RESULT_OK, intent);
					ResCommentActivity.this.finish();
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
		content = contentEditText.getText().toString();
		if (content.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info65);
			contentEditText.requestFocus();
			return;
		}
		if(type==3)
		{
			ConnHelper.getInstance(getApplicationContext()).cmtGongxu(mUIHandler, MsgTagVO.PUB_INFO, msgid, content, toId, toUserid);
		}
		else
		AppClient.getInstance(ResCommentActivity.this).pubComment(
				ResCommentActivity.this, mUIHandler, MsgTagVO.PUB_INFO, msgid,
				content, toId, toUserid);
	}

}
