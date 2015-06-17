package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudui.zhuojiaren.lz.EventListAdapter;
import com.cpstudui.zhuojiaren.lz.ZhuoQuanMainActivity;

public class QuanziActiveFra extends Fragment {
	@InjectView(R.id.fqtl_list)
	ListView mListView;

	private EventListAdapter mAdapter;

	private ArrayList<EventVO> mList = new ArrayList<EventVO>();

	private ZhuoConnHelper mConnHelper = null;
	private int mPage = 1;
	private int mType = 6;
	private ListViewFooter mListViewFooter = null;
	private Context mContext;
	private String mLastId = null;
	// private PopupWindows pupWindow;

	// ��View
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

		// ���ص�Ȧ����ҳ���ͣ�Ȧ�ӻ��⣬Ȧ�ӻ�����Ȧ�ӳ�Ա
		Bundle intent = getArguments();
		mType = intent.getInt(QuanVO.QUANZIMAINTYPE);

		// if (mType == QuanVO.QUANZIACTIVE)
		// mAdapter = new ActiveListAdapter(getActivity(), activeList);
		// else if (mType == QuanVO.QUANZITOPIC)
		mAdapter = new EventListAdapter(getActivity(), mList);
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
				// Intent i = new Intent(getActivity(),
				// EventDetailActivity.class);
				// i.putExtra("eventId", mList.get(position).getEventId());
				// startActivity(i);
			}

		});

		// ���ԣ�����������
		// loadData();
		for (int i = 0; i < 8; i++)
			mList.add(new EventVO());
		mAdapter.notifyDataSetChanged();
		groupId = ((ZhuoQuanMainActivity) getActivity()).getGroupid();
		return layout;
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data, getActivity()
						.getApplicationContext());
				ArrayList<EventVO> list = nljh.parseEventInfoList();
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
					if (mList.size() > 0) {
						mLastId = mList.get(mList.size() - 1).getEventId();
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

		String url = ZhuoCommHelper.getUrlUserInfo()
				+ "?uid="
				+ ResHelper.getInstance(getActivity().getApplicationContext())
						.getUserid();

		// ����ˢ�¸�����Ϣ
		mConnHelper.getFromServer(url, mUIHandler, MsgTagVO.UPDATE);

		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			String params = ZhuoCommHelper.getUrlMsgList();
			params += "?pageflag=" + "0";
			params += "&reqnum=" + "10";
			params += "&lastid=" + "0";
			params += "&type=" + "0";

			params += "&gongxutype=" + "0";
			params += "&from=" + "0";
			params += "&uid=" + uid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlMsgList();
			params += "?pageflag=" + "1";
			params += "&reqnum=" + "10";
			params += "&lastid=" + mLastId;
			params += "&type=" + "0";
			params += "&gongxutype=" + "0";
			params += "&from=" + "0";
			params += "&uid=" + uid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			loadMore();
		}
	};
}