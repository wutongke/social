package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class CardAddUserEmailActivity extends Activity {
	int isOpen = 0;
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
		setContentView(R.layout.activity_card_add_user_email);
		Intent i = getIntent();
		String email = i.getStringExtra(CardEditActivity.EDIT_EMAIL_STR1);
		((EditText) findViewById(R.id.editTextEmail)).setText(email);
		int emailopen = i.getIntExtra(CardEditActivity.EDIT_EMAIL_STR2, 0);
		if (emailopen==1) {
			((RadioButton) (findViewById(R.id.radioPrivate))).setChecked(true);
			isOpen = 1;
		} else {
			((RadioButton) (findViewById(R.id.radioOpen))).setChecked(true);
			isOpen = 0;
		}
		((RadioGroup) this.findViewById(R.id.radioGroup))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						// 获取变更后的选中项的ID
						int radioButtonId = arg0.getCheckedRadioButtonId();
						if (radioButtonId == R.id.radioPrivate)
							isOpen = 1;
						else
							isOpen = 0;
					}
				});
		initClick();

	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(CardAddUserEmailActivity.this.getCurrentFocus().getWindowToken(), 0);
				CardAddUserEmailActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String email = ((EditText) findViewById(R.id.editTextEmail))
								.getText().toString();

						UserNewVO userInfo = new UserNewVO();
						userInfo.setEmail(email);
						userInfo.setIsEmailOpen(isOpen);
						ZhuoConnHelper.getInstance(getApplicationContext())
								.modifyUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
										userInfo);
					}
				});
	}
}
