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
		g.setName("�Ų�ʿ����");
		g.setContent("�����Ǹ��ӱ�����ӹ���ɡ��������Ǹ�������սʿ�������޿����ɵ��츳����������������������˵��һ���������һ�����ѣ���������һ�����˾���������������µ��ǣ����й���δ���Ĺ滮���ȴ�ϡ�����һ�������������������������Ե��ǿ�����Զ�����ػ��۷����ʵ�������ʵ��������˵��̫���пᡣ");
		g.setTime("2013.5.6");
		g.setOrder("��ʮ����");
		
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
