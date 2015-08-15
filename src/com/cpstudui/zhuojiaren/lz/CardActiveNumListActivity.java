package com.cpstudui.zhuojiaren.lz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.ui.LZUserSameActivity;
import com.cpstudio.zhuojiaren.ui.UserSameActivity;

public class CardActiveNumListActivity extends BaseActivity {
	@InjectView(R.id.textViewCardRequestedNum)
	TextView tvRequestedNum;
	@InjectView(R.id.textViewViewedNum)
	TextView tvViededNum;
	@InjectView(R.id.textCollectedMeNum)
	TextView tvCollectedNum;

	@InjectView(R.id.textZanMeNum)
	TextView tvZanedNum;

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
		// ���¶����˵��б�
		tvRequestedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardActiveNumListActivity.this,
						LZUserSameActivity.class);
				startActivity(i);
			}
		});

		tvViededNum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardActiveNumListActivity.this,
						UserSameActivity.class);
				i.putExtra("type", 8);
				startActivity(i);
			}
		});

		tvCollectedNum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(CardActiveNumListActivity.this,
						UserSameActivity.class);
				i.putExtra("type", 9);
				startActivity(i);
			}
		});

		tvZanedNum.setOnClickListener(new OnClickListener() {
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
