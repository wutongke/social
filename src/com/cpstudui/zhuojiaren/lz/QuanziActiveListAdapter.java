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
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.utils.ImageRectUtil;

/**
 * 圈子动态Adapter,包括发布的动态和活动 布局可以综合已有的ZhuoQuanMainActivity中的两种布局
 * 
 * @author lz
 */
public class QuanziActiveListAdapter extends BaseAdapter {
	private List<ZhuoInfoVO> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage = new LoadImage(10);
	private Context mContext = null;
	private int width = 720;
	private float times = 2;
	private ZhuoConnHelper mConnHelper = null;
	private PopupWindows phw = null, phwChild;
	String msgid = "11";
	String groupId;

	// 两种布局类型
	int typeCount = 2;
	boolean isFollow = false;// 标志个人是否属于该圈子

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public QuanziActiveListAdapter(Activity activity, ArrayList<ZhuoInfoVO> list) {
		this.mContext = activity;
		this.mList = list;
		this.inflater = LayoutInflater.from(mContext);
		this.width = DeviceInfoUtil.getDeviceCsw(mContext);
		this.times = DeviceInfoUtil.getDeviceCsd(mContext);
		this.mConnHelper = ZhuoConnHelper.getInstance(mContext);
		this.phw = new PopupWindows((Activity) mContext);
	}

