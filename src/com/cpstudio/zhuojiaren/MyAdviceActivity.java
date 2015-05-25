package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class MyAdviceActivity extends Activity {

	private PopupWindows pwh = null;
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_advice);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(MyAdviceActivity.this);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyAdviceActivity.this.finish();
			}
		});

		findViewById(R.id.buttonSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = ((EditText) findViewById(R.id.editText))
						.getText().toString();
				mConnHelper.advice(text, mUIHandler, MsgTagVO.PUB_INFO,
						MyAdviceActivity.this, true, null, null);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (msg.obj != null && !msg.obj.equals("")) {
					if (JsonHandler.checkResult((String) msg.obj,
							getApplicationContext())) {
						OnClickListener listener = new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								MyAdviceActivity.this.finish();
							}
						};
						pwh.showPopDlgOne(findViewById(R.id.rootLayout),
								listener, R.string.info62);
					}
				}
				break;
			}
			}
		}
	};

}
