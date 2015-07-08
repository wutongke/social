package com.cpstudio.zhuojiaren.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class LocateActivity extends BaseActivity {
	@InjectView(R.id.alocate_area)
	EditText area;
	@InjectView(R.id.alocate_mail_number)
	EditText mail;
	@InjectView(R.id.alocate_street)
	EditText street;
	@InjectView(R.id.alocate_more_info)
	EditText moreInfo;
	@InjectView(R.id.alocate_receive_name)
	EditText name;
	@InjectView(R.id.alocate_phone_number)
	EditText phone;
	@InjectView(R.id.alocate_save)
	Button save;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locate);
		ButterKnife.inject(this);
		initTitle();
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(area.getText().toString().isEmpty()||
						mail.getText().toString().isEmpty()||
						street.getText().toString().isEmpty()||
						moreInfo.getText().toString().isEmpty()||
						name.getText().toString().isEmpty()||
						phone.getText().toString().isEmpty()
						){
					CommonUtil.displayToast(LocateActivity.this, R.string.please_finish);
				}else{
					SharedPreferences sp = getSharedPreferences("receive_goods", MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("name", name.getText().toString());
					editor.putString("phone", phone.getText().toString());
					editor.putString("locate", area.getText().toString()+
								street.getText().toString()+
								moreInfo.getText().toString()
							);
					editor.putString("mail", mail.getText().toString());
					editor.commit();
					setResult(RESULT_OK);
					LocateActivity.this.finish();
				}
			}
		});
	}

}
