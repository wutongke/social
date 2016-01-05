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
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudui.zhuojiaren.lz.QuanMemberListAdapter;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;
import com.cpstudui.zhuojiaren.lz.ZhuoQuanMainActivity;

public class QuanziMemberFra extends Fragment {
	@InjectView(R.id.fqtl_list)
	ListView mListView;

	private QuanMemberListAdapter mAdapter;
	// private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	private ArrayList<UserNewVO> mList = new ArrayList<UserNewVO>();
	// 需要改
	// private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private ConnHelper mConnHelper = null;
	private int mPage = 1;
	private int mType = 6;
	private ListViewFooter mListViewFooter = null;
	private Context mContext;
	private String mLastId = null;
	// private PopupWindows pupWindow;
	String groupId = null;
	// 主View
	View layout;
	private String uid = null;

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

		uid = ResHelper.getInstance(getActivity().getApplicationContext())
				.getUserid();

		// 加载的圈子主页类型：圈子话题，圈子互动，圈子成员
		Bundle intent = getArguments();
		mType = intent.getInt(QuanVO.QUANZIMAINTYPE);

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

		// for (int i = 0; i < 8; i++)
		// mList.add(new UserNewVO());
		// mAdapter.notifyDataSetChanged();

		groupId = ((ZhuoQuanMainActivity) getActivity()).getGroupid();
		// 测试，先不用
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
					if (mList.size() > 0) {
						// mLastId = mList.get(mList.size() - 1).get;
					}
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
			mConnHelper.getQuanMemberList(mUIHandler, MsgTagVO.DATA_LOAD,
					groupId);
		}
	}

	private void loadMore() {
		// if (mListViewFooter.startLoading()) {
		// mList.clear();
		// mAdapter.notifyDataSetChanged();
		// mPage = 0;
		// mConnHelper.getQuanMemberList(mUIHandler, MsgTagVO.DATA_LOAD,
		// groupId);
		// }
	}

	private OnClickListener onMoreClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// loadMore();
		}
	};
}
