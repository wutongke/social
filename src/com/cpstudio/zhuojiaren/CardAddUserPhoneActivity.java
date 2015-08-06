package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CardAddUserPhoneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_phone);
		Intent i = getIntent();
		String phone = i.getStringExtra(CardEditActivity.EDIT_PHONE_STR1);
		((EditText) findViewById(R.id.edtPhone)).setText(phone);
		int phoneopen = i.getIntExtra(CardEditActivity.EDIT_PHONE_STR2,0);
		if (0==phoneopen) {
			((CheckBox) findViewById(R.id.checkBoxIsOpen)).setChecked(true);
		}else{
			((CheckBox) findViewById(R.id.checkBoxIsOpen)).setChecked(false);
		}
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserPhoneActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.putExtra(CardEditActivity.EDIT_PHONE_STR1,
								((EditText) findViewById(R.id.edtPhone))
										.getText().toString());
						if (((CheckBox) findViewById(R.id.checkBoxIsOpen))
								.isChecked()) {
							i.putExtra(CardEditActivity.EDIT_PHONE_STR2, 1);
						} else {
							i.putExtra(CardEditActivity.EDIT_PHONE_STR2, 0);
						}
						setResult(RESULT_OK, i);
						CardAddUserPhoneActivity.this.finish();
					}
				});
	}

}
