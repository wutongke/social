package com.cpstudio.zhuojiaren.ui;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class MyChangePwdActivity extends BaseActivity {
	private PopupWindows pwh = null;
	private String mPassword = null;
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_change_pwd);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(MyChangePwdActivity.this);
		
		initTitle();
		title.setText(R.string.zhuo_password);
		initClick();
	}

	private void initClick() {


		findViewById(R.id.buttonSend).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String oldPwd = ((EditText) findViewById(R.id.editTextOldPwd))
						.getText().toString();
				String newPwd = ((EditText) findViewById(R.id.editTextNewPwd))
						.getText().toString();
				String reNewPwd = ((EditText) findViewById(R.id.editTextReNewPwd))
						.getText().toString();
				if (oldPwd.equals("")) {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error13);
					return;
				}
				if (newPwd.equals("")) {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error14);
					return;
				}
				if (reNewPwd.equals("")) {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error15);
					return;
				}
				if (!reNewPwd.equals(newPwd)) {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error16);
					return;
				}
				mConnHelper.modifyPwd(CommonUtil.getMD5String(oldPwd),
						CommonUtil.getMD5String(newPwd), mUIHandler,
						MsgTagVO.PUB_INFO, MyChangePwdActivity.this, true,
						null, null);
			}
		});

	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put(ResHelper.PASSWORD, mPassword);
					ResHelper.getInstance(getApplicationContext())
							.setPreference(map);
					ResHelper.getInstance(getApplicationContext()).setPassword(
							mPassword);
					ZhuoConnHelper.getInstance(getApplicationContext())
							.setPassword(mPassword);
					pwh.showPopDlgOne(findViewById(R.id.rootLayout),
							new OnClickListener() {

								@Override
								public void onClick(View paramView) {
									MyChangePwdActivity.this.finish();

								}
							}, R.string.info60);
				}
				break;

			default:
				break;
			}
		}
	};

}
