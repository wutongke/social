package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String title = intent.getStringExtra("title");
		String content = intent.getStringExtra("content");
		((TextView) findViewById(R.id.userNameShow)).setText(name);
		((TextView) findViewById(R.id.textViewTitle)).setText(title);
		((TextView) findViewById(R.id.textViewContent)).setText(content);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InfoActivity.this.finish();
			}
		});
	}
	
}
