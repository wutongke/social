package com.cpstudui.zhuojiaren.lz;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

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
	}

}
