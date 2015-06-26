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

import com.cpstudio.zhuojiaren.adapter.ActiveListAdapter;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.facade.UserInfoFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;

public class UserHomeActivity extends Activity implements OnPullDownListener,
		OnItemClickListener {
	private ListView mListView;
	private ActiveListAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private int mPage = 1;
	private String uid = null;
	private ZhuoConnHelper mConnHelper = null;
	private UserInfoFacade mFacade = null;
	private UserFacade userFacade = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		uid = i.getStringExtra("userid");
		String from = i.getStringExtra("from");
		if (from != null && from.equals("home")) {
			findViewById(R.id.buttonCard).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.buttonCard).setVisibility(View.GONE);
		}
		mFacade = new UserInfoFacade(getApplicationContext(),
				UserInfoFacade.USERDAILY, uid);
		userFacade = new UserFacade(getApplicationContext());
		pwh = new PopupWindows(UserHomeActivity.this);
		mLoadImage = new LoadImage();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header9);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mListView.setDividerHeight(0);
		mAdapter = new ActiveListAdapter(UserHomeActivity.this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		loadData();
		loadInfo();
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonActiveFabu).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						pwh.showPop(findViewById(R.id.layoutJiarenActive));
					}
				});
		findViewById(R.id.buttonCard).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserHomeActivity.this,
						ZhuoMaiCardActivity.class);
				i.putExtra("userid", uid);
				startActivity(i);
			}
		});

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserHomeActivity.this.finish();
			}
		});

	}

	private void updateItemList(ArrayList<ZhuoInfoVO> list, boolean refresh,
			boolean append) {
		if (!list.isEmpty()) {
			mPullDownView.hasData();
			if (!append) {
				mList.clear();
			}
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			mPage++;
		} else {
			mPullDownView.noData(!refresh);
		}
	}

	private void updateUserInfo(UserVO user) {
		if (null != user) {
			String name = user.getUsername();
			String blog = user.getActivenum();
//			String families = user.getFamilytotal();//ЮЊПе
			String families = ""+user.getFamily().size();//ЮЊПе
			
			String headurl = user.getUheader();

			((TextView) findViewById(R.id.textViewUsername)).setText(name);
			if (blog != null && families != null) {
				((TextView) findViewById(R.id.textViewBolgnum))
						.setText(families+getString(R.string.p_jiaren_active_families)
								+ "~"
								+ blog
								+ getString(R.string.p_jiaren_active_rizhi));
			}
			ImageView iv = (ImageView) findViewById(R.id.imageViewUserHead);
			iv.setTag(headurl);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View paramView) {
					Intent i = new Intent(UserHomeActivity.this,
							ZhuoMaiCardActivity.class);
					i.putExtra("userid", uid);
					startActivity(i);
				}
			});
			mLoadImage.addTask(headurl, iv);
			mLoadImage.doTask();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				ArrayList<ZhuoInfoVO> list = new ArrayList<ZhuoInfoVO>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<ZhuoInfoVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseZhuoInfoList();
						if (!list.isEmpty()) {
							mFacade.update(list);
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
					ArrayList<ZhuoInfoVO> list = nljh.parseZhuoInfoList();
					updateItemList(list, false, false);
				}
				mPullDownView.RefreshComplete(loadState);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				ArrayList<ZhuoInfoVO> list = new ArrayList<ZhuoInfoVO>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<ZhuoInfoVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseZhuoInfoList();
						if (!list.isEmpty()) {
							mFacade.update(list);
						}
					}
				}
				updateItemList(list, false, true);
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				UserVO user = null;
				if (msg.obj instanceof UserVO) {
					user = (UserVO) msg.obj;
				} else {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					user = nljh.parseUser();
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
		if (id != -1) {
			Intent i = new Intent();
			i.setClass(UserHomeActivity.this, MsgDetailActivity.class);
			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		String params = ZhuoCommHelper.getUrlMyResource();
		params += "?uid=" + uid;
		params += "&page=" + mPage;
		params += "&type=" + "3";
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_REFRESH);
	}

	@Override
	public void onMore() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			ArrayList<ZhuoInfoVO> list = mFacade.getByPage(mPage);
			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
			msg.obj = list;
			msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getUrlMyResource();
			params += "?uid=" + uid;
			params += "&page=" + mPage;
			params += "&type=" + "3";
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private void loadData() {
		if (mPullDownView.startLoadData()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
				ArrayList<ZhuoInfoVO> list = mFacade.getByPage(mPage);
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = list;
				msg.sendToTarget();
			} else {
				String params = ZhuoCommHelper.getUrlMyResource();
				params += "?uid=" + uid;
				params += "&page=" + mPage;
				params += "&type=" + "3";
				mConnHelper.getFromServer(params, mUIHandler,
						MsgTagVO.DATA_LOAD);
			}
		}
	}

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			UserVO user = userFacade.getSimpleInfoById(uid);
			if (user == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_OTHER);
				msg.obj = user;
				msg.sendToTarget();
			}
		} else {
			String params = ZhuoCommHelper.getUrlUserInfo() + "?uid=" + uid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_OTHER);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String filePath = pwh.dealPhotoReturn(requestCode, resultCode, data);
		if (filePath != null) {
			Intent i = new Intent(UserHomeActivity.this,
					PublishActiveActivity.class);
			i.putExtra("filePath", filePath);
			startActivity(i);
		}

	}

}
