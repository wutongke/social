package com.cpstudio.zhuojiaren.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.CardAddUserResourceActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BusinessInfoVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResourceGXVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ImageLoader;
import com.cpstudui.zhuojiaren.lz.GongXuDetailActivity;

public class ZhuomaiCardCommercyInfoFra extends Fragment {
	@InjectView(R.id.tvMoreResource)
	TextView tvMoreResource;
	@InjectView(R.id.tvMoreNeed)
	TextView tvMoreNeed;
	@InjectView(R.id.textViewResourceTitle)
	TextView textViewResourceTitle;
	@InjectView(R.id.textViewTime)
	TextView textViewTime;
	@InjectView(R.id.textViewNeedTitle)
	TextView textViewNeedTitle;
	@InjectView(R.id.textViewNeedTime)
	TextView textViewNeedTime;
	@InjectView(R.id.textViewWeidian)
	ImageView ivWeidian;
	@InjectView(R.id.textViewVideo)
	ImageView ivVideo;
	@InjectView(R.id.imageViewResourcePic)
	ImageView imageViewResourcePic;
	@InjectView(R.id.imageViewNeedPic)
	ImageView imageViewNeedPic;
	@InjectView(R.id.rlmyResource)
	View rlmyResource;
	@InjectView(R.id.rlmyNeed)
	View rlmyNeed;
	private LoadImage mLoadImage = LoadImage.getInstance();
	public final static String EDIT_RES_STR1 = "type";
	public final static String EDIT_RES_STR2 = "userid";
	private ZhuoConnHelper mConnHelper = null;

	private Context mContext;
	BusinessInfoVO info;
	// 主View
	View layout;
	private String uid = null;
	Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj, getActivity()
						.getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getActivity().getApplicationContext());
					info = nljh.parseBusinessInfo();
				} else {
					CommonUtil.displayToast(getActivity(), R.string.data_error);
					return;
				}
				updateInfo();
				break;
			}
			}
		}

	};

	public interface functionListener {
		public void onTypeChange(int type);
	}

	private void updateInfo() {
		// TODO Auto-generated method stub
		if (info == null)
			return;
		final ResourceGXVO resource = info.getSupply();
		if (resource != null) {
			mLoadImage.beginLoad(resource.getPicture(),
					imageViewResourcePic);
			textViewResourceTitle.setText(resource.getTitle());
			textViewTime.setText(resource.getAddtime());
		}
		final ResourceGXVO need = info.getDemand();
		if (need != null) {
			mLoadImage.beginLoad(need.getPicture(), imageViewNeedPic);
			textViewNeedTitle.setText(need.getTitle());
			textViewNeedTime.setText(need.getAddtime());
		}
		rlmyResource.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						GongXuDetailActivity.class);
				intent.putExtra("msgid",resource.getSdid());
				startActivity(intent);
			}
		});
		rlmyNeed.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						GongXuDetailActivity.class);
				intent.putExtra("msgid",need.getSdid());
				startActivity(intent);
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = inflater.inflate(R.layout.fragment_zhuomai_commercy_info,
				container, false);
		ButterKnife.inject(this, layout);

		mContext = getActivity();
		mConnHelper = ZhuoConnHelper.getInstance(getActivity()
				.getApplicationContext());
		uid = getArguments().getString("userid");
		loadData();
		initclick();
		return layout;
	}

	private void initclick() {
		// TODO Auto-generated method stub
		ivWeidian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		ivVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		tvMoreResource.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 跳转到我的资源列表
				Intent i = new Intent(mContext,
						CardAddUserResourceActivity.class);
				i.putExtra(EDIT_RES_STR1, 0);
				i.putExtra(EDIT_RES_STR2, uid);
				startActivity(i);
			}
		});
		tvMoreNeed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 跳转到我的需求列表
				Intent i = new Intent(mContext,
						CardAddUserResourceActivity.class);
				i.putExtra(EDIT_RES_STR1, 1);
				i.putExtra(EDIT_RES_STR2, uid);
				startActivity(i);
			}
		});
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getActivity().getApplicationContext()) == 2) {
		} else {
			mConnHelper.getBusinessInfo(mUIHandler, MsgTagVO.DATA_LOAD, uid);
		}
	}

}
