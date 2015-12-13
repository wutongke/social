package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;

public class CardAddUserNameActivity extends Activity {
	boolean isEditable;
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
		setContentView(R.layout.activity_card_add_user_name);
		Intent i = getIntent();
		String name = i.getStringExtra(CardEditActivity.EDIT_NAME_STR1);
		((EditText) findViewById(R.id.editTextName)).setText(name);
		int sex = i.getIntExtra(CardEditActivity.EDIT_NAME_STR2, 0);
		int ismarry = i.getIntExtra(CardEditActivity.EDIT_NAME_STR3, 0);
		isEditable = i.getBooleanExtra(CardEditActivity.EDITABLE, false);
		if (1 == sex) {
			((RadioButton) findViewById(R.id.radioMale)).setChecked(true);
		} else if (2 == sex) {
			((RadioButton) findViewById(R.id.radioFemale)).setChecked(true);
		} else {
			((RadioButton) findViewById(R.id.radioSexUnknow)).setChecked(true);
		}

		if (1 == ismarry) {
			((RadioButton) findViewById(R.id.radioNotmarry)).setChecked(true);
		} else if (2 == ismarry) {
			((RadioButton) findViewById(R.id.radioIsmarray)).setChecked(true);
		} else
			((RadioButton) findViewById(R.id.radioMarrageUnknow))
					.setChecked(true);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(CardAddUserNameActivity.this.getCurrentFocus().getWindowToken(), 0);
				CardAddUserNameActivity.this.finish();
			}
		});
		if (!isEditable)
			findViewById(R.id.buttonSubmit).setVisibility(View.GONE);
		else {
			findViewById(R.id.buttonSubmit).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent();
							String name = ((EditText) findViewById(R.id.editTextName))
									.getText().toString();
							int gender = 0;
							if (((RadioButton) findViewById(R.id.radioMale))
									.isChecked()) {
								gender = 1;
							} else if (((RadioButton) findViewById(R.id.radioFemale))
									.isChecked()) {
								gender = 2;
							}
							int isMarray = 0;
							if (((RadioButton) findViewById(R.id.radioNotmarry))
									.isChecked()) {
								isMarray = 1;
							} else if (((RadioButton) findViewById(R.id.radioIsmarray))
									.isChecked()){
								isMarray = 2;
							}
							UserNewVO userInfo = new UserNewVO();
							userInfo.setName(name);
							userInfo.setGender(gender);
							userInfo.setMarried(isMarray);
							ZhuoConnHelper.getInstance(getApplicationContext())
									.modifyUserInfo(mUIHandler,
											MsgTagVO.DATA_LOAD, userInfo);
						}
					});
		}
	}
}
