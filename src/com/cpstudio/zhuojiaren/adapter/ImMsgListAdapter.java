package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;
import com.cpstudio.zhuojiaren.facade.ImChatFacade;
import com.cpstudio.zhuojiaren.helper.EmotionPopHelper;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.ImMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.GifMovieView;
import com.utils.ImageRectUtil;

public class ImMsgListAdapter extends BaseAdapter {
	private List<ImMsgVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private boolean mEditMode = false;
	private Set<String> mSelectedId = new HashSet<String>();
	private Context mContext = null;
	private ImChatFacade mFacade = null;
	private float times = 0;
	private OnClickListener clickListener;
	private OnClickListener sendListener;
	private OnLongClickListener longListener;

	// private static final int LOOP_TIMES = 5;

	public ImMsgListAdapter(Context context, ArrayList<ImMsgVO> list,
			OnClickListener clickListener, OnLongClickListener longListener,
			OnClickListener sendListener) {
		this.mList = list;
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.mFacade = new ImChatFacade(context);
		this.times = DeviceInfoUtil.getDeviceCsd(context);
		this.clickListener = clickListener;
		this.sendListener = sendListener;
		this.longListener = longListener;
	}

	public void delAll() {
		for (ImMsgVO mu : mList) {
			String id = mu.getId();
			mFacade.delete(id);
		}
		mList.clear();
		finishEdit();
		notifyDataSetChanged();
	}

