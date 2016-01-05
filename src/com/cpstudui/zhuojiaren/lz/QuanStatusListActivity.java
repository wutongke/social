package com.cpstudui.zhuojiaren.lz;

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

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.array;
import com.cpstudio.zhuojiaren.R.drawable;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GroupStatus;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.EventDetailActivity;
import com.cpstudio.zhuojiaren.ui.PublishActiveActivity;
import com.cpstudio.zhuojiaren.ui.UserHomeActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;

/**
 * 圈子动态界面，包括圈子话题和圈子活动
 * 
 * @author lz
 * 
 */
@SuppressLint("ShowToast")
public class QuanStatusListActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {
	@InjectView(R.id.activity_function_image)
	ImageView ivPub;
	@InjectView(R.id.activity_back)
	TextView tvBack;
	@InjectView(R.id.activity_title)
	TextView tvTitle;

	private ListView mListView;

	private QuanStatusListAdapter mAdapter;

	private PullDownView mPullDownView;
	private ArrayList<GroupStatus> mList = new ArrayList<GroupStatus>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private String mUid = null;
	private int mType = GroupStatus.GROUP_STATUS_TYPE_ALL;// 0-全部圈子 1-我创建的圈子
															// 2-我加入的圈子
	String[] titleArray;
	private ConnHelper mConnHelper = null;
	private int mPage = 0;
	final int pageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiaren_active);
		ButterKnife.inject(this);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());

		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(QuanStatusListActivity.this);
		mLoadImage =  LoadImage.getInstance();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		// 是否和圈子话题公用一个数据结构还不一定
		mAdapter = new QuanStatusListAdapter(QuanStatusListActivity.this,
				mList, 1);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowFooter(false);
		mType = getIntent().getIntExtra("mType", 0);
		titleArray = getResources().getStringArray(R.array.quan_active_types);
		if (titleArray != null && titleArray.length > 0) {
			mType = mType % titleArray.length;
			tvTitle.setText(titleArray[mType]);
		}
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
				QuanStatusListActivity.this.finish();
			}
		});

		ivPub.setImageResource(R.drawable.iwrite);
		ivPub.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pwh.showPop(findViewById(R.id.layoutJiarenActive));
			}
		});
	}

	private void updateItemList(ArrayList<GroupStatus> list, boolean refresh,
			boolean append) {
		if (!list.isEmpty()) {
			mPullDownView.hasData();
			if (!append) {
				mList.clear();
			}
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			if (mList.size() > 0) {
				// mLastId = mList.get(mList.size() - 1).getStatusid();
			}
			mPage++;
		} else {
			mPullDownView.noData(!refresh);
		}
	}

	private void updateUserInfo(UserNewVO user) {
		try {
			if (null != user) {
				String name = user.getName();
				int blogNum = user.getStatusNum();
				int friendNum = user.getFriendNum();
				String families = "" + friendNum;

				String headurl = user.getUheader();

				((TextView) findViewById(R.id.textViewUsername)).setText(name);

				((TextView) findViewById(R.id.textViewBolgnum))
						.setText(families
								+ getString(R.string.p_jiaren_active_families)
								+ "~" + blogNum
								+ getString(R.string.p_jiaren_active_rizhi));

				ImageView iv = (ImageView) findViewById(R.id.imageViewUserHead);
				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(QuanStatusListActivity.this,
								UserHomeActivity.class);
						i.putExtra("userid", mUid);
						i.putExtra("from", "home");
						startActivity(i);
					}
				});
				if (headurl != null && !headurl.equals("")) {
					iv.setTag(headurl);
					mLoadImage.addTask(headurl, iv);
					mLoadImage.doTask();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ArrayList<GroupStatus> list = new ArrayList<GroupStatus>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<GroupStatus>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseGroupStatusList();
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
					ArrayList<GroupStatus> list = nljh.parseGroupStatusList();
					updateItemList(list, false, false);
				}
				mPullDownView.RefreshComplete(loadState);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				ArrayList<GroupStatus> list = new ArrayList<GroupStatus>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<GroupStatus>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseGroupStatusList();
						if (!list.isEmpty()) {
							// infoFacade.update(list);
						}
					}
				}
				updateItemList(list, false, true);
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				UserNewVO user = null;
				if (msg.obj instanceof UserVO) {
					user = (UserNewVO) msg.obj;
				} else if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					user = nljh.parseNewUser();
				}
				updateUserInfo(user);
				break;
			}
			}

		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id <=0 )
			return;

		int type = mList.get(position-1).getType();
		String msgid = (String) view.getTag(R.id.tag_id);
		Intent i = new Intent();
		if (type == 0) {
			i.setClass(QuanStatusListActivity.this, TopicDetailActivity.class);
			i.putExtra("topicid", msgid);
		} else {
			i.setClass(QuanStatusListActivity.this, EventDetailActivity.class);
			i.putExtra("eventId", msgid);
		}
		startActivity(i);
	}


	public void loadData() {
		if (mPullDownView.startLoadData()) {
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			} else {
				mConnHelper.getQuanStatusList(mUIHandler, MsgTagVO.DATA_LOAD,
						mType, mPage, pageSize);
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
		} else {
			mConnHelper.getQuanStatusList(mUIHandler, MsgTagVO.DATA_MORE,
					mType, mPage, pageSize);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.DATA_REFRESH) {
				onRefresh();
			} else if (requestCode == MsgTagVO.MSG_CMT) {
				Toast.makeText(QuanStatusListActivity.this, "评论成功！", 2000)
						.show();
			} else {
				String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
						data, false);
				if (filePath != null) {
					try {
						Intent i = new Intent(QuanStatusListActivity.this,
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
