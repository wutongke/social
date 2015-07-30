package com.cpstudio.zhuojiaren;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

public class CardAddUserQQActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_qq);
		Intent i = getIntent();
		String qq = i.getStringExtra(CardEditActivity.EDIT_QQ_STR1);
		((TextView) findViewById(R.id.textViewQQ)).setText(qq);
		String qqopen = i.getStringExtra(CardEditActivity.EDIT_QQ_STR2);
		if (qqopen != null && qqopen.equals("1")) {
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
				CardAddUserQQActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.putExtra(CardEditActivity.EDIT_QQ_STR1,
								((TextView) findViewById(R.id.textViewQQ))
										.getText().toString());
						if (((CheckBox) findViewById(R.id.checkBoxIsOpen))
								.isChecked()) {
							i.putExtra(CardEditActivity.EDIT_QQ_STR2, "1");
						} else {
							i.putExtra(CardEditActivity.EDIT_QQ_STR2, "0");
						}
						setResult(RESULT_OK, i);
						CardAddUserQQActivity.this.finish();
					}
				});
	}

}