	public void delSelected() {
		if (mSelectedId.size() > 0) {
			ArrayList<ImMsgVO> list = new ArrayList<ImMsgVO>();
			for (int i = mList.size() - 1; i >= 0; i--) {
				ImMsgVO mu = mList.get(i);
				String id = mu.getId();
				if (mSelectedId.contains(id)) {
					list.add(mu);
					mFacade.delete(id);
				}
			}
			mList.removeAll(list);
		}
		finishEdit();
		notifyDataSetChanged();
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_chat_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		ImMsgVO msgUser = mList.get(position);
		final String id = msgUser.getId();
		String time = msgUser.getSecs();
		UserVO user = msgUser.getSender();
		String headUrl = user.getUheader();
		String msgContent = msgUser.getContent();
		String msgType = msgUser.getType();
		final String userid = user.getUserid();
		String myId = ResHelper.getInstance(mContext).getUserid();
		convertView.setTag(R.id.tag_id, id);
		TextView timeTV;
		TextView msgTV;
		ImageView headIV;
		CheckBox checkBox;
		RelativeLayout rl;
		RelativeLayout gv;
		TextView cardname;
		TextView cardcompany;
		ImageView cardhead;
		View cardView;
		View failed;
		View progressBar;
		boolean left = false;
		destoryGif(holder.gv);
		destoryGif(holder.gv2);
		if (!myId.equals(userid)) {
			rl = holder.layoutLeft;
			holder.relativeLayoutChatLeft.setVisibility(View.VISIBLE);
			holder.relativeLayoutChatRight.setVisibility(View.GONE);
			timeTV = holder.timeTV;
			msgTV = holder.msgTV;
			headIV = holder.headIV;
			gv = holder.gv;
			checkBox = holder.checkBox;
			holder.layoutLeft.setPadding(holder.paddingLeft, holder.paddingTop,
					holder.paddingRight, holder.paddingBottom);
			cardname = holder.cardname;
			cardcompany = holder.cardcompany;
			cardhead = holder.cardhead;
			cardView = holder.layoutCard;
			failed = holder.failed;
			progressBar = holder.progressBar;
			left = true;
		} else {
			rl = holder.layoutRight;
			holder.relativeLayoutChatLeft.setVisibility(View.GONE);
			holder.relativeLayoutChatRight.setVisibility(View.VISIBLE);
			timeTV = holder.timeTV2;
			msgTV = holder.msgTV2;
			headIV = holder.headIV2;
			gv = holder.gv2;
			checkBox = holder.checkBox2;
			LayoutParams lp = (LayoutParams) msgTV.getLayoutParams();
			lp.leftMargin = holder.marginLeft;
			msgTV.setLayoutParams(lp);
			cardname = holder.cardname2;
			cardcompany = holder.cardcompany2;
			cardhead = holder.cardhead2;
			cardView = holder.layoutCard2;
			failed = holder.failed2;
			progressBar = holder.progressBar2;
		}
		cardView.setVisibility(View.GONE);
		msgTV.setBackgroundResource(0);
		msgTV.setText("");
		rl.setVisibility(View.VISIBLE);
		timeTV.setText("");
		rl.setTag(R.id.tag_id, id);
		rl.setTag(R.id.tag_boolean, left);
		rl.setTag(R.id.tag_type, msgType);
		rl.setTag(R.id.tag_path, "");
		rl.setTag(R.id.tag_state, msgUser.getIsread());
		if (msgUser.getIsread().equals("2")) {
			failed.setVisibility(View.VISIBLE);
		} else {
			failed.setVisibility(View.GONE);
		}
		failed.setTag(R.id.tag_id, id);
		failed.setOnClickListener(sendListener);
		if (msgUser.getIsread().equals("4")) {
			progressBar.setVisibility(View.VISIBLE);
		} else {
			progressBar.setVisibility(View.GONE);
		}
		checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					mSelectedId.add(id);
				} else {
					mSelectedId.remove(id);
				}
			}
		});
		if (mEditMode) {
			checkBox.setVisibility(View.VISIBLE);
			if (mSelectedId.contains(id)) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}
		} else {
			checkBox.setVisibility(View.GONE);
		}
		if (null == msgType || msgType.equals("text")) {
			msgTV.setText(msgContent);
		} else if (msgType.equals("voice")) {
			int margin = (int) (10 * times);
			try {
				int sec = Integer.valueOf(time);
				margin = (int) (5 * times * sec);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (left) {
				holder.layoutLeft.setPadding(holder.paddingLeft,
						holder.paddingTop, holder.paddingRight + margin,
						holder.paddingBottom);
				msgTV.setBackgroundResource(R.drawable.left_voice_3l);
			} else {
				LayoutParams lp = (LayoutParams) msgTV.getLayoutParams();
				lp.leftMargin = margin;
				msgTV.setLayoutParams(lp);
				msgTV.setBackgroundResource(R.drawable.right_voice_3r);
			}
			timeTV.setText(time + "''");
			String savePath = msgUser.getSavepath();
			rl.setTag(R.id.tag_path, savePath);
		} else if (msgType.equals("image")) {
			String url = msgUser.getFile();
			String thumbUrl = msgUser.getSavepath();
			String fullUrl = "";
			String type = "image-local";
			if (url != null && url.indexOf("__") > -1) {
				fullUrl = url.substring(0, url.lastIndexOf("__"));
				type = "image-network";
			} else {
				fullUrl = thumbUrl;
			}
			if (thumbUrl != null && !thumbUrl.equals("")) {
				try {
					int toHeight = (int) (DeviceInfoUtil.getDeviceCsd(mContext) * 100);
					Bitmap bitmap = ImageRectUtil.thumbImage(thumbUrl, 0,
							toHeight);
					BitmapDrawable bd = new BitmapDrawable(
							mContext.getResources(), bitmap);
					msgTV.setBackgroundDrawable(bd);
					rl.setTag(R.id.tag_type, type);
					rl.setTag(R.id.tag_path, fullUrl);
				} catch (Exception e) {
					e.printStackTrace();
					String mspString = mContext.getString(R.string.error1);
					SpannableString msp = new SpannableString(mspString);
					msp.setSpan(new ForegroundColorSpan(Color.RED), 0,
							mspString.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					msgTV.setText(msp);
					msgTV.setMovementMethod(LinkMovementMethod.getInstance());
				}
			} else {
				String mspString = mContext.getString(R.string.error1);
				SpannableString msp = new SpannableString(mspString);
				msp.setSpan(new ForegroundColorSpan(Color.RED), 0,
						mspString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				msgTV.setText(msp);
				msgTV.setMovementMethod(LinkMovementMethod.getInstance());
			}
		} else if (msgType.equals("emotion")) {
			try {
				int resid = EmotionPopHelper.gentEmotionMap().get(msgContent);
				// GifView gifView = new GifView(mContext);
				// gifView.setGifImage(resid);
				// // gifView.setLoopAnimation();
				// // gifView.setLoopNumber(LOOP_TIMES);
				// gv.addView(gifView);
				GifMovieView gif = new GifMovieView(mContext);
				gif.setMovieResource(resid);
				gv.addView(gif);
			} catch (Exception e) {
				e.printStackTrace();
			}
			rl.setVisibility(View.GONE);
		} else if (msgType.equals("card")) {
			try {
				String[] strs = msgContent.split("____");
				cardname.setText(strs[1]);
				cardcompany.setText(strs[2]);
				cardhead.setImageResource(R.drawable.default_userhead);
				rl.setVisibility(View.GONE);
				cardView.setVisibility(View.VISIBLE);
				if (strs.length > 3) {
					cardhead.setTag(strs[3]);
					mLoadImage.addTask(strs[3], cardhead);
				}
				rl.setTag(R.id.tag_string, strs[0]);
				cardView.setTag(R.id.tag_string, strs[0]);
				cardView.setTag(R.id.tag_type, msgType);
				cardView.setTag(R.id.tag_state, msgUser.getIsread());
				cardView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								UserCardActivity.class);
						intent.putExtra("userid",
								(String) v.getTag(R.id.tag_string));
						mContext.startActivity(intent);
					}
				});
				cardView.setOnLongClickListener(longListener);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		rl.setOnClickListener(clickListener);
		rl.setOnLongClickListener(longListener);
		headIV.setImageResource(R.drawable.default_userhead);
		headIV.setTag(headUrl);
		headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, UserCardActivity.class);
				intent.putExtra("userid", userid);
				mContext.startActivity(intent);
			}
		});
		holder.msgShowTimeTV.setText(CommonUtil.calcTime(msgUser.getAddtime(),
				false, true));
		if (msgUser.isShowtime()) {
			holder.msgShowTimeTV.setVisibility(View.VISIBLE);
		} else {
			holder.msgShowTimeTV.setVisibility(View.GONE);
		}
		mLoadImage.addTask(headUrl, headIV);
		mLoadImage.doTask();
		return convertView;
	}

	private void destoryGif(RelativeLayout gv) {
		if (gv.getChildCount() > 0) {
			gv.removeAllViews();
		}
	}

	static class ViewHolder {
		View relativeLayoutChatLeft;
		View relativeLayoutChatRight;
		RelativeLayout layoutLeft;
		RelativeLayout layoutRight;
		TextView msgShowTimeTV;
		TextView timeTV;
		TextView timeTV2;
		TextView msgTV;
		TextView msgTV2;
		ImageView headIV;
		ImageView headIV2;
		RelativeLayout gv;
		RelativeLayout gv2;
		CheckBox checkBox;
		CheckBox checkBox2;
		ImageView cardhead;
		ImageView cardhead2;
		TextView cardname;
		TextView cardname2;
		TextView cardcompany;
		TextView cardcompany2;
		View layoutCard;
		View layoutCard2;
		View failed;
		View failed2;
		View progressBar;
		View progressBar2;
		int marginLeft = 0;
		int paddingRight = 0;
		int paddingLeft = 0;
		int paddingTop = 0;
		int paddingBottom = 0;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.msgTV = (TextView) convertView
				.findViewById(R.id.textViewMsgContent);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		holder.gv = (RelativeLayout) convertView.findViewById(R.id.gifLeft);
		holder.checkBox = (CheckBox) convertView
				.findViewById(R.id.checkBoxLeft);
		holder.timeTV2 = (TextView) convertView
				.findViewById(R.id.textViewTime2);
		holder.msgTV2 = (TextView) convertView
				.findViewById(R.id.textViewMsgContent2);
		holder.headIV2 = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader2);
		holder.gv2 = (RelativeLayout) convertView.findViewById(R.id.gifRight);
		holder.checkBox2 = (CheckBox) convertView
				.findViewById(R.id.checkBoxRight);
		holder.relativeLayoutChatLeft = convertView
				.findViewById(R.id.relativeLayoutChatLeft);
		holder.relativeLayoutChatRight = convertView
				.findViewById(R.id.relativeLayoutChatRight);
		holder.layoutLeft = (RelativeLayout) convertView
				.findViewById(R.id.relativeLayoutLeft);
		holder.layoutRight = (RelativeLayout) convertView
				.findViewById(R.id.relativeLayoutRight);
		holder.msgShowTimeTV = (TextView) convertView
				.findViewById(R.id.textViewMsgShowTime);
		holder.marginLeft = ((LayoutParams) holder.msgTV.getLayoutParams()).leftMargin;
		holder.paddingRight = holder.layoutLeft.getPaddingRight();
		holder.paddingLeft = holder.layoutLeft.getPaddingLeft();
		holder.paddingTop = holder.layoutLeft.getPaddingTop();
		holder.paddingBottom = holder.layoutLeft.getPaddingBottom();
		holder.cardhead = (ImageView) convertView
				.findViewById(R.id.imageViewCardHead);
		holder.cardhead2 = (ImageView) convertView
				.findViewById(R.id.imageViewCardHead2);
		holder.cardname = (TextView) convertView
				.findViewById(R.id.textViewCardName);
		holder.cardname2 = (TextView) convertView
				.findViewById(R.id.textViewCardName2);
		holder.cardcompany = (TextView) convertView
				.findViewById(R.id.textViewCardCompany);
		holder.cardcompany2 = (TextView) convertView
				.findViewById(R.id.textViewCardCompany2);
		holder.layoutCard = convertView.findViewById(R.id.linearLayoutCard);
		holder.layoutCard2 = convertView.findViewById(R.id.linearLayoutCard2);
		holder.failed = convertView.findViewById(R.id.imageViewFailed);
		holder.failed2 = convertView.findViewById(R.id.imageViewFailed2);
		holder.progressBar = convertView.findViewById(R.id.progressBar);
		holder.progressBar2 = convertView.findViewById(R.id.progressBar2);
		return holder;
	}

	public void startEdit() {
		this.mEditMode = true;
	}

	public void finishEdit() {
		mSelectedId.clear();
		this.mEditMode = false;
	}

	public Set<String> getmSelectedId() {
		return mSelectedId;
	}
}
