package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.facade.PlanFacade;
import com.cpstudio.zhuojiaren.model.PlanVO;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.EditText;

public class MyPlanDetailActivity extends Activity {
	private EditText mEditText;
	private TextView mTextView;
	private String id = null;
	private String time = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_plan_detail);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		String content = intent.getStringExtra("content");
		time = intent.getStringExtra("addtime");
		mTextView = (TextView) findViewById(R.id.textViewContent);
		mTextView.setText(content);
		mEditText = (EditText) findViewById(R.id.editText);
		mEditText.setText(content);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyPlanDetailActivity.this.finish();
			}
		});

		mTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEditText.getVisibility() == View.GONE) {
					mEditText.setVisibility(View.VISIBLE);
					mTextView.setVisibility(View.GONE);
				}
			}
		});

		findViewById(R.id.buttonSave).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String content = mEditText.getText().toString();
				PlanVO plan = new PlanVO();
				plan.setId(id);
				plan.setContent(content);
				plan.setAddtime(time);
				PlanFacade planFacade = new PlanFacade(getApplicationContext());
				if (planFacade.update(plan) > 0) {
					mTextView.setText(content);
					mEditText.setVisibility(View.GONE);
					mTextView.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
}
