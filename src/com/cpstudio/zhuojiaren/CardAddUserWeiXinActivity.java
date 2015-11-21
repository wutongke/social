package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class CardAddUserWeiXinActivity extends Activity {
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_success);
				} else
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_failed);
				break;
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_weixin);
		Intent i = getIntent();
		String weixin = i.getStringExtra(CardEditActivity.EDIT_WEIXIN_STR1);
		((EditText) findViewById(R.id.edtWeixin)).setText(weixin);
		int weixinopen = i.getIntExtra(CardEditActivity.EDIT_WEIXIN_STR2, 0);
		if (0 == weixinopen) {
			((CheckBox) findViewById(R.id.checkBoxIsOpen)).setChecked(true);
		} else {
			((CheckBox) findViewById(R.id.checkBoxIsOpen)).setChecked(false);
		}
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserWeiXinActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						int open = 0;
						if (((CheckBox) findViewById(R.id.checkBoxIsOpen))
								.isChecked()) {
							open = 0;
						} else {
							open = 1;
						}
						UserNewVO userInfo = new UserNewVO();
						userInfo.setWeixin(((EditText) findViewById(R.id.edtWeixin))
								.getText().toString());
						userInfo.setIsWeixinOpen(open);
						ZhuoConnHelper.getInstance(getApplicationContext())
								.modifyUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
										userInfo);
					}
				});
	}

}
