package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import butterknife.ButterKnife;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserSelectActivity;
import com.cpstudio.zhuojiaren.adapter.MsgCmtListAdapter;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.facade.ZhuoInfoFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class TopicDetailActivity extends BaseActivity implements
		OnItemClickListener {
	private ListView mListView;
	private MsgCmtListAdapter mAdapter;
	private ArrayList<CmtVO> mList = new ArrayList<CmtVO>();
	private LoadImage mLoadImage = new LoadImage();
	private View mHeadView = null;
	private PopupWindows pwh;
	
	private String msgid = null;
	
	private int mPage = 1;
	private ZhuoConnHelper mConnHelper = null;
	private String isCollect = "0";
	private ListViewFooter mListViewFooter = null;
	private String uid = null;
	private ZhuoInfoFacade mFacade = null;
	private String myid = null;
	TextView collectBtn ;
	View textViewTip;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_detail);
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.title_activity_topic_detail);

		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new ZhuoInfoFacade(getApplicationContext());
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		
		Intent intent = getIntent();
		msgid = intent.getStringExtra("msgid");
		
		
		pwh = new PopupWindows(TopicDetailActivity.this);
		mAdapter = new MsgCmtListAdapter(this, mList, msgid);
		mListView = (ListView) findViewById(R.id.listViewDetail);
		mListView.setDividerHeight(0);
		LayoutInflater inflater = LayoutInflater.from(TopicDetailActivity.this);
		mHeadView = (LinearLayout) inflater.inflate(
				R.layout.listview_header_topic_detail, null);
		RelativeLayout footerView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		mListView.addHeaderView(mHeadView);
		mListView.addFooterView(footerView);
		mListViewFooter = new ListViewFooter(footerView, onMoreClick);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		initClick();
		loadMainData(intent);
		loadData();
		loadCmt();
	}

	/**
	 * 加载从前一个页面传过来的话题基本信息
	 * @param intent
	 */
	private void loadMainData(Intent intent) {
		// TODO Auto-generated method stub
		
	}

	public void loadHead(ZhuoInfoVO zhuoinfo) {
		Context context = mHeadView.getContext();
		mHeadView.setTag(zhuoinfo.getMsgid());
		UserVO user = zhuoinfo.getUser();
		String company = user.getCompany();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String work = user.getPost();
		String type = zhuoinfo.getType();
		String category = zhuoinfo.getCategory();
		String title = zhuoinfo.getTitle();
		String detail = zhuoinfo.getText();
		isCollect = zhuoinfo.getIscollect();
		uid = user.getUserid();
//		if (!myid.equals(uid)) {
//			findViewById(R.id.buttonChat).setVisibility(View.VISIBLE);
//		}
		TextView nameTV = (TextView) findViewById(R.id.textViewAuthorName);
		nameTV.setText(authorName);
		TextView workTV = (TextView) findViewById(R.id.textViewWork);
		workTV.setText(work);
		TextView conpanyTV = (TextView) findViewById(R.id.textViewRes);
		conpanyTV.setText(company);
//		String woco = ZhuoCommHelper.concatStringWithTag(work, company, "|");
//		if (woco != null && !woco.equals("")) {
//			workTV.setText(woco);
//		} else {
//			workTV.setVisibility(View.GONE);
//		}
		//加载评论内容
		if (type != null && !type.equals("")) {
//			ImageView resIV = (ImageView) findViewById(R.id.imageViewRes);
			TextView resTV = (TextView) findViewById(R.id.textViewCmtContent);
			Map<String, Object> resifo = ZhuoCommHelper.gentResInfo(type,
					category, title, detail, context);
//			resIV.setImageResource((Integer) resifo.get("ico"));
			resTV.setText((String) resifo.get("category")
					+ (String) resifo.get("title")
					+ (String) resifo.get("content"));
		}
		ImageView headIV = (ImageView) findViewById(R.id.imageViewAuthorHeader);
		headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, headIV);
		headIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(TopicDetailActivity.this,
						ZhuoMaiCardActivity.class);
				intent.putExtra("userid", uid);
				startActivity(intent);
			}
		});
		final List<PicVO> pics = zhuoinfo.getPic();
		TableLayout.LayoutParams tllp = new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams trlp = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		trlp.setMargins(0, 0, 10, 10);
		if (pics != null && pics.size() > 0) {
			float times = DeviceInfoUtil.getDeviceCsd(context);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					(int) (70 * times), (int) (70 * times));
			TableLayout tl = (TableLayout) findViewById(R.id.tableLayoutPics);
			TableRow tr = null;
			for (int i = 0; i < pics.size(); i++) {
				if (i % 3 == 0) {
					tr = new TableRow(TopicDetailActivity.this);
					tl.addView(tr);
				}
				tr.setLayoutParams(tllp);
				RelativeLayout rl = new RelativeLayout(TopicDetailActivity.this);
				rl.setLayoutParams(trlp);
				ImageView iv = new ImageView(TopicDetailActivity.this);
				iv.setLayoutParams(rlp);
				rl.addView(iv);
				rl.setTag(pics.get(i).getOrgurl());
				iv.setTag(pics.get(i).getUrl());
				iv.setImageResource(R.drawable.default_image);
				mLoadImage.addTask(pics.get(i).getUrl(), iv);
				rl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TopicDetailActivity.this,
								PhotoViewMultiActivity.class);
						ArrayList<String> orgs = new ArrayList<String>();
						for (int j = 0; j < pics.size(); j++) {
							orgs.add(pics.get(j).getOrgurl());
						}
						intent.putStringArrayListExtra("pics", orgs);
						intent.putExtra("pic", (String) v.getTag());
						startActivity(intent);
					}
				});
				tr.addView(rl);
			}
		}
		String place = zhuoinfo.getPosition();
		if (null == place) {
			place = "";
		}
		String time = zhuoinfo.getAddtime();
		time = CommonUtil.calcTime(time);
		TextView timeTV = (TextView) findViewById(R.id.textViewTime);
		timeTV.setText(time);
		TextView placeTV = (TextView) findViewById(R.id.textViewPlace);
		placeTV.setText(place);

