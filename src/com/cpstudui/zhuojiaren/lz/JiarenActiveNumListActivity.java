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
import com.cpstudio.zhuojiaren.JiarenActiveSimpleActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.Dynamic;

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
						JiarenActiveSimpleActivity.class);
				i.putExtra("mType", Dynamic.DYNATIC_TYPE_IFOCUS_JIAREN);
				startActivity(i);
			}
		});
		tvAllNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(JiarenActiveNumListActivity.this,
						JiarenActiveSimpleActivity.class);
				i.putExtra("mType", Dynamic.DYNATIC_TYPE_ALL_JIAREN);
				startActivity(i);
			}
		});
	}

}
