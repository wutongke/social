package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.helper.ConnHelper;

public class BaseActivity extends Activity {
	protected ConnHelper connHelper = null;
	protected TextView title;
	protected TextView function;
	protected TextView function2;
	protected ImageView imageFunction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connHelper = ConnHelper.getInstance(getApplicationContext());
	}

	protected void initTitle() {
		title = (TextView) findViewById(R.id.activity_title);
		function = (TextView) findViewById(R.id.activity_function);
		function2 = (TextView) findViewById(R.id.activity_function2);
		imageFunction = (ImageView)findViewById((R.id.activity_function_image));
		findViewById(R.id.activity_back).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(android.view.View v) {
						// TODO Auto-generated method stub
						BaseActivity.this.finish();
					}

				});
	}
}