//		List<String> tags = zhuoinfo.getTags();
//		if (tags != null && tags.size() > 0) {
//			String tagStr = "	 ";
//			for (String tag : tags) {
//				tagStr += tag + " ";
//			}
//			((TextView) findViewById(R.id.textViewGongXuTag)).setText(tagStr);
//			findViewById(R.id.relativeLayoutTag).setVisibility(View.VISIBLE);
//		} else {
//			findViewById(R.id.relativeLayoutTag).setVisibility(View.GONE);
//		}
//		String cmtNum = zhuoinfo.getCmtnum();
//		String goodNum = zhuoinfo.getGoodnum();
//		String collectNum = zhuoinfo.getCollectnum();
//		String zfNum = zhuoinfo.getForwardnum();
//		if (null == cmtNum || cmtNum.equals("")) {
//			cmtNum = "0";
//		}
//		if (null == goodNum || goodNum.equals("")) {
//			goodNum = "0";
//		}
//		if (null == collectNum || collectNum.equals("")) {
//			collectNum = "0";
//		}
//		if (null == zfNum || zfNum.equals("")) {
//			zfNum = "0";
//		}
//		TextView goodNumTV = (TextView) findViewById(R.id.textViewGongXuGoodNum);
//		goodNumTV.setText(goodNum);
//		TextView cmtNumTV = (TextView) findViewById(R.id.textViewGongXuCmtNum);
//		cmtNumTV.setText(cmtNum);
//		TextView zfNumTV = (TextView) findViewById(R.id.textViewGongXuZfNum);
//		zfNumTV.setText(zfNum);
//		TextView collectNumTV = (TextView) findViewById(R.id.textViewGongXuCollectNum);
//		collectNumTV.setText(collectNum);
		if (isCollect != null && isCollect.equals("1")) {
			collectBtn = (TextView) findViewById(R.id.buttonTabCollect);
			collectBtn.setText(R.string.label_collectCancel);
			Drawable drawable = getResources().getDrawable(
					R.drawable.tab_collect_on);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			collectBtn.setCompoundDrawables(null, drawable, null, null);
		}
		List<CmtVO> cmts = zhuoinfo.getCmt();
		if (cmts != null && !cmts.isEmpty()) {
			mList.addAll(cmts);
			mAdapter.notifyDataSetChanged();
		} else {
			mListViewFooter.noData(false);
		}
		List<UserVO> users = zhuoinfo.getGood();
		if (users != null && !users.isEmpty()) {
			TableLayout tl = (TableLayout) findViewById(R.id.tableLayoutGood);
			int width = (int) (40 * DeviceInfoUtil
					.getDeviceCsd(TopicDetailActivity.this));
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					width, width);
			TableRow tr = null;
			for (int i = 0; i < users.size(); i++) {
				if (i % 6 == 0) {
					tr = new TableRow(context);
					tr.setLayoutParams(tllp);
					tl.addView(tr);
				}
				RelativeLayout rl = new RelativeLayout(context);
				rl.setLayoutParams(trlp);
				ImageView iv = new ImageView(context);
				iv.setLayoutParams(rlp);
				rl.addView(iv);
				iv.setImageResource(R.drawable.default_userhead);
				iv.setTag(users.get(i).getUheader());
				mLoadImage.addTask(users.get(i).getUheader(), iv);
				final String userid = users.get(i).getUserid();
				rl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(TopicDetailActivity.this,
								ZhuoMaiCardActivity.class);
						intent.putExtra("userid", userid);
						startActivity(intent);
					}
				});
				tr.addView(rl);
			}
		} else {
			findViewById(R.id.layoutGood).setVisibility(View.GONE);
			findViewById(R.id.imageViewGood).setVisibility(View.GONE);
		}
		mLoadImage.doTask();
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				List<CmtVO> list = nljh.parsePagesCmt().getData();
				if (!list.isEmpty()) {
					textViewTip.setVisibility(View.GONE);
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mPage++;
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				} else {
					textViewTip.setVisibility(View.VISIBLE);
					
					
					mListViewFooter.noData(!refresh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (msg.obj != null) {
					if (msg.obj instanceof ZhuoInfoVO) {
						ZhuoInfoVO zhuoinfo = (ZhuoInfoVO) msg.obj;
						loadHead(zhuoinfo);
					} else if (!msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						ZhuoInfoVO zhuoinfo = nljh.parseZhuoInfo();
						if (null != zhuoinfo) {
							loadHead(zhuoinfo);
							mFacade.saveOrUpdate(zhuoinfo);
						}
					}
				}
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			case MsgTagVO.MSG_LIKE: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_zanSuccess);
//					TextView numTV = (TextView) findViewById(R.id.textViewGongXuGoodNum);
//					numTV.setText(String.valueOf(Integer.valueOf(numTV
//							.getText().toString()) + 1));
					TableLayout tl = (TableLayout) findViewById(R.id.tableLayoutGood);
					TableLayout.LayoutParams tllp = new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					TableRow.LayoutParams trlp = new TableRow.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					trlp.setMargins(0, 0, 10, 10);
					int width = (int) (40 * DeviceInfoUtil
							.getDeviceCsd(TopicDetailActivity.this));
					RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
							width, width);
					RelativeLayout rl = new RelativeLayout(
							TopicDetailActivity.this);
					rl.setLayoutParams(trlp);
					ImageView iv = new ImageView(TopicDetailActivity.this);
					iv.setLayoutParams(rlp);
					rl.addView(iv);
					iv.setImageResource(R.drawable.default_userhead);
					UserFacade facade = new UserFacade(getApplicationContext());
					String uhead = facade.getMySimpleInfo().getUheader();
					iv.setTag(uhead);
					mLoadImage.addTask(uhead, iv);
					rl.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(
									TopicDetailActivity.this,
									ZhuoMaiCardActivity.class);
							intent.putExtra("userid", myid);
							startActivity(intent);
						}
					});
					TableRow tr = null;
					if (tl.getChildCount() > 0
							&& ((TableRow) tl
									.getChildAt(tl.getChildCount() - 1))
									.getChildCount() < 6) {
						tr = (TableRow) tl.getChildAt(tl.getChildCount() - 1);
						tr.addView(rl);
					} else {
						tr = new TableRow(TopicDetailActivity.this);
						tr.setLayoutParams(tllp);
						tr.addView(rl);
						tl.addView(tr);
						findViewById(R.id.layoutGood).setVisibility(
								View.VISIBLE);
						findViewById(R.id.imageViewGood).setVisibility(
								View.VISIBLE);
					}
					mLoadImage.doTask();
				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.info10);
