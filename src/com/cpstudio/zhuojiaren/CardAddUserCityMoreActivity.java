package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.app.Activity;
import android.content.Intent;

public class CardAddUserCityMoreActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_city_more);
		Intent i = getIntent();
		String old = i.getStringExtra("city");
		((EditText) findViewById(R.id.editText)).setText(old);

		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("city",
						((EditText) findViewById(R.id.editText)).getText()
								.toString());
				setResult(RESULT_OK, intent);
				CardAddUserCityMoreActivity.this.finish();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent();
			intent.putExtra("city", ((EditText) findViewById(R.id.editText))
					.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
