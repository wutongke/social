package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class CardAddUserCityActivity extends Activity {
	private static int OTHER_TOWN = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_city);
		Intent i = getIntent();
		String place = i.getStringExtra(CardEditActivity.EDIT_PLACE_STR1);
		((EditText) findViewById(R.id.editTextPlace)).setText(place);
		String othertowns = i.getStringExtra(CardEditActivity.EDIT_PLACE_STR3);
		((TextView) findViewById(R.id.textViewOtherTowns)).setText(othertowns);
		String hometown = i.getStringExtra(CardEditActivity.EDIT_PLACE_STR2);
		((EditText) findViewById(R.id.editTextHomeTown)).setText(hometown);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserCityActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.putExtra(CardEditActivity.EDIT_PLACE_STR1,
								((EditText) findViewById(R.id.editTextPlace))
										.getText().toString());
						intent.putExtra(
								CardEditActivity.EDIT_PLACE_STR3,
								((TextView) findViewById(R.id.textViewOtherTowns))
										.getText().toString());
						intent.putExtra(
								CardEditActivity.EDIT_PLACE_STR2,
								((EditText) findViewById(R.id.editTextHomeTown))
										.getText().toString());
						setResult(RESULT_OK, intent);
						CardAddUserCityActivity.this.finish();
					}
				});
		findViewById(R.id.textViewOtherTowns).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(CardAddUserCityActivity.this,
								CardAddUserCityMoreActivity.class);
						i.putExtra("city", ((TextView) v).getText());
						startActivityForResult(i, OTHER_TOWN);

					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == OTHER_TOWN) {
			String str = data.getStringExtra("city");
			((TextView) findViewById(R.id.textViewOtherTowns)).setText(str);
		}
	}
}
