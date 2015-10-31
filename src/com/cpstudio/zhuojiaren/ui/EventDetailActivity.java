package com.cpstudio.zhuojiaren.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.MapLocateActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.EventVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.Util;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.RoundImageView;
import com.cpstudui.zhuojiaren.lz.ZhuoMaiCardActivity;
import com.google.gson.Gson;
import com.umeng.socialize.media.UMImage;

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

	private AppClientLef mConnHelper = null;
	private LoadImage mLoadImage = new LoadImage();
	// 不同身份，功能不同
	private String memberType = "";
	private PopupWindows pwh = null;
	private String eventId = null;
	private Context mContext;
	private EventVO event;
	// 退出活动的tag
	private final int quit = 7;
	private long servertime;

	/**
	 * 从intent中获取id号，然后加载数据，需要区别用户身份，是否是创建人，是否是联系人，是否已经加入到活动
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_detail);
		ButterKnife.inject(this);
		mContext = this;
		mConnHelper = AppClientLef.getInstance(getApplicationContext());
		Intent i = getIntent();
		eventId = i.getStringExtra("eventId");
		pwh = new PopupWindows((Activity) mContext);
		loadData();
		initOnclick();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		goBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EventDetailActivity.this.finish();
			}
		});
		bossLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (event != null) {
					Intent intent = new Intent(mContext,
							ZhuoMaiCardActivity.class);
					intent.putExtra("userid", event.getUserid());
					startActivity(intent);
				}
			}
		});
	}

	private void loadData() {
		mConnHelper.getEventDetail(EventDetailActivity.this, mUIHandler,
				MsgTagVO.DATA_LOAD, eventId);
//		mConnHelper.getTime(EventDetailActivity.this, mUIHandler,
//				MsgTagVO.DATA_LOAD);
	}

	private Handler mUIHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (msg.obj != null && !msg.obj.equals("")) {
					ResultVO res;
					if (JsonHandler.checkResult((String) msg.obj,
							EventDetailActivity.this)) {
						res = JsonHandler.parseResult((String) msg.obj);
					} else {
						CommonUtil.displayToast(mContext, R.string.data_error);
						return;
					}
					String data = res.getData();
					EventVO detail = null;
//					JsonHandler nljh = new JsonHandler(data,
//							getApplicationContext());
//					detail = nljh.parseEvent();
					try {
						Gson gson = new Gson();
						detail = gson.fromJson(data, EventVO.class);
					} catch (Exception e) {
						e.printStackTrace();
					}
					event = detail;
					if(event==null){
						CommonUtil.displayToast(EventDetailActivity.this,
								R.string.data_error);
						Log.d("Debug", "json数据出错。。。。。。。。。。。。。。");
						return;
					}
					if (event.getIsjoined().equals("1")) {
						toApply.setText(R.string.toapply);
					} else {
						toApply.setText(R.string.tonoapply);
					}
					// 是否加入
					toApply.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (event.getIsjoined().equals("1")) {
								// 退出
								mConnHelper.quitEvent(
										ZhuoCommHelper.getEventadd(),
										"activityid", eventId, "type", "0",
										mUIHandler, quit,
										EventDetailActivity.this);
								if(event!=null){
									int count = 0;
									if(Integer.parseInt(event.getJoinCount())>0){
										count = Integer.parseInt(event.getJoinCount());
									}
									event.setJoinCount(count +"");
									applyCount.setText(event.getJoinCount());
								}
							} else {
								int count = Integer.parseInt(event.getJoinCount())+1;
								event.setJoinCount(count+"");
								applyCount.setText(event.getJoinCount());
								mConnHelper.quitEvent(
										ZhuoCommHelper.getEventadd(),
										"activityid", eventId, "type", "1",
										mUIHandler, quit,
										EventDetailActivity.this);
							}
						}
					});
					share.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							com.cpstudio.zhuojiaren.widget.CustomShareBoard shareBoard = new com.cpstudio.zhuojiaren.widget.CustomShareBoard(
									EventDetailActivity.this);
							shareBoard.setTitle(event.getTitle());
							UMImage image = new UMImage(mContext, event
									.getUheader());
							shareBoard.setImage(image);
							shareBoard.setContent(event.getContent());
							shareBoard.showAtLocation(EventDetailActivity.this
									.getWindow().getDecorView(),
									Gravity.BOTTOM, 0, 0);
						}
					});
					if (null != detail) {
						name.setText(detail.getTitle());
						browerCount.setText(detail.getViewCount());
						shareCount.setText(detail.getShareCount());
						applyCount.setText(detail.getJoinCount());
						content.setText(detail.getContent());
						mLoadImage.beginLoad(detail.getUheader(), peopleImage);
						peopleCompany.setText(detail.getCompany());
						peopleName.setText(detail.getName());
						peoplePostion.setText(detail.getPosition());
						time.setText(detail.getStarttime() + "\n"
								+ detail.getEndtime());
						// 倒计时
						servertime = Long.parseLong(detail.getLefttime());
						Runnable runnable = new Runnable() { 
					        @Override 
					        public void run() { 
					        	servertime--; 
					        	long[]timeLeft = Util.getTimeFromSeconds(servertime);
					            day.setText(timeLeft[3]+"");
								hour.setText(timeLeft[2]+"");
								minute.setText(timeLeft[1]+"");
								second.setText(timeLeft[0]+"");
								mUIHandler.postDelayed(this, 1000); 
					        } 
					    }; 
						mUIHandler.post(runnable);
						
						locate.setText(detail.getAddress());
						locate.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Intent intent = new Intent(
										EventDetailActivity.this,
										MapLocateActivity.class);
								intent.putExtra("LONGITUDE",
										event.getLongitude());
								intent.putExtra("LATITUDE", event.getLatitude());
								startActivity(intent);
							}
						});
						String contacts = detail.getContacts();
						if (!TextUtils.isEmpty(contacts)) {
							String[] cons = contacts.split(",");
							String[] phones = detail.getPhone().split(",");
							for (int i = 0; i < cons.length; i++) {
								addContactPeople(cons[i], phones[i]);
							}
						}
						collect.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if (event.isCollect()) {
									// 取消收藏
									collect.setBackgroundResource(R.drawable.qhdcollect);
									mConnHelper.collection((Activity)mContext,
											ZhuoCommHelper.getEventcollection(),
											"activityid", eventId, "type", "0");
									event.setIscollected("0");
									CommonUtil.displayToast(mContext, "取消收藏");
								} else {
									mConnHelper.collection((Activity)mContext,
											ZhuoCommHelper.getEventcollection(),
											"activityid", eventId, "type", "1");
									event.setIscollected("1");
									collect.setBackgroundResource(R.drawable.zcollect2);
									CommonUtil.displayToast(mContext, "收藏");
								}
							}
						});

					}
				}
				break;
			case quit:
				if (JsonHandler.checkResult((String) msg.obj,
						EventDetailActivity.this)) {
					CommonUtil.displayToast(mContext, "操作成功");
					if (event.getIsjoined().equals("1")) {
						// 退出
						event.setIsjoined("0");

					} else {
						event.setIsjoined("1");
					}
					if (event.getIsjoined().equals("1")) {
						toApply.setText(R.string.toapply);
					} else {
						toApply.setText(R.string.tonoapply);
					}
				} else {
					CommonUtil.displayToast(mContext, "操作失败");
				}
				break;
			case MsgTagVO.time:
				if (JsonHandler.checkResult((String) msg.obj,
						EventDetailActivity.this)) {
					
				} else {
					CommonUtil.displayToast(mContext, "操作失败");
				}
			}
		};
	};

	private void addContactPeople(String name, String phone) {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.view_event_people, null);
		TextView nameTV = (TextView) view.findViewById(R.id.vep_people);
		if (null != name) {
			nameTV.setText(name);
		}
		final TextView phoneTV = (TextView) view
				.findViewById(R.id.vep_phone);
		if (null != phone) {
			phoneTV.setText(phone);
			phoneTV.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri
							.parse("tel:" + phoneTV.getText().toString()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			});
		}
		peopleLayout.addView(view);
	}
	
}
