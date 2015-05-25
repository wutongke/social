package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.facade.CmtRcmdFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CmtRcmdVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
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
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CmtRcmdListAdapter extends BaseAdapter {
	private List<CmtRcmdVO> mList = null;
	private LoadImage mLoadImage = new LoadImage();
	private LayoutInflater inflater = null;
	private ZhuoConnHelper mConnHelper = null;
	private Activity mContext = null;
	private int width = 720;
	private float times = 2;
	private PopupWindows phw = null;
	private String myid = null;
	private CmtRcmdFacade mFacade = null;

	public CmtRcmdListAdapter(Activity context, ArrayList<CmtRcmdVO> list) {
		this.mContext = context;
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.mConnHelper = ZhuoConnHelper.getInstance(context);
		this.width = DeviceInfoUtil.getDeviceCsw(context);
		this.times = DeviceInfoUtil.getDeviceCsd(context);
		this.phw = new PopupWindows(context);
		this.myid = ResHelper.getInstance(context).getUserid();
		this.mFacade = new CmtRcmdFacade(context);
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
			convertView = inflater.inflate(R.layout.item_zhuoinfo_inner_list,
					null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		CmtRcmdVO crvo = mList.get(position);
		UserVO user2 = crvo.getSender();
		final String id = crvo.getId();
		String outtercompany = user2.getCompany();
		String outterauthorName = user2.getUsername();
		String outterheadUrl = user2.getUheader();
		String outterwork = user2.getPost();
		final String outteruserid = user2.getUserid();
		String cmtText = crvo.getContent();
		String outtertime = crvo.getAddtime();
		ZhuoInfoVO zhuoinfo = crvo.getOrgin();
		String msgid = zhuoinfo.getMsgid();
		convertView.setTag(R.id.tag_id, msgid);
		convertView.setTag(R.id.tag_string, zhuoinfo.getType());
		UserVO user = zhuoinfo.getUser();
		final String userid = user.getUserid();
		String company = user.getCompany();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String work = user.getPost();
		String type = zhuoinfo.getType();
		String category = zhuoinfo.getCategory();
		String title = zhuoinfo.getTitle();
		String detail = zhuoinfo.getText();
		holder.outterNameTV.setText(outterauthorName);
		holder.outterHeadIV.setImageResource(R.drawable.default_userhead);
		holder.outterHeadIV.setTag(outterheadUrl);
		holder.outterHeadIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, UserCardActivity.class);
				i.putExtra("userid", outteruserid);
				mContext.startActivity(i);
			}
		});
		mLoadImage.addTask(outterheadUrl, holder.outterHeadIV);
		String outterwoco = ZhuoCommHelper.concatStringWithTag(outterwork,
				outtercompany, "|");
		if (outterwoco.length() > 1) {
			holder.outterWorkTV.setText(outterwoco);
			holder.outterWorkTV.setVisibility(View.VISIBLE);
		} else {
			holder.outterWorkTV.setText("");
			holder.outterWorkTV.setVisibility(View.GONE);
		}
		holder.textViewCmtContent.setText(cmtText);

		outtertime = CommonUtil.calcTime(outtertime);
		holder.timeTVoutter.setText(outtertime);
		if (myid.equals(outteruserid)) {
			holder.delTVoutter.setVisibility(View.VISIBLE);
			holder.delTVoutter.setTag(position);
			holder.delTVoutter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mFacade.delete(id) > 0) {
						CommonUtil.displayToast(mContext, R.string.info12);
						for (CmtRcmdVO item : mList) {
							if (id != null && id.equals(item.getId())) {
								mList.remove(item);
								break;
							}
						}
						notifyDataSetChanged();
					} else {
						CommonUtil.displayToast(mContext, R.string.error8);
					}
				}
			});
		} else {
			holder.delTVoutter.setVisibility(View.GONE);
		}
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
			Map<String, Object> resifo = ZhuoCommHelper.gentResInfo(type,
					category, title, detail, mContext);
			holder.resIV.setImageResource((Integer) resifo.get("ico"));
			String text = (String) resifo.get("category")
					+ (String) resifo.get("title")
					+ (String) resifo.get("content");
			holder.resTV.setText(text);
			Rect bounds = new Rect();
			TextPaint paint = holder.resTV.getPaint();
			paint.getTextBounds(text, 0, text.length(), bounds);
			int width = bounds.width();
			if (width / (this.width - 88 * times) > 4) {
				holder.moreTV.setVisibility(View.VISIBLE);
			} else {
				holder.moreTV.setVisibility(View.GONE);
			}
		} else {
			holder.resIV.setImageResource(0);
			holder.resTV.setText("");
		}
		holder.resTV.setMaxLines(4);
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		holder.headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, UserCardActivity.class);
				i.putExtra("userid", userid);
				mContext.startActivity(i);
			}
		});
		mLoadImage.addTask(headUrl, holder.headIV);
		holder.tl.removeAllViews();
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
		holder.moreTV.setTag(userid);
		holder.moreTV.setText(R.string.info1);
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
		final String innerMsgid = zhuoinfo.getMsgid();
		convertView.setTag(innerMsgid);
		holder.optionIV.setTag(userid);
		holder.optionIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				OnClickListener zanListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						mConnHelper
								.goodMsg(innerMsgid, mHandler,
										MsgTagVO.MSG_LIKE, null, true, null,
										innerMsgid);
					}
				};
				OnClickListener cmtListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(mContext, MsgCmtActivity.class);
						i.putExtra("msgid", innerMsgid);
						i.putExtra("parentid", innerMsgid);
						i.putExtra("outterid", id);
						i.putExtra("userid", userid);
						mContext.startActivityForResult(i, MsgTagVO.MSG_CMT);
					}
				};
				phw.showOptionsPop(view, times, zanListener, cmtListener);
			}
		});
		mLoadImage.doTask();
		return convertView;
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
					int now = -1;
					for (CmtRcmdVO item : mList) {
						if (id != null && item.getOrgin() != null
								&& id.equals(item.getOrgin().getMsgid())) {
							if (now < 0) {
								now = Integer.valueOf(item.getOrgin()
										.getGoodnum());
							}
							item.getOrgin().setGoodnum((now + 1) + "");
						}
					}
					notifyDataSetChanged();
				}
				break;
			}
			}
		}

	};

	static class ViewHolder {
		ImageView outterHeadIV;
		TextView outterNameTV;
		TextView outterWorkTV;
		TextView timeTVoutter;
		TextView delTVoutter;
		TextView textViewCmtContent;
		ImageView headIV;
		TextView nameTV;
		TextView workTV;
		TextView gongTV;
		TextView xuTV;
		TextView boardTV;
		TableLayout tl;
		TableLayout.LayoutParams tllp;
		TableRow.LayoutParams trlp;
		RelativeLayout.LayoutParams rlp;
		RelativeLayout.LayoutParams rlp2;
		TextView timeTV;
		TextView tagTV;
		TextView goodNumTV;
		TextView cmtNumTV;
		TextView zfNumTV;
		TextView collectNumTV;
		TextView moreTV;
		View optionIV;
		TextView resTV;
		ImageView resIV;
		RelativeLayout relativeLayoutTag;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.outterHeadIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		holder.outterNameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.outterWorkTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorWork);
		holder.timeTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorTime);
		holder.delTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorDel);
		holder.textViewCmtContent = (TextView) convertView
				.findViewById(R.id.textViewCmtContent);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewHeader);
		holder.nameTV = (TextView) convertView.findViewById(R.id.textViewName);
		holder.workTV = (TextView) convertView.findViewById(R.id.textViewWork);
		holder.gongTV = (TextView) convertView.findViewById(R.id.textViewGong);
		holder.xuTV = (TextView) convertView.findViewById(R.id.textViewXu);
		holder.boardTV = (TextView) convertView
				.findViewById(R.id.textViewBoard);
		holder.tl = (TableLayout) convertView
				.findViewById(R.id.tableLayoutPics);
		holder.tllp = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		holder.trlp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		holder.rlp = new RelativeLayout.LayoutParams((int) (50 * times),
				(int) (50 * times));
		holder.rlp2 = new RelativeLayout.LayoutParams((int) (114 * times),
				(int) (114 * times));
		holder.trlp.setMargins(0, 0, 10, 10);
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
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
		holder.moreTV = (TextView) convertView
				.findViewById(R.id.textViewViewMore);
		holder.optionIV = convertView.findViewById(R.id.optionButton);
		holder.resTV = (TextView) convertView.findViewById(R.id.textViewRes);
		holder.resIV = (ImageView) convertView.findViewById(R.id.imageViewRes);
		holder.relativeLayoutTag = (RelativeLayout) convertView
				.findViewById(R.id.relativeLayoutTag);
		return holder;
	}
}
