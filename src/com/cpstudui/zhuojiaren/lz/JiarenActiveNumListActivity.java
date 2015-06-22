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

public class JiarenActiveNumListActivity extends BaseActivity {
	@InjectView(R.id.textViewIFocusedNum)
	TextView tvIFocusedNum;
	@InjectView(R.id.textViewAllNum)
	TextView tvAllNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiaren_active_num_list);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.label_active_jiaren);
	}

}
