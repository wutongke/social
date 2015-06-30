package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserSelectActivity;
import com.cpstudio.zhuojiaren.adapter.MsgCmtListAdapter;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class GongXuDetailActivity extends BaseActivity {
	@InjectView(R.id.activity_function)
	TextView tvShare;
	@InjectView(R.id.activity_function2)
	TextView tvCollect;// 收藏
	@InjectView(R.id.textViewGongxuTitle)
	TextView tvTitle;
	@InjectView(R.id.textViewTime)
	TextView tvTime;
	@InjectView(R.id.textViewContent)
	TextView tvContent;

	@InjectView(R.id.imageViewAuthorHeader)
	ImageView ivHead;
	@InjectView(R.id.textViewAuthorName)
	TextView tvName;
	@InjectView(R.id.textWork)
	TextView tvWork;
	@InjectView(R.id.textViewCompany)
	TextView tvCompany;
	@InjectView(R.id.buttonTabWrite)
	TextView tvWrite;
	@InjectView(R.id.buttonTabCall)
	TextView tvCall;
	@InjectView(R.id.textViewivEdit)
	TextView tvComment;

	private ListView mListView;
	private MsgCmtListAdapter mAdapter;
	private ArrayList<CmtVO> mList = new ArrayList<CmtVO>();
	private LoadImage mLoadImage = new LoadImage();
	private View mHeadView = null;
	private String msgid = null;
	private int mPage = 1;
	private ZhuoConnHelper mConnHelper = null;
	private String isCollect = "0";
	private ListViewFooter mListViewFooter = null;
	private String uid = null;
	private PopupWindows pwh;
	// 不要本地缓存
	// private ZhuoInfoFacade mFacade = null;
	private String myid = null;
	UserVO sharer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gong_xu_detail);
		initTitle();

		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		// mFacade = new ZhuoInfoFacade(getApplicationContext());
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");

		mAdapter = new MsgCmtListAdapter(this, mList, msgid);
		mListView = (ListView) findViewById(R.id.listViewDetail);
		mListView.setDividerHeight(0);
		LayoutInflater inflater = LayoutInflater
				.from(GongXuDetailActivity.this);
		mHeadView = (LinearLayout) inflater.inflate(
				R.layout.gongxu_detail_main_header, null);
		RelativeLayout footerView = (RelativeLayout) inflater.inflate(
				R.layout.listview_footer, null);
		pwh = new PopupWindows(GongXuDetailActivity.this);
		mListView.addHeaderView(mHeadView);
		ButterKnife.inject(this);
		title.setText(R.string.title_activity_gong_xu_detail);
		mListView.addFooterView(footerView);
		mListViewFooter = new ListViewFooter(footerView, onMoreClick);
		mListView.setAdapter(mAdapter);

		initClick();
		// loadData();
		loadCmt();
	}

	public void loadHead(ResourceGXVO gxInfo) {
		Context context = mHeadView.getContext();
		if (gxInfo == null)
			return;
		tvTitle.setText(gxInfo.getTitle());

		String time = gxInfo.getAddtime();
		time = CommonUtil.calcTime(time);
		tvTime.setText(time);

		tvContent.setText(gxInfo.getDetailContent());

		isCollect = gxInfo.getIsCollect();
		sharer = gxInfo.getOwner();// 分享者
		tvName.setText(sharer.getUsername());
		// lz不确定
		tvWork.setText(sharer.getLevel());
		tvCompany.setText(sharer.getCompany());
		// lz不确定
		mLoadImage.addTask(sharer.getUheader(), ivHead);
		// 填充控件内容
		final List<PicVO> pics = gxInfo.getPic();
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
					tr = new TableRow(GongXuDetailActivity.this);
					tl.addView(tr);
				}
				tr.setLayoutParams(tllp);
				RelativeLayout rl = new RelativeLayout(
						GongXuDetailActivity.this);
				rl.setLayoutParams(trlp);
				ImageView iv = new ImageView(GongXuDetailActivity.this);
				iv.setLayoutParams(rlp);
				rl.addView(iv);
				rl.setTag(pics.get(i).getOrgurl());
				iv.setTag(pics.get(i).getUrl());
				iv.setImageResource(R.drawable.default_image);
				mLoadImage.addTask(pics.get(i).getUrl(), iv);
				rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GongXuDetailActivity.this,
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
		mLoadImage.doTask();
		if (isCollect != null && isCollect.equals("1")) {
			// tvCollect.setText(R.string.label_collectCancel);
			// Drawable drawable = getResources().getDrawable(
			// R.drawable.tab_collect_on);
			// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
			// drawable.getMinimumHeight());
			// tvCollect.setCompoundDrawables(null, drawable, null, null);
			tvCollect.setBackgroundResource(R.drawable.tab_collect_on);
		}

		List<CmtVO> cmts = gxInfo.getCmt();
		if (cmts != null && !cmts.isEmpty()) {
			mList.addAll(cmts);
			mAdapter.notifyDataSetChanged();
		} else {
			mListViewFooter.noData(false);
		}
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				JsonHandler nljh = new JsonHandler(data,
						getApplicationContext());
				List<CmtVO> list = nljh.parsePagesCmt().getData();
				if (!list.isEmpty()) {
					// textViewTip.setVisibility(View.GONE);
					mListViewFooter.hasData();
					if (!append) {
						mList.clear();
					}
					mPage++;
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				} else {
					// textViewTip.setVisibility(View.VISIBLE);

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
					if (msg.obj instanceof ResourceGXVO) {
						ResourceGXVO gxinfo = (ResourceGXVO) msg.obj;
						loadHead(gxinfo);
					} else if (!msg.obj.equals("")) {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						ResourceGXVO gxinfo = nljh.parseGongxuInfo();
						if (null != gxinfo) {
							loadHead(gxinfo);
							// mFacade.saveOrUpdate(gxinfo);
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

			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.info10);
					// TextView numTV = (TextView)
					// findViewById(R.id.textViewGongXuZfNum);
					// numTV.setText(String.valueOf(Integer.valueOf(numTV
					// .getText().toString()) + 1));
				}
				break;
			}
			case MsgTagVO.MSG_COLLECT: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					// TextView collectBtn = (TextView)
					// findViewById(R.id.buttonTabCollect);
					// TextView numTV = (TextView)
					// findViewById(R.id.textViewGongXuCollectNum);
					if (isCollect != null && isCollect.equals("0")) {
						// tvCollect.setText(R.string.label_collectCancel);
						// Drawable drawable = getResources().getDrawable(
						// R.drawable.tab_collect_on);
						// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						// drawable.getMinimumHeight());
						// tvCollect.setCompoundDrawables(null, drawable, null,
						// null);
						tvCollect
								.setBackgroundResource(R.drawable.tab_collect_on);
						isCollect = "1";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_collectSuccess);
						// numTV.setText(String.valueOf(Integer.valueOf(numTV
						// .getText().toString()) + 1));
					} else {
						// tvCollect.setText(R.string.label_collect);
						// Drawable drawable = getResources().getDrawable(
						// R.drawable.tab_collect_off);
						// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						// drawable.getMinimumHeight());
						// tvCollect.setCompoundDrawables(null, drawable, null,
						// null);
						tvCollect
								.setBackgroundResource(R.drawable.tab_collect_off);
						isCollect = "0";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_cancelCollect);
						// numTV.setText(String.valueOf(Integer.valueOf(numTV
						// .getText().toString()) - 1));
					}
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			// 不缓存本地数据
			// ZhuoInfoVO info = mFacade.getById(msgid);
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			// msg.obj = info;
			// msg.sendToTarget();
		} else {
			String params = ZhuoCommHelper.getResourceGongxuDetail()
					+ "?msgid=" + msgid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
					GongXuDetailActivity.this, true, new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							GongXuDetailActivity.this.finish();
						}
					});
		}
	}

	private void loadCmt() {
		// 空的数据
		for (int i = 0; i < 10; i++)
			mList.add(new CmtVO());
		mAdapter.notifyDataSetChanged();
		// if (mListViewFooter.startLoading()) {
		// mList.clear();
		// mAdapter.notifyDataSetChanged();
		// mPage = 1;
		// String params = ZhuoCommHelper.getUrlCmtList();
		// params += "?msgid=" + msgid;
		// params += "&page=" + mPage;
		// mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_OTHER);
		// }
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

		// findViewById(R.id.buttonChat).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(TopicDetailActivity.this,
		// ChatActivity.class);
		// i.putExtra("userid", uid);
		// startActivity(i);
		// }
		// });
		tvShare.setBackgroundResource(R.drawable.tab_trainsfer);
		tvShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(GongXuDetailActivity.this,
						UserSelectActivity.class);
				ArrayList<String> tempids = new ArrayList<String>(1);
				tempids.add(uid);
				i.putStringArrayListExtra("otherids", tempids);
				startActivityForResult(i, MsgTagVO.MSG_FOWARD);
			}
		});
		tvCollect.setVisibility(View.VISIBLE);
		tvCollect.setBackgroundResource(R.drawable.tab_collect_off);
		tvCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isCollect == null || isCollect.equals("0")) {
					mConnHelper.collectMsg(msgid, "1", mUIHandler,
							MsgTagVO.MSG_COLLECT, null, true, null, null);
				} else {
					mConnHelper.collectMsg(msgid, "0", mUIHandler,
							MsgTagVO.MSG_COLLECT, null, true, null, null);
				}
			}
		});
		tvWrite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		tvCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 用intent启动拨打电话
				if (sharer==null  || sharer.getPhone() == null) {
					Toast.makeText(GongXuDetailActivity.this, "号码为空", 1100)
							.show();
					return;
				}
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ sharer.getPhone()));
				startActivity(intent);
			}
		});
		tvComment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(GongXuDetailActivity.this,
						MsgCmtActivity.class);
				i.putExtra("msgid", msgid);
				i.putExtra("parentid", msgid);
				startActivityForResult(i, MsgTagVO.MSG_CMT);
			}
		});
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
				// String forward = data.getStringExtra("forward");
				// if (forward != null && forward.equals("1")) {
				// TextView numTV = (TextView)
				// findViewById(R.id.textViewGongXuZfNum);
				// numTV.setText(String.valueOf(Integer.valueOf(numTV
				// .getText().toString()) + 1));
				// }
				// TextView numTV = (TextView)
				// findViewById(R.id.textViewGongXuCmtNum);
				// numTV.setText(String.valueOf(Integer.valueOf(numTV.getText()
				// .toString()) + 1));
				loadCmt();//
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
