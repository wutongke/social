package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ZhuoInfoListAdapter extends BaseAdapter {
	private List<ZhuoInfoVO> mList = null;
	private LoadImage mLoadImage = new LoadImage();
	private LayoutInflater inflater = null;
	private ZhuoConnHelper mConnHelper = null;
	private Activity mContext = null;
	private int width = 720;
	private float times = 2;
	private PopupWindows phw = null;
	private String myid = null;

	public ZhuoInfoListAdapter(Activity context, ArrayList<ZhuoInfoVO> list) {
		this.mContext = context;
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.mConnHelper = ZhuoConnHelper.getInstance(context);
		this.width = DeviceInfoUtil.getDeviceCsw(context);
		this.times = DeviceInfoUtil.getDeviceCsd(context);
		this.phw = new PopupWindows(context);
		this.myid = ResHelper.getInstance(context).getUserid();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_zhuoinfo_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}

		ZhuoInfoVO zhuoinfo = mList.get(position);
		String id = zhuoinfo.getMsgid();
		UserVO user = zhuoinfo.getUser();
		String uid = user.getUserid();
		String company = user.getCompany();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String work = user.getPost();
		String type = zhuoinfo.getType();
		String category = zhuoinfo.getCategory();
		String title = zhuoinfo.getTitle();
		String detail = zhuoinfo.getText();
		convertView.setTag(R.id.tag_id, id);
		convertView.setTag(R.id.tag_string, type);
		holder.nameTV.setText(authorName);
		String woco = ZhuoCommHelper.concatStringWithTag(work, company, "|");
		if (woco.length() > 1) {
			holder.workTV.setText(woco);
			holder.workTV.setVisibility(View.VISIBLE);
		} else {
			holder.workTV.setText("");
			holder.workTV.setVisibility(View.GONE);
		}
		if (type != null && !type.equals("")) {
			Map<String, Object> resinfo = ZhuoCommHelper.gentResInfo(type,
					category, title, detail, mContext);
			holder.resIV.setImageResource((Integer) resinfo.get("ico"));
			String text = (String) resinfo.get("category")
					+ (String) resinfo.get("title")
					+ (String) resinfo.get("content");
			holder.resTV.setText(text);
			Rect bounds = new Rect();
			TextPaint paint = holder.resTV.getPaint();
			paint.getTextBounds(text, 0, text.length(), bounds);
			int width = bounds.width();
			if (width / (this.width - 81 * times) > 4) {
				holder.moreTV.setVisibility(View.VISIBLE);
			} else {
				holder.moreTV.setVisibility(View.GONE);
			}
		} else {
			holder.resIV.setImageResource(0);
			holder.resTV.setText("");
		}
		holder.resTV.setMaxLines(4);
		holder.moreTV.setText(R.string.info1);
		holder.tl.removeAllViews();
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIV);
		final List<PicVO> pics = zhuoinfo.getPic();
		RelativeLayout.LayoutParams layoutParams = holder.rlp;
		if (pics != null && pics.size() == 1) {
			layoutParams = holder.rlp2;
		}
		if (pics != null && pics.size() > 0) {
			TableRow tr = null;
			for (int i = 0; i < pics.size(); i++) {
				if (i % 3 == 0) {
					tr = new TableRow(mContext);
					holder.tl.addView(tr);
				}
				tr.setLayoutParams(holder.tllp);
				RelativeLayout rl = new RelativeLayout(mContext);
				rl.setLayoutParams(holder.trlp);
				ImageView iv = new ImageView(mContext);
				iv.setLayoutParams(layoutParams);
				rl.addView(iv);
				rl.setTag(pics.get(i).getOrgurl());
				iv.setTag(pics.get(i).getUrl());
				iv.setImageResource(R.drawable.default_image);
				mLoadImage.addTask(pics.get(i).getUrl(), iv);
				rl.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								PhotoViewMultiActivity.class);
						ArrayList<String> orgs = new ArrayList<String>();
						for (int j = 0; j < pics.size(); j++) {
							orgs.add(pics.get(j).getOrgurl());
						}
						intent.putStringArrayListExtra("pics", orgs);
						intent.putExtra("pic", (String) v.getTag());
						mContext.startActivity(intent);
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
		holder.timeTV.setText(time + "  " + place);
		if (myid.equals(uid)) {
			holder.delTV.setVisibility(View.VISIBLE);
		} else {
			holder.delTV.setVisibility(View.GONE);
		}
		List<String> tags = zhuoinfo.getTags();
		if (tags != null && tags.size() > 0) {
			String tagStr = "	 ";
			for (String tag : tags) {
				tagStr += tag + " ";
			}
			holder.tagTV.setText(tagStr);
			holder.relativeLayoutTag.setVisibility(View.VISIBLE);
		} else {
			holder.relativeLayoutTag.setVisibility(View.GONE);
		}
		String cmtNum = zhuoinfo.getCmtnum();
		String goodNum = zhuoinfo.getGoodnum();
		String collectNum = zhuoinfo.getCollectnum();
		String zfNum = zhuoinfo.getForwardnum();
		if (null == cmtNum || cmtNum.equals("")) {
			cmtNum = "0";
		}
		if (null == goodNum || goodNum.equals("")) {
			goodNum = "0";
		}
		if (null == collectNum || collectNum.equals("")) {
			collectNum = "0";
		}
		if (null == zfNum || zfNum.equals("")) {
			zfNum = "0";
		}
		holder.goodNumTV.setText(goodNum);
		holder.cmtNumTV.setText(cmtNum);
		holder.zfNumTV.setText(zfNum);
		holder.collectNumTV.setText(collectNum);
		List<CmtVO> cmts = zhuoinfo.getCmt();
		if (cmts != null && cmts.size() > 0) {
			holder.layoutGongXuCmt
					.setBackgroundResource(R.drawable.shape_gray_corners);
			for (CmtVO msg : cmts) {
				LinearLayout layoutCmtOne = new LinearLayout(mContext);
				layoutCmtOne.setLayoutParams(holder.layoutParams);
				layoutCmtOne.setOrientation(LinearLayout.HORIZONTAL);
				TextView userTv = new TextView(mContext);
				userTv.setLayoutParams(holder.layoutParams);
				userTv.setTextColor(0xff41546d);
				TextView contentTV = new TextView(mContext);
				contentTV.setLayoutParams(holder.layoutParams);
				UserVO cmtUser = msg.getUser();
				String uname = cmtUser.getUsername();
				String userid = cmtUser.getUserid();
				String msgContent = msg.getContent();
				userTv.setText(uname + ":");
				contentTV.setText(msgContent);
				layoutCmtOne.addView(userTv);
				layoutCmtOne.addView(contentTV);
				layoutCmtOne.setTag(userid);
				holder.layoutGongXuCmt.addView(layoutCmtOne);
			}
		}
		initEvent(holder, convertView, id, uid);
		mLoadImage.doTask();
		return convertView;
	}

	private void initEvent(final ViewHolder holder, View convertView,
			final String id, final String userid) {
		holder.headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, UserCardActivity.class);
				i.putExtra("userid", userid);
				mContext.startActivity(i);
			}
		});
		holder.moreTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				TextView showTypeView = (TextView) view;
				if (showTypeView.getText().equals(
						mContext.getString(R.string.info2))) {
					showTypeView.setText(R.string.info1);
					holder.resTV.setMaxLines(4);
				} else {
					showTypeView.setText(R.string.info2);
					holder.resTV.setMaxLines(200);
				}
			}
		});

		holder.delTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConnHelper.delResource(mHandler, MsgTagVO.MSG_DEL, null, id,
						false, null, id);
			}
		});

		holder.optionIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				OnClickListener zanListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						mConnHelper.goodMsg(id, mHandler, MsgTagVO.MSG_LIKE,
								null, true, null, id);
					}
				};
				OnClickListener cmtListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(mContext, MsgCmtActivity.class);
						i.putExtra("msgid", id);
						i.putExtra("parentid", id);
						mContext.startActivityForResult(i, MsgTagVO.MSG_CMT);
					}
				};
				phw.showOptionsPop(view, times, zanListener, cmtListener);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.MSG_LIKE: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					CommonUtil
							.displayToast(mContext, R.string.label_zanSuccess);
					Bundle bundle = msg.getData();
					String id = bundle.getString("data");
					for (ZhuoInfoVO item : mList) {
						if (id != null && id.equals(item.getMsgid())) {
								item.setGoodnum((Integer.valueOf(item
										.getGoodnum()) + 1) + "");
								break;
						}
					}
					notifyDataSetChanged();
				}
				break;
			}
			case MsgTagVO.MSG_DEL: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					CommonUtil
							.displayToast(mContext, R.string.info12);
					Bundle bundle = msg.getData();
					String id = bundle.getString("data");
					for (ZhuoInfoVO item : mList) {
						if (id != null && id.equals(item.getMsgid())) {
								mList.remove(item);
								break;
						}
					}
					notifyDataSetChanged();
				}
			}
			}
		}

	};

	static class ViewHolder {
		TextView nameTV;
		TextView workTV;
		TextView resTV;
		ImageView resIV;
		ImageView headIV;
		TableLayout tl;
		TableLayout.LayoutParams tllp;
		TableRow.LayoutParams trlp;
		RelativeLayout.LayoutParams rlp;
		RelativeLayout.LayoutParams rlp2;
		TextView timeTV;
		TextView delTV;
		TextView tagTV;
		RelativeLayout relativeLayoutTag;
		TextView goodNumTV;
		TextView cmtNumTV;
		TextView zfNumTV;
		TextView collectNumTV;
		LinearLayout layoutGongXuCmt;
		LayoutParams layoutParams;
		TextView moreTV;
		View optionIV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.workTV = (TextView) convertView
				.findViewById(R.id.textViewCmtContent);
		holder.resTV = (TextView) convertView.findViewById(R.id.textViewRes);
		holder.resIV = (ImageView) convertView.findViewById(R.id.imageViewRes);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);

		holder.tl = (TableLayout) convertView
				.findViewById(R.id.tableLayoutPics);
		holder.tllp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		holder.trlp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		holder.rlp = new RelativeLayout.LayoutParams((int) (70 * times),
				(int) (70 * times));
		holder.rlp2 = new RelativeLayout.LayoutParams((int) (160 * times),
				(int) (160 * times));
		holder.trlp.setMargins(0, 0, 10, 10);
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.delTV = (TextView) convertView.findViewById(R.id.textViewDel);
		holder.tagTV = (TextView) convertView
				.findViewById(R.id.textViewGongXuTag);
		holder.goodNumTV = (TextView) convertView
				.findViewById(R.id.textViewGongXuGoodNum);
		holder.cmtNumTV = (TextView) convertView
				.findViewById(R.id.textViewGongXuCmtNum);
		holder.zfNumTV = (TextView) convertView
				.findViewById(R.id.textViewGongXuZfNum);
		holder.collectNumTV = (TextView) convertView
				.findViewById(R.id.textViewGongXuCollectNum);
		holder.layoutGongXuCmt = ((LinearLayout) convertView
				.findViewById(R.id.layoutGongXuCmt));
		holder.layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		holder.moreTV = (TextView) convertView
				.findViewById(R.id.textViewViewMore);
		holder.optionIV = convertView.findViewById(R.id.optionButton);
		holder.relativeLayoutTag = (RelativeLayout) convertView
				.findViewById(R.id.relativeLayoutTag);
		return holder;
	}
}
