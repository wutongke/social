package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.QuanziTopicListAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.ui.TopicDetailActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanMainActivity;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

public class QuanziTopicFra extends Fragment {
	@InjectView(R.id.fqtl_list)
	ListView mListView;

	private QuanziTopicListAdapter mAdapter;
	// private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	// private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private ArrayList<QuanTopicVO> mList = new ArrayList<QuanTopicVO>();

	private ConnHelper mConnHelper = null;
	private int mPage = 0;
	private int mPageSize = 5;
	private int role = QuanVO.QUAN_ROLE_YOUKE;
	private ListViewFooter mListViewFooter = null;
	private Context mContext;
	View layout;
	String groupId = null;

	public interface functionListener {
		//
		public void onTypeChange(int type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.fragment_quan_main_list, container,
				false);
		ButterKnife.inject(this, layout);

		mContext = getActivity();
		mConnHelper = ConnHelper.getInstance(getActivity()
				.getApplicationContext());

		Bundle intent = getArguments();
		role = intent.getInt(QuanVO.QUANROLE);

		mAdapter = new QuanziTopicListAdapter(this, mList, role);

		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position>mList.size()-1)
					return;
				Intent i = new Intent();
				QuanTopicVO vo = mList.get(position);
				i.setClass(getActivity(), TopicDetailActivity.class);
				i.putExtra("topicid", vo.getTopicid());
				startActivity(i);
			}

		});

		groupId = ((ZhuoQuanMainActivity) getActivity()).getGroupid();
		mAdapter.setGroupId(groupId);
		loadData();
		return layout;
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data, getActivity()
						.getApplicationContext());
				ArrayList<QuanTopicVO> list = nljh.parseQuanTopicList();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					mPage++;
				} else {
					mListViewFooter.noData(!refresh);
				}
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
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}
		}
	};

	private void loadData() {

		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 0;
			mConnHelper.getQuanTopicList(mUIHandler, MsgTagVO.DATA_LOAD,
					groupId, null, mPage, mPageSize);
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK) {
			Toast.makeText(mContext, "ÆÀÂÛ³É¹¦£¡", 2000).show();
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			mConnHelper.getQuanTopicList(mUIHandler, MsgTagVO.DATA_MORE,
					groupId, null, mPage, mPageSize);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			loadMore();
		}
	};
}
