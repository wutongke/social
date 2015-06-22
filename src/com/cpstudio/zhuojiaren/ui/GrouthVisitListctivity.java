package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.GrouthVisitDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.GrouthVisitAdapter;
import com.cpstudio.zhuojiaren.model.GrouthVisit;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

public class GrouthVisitListctivity extends BaseActivity {

	@InjectView(R.id.agv_pulldown)
	PullDownView pullDownView;
	private GrouthVisitAdapter mAdapter;
	private ListView listView;
	private ArrayList<GrouthVisit> mDatas = new ArrayList<GrouthVisit>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grouth_visit_listctivity);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.grouth_visite_label);
		initPullDownView();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub
		// test
		GrouthVisit g = new GrouthVisit();
		g.setImageUrl("http://img0.imgtn.bdimg.com/it/u=3317101867,3739965699&fm=11&gp=0.jpg");
		g.setName("张博士亲授");
		g.setContent("三井是个逃兵，毋庸置疑。他曾经是个骄傲的战士，带着无可置疑的天赋与荣誉。对于这样的人来说，一场意外就是一次灾难，他陷入了一个令人绝望的困境，最可怕的是，所有关于未来的规划被迫打断。这是一个少年遇到的最大困境，他面对的是可能永远不能重回巅峰的现实，这个现实对于他来说，太过残酷。");
		g.setTime("2013.5.6");
		g.setOrder("第十二期");
		
		mDatas.add(g);
		mDatas.add(g);
		mDatas.add(g);
		mDatas.add(g);
		mAdapter.notifyDataSetChanged();
		Message msg = uiHandler.obtainMessage();
		msg.sendToTarget();
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pullDownView.finishLoadData(true);
			pullDownView.hasData();
		};
	};

	private void initPullDownView() {
		// TODO Auto-generated method stub
		pullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.head_pull_all_no);
		listView = pullDownView.getListView();
		mAdapter = new GrouthVisitAdapter(this, mDatas, R.layout.item_visite);
		listView.setAdapter(mAdapter);
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
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GrouthVisitListctivity.this,GrouthVisitDetailActivity.class);
				startActivity(intent);
			}
		});
	}


}
