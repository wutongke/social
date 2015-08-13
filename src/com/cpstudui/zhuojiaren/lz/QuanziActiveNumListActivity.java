package com.cpstudui.zhuojiaren.lz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.GroupStatusListActivity;
import com.cpstudio.zhuojiaren.JiarenActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.GroupStatus;

public class QuanziActiveNumListActivity extends BaseActivity {
	@InjectView(R.id.textViewICreatedNum)
	TextView tvICreatedNum;
	@InjectView(R.id.textViewIJoinedNum)
	TextView tvIJoinedNum;
	@InjectView(R.id.textAllQuanNum)
	TextView tvAllQuanNum;

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
		tvICreatedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuanziActiveNumListActivity.this,
						GroupStatusListActivity.class);
				i.putExtra("mType", GroupStatus.GROUP_STATUS_TYPE_CREATED);
				startActivity(i);
			}
		});
		tvAllQuanNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuanziActiveNumListActivity.this,
						GroupStatusListActivity.class);
				i.putExtra("mType", GroupStatus.GROUP_STATUS_TYPE_ALL);
				startActivity(i);
			}
		});
		tvIJoinedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(QuanziActiveNumListActivity.this,
						GroupStatusListActivity.class);
				i.putExtra("mType", GroupStatus.GROUP_STATUS_TYPE_JOINED);
				startActivity(i);
			}
		});
	}

}
