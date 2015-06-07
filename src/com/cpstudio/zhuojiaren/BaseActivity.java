package com.cpstudio.zhuojiaren;

import butterknife.InjectView;

import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BaseActivity extends Activity {
	protected ZhuoConnHelper connHelper = null;
	protected TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		connHelper = ZhuoConnHelper.getInstance(getApplicationContext());
	}

	protected void initTitle() {
		title=(TextView)findViewById(R.id.activity_title);
		findViewById(R.id.activity_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseActivity.this.finish();
					}
				});
	}
}
