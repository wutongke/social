package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class RuleActivity extends Activity {

	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rule);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		initClick();
		loadData();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RuleActivity.this.finish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				// if (msg.obj != null && !msg.obj.equals("")) {
				// JsonHandler nljh = new JsonHandler((String) msg.obj,
				// getApplicationContext());
				// RuleVO rule = nljh.parseRule();
				// String content = rule.getContent();
				// ((TextView) findViewById(R.id.textViewContent))
				// .setText(content);
				// }
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					ResultVO rs = JsonHandler.parseResult((String) msg.obj);
					String content = rs.getData();
					((TextView) findViewById(R.id.textViewContent))
							.setText(content);
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		String params = ZhuoCommHelper.getUrlGetGoodsRule();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
				RuleActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						RuleActivity.this.finish();
					}
				});
	}

}
