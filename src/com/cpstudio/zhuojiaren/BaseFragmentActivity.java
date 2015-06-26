package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;

public class BaseFragmentActivity extends FragmentActivity {

	protected ZhuoConnHelper connHelper = null;
	protected TextView title;
	protected TextView function,function2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connHelper = ZhuoConnHelper.getInstance(getApplicationContext());
	}

	protected void initTitle() {
		title=(TextView)findViewById(R.id.activity_title);
		function = (TextView)findViewById(R.id.activity_function);
		function2 = (TextView)findViewById(R.id.activity_function2);
		findViewById(R.id.activity_back).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						BaseFragmentActivity.this.finish();
					}
				});
	}


}
