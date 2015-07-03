package com.cpstudio.zhuojiaren.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class QuanCreateSuccessActivity extends BaseActivity {
	@InjectView(R.id.aqcs_button)
	Button button;
	@InjectView(R.id.aqcs_text)
	TextView text;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_create_success);
		ButterKnife.inject(this);
		initTitle();
		function.setText(R.string.finish);
		initClick();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(text.getText().toString().isEmpty()){
					CommonUtil.displayToast(QuanCreateSuccessActivity.this, R.string.label_null);
				}
				else {
					
				}
			}
		});
	}

}
