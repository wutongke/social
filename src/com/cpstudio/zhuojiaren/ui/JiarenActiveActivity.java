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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.drawable;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.adapter.DynamicListAdapter;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
/**
 * 动态主界面
 * @author lz
 *
 */
public class JiarenActiveActivity extends Activity implements
		OnPullDownListener {
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
	private int mType = Dynamic.DYNATIC_TYPE_MY_JIAREN;// 类型 0-我的家人动态
	private ConnHelper mConnHelper = null;
	private UserFacade mFacade = null;
	private int mPage = 0;
	final int pageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiaren_active);
		ButterKnife.inject(this);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		mFacade = new UserFacade(getApplicationContext());
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(JiarenActiveActivity.this);
		mLoadImage = LoadImage.getInstance();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		tvBack.setVisibility(View.GONE);
		tvTitle.setText(R.string.title_active);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header3);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		mListView = mPullDownView.getListView();
		mAdapter = new DynamicListAdapter(JiarenActiveActivity.this, mList, 1);
		mListView.setAdapter(mAdapter);
		mListView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (id != -1) {
					Intent i = new Intent();
					i.setClass(JiarenActiveActivity.this,
							DynamicDetailActivity.class);
					i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
					startActivity(i);
				}
			}
		});

		loadData();
		initClick();
	}

	@Override
	protected void onResume() {
		if (Dynamic.DYNATIC_TYPE_MY_JIAREN == mType)
			loadInfo();
		super.onResume();
	}

	private void initClick() {
		ivPub.setImageResource(R.drawable.iwrite);
		ivPub.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pwh.showPop(findViewById(R.id.layoutJiarenActive));
			}
		});
		findViewById(R.id.textViewActiveJiaren).setOnClickListener(
				new OnClickListener() {// 我的所有家人动态
					@Override
					public void onClick(View v) {
						Intent i = new Intent(JiarenActiveActivity.this,
								JiarenActiveNumListActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.textViewActiveCard).setOnClickListener(
				new OnClickListener() {// 我的名片动态
					@Override
					public void onClick(View v) {
						Intent i = new Intent(JiarenActiveActivity.this,
								CardActiveNumListActivity.class);
						startActivity(i);
					}
				});
		findViewById(R.id.textViewActiveQuanzi).setOnClickListener(
				new OnClickListener() {// 我的圈子动态，圈子话题和圈子活动混合起来的
					@Override
					public void onClick(View v) {
						Intent i = new Intent(JiarenActiveActivity.this,
								QuanziActiveNumListActivity.class);
						startActivity(i);
					}
				});

		findViewById(R.id.textViewActiveZhuomai).setOnClickListener(
				new OnClickListener() { // 倬脉动态，即倬脉动态就是公告信息
					@Override
					public void onClick(View v) {
						Intent i = new Intent(JiarenActiveActivity.this,
								ZhuoMaiActiveListActivity.class);
						startActivity(i);
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
						Intent i = new Intent(JiarenActiveActivity.this,
								UserHomeActivity.class);
						i.putExtra("userid", mUid);
						i.putExtra("from", "home");
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
					}
				}
				mPullDownView.finishLoadData(loadState);
				updateItemList(list, true, false);
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
					}
				}
				updateItemList(list, false, true);
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				UserNewVO user = null;
				if (msg.obj instanceof UserNewVO) {
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
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
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
				Toast.makeText(JiarenActiveActivity.this, "评论成功！", 2000).show();
			} else {
				String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
						data, false);
				if (filePath != null) {
					try {
						Intent i = new Intent(JiarenActiveActivity.this,
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
