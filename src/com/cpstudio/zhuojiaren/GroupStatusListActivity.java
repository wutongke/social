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

import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.GroupStatus;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.EventDetailActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.CardActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.DynamicDetailActivity;
import com.cpstudui.zhuojiaren.lz.JiarenActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.QuanStatusListAdapter;
import com.cpstudui.zhuojiaren.lz.QuanziActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.TopicDetailActivity;

/**
 * 圈活动列表，包括圈话题和圈活动
 * 
 * @author lz
 * 
 */
public class GroupStatusListActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {
	private ListView mListView;
	// private ZhuoInfoAdapter mAdapter;

	private QuanStatusListAdapter mAdapter;

	private PullDownView mPullDownView;
	private ArrayList<GroupStatus> mList = new ArrayList<GroupStatus>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private String mUid = null;
	private int mType = GroupStatus.GROUP_STATUS_TYPE_ALL;// 0-全部圈子 1-我创建的圈子
															// 2-我加入的圈子
	private String mLastId = "0";
	private ZhuoConnHelper mConnHelper = null;
	private UserFacade mFacade = null;
	private int mPage = 1;
	final int pageSize = 10;

	// private InfoFacade infoFacade = null;

	// type==0时所有人动态，就只是动态，不包括需求，话题，活动什么的
	/*
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiaren_active);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new UserFacade(getApplicationContext());
		// infoFacade = new InfoFacade(getApplicationContext(),
		// InfoFacade.ACTIVELIST);
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(GroupStatusListActivity.this);
		mLoadImage = new LoadImage();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header2);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		// 是否和圈子话题公用一个数据结构还不一定
		mAdapter = new QuanStatusListAdapter(GroupStatusListActivity.this,
				mList, 1);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowFooter(false);

		mType = getIntent().getIntExtra("mType", 0);
		loadData();
		initClick();
	}

	@Override
	protected void onResume() {
		loadInfo();
		super.onResume();
	}

	private void initClick() {
		findViewById(R.id.buttonViewPub).setOnClickListener(
				new OnClickListener() {

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
				// String families = user.getFamilytotal();//为空
				// 好友个数不知道从哪里取
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
						Intent i = new Intent(GroupStatusListActivity.this,
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
		if (id == -1)
			return;

		int type = mList.get(position).getType();
		String msgid = (String) view.getTag(R.id.tag_id);
		Intent i = new Intent();
		if (type == 0) {
			i.setClass(GroupStatusListActivity.this, TopicDetailActivity.class);
			i.putExtra("topicid", msgid);
		} else {
			i.setClass(GroupStatusListActivity.this, EventDetailActivity.class);
			i.putExtra("eventId", msgid);
		}
		startActivity(i);
	}

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			UserNewVO user = mFacade.getSimpleInfoById(mUid);
			if (user == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_OTHER);
				msg.obj = user;
				msg.sendToTarget();
			}
		} else {
			mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_OTHER, mUid);
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
				mConnHelper.getQuanStatusList(mUIHandler, MsgTagVO.DATA_LOAD,
						mType, mPage, pageSize);
			}
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
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
			// String params = ZhuoCommHelper.getUrlMsgList();
			// params += "?pageflag=" + "1";
			// params += "&reqnum=" + "10";
			// params += "&lastid=" + mLastId;
			// params += "&type=" + mType;
			// params += "&gongxutype=" + "0";
			// params += "&from=" + "6";
			// params += "&uid=" + mUid;
			// mConnHelper.getFromServer(params, mUIHandler,
			// MsgTagVO.DATA_MORE);
			mConnHelper.getQuanStatusList(mUIHandler, MsgTagVO.DATA_MORE,
					mType, mPage, pageSize);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.DATA_REFRESH) {
				onRefresh();
			} else if (requestCode == MsgTagVO.MSG_CMT) {
				Toast.makeText(GroupStatusListActivity.this, "评论成功！", 2000)
						.show();
				// String forward = data.getStringExtra("forward");
				// String msgid = data.getStringExtra("msgid");
				// String outterid = data.getStringExtra("outterid");
				// if (forward != null && forward.equals("1")) {
				// onRefresh();
				// } else {
				// for (int i = 0; i < mList.size(); i++) {
				// ZhuoInfoVO item = mList.get(i);
				// if (msgid != null) {
				// if (item.getMsgid().equals(msgid)
				// && outterid == null) {
				// if (forward != null && forward.equals("1")) {
				// item.setForwardnum(String.valueOf(Integer
				// .valueOf(item.getForwardnum()) + 1));
				// }
				// item.setCmtnum(String.valueOf(Integer
				// .valueOf(item.getCmtnum()) + 1));
				// mList.set(i, item);
				// break;
				// } else if (outterid != null
				// && item.getOrigin() != null
				// && item.getOrigin().getMsgid()
				// .equals(msgid)) {
				// if (forward != null && forward.equals("1")) {
				// String forwardStr = String.valueOf(Integer
				// .valueOf(item.getOrigin()
				// .getForwardnum()) + 1);
				// item.getOrigin().setForwardnum(forwardStr);
				// }
				// item.getOrigin().setCmtnum(
				// String.valueOf(Integer.valueOf(item
				// .getOrigin().getCmtnum()) + 1));
				// mList.set(i, item);
				// }
				// }
				// }
				// }
				// mAdapter.notifyDataSetChanged();
			} else {
				String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
						data, false);
				if (filePath != null) {
					try {
						Intent i = new Intent(GroupStatusListActivity.this,
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
