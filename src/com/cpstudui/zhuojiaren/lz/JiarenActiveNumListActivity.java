package com.cpstudui.zhuojiaren.lz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.JiarenActiveActivity;
import com.cpstudio.zhuojiaren.R;

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
		initClick();
	}

	private void initClick() {
		// TODO Auto-generated method stub
		tvIFocusedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(JiarenActiveNumListActivity.this,
						JiarenActiveActivity.class);
				i.putExtra("mType", 2);
				startActivity(i);
			}
		});
		tvAllNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(JiarenActiveNumListActivity.this,
						JiarenActiveActivity.class);
				i.putExtra("mType", 1);
				startActivity(i);
			}
		});
	}

}
