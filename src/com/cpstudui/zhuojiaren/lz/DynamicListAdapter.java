package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.MsgCmtActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.Dynamic;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.MyGridView;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

/**
 * 
 * 
 * @author lz
 * 
 */
public class DynamicListAdapter extends BaseAdapter {
	private List<Dynamic> mList = null;
	private LayoutInflater inflater = null;
	private LoadImage mLoadImage;
	private Context mContext = null;
	private int width = 720;
	private float times = 2;
	private ZhuoConnHelper mConnHelper = null;
	private PopupWindows phw = null, phwChild;
	String msgid = "11";
	String groupId;
	BaseCodeData baseDataSet;
	// 娌＄敤
	int role;// 鈥滄垜鍦ㄥ湀瀛愪腑鐨勮韩浠解�

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public DynamicListAdapter(Activity activity, ArrayList<Dynamic> list,
			int role) {
		// 濂藉弸鍔ㄦ�鐨勫垪琛紝fragment涓簄ull..涓庡湀璇濋鐨勫唴瀹逛竴鑷�
		mLoadImage = new LoadImage(0, 100, 100);
		this.mContext = activity;
		this.mList = list;
		this.inflater = LayoutInflater.from(mContext);
		this.width = DeviceInfoUtil.getDeviceCsw(mContext);
		this.times = DeviceInfoUtil.getDeviceCsd(mContext);
		this.mConnHelper = ZhuoConnHelper.getInstance(mContext);
		this.phw = new PopupWindows((Activity) mContext);
		this.role = role;
		baseDataSet = mConnHelper.getBaseDataSet();
	}

	public DynamicListAdapter(Activity activity, LoadImage imageLoad,
			ArrayList<Dynamic> list, int role) {
		// 濂藉弸鍔ㄦ�鐨勫垪琛紝fragment涓簄ull..涓庡湀璇濋鐨勫唴瀹逛竴鑷�
		this.mLoadImage = imageLoad;
		this.mContext = activity;
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
		final ViewHolderDynamic holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_quanzi_topic_list,
					null);
			holder = initHolder(convertView);
			convertView.setTag(R.id.tag_view_holder, holder);
		} else {
			holder = (ViewHolderDynamic) convertView
					.getTag(R.id.tag_view_holder);
		}
		Dynamic item = mList.get(position);
		msgid = item.getStatusid();

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
		// 鐢ㄤ簬activity鐐瑰嚮鏃惰幏寰梚d
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

		// holder.headIV.setImageBitmap(ImageRectUtil.toRoundCorner(BitmapFactory
		// .decodeResource(mContext.getResources(),
		// R.drawable.default_userhead), 10));

		// if (holder.headIV.getTag()!=null &&
		// !holder.headIV.getTag().equals(headUrl)) {
		holder.headIV.setTag(headUrl);
		mLoadImage.addTask(headUrl, holder.headIV);
		holder.headIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, ZhuoMaiCardActivity.class);
				intent.putExtra("userid", userId);
				mContext.startActivity(intent);
			}
		});
		// }
		// IMAGE_SD_CACHE.get(headUrl, holder.headIV);
		holder.optionIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

//				OnClickListener zanListener = new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						mConnHelper.praiseDynamic(mHandler, MsgTagVO.MSG_LIKE,
//								msgid, 1);
//					}
//				};
//				OnClickListener cmtListener = new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//
//						Intent i = new Intent(mContext, MsgCmtActivity.class);
//						i.putExtra("msgid", msgid);
//						i.putExtra("parentid", msgid);
//						i.putExtra("type", 2);
//						// ((Activity)
//						// mContext).startActivityForResult(i,MsgTagVO.MSG_CMT);
//						((Activity) mContext).startActivityForResult(i,
//								MsgTagVO.MSG_CMT);
//					}
//				};
//				phw.showOptionsPop(view, times, zanListener, cmtListener);
			}
		});

		final List<PicNewVO> picsinner = item.getStatusPic();
		holder.gvImages.setVisibility(View.GONE);
		// 鏄剧ず鍥剧墖
		if (picsinner != null && picsinner.size() > 0) {

			ArrayList<String> urls = new ArrayList<String>();
			for (PicNewVO temp : picsinner) {
				if (temp.getPic() != null && !"".equals(temp.getPic().trim()))
					urls.add(temp.getPic());
			}
			if (urls.size() > 0)
				holder.gvImages.setVisibility(View.VISIBLE);
			holder.gvImages.setAdapter(new GridViewAdapter(mContext, urls,
					R.layout.item_gridview_image));
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

	static class ViewHolderDynamic {
		TextView nameTV;
		TextView timeTV;
		TextView workTV;
		TextView resTV;
		ImageView resIV;
		ImageView headIV;
		// TextView moreTV;
		View optionIV;
		MyGridView gvImages;
	}

	private ViewHolderDynamic initHolder(View convertView) {
		ViewHolderDynamic holder = new ViewHolderDynamic();

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

	// 澶氬紶鍥剧墖
	class GridViewAdapter extends CommonAdapter<String> {

		public GridViewAdapter(Context context, List<String> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void convert(ViewHolder helper, String item) {
			// TODO Auto-generated method stub
			// helper.setImageResource(R.id.gridview_image,
			// R.drawable.ico_chat_pic);

			ImageView iv = helper.getView(R.id.gridview_image);
			// iv.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ico_chat_pic));
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
			// mLoadImage.doTask();
		}

	}
}
