package com.cpstudio.zhuojiaren.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class LocateActivity extends BaseActivity {
	@InjectView(R.id.alocate_area)
	EditText area;
	@InjectView(R.id.alocate_mail_number)
	EditText mail;
	@InjectView(R.id.alocate_more_info)
	EditText moreInfo;
	@InjectView(R.id.alocate_receive_name)
	EditText name;
	@InjectView(R.id.alocate_phone_number)
	EditText phone;
	@InjectView(R.id.alocate_save)
	Button save;
	SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		ButterKnife.inject(this);
		initTitle();
		sp = getSharedPreferences("receive_goods", MODE_PRIVATE);
		area.setText(sp.getString("locate1", ""));
		moreInfo.setText(sp.getString("locate3", ""));
		name.setText(sp.getString("name", ""));
		phone.setText(sp.getString("phone", ""));
		mail.setText(sp.getString("mail", ""));

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (area.getText().toString().isEmpty()
						|| mail.getText().toString().isEmpty()
						|| moreInfo.getText().toString().isEmpty()
						|| name.getText().toString().isEmpty()
						|| phone.getText().toString().isEmpty()) {
					CommonUtil.displayToast(LocateActivity.this,
							R.string.please_finish);
				} else {
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("name", name.getText().toString());
					editor.putString("phone", phone.getText().toString());
					editor.putString("locate", area.getText().toString()
							+ moreInfo.getText().toString());
					editor.putString("locate1", area.getText().toString());
					editor.putString("locate3", moreInfo.getText().toString());
					editor.putString("mail", mail.getText().toString());
					editor.commit();
					setResult(RESULT_OK);
					LocateActivity.this.finish();
					AppClient.getInstance(
							LocateActivity.this.getApplicationContext())
							.setShippingAddress(
									LocateActivity.this,
									uiHandler,
									MsgTagVO.PUB_INFO,
									area.getText().toString(),
									moreInfo.getText().toString(),
									name.getText().toString(),
									phone.getText().toString(),
									mail.getText().toString());
				}
			}
		});
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MsgTagVO.PUB_INFO) {
				ResultVO res = null;
				if (JsonHandler.checkResult((String) msg.obj,
						LocateActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
					CommonUtil.displayToast(LocateActivity.this, "±£´æ³É¹¦");
				} else {
					res = JsonHandler.parseResult((String) msg.obj);
					CommonUtil.displayToast(LocateActivity.this, res.getMsg());
					return;
				}
			}
		};
	};

}
