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
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.CardActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.DynamicDetailActivity;
import com.cpstudui.zhuojiaren.lz.DynamicListAdapter;
import com.cpstudui.zhuojiaren.lz.JiarenActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.QuanziActiveNumListActivity;

public class JiarenActiveActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {
	private ListView mListView;
	// private ZhuoInfoAdapter mAdapter;

	// 倬家人所有的动态及谁加入倬家人，不包括话题，需求等，内容布局与话题一样
	// 此处暂时用了圈话题的适配器，之后需要修改
	private DynamicListAdapter mAdapter;

	private PullDownView mPullDownView;
	private ArrayList<Dynamic> mList = new ArrayList<Dynamic>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private String mUid = null;
	private int mType = Dynamic.DYNATIC_TYPE_MY_JIAREN;// 类型 0-自己的家人动态
														// 1-指定用户的家人动态 2-所有家人动态
	private String mLastId = "0";
	private ZhuoConnHelper mConnHelper = null;
	private UserFacade mFacade = null;
	private int mPage = 1;
	final int pageSize = 10;
	final int titleIds[] = { R.string.title_active, R.string.title_active,
			 R.string.label_active_all,R.string.label_active_i_focus};

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
		pwh = new PopupWindows(JiarenActiveActivity.this);
		mLoadImage = new LoadImage();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mType = getIntent().getIntExtra("mType", 0);
		((TextView) findViewById(R.id.userNameShow)).setText(titleIds[mType]);
		if (Dynamic.DYNATIC_TYPE_MY_JIAREN == mType)
			mPullDownView.initHeaderViewAndFooterViewAndListView(this,
					R.layout.listview_header3);
		else
			mPullDownView.initHeaderViewAndFooterViewAndListView(this,
					R.layout.listview_header2);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		// 是否和圈子话题公用一个数据结构还不一定
		mAdapter = new DynamicListAdapter(JiarenActiveActivity.this, mList, 1);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
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
		findViewById(R.id.buttonViewPub).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						pwh.showPop(findViewById(R.id.layoutJiarenActive));
					}
				});
		if (Dynamic.DYNATIC_TYPE_MY_JIAREN == mType) {
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

							// 倬脉。是否可以用旧版本中的UplevelActivity
							// Intent i = new Intent(JiarenActiveActivity.this,
							// ZhuoMaiActiveActivity.class);
							// startActivity(i);
						}
					});
		}
		// findViewById(R.id.textViewActiveFabu).setOnClickListener(
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// pwh.showPop(findViewById(R.id.layoutJiarenActive));
		// }
		// });
		// findViewById(R.id.textViewActiveZenghui).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(JiarenActiveActivity.this,
		// ZenghuiTypeActivity.class);
		// startActivity(i);
		// }
		// });
		//
		// findViewById(R.id.textViewActiveGongXu).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(JiarenActiveActivity.this,
		// PublishResourceActivity.class);
		// startActivityForResult(i, MsgTagVO.DATA_REFRESH);
		// }
		// });

		// findViewById(R.id.buttonViewType).setOnClickListener(
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (mType == "1") {
		// mType = "0";
		// ((Button) v).setText(R.string.label_view2);
		// } else {
		// mType = "1";
		// ((Button) v).setText(R.string.label_view1);
		// }
		// loadData();
		// }
		// });

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
				mLastId = mList.get(mList.size() - 1).getStatusid();
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
						Intent i = new Intent(JiarenActiveActivity.this,
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id != -1) {
			Intent i = new Intent();
			i.setClass(JiarenActiveActivity.this, DynamicDetailActivity.class);
			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			startActivity(i);
		}
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
				mConnHelper.getDynamicList(mUIHandler, MsgTagVO.DATA_LOAD,
						mType, null, mPage, pageSize);
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
