package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.ui.BaseActivity;

import android.os.Bundle;
import android.widget.TextView;
import butterknife.InjectView;

public class AboutZhuomaiActivity extends BaseActivity {
	@InjectView(R.id.tvAboutZM)
	TextView tvAboutZM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_zhuomao);
		initTitle();
		title.setText(R.string.label_my_about);
	}
}
