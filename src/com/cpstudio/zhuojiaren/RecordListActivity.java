package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cpstudio.zhuojiaren.adapter.RecordListAdapter;
import com.cpstudio.zhuojiaren.facade.RecordChatFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class RecordListActivity extends Activity implements OnItemClickListener {

	private ListView mListView;
	private RecordListAdapter mAdapter;
	private RecordChatFacade mFacade = null;
	private ArrayList<ImMsgVO> mList = new ArrayList<ImMsgVO>();
	private String mSearchKey = "";
	private String myid = null;
	private boolean toTab = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_list);
		Intent intent = getIntent();
		toTab = intent.getBooleanExtra("toTab", false);
		mFacade = new RecordChatFacade(getApplicationContext());
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mListView = (ListView) findViewById(R.id.listView);
		mAdapter = new RecordListAdapter(this, mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
	}

	@Override
	protected void onResume() {
		loadData();
		super.onResume();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						RecordListActivity.this.finish();
					}
				});
		findViewById(R.id.buttonRecord).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(RecordListActivity.this,
								MyRecordActivity.class);
						startActivity(i);
					}
				});
		final EditText searchView = (EditText) findViewById(R.id.editTextSearch);
		final View delSearch = findViewById(R.id.imageViewDelSearch);
		searchView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					mSearchKey = v.getText().toString();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					loadData();
				}
				return false;
			}
		});
		searchView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (searchView.getText().toString().equals("")) {
					delSearch.setVisibility(View.GONE);
				} else {
					delSearch.setVisibility(View.VISIBLE);
				}
			}
		});

		delSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				searchView.setText("");
				if (mSearchKey != null && !mSearchKey.equals("")) {
					mSearchKey = "";
					loadData();
				}
			}
		});
	}

	private void loadData() {
		Map<String, ImMsgVO> map = new HashMap<String, ImMsgVO>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		ArrayList<ImMsgVO> list1 = mFacade.getAllByCondition("senderid <> ?",
				new String[] { myid }, "senderid", "length(id) desc,id desc",
				mSearchKey.trim());
		ArrayList<ImMsgVO> list2 = mFacade.getAllByCondition("receiverid <> ?",
				new String[] { myid }, "receiverid", "length(id) desc,id desc",
				mSearchKey.trim());
		for (ImMsgVO msg : list1) {
			map.put(msg.getSender().getUserid(), msg);
		}
		for (ImMsgVO msg : list2) {
			try {
				if (map.get(msg.getReceiver().getUserid()) == null) {
					map.put(msg.getReceiver().getUserid(), msg);
				} else {
					String id1 = map.get(msg.getReceiver().getUserid()).getId();
					String id2 = msg.getId();
					if (Long.valueOf(id1) < Long.valueOf(id2)) {
						map.put(msg.getReceiver().getUserid(), msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ArrayList<ImMsgVO> list = new ArrayList<ImMsgVO>();
		for (String userid : map.keySet()) {
			if (map.get(userid).getIsread().equals("0")) {
				list.add(map.get(userid));
				map2.put(userid, mFacade.getUnreadById(userid).size());
			}
		}
		for (String userid : map.keySet()) {
			if (map.get(userid).getIsread().equals("1")) {
				list.add(map.get(userid));
				map2.put(userid, 0);
			}
		}
		mList.clear();
		mAdapter.setUnreadCount(map2);
		mList.addAll(list);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(RecordListActivity.this,
					RecordUserActivity.class);
			String userid = (String) arg1.getTag(R.id.tag_id);
			i.putExtra("userid", userid);
			startActivity(i);
		}
	}
	
	@Override
	public void finish() {
		if(toTab && !ResHelper.getInstance(getApplicationContext()).isAppShow()){
			Intent intent = new Intent(getApplicationContext(),
					TabContainerActivity.class);
			intent.putExtra(TabContainerActivity.SHOW_PAGE,
					TabContainerActivity.HOME_PAGE);
			startActivity(intent);
		}
		super.finish();
	}
}
