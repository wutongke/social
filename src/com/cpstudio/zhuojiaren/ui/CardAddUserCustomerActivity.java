package com.cpstudio.zhuojiaren.ui;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CardAddUserCustomerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_customer);
		Intent i = getIntent();
		String customer = i.getStringExtra(CardEditActivity.EDIT_CUSTOMER_STR);
		((EditText) findViewById(R.id.editText)).setText(customer);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserCustomerActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent();
						i.putExtra(CardEditActivity.EDIT_CUSTOMER_STR,
								((EditText) findViewById(R.id.editText))
										.getText().toString());
						setResult(RESULT_OK, i);
						CardAddUserCustomerActivity.this.finish();
					}
				});
	}

}
