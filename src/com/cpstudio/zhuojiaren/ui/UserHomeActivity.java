package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.adapter.ActiveListAdapter;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
/**
 * 我的成长界面
 * @author lz
 *
 */
public class UserHomeActivity extends Activity implements OnPullDownListener {
	private ListView mListView;
	private ActiveListAdapter mAdapter;
	private PullDownView mPullDownView;
	private ArrayList<Dynamic> mList = new ArrayList<Dynamic>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private int mPage = 0;
	final int pageSize = 10;
	private String uid = null;
	private ConnHelper mConnHelper = null;
	// private UserInfoFacade mFacade = null;
	private UserFacade userFacade = null;
	private int mType = Dynamic.DYNATIC_TYPE_MY_JIAREN;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_home);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		uid = i.getStringExtra("userid");
		String from = i.getStringExtra("from");
		if (from != null && from.equals("home")) {
			findViewById(R.id.buttonCard).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.buttonCard).setVisibility(View.GONE);
		}
		userFacade = new UserFacade(getApplicationContext());
		pwh = new PopupWindows(UserHomeActivity.this);
		mLoadImage = LoadImage.getInstance();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header9);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setDividerHeight(0);
		mAdapter = new ActiveListAdapter(UserHomeActivity.this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		initClick();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mPage = 0;
		loadData();
		loadInfo();
		super.onResume();
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

	private void updateItemList(ArrayList<Dynamic> list, boolean refresh,
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
			// String families = user.getFamilytotal();//为空
			String families = "" + user.getFamily().size();// 为空

			String headurl = user.getUheader();

			((TextView) findViewById(R.id.textViewUsername)).setText(name);
			if (blog != null && families != null) {
				((TextView) findViewById(R.id.textViewBolgnum))
						.setText(families
								+ getString(R.string.p_jiaren_active_families)
								+ "~" + blog
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
			mLoadImage.beginLoad(headurl, iv);
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
				ImageView bgView = (ImageView) findViewById(R.id.bgImg);

				iv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(UserHomeActivity.this,
								ZhuoMaiCardActivity.class);
						i.putExtra("userid", uid);
						startActivity(i);
					}
				});
				if (user.getBgpic() != null)
					mLoadImage.beginLoad(user.getBgpic(), bgView);
				if (headurl != null && !headurl.equals("")) {
					iv.setTag(headurl);
					mLoadImage.beginLoad(headurl, iv);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			mConnHelper.getDynamicList(mUIHandler, MsgTagVO.DATA_MORE, mType,
					null, mPage, pageSize);
		}
	}

	private void loadData() {
		if (mPullDownView.startLoadData()) {
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			} else {
				mConnHelper.getDynamicList(mUIHandler, MsgTagVO.DATA_LOAD,
						mType, null, mPage, pageSize);
			}
		}
	}

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			UserNewVO user = userFacade.getSimpleInfoById(uid);
			if (user == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_OTHER);
				msg.obj = user;
				msg.sendToTarget();
			}
		} else {
			mConnHelper.getUserInfo(mUIHandler, MsgTagVO.DATA_OTHER, null);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
					data, false);
			if (filePath != null) {
				try {
					Intent i = new Intent(UserHomeActivity.this,
							PublishActiveActivity.class);
					i.putExtra("filePath", filePath);
					startActivityForResult(i, MsgTagVO.DATA_REFRESH);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
