package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;

import com.cpstudio.zhuojiaren.adapter.LinkListAdapter;
import com.cpstudio.zhuojiaren.facade.RelateFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.ContactVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.AlphabetComparator;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.IndexableListView;
import com.cpstudio.zhuojiaren.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class LinkListActivity extends Activity implements OnItemClickListener {
	private IndexableListView mListView;
	private LinkListAdapter mAdapter;
	private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private ZhuoConnHelper mConnHelper = null;
	private String mSearchKey = null;
	private RelateFacade mFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_link_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new RelateFacade(getApplicationContext(),
				RelateFacade.CONTRACTLIST);
		mAdapter = new LinkListAdapter(this, mList);
		mListView = (IndexableListView) findViewById(R.id.listview);
		LayoutInflater inflater = LayoutInflater.from(LinkListActivity.this);
		View mHeadView = inflater.inflate(R.layout.listview_header8, null);
		mListView.addHeaderView(mHeadView);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer2, null);
		mListView.addFooterView(mFooterView);
		mListView.setAdapter(mAdapter);
		mListView.setDividerHeight(0);
		mListView.setFastScrollEnabled(true);
		mListView.setOnItemClickListener(this);
		loadData();
		initClick();
	}

	private void initClick() {
		findViewById(R.id.linearLayoutBirthUserList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(LinkListActivity.this,
								UserBirthActivity.class);
						startActivity(i);
					}
				});

		findViewById(R.id.linearLayoutFollowList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(LinkListActivity.this,
								FanFollowActivity.class);
						i.putExtra("type", "0");
						startActivity(i);
					}
				});

		findViewById(R.id.linearLayoutFanList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(LinkListActivity.this,
								FanFollowActivity.class);
						i.putExtra("type", "1");
						startActivity(i);
					}
				});

		findViewById(R.id.linearLayoutTeacherList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(LinkListActivity.this,
								TeacherListActivity.class);
						startActivity(i);
					}
				});

		findViewById(R.id.linearLayoutAllUserList).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(LinkListActivity.this,
								UserAllActivity.class);
						startActivity(i);
					}
				});
		
		findViewById(R.id.linearLayoutGroups).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(LinkListActivity.this,
								QuanListMoreActivity.class);
						intent.putExtra("userid", ResHelper.getInstance(getApplicationContext()).getUserid());
						startActivity(intent);
					}
				});
		
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinkListActivity.this.finish();
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
					ContactVO contact = null;
					if (msg.obj instanceof ContactVO) {
						contact = (ContactVO) msg.obj;
					} else {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						contact = nljh.parseContact();
						if (null != contact) {
							mFacade.update((ArrayList<UserVO>) contact
									.getUsers());
						} else if (mSearchKey != null && !mSearchKey.equals("")) {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error18);
							return;
						}
					}
					String follownum = contact.getFollownum();
					String fansnum = contact.getFansnum();
					((TextView) findViewById(R.id.textViewFollowNum))
							.setText(follownum);
					((TextView) findViewById(R.id.textViewFanNum))
							.setText(fansnum);
					ArrayList<UserVO> list = (ArrayList<UserVO>) contact
							.getUsers();

					Collections.sort(list, new AlphabetComparator());
					if (!list.isEmpty()) {
						findViewById(R.id.textViewNoData).setVisibility(
								View.GONE);
						mList.clear();
						mList.addAll(list);
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
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2
				&& (mSearchKey == null || mSearchKey.equals(""))) {
			ContactVO contact = new ContactVO();
			RelateFacade fanFacade = new RelateFacade(getApplicationContext(),
					RelateFacade.FANLIST);
			RelateFacade followFacade = new RelateFacade(
					getApplicationContext(), RelateFacade.FOLLOWLIST);
			ArrayList<UserVO> list = mFacade.getAll();
			contact.setUsers(list);
			contact.setFansnum(String.valueOf(fanFacade.getAll().size()));
			contact.setFollownum(String.valueOf(followFacade.getAll().size()));
			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			msg.obj = contact;
			msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getUrlContact();
			if (null != mSearchKey) {
				params += "?key=" + mSearchKey.trim();
			}
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(LinkListActivity.this, ChatActivity.class);
			i.putExtra("userid", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}
}
