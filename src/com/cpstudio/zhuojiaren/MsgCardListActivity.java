package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.adapter.CardListAdapter;
import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MsgCardListActivity extends Activity {
	private ListView mListView;
	private CardListAdapter mAdapter;
	private ArrayList<CardMsgVO> mList = new ArrayList<CardMsgVO>();
	private ListViewFooter mListViewFooter = null;
	private CardMsgFacade mFacade = null;
	private String myid = null;
	private boolean toTab = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_list);
		Intent intent = getIntent();
		toTab = intent.getBooleanExtra("toTab", false);
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		mAdapter = new CardListAdapter(this, mList, myid);
		mListView = (ListView) findViewById(R.id.listView);
		LayoutInflater inflater = LayoutInflater.from(MsgCardListActivity.this);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setDividerHeight(0);
		mListView.setAdapter(mAdapter);
		mFacade = new CardMsgFacade(MsgCardListActivity.this);
		initClick();
		loadData("0");
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MsgCardListActivity.this.finish();
			}
		});
		// findViewById(R.id.buttonView).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// String type = "0";
		// if (v.getTag() != null && v.getTag().equals("viewMy")) {
		// type = "1";
		// v.setTag("viewAll");
		// v.setBackgroundResource(R.drawable.button_view_all);
		// } else {
		// v.setTag("viewMy");
		// v.setBackgroundResource(R.drawable.button_view_my);
		// }
		// loadData(type);
		// }
		// });
	}

	@Override
	protected void onResume() {
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		super.onResume();
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String type = "0";
			if (v.getTag() != null && v.getTag().equals("viewMy")) {
				type = "1";
			}
			loadMore(type);
		}
	};

	private void loadData(String type) {
		ArrayList<CardMsgVO> list = new ArrayList<CardMsgVO>();
		if (type.equals("0")) {
			list = mFacade.getAllByCondition(null, null, null,
					"addtime desc limit 0,20");
		} else {
			list = mFacade.getAllByCondition("senderid = ?",
					new String[] { myid }, null, "addtime desc limit 0,20");
		}
		if (!list.isEmpty()) {
			mList.clear();
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mFacade.updateReadState();
		} else {
			mListViewFooter.noData(false);
		}
	}

	private void loadMore(String type) {
		int offset = mList.size();
		ArrayList<CardMsgVO> list = new ArrayList<CardMsgVO>();
		if (type.equals("0")) {
			list = mFacade.getAllByCondition(null, null, null,
					"addtime desc limit 20 offset " + offset);
		} else {
			list = mFacade.getAllByCondition("senderid = ?",
					new String[] { myid }, null,
					"addtime desc limit 20 offset " + offset);
		}
		if (!list.isEmpty()) {
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mFacade.updateReadState();
		} else {
			mListViewFooter.noData(true);
		}
	}

	@Override
	public void finish() {
		if(toTab && !ResHelper.getInstance(getApplicationContext()).isAppShow()){
			Intent intent = new Intent(getApplicationContext(),
					TabContainerActivity.class);
			intent.putExtra(TabContainerActivity.SHOW_PAGE,
					TabContainerActivity.MSG_PAGE);
			startActivity(intent);
		}
		super.finish();
	}
}
