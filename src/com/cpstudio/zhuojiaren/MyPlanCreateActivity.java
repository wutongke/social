package com.cpstudio.zhuojiaren;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.cpstudio.zhuojiaren.facade.PlanFacade;
import com.cpstudio.zhuojiaren.model.PlanVO;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;

public class MyPlanCreateActivity extends Activity {
	String time = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_plan_create);
		SimpleDateFormat sdf = new SimpleDateFormat("MM"
				+ getString(R.string.label_month) + "dd"
				+ getString(R.string.label_day) + " a hh:mm",
				Locale.getDefault());
		Date date = new Date();
		time = sdf.format(date);
		((TextView) findViewById(R.id.textViewTime)).setText(time);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyPlanCreateActivity.this.finish();
			}
		});

		findViewById(R.id.buttonSave).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = ((EditText) findViewById(R.id.editText))
						.getText().toString();
				PlanVO item = new PlanVO();
				item.setId(String.valueOf(System.currentTimeMillis()));
				item.setContent(content);
				item.setAddtime(time);
				PlanFacade planFacade = new PlanFacade(getApplicationContext());
				if (planFacade.insert(item) > -1) {
					MyPlanCreateActivity.this.finish();
				}
			}
		});
	}
	
}
