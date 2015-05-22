package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.ChatActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CardListAdapter extends BaseAdapter {
	private List<CardMsgVO> mList = null;
	private String myid = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	// private ZhuoConnHelper mConnHelper = null;
	private Context mContext = null;
	// private CardMsgVO mCard = null;
	private CardMsgFacade mFacade = null;

	public CardListAdapter(Context context, ArrayList<CardMsgVO> list,
			String myid) {
		this.mFacade = new CardMsgFacade(context);
		this.mContext = context;
		// this.mConnHelper = ZhuoConnHelper.getInstance(context);
		this.mList = list;
		this.myid = myid;
		this.inflater = LayoutInflater.from(context);
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
		ViewHolder holder;
		if (convertView == null) {
			convertView = this.inflater.inflate(R.layout.item_card_list, null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}

		final CardMsgVO card = mList.get(position);
		final String id = card.getId();
		convertView.setTag(R.id.tag_id, id);
		UserVO user = card.getSender();
		final String senderid = user.getUserid();
		final String receiverName = card.getReceiver().getUsername();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String work = user.getPost();
		String company = user.getCompany();
		String time = card.getAddtime();
		time = CommonUtil.calcTime(time);
		String text = card.getLeavemsg();
		if (text == null) {
			text = "";
		}
		String isopen = card.getIsopen();
		String state = card.getState();
		String cardState = "0";
		if (senderid.equals(myid)) {
			if (state.equals("send")) {
				cardState = "0";
			} else {
				cardState = "2";
			}
		} else {
			if (state.equals("send")) {
				cardState = "1";
			} else {
				cardState = "3";
			}
		}
		Context context = convertView.getContext();
		if (isopen != null && isopen.equals("1")) {
			holder.textViewOpen.setText(R.string.label_isopen);
			holder.textViewOpen.setVisibility(View.VISIBLE);
		} else {
			holder.textViewOpen.setText("");
			holder.textViewOpen.setVisibility(View.GONE);
		}
		if (cardState.equals("0")) {
			if (!text.equals("")) {
				holder.textViewLabel.setText(R.string.label_mytext);
				holder.textViewMsg.setText(text);
			} else {
				holder.textViewLabelParent.setVisibility(View.GONE);
			}
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewState
					.setText(context.getString(R.string.label_tohe)
							+ receiverName
							+ context.getString(R.string.label_sendcard));
		} else if (cardState.equals("1")) {
			if (!text.equals("")) {
				holder.textViewLabel.setText(R.string.label_hetext);
				holder.textViewMsg.setText(text);
			} else {
				holder.textViewLabelParent.setVisibility(View.GONE);
			}
			holder.relativeLayoutRight.setVisibility(View.VISIBLE);
			holder.textViewState.setText(authorName
					+ context.getString(R.string.label_tomy)
					+ context.getString(R.string.label_sendcard));
			holder.textViewRight.setOnClickListener(getViewCard(senderid));
			// mCard = card;
			// mConnHelper.followUser(senderid, "1", mHandler,
			// MsgTagVO.PUB_INFO, null,true,null);
		} else if (cardState.equals("2")) {
			if (!text.equals("")) {
				holder.textViewLabel.setText(R.string.label_hetext);
				holder.textViewMsg.setText(text);
			} else {
				holder.textViewLabelParent.setVisibility(View.GONE);
			}
			holder.relativeLayoutRight.setVisibility(View.VISIBLE);
			holder.textViewRight.setText(R.string.label_hello);
			holder.textViewState.setText(authorName
					+ context.getString(R.string.label_acceptmycard));
			holder.textViewRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(mContext, ChatActivity.class);
					i.putExtra("userid", senderid);
					mContext.startActivity(i);
				}
			});
		} else if (cardState.equals("3")) {
			if (!text.equals("")) {
				holder.textViewLabel.setText(R.string.label_mytext);
				holder.textViewMsg.setText(text);
			} else {
				holder.textViewLabelParent.setVisibility(View.GONE);
			}
			holder.relativeLayoutRight.setVisibility(View.VISIBLE);
			holder.textViewRight.setText(R.string.label_hello);
			holder.textViewState.setText(context
					.getString(R.string.label_accepthecard)
					+ authorName
					+ context.getString(R.string.label_acceptcard));
			holder.textViewRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(mContext, ChatActivity.class);
					i.putExtra("userid", senderid);
					i.putExtra("card", true);
					mContext.startActivity(i);
				}
			});
		}
		holder.nameTV.setText(authorName);
		holder.nameTV.setOnClickListener(getViewCard(senderid));
		holder.timeTV.setText(time);
		String companyWork = ZhuoCommHelper.concatStringWithTag(work, company,
				"|");
		holder.contentTV.setText(companyWork);
		if (!companyWork.equals("")) {
			holder.contentTV.setVisibility(View.VISIBLE);
		} else {
			holder.contentTV.setVisibility(View.GONE);
		}
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		holder.headIV.setOnClickListener(getViewCard(senderid));
		holder.closeIV.setTag(id);
		holder.closeIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				delCardMsg(card);
			}
		});
		return convertView;
	}

	private OnClickListener getViewCard(final String senderid) {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(mContext, UserCardActivity.class);
				i.putExtra("userid", senderid);
				mContext.startActivity(i);
			}
		};
	}

	private void delCardMsg(CardMsgVO card) {
		mFacade.delete(card.getId());
		mList.remove(card);
		notifyDataSetChanged();
	}

	// @SuppressLint("HandlerLeak")
	// private Handler mHandler = new Handler() {
	// @Override
	// public void handleMessage(Message msg) {
	// switch (msg.what) {
	// case MsgTagVO.PUB_INFO: {
	// String data = (String) msg.obj;
	// if (JsonHandler.parseResult(data).getCode().equals("30015")
	// || JsonHandler.checkResult(data, mContext)) {
	// CommonUtil.displayToast(mContext,
	// R.string.label_followsuccess);
	// mCard.setState("3");
	// mFacade.saveOrUpdate(mCard);
	// notifyDataSetChanged();
	// }
	// break;
	// }
	// }
	// }
	// };

	static class ViewHolder {
		TextView textViewOpen;
		TextView textViewLabel;
		TextView textViewMsg;
		TextView textViewRight;
		TextView textViewState;
		TextView nameTV;
		TextView timeTV;
		TextView contentTV;
		View textViewLabelParent;
		View relativeLayoutRight;
		ImageView closeIV;
		ImageView headIV;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.textViewOpen = (TextView) convertView
				.findViewById(R.id.textViewOpen);
		holder.textViewLabel = (TextView) convertView
				.findViewById(R.id.textViewLabel);
		holder.textViewMsg = (TextView) convertView
				.findViewById(R.id.textViewMsg);
		holder.textViewState = (TextView) convertView
				.findViewById(R.id.textViewState);
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.contentTV = (TextView) convertView
				.findViewById(R.id.textViewWork);
		holder.textViewRight = (TextView) convertView
				.findViewById(R.id.textViewRight);
		holder.textViewLabelParent = (View) convertView.findViewById(
				R.id.textViewLabel).getParent();
		holder.relativeLayoutRight = convertView
				.findViewById(R.id.relativeLayoutRight);
		holder.closeIV = (ImageView) convertView
				.findViewById(R.id.imageViewClose);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		return holder;
	}
}
