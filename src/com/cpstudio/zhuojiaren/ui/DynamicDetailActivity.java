package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.TopicCommentListAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.Comment;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.Praise;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.CustomShareBoard;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.RoundImageView;
import com.cpstudio.zhuojiaren.widget.ViewHolder;

/**
 * 动态详情页面，与话题详情页面TopicDetailActivity的布局一样
 * 
 * @author lz
 * 
 */
public class DynamicDetailActivity extends BaseActivity {
	private ListView mListView;
	private TopicCommentListAdapter mAdapter;
	private ArrayList<Comment> mList = new ArrayList<Comment>();
	private LoadImage mLoadImage = LoadImage.getInstance();
	private View mHeadView = null;
	private PopupWindows pwh;
	private String msgid = null;
	private ConnHelper mConnHelper = null;
	private String isCollect = "0";
	private String uid = null;
	TextView collectBtn;
	View textViewTip;
	Dynamic dynamicDetail;
	ImageView headIV;
	BaseCodeData baseDataSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_detail);
		initTitle();
		ButterKnife.inject(this);
		title.setText(R.string.dynamic_detail);

		mConnHelper = ConnHelper.getInstance(getApplicationContext());

		Intent intent = getIntent();
		msgid = intent.getStringExtra("msgid");

		pwh = new PopupWindows(DynamicDetailActivity.this);
		mAdapter = new TopicCommentListAdapter(this, mList, msgid);
		mListView = (ListView) findViewById(R.id.listViewDetail);
		mListView.setDividerHeight(0);
		LayoutInflater inflater = LayoutInflater
				.from(DynamicDetailActivity.this);
		mHeadView = (LinearLayout) inflater.inflate(
				R.layout.listview_header_topic_detail, null);
		mListView.addHeaderView(mHeadView);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (mList.size() <= 0 || position < 1)
					return;
				Comment cmt = mList.get(position - 1);
				startCommentActivity(cmt.getId(), cmt.getUserid());
			}
		});
		baseDataSet = mConnHelper.getBaseDataSet();
		initClick();
		loadData();
	}

	public void fillData() {
		((TextView) (mHeadView.findViewById(R.id.textViewAuthorName)))
				.setText(dynamicDetail.getName());

		String work = "";
		if (baseDataSet != null) {
			int pos = dynamicDetail.getPosition();
			if (pos != 0)
				pos--;
			work = ((baseDataSet.getPosition()).get(pos)).getContent();
		}

		((TextView) (mHeadView.findViewById(R.id.textViewWork))).setText(work);

		((TextView) (mHeadView.findViewById(R.id.textViewRes)))
				.setText(dynamicDetail.getCompany());

		((TextView) (mHeadView.findViewById(R.id.textViewTime)))
				.setText(dynamicDetail.getAddtime());
		((TextView) (mHeadView.findViewById(R.id.textViewCmtContent)))
				.setText(dynamicDetail.getContent());

		String url = dynamicDetail.getUheader();

		headIV.setTag(url);
		mLoadImage.addTask(url, headIV);

		mHeadView.setTag(dynamicDetail.getStatusid());

		uid = dynamicDetail.getUserid();

		Context context = mHeadView.getContext();
		final List<PicNewVO> pics = dynamicDetail.getStatusPic();
		MyGridView gvImages = (MyGridView) mHeadView
				.findViewById(R.id.picGridView);
		gvImages.setVisibility(View.GONE);
		if (pics != null && pics.size() > 0) {
			ArrayList<String> urls = new ArrayList<String>();
			for (PicNewVO temp : pics) {
				if (temp.getPic() != null && !"".equals(temp.getPic().trim()))
					urls.add(temp.getPic());
			}
			if (urls.size() > 0)
				gvImages.setVisibility(View.VISIBLE);
			GridViewAdapter gv = new GridViewAdapter(context, urls,
					R.layout.item_gridview_image);
			gvImages.setAdapter(gv);
			gv.notifyDataSetChanged();
		}

		mLoadImage.doTask();

		fillPraiseList(dynamicDetail.getPraiseList());
		fillCommentList(dynamicDetail.getCommentList());
	}

	private void fillCommentList(List<Comment> cmts) {
		if (cmts != null && !cmts.isEmpty()) {
			if (mList != null)
				mList.clear();
			mList.addAll(cmts);
			mAdapter.notifyDataSetChanged();
			textViewTip.setVisibility(View.GONE);
		} else {
			textViewTip.setVisibility(View.VISIBLE);
		}
	}

	private void fillPraiseList(List<Praise> praiseList) {
		if (praiseList != null && praiseList.size() > 0) {
			findViewById(R.id.layoutGood).setVisibility(View.VISIBLE);
			findViewById(R.id.imageViewGood).setVisibility(View.VISIBLE);
			Context context = mHeadView.getContext();
			TableLayout.LayoutParams tllp = new TableLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

			TableRow.LayoutParams trlp = new TableRow.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			TableLayout tl = (TableLayout) findViewById(R.id.tableLayoutGood);
			if (tl != null)
				tl.removeAllViews();
			int width = (int) (30 * DeviceInfoUtil
					.getDeviceCsd(DynamicDetailActivity.this));
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					width, width);
			rlp.setMargins(10, 0, 10, 0);
			TableRow tr = null;
			for (int i = 0; i < praiseList.size(); i++) {
				Praise praise = praiseList.get(i);
				if (i % 6 == 0) {
					tr = new TableRow(context);
					tr.setLayoutParams(tllp);
					tl.addView(tr);
				}
				RelativeLayout rl = new RelativeLayout(context);
				rl.setLayoutParams(trlp);
				RoundImageView iv = new RoundImageView(context);
				iv.setLayoutParams(rlp);
				rl.addView(iv);
				iv.setImageResource(R.drawable.default_userhead);
				iv.setTag(praise.getUheader());
				mLoadImage.addTask(praise.getUheader(), iv);
				final String userid = praise.getUserid();
				rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(DynamicDetailActivity.this,
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
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					dynamicDetail = nljh.parseDynamicDetail();
					if (null != dynamicDetail) {
						fillData();
					}
				}
				break;
			case MsgTagVO.MSG_LIKE:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_zanSuccess);
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					List<Praise> praiseList = nljh.parseQuanTopicPraiseList();
					fillPraiseList(praiseList);
				}
				break;
			case MsgTagVO.MSG_FOWARD:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.info10);
				}
				break;
			case MsgTagVO.MSG_COLLECT:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (isCollect != null && isCollect.equals("0")) {
						isCollect = "1";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_collectSuccess);
					} else {
						isCollect = "0";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_cancelCollect);
					}
				}
				break;
			}
		}
	};

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getDetailDynamic(mUIHandler, MsgTagVO.DATA_LOAD, msgid);
		}
	}

	private void initClick() {
		headIV = (ImageView) findViewById(R.id.imageViewAuthorHeader);
		headIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				Intent intent = new Intent(DynamicDetailActivity.this,
						ZhuoMaiCardActivity.class);
				intent.putExtra("userid", uid);
				startActivity(intent);
			}
		});
		findViewById(R.id.buttonTabZan).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						mConnHelper.praiseDynamic(mUIHandler,
								MsgTagVO.MSG_LIKE, msgid, 1);
					}
				});
		findViewById(R.id.buttonTabCmt).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startCommentActivity(null, null);
					}
				});
		findViewById(R.id.buttonTabShare).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						CustomShareBoard cb = new CustomShareBoard(
								DynamicDetailActivity.this);
						cb.showCustomShareContent();
					}
				});
		collectBtn = (TextView) findViewById(R.id.buttonTabCollect);
		collectBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConnHelper.collectDynamic(mUIHandler, MsgTagVO.MSG_COLLECT,
						msgid, 1);
			}
		});
		textViewTip = findViewById(R.id.textViewTip);
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
				String dataStr = data.getStringExtra("data");
				JsonHandler nljh = new JsonHandler(dataStr,
						getApplicationContext());
				List<Comment> commentList = nljh.parseQuanTopicCommentList();
				fillCommentList(commentList);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	void startCommentActivity(String toId, String toUserid) {
		Intent i = new Intent(DynamicDetailActivity.this, MsgCmtActivity.class);
		i.putExtra("msgid", msgid);
		if (toId != null)
			i.putExtra("toId", toId);
		if (toUserid != null)
			i.putExtra("toUserid", toUserid);
		i.putExtra("type", 2);
		startActivityForResult(i, MsgTagVO.MSG_CMT);
	}

	// 多张图片
	class GridViewAdapter extends CommonAdapter<String> {

		public GridViewAdapter(Context context, List<String> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder helper, String item) {
			// TODO Auto-generated method stub
			ImageView iv = helper.getView(R.id.gridview_image);
			iv.setTag(item);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(mContext,
							PhotoViewMultiActivity.class);
					ArrayList<String> orgs = new ArrayList<String>();
					orgs = (ArrayList<String>) mDatas;
					intent.putStringArrayListExtra("pics", orgs);
					intent.putExtra("pic", (String) v.getTag());
					mContext.startActivity(intent);
				}
			});
			mLoadImage.addTask(item,
					(ImageView) helper.getView(R.id.gridview_image));
		}

	}
}
