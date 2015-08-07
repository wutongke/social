package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.QuanTopicVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.utils.ImageRectUtil;

/**
 * 
 * 
 * @author lz
 * 
 */
public class QuanziTopicListAdapter extends BaseAdapter {
	private List<QuanTopicVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage();
	private Context mContext = null;
	private int width = 720;
	private float times = 2;
	private ZhuoConnHelper mConnHelper = null;
	private PopupWindows phw = null, phwChild;
	String msgid = "11";
	Fragment fragment;
	String groupId;
	BaseCodeData baseDataSet;

	int role;// “我在圈子中的身份”

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public QuanziTopicListAdapter(Fragment fragment,
			ArrayList<QuanTopicVO> list, int role) {
		// 圈子话题的列表
		this.mContext = fragment.getActivity();
		this.fragment = fragment;
		this.mList = list;
		this.inflater = LayoutInflater.from(mContext);
		this.width = DeviceInfoUtil.getDeviceCsw(mContext);
		this.times = DeviceInfoUtil.getDeviceCsd(mContext);
		this.mConnHelper = ZhuoConnHelper.getInstance(mContext);
		this.phw = new PopupWindows((Activity) mContext);
		baseDataSet = mConnHelper.getBaseDataSet();
		this.role = role;
	}

	public QuanziTopicListAdapter(Activity activity,
			ArrayList<QuanTopicVO> list, int role) {
		// 好友动态的列表，fragment为null..与圈话题的内容一致
		this.mContext = activity;
		this.fragment = null;
		this.mList = list;
		this.inflater = LayoutInflater.from(mContext);
		this.width = DeviceInfoUtil.getDeviceCsw(mContext);
		this.times = DeviceInfoUtil.getDeviceCsd(mContext);
		this.mConnHelper = ZhuoConnHelper.getInstance(mContext);
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
		final ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_quanzi_topic_list,
					null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.tag_view_holder);
		}
		QuanTopicVO item = mList.get(position);
		msgid = item.getTopicid();
		final String userId = item.getUserid();
		String authorName = item.getName();
		String headUrl = item.getUheader();
		String work = "";
		if (baseDataSet != null)
			work = ((baseDataSet.getPosition()).get(item.getPosition() - 1))
					.getContent();
		String detail = item.getContent();
		String time = item.getAddtime();
		time = CommonUtil.calcTime(time);
		convertView.setTag(R.id.tag_id, msgid);
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

		holder.headIV.setImageBitmap(ImageRectUtil.toRoundCorner(BitmapFactory
				.decodeResource(mContext.getResources(),
						R.drawable.default_userhead), 10));
		holder.headIV.setTag(headUrl);
		holder.headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ZhuoMaiCardActivity.class);
				intent.putExtra("userid", userId);
				mContext.startActivity(intent);
			}
		});
		mLoadImage.addTask(headUrl, holder.headIV);

		holder.optionIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				if (fragment != null && role == QuanVO.QUAN_ROLE_YOUKE) {
					OnClickListener applyTojoinListener = new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(mContext,
									ApplyToJoinQuanActicvity.class);
							i.putExtra("groupid", msgid);
							if (fragment != null)
								fragment.startActivity(i);
							else
								mContext.startActivity(i);
						}
					};
					OnClickListener noListener = new OnClickListener() {
						@Override
						public void onClick(View v) {
							//
						}
					};
					phw.showPopDlg(view, R.string.label_apply,
							R.string.label_nowno, applyTojoinListener,
							noListener, R.string.title_topic_tip);
				} else {
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
							if (fragment != null)
								fragment.startActivityForResult(i,
										Activity.RESULT_FIRST_USER);
							else
								((Activity) mContext).startActivityForResult(i,
										MsgTagVO.MSG_CMT);
						}
					};
					phw.showOptionsPop(view, times, zanListener, cmtListener);
				}
			}
		});

		holder.tl.removeAllViews();
		final List<PicNewVO> picsinner = item.getTopicPic();
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
				final String url = picsinner.get(i).getPic();
				rl.setTag(url);
				iv.setTag(url);
				iv.setImageResource(R.drawable.default_image);
				mLoadImage.addTask(url, iv);
				rl.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								PhotoViewMultiActivity.class);
						ArrayList<String> orgs = new ArrayList<String>();
						for (int j = 0; j < picsinner.size(); j++) {
							orgs.add(picsinner.get(j).getPic());
						}
						intent.putStringArrayListExtra("pics", orgs);
						intent.putExtra("pic", (String) v.getTag());
						mContext.startActivity(intent);
					}
				});
				tr.addView(rl);
			}
		}
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

	static class ViewHolder {
		TextView nameTV;
		TextView timeTV;
		TextView workTV;
		TextView resTV;
		ImageView resIV;
		ImageView headIV;
		View optionIV;
		TableLayout tl;
		RelativeLayout.LayoutParams rlp;
		RelativeLayout.LayoutParams rlp2;
		TableLayout.LayoutParams tllpoutter;
		TableRow.LayoutParams trlpoutter;
	}

	private ViewHolder initHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.tllpoutter = new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		holder.trlpoutter = new TableRow.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		holder.rlp = new RelativeLayout.LayoutParams((int) (50 * times),
				(int) (50 * times));
		holder.rlp2 = new RelativeLayout.LayoutParams((int) (114 * times),
				(int) (114 * times));
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
		holder.tl = (TableLayout) convertView
				.findViewById(R.id.tableLayoutAuthorPics);
		return holder;
	}
}
