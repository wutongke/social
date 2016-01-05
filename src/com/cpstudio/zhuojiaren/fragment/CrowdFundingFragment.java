package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.CrowdFundingAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter.ImageOnclick;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.ImageRadioButton;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.ui.CrowdFundingDetailActivity;
import com.cpstudio.zhuojiaren.ui.CrowdFundingListActivity;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

/**
 * 众筹Fragmengt，包括我的众筹、寻找众筹等， 使用gridView实现子选项，加入在下拉刷新列表的头部
 * 
 * @author lef
 * 
 */
public class CrowdFundingFragment extends Fragment {
	@InjectView(R.id.fcd_pull_down_view)
	PullDownView pullDownView;
	private View view;
	private ListView mListView;
	private CrowdFundingAdapter mAdapter;
	private TitleAdapter mTitleAdapter;
	private ArrayList<CrowdFundingVO> mDatas = new ArrayList<CrowdFundingVO>();
	private int type;
	private int crowdType = 0;
	private boolean isInvest = false;
	// 分页
	private int mPage = 0;
	private AppClient appClientLef;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_crowdfunding, null);
		type = getArguments().getInt(CrowdFundingVO.CROWDFUNDINGTYPE, 1);
		ButterKnife.inject(this, view);
		initPullDownView();
		appClientLef = AppClient.getInstance(getActivity());
		loadData();
		return view;
	}

	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(getActivity(),
				R.layout.head_crowdfunding);
		pullDownView.setOnPullDownListener(new OnPullDownListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadData();
			}

			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				loadMore();
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
		mListView = pullDownView.getListView();
		mAdapter = new CrowdFundingAdapter(getActivity(), mDatas,
				R.layout.item_crowdfunding);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CrowdFundingFragment.this
						.getActivity(), CrowdFundingDetailActivity.class);
				intent.putExtra(CrowdFundingVO.CROWDFUNDINGID, mAdapter
						.getItem(position - 1).getId());
				startActivity(intent);
			}
		});
		MyGridView gridView = (MyGridView) view.findViewById(R.id.hcd_gridview);
		final ArrayList<ImageRadioButton> list = new ArrayList<ImageRadioButton>();
		if (type == CrowdFundingVO.CROWDFUNDINGMY) {
			gridView.setNumColumns(2);
			list.add(new ImageRadioButton(R.drawable.startucrowd,
					R.drawable.startdcrowd));
			list.add(new ImageRadioButton(R.drawable.investucrowd,
					R.drawable.investdcrowd));
		} else if (type == CrowdFundingVO.CROWDFUNDINGQUERY) {
			list.add(new ImageRadioButton(R.drawable.scienceucrowd,
					R.drawable.sciencedcrowd));
			list.add(new ImageRadioButton(R.drawable.publishingucrowd,
					R.drawable.publishingdcrowd));
			list.add(new ImageRadioButton(R.drawable.gameucrowd,
					R.drawable.publicdcrowd));
			list.add(new ImageRadioButton(R.drawable.artucrowd,
					R.drawable.gamedcrowd));
			list.add(new ImageRadioButton(R.drawable.farmerucrowd,
					R.drawable.farmerdcrowd));
			list.add(new ImageRadioButton(R.drawable.publicucrowd,
					R.drawable.artdcrowd));
		}
		mTitleAdapter = new TitleAdapter(getActivity(), list,
				R.layout.item_title_image);
		mTitleAdapter.setImageOnclick(new ImageOnclick() {

			@Override
			public void OnClickItem(ImageRadioButton item) {
				// TODO Auto-generated method stub
				int position = list.indexOf(item);
				if (type == CrowdFundingVO.CROWDFUNDINGMY) {
					if (position == 1)
						loadDataInvest();
					else{
						crowdType = 0;
						loadData();
					}
				} else if (type == CrowdFundingVO.CROWDFUNDINGQUERY) {
					crowdType = position+1;
					loadData();
				}
				mTitleAdapter.notifyDataSetChanged();
			}
		});
		gridView.setAdapter(mTitleAdapter);
		// 跳转到某个类型的list
		// gridView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	private void loadData() {
		mDatas.clear();
		mPage = 0;
		mAdapter.notifyDataSetChanged();
		// isMy=1 我发布 0 其他
		int isMy = 1;
		if (crowdType > 0)
			isMy = 0;
		appClientLef.getFundingList(isMy, crowdType, mPage, 5, uiHandler,
				MsgTagVO.DATA_LOAD, getActivity(), true, null, null);

	}

	private void loadDataInvest() {
		mDatas.clear();
		mPage = 0;
		mAdapter.notifyDataSetChanged();
		appClientLef.getFundingListInvest(mPage, 5, uiHandler,
				MsgTagVO.DATA_LOAD, getActivity(), true, null, null);
	}

	private void loadMore() {
		if (isInvest) {
			appClientLef.getFundingListInvest(mPage, 5, uiHandler,
					MsgTagVO.DATA_MORE, getActivity(), true, null, null);
		} else {
			int isMy = 1;
			if (crowdType > 0)
				isMy = 0;
			appClientLef.getFundingList(isMy, crowdType, mPage, 5, uiHandler,
					MsgTagVO.DATA_MORE, getActivity(), true, null, null);
		}

	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					return;
				}
				String data = res.getData();
				updateItemList(data, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				updateItemList((String) msg.obj, false, true);
				break;
			}
			}
			;
		}

	};

	private void updateItemList(String data, boolean refresh, boolean append) {
		// TODO Auto-generated method stub
		try {
			pullDownView.finishLoadData(true);
			if (data != null && !data.equals("")) {
				ArrayList<CrowdFundingVO> list = JsonHandler_Lef
						.parseFundingList(data);
				if (!list.isEmpty()) {
					if (!append) {
						mDatas.clear();
						pullDownView.hasData();
					}
					mDatas.addAll(list);
					mAdapter.notifyDataSetChanged();
					mPage++;
				} else {
					pullDownView.noData(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
