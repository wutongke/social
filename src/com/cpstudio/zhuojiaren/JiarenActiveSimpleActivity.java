package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.DynamicDetailActivity;
import com.cpstudui.zhuojiaren.lz.DynamicListAdapter;

public class JiarenActiveSimpleActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {
	@InjectView(R.id.activity_function_image)
	ImageView ivPub;
	@InjectView(R.id.activity_back)
	TextView tvBack;
	@InjectView(R.id.activity_title)
	TextView tvTitle;
	private ListView mListView;
	private DynamicListAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<Dynamic> mList = new ArrayList<Dynamic>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private String mUid = null;
	private int mType = Dynamic.DYNATIC_TYPE_MY_JIAREN;// 类型 0-自己的家人动态
														// 1-指定用户的家人动态 2-所有家人动态
	private ZhuoConnHelper mConnHelper = null;
	private UserFacade mFacade = null;
	private int mPage = 0;
	final int pageSize = 10;
	final int titleIds[] = { R.string.title_active, R.string.title_active,
			R.string.label_active_all, R.string.label_active_i_focus };

	// private InfoFacade infoFacade = null;
	// type==0时所有人动态，就只是动态，不包括需求，话题，活动什么的
	/*
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiaren_active);
		ButterKnife.inject(this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new UserFacade(getApplicationContext());
		// infoFacade = new InfoFacade(getApplicationContext(),
		// InfoFacade.ACTIVELIST);
		ivPub.setVisibility(View.GONE);
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(JiarenActiveSimpleActivity.this);
		mLoadImage = new LoadImage();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mType = getIntent().getIntExtra("mType", 0);
		tvTitle.setText(titleIds[mType]);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new DynamicListAdapter(JiarenActiveSimpleActivity.this,
				mList, 1);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		loadData();
		initClick();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initClick() {
		tvBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				JiarenActiveSimpleActivity.this.finish();
			}
		});
//		ivPub.setImageResource(R.drawable.iwrite);
//		ivPub.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				pwh.showPop(findViewById(R.id.layoutJiarenActive));
//			}
//		});
	}

	private void updateItemList(ArrayList<Dynamic> list, boolean refresh,
			boolean append) {
		if (!list.isEmpty()) {
			mPullDownView.hasData();
			if (!append) {
				mList.clear();
			}
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			if (mList.size() > 0) {
				;
			}
			mPage++;
		} else {
			mPullDownView.noData(!refresh);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ArrayList<Dynamic> list = new ArrayList<Dynamic>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<Dynamic>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseDynamicList();
						if (!list.isEmpty()) {
							// infoFacade.update(list);
						}
					}
				}
				mPullDownView.finishLoadData(loadState);
				updateItemList(list, true, false);
				break;
			}
			case MsgTagVO.DATA_REFRESH: {
				boolean loadState = false;
				if (msg.obj != null && !msg.obj.equals("")) {
					loadState = true;
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					ArrayList<Dynamic> list = nljh.parseDynamicList();
					updateItemList(list, false, false);
				}
				mPullDownView.RefreshComplete(loadState);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				ArrayList<Dynamic> list = new ArrayList<Dynamic>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<Dynamic>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseDynamicList();
						if (!list.isEmpty()) {
							// infoFacade.update(list);
						}
					}
				}
				updateItemList(list, false, true);
				break;
			}
			}
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id != -1) {
			Intent i = new Intent();
			i.setClass(JiarenActiveSimpleActivity.this,
					DynamicDetailActivity.class);
			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	public void loadData() {
		if (mPullDownView.startLoadData()) {
			// mList.clear();
			// mAdapter.notifyDataSetChanged();
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
				// ArrayList<Dynamic> list = infoFacade.getByPage(mPage);
				// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				// msg.obj = list;
				// msg.sendToTarget();
			} else {
				mConnHelper.getDynamicList(mUIHandler, MsgTagVO.DATA_LOAD,
						mType, null, mPage, pageSize);
			}
		}
	}

	@Override
	public void onRefresh() {
		mPage = 0;
		loadData();
	}

	@Override
	public void onMore() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			// ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
			// msg.obj = list;
			// msg.sendToTarget();
		} else {
			mConnHelper.getDynamicList(mUIHandler, MsgTagVO.DATA_MORE, mType,
					null, mPage, pageSize);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.DATA_REFRESH) {
				onRefresh();
			} else if (requestCode == MsgTagVO.MSG_CMT) {
				Toast.makeText(JiarenActiveSimpleActivity.this, "评论成功！", 2000)
						.show();
			} else {
				String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
						data, false);
				if (filePath != null) {
					try {
						Intent i = new Intent(JiarenActiveSimpleActivity.this,
								PublishActiveActivity.class);
						i.putExtra("filePath", filePath);
						startActivityForResult(i, MsgTagVO.DATA_REFRESH);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
