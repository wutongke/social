package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
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
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.ImageRadioButton;
import com.cpstudio.zhuojiaren.ui.CrowdFundingDetailActivity;
import com.cpstudio.zhuojiaren.ui.CrowdFundingListActivity;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
/**
 * 众筹Fragmengt，包括我的众筹、寻找众筹等，
 * 使用gridView实现子选项，加入在下拉刷新列表的头部
 * @author lef
 *
 */
public class CrowdFundingFragment extends Fragment{
	@InjectView(R.id.fcd_pull_down_view)
	PullDownView pullDownView;
	private View view;
	private ListView mListView;
	private CrowdFundingAdapter mAdapter;
	private TitleAdapter mTitleAdapter;
	private ArrayList<CrowdFundingVO> mListDatas=new ArrayList<CrowdFundingVO>();
	private int type;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 view  = inflater.inflate(R.layout.fragment_crowdfunding, null);
		type = getArguments().getInt(CrowdFundingVO.CROWDFUNDINGTYPE,1);
		ButterKnife.inject(this,view);
		initPullDownView();
		loadData();
		return view;
	}
	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(getActivity(), R.layout.head_crowdfunding);
		pullDownView.setOnPullDownListener(new OnPullDownListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				loadData();
			}
			
			@Override
			public void onMore() {
				// TODO Auto-generated method stub
				loadData();
			}
		});
		pullDownView.setShowHeader();
		pullDownView.setShowFooter(false);
		mListView = pullDownView.getListView();
		mAdapter = new CrowdFundingAdapter(getActivity(), mListDatas, R.layout.item_crowdfunding);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CrowdFundingFragment.this.getActivity(),CrowdFundingDetailActivity.class);
				intent.putExtra(CrowdFundingVO.CROWDFUNDINGID,mListDatas.get(position-1).getFundingId());
				startActivity(intent);
			}
		});
		MyGridView gridView = (MyGridView) view.findViewById(R.id.hcd_gridview);
		final ArrayList<ImageRadioButton> list = new ArrayList<ImageRadioButton>();
		if(type==CrowdFundingVO.CROWDFUNDINGMY){
			list.add(new ImageRadioButton(R.drawable.startucrowd, R.drawable.startdcrowd));
			list.add(new ImageRadioButton(R.drawable.investucrowd, R.drawable.investdcrowd));
		}else if(type==CrowdFundingVO.CROWDFUNDINGQUERY){
			list.add(new ImageRadioButton(R.drawable.scienceucrowd, R.drawable.sciencedcrowd));
			list.add(new ImageRadioButton(R.drawable.publishingucrowd, R.drawable.publishingdcrowd));
			list.add(new ImageRadioButton(R.drawable.publicucrowd, R.drawable.publicdcrowd));
			list.add(new ImageRadioButton(R.drawable.gameucrowd, R.drawable.gamedcrowd));
			list.add(new ImageRadioButton(R.drawable.farmerucrowd, R.drawable.farmerdcrowd));
			list.add(new ImageRadioButton(R.drawable.artucrowd, R.drawable.artdcrowd));
		}
		mTitleAdapter = new TitleAdapter(getActivity(), list, R.layout.item_title_image);
		mTitleAdapter.setImageOnclick(new ImageOnclick() {
			
			@Override
			public void OnClickItem(ImageRadioButton item) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),CrowdFundingListActivity.class);
				int position = list.indexOf(item);
				if(type==CrowdFundingVO.CROWDFUNDINGMY){
					
					intent.putExtra("type", CrowdFundingVO.typeStr[position+1]);
				}else if(type==CrowdFundingVO.CROWDFUNDINGQUERY){
					intent.putExtra("type", CrowdFundingVO.typeStr[position+3]);
				}
				mTitleAdapter.notifyDataSetChanged();
				startActivity(intent);
			}
		});
		gridView.setAdapter(mTitleAdapter);
		//跳转到某个类型的list
//		gridView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}
	private void loadData() {
		// TODO Auto-generated method stub
		//test
		CrowdFundingVO test = new CrowdFundingVO();
		test.setFundingId("123");
		test.setName("asdf");
		test.setMinPrice("5");
		test.setMoneyGet("8000");
		pullDownView.finishLoadData(true);
		pullDownView.hasData();
		mListDatas.add(test);
		
		mAdapter.notifyDataSetChanged();
	}
}
