package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class NewPasswordActivity extends BaseActivity {
	@InjectView(R.id.anp_password1)
	EditText password1View;
	@InjectView(R.id.anp_password2)
	EditText password2View;
	@InjectView(R.id.anp_submitPassword)
	Button submitBtn;
	private Context mContext;
	private PopupWindows pwh = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_password);
		ButterKnife.inject(this);
		mContext = this;
		pwh = new PopupWindows(this);
		initTitle();
		title.setText(R.string.get_password);
		initOnclick();
	}
	private void initOnclick() {
		// TODO Auto-generated method stub
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(password1View.getText().toString().isEmpty()||password2View.getText().toString().isEmpty()){
					pwh.showPopDlgOne(findViewById(R.id.activity_new_password), null, R.string.empty);
					return;
				}
				if(!password1View.getText().toString().equals(password2View.getText().toString())){
					pwh.showPopDlgOne(findViewById(R.id.activity_new_password), null, R.string.error16);
					return;
				}
				connHelper.modifyPwd(null,
						CommonUtil.getMD5String(password1View.getText().toString()), mUIHandler,
						MsgTagVO.PUB_INFO, (Activity)mContext, true,
						null, null);
			}
		});
	}
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					startActivity(new Intent(mContext,LoginActivity.class));
				}
				break;

			default:
				break;
			}
		}
	};
}
