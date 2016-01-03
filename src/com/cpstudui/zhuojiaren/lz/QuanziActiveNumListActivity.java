package com.cpstudui.zhuojiaren.lz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.GroupStatus;

public class QuanziActiveNumListActivity extends BaseActivity {
	@InjectView(R.id.rlICreatedNum)
	View vICreatedNum;
	@InjectView(R.id.rlIJoinedNum)
	View vIJoinedNum;
	@InjectView(R.id.rlAllQuanNum)
	View vAllQuanNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quanzi_active_num_list);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.label_active_quanzi);
		initClick();
	}

	private void initClick() {
		// TODO Auto-generated method stub
		vICreatedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuanziActiveNumListActivity.this,
						QuanStatusListActivity.class);
				i.putExtra("mType", GroupStatus.GROUP_STATUS_TYPE_CREATED);
				startActivity(i);
			}
		});
		vAllQuanNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuanziActiveNumListActivity.this,
						QuanStatusListActivity.class);
				i.putExtra("mType", GroupStatus.GROUP_STATUS_TYPE_ALL);
				startActivity(i);
			}
		});
		vIJoinedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuanziActiveNumListActivity.this,
						QuanStatusListActivity.class);
				i.putExtra("mType", GroupStatus.GROUP_STATUS_TYPE_JOINED);
				startActivity(i);
			}
		});
	}

}
