package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.CrowdFundingAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.ImageRadioButton;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.ui.CrowdFundingListActivity;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.GongXuDetailActivity;
import com.cpstudui.zhuojiaren.lz.ResourceGXAdapter;
/**
 * 众筹Fragmengt，包括我的众筹、寻找众筹等，
 * 使用gridView实现子选项，加入在下拉刷新列表的头部
 * @author lef
 *
 */
public class ResourceGXFragment extends Fragment{
	@InjectView(R.id.fcd_pull_down_view)
	PullDownView pullDownView;
//	@InjectView(R.id.editTextSearch)
//	EditText searchView;
//	@InjectView(R.id.imageViewDelSearch)
//	View delSearch;
	EditText searchView;
	View delSearch;
	String mSearchKey;
	private View view;
	private ListView mListView;
	private ResourceGXAdapter mAdapter;
	private TitleAdapter mTitleAdapter;
	private ArrayList<ResourceGXVO> mListDatas=new ArrayList<ResourceGXVO>();
	//用于数据筛选
	private int type=0;//资源还是需求
	private int item=0;
	
	String  subType;//过滤子类
	String location=null;//区域
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view  = inflater.inflate(R.layout.fragment_resource_gx, null);
		type = getArguments().getInt(ResourceGXVO.RESOURCEGXTYPE,1);
		ButterKnife.inject(this,view);
		initPullDownView();
		loadData();
		return view;
	}
	private void initPullDownView() {
		// TODO Auto-generated method stub
		
		pullDownView.initHeaderViewAndFooterViewAndListView(getActivity(), R.layout.head_resource_gx);
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
		mAdapter = new ResourceGXAdapter(getActivity(), mListDatas, R.layout.item_resource_gx);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						GongXuDetailActivity.class);
//暂时写死，测试				intent.putExtra("msgid",mListDatas.get(arg2).getMsgId());
				intent.putExtra("msgid","123");
				startActivity(intent);
			}
		});
		MyGridView gridView = (MyGridView) view.findViewById(R.id.hcd_gridview);
		final ArrayList<ImageRadioButton> list = new ArrayList<ImageRadioButton>();
		if(type==ResourceGXVO.RESOURCE_FIND){//此处需要加载不同图片
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
		}else if(type==ResourceGXVO.NEED_FIND){
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
			list.add(new ImageRadioButton(R.drawable.bg_grid, R.drawable.bg_grid_on));
		}
		mTitleAdapter = new TitleAdapter(getActivity(), list, R.layout.item_title_image);
		gridView.setAdapter(mTitleAdapter);
		//跳转到某个类型的list
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
			//	重新请求数据刷新列表
				item=position;
				loadData();
//				Intent intent = new Intent(getActivity(),CrowdFundingListActivity.class);
//				if(type==ResourceGXVO.RESOURCE_FIND){
//					intent.putExtra("type", CrowdFundingVO.typeStr[position+1]);
//				}else if(type==CrowdFundingVO.CROWDFUNDINGQUERY){
//					intent.putExtra("type", CrowdFundingVO.typeStr[position+3]);
//				}
//				startActivity(intent);
			}
		});
		
		searchView=(EditText) pullDownView.findViewById(R.id.editTextSearch);
		searchView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					mSearchKey = v.getText().toString();
					InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
		delSearch= pullDownView.findViewById(R.id.imageViewDelSearch);
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
	
	
	public void filterData(String loc,String subType)
	{
		location=loc;
		this.subType=subType;
		loadData();
	}
	
	private void loadData() {
		// TODO Auto-generated method stub
		//test
		//根据参数type和item来加载数据
		for(int i=0;i<10;i++)
			mListDatas.add(new ResourceGXVO());
		pullDownView.finishLoadData(true);
		pullDownView.hasData();
		mAdapter.notifyDataSetChanged();
	}
}
