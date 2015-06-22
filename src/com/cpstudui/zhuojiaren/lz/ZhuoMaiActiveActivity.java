package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.ButterKnife;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.facade.InfoFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.ZhuoMaiVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class ZhuoMaiActiveActivity extends BaseActivity implements
		OnPullDownListener, OnItemClickListener {
	private ListView mListView;
	ZhuoMaiActiveAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<ZhuoMaiVO> mList = new ArrayList<ZhuoMaiVO>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private String mUid = null;
	private String mType = "0";// 0：查看全部，1：查看资源
	private String mLastId = "0";
	private ArrayList<ZhuoMaiVO> mListDatas = new ArrayList<ZhuoMaiVO>();
	private ZhuoConnHelper mConnHelper = null;
	private UserFacade mFacade = null;
	private int mPage = 1;
	private InfoFacade infoFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhuo_mai_active);
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.label_active_zhuomai);

		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new ZhuoMaiActiveAdapter(ZhuoMaiActiveActivity.this,
				mListDatas, R.layout.item_zhuomai);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		loadData();
		// initClick();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMore() {
		// TODO Auto-generated method stub

	}

	private void loadData() {
		// TODO Auto-generated method stub
		// test
		// 根据参数type和item来加载数据
		for (int i = 0; i < 10; i++)
			mListDatas.add(new ZhuoMaiVO());
		mPullDownView.finishLoadData(true);
		mPullDownView.hasData();
		mAdapter.notifyDataSetChanged();
	}
}
