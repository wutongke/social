package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.facade.PlanFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.PlanVO;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class MyPlanActivity extends Activity implements OnItemClickListener,
		OnItemLongClickListener {
	private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();
	private ListView mListView;
	private SimpleAdapter mAdapter;
	private PlanFacade mPlanFacade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_plan);
		initClick();
		mPlanFacade = new PlanFacade(getApplicationContext());
		mAdapter = new SimpleAdapter(this, mList, R.layout.item_plan_list,
				new String[] { "id", "content", "addtime" },
				new int[] { R.id.textViewId, R.id.textViewTitle,
						R.id.textViewTime });
		mListView = (ListView) findViewById(R.id.listView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
	}

	@Override
	protected void onResume() {
		loadData();
		super.onResume();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.obj != null && !msg.obj.equals("")) {
				JsonHandler nljh = new JsonHandler((String) msg.obj,
						getApplicationContext());
				ArrayList<PlanVO> list = nljh.parsePlanList();
				updateItem(list);
			}
		}
	};

	private void updateItem(ArrayList<PlanVO> list) {
		mList.clear();
		((TextView) findViewById(R.id.userNameShow))
				.setText(getString(R.string.title_activity_my_plan) + "("
						+ list.size() + ")");
		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", list.get(i).getId());
			map.put("content", list.get(i).getContent());
			map.put("addtime", list.get(i).getAddtime());
			mList.add(map);
		}
		mAdapter.notifyDataSetChanged();
	}

	private void loadData() {
		ArrayList<PlanVO> list = mPlanFacade.getAll();
		updateItem(list);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(MyPlanActivity.this,
					MyPlanDetailActivity.class);
			i.putExtra("id", ((TextView) arg1.findViewById(R.id.textViewId))
					.getText().toString());
			i.putExtra("content", ((TextView) arg1
					.findViewById(R.id.textViewTitle)).getText().toString());
			i.putExtra("addtime", ((TextView) arg1
					.findViewById(R.id.textViewTime)).getText().toString());
			startActivity(i);
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyPlanActivity.this.finish();
			}
		});

		findViewById(R.id.buttonCreate).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MyPlanActivity.this,
								MyPlanCreateActivity.class);
						startActivity(intent);
					}
				});
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		final String id = ((TextView) arg1.findViewById(R.id.textViewId))
				.getText().toString();
		new AlertDialog.Builder(MyPlanActivity.this)
				.setItems(R.array.array_long_menu_plan,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									mPlanFacade.delete(id);
									loadData();
									break;
								default:
									break;
								}
							}
						}).setNegativeButton(R.string.CANCEL, null).show();

		return false;
	}
	
}
