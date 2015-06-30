package com.cpstudio.zhuojiaren.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;

public class QQActivity extends BaseActivity {
	@InjectView(R.id.aq_qq_bang_image)
	ImageView qqBang;
	@InjectView(R.id.aq_qq_number)
	TextView qqNumber;
	@InjectView(R.id.out_bang)
	Button outBang;
	@InjectView(R.id.aq_qq_des)
	TextView des;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qq);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.qq_number);
		TextView goBack = (TextView)findViewById(R.id.activity_back);
		goBack.setText(R.string.account_and_security);
		initClick();
	}
	private void initClick() {
		// TODO Auto-generated method stub
		String qq = getIntent().getStringExtra("qq");
		if(qq!=null){
			qqNumber.setVisibility(View.VISIBLE);
			qqNumber.setText(qq);
			outBang.setBackgroundColor(getResources().getColor(R.color.lightgreen));
			outBang.setText(R.string.bang_qq);
			outBang.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(QQActivity.this,TextEditActivity.class);
					startActivityForResult(intent, 1);
				}
			});
		}else{
			qqNumber.setVisibility(View.GONE);
			des.setText(R.string.no_qq);
			qqBang.setBackgroundResource(R.drawable.picture_unselected);
			outBang.setBackgroundColor(Color.RED);
			outBang.setText(R.string.bang_qq);
			outBang.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				}
			});
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==1&&resultCode==RESULT_OK){
			String str = data.getStringExtra("data");
			if(!str.isEmpty()){
				
				qqNumber.setText(data.getStringExtra("data"));
				outBang.setBackgroundColor(Color.RED);
				outBang.setText(R.string.bang_qq);
				outBang.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			intent.putExtra("data", qqNumber.getText().toString());
			setResult(RESULT_OK, intent);
			QQActivity.this.finish();
			return true;
		}
		else
		return super.onKeyDown(keyCode, event);
	}
}
