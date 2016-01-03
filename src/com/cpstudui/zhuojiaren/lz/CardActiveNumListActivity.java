package com.cpstudui.zhuojiaren.lz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.ui.UserSameActivity;

public class CardActiveNumListActivity extends BaseActivity {
	@InjectView(R.id.rlCardRequestedNum)
	View vRequestedNum;
	@InjectView(R.id.rlViewedNum)
	View vViededNum;
	@InjectView(R.id.rlCollectedMeNum)
	View vCollectedNum;

	@InjectView(R.id.rlZanMeNum)
	View vZanedNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_active_num_list);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.label_active_card);
		initClick();
	}

	private void initClick() {
		// TODO Auto-generated method stub
		// 以下都是人的列表
		vRequestedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardActiveNumListActivity.this,
						LZUserSameActivity.class);
				startActivity(i);
			}
		});

		vViededNum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardActiveNumListActivity.this,
						UserSameActivity.class);
				i.putExtra("type", 8);
				startActivity(i);
			}
		});

		vCollectedNum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardActiveNumListActivity.this,
						UserSameActivity.class);
				i.putExtra("type", 9);
				startActivity(i);
			}
		});

		vZanedNum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardActiveNumListActivity.this,
						UserSameActivity.class);
				i.putExtra("type", 10);
				startActivity(i);
			}
		});
	}

}