	public void setIsFollow(boolean flag) {
		isFollow = flag;
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
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
//测试用
		return position%2;
		
//		String typeStr = mList.get(position).getType();
//		if (typeStr.equals("1"))
//			return 1;
//		else
//			return 0;
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return typeCount;
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
		ViewHolderTopic holder = null;
		ViewHolderActive holderActive = null;
		int itemType = getItemViewType(position);
		if (convertView == null) {
			if (0 == itemType)
			{
				convertView = inflater.inflate(R.layout.item_quanzi_topic_list, null);
				holder = initHolderTopic(convertView);
				convertView.setTag(R.id.tag_view_holder, holder);
			}
			else
			{
				convertView = inflater.inflate(R.layout.item_event_list, null);
				holderActive = initHolderActive(convertView);
				convertView.setTag(R.id.tag_view_holder, holderActive);
			}
		} else {
			if (0 == itemType)
				holder = (ViewHolderTopic) convertView
						.getTag(R.id.tag_view_holder);
			else
				holderActive = (ViewHolderActive) convertView
						.getTag(R.id.tag_view_holder);
		}


		if (0 == itemType) {
			ZhuoInfoVO item = mList.get(position);
			msgid = item.getMsgid();
			UserVO user = item.getUser();
			final String userid = user.getUserid();
			String company = user.getCompany();
			String authorName = user.getUsername();
			String headUrl = user.getUheader();
			String work = user.getPost();
			String type = item.getType();
			String category = item.getCategory();
			String title = item.getTitle();
			String detail = item.getText();
			String time = item.getAddtime();
			time = CommonUtil.calcTime(time);
			convertView.setTag(R.id.tag_id, item.getMsgid());
			holder.nameTV.setText(authorName);
			holder.timeTV.setText(time);
			String woco = ZhuoCommHelper
					.concatStringWithTag(work, company, "|");
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
				holder.resTV.setText(text.trim());
				Rect bounds = new Rect();
				TextPaint paint = holder.resTV.getPaint();
				paint.getTextBounds(text, 0, text.length(), bounds);
				// int width = bounds.width();
				// if (width / (this.width - 85 * times) > 4) {
				// holder.moreTV.setVisibility(View.VISIBLE);
				// holder.moreTV.setOnClickListener(new OnClickListener() {
				//
				// @Override
				// public void onClick(View view) {
				// TextView showTypeView = (TextView) view;
				// if (showTypeView.getText().equals(
				// mContext.getString(R.string.info2))) {
				// showTypeView.setText(R.string.info1);
				// holder.resTV.setMaxLines(4);
				// } else {
				//
				// showTypeView.setText(R.string.info2);
				// holder.resTV.setMaxLines(200);
				// }
				// }
				// });
				// } else {
				// holder.moreTV.setVisibility(View.GONE);
				// }
			} else {
				holder.resIV.setImageResource(0);
				holder.resTV.setText("");
			}
			holder.headIV.setImageBitmap(ImageRectUtil.toRoundCorner(
					BitmapFactory.decodeResource(mContext.getResources(),
							R.drawable.default_userhead), 10));
			holder.headIV.setTag(headUrl);
			holder.headIV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,
							ZhuoMaiCardActivity.class);
					intent.putExtra("userid", userid);
					mContext.startActivity(intent);
				}
			});
			mLoadImage.addTask(headUrl, holder.headIV);
			mLoadImage.doTask();
			// 濞夈劍鍓伴崢鐔告降閻楀牊婀版稉顓犳畱閳ユ粍妞块崝銊拷閸掓銆冩稊鐔惰厬閻拷+"閺堝琚辨稉顏庣礉閸掑棗鍩嗘禒锝堛�鐎电娴嗛崣鎴濆敶鐎瑰湱娈戞径鍕倞閿涘牏鍋ｇ挧鐐叉嫲鐠囧嫯顔戦敍澶涚礉娴犮儱寮风�瑙勬拱濞戝牊浼呴惃鍕槱閻烇拷
			holder.optionIV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					// if (fragment != null && !isFollow) {
					if (!isFollow) {
						OnClickListener applyTojoinListener = new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent i = new Intent(mContext,
										ApplyToJoinQuanActicvity.class);
								i.putExtra("groupid", msgid);
								// if (fragment != null)
								// fragment.startActivity(i);
								// else
								mContext.startActivity(i);
								// mConnHelper.goodMsg(msgid, mHandler,
								// MsgTagVO.MSG_LIKE,
								// null, true, null, msgid);
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
								mConnHelper.goodMsg(msgid, mHandler,
										MsgTagVO.MSG_LIKE, null, true, null,
										msgid);
							}
						};
						OnClickListener cmtListener = new OnClickListener() {

							@Override
							public void onClick(View v) {

								Intent i = new Intent(mContext,
										MsgCmtActivity.class);
								i.putExtra("msgid", msgid);
								i.putExtra("parentid", msgid);
								// ((Activity)
								// mContext).startActivityForResult(i,MsgTagVO.MSG_CMT);
								// if (fragment != null)
								// fragment.startActivityForResult(i,
								// Activity.RESULT_FIRST_USER);
								// else
								((Activity) mContext).startActivityForResult(i,
										MsgTagVO.MSG_CMT);

							}
						};
						phw.showOptionsPop(view, times, zanListener,
								cmtListener);
					}
				}
			});

			holder.tl.removeAllViews();
			final List<PicVO> picsinner = item.getPic();
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
		} else {
			// 填充活动数据
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
					// Bundle bundle = msg.getData();
					// String id = bundle.getString("data");
					// for (ZhuoInfoVO item : mList) {
					// if (id != null) {
					// if (id.equals(item.getMsgid())) {
					// item.setGoodnum((Integer.valueOf(item
					// .getGoodnum()) + 1) + "");
					// } else if (item.getOrigin() != null
					// && id.equals(item.getOrigin().getMsgid())) {
					// item.getOrigin().setGoodnum(
					// (Integer.valueOf(item.getOrigin()
					// .getGoodnum()) + 1) + "");
					// }
					// }
					// }
					// notifyDataSetChanged();
				}
				break;
			}
			case MsgTagVO.MSG_DEL: {
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					CommonUtil.displayToast(mContext, R.string.info12);
					// Bundle bundle = msg.getData();
					// String id = bundle.getString("data");
					// for (ZhuoInfoVO item : mList) {
					// if (id != null && id.equals(item.getMsgid())) {
					// mList.remove(item);
					// break;
					// }
					// }
					// notifyDataSetChanged();
				}
			}
			}
		}
	};

	static class ViewHolderTopic {
		TextView nameTV;
		TextView timeTV;
		TextView workTV;
		TextView resTV;
		ImageView resIV;
		ImageView headIV;
		// TextView moreTV;
		View optionIV;
		TableLayout tl;
		RelativeLayout.LayoutParams rlp;
		RelativeLayout.LayoutParams rlp2;

		TableLayout.LayoutParams tllpoutter;
		TableRow.LayoutParams trlpoutter;
	}

	static class ViewHolderActive {
		TextView textViewTitle;
		TextView textViewDateTime;

		TextView textViewPlace;
		TextView textViewIsOverTime;
		TextView textViewNums;
		TextView textViewDetail;
	}

	private ViewHolderTopic initHolderTopic(View convertView) {
		
		ViewHolderTopic holder = new ViewHolderTopic();
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
		// holder.moreTV = (TextView) convertView
		// .findViewById(R.id.textViewViewMore);
		holder.tl = (TableLayout) convertView
				.findViewById(R.id.tableLayoutAuthorPics);
		
		return holder;
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
		
		return holder;
	}
}
