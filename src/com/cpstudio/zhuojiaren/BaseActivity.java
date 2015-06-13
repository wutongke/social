package com.cpstudio.zhuojiaren;

import javax.swing.text.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;

public class BaseActivity extends Activity {
	protected ZhuoConnHelper connHelper = null;
	protected TextView title;
	protected TextView function;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connHelper = ZhuoConnHelper.getInstance(getApplicationContext());
	}

	protected void initTitle() {
		title = (TextView) findViewById(R.id.activity_title);
		function = (TextView) findViewById(R.id.activity_function);
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
