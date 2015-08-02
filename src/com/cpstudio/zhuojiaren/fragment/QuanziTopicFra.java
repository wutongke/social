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

import com.cpstudio.zhuojiaren.JiarenActiveActivity;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.PublishActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelperLz;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudui.zhuojiaren.lz.QuanziTopicListAdapter;
import com.cpstudui.zhuojiaren.lz.TopicDetailActivity;
import com.cpstudui.zhuojiaren.lz.ZhuoQuanMainActivity;

public class QuanziTopicFra extends Fragment {
	@InjectView(R.id.fqtl_list)
	ListView mListView;

	private QuanziTopicListAdapter mAdapter;
	// private ArrayList<UserVO> mList = new ArrayList<UserVO>();
	// private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private ArrayList<QuanTopicVO> mList = new ArrayList<QuanTopicVO>();

	private ZhuoConnHelper mConnHelper = null;
	private int mPage =0;
	private int mPageSize = 5;
	private int mType = 5;
	private ListViewFooter mListViewFooter = null;
	private Context mContext;
	private String mLastId = null;
	// private PopupWindows pupWindow;

	// 主View
	View layout;
	private String uid = null;
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
		mConnHelper = ZhuoConnHelper.getInstance(getActivity()
				.getApplicationContext());

		uid = ResHelper.getInstance(getActivity().getApplicationContext())
				.getUserid();

		// 加载的圈子主页类型：圈子话题，圈子互动，圈子成员
		Bundle intent = getArguments();
		mType = intent.getInt(QuanVO.QUANZIMAINTYPE);

		// if (mType == QuanVO.QUANZIACTIVE)
		// mAdapter = new ActiveListAdapter(getActivity(), activeList);
		// else if (mType == QuanVO.QUANZITOPIC)
		mAdapter = new QuanziTopicListAdapter(this, mList);
		// else
		// mAdapter = new QuanMemberListAdapter(getActivity(), memberList);

		RelativeLayout mFooterView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 跳到话题详情页，话题详情内容用活动内容测试通过
				Intent i = new Intent();
				QuanTopicVO vo=mList.get(position);
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
					if (mList.size() > 0) {
						mLastId = mList.get(mList.size() - 1).getTopicid();
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
				// 通过返回数据判断是否属于该圈子，然后就行设置，暂时假定属于
				mAdapter.setIsFollow(false);
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			case MsgTagVO.UPDATE: {
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getActivity().getApplicationContext());
					UserVO user = nljh.parseUser();
					if (null != user) {
						UserFacade facade = new UserFacade(getActivity()
								.getApplicationContext());
						facade.saveOrUpdate(user);
					}
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		// 重要：：：：此处请求的返回结果中还应包含“我”是否在这个圈子之中的标志
		// String url = ZhuoCommHelper.getUrlUserInfo()
		// + "?uid="
		// + ResHelper.getInstance(getActivity().getApplicationContext())
		// .getUserid();
		//
		// // 加载刷新个人信息
		// mConnHelper.getFromServer(url, null,mUIHandler, MsgTagVO.UPDATE);

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
			// if (requestCode == MsgTagVO.MSG_CMT) {
			// String forward = data.getStringExtra("forward");
			// String msgid = data.getStringExtra("msgid");
			// String outterid = data.getStringExtra("outterid");
			Toast.makeText(mContext, "评论成功！", 2000).show();
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
