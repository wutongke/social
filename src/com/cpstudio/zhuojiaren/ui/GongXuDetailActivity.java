package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.TopicCommentListAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.UrlHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.Comment;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.google.gson.Gson;
/**
 * 供需详情接界面
 * @author lz
 *
 */
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
	@InjectView(R.id.imageViewCard)
	ImageView ivCard;

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
	private TopicCommentListAdapter mAdapter;
	private ArrayList<Comment> mList = new ArrayList<Comment>();
	private LoadImage mLoadImage = LoadImage.getInstance();
	private View mHeadView = null;
	private String msgid = null;
	private ConnHelper mConnHelper = null;
	private ConnHelper mConnHelperlz = null;
	private String isCollect = "0";
	private PopupWindows pwh;
	private String toId;
	UserNewVO sharer;

	ResourceGXVO resDetail;
	BaseCodeData baseDataSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gong_xu_detail);
		initTitle();

		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		mConnHelperlz = ConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");

		mAdapter = new TopicCommentListAdapter(GongXuDetailActivity.this,
				mList, msgid, Color.GRAY);

		mListView = (ListView) findViewById(R.id.listViewDetail);
		mListView.setDividerHeight(0);
		baseDataSet = mConnHelperlz.getBaseDataSet();
		LayoutInflater inflater = LayoutInflater
				.from(GongXuDetailActivity.this);
		mHeadView = (LinearLayout) inflater.inflate(
				R.layout.gongxu_detail_main_header, null);
		pwh = new PopupWindows(GongXuDetailActivity.this);
		mListView.addHeaderView(mHeadView);
		ButterKnife.inject(this);
		title.setText(R.string.title_activity_gong_xu_detail);
		mListView.setAdapter(mAdapter);
		loadData();

	}

	public void loadHead(ResourceGXVO gxInfo) {
		Context context = mHeadView.getContext();
		if (gxInfo == null)
			return;
		tvTitle.setText(gxInfo.getTitle());

		String time = gxInfo.getAddtime();
		tvTime.setText(time);

		tvContent.setText(gxInfo.getContent());

		isCollect = gxInfo.getIsCollection();

		sharer = new UserNewVO();// 分享者
		sharer.setName(gxInfo.getName());
		sharer.setCompany(gxInfo.getCompany());
		sharer.setUheader(gxInfo.getUheader());
		sharer.setUserid(gxInfo.getUserid());
		sharer.setPhone(gxInfo.getPhone());
		tvName.setText(sharer.getName());
		String work="";
		if (baseDataSet != null) {
			int pos = sharer.getPosition();
			if(pos>=1 && pos<=baseDataSet.getPosition().size())
			{
				pos--; //默认为0，城市标号从1开始
				work = ((baseDataSet.getPosition()).get(pos)).getContent();
			}
		}
		tvWork.setText(work);
		tvCompany.setText(sharer.getCompany());
		// lz不确定
		mLoadImage.addTask(sharer.getUheader(), ivHead);
		// 填充控件内容
		final List<PicNewVO> pics = gxInfo.getSdPic();
		TableLayout.LayoutParams tllp = new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		TableRow.LayoutParams trlp = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		trlp.setMargins(0, 0, 10, 10);
		if (pics != null && pics.size() > 0) {
			float times = DeviceInfoUtil.getDeviceCsd(context);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
					(int) (60 * times), (int) (60 * times));
			TableLayout tl = (TableLayout) findViewById(R.id.gongxu_tableLayoutAuthorPics);
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
				iv.setScaleType(ScaleType.CENTER_CROP);
				iv.setLayoutParams(rlp);
				rl.addView(iv);
				rl.setTag(pics.get(i).getPic());
				iv.setTag(pics.get(i).getPic());
				iv.setImageResource(R.drawable.default_image);
				mLoadImage.addTask(pics.get(i).getPic(), iv);
				rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(GongXuDetailActivity.this,
								PhotoViewMultiActivity.class);
						ArrayList<String> orgs = new ArrayList<String>();
						for (int j = 0; j < pics.size(); j++) {
							orgs.add(pics.get(j).getPic());
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
			tvCollect.setBackgroundResource(R.drawable.dongt);
		}

		List<Comment> cmts = gxInfo.getCommentList();
		if (cmts != null && !cmts.isEmpty()) {
			mList.addAll(cmts);
			mAdapter.notifyDataSetChanged();
		}
	}


	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				com.cpstudio.zhuojiaren.model.ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj,
						GongXuDetailActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(GongXuDetailActivity.this,
							R.string.data_error);
					return;
				}
				Gson gson = new Gson();
				try {
					resDetail = gson
							.fromJson(res.getData(), ResourceGXVO.class);
					loadHead(resDetail);
				} catch (Exception e) {
					// TODO: handle exception
					CommonUtil.displayToast(GongXuDetailActivity.this,
							R.string.data_error);
					Log.d("Debug", "json数据出错。。。。。。。。。。。。。。");
					return;
				}
				initClick();
				break;
			}

			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					pwh.showPopTip(findViewById(R.id.linearLayoutBottom), null,
							R.string.label_share_success);
				} else {
					com.cpstudio.zhuojiaren.model.ResultVO res = JsonHandler
							.parseResult((String) msg.obj);
					CommonUtil.displayToast(getApplicationContext(),
							res.getMsg());
				}
				break;
			}
			case MsgTagVO.MSG_COLLECT: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (isCollect != null && isCollect.equals("1")) {
						tvCollect.setBackgroundResource(R.drawable.dongt2);
						isCollect = "0";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_collectSuccess);
					} else {
						tvCollect.setBackgroundResource(R.drawable.dongt);
						isCollect = "1";
						pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
								null, R.string.label_cancelCollect);
					}
				}
				break;
			}
			}
		}
	};

	private void loadData() {
		mConnHelper.getGongxuDetail(GongXuDetailActivity.this, mUIHandler,
				MsgTagVO.DATA_LOAD, msgid);
	}

	private void initClick() {
		ivCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (sharer != null) {
					Intent intent = new Intent(GongXuDetailActivity.this,
							ZhuoMaiCardActivity.class);
					intent.putExtra("userid", sharer.getUserid());
					startActivity(intent);
				}
			}
		});
		tvShare.setBackgroundResource(R.drawable.share);
		tvShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConnHelper.shareRESToZhuo(GongXuDetailActivity.this,
						mUIHandler, MsgTagVO.MSG_FOWARD, msgid);
			}
		});
		tvCollect.setVisibility(View.VISIBLE);
		tvCollect.setBackgroundResource(R.drawable.dongt);
		tvCollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String type = "0";
				if (isCollect == null || isCollect.equals("0")) {
					type = "0";
				} else {
					type = "1";
				}
				mConnHelper.collection(GongXuDetailActivity.this, mUIHandler,
						MsgTagVO.MSG_COLLECT,
						UrlHelper.getCOMMONCOLLECTION(), "sdid", msgid,
						"type", type);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position <= 0)
					return;
				toId = mList.get(position - 1).getId();
				Intent i = new Intent(GongXuDetailActivity.this,
						ResCommentActivity.class);
				i.putExtra("type", 3);
				i.putExtra("msgid", msgid);
				i.putExtra("toId", toId);
				i.putExtra("toUserid", mList.get(position - 1).getUserid());
				i.putExtra("toUserName", mList.get(position - 1).getName());
				startActivityForResult(i, MsgTagVO.MSG_CMT);
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
				if (sharer == null || sharer.getPhone() == null) {
					Toast.makeText(GongXuDetailActivity.this, "号码为空", Toast.LENGTH_SHORT)
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
						ResCommentActivity.class);
				i.putExtra("type", 3);
				i.putExtra("msgid", msgid);
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
				ArrayList<Comment> list = JsonHandler
						.parseCommentLZList(data.getStringExtra("data"));
				mList.clear();
				mList.addAll(list);
				mAdapter.notifyDataSetChanged();
				mListView.setSelection(mList.size() - 1);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
