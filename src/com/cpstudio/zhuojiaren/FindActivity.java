package com.cpstudio.zhuojiaren;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.ui.FieldSelectUserActivity;
import com.cpstudio.zhuojiaren.ui.UserSameActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class FindActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FindActivity.this.finish();
			}
		});
		findViewById(R.id.relativeLayoutZJQZ).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(FindActivity.this,
								ZhuoQuanActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutTCJR).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(FindActivity.this,
								UserSameActivity.class);
						i.putExtra("type", 1);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutTHJR).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(FindActivity.this,
								FieldSelectUserActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutTXJR).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(FindActivity.this,
								UserSameActivity.class);
						i.putExtra("type", 3);
						startActivity(i);
					}
				});

		findViewById(R.id.relativeLayoutZYGX).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(FindActivity.this,
								MsgResourceActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.relativeLayoutZXDS).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(FindActivity.this,
								UserSameActivity.class);
						startActivity(i);
					}
				});
		
		findViewById(R.id.relativeLayoutSYJR).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(FindActivity.this,
								UserAllActivity.class);
						startActivity(i);
					}
				});

	}
}
