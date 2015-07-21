package com.cpstudio.zhuojiaren.ui;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.menu;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.zhuojiaren.sortlistview.NamePup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class GrouthChooseActivity extends BaseActivity {
	@InjectView (R.id.layout)
	LinearLayout ll;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_choose);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.grouth_online);
		
		findViewById(R.id.according_teacher).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PopupWindows(GrouthChooseActivity.this).showGrouthTeacher(ll);
			}
		});
		findViewById(R.id.according_type).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new PopupWindows(GrouthChooseActivity.this).showGrouthType(ll, 0);
			}
		});
	}
	OnClickListener toActivity = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(GrouthChooseActivity.this,GrouthListActivity.class);
			intent.putExtra("type", 1);
			intent.putExtra("teacher", 1);
			GrouthChooseActivity.this.startActivity(intent);
			GrouthChooseActivity.this.finish();
		}
	};
}
