package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;

public class ZhuoNameEditActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_name_edit);
		initTitle();
		title.setText(R.string.edit);
		function.setText(R.string.finish);
		Intent i = getIntent();
		String text = i.getStringExtra("edittext");
		if(text!=null)
		((EditText) findViewById(R.id.editText)).setText(text);
		initClick();
	}

	private void initClick() {
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("data",
						((EditText) findViewById(R.id.ate_editText))
								.getText().toString());
				setResult(RESULT_OK, intent);
				ZhuoNameEditActivity.this.finish();
			}
		});
	}
	

}
