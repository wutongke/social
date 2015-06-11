package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Browser;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.CardAddUserWorkActivity;
import com.cpstudio.zhuojiaren.MsgDetailActivity;
import com.cpstudio.zhuojiaren.QuanDetailActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.UserCardActivity;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.RoundImageView;

public class EventDetailActivity extends Activity {
	@InjectView(R.id.aed_activity_back)
	TextView goBack;
	@InjectView(R.id.aed_collect)
	ImageView collect;
	@InjectView(R.id.aed_share)
	ImageView share;
	@InjectView(R.id.aed_name)
	TextView name;
	@InjectView(R.id.aed_brower)
	TextView browerCount;
	@InjectView(R.id.aed_share_count)
	TextView shareCount;
	@InjectView(R.id.aed_apply)
	TextView applyCount;
	@InjectView(R.id.aed_content)
	TextView content;
	@InjectView(R.id.aed_people_image)
	RoundImageView peopleImage;
	@InjectView(R.id.aed_people_name)
	TextView peopleName;
	@InjectView(R.id.aed_people_position)
	TextView peoplePostion;
	@InjectView(R.id.aed_people_company)
	TextView peopleCompany;
	@InjectView(R.id.aed_time_day)
	TextView day;
	@InjectView(R.id.aed_time_hour)
	TextView hour;
	@InjectView(R.id.aed_time_minute)
	TextView minute;
	@InjectView(R.id.aed_time_second)
	TextView second;
	@InjectView(R.id.aed_time)
	TextView time;
	@InjectView(R.id.aed_locate)
	TextView locate;
	@InjectView(R.id.aed_people)
	LinearLayout peopleLayout;
	@InjectView(R.id.aed_toapply)
	Button toApply;
	@InjectView(R.id.aed_boss_layout)
	RelativeLayout bossLayout;
	
	private ZhuoConnHelper mConnHelper = null;
	private LoadImage mLoadImage = new LoadImage();
	// 不同身份，功能不同
	private String memberType = "";
	private PopupWindows pwh = null;
	private String eventId = null;
	private Context mContext;
	private EventVO event;
	/**
	 * 从intent中获取id号，然后加载数据，需要区别用户身份，是否是创建人，是否是联系人，是否已经加入到活动
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		ButterKnife.inject(this);
		mContext = this;
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		eventId = i.getStringExtra("eventId");
		pwh = new PopupWindows((Activity) mContext);
		loadData();
		initOnclick();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		collect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null == event.getIsCollection()  || event.getIsCollection().equals("0")) {
					mConnHelper.collectMsg(eventId, "1", mUIHandler,
							MsgTagVO.MSG_COLLECT, null, true, null,
							null);
				} else {
					mConnHelper.collectMsg(eventId, "0", mUIHandler,
							MsgTagVO.MSG_COLLECT, null, true, null,
							null);
				}
			}
		});
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		bossLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(event!=null){
					Intent intent = new Intent(mContext,
							UserCardActivity.class);
					intent.putExtra("userid", event.getBoss().getUserid());
					startActivity(intent);
				}
			}
		});
		toApply.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//需要判断是否已经加入
			}
		});
	}

	private void loadData() {

		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {

			CommonUtil.displayToast(getApplicationContext(), R.string.error0);
		} else {
			String params = ZhuoCommHelper.getUrlGroupDetail() + "?eventid="
					+ eventId;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
					(Activity) mContext, true, new OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							((Activity) mContext).finish();
						}
					});
		}
	}

	private Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (msg.obj != null && !msg.obj.equals("")) {
					EventVO detail = null;
					if (msg.obj instanceof EventVO) {
						detail = (EventVO) msg.obj;
					} else {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						detail = nljh.parseEvent();
						event = detail;
					}
					if (null != detail) {
						name.setText(detail.getName());
						browerCount.setText(detail.getBrowerCount());
						shareCount.setText(detail.getShareCount());
						applyCount.setText(detail.getShareCount());
						content.setText(detail.getContent());
						//活动发起人
						UserVO boss = detail.getBoss();
						String imageUrl = boss.getUheader();
						peopleImage.setTag(imageUrl);
						mLoadImage.addTask(imageUrl, peopleImage);
						peopleName.setText(boss.getUsername());
						peoplePostion.setText(boss.getPost());
						time.setText(detail.getTime());
						locate.setText(detail.getLocate());
						String contactPeopels = detail.getPeople();
						String phones = detail.getPhone();
						String[]contact=null;
						String[]phone = null;
						if(contactPeopels!=null&&phones!=null){
							contact = contactPeopels.split(";");
							phone = phones.split(";");
						}
						if(contact!=null&&phone!=null)
						for(int i =0;i<contact.length;i++){
							addContactPeople(contact[i],phone[i]);
						}
					}
				}
			}
		};
	};
	private void addContactPeople(String name, String phone) {
		LayoutInflater inflater = LayoutInflater
				.from(mContext);
		View view = inflater.inflate(R.layout.view_event_people, null);
		TextView nameTV = (TextView) view.findViewById(R.id.vep_people);
		if (null != name) {
			nameTV.setText(name);
		}
		final TextView phoneTV = (TextView) view.findViewById(R.id.editTextWork);
		if (null != phone) {
			phoneTV.setText(phone);
			phoneTV.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + phoneTV.getText().toString()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			});
		}
		peopleLayout.addView(view);
	}
}
