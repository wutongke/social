package com.cpstudio.zhuojiaren.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ResourceGXAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter;
import com.cpstudio.zhuojiaren.adapter.TitleAdapter.ImageOnclick;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.GXTypeCodeData;
import com.cpstudio.zhuojiaren.model.GXTypeItemVO;
import com.cpstudio.zhuojiaren.model.ImageRadioButton;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.gtype;
import com.cpstudio.zhuojiaren.ui.GongXuDetailActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class ResourceGXFragment extends Fragment {
	@InjectView(R.id.fcd_pull_down_view)
	PullDownView pullDownView;
	EditText searchView;
	View delSearch;
	String mSearchKey;
	private View view;
	private ListView mListView;
	private ResourceGXAdapter mAdapter;
	private TitleAdapter mTitleAdapter;
	private ArrayList<ResourceGXVO> mDatas = new ArrayList<ResourceGXVO>();
	// 用于数据筛选
	private int type = 0;// 资源还是需求
	private int item = -1;

	int subType = -1;// 过滤子类
	String location = null;// 区域
	private int mPage = 0;
	private ConnHelper appClientLef;
	List<gtype> gtypes;

	void getCodedData() {
		GXTypeCodeData baseCodeData = ConnHelper.getInstance(getActivity())
				.getGxTypeCodeDataSet();
		gtypes = new ArrayList<gtype>();
		if (baseCodeData != null) {

			for (GXTypeItemVO item : baseCodeData.getSupply()) {
				if (item.getSdtype() != null && item.getSdtype().size() > 0)
					gtypes.add(new gtype(item.getSdtype().get(0).getId(), item
							.getTitle()));

			}
			for (GXTypeItemVO item : baseCodeData.getDemand()) {
				if (item.getSdtype() != null && item.getSdtype().size() > 0)
					gtypes.add(new gtype(item.getSdtype().get(0).getId(), item
							.getTitle()));

			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_resource_gx, null);
		type = getArguments().getInt(ResourceGXVO.RESOURCEGXTYPE, 0);
		ButterKnife.inject(this, view);
		appClientLef = ConnHelper.getInstance(getActivity());
		initPullDownView();
		getCodedData();
		loadData();
		return view;
	}

	private void initPullDownView() {
		// TODO Auto-generated method stub

		pullDownView.initHeaderViewAndFooterViewAndListView(getActivity(),
				R.layout.head_resource_gx);
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
		mAdapter = new ResourceGXAdapter(getActivity(), mDatas,
				R.layout.item_resource_gx);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						GongXuDetailActivity.class);
				intent.putExtra("msgid", mDatas.get(arg2 - 1).getSdid());
				startActivity(intent);
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ResourceGXFragment.this.getActivity(),
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle("删除信息？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										appClientLef
												.deleteGongxu(
														mDatas.get(position - 1)
																.getSdid(),
														uiHandler,
														MsgTagVO.MSG_DEL,
														getActivity());
										mDatas.remove(position - 1);
										mAdapter.notifyDataSetChanged();
									}
								}).setNegativeButton("取消", null).create()
						.show();
				return true;
			}
		});
		MyGridView gridView = (MyGridView) view.findViewById(R.id.hcd_gridview);
		final ArrayList<ImageRadioButton> list = new ArrayList<ImageRadioButton>();
		if (type == ResourceGXVO.RESOURCE_FIND) {// 此处需要加载不同图片
			list.add(new ImageRadioButton(R.drawable.moneyu, R.drawable.moneyd));

			list.add(new ImageRadioButton(R.drawable.busyu, R.drawable.busyd));
			list.add(new ImageRadioButton(R.drawable.talentu,
					R.drawable.talentd));
			list.add(new ImageRadioButton(R.drawable.technologyu,
					R.drawable.technologyd));
			list.add(new ImageRadioButton(R.drawable.personu,
					R.drawable.persond));
			list.add(new ImageRadioButton(R.drawable.wisdomu,
					R.drawable.wisdomd));
		} else if (type == ResourceGXVO.NEED_FIND) {
			list.add(new ImageRadioButton(R.drawable.moneyu_1,
					R.drawable.moneyd_1));

			list.add(new ImageRadioButton(R.drawable.busyu_1,
					R.drawable.busyd_1));
			list.add(new ImageRadioButton(R.drawable.talentu_1,
					R.drawable.talentd_1));
			list.add(new ImageRadioButton(R.drawable.technologyu_1,
					R.drawable.technologyd_1));
			list.add(new ImageRadioButton(R.drawable.personu_1,
					R.drawable.persond_1));
			list.add(new ImageRadioButton(R.drawable.wisdomu_1,
					R.drawable.wisdomd_1));
		}
		mTitleAdapter = new TitleAdapter(getActivity(), list,
				R.layout.item_title_image, true);

		mTitleAdapter.setImageOnclick(new ImageOnclick() {

			@Override
			public void OnClickItem(ImageRadioButton btnview) {
				// TODO Auto-generated method stub
				int i = 0;
				for (ImageRadioButton temp : list) {
					if (btnview.equals(temp)) {
						break;
					}
					i++;
				}
				// 0资源1需求
				int index = 0;
				if (type == 1)
					index = 6;
				index += i;
				if (index >= gtypes.size())
					index = 0;
				subType = gtypes.get(index).getId();
				if (item != i) {
					mTitleAdapter.notifyDataSetChanged();
					item = index;
					// 重新请求数据刷新列表
					loadData();
				}
			}
		});

		gridView.setAdapter(mTitleAdapter);

		searchView = (EditText) pullDownView.findViewById(R.id.editTextSearch);
		searchView.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					mSearchKey = v.getText().toString();
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
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
		delSearch = pullDownView.findViewById(R.id.imageViewDelSearch);
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

	public void filterData(String loc, int subType) {
		location = loc;
		this.subType = subType;
		loadData();
	}

	private void loadData() {
		mDatas.clear();
		mPage = 0;
		mAdapter.notifyDataSetChanged();
		String subStr = null;
		if (subType != -1)
			subStr = String.valueOf(subType);
		appClientLef.getGongXuList(String.valueOf(type), subStr, null, mPage,
				5, uiHandler, MsgTagVO.DATA_LOAD, getActivity(), true, null,
				null, null);
	}

	private void loadMore() {
		String subStr = null;
		if (subType != -1)
			subStr = String.valueOf(subType);
		appClientLef.getGongXuList(String.valueOf(type), subStr, null, mPage,
				5, uiHandler, MsgTagVO.DATA_MORE, getActivity(), true, null,
				null, null);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}
				String data = res.getData();
				updateItemList(data, true, false);

				break;
			}
			case MsgTagVO.DATA_MORE: {
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}
				String data = res.getData();
				updateItemList(data, false, true);
				break;
			}
			case MsgTagVO.MSG_DEL:
				if (JsonHandler.checkResult((String) msg.obj, getActivity())) {
					CommonUtil.displayToast(getActivity(), "删除成功");
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}
				break;
			}
			;
		}

	};

	private void updateItemList(String data, boolean refresh, boolean append) {
		// TODO Auto-generated method stub
		try {
			pullDownView.finishLoadData(true);
			if (data != null && !data.equals("")) {
				ArrayList<ResourceGXVO> list = JsonHandler
						.parseResourceGXVOList(data);
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
