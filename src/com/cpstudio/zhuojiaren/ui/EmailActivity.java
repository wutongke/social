package com.cpstudio.zhuojiaren.ui;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class EmailActivity extends BaseActivity {
	@InjectView(R.id.ae_email)
	LinearLayout emailLayout;
	@InjectView(R.id.ae_email_text)
	TextView emailText;
	@InjectView(R.id.ae_ok)
	Button ok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email);
		ButterKnife.inject(this);
		
		initTitle();
		title.setText(R.string.change_email);
		initClick();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		String str = getIntent().getStringExtra("email");
		if(str!=null&&!str.isEmpty()){
			emailText.setText(str);
		}
		emailLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(EmailActivity.this,TextEditActivity.class ), 1);
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("data", emailText.getText().toString());
				setResult(RESULT_OK,intent);
				EmailActivity.this.finish();
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1&&resultCode==RESULT_OK){
			String str = data.getStringExtra("data");
			if(str!=null&&!str.isEmpty()){
				emailText.setText(str);
			}
		}
	}
	
}
