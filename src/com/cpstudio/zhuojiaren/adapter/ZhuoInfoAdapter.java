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

public class ZhuoInfoAdapter extends BaseAdapter {
	private List<ZhuoInfoVO> mList = null;
	private LoadImage mLoadImage = new LoadImage();
	private LayoutInflater inflater = null;
	private ZhuoConnHelper mConnHelper = null;
	private Activity mContext = null;
	private int width = 720;
	private float times = 2;
	private PopupWindows phw = null;
	private String myid = null;

	public ZhuoInfoAdapter(Activity context, ArrayList<ZhuoInfoVO> list) {
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
			convertView = inflater.inflate(R.layout.item_zhuoinfo, null);
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
		holder.nameTVoutter.setText(authorName);
		String woco = ZhuoCommHelper.concatStringWithTag(work, company, "|");
		if (woco.length() > 1) {
			holder.workTVoutter.setText(woco);
			holder.workTVoutter.setVisibility(View.VISIBLE);
		} else {
			holder.workTVoutter.setText("");
			holder.workTVoutter.setVisibility(View.GONE);
		}
		holder.headIVoutter.setImageResource(R.drawable.default_userhead);
		holder.headIVoutter.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIVoutter);
		String type = zhuoinfo.getType();
		String category = zhuoinfo.getCategory();
		String title = zhuoinfo.getTitle();
		String detail = zhuoinfo.getText();
		if (type != null && !type.equals("")) {
			Map<String, Object> resinfo = ZhuoCommHelper.gentResInfo(type,
					category, title, detail, mContext);
			holder.resIVoutter.setImageResource((Integer) resinfo.get("ico"));
			String text = (String) resinfo.get("category")
					+ (String) resinfo.get("title")
					+ (String) resinfo.get("content");
			if (text.trim().length() > 0) {
				((View) holder.resTVoutter.getParent())
						.setVisibility(View.VISIBLE);
				holder.resTVoutter.setText(text.trim());
				Rect bounds = new Rect();
				TextPaint paint = holder.resTVoutter.getPaint();
				paint.getTextBounds(text, 0, text.length(), bounds);
				int width = bounds.width();
				if (width / (this.width - 68 * times) > 4) {
					holder.moreTVoutter.setVisibility(View.VISIBLE);
				} else {
					holder.moreTVoutter.setVisibility(View.GONE);
				}
			} else {
				((View) holder.resTVoutter.getParent())
						.setVisibility(View.GONE);
				holder.moreTVoutter.setVisibility(View.GONE);
			}
		} else {
			holder.resIVoutter.setImageResource(0);
			holder.resTVoutter.setText("");
		}
		holder.resTVoutter.setMaxLines(4);
		holder.moreTVoutter.setText(R.string.info1);
		holder.tloutter.removeAllViews();
		String place = zhuoinfo.getPosition();
		if (null == place) {
			place = "";
		}
		String time = zhuoinfo.getAddtime();
		time = CommonUtil.calcTime(time);
		holder.timeTVoutter.setText(time + "  " + place);
		if (myid.equals(uid)) {
			holder.delTVoutter.setVisibility(View.VISIBLE);
		} else {
			holder.delTVoutter.setVisibility(View.GONE);
		}
		ZhuoInfoVO origin = zhuoinfo.getOrigin();
		if (origin == null) {
			convertView.setTag(R.id.tag_id, id);
			convertView.setTag(R.id.tag_string, type);
			holder.inner.setVisibility(View.GONE);
			holder.outter.setVisibility(View.VISIBLE);
			holder.tloutter.setVisibility(View.VISIBLE);
			final List<PicVO> pics = zhuoinfo.getPic();
			RelativeLayout.LayoutParams layoutParams = holder.rlpoutter;
			if (pics != null && pics.size() == 1) {
				layoutParams = holder.rlpoutter2;
			}
			if (pics != null && pics.size() > 0) {
				TableRow tr = null;
				for (int i = 0; i < pics.size(); i++) {
					if (i % 3 == 0) {
						tr = new TableRow(mContext);
						holder.tloutter.addView(tr);
					}
					tr.setLayoutParams(holder.tllpoutter);
					RelativeLayout rl = new RelativeLayout(mContext);
					rl.setLayoutParams(holder.trlpoutter);
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
			List<String> tags = zhuoinfo.getTags();
			if (tags != null && tags.size() > 0) {
				String tagStr = "	 ";
				for (String tag : tags) {
					tagStr += tag + " ";
				}
				holder.tagTVoutter.setText(tagStr);
				holder.relativeLayoutTagoutter.setVisibility(View.VISIBLE);
			} else {
				holder.relativeLayoutTagoutter.setVisibility(View.GONE);
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
			holder.goodNumTVoutter.setText(goodNum);
			holder.cmtNumTVoutter.setText(cmtNum);
			holder.zfNumTVoutter.setText(zfNum);
			holder.collectNumTVoutter.setText(collectNum);
			List<CmtVO> cmts = zhuoinfo.getCmt();
			if (cmts != null && cmts.size() > 0) {
				holder.layoutGongXuCmtoutter
						.setBackgroundResource(R.drawable.shape_gray_corners);
				for (CmtVO msg : cmts) {
					LinearLayout layoutCmtOne = new LinearLayout(mContext);
					layoutCmtOne.setLayoutParams(holder.layoutParamsoutter);
					layoutCmtOne.setOrientation(LinearLayout.HORIZONTAL);
					TextView userTv = new TextView(mContext);
					userTv.setLayoutParams(holder.layoutParamsoutter);
					userTv.setTextColor(0xff41546d);
					TextView contentTV = new TextView(mContext);
					contentTV.setLayoutParams(holder.layoutParamsoutter);
					UserVO cmtUser = msg.getUser();
					String uname = cmtUser.getUsername();
					String uidcmt = cmtUser.getUserid();
					String msgContent = msg.getContent();
					userTv.setText(uname + ":");
					contentTV.setText(msgContent);
					layoutCmtOne.addView(userTv);
					layoutCmtOne.addView(contentTV);
					layoutCmtOne.setTag(uidcmt);
					holder.layoutGongXuCmtoutter.addView(layoutCmtOne);
				}
			}
			initEventoutter(holder, convertView, id, uid);
		} else {
			holder.outter.setVisibility(View.GONE);
			holder.inner.setVisibility(View.VISIBLE);
			holder.tloutter.setVisibility(View.GONE);
			String msgidinner = origin.getMsgid();
			convertView.setTag(R.id.tag_id, msgidinner);
			UserVO orgUser = origin.getUser();
			final String userid = orgUser.getUserid();
			String companyinner = orgUser.getCompany();
			String authorNameinner = orgUser.getUsername();
			String headUrlinner = orgUser.getUheader();
			String workinner = orgUser.getPost();
			String typeinner = origin.getType();
			String categoryinner = origin.getCategory();
			String titleinner = origin.getTitle();
			String detailinner = origin.getText();
			holder.nameTV.setText(authorNameinner);
			String wocoinner = ZhuoCommHelper.concatStringWithTag(workinner,
					companyinner, "|");
			if (wocoinner.length() > 1) {
				holder.workTV.setText(wocoinner);
				holder.workTV.setVisibility(View.VISIBLE);
			} else {
				holder.workTV.setText("");
				holder.workTV.setVisibility(View.GONE);
			}
			convertView.setTag(R.id.tag_string, typeinner);
			if (typeinner != null && !typeinner.equals("")) {
				Map<String, Object> resifo = ZhuoCommHelper.gentResInfo(
						typeinner, categoryinner, titleinner, detailinner,
						mContext);
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
			holder.headIV.setTag(headUrlinner);
			holder.headIV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(mContext, UserCardActivity.class);
					i.putExtra("userid", userid);
					mContext.startActivity(i);
				}
			});
			mLoadImage.addTask(headUrlinner, holder.headIV);
			holder.tl.removeAllViews();
			final List<PicVO> picsinner = origin.getPic();
			RelativeLayout.LayoutParams layoutParams = holder.rlp;
			if (picsinner != null && picsinner.size() == 1) {
				layoutParams = holder.rlp2;
			}
			if (picsinner != null && picsinner.size() > 0) {
				TableRow tr = null;
				for (int i = 0; i < picsinner.size(); i++) {
					if (i % 3 == 0) {
						tr = new TableRow(mContext);
						holder.tl.addView(tr);
					}
					tr.setLayoutParams(holder.tllpoutter);
					RelativeLayout rl = new RelativeLayout(mContext);
					rl.setLayoutParams(holder.trlpoutter);
					ImageView iv = new ImageView(mContext);
					iv.setLayoutParams(layoutParams);
					rl.addView(iv);
					rl.setTag(picsinner.get(i).getOrgurl());
					iv.setTag(picsinner.get(i).getUrl());
					iv.setImageResource(R.drawable.default_image);
					mLoadImage.addTask(picsinner.get(i).getUrl(), iv);
					rl.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(mContext,
									PhotoViewMultiActivity.class);
							ArrayList<String> orgs = new ArrayList<String>();
							for (int j = 0; j < picsinner.size(); j++) {
								orgs.add(picsinner.get(j).getOrgurl());
							}
							intent.putStringArrayListExtra("pics", orgs);
							intent.putExtra("pic", (String) v.getTag());
							mContext.startActivity(intent);
						}
					});
					tr.addView(rl);
				}
			}
			String placeinner = origin.getPosition();
			if (null == placeinner) {
				placeinner = "";
			}
			String timeinner = origin.getAddtime();
			timeinner = CommonUtil.calcTime(timeinner);
			holder.timeTV.setText(timeinner + "  " + placeinner);
			List<String> tagsinner = origin.getTags();
			if (tagsinner != null && tagsinner.size() > 0) {
				String tagStr = "	 ";
				for (String tag : tagsinner) {
					tagStr += tag + " ";
				}
				holder.tagTV.setText(tagStr);
				holder.relativeLayoutTag.setVisibility(View.VISIBLE);
			} else {
				holder.relativeLayoutTag.setVisibility(View.GONE);
			}
			String cmtNuminner = origin.getCmtnum();
			String goodNuminner = origin.getGoodnum();
			String collectNuminner = origin.getCollectnum();
			String zfNuminner = origin.getForwardnum();
			if (null == cmtNuminner || cmtNuminner.equals("")) {
				cmtNuminner = "0";
			}
			if (null == goodNuminner || goodNuminner.equals("")) {
				goodNuminner = "0";
			}
			if (null == collectNuminner || collectNuminner.equals("")) {
				collectNuminner = "0";
			}
			if (null == zfNuminner || zfNuminner.equals("")) {
				zfNuminner = "0";
			}
			holder.goodNumTV.setText(goodNuminner);
			holder.cmtNumTV.setText(cmtNuminner);
			holder.zfNumTV.setText(zfNuminner);
			holder.collectNumTV.setText(collectNuminner);
			holder.moreTV.setTag(userid);
			holder.moreTV.setText(R.string.info1);
			holder.optionIV.setTag(userid);
			initEvent(holder, convertView, msgidinner, id, userid);
		}
		mLoadImage.doTask();
		return convertView;
	}

	private void initEvent(final ViewHolder holder, View convertView,
			final String innerMsgid, final String id, final String userid) {
		holder.moreTVoutter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				TextView showTypeView = (TextView) view;
				if (showTypeView.getText().equals(
						mContext.getString(R.string.info2))) {
					showTypeView.setText(R.string.info1);
					holder.resTVoutter.setMaxLines(4);
				} else {
					showTypeView.setText(R.string.info2);
					holder.resTVoutter.setMaxLines(200);
				}
			}
		});

		holder.delTVoutter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConnHelper.delResource(mHandler, MsgTagVO.MSG_DEL, null, id,
						false, null, id);
			}
		});

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
						mContext.startActivityForResult(i, MsgTagVO.MSG_CMT);
					}
				};
				phw.showOptionsPop(view, times, zanListener, cmtListener);
			}
		});
	}

	private void initEventoutter(final ViewHolder holder, View convertView,
			final String id, final String uid) {
		holder.headIVoutter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, UserCardActivity.class);
				i.putExtra("userid", uid);
				mContext.startActivity(i);
			}
		});
		holder.moreTVoutter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				TextView showTypeView = (TextView) view;
				if (showTypeView.getText().equals(
						mContext.getString(R.string.info2))) {
					showTypeView.setText(R.string.info1);
					holder.resTVoutter.setMaxLines(4);
				} else {
					showTypeView.setText(R.string.info2);
					holder.resTVoutter.setMaxLines(200);
				}
			}
		});

		holder.delTVoutter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConnHelper.delResource(mHandler, MsgTagVO.MSG_DEL, null, id,
						false, null, id);
			}
		});

		holder.optionIVoutter.setOnClickListener(new OnClickListener() {

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
						if (id != null) {
							if (id.equals(item.getMsgid())) {
								item.setGoodnum((Integer.valueOf(item
										.getGoodnum()) + 1) + "");
							} else if (item.getOrigin() != null
									&& id.equals(item.getOrigin().getMsgid())) {
								item.getOrigin().setGoodnum(
										(Integer.valueOf(item.getOrigin()
												.getGoodnum()) + 1) + "");
							}
						}
					}
					notifyDataSetChanged();
				}
				break;
			}
			case MsgTagVO.MSG_DEL: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					CommonUtil.displayToast(mContext, R.string.info12);
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
		TableLayout.LayoutParams tllpoutter;
		TableRow.LayoutParams trlpoutter;
		RelativeLayout.LayoutParams rlpoutter;
		RelativeLayout.LayoutParams rlpoutter2;
		LayoutParams layoutParamsoutter;
		TextView nameTVoutter;
		TextView workTVoutter;
		TextView resTVoutter;
		ImageView resIVoutter;
		ImageView headIVoutter;
		TableLayout tloutter;
		TextView timeTVoutter;
		TextView delTVoutter;
		TextView tagTVoutter;
		RelativeLayout relativeLayoutTagoutter;
		TextView goodNumTVoutter;
		TextView cmtNumTVoutter;
		TextView zfNumTVoutter;
		TextView collectNumTVoutter;
		LinearLayout layoutGongXuCmtoutter;
		TextView moreTVoutter;
		View optionIVoutter;

		ImageView headIV;
		TextView nameTV;
		TextView workTV;
		TextView gongTV;
		TextView xuTV;
		TextView boardTV;
		TableLayout tl;
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

		View outter;
		View inner;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.tllpoutter = new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		holder.trlpoutter = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		holder.rlpoutter = new RelativeLayout.LayoutParams((int) (70 * times),
				(int) (70 * times));
		holder.rlpoutter2 = new RelativeLayout.LayoutParams(
				(int) (160 * times), (int) (160 * times));
		holder.trlpoutter.setMargins(0, 0, 10, 10);
		holder.layoutGongXuCmtoutter = ((LinearLayout) convertView
				.findViewById(R.id.layoutAuthorGongXuCmt));
		holder.layoutParamsoutter = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		holder.nameTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.workTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorWork);
		holder.resTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorRes);
		holder.resIVoutter = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorRes);
		holder.headIVoutter = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		holder.tloutter = (TableLayout) convertView
				.findViewById(R.id.tableLayoutAuthorPics);
		holder.timeTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorTime);
		holder.delTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorDel);
		holder.tagTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorGongXuTag);
		holder.goodNumTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorGongXuGoodNum);
		holder.cmtNumTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorGongXuCmtNum);
		holder.zfNumTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorGongXuZfNum);
		holder.collectNumTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorGongXuCollectNum);
		holder.moreTVoutter = (TextView) convertView
				.findViewById(R.id.textViewAuthorViewMore);
		holder.optionIVoutter = convertView
				.findViewById(R.id.authorOptionButton);
		holder.relativeLayoutTagoutter = (RelativeLayout) convertView
				.findViewById(R.id.relativeLayoutAuthorTag);

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
		holder.rlp = new RelativeLayout.LayoutParams((int) (50 * times),
				(int) (50 * times));
		holder.rlp2 = new RelativeLayout.LayoutParams((int) (114 * times),
				(int) (114 * times));
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

		holder.outter = convertView.findViewById(R.id.layoutNew);
		holder.inner = convertView.findViewById(R.id.layoutOrgin);
		return holder;
	}
}
