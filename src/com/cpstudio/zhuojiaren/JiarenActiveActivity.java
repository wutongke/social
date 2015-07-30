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

import com.cpstudio.zhuojiaren.facade.InfoFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.PullDownView.OnPullDownListener;
import com.cpstudui.zhuojiaren.lz.CardActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.JiarenActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.QuanziActiveNumListActivity;
import com.cpstudui.zhuojiaren.lz.QuanziTopicListAdapter;
import com.cpstudui.zhuojiaren.lz.TopicDetailActivity;

public class JiarenActiveActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {
	private ListView mListView;
	// private ZhuoInfoAdapter mAdapter;

	// 倬家人所有的动态及谁加入倬家人，不包括话题，需求等，内容布局与话题一样
	// 此处暂时用了圈话题的适配器，之后需要修改
	private QuanziTopicListAdapter mAdapter;

	private PullDownView mPullDownView;
	private ArrayList<QuanTopicVO> mList = new ArrayList<QuanTopicVO>();
	private LoadImage mLoadImage = null;
	private PopupWindows pwh = null;
	private String mUid = null;
	private String mType = "0";// 原来 ：0：查看全部，1：查看资源
								// //新的：0：插查看全部，1：所有倬家人动态，2：我关注的家人动态
	private String mLastId = "0";
	private ZhuoConnHelper mConnHelper = null;
	private UserFacade mFacade = null;
	private int mPage = 1;
	private InfoFacade infoFacade = null;

	// type==0时所有人动态，就只是动态，不包括需求，话题，活动什么的
	/*
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jiaren_active);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new UserFacade(getApplicationContext());
		infoFacade = new InfoFacade(getApplicationContext(),
				InfoFacade.ACTIVELIST);
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(JiarenActiveActivity.this);
		mLoadImage = new LoadImage();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header3);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new QuanziTopicListAdapter(JiarenActiveActivity.this, mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);

		mType = getIntent().getIntExtra("mType", 0) + "";
		if (mType.equals("0") == false)
			findViewById(R.id.ll_active_menue).setVisibility(View.GONE);
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
						// if (mType == "1") {
						// mType = "0";
						// ((Button) v).setText(R.string.label_view2);
						// } else {
						// mType = "1";
						// ((Button) v).setText(R.string.label_view1);
						// }
						// loadData();
					}
				});
		if (mType.equals("0") == true) {
			findViewById(R.id.textViewActiveJiaren).setOnClickListener(
					new OnClickListener() {// 我的家人动态
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
					new OnClickListener() {// 我的圈子动态
						@Override
						public void onClick(View v) {
							Intent i = new Intent(JiarenActiveActivity.this,
									QuanziActiveNumListActivity.class);
							startActivity(i);
						}
					});

			findViewById(R.id.textViewActiveZhuomai).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(JiarenActiveActivity.this,
									UplevelActivity.class);
							startActivity(i);
							
							// 倬脉。是否可以用旧版本中的UplevelActivity
//							Intent i = new Intent(JiarenActiveActivity.this,
//									ZhuoMaiActiveActivity.class);
//							startActivity(i);
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

	private void updateItemList(ArrayList<QuanTopicVO> list, boolean refresh,
			boolean append) {
		if (!list.isEmpty()) {
			mPullDownView.hasData();
			if (!append) {
				mList.clear();
			}
			mList.addAll(list);
			mAdapter.notifyDataSetChanged();
			if (mList.size() > 0) {
				mLastId = mList.get(mList.size() - 1).getTopicid();
			}
			mPage++;
		} else {
			mPullDownView.noData(!refresh);
		}
	}

	private void updateUserInfo(UserVO user) {
		try {
			if (null != user) {
				String name = user.getUsername();
				String blog = user.getActivenum();
				// String families = user.getFamilytotal();//为空
				// 好友个数不知道从哪里取
				String families = "" + user.getFamily().size();

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
				ArrayList<QuanTopicVO> list = new ArrayList<QuanTopicVO>();
				boolean loadState = false;
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<QuanTopicVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						loadState = true;
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseQuanTopicList();
						if (!list.isEmpty()) {
//							infoFacade.update(list);
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
					ArrayList<QuanTopicVO> list = nljh.parseQuanTopicList();
					updateItemList(list, false, false);
				}
				mPullDownView.RefreshComplete(loadState);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mPullDownView.notifyDidMore();
				ArrayList<QuanTopicVO> list = new ArrayList<QuanTopicVO>();
				if (msg.obj instanceof ArrayList) {
					list = (ArrayList<QuanTopicVO>) msg.obj;
				} else {
					if (msg.obj != null && !msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						list = nljh.parseQuanTopicList();
						if (!list.isEmpty()) {
//							infoFacade.update(list);
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
			// String type = view.getTag(R.id.tag_string).toString();
			// if (type.equals("uplevel")) {
			// i.setClass(JiarenActiveActivity.this,
			// UplevelDetailActivity.class);
			// } else {
			// boolean iszenghui = false;
			// for (String zh : ZenghuiTypeActivity.ZENGHUI_TYPE) {
			// if (zh.equals(type)) {
			// i.setClass(JiarenActiveActivity.this,
			// ZenghuiActivity.class);
			// iszenghui = true;
			// break;
			// }
			// }
			// if (!iszenghui) {

			// lz暂时该为话题详情，测试用
			// i.setClass(JiarenActiveActivity.this, MsgDetailActivity.class);
			i.setClass(JiarenActiveActivity.this, TopicDetailActivity.class);
			// lz
			// }
			// }
			i.putExtra("msgid", (String) view.getTag(R.id.tag_id));
			startActivity(i);
		}
	}

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			UserVO user = mFacade.getSimpleInfoById(mUid);
			if (user == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_OTHER);
				msg.obj = user;
				msg.sendToTarget();
			}
		} else {
			String params = ZhuoCommHelper.getUrlUserInfo() + "?uid=" + mUid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_OTHER);
		}
	}

	public void loadData() {
		if (mPullDownView.startLoadData()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
				ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = list;
				msg.sendToTarget();
			} else {
				String params = ZhuoCommHelper.getUrlMsgList();
				params += "?pageflag=" + "0";
				params += "&reqnum=" + "10";
				params += "&lastid=" + "0";
				params += "&type=" + mType;
				params += "&gongxutype=" + "0";
				params += "&from=" + "6";
				params += "&uid=" + mUid;
				mConnHelper.getFromServer(params, mUIHandler,
						MsgTagVO.DATA_LOAD);
			}
		}
	}

	@Override
	public void onRefresh() {
		String params = ZhuoCommHelper.getUrlMsgList();
		params += "?pageflag=" + "0";
		params += "&reqnum=" + "10";
		params += "&lastid=" + "0";
		params += "&type=" + mType;
		params += "&gongxutype=" + "0";
		params += "&from=" + "6";
		params += "&uid=" + mUid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_REFRESH);
	}

	@Override
	public void onMore() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			ArrayList<ZhuoInfoVO> list = infoFacade.getByPage(mPage);
			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_MORE);
			msg.obj = list;
			msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getUrlMsgList();
			params += "?pageflag=" + "1";
			params += "&reqnum=" + "10";
			params += "&lastid=" + mLastId;
			params += "&type=" + mType;
			params += "&gongxutype=" + "0";
			params += "&from=" + "6";
			params += "&uid=" + mUid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.DATA_REFRESH) {
				onRefresh();
			} else if (requestCode == MsgTagVO.MSG_CMT) {
				Toast.makeText(JiarenActiveActivity.this, "评论成功！", 2000).show();
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
