package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;

public class CardAddUserEmailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_email);
		Intent i = getIntent();
		String email = i.getStringExtra(CardEditActivity.EDIT_EMAIL_STR1);
		((EditText) findViewById(R.id.editTextEmail)).setText(email);
		String emailopen = i.getStringExtra(CardEditActivity.EDIT_EMAIL_STR2);
		if (emailopen != null && emailopen.equals("1")) {
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
				CardAddUserEmailActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra(CardEditActivity.EDIT_EMAIL_STR1,
								((EditText) findViewById(R.id.editTextEmail))
										.getText().toString());
						if (((CheckBox) findViewById(R.id.checkBoxIsOpen))
								.isChecked()) {
							intent.putExtra(CardEditActivity.EDIT_EMAIL_STR2, "1");
						} else {
							intent.putExtra(CardEditActivity.EDIT_EMAIL_STR2, "0");
						}
						setResult(RESULT_OK, intent);
						CardAddUserEmailActivity.this.finish();
					}
				});
	}
}
