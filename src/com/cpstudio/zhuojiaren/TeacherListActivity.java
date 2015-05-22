package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.adapter.TeacherListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.TeacherVO;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class TeacherListActivity extends Activity implements
		OnItemClickListener {

	private ZhuoConnHelper mConnHelper = null;
	private TeacherListAdapter mAdapter = null;
	private ArrayList<TeacherVO> mList = new ArrayList<TeacherVO>();
	private GridView gridview = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		gridview = (GridView) findViewById(R.id.gridView);
		mAdapter = new TeacherListAdapter(this, mList);
		gridview.setAdapter(mAdapter);
		gridview.setOnItemClickListener(this);
		initClick();
		loadData();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				TeacherListActivity.this.finish();
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				findViewById(R.id.progressLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewLoading).setVisibility(View.GONE);
				findViewById(R.id.textViewFail).setVisibility(View.GONE);
				if (msg.obj != null && !((String) msg.obj).equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					ArrayList<TeacherVO> list = nljh.parseTeacherList();
					if (!list.isEmpty()) {
						findViewById(R.id.textViewNoData).setVisibility(
								View.GONE);
						mList.clear();
						mList.addAll(list);
						mAdapter.notifyDataSetChanged();
					} else {
						findViewById(R.id.textViewNoData).setVisibility(
								View.VISIBLE);
					}
				} else {
					findViewById(R.id.textViewFail).setVisibility(View.VISIBLE);
				}
				break;
			}
			}
		}
	};

	public void loadData() {
		findViewById(R.id.textViewFail).setVisibility(View.GONE);
		findViewById(R.id.progressLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewNoData).setVisibility(View.GONE);
		String params = ZhuoCommHelper.getUrlTeacherList();
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(TeacherListActivity.this,
					TeacherInfoActivity.class);
			i.putExtra("id", (String) arg1.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

}