//					TextView numTV = (TextView) findViewById(R.id.textViewGongXuZfNum);
//					numTV.setText(String.valueOf(Integer.valueOf(numTV
//							.getText().toString()) + 1));
				}
				break;
			}
			case MsgTagVO.MSG_COLLECT: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
//					TextView collectBtn = (TextView) findViewById(R.id.buttonTabCollect);
//					TextView numTV = (TextView) findViewById(R.id.textViewGongXuCollectNum);
					if (isCollect != null && isCollect.equals("0")) {
						collectBtn.setText(R.string.label_collectCancel);
						Drawable drawable = getResources().getDrawable(
								R.drawable.tab_collect_on);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						collectBtn.setCompoundDrawables(null, drawable, null,
								null);
						isCollect = "1";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_collectSuccess);
//						numTV.setText(String.valueOf(Integer.valueOf(numTV
//								.getText().toString()) + 1));
					} else {
						collectBtn.setText(R.string.label_collect);
						Drawable drawable = getResources().getDrawable(
								R.drawable.tab_collect_off);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(),
								drawable.getMinimumHeight());
						collectBtn.setCompoundDrawables(null, drawable, null,
								null);
						isCollect = "0";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_cancelCollect);
//						numTV.setText(String.valueOf(Integer.valueOf(numTV
//								.getText().toString()) - 1));
					}
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			ZhuoInfoVO info = mFacade.getById(msgid);
			Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			msg.obj = info;
			msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getUrlMsgDetail() + "?msgid="
					+ msgid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
					TopicDetailActivity.this, true, new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							TopicDetailActivity.this.finish();
						}
					});
		}
	}

	private void loadCmt() {
		if (mListViewFooter.startLoading()) {
			mList.clear();
			mAdapter.notifyDataSetChanged();
			mPage = 1;
			String params = ZhuoCommHelper.getUrlCmtList();
			params += "?msgid=" + msgid;
			params += "&page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_OTHER);
		}
	}

	private void loadMore() {
		if (mListViewFooter.startLoading()) {
			String params = ZhuoCommHelper.getUrlCmtList();
			params += "?msgid=" + msgid;
			params += "&page=" + mPage;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_MORE);
		}
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	private void initClick() {

//		findViewById(R.id.buttonChat).setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent i = new Intent(TopicDetailActivity.this,
//						ChatActivity.class);
//				i.putExtra("userid", uid);
//				startActivity(i);
//			}
//		});
		findViewById(R.id.buttonTabZan).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mConnHelper.goodMsg(msgid, mUIHandler,
								MsgTagVO.MSG_LIKE, null, true, null, null);
					}
				});
		findViewById(R.id.buttonTabCmt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(TopicDetailActivity.this,
								MsgCmtActivity.class);
						i.putExtra("msgid", msgid);
						i.putExtra("parentid", msgid);
						startActivityForResult(i, MsgTagVO.MSG_CMT);
					}
				});
		findViewById(R.id.buttonTabShare).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(TopicDetailActivity.this,
								UserSelectActivity.class);
						ArrayList<String> tempids = new ArrayList<String>(1);
						tempids.add(uid);
						i.putStringArrayListExtra("otherids", tempids);
						startActivityForResult(i, MsgTagVO.MSG_FOWARD);
					}
				});
		findViewById(R.id.buttonTabCollect).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isCollect == null || isCollect.equals("0")) {
							mConnHelper.collectMsg(msgid, "1", mUIHandler,
									MsgTagVO.MSG_COLLECT, null, true, null,
									null);
						} else {
							mConnHelper.collectMsg(msgid, "0", mUIHandler,
									MsgTagVO.MSG_COLLECT, null, true, null,
									null);
						}
					}
				});
		textViewTip=findViewById(R.id.textViewTip);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == MsgTagVO.MSG_FOWARD) {
				ArrayList<String> ids = data.getStringArrayListExtra("ids");
				String useridlist = "";
				if (ids != null && ids.size() > 0) {
					for (String id : ids) {
						useridlist += id + ";";
					}
					useridlist = useridlist.substring(0,
							useridlist.length() - 1);
					mConnHelper.recommandMsg(msgid, useridlist, mUIHandler,
							MsgTagVO.MSG_FOWARD, null, true, null, null);
				}
			} else if (requestCode == MsgTagVO.MSG_CMT) {
//				String forward = data.getStringExtra("forward");
//				if (forward != null && forward.equals("1")) {
//					TextView numTV = (TextView) findViewById(R.id.textViewGongXuZfNum);
//					numTV.setText(String.valueOf(Integer.valueOf(numTV
//							.getText().toString()) + 1));
//				}
//				TextView numTV = (TextView) findViewById(R.id.textViewGongXuCmtNum);
//				numTV.setText(String.valueOf(Integer.valueOf(numTV.getText()
//						.toString()) + 1));
				loadCmt();//
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
		}
	}
}
