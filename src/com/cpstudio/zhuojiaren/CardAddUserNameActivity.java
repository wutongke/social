package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioButton;

public class CardAddUserNameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_name);
		Intent i = getIntent();
		String name = i.getStringExtra(CardEditActivity.EDIT_NAME_STR1);
		((EditText) findViewById(R.id.editTextName)).setText(name);
		String sex = i.getStringExtra(CardEditActivity.EDIT_NAME_STR2);
		String ismarry = i.getStringExtra(CardEditActivity.EDIT_NAME_STR3);
		if (sex != null && !sex.equals("")) {
			if (sex.equals(getString(R.string.mp_male))) {
				((RadioButton) findViewById(R.id.radioMale)).setChecked(true);
			} else if (sex.equals(getString(R.string.mp_female))) {
				((RadioButton) findViewById(R.id.radioFemale)).setChecked(true);
			} else {
				((RadioButton) findViewById(R.id.radioSexUnknow))
						.setChecked(true);
			}
		} else {
			((RadioButton) findViewById(R.id.radioSexUnknow)).setChecked(true);
		}
		if (ismarry != null && ismarry.equals("1")) {
			((RadioButton) findViewById(R.id.radioIsmarray)).setChecked(true);
		} else {
			((RadioButton) findViewById(R.id.radioNotmarry)).setChecked(true);
		}
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserNameActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra(CardEditActivity.EDIT_NAME_STR1,
								((EditText) findViewById(R.id.editTextName))
										.getText().toString());
						if (((RadioButton) findViewById(R.id.radioMale))
								.isChecked()) {
							intent.putExtra(CardEditActivity.EDIT_NAME_STR2,
									getString(R.string.mp_male));
						} else if (((RadioButton) findViewById(R.id.radioFemale))
								.isChecked()) {
							intent.putExtra(CardEditActivity.EDIT_NAME_STR2,
									getString(R.string.mp_female));
						} else {
							intent.putExtra(CardEditActivity.EDIT_NAME_STR2,
									getString(R.string.mp_unknow));
						}
						if (((RadioButton) findViewById(R.id.radioIsmarray))
								.isChecked()) {
							intent.putExtra(CardEditActivity.EDIT_NAME_STR3,
									"1");
						} else {
							intent.putExtra(CardEditActivity.EDIT_NAME_STR3,
									"0");
						}
						setResult(RESULT_OK, intent);
						CardAddUserNameActivity.this.finish();
					}
				});
	}

}
