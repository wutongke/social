package com.cpstudui.zhuojiaren.lz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.JiarenActiveSimpleActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.ui.BaseActivity;

public class JiarenActiveNumListActivity extends BaseActivity {
	@InjectView(R.id.rlIFocusedNum)
	View vIFocusedNum;
	@InjectView(R.id.rlAllNum)
	View vAllNum;

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
		vIFocusedNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(JiarenActiveNumListActivity.this,
						JiarenActiveSimpleActivity.class);
				i.putExtra("mType", Dynamic.DYNATIC_TYPE_IFOCUS_JIAREN);
				startActivity(i);
			}
		});
		vAllNum.setOnClickListener(new OnClickListener() {

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
