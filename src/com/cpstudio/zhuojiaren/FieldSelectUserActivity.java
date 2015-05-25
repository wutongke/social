package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.HangYeVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class FieldSelectUserActivity extends Activity implements
		OnItemClickListener {
	private ListView mListView;
	private SimpleAdapter mAdapter;
	List<Map<String, String>> contents = new ArrayList<Map<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_field_select);
		initClick();
		mAdapter = new SimpleAdapter(this, contents, R.layout.item_field_list,
				new String[] { "id", "name" }, new int[] {
						R.id.textViewGroup3Id, R.id.textViewGroup3Name });
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater
				.from(FieldSelectUserActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer2, null);
		View head = inflater.inflate(R.layout.listview_header11, null);
		mListView.addHeaderView(head);
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		loadData();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				findViewById(R.id.progressLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewFail).setVisibility(View.GONE);
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					ArrayList<HangYeVO> list = nljh.parseFieldList();
					if (!list.isEmpty()) {
						findViewById(R.id.textViewNoData).setVisibility(
								View.GONE);
						for (int i = 0; i < list.size(); i++) {
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", list.get(i).getId());
							map.put("name", list.get(i).getName());
							contents.add(map);
						}
						mAdapter.notifyDataSetChanged();
					} else {
						findViewById(R.id.textViewNoData).setVisibility(
								View.VISIBLE);
					}
				} else {
					findViewById(R.id.textViewFail).setVisibility(View.VISIBLE);
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		findViewById(R.id.textViewFail).setVisibility(View.GONE);
		findViewById(R.id.progressLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewNoData).setVisibility(View.GONE);
		Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
		String[] fields = getResources().getStringArray(
				R.array.array_field_type);
		StringBuffer sb = new StringBuffer();
		sb.append("{\"code\":\"10000\",\"msg\":\"\",\"data\":[{\"name\":\"");
		for (int i = 0; i < fields.length; i++) {
			if (i != fields.length - 1) {
				sb.append(fields[i]);
				sb.append("\"},{\"name\":\"");
			} else {
				sb.append(fields[i]);
				sb.append("\"}]}");
			}
		}
		String data = sb.toString();
		msg.obj = data;
		msg.sendToTarget();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent intent = new Intent(FieldSelectUserActivity.this,
					UserSameActivity.class);
			intent.putExtra("type", 2);
			intent.putExtra("mSearchKey", ((TextView) arg1
					.findViewById(R.id.textViewGroup3Name)).getText()
					.toString() + ":");
			startActivity(intent);
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FieldSelectUserActivity.this.finish();
			}
		});
	}
}
