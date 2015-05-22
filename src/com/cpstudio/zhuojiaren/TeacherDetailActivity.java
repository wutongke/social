package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class TeacherDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_detail);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String info = intent.getStringExtra("info");
		((TextView) findViewById(R.id.userNameShow)).setText(title);
		((TextView) findViewById(R.id.textViewContent)).setText(info);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TeacherDetailActivity.this.finish();
			}
		});
	}
	
}
