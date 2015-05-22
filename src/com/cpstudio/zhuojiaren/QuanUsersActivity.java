package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.Collections;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.LinkListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanUserVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.AlphabetComparator;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.IndexableListView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class QuanUsersActivity extends Activity implements OnItemClickListener {

	private IndexableListView mListView;
	private LinkListAdapter mAdapter;
	private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private String mSearchKey = "";
	private String groupid = null;
	private ZhuoConnHelper mConnHelper = null;
	private int mPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_users);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		mAdapter = new LinkListAdapter(this, mList);
		mListView = (IndexableListView) findViewById(R.id.listview);
		inflater = LayoutInflater.from(QuanUsersActivity.this);
		View mHeadView = inflater.inflate(R.layout.listview_header6, null);
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
						Intent i = new Intent(QuanUsersActivity.this,
								UserBirthActivity.class);
						i.putExtra("groupid", groupid);
						startActivity(i);
					}
				});

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuanUsersActivity.this.finish();
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

	private void updateHeadview(ArrayList<QuanUserVO> managers) {
		for (QuanUserVO member : managers) {
			View rowView = inflater.inflate(R.layout.item_link_list, null);
			UserVO user = member.getUser();
			String userid = user.getUserid();
			String authorName = user.getUsername();
			String headUrl = user.getUheader();
			String work = user.getPost();
			rowView.setTag(userid);
			TextView nameTV = (TextView) rowView
					.findViewById(R.id.textViewAuthorName);
			nameTV.setText(authorName);
			TextView workTV = (TextView) rowView
					.findViewById(R.id.textViewContent);
			workTV.setText(work);
			ImageView headIV = (ImageView) rowView
					.findViewById(R.id.imageViewAuthorHeader);
			headIV.setTag(headUrl);
			mLoadImage.addTask(headUrl, headIV);
			rowView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String userid = (String) v.getTag();
					if (null != userid) {
						Intent i = new Intent(QuanUsersActivity.this,
								UserHomeActivity.class);
						i.putExtra("userid", userid);
						startActivity(i);
					}
				}
			});
			if (member.getGlevel().equals("1")) {
				((LinearLayout) findViewById(R.id.linearLayoutManager))
						.addView(rowView);
			} else if (member.getGlevel().equals("0")) {
				((LinearLayout) findViewById(R.id.linearLayoutCreater))
						.addView(rowView);
			}
		}
		mLoadImage.doTask();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			findViewById(R.id.progressLoading).setVisibility(View.GONE);
			findViewById(R.id.textViewLoading).setVisibility(View.GONE);
			findViewById(R.id.textViewFail).setVisibility(View.GONE);
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				ArrayList<QuanUserVO> memberlist = nljh.parseGroupMemberList();
				ArrayList<UserVO> list = new ArrayList<UserVO>();
				ArrayList<QuanUserVO> managers = new ArrayList<QuanUserVO>();
				for (QuanUserVO member : memberlist) {
					if (member.getGlevel().equals("2")) {
						list.add(member.getUser());
					} else {
						managers.add(member);
					}
				}
				updateHeadview(managers);
				Collections.sort(list, new AlphabetComparator());
				if (!list.isEmpty()) {
					findViewById(R.id.textViewNoData).setVisibility(View.GONE);
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				} else {
					if (mSearchKey != null && !mSearchKey.equals("")) {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error18);
					}
					findViewById(R.id.textViewNoData).setVisibility(
							View.VISIBLE);
				}
			} else {
				findViewById(R.id.textViewFail).setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				updateItemList((String) msg.obj, true, false);
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
		mPage = 1;
		String params = ZhuoCommHelper.getUrlGroupMembers();
		params += "?page=" + mPage;
		params += "&groupid=" + groupid;
		if (mSearchKey != null && !mSearchKey.trim().equals("")) {
			params += "&key=" + mSearchKey.trim();
		}
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			String userid = (String) arg1.getTag(R.id.tag_id);
			if (null != userid) {
				Intent i = new Intent(QuanUsersActivity.this,
						UserCardActivity.class);
				i.putExtra("userid", userid);
				startActivity(i);
			}
		}
	}

}
