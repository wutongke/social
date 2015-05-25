package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CardAddUserUrlActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_url);
		Intent i = getIntent();
		String webSite = i.getStringExtra(CardEditActivity.EDIT_WEBSITE_STR);
		((EditText) findViewById(R.id.editTextUrl)).setText(webSite);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserUrlActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.putExtra(CardEditActivity.EDIT_WEBSITE_STR,
								((EditText) findViewById(R.id.editTextUrl))
										.getText().toString());
						setResult(RESULT_OK, i);
						CardAddUserUrlActivity.this.finish();
					}
				});
	}

}
