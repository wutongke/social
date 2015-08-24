package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.Comment;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.ui.ResCommentActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.google.gson.Gson;

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
	private TopicCommentListAdapter mAdapter;
	private ArrayList<Comment> mList = new ArrayList<Comment>();
	private LoadImage mLoadImage =LoadImage.getInstance();
	private View mHeadView = null;
	private String msgid = null;
	private int mPage = 1;
	private AppClientLef mConnHelper = null;
	private String isCollect = "0";
	// private ListViewFooter mListViewFooter = null;
	private String uid = null;
	private PopupWindows pwh;
	// 不要本地缓存
	// private ZhuoInfoFacade mFacade = null;
	private String myid = null;
	private String toId;
	UserVO sharer;

	ResourceGXVO resDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gong_xu_detail);
		initTitle();

		mConnHelper = AppClientLef.getInstance(getApplicationContext());
		// mFacade = new ZhuoInfoFacade(getApplicationContext());
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		Intent i = getIntent();
		msgid = i.getStringExtra("msgid");

		mAdapter = new TopicCommentListAdapter(GongXuDetailActivity.this,
				mList, msgid);
		mListView = (ListView) findViewById(R.id.listViewDetail);
		mListView.setDividerHeight(0);
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
		// loadCmt();
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

		sharer = new UserVO();// 分享者
		sharer.setUsername(gxInfo.getName());
		sharer.setPost(ZhuoConnHelper.getInstance(context).getBaseDataSet()
				.getPosition().get(gxInfo.getPosition()).getContent());
		sharer.setCompany(gxInfo.getCompany());
		sharer.setUheader(gxInfo.getUheader());
		sharer.setUserid(gxInfo.getUserid());
		sharer.setPhone(gxInfo.getPhone());

		tvName.setText(sharer.getUsername());
		// lz不确定
		tvWork.setText(sharer.getLevel());
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
					(int) (70 * times), (int) (70 * times));
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
				iv.setScaleType(ScaleType.FIT_CENTER);
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

	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			if (data != null && !data.equals("")) {
				List<Comment> list = JsonHandler_Lef.parseCommentLZList(data);
				if (!list.isEmpty()) {
					// textViewTip.setVisibility(View.GONE);
					if (!append) {
						mList.clear();
					}
					mPage++;
					mList.addAll(list);
					mAdapter.notifyDataSetChanged();
				} else {
					// textViewTip.setVisibility(View.VISIBLE);

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
			case MsgTagVO.DATA_OTHER: {
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				updateItemList((String) msg.obj, false, true);
				break;
			}

			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					pwh.showPopTip(findViewById(R.id.linearLayoutBottom),
							null, R.string.label_share_success);
				}else{
					com.cpstudio.zhuojiaren.model.ResultVO res = JsonHandler.parseResult((String) msg.obj);
					CommonUtil.displayToast(getApplicationContext(),
							res.getMsg());
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
					if (isCollect != null && isCollect.equals("1")) {
						// tvCollect.setText(R.string.label_collectCancel);
						// Drawable drawable = getResources().getDrawable(
						// R.drawable.tab_collect_on);
						// drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						// drawable.getMinimumHeight());
						// tvCollect.setCompoundDrawables(null, drawable, null,
						// null);
						tvCollect
								.setBackgroundResource(R.drawable.dongt2);
						isCollect = "0";
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
								.setBackgroundResource(R.drawable.dongt);
						isCollect = "1";
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
		mConnHelper.getGongxuDetail(GongXuDetailActivity.this, mUIHandler,
				MsgTagVO.DATA_LOAD, msgid);
	}

	private void initClick() {

		tvShare.setBackgroundResource(R.drawable.share);
		tvShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConnHelper.shareRESToZhuo(GongXuDetailActivity.this,mUIHandler,MsgTagVO.MSG_FOWARD,msgid);
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
						MsgTagVO.MSG_COLLECT, ZhuoCommHelper.getCOMMONCOLLECTION(),"sdid", msgid, "type", type);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				toId = mList.get(position - 1).getId();
				Intent i = new Intent(GongXuDetailActivity.this,
						ResCommentActivity.class);
				i.putExtra("msgid", msgid);
				i.putExtra("toId", toId);
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
				toId = "-1";
				Intent i = new Intent(GongXuDetailActivity.this,
						ResCommentActivity.class);
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
				mList = JsonHandler_Lef.parseCommentLZList(data
						.getStringExtra("data"));
				mAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
