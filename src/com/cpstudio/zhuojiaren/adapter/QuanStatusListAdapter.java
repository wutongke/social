package com.cpstudio.zhuojiaren.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.GroupStatus;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.ui.EventDetailActivity;
import com.cpstudio.zhuojiaren.ui.MsgCmtActivity;
import com.cpstudio.zhuojiaren.ui.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.ui.ZhuoMaiCardActivity;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.ViewHolder;
import com.utils.ImageRectUtil;

/**
 * 圈子动态Adapter,动态内容有话题和活动两种。包括发布的动态和活动 布局可以综合已有的ZhuoQuanMainActivity中的两种布局
 * 
 * @author lz
 * 
 */
public class QuanStatusListAdapter extends BaseAdapter {
	private List<GroupStatus> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = LoadImage.getInstance();
	private Context mContext = null;
	private float times = 2;
	private ConnHelper mConnHelper = null;
	private PopupWindows phw = null;
	String msgid = "11";
	String groupId;
	BaseCodeData baseDataSet;
	boolean isManaging = false;
	int role;// “我在圈子中的身份”

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return mList.get(position).getType();
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	public QuanStatusListAdapter(Activity activity,
			ArrayList<GroupStatus> list, int role) {
		// 好友动态的列表，fragment为null..与圈话题的内容一致
		this.mContext = activity;
		this.mList = list;
		this.inflater = LayoutInflater.from(mContext);
		this.times = DeviceInfoUtil.getDeviceCsd(mContext);
		this.mConnHelper = ConnHelper.getInstance(mContext);
		this.phw = new PopupWindows((Activity) mContext);
		this.role = role;
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
		ViewHolderStatus holder = null;// 圈话题
		ViewHolderActive holderActive = null;// 圈活动
		int type = getItemViewType(position);
		if (convertView == null) {
			if (type == 0) {
				convertView = inflater.inflate(R.layout.item_quanzi_topic_list,
						null);
				holder = initHolder(convertView);
				convertView.setTag(R.id.tag_view_holder, holder);
			} else {
				convertView = inflater.inflate(R.layout.item_event_list, null);
				holderActive = initHolderActive(convertView);
				convertView.setTag(R.id.tag_view_holder, holderActive);
			}

		} else {
			if (type == 0)
				holder = (ViewHolderStatus) convertView
						.getTag(R.id.tag_view_holder);
			else
				holderActive = (ViewHolderActive) convertView
						.getTag(R.id.tag_view_holder);
		}
		if (type == 0 && mList.get(position).getGroupTopic() != null) {
			QuanTopicVO item = mList.get(position).getGroupTopic();
			msgid = item.getTopicid();
			final String userId = item.getUserid();
			String authorName = item.getName();
			String headUrl = item.getUheader();
			String work = "";
			if (baseDataSet != null) {
				int pos = item.getPosition();
				if (pos != 0)
					pos--;
				work = ((baseDataSet.getPosition()).get(pos)).getContent();
			}
			String detail = item.getContent();
			String time = item.getAddtime();
			time = CommonUtil.calcTime(time);
			convertView.setTag(R.id.tag_id, msgid);
			if (item.getGname() != null)
				holder.nameTV.setText(item.getGname());
			else
				holder.nameTV.setText(authorName);
			holder.timeTV.setText(time);
			if (work != null) {
				holder.workTV.setText(work);
				holder.workTV.setVisibility(View.VISIBLE);
			} else {
				holder.workTV.setText("");
				holder.workTV.setVisibility(View.GONE);
			}

			holder.resTV.setText(detail.trim());

			holder.headIV.setImageBitmap(ImageRectUtil.toRoundCorner(
					BitmapFactory.decodeResource(mContext.getResources(),
							R.drawable.default_userhead), 10));
			holder.headIV.setTag(headUrl);
			holder.headIV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,
							ZhuoMaiCardActivity.class);
					intent.putExtra("userid", userId);
					mContext.startActivity(intent);
				}
			});
			mLoadImage.addTask(headUrl, holder.headIV);

			holder.optionIV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {

					OnClickListener zanListener = new OnClickListener() {

						@Override
						public void onClick(View v) {
							mConnHelper.praiseTopic(mHandler,
									MsgTagVO.MSG_LIKE, msgid, 1);
						}
					};
					OnClickListener cmtListener = new OnClickListener() {

						@Override
						public void onClick(View v) {

							Intent i = new Intent(mContext,
									MsgCmtActivity.class);
							i.putExtra("msgid", msgid);
							i.putExtra("parentid", msgid);
							i.putExtra("type", 1);
							((Activity) mContext).startActivityForResult(i,
									MsgTagVO.MSG_CMT);
						}
					};
					phw.showOptionsPop(view, times, zanListener, cmtListener);

				}
			});

			final List<PicNewVO> picsinner = item.getTopicPic();
			holder.gvImages.setVisibility(View.GONE);
			// 显示图片
			if (picsinner != null && picsinner.size() > 0) {
				holder.gvImages.setVisibility(View.VISIBLE);
				ArrayList<String> urls = new ArrayList<String>();
				for (PicNewVO temp : picsinner) {
					urls.add(temp.getPic());
				}
				holder.gvImages.setAdapter(new GridViewAdapter(mContext, urls,
						R.layout.item_gridview_image));
			}
			mLoadImage.doTask();
		} else if (mList.get(position).getGroupActivity() != null) {
			final EventVO event = mList.get(position).getGroupActivity();
			msgid = event.getActivityid();
			if (event.getGname() != null) {
				holderActive.tvQuanName.setVisibility(View.VISIBLE);
				holderActive.tvQuanName.setText(event.getGname());
			} else
				holderActive.tvQuanName.setVisibility(View.GONE);
			holderActive.textViewTitle.setText(event.getTitle());
			holderActive.textViewDateTime.setText(event.getStarttime());
			if (event.getOutdate() == 1)
				holderActive.textViewIsOverTime.setText("已过期");
			else
				holderActive.textViewIsOverTime.setText("未过期");
			holderActive.textViewNums.setText(event.getJoinCount() + "人报名");
			convertView.setTag(R.id.tag_id, msgid);
			holderActive.textViewPlace.setText(event.getAddress());
			holderActive.textViewDetail
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent i = new Intent(mContext,
									EventDetailActivity.class);
							String id = "111";
							if (event.getActivityid() != null)
								id = event.getActivityid();
							i.putExtra("eventId", id);

							mContext.startActivity(i);
						}
					});
			if (isManaging)
				holderActive.cbSelected.setVisibility(View.VISIBLE);
			else
				holderActive.cbSelected.setVisibility(View.GONE);
			holderActive.cbSelected
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub
							event.setSelected(arg1);
						}
					});
		}
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
				}
				break;
			}
			case MsgTagVO.MSG_DEL: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					CommonUtil.displayToast(mContext, R.string.info12);
				}
			}
			}
		}
	};

	static class ViewHolderStatus {
		TextView nameTV;
		TextView timeTV;
		TextView workTV;
		TextView resTV;
		ImageView resIV;
		ImageView headIV;
		View optionIV;
		MyGridView gvImages;
	}

	static class ViewHolderActive {
		TextView textViewTitle;
		TextView textViewDateTime;

		TextView textViewPlace;
		TextView textViewIsOverTime;
		TextView textViewNums;
		TextView textViewDetail;
		CheckBox cbSelected;
		TextView tvQuanName;
	}

	private ViewHolderActive initHolderActive(View convertView) {

		ViewHolderActive holder = new ViewHolderActive();

		holder.textViewTitle = (TextView) convertView
				.findViewById(R.id.tvActiveTitle);

		holder.textViewDateTime = (TextView) convertView
				.findViewById(R.id.textViewDateTime);
		holder.textViewPlace = (TextView) convertView
				.findViewById(R.id.textViewPlace);
		holder.textViewIsOverTime = (TextView) convertView
				.findViewById(R.id.textViewIsOverTime);
		holder.textViewNums = (TextView) convertView
				.findViewById(R.id.textViewNums);
		holder.textViewDetail = (TextView) convertView
				.findViewById(R.id.textViewDetail);
		holder.tvQuanName = (TextView) convertView
				.findViewById(R.id.tvQuanName);

		holder.cbSelected = (CheckBox) convertView.findViewById(R.id.cbChecked);

		return holder;
	}

	private ViewHolderStatus initHolder(View convertView) {
		ViewHolderStatus holder = new ViewHolderStatus();
		holder.nameTV = (TextView) convertView
				.findViewById(R.id.textViewAuthorName);
		holder.timeTV = (TextView) convertView.findViewById(R.id.textViewTime);
		holder.workTV = (TextView) convertView
				.findViewById(R.id.textViewContent);
		holder.resTV = (TextView) convertView.findViewById(R.id.textViewRes);
		holder.resIV = (ImageView) convertView.findViewById(R.id.imageViewRes);
		holder.headIV = (ImageView) convertView
				.findViewById(R.id.imageViewAuthorHeader);
		holder.optionIV = convertView.findViewById(R.id.optionButton);
		holder.gvImages = (MyGridView) convertView
				.findViewById(R.id.picGridView);
		return holder;
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
