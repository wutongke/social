package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.ZhuoQuanFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ZhuoQuanVO;
import com.cpstudio.zhuojiaren.ui.QuanCreateActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class QuanListActivity extends Activity {
	private DisplayMetrics dm;
	private LoadImage mLoadImage = new LoadImage();
	private ZhuoConnHelper mConnHelper = null;
	private String mSearchKey = null;
	private ZhuoQuanFacade mFacade = null;
	private static final int CREATE_GROUP = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_list);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new ZhuoQuanFacade(getApplicationContext());
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		loadData();
		initClick();
	}
	//数据加载完成后初始化界面
	/**
	 * 加载时每类圈子（我的圈子，热门圈子，最新圈子...)是一个item，全部add到R.id.linearLayoutQuanContainer中，
	 * 添加完成后，异步下载圈子图片
	 * @param list
	 */
	private void initQuan(ArrayList<ZhuoQuanVO> list) {
		if (!list.isEmpty()) {
			LayoutInflater inflater = LayoutInflater
					.from(QuanListActivity.this);
			LinearLayout container = (LinearLayout) findViewById(R.id.linearLayoutQuanContainer);
			LinearLayout line = (LinearLayout) inflater.inflate(
					R.layout.item_quans, null);
			LinearLayout subContainer = (LinearLayout) line
					.findViewById(R.id.linearLayoutContainer);
			LinearLayout subContainer2 = (LinearLayout) line
					.findViewById(R.id.linearLayoutContainer2);
			LayoutParams ll = (LayoutParams) subContainer.getLayoutParams();
			int num = 4;
			int width = (dm.widthPixels - ll.leftMargin - ll.rightMargin);
			int height = (width - 3 * 14) / num;
			int marginright = (width - num * height) / (num - 1);
			LayoutParams lp = new LayoutParams(height, height);
			lp.rightMargin = marginright;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getGroups().size() > 0) {
					final String gType = list.get(i).getType();
					final String gTypeid = list.get(i).getTypeid();
					((TextView) line.findViewById(R.id.textViewTitle))
							.setText(gType);
					line.findViewById(R.id.relativeLayoutMore)
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View paramView) {
									Intent i = new Intent(
											QuanListActivity.this,
											QuanListDetailActivity.class);
									i.putExtra("type", gTypeid);
									i.putExtra("typename", gType);
									startActivity(i);
								}
							});
					container.addView(line);
					for (int j = 0; j < num * 2
							&& j < list.get(i).getGroups().size(); j++) {
						QuanVO group = list.get(i).getGroups().get(j);
						String id = group.getGroupid();
						String name = group.getGname();
						String headUrl = group.getGheader();
						RelativeLayout item = (RelativeLayout) inflater
								.inflate(R.layout.item_quan, null);
						item.setLayoutParams(lp);
						ImageView imageView = (ImageView) item
								.findViewById(R.id.imageViewHead);
						item.setTag(id);
						if (j < num) {
							subContainer.addView(item);
						} else {
							subContainer2.addView(item);
						}
						imageView.setTag(headUrl);
						imageView
								.setImageResource(R.drawable.default_grouphead);
						mLoadImage.addTask(headUrl, imageView);
						TextView textView = (TextView) item
								.findViewById(R.id.textViewName);
						textView.setText(name);
						item.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View paramView) {
								String id = (String) paramView.getTag();
								Intent i = new Intent(QuanListActivity.this,
										QuanDetailActivity.class);
								i.putExtra("groupid", id);
								startActivity(i);
							}
						});
					}
					line = (LinearLayout) inflater.inflate(R.layout.item_quans,
							null);
					subContainer = (LinearLayout) line
							.findViewById(R.id.linearLayoutContainer);
					subContainer2 = (LinearLayout) line
							.findViewById(R.id.linearLayoutContainer2);
				}
			}
			
			mLoadImage.doTask();
		} else {
			TextView nodata = (TextView) findViewById(R.id.textViewNoData);
			nodata.setText(R.string.label_no_data2);
			nodata.setVisibility(View.VISIBLE);
		}
		findViewById(R.id.progressLoading).setVisibility(View.GONE);
		findViewById(R.id.textViewLoading).setVisibility(View.GONE);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ArrayList<ZhuoQuanVO> list = new ArrayList<ZhuoQuanVO>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<ZhuoQuanVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseZhuoQuanList();
						if (!list.isEmpty()) {
							mFacade.update(list);
						}
					}
				}
				initQuan(list);
				break;
			}
			}
		}
	};

	private void loadData() {
		((LinearLayout) findViewById(R.id.linearLayoutQuanContainer))
				.removeAllViews();
		findViewById(R.id.progressLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewLoading).setVisibility(View.VISIBLE);
		findViewById(R.id.textViewNoData).setVisibility(View.GONE);
		//如果未联网，从数据库中去
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			ArrayList<ZhuoQuanVO> list = mFacade.getAll();
			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			msg.obj = list;
			msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getUrlHotGroupList();
			if (null != mSearchKey) {
				params += "?key=" + mSearchKey;
			}
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD);
		}
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				QuanListActivity.this.finish();
			}
		});
		findViewById(R.id.buttonCreateGroup).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(QuanListActivity.this,
								QuanCreateActivity.class);
						startActivityForResult(i, CREATE_GROUP);
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == CREATE_GROUP) {
			loadData();
		}
	}

}
