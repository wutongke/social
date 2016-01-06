package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.ui.QuanMemberListAdapter;
import com.cpstudio.zhuojiaren.ui.ZhuoMaiCardActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoQuanMainActivity;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;

public class QuanziMemberFra extends Fragment {
	@InjectView(R.id.fqtl_list)
	ListView mListView;

	private QuanMemberListAdapter mAdapter;
	private ArrayList<UserNewVO> mList = new ArrayList<UserNewVO>();
	private ConnHelper mConnHelper = null;
	private ListViewFooter mListViewFooter = null;
	private Context mContext;
	String groupId = null;
	View layout;

	public interface functionListener {
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
		mAdapter = new QuanMemberListAdapter(getActivity(), mList);
		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position>mList.size()-1){
					return;
				}
				UserNewVO user = mList.get(position);
				if (user != null) {
					Intent intent = new Intent(mContext,
							ZhuoMaiCardActivity.class);
					intent.putExtra("userid", user.getUserid());
					startActivity(intent);
				}
			}

		});


		groupId = ((ZhuoQuanMainActivity) getActivity()).getGroupid();
		loadData();
		return layout;
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data, getActivity()
						.getApplicationContext());

				ArrayList<UserNewVO> list = nljh.parseUserNewList();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
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
			mConnHelper.getQuanMemberList(mUIHandler, MsgTagVO.DATA_LOAD,
					groupId);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
		}
	};
}
