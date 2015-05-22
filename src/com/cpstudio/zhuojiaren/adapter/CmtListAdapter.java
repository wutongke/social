package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CmtVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class CmtListAdapter extends BaseAdapter {
	private List<CmtVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage(10);
	private ZhuoConnHelper mConnHelper = null;
	private Activity mContext;
	private float times = 2;
	private PopupWindows phw = null;
	private String msgid = null;

	public CmtListAdapter(Activity context, ArrayList<CmtVO> list) {
		this.mConnHelper = ZhuoConnHelper.getInstance(context);
		this.mList = list;
		this.inflater = LayoutInflater.from(context);
		this.mContext = context;
		this.times = DeviceInfoUtil.getDeviceCsd(context);
		this.phw = new PopupWindows(context);
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
			convertView = inflater.inflate(R.layout.item_cmt_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		CmtVO cmt = mList.get(position);
		final String id = cmt.getId();
		convertView.setTag(R.id.tag_id, id);
		UserVO user = cmt.getUser();
		final String userid = user.getUserid();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String time = cmt.getAddtime();
		time = CommonUtil.calcTime(time);
		String content = cmt.getContent();
		String goodNum = cmt.getLikecnt();
		String company = user.getCompany();
		String work = user.getPost();
		String woco = ZhuoCommHelper.concatStringWithTag(work, company, "|");
		if (woco != null && !woco.equals("")) {
			holder.workTV.setText(woco);
		} else {
			holder.workTV.setVisibility(View.GONE);
		}
		holder.goodTV.setText(goodNum);
		holder.nameTV.setText(authorName);
		holder.timeTV.setText(time);
		holder.contentTV.setText(content);
		holder.contentTV.setBackgroundColor(Color.rgb(249, 249, 249));
		msgid = cmt.getMsgid();
		if (cmt.isLike()) {
			cmt.setLike(false);
			Drawable ico = mContext.getResources().getDrawable(
					R.drawable.ico_good_red);
			ico.setBounds(0, 0, ico.getMinimumWidth(), ico.getMinimumHeight());
			holder.goodTV.setCompoundDrawables(null, null, ico, null);
			holder.addOne.setVisibility(View.VISIBLE);
			holder.addOne.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.plus_one));
		}
		holder.contentTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				OnClickListener zanListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						String params = ZhuoCommHelper.getUrlCmtLike();
						params += "?msgid=" + id;
						mConnHelper.getFromServer(params, handler,
								MsgTagVO.MSG_LIKE, id);
					}
				};
				OnClickListener cmtListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent i = new Intent(mContext, MsgCmtActivity.class);
						i.putExtra("msgid", msgid);
						i.putExtra("parentid", id);
						i.putExtra("after", " || "
								+ holder.nameTV.getText().toString() + " "
								+ holder.contentTV.getText().toString());
						mContext.startActivityForResult(i, MsgTagVO.MSG_FOWARD);
					}
				};
				OnClickListener copyListener = new OnClickListener() {

					@Override
					public void onClick(View v) {
						ClipboardManager cbm = (ClipboardManager) mContext
								.getSystemService(Context.CLIPBOARD_SERVICE);
						cbm.setText(holder.contentTV.getText().toString());
						CommonUtil.displayToast(mContext, R.string.info78);
					}
				};
				view.setBackgroundColor(Color.rgb(234, 234, 234));
				phw.showCmtOptionsPop(view, times, zanListener, cmtListener,
						copyListener, holder.contentTV);
			}
		});
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		holder.headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserCardActivity.class);
				intent.putExtra("userid", userid);
				mContext.startActivity(intent);
			}
		});
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView nameTV;
		TextView timeTV;
		TextView goodTV;
		TextView workTV;
		TextView contentTV;
		ImageView headIV;
		View addOne;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.goodTV = (TextView) convertView
				.findViewById(R.id.textViewGoodnum);
		holder.workTV = (TextView) convertView.findViewById(R.id.textViewWork);
		holder.contentTV = (TextView) convertView
				.findViewById(R.id.textViewContent);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		holder.addOne = convertView.findViewById(R.id.viewAddOne);
		return holder;
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.MSG_LIKE: {
				if (JsonHandler.checkResult((String) msg.obj)) {
					Bundle bundle = msg.getData();
					String id = bundle.getString("data");
					for (CmtVO item : mList) {
						if (id.equals(item.getId())) {
							item.setLikecnt((Integer.valueOf(item.getLikecnt()) + 1)
									+ "");
							item.setLike(true);
							break;
						}
					}
					notifyDataSetChanged();
				}
				break;
			}
			}
		}
	};
}
