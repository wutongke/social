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

import com.cpstudio.zhuojiaren.PublishActiveActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserHomeActivity;
import com.cpstudio.zhuojiaren.facade.InfoFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
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


/**
 * 家人动态Activity
 * 
 * @author lz
 * 
 */
public class QuanziActiveActiveActivity extends Activity implements
		OnPullDownListener, OnItemClickListener {
	//数据根据这个向网络取出不同的
	int dataType;//0:倬脉全部圈子动态，创建的圈子动态，我加入的圈子动态
	
	private ListView mListView;
	// private ZhuoInfoAdapter mAdapter;

	private QuanziActiveListAdapter mAdapter;

	private PullDownView mPullDownView;

	private ArrayList<ZhuoInfoVO> mList = new ArrayList<ZhuoInfoVO>();

	private LoadImage mLoadImage = null;

	private PopupWindows pwh = null;

	private String mUid = null;

	private String mType = "0";

	private String mLastId = "0";

	private ZhuoConnHelper mConnHelper = null;

	private UserFacade mFacade = null;

	private int mPage = 1;

	private InfoFacade infoFacade = null;

	int[] titleStrId={R.string.label_active_all_quan,R.string.label_active_created_by_me,R.string.label_active_joined_me};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quanzi_active);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new UserFacade(getApplicationContext());
		infoFacade = new InfoFacade(getApplicationContext(),
				InfoFacade.ACTIVELIST);
		mUid = ResHelper.getInstance(getApplicationContext()).getUserid();
		pwh = new PopupWindows(QuanziActiveActiveActivity.this);
		mLoadImage = new LoadImage();
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.listview_header3);
		mPullDownView.setOnPullDownListener(this);
		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);
		mAdapter = new QuanziActiveListAdapter(QuanziActiveActiveActivity.this,
				mList);
		mListView.setAdapter(mAdapter);
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);

		dataType=getIntent().getIntExtra("mType", 0);
		
		if(dataType>=0 && dataType<=2)
			((TextView)findViewById(R.id.userNameShow)).setText(titleStrId[dataType]);
		
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
			if (mList.size() > 0) {
				mLastId = mList.get(mList.size() - 1).getMsgid();
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
						Intent i = new Intent(QuanziActiveActiveActivity.this,
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
							infoFacade.update(list);
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
							infoFacade.update(list);
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
			i.setClass(QuanziActiveActiveActivity.this,
					TopicDetailActivity.class);
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
				Toast.makeText(QuanziActiveActiveActivity.this, "评论成功！", 2000)
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
						Intent i = new Intent(QuanziActiveActiveActivity.this,
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
