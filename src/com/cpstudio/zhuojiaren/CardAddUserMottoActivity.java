package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CardAddUserMottoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_motto);
		Intent i = getIntent();
		String motto = i.getStringExtra(CardEditActivity.EDIT_MOTTO_STR);
		((EditText) findViewById(R.id.editText)).setText(motto);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserMottoActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra(CardEditActivity.EDIT_MOTTO_STR,
								((EditText) findViewById(R.id.editText))
										.getText().toString());
						setResult(RESULT_OK, intent);
						CardAddUserMottoActivity.this.finish();
					}
				});
	}

}
