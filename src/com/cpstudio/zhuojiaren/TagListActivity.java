package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.utils.NumberUtil;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class TagListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_list);
		Intent intent = getIntent();
		String name = intent.getStringExtra("name");
		String title = intent.getStringExtra("title");
		String[] list = intent.getStringArrayExtra("list");
		ArrayList<Map<String, String>> mList = new ArrayList<Map<String, String>>(
				list.length);
		int i = 1;
		for (String item : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", title + NumberUtil.numberArab2CN(i));
			map.put("content", item);
			mList.add(map);
			i++;
		}
		((TextView) findViewById(R.id.userNameShow)).setText(name);
		ListView listView = (ListView) findViewById(R.id.listView);
		SimpleAdapter adapter = new SimpleAdapter(TagListActivity.this, mList,
				R.layout.item_tag_list, new String[] { "title", "content" },
				new int[] { R.id.textViewTitle, R.id.textViewContent });
		listView.setAdapter(adapter);
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TagListActivity.this.finish();
			}
		});
	}
	
}
