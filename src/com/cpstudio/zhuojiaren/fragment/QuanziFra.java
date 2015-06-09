package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.QuanDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.QuanListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class QuanziFra extends Fragment {
	@InjectView(R.id.fql_list)
	ListView mListView;
	@InjectView(R.id.fql_my_layout)
	RadioGroup myQuanziLayout;
	@InjectView(R.id.fql_my_create)
	RadioButton myCreate;
	@InjectView(R.id.fql_my_add)
	RadioButton myAdd;
	@InjectView(R.id.fql_quanzi_recommend)
	GridView quanziRecommend;
	@InjectView(R.id.fql_footer)
	LinearLayout shareFooter;
	@InjectView(R.id.fql_share)
	TextView share;
	@InjectView(R.id.fql_dissolve)
	TextView dissolve;
	private QuanListAdapter mAdapter;
	private ArrayList<QuanVO> mList = new ArrayList<QuanVO>();
	private ZhuoConnHelper mConnHelper = null;
	private int mPage = 1;
	private int mType = 6;
	private ListViewFooter mListViewFooter = null;
	private Context mContext;
	private PopupWindows pupWindow;
	//主View
	View layout;
	public interface functionListener {
		//
		public void onTypeChange(int type);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.fragment_quanzi_list,
				container, false);
		ButterKnife.inject(this, layout);
		pupWindow = new PopupWindows(getActivity());
		mContext = getActivity();
		mConnHelper = ZhuoConnHelper.getInstance(getActivity()
				.getApplicationContext());
		// 加载的圈子类型
		Bundle intent = getArguments();
		mType = intent.getInt(QuanVO.QUANZITYPE);

		mAdapter = new QuanListAdapter(mContext, mList);
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
				if (position != -1) {
					String groupid = (String) view.getTag(R.id.tag_id);
					Intent i = new Intent(mContext, QuanDetailActivity.class);
					i.putExtra("groupid", groupid);
					startActivity(i);
				}
			}

		});
		initOnclick();
		loadData();
		return layout;
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		//1为解散，2为退出
		dissolve.setTag(1);
		dissolve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((Integer)(v.getTag())==1) {
					// TODO Auto-generated method stub
					pupWindow.showBreakQuanzi(1,layout,
							R.layout.popup_window_break_quazi, 10,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

								}
							});
				}else if((Integer)(v.getTag())==2){
					pupWindow.showBreakQuanzi(2,layout,
							R.layout.popup_window_off_quazi, 10,
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub

								}
							});
				}
			}
		});
		
		// 如果是我的圈子，则展示我的圈子
		if (mType == 6) {
			myQuanziLayout.setVisibility(View.VISIBLE);
			//设置默认选中第一个；
			myQuanziLayout.check(myCreate.getId());
			myQuanziLayout.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					if(checkedId==myCreate.getId()){
						loadData();
						dissolve.setTag(1);
						dissolve.setText(R.string.dissolve);
					}
						
					else if(checkedId==myAdd.getId()){
						dissolve.setTag(2);
						dissolve.setText(R.string.off);
						loadData();
					}
				}
			});
		} else if (mType == 1) {
			quanziRecommend.setVisibility(View.VISIBLE);
			
		}

	}

	/**
	 * 管理圈子
	 */
	public void setManager() {
		if (mAdapter != null) {
			mAdapter.setManagerVisible(true);
			mAdapter.notifyDataSetChanged();
		}
		 shareFooter.setVisibility(View.VISIBLE);
	}

	/**
	 * 退出管理
	 */
	public void offManager() {
		if (mAdapter != null) {
			mAdapter.setManagerVisible(false);
			mAdapter.notifyDataSetChanged();
		}
		shareFooter.setVisibility(View.GONE);
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data, getActivity()
						.getApplicationContext());
				ArrayList<QuanVO> list = nljh.parseQuanList();
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
			mPage = 1;
			String params = ZhuoCommHelper.getUrlGroupList() + "?page=" + mPage;
			params += "&type=" + mType;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlGroupList() + "?page=" + mPage;
			params += "&type=" + mType;
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
