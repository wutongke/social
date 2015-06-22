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

public class CardActiveNumListActivity extends BaseActivity {
	@InjectView(R.id.textViewCardRequestedNum)
	TextView tvRequestedNum;
	@InjectView(R.id.textViewViewedNum)
	TextView tvViededNum;
	@InjectView(R.id.textCollectedMeNum)
	TextView tvCollectedNum;
	
	@InjectView(R.id.textZanMeNum)
	TextView tvZanedNum;
	@InjectView(R.id.textByMeNum)
	TextView tvByMeNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_active_num_list);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.label_active_card);
	}

}
