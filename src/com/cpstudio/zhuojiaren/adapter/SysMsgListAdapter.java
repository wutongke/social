package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.facade.SysMsgFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.SysMsgVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.QuanDetailActivity;
import com.cpstudio.zhuojiaren.UserCardActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SysMsgListAdapter extends BaseAdapter {
	private List<SysMsgVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private ZhuoConnHelper mConnHelper = null;
	private SysMsgFacade sysMsgFacade = null;
	private Context mContext = null;

	// private String acceptApplyId = null;
	// private String refuseApplyId = null;
	// private String acceptInventId = null;
	// private String acceptRcmdId = null;

	public SysMsgListAdapter(Context context, ArrayList<SysMsgVO> list) {
		this.mList = list;
		this.mContext = context;
		this.sysMsgFacade = new SysMsgFacade(context);
		this.inflater = LayoutInflater.from(context);
		this.mConnHelper = ZhuoConnHelper.getInstance(context);
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
			convertView = this.inflater
					.inflate(R.layout.item_sysmsg_list, null);
			holder = new ViewHolder();
			holder.textViewStart = (TextView) convertView
					.findViewById(R.id.textViewStart);
			holder.textViewMiddle = (TextView) convertView
					.findViewById(R.id.textViewMiddle);
			holder.textViewLeft = (TextView) convertView
					.findViewById(R.id.textViewLeft);
			holder.textViewRight = (TextView) convertView
					.findViewById(R.id.textViewRight);
			holder.textViewEnd = (TextView) convertView
					.findViewById(R.id.textViewEnd);
			holder.nameTV = (TextView) convertView
					.findViewById(R.id.textViewAuthorName);
			holder.timeTV = (TextView) convertView
					.findViewById(R.id.textViewTime);
			holder.relativeLayoutLeft = convertView
					.findViewById(R.id.relativeLayoutLeft);
			holder.relativeLayoutRight = convertView
					.findViewById(R.id.relativeLayoutRight);
			holder.headIV = (ImageView) convertView
					.findViewById(R.id.imageViewAuthorHeader);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}

		final SysMsgVO msg = mList.get(position);
		final String id = msg.getId();
		convertView.setTag(R.id.tag_id, id);
		UserVO user = msg.getSender();
		final String uid = user.getUserid();
		String authorName = user.getUsername();
		String headUrl = user.getUheader();
		String time = msg.getAddtime();
		time = CommonUtil.calcTime(time);
		String groupname = msg.getGroupname();
		final String groupid = msg.getGroupid();
		final String state = msg.getType();
		UserVO user2 = msg.getUser();
		String uid2 = "";
		String uname2 = "";
		if (user2 != null) {
			uid2 = user2.getUserid();
			uname2 = user2.getUsername();
		}
		final String user2Str = uid2;
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent i = new Intent(mContext, QuanDetailActivity.class);
				i.putExtra("groupid", groupid);
				mContext.startActivity(i);
			}
		};
		OnClickListener lc = new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (state.equals("2")) {
					if (mConnHelper.acceptUser(mHandler, MsgTagVO.ACCEPT_APPLY,
							null, groupid, uid, "0", true, null, id)) {
						// acceptApplyId = id;
					}
				} else if (state.equals("5")) {
					if (mConnHelper.followGroup(groupid, "1", mHandler,
							MsgTagVO.ACCEPT_INVENT, null, true, null, id)) {
						// acceptInventId = id;
					}
				} else if (state.equals("6")) {
					if (mConnHelper.followUser(user2Str, "1", mHandler,
							MsgTagVO.ACCEPT_RCMD, null, true, null, id)) {
						// acceptRcmdId = id;
					}
				}
			}
		};
		OnClickListener lr = new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				if (state.equals("2")) {
					if (mConnHelper.acceptUser(mHandler, MsgTagVO.REFUSE_APPLY,
							null, groupid, uid, "1", true, null, id)) {
						// refuseApplyId = id;
					}
				} else if (state.equals("5")) {
					SysMsgVO item = sysMsgFacade.getById(id);
					item.setType("101");
					sysMsgFacade.saveOrUpdate(item);
					for (SysMsgVO sysmsg : mList) {
						if (sysmsg.getId().equals(id)) {
							sysmsg.setType("101");
							break;
						}
					}
					notifyDataSetChanged();
				}
			}
		};
		if (state.equals("0")) {// AcceptCreateGroup
			holder.relativeLayoutLeft.setVisibility(View.VISIBLE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_sysacceptcreate));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joinreq));
			holder.textViewLeft.setText(R.string.label_joingroup);
			holder.relativeLayoutLeft.setOnClickListener(listener);
		} else if (state.equals("1")) {// RefuseCreateGroup
			holder.relativeLayoutLeft.setVisibility(View.GONE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_sysrefusecreate));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joinreq));
		} else if (state.equals("2")) {// ApplyJoinGroup2
			holder.relativeLayoutLeft.setVisibility(View.VISIBLE);
			holder.relativeLayoutRight.setVisibility(View.VISIBLE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_applyjoin));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext.getString(R.string.label_quan));
			holder.textViewLeft.setText(R.string.label_accept);
			holder.textViewRight.setText(R.string.label_refuse);
			holder.relativeLayoutLeft.setOnClickListener(lc);
			holder.relativeLayoutRight.setOnClickListener(lr);
		} else if (state.equals("3")) {// RefuseJoinGroup3
			holder.relativeLayoutLeft.setVisibility(View.GONE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_sysrefusejoin));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joinreq));
		} else if (state.equals("4")) {// AcceptJoinGroup4
			holder.relativeLayoutLeft.setVisibility(View.VISIBLE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_sysacceptjoin));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joinreq));
			holder.textViewLeft.setText(R.string.label_joingroup);
			holder.relativeLayoutLeft.setOnClickListener(listener);
		} else if (state.equals("5")) {// InviteJoinGroup5
			holder.relativeLayoutLeft.setVisibility(View.VISIBLE);
			holder.relativeLayoutRight.setVisibility(View.VISIBLE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_invenjoin));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext.getString(R.string.label_quan));
			holder.textViewLeft.setText(R.string.label_accept);
			holder.textViewRight.setText(R.string.label_refuse);
			holder.relativeLayoutLeft.setOnClickListener(lc);
			holder.relativeLayoutRight.setOnClickListener(lr);
		} else if (state.equals("6")) {// RcmdUser
			holder.relativeLayoutLeft.setVisibility(View.VISIBLE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_rcmd));
			holder.textViewMiddle.setText(uname2);
			holder.textViewEnd.setText("");
			holder.textViewLeft.setText(R.string.label_follow);
			holder.relativeLayoutLeft.setOnClickListener(lc);
		} else if (state.equals("100")) {// 接收邀请
			holder.relativeLayoutLeft.setVisibility(View.GONE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_acceptjoin)
					+ mContext.getString(R.string.label_join));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joininven));
		} else if (state.equals("101")) {// 拒绝邀请
			holder.relativeLayoutLeft.setVisibility(View.GONE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_refusejoin)
					+ mContext.getString(R.string.label_join));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joininven));
		} else if (state.equals("102")) {// 接收请求
			holder.relativeLayoutLeft.setVisibility(View.GONE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_acceptjoin)
					+ authorName
					+ mContext.getString(R.string.label_join));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joinreq));
		} else if (state.equals("103")) {// 拒绝请求
			holder.relativeLayoutLeft.setVisibility(View.GONE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_refusejoin)
					+ authorName
					+ mContext.getString(R.string.label_join));
			holder.textViewMiddle.setText(groupname);
			holder.textViewEnd.setText(mContext
					.getString(R.string.label_joinreq));
		} else if (state.equals("104")) {// 关注
			holder.relativeLayoutLeft.setVisibility(View.GONE);
			holder.relativeLayoutRight.setVisibility(View.GONE);
			holder.textViewStart.setText(mContext
					.getString(R.string.label_followed));
			holder.textViewMiddle.setText(msg.getUser().getUsername());
			holder.textViewEnd.setText("");
		}
		holder.nameTV.setText(authorName);
		holder.timeTV.setText(time);
		holder.headIV.setImageResource(R.drawable.default_userhead);
		holder.headIV.setTag(headUrl);
		holder.headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View paramView) {
				Intent i = new Intent(mContext, UserCardActivity.class);
				i.putExtra("userid", uid);
				mContext.startActivity(i);
			}
		});
		mLoadImage.addTask(headUrl, holder.headIV);
		mLoadImage.doTask();
		return convertView;
	}

	static class ViewHolder {
		TextView textViewLeft;
		TextView textViewRight;
		TextView textViewStart;
		TextView textViewMiddle;
		TextView textViewEnd;
		TextView nameTV;
		TextView timeTV;
		ImageView headIV;
		View relativeLayoutLeft;
		View relativeLayoutRight;
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.ACCEPT_APPLY: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					try {
						Bundle bundle = msg.getData();
						String id = bundle.getString("data");
						SysMsgVO item = sysMsgFacade.getById(id);
						item.setType("102");
						sysMsgFacade.saveOrUpdate(item);
						for (SysMsgVO sysmsg : mList) {
							if (sysmsg.getId().equals(id)) {
								sysmsg.setType("102");
								break;
							}
						}
						notifyDataSetChanged();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case MsgTagVO.REFUSE_APPLY: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					try {
						Bundle bundle = msg.getData();
						String id = bundle.getString("data");
						SysMsgVO item = sysMsgFacade.getById(id);
						item.setType("103");
						sysMsgFacade.saveOrUpdate(item);
						for (SysMsgVO sysmsg : mList) {
							if (sysmsg.getId().equals(id)) {
								sysmsg.setType("103");
								break;
							}
						}
						notifyDataSetChanged();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case MsgTagVO.ACCEPT_INVENT: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					try {
						Bundle bundle = msg.getData();
						String id = bundle.getString("data");
						SysMsgVO item = sysMsgFacade.getById(id);
						item.setType("100");
						sysMsgFacade.saveOrUpdate(item);
						for (SysMsgVO sysmsg : mList) {
							if (sysmsg.getId().equals(id)) {
								sysmsg.setType("100");
								break;
							}
						}
						notifyDataSetChanged();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				break;
			}
			case MsgTagVO.ACCEPT_RCMD: {
				String code = JsonHandler.parseResult((String) msg.obj)
						.getCode();
				if (code.equals("30015") || code.equals("10000")) {
					try {
						Bundle bundle = msg.getData();
						String id = bundle.getString("data");
						SysMsgVO item = sysMsgFacade.getById(id);
						item.setType("104");
						sysMsgFacade.saveOrUpdate(item);
						for (SysMsgVO sysmsg : mList) {
							if (sysmsg.getId().equals(id)) {
								sysmsg.setType("104");
								break;
							}
						}
						notifyDataSetChanged();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					JsonHandler.checkResult((String) msg.obj, mContext);
				}
				break;
			}
			}
		}
	};
}
