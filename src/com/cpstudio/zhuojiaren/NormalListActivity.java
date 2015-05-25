package com.cpstudio.zhuojiaren;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class NormalListActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_normal_list);
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String[] list = intent.getStringArrayExtra("list");
		((TextView) findViewById(R.id.userNameShow)).setText(name);
		ListView listView = (ListView) findViewById(R.id.listView);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				NormalListActivity.this, R.layout.item_array, list);
		listView.setAdapter(adapter);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NormalListActivity.this.finish();
			}
		});
	}

}
