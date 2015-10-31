package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ImageGridAdapter;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.DateTimePickDialogUtil;
import com.cpstudio.zhuojiaren.widget.ImageChooseAdapter;
import com.cpstudio.zhuojiaren.widget.PicChooseActivity;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class EditEventActivity extends BaseActivity {
	@InjectView(R.id.aee_event_name)
	EditText nameET;
	@InjectView(R.id.aee_event_des)
	EditText desET;
	@InjectView(R.id.aee_image_layout)
	GridView imageGrid;
	@InjectView(R.id.aee_add_image)
	TextView addImage;
	@InjectView(R.id.aee_start_time)
	TextView startTime;
	@InjectView(R.id.aee_end_time)
	TextView endTime;
	@InjectView(R.id.aee_start_time_layout)
	LinearLayout startTimeLayout;
	@InjectView(R.id.aee_end_time_layout)
	LinearLayout endTimeLayout;
	@InjectView(R.id.aee_event_locate)
	TextView locate;
	@InjectView(R.id.aee_event_locate_more)
	TextView locateMore;
	@InjectView(R.id.aee_event_locate_layout)
	LinearLayout locateLayout;
	@InjectView(R.id.aee_event_locate_more_layout)
	LinearLayout locateMoreLayout;
	@InjectView(R.id.aee_people_contact)
	LinearLayout contactPeople;
	@InjectView(R.id.aee_add_contact_people)
	TextView addPeople;
	// 提交的时间
	String timeStart;
	String timeEnd;
	// 经纬度
	String longitude;
	String latitude;
	private Context mContext;
	// 图片
	private int requestCode = 1;
	// 位置
	private int requestLocate = 2;
	private ArrayList<String> imageDir = new ArrayList<String>();
	private CommonAdapter<String> imageAdatper;
	private ArrayList<EditText> peopleList = new ArrayList<EditText>();
	private ArrayList<EditText> phoneList = new ArrayList<EditText>();
	// 从圈子主页获得 圈子id
	private String groupeId = "1";
	// 结果提示
	PopupWindows pwh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);
		ButterKnife.inject(this);
		groupeId = getIntent().getStringExtra("groupid");
		mContext = this;
		pwh = new PopupWindows(EditEventActivity.this);
		initTitle();
		function.setText(R.string.finish);
		title.setText(R.string.public_event);
		initOnclick();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		addImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageChooseAdapter.mSelectedImage.clear();
				Intent intent = new Intent(mContext, PicChooseActivity.class);
				intent.putExtra("IMAGECOUNT", 5 - imageDir.size());
				startActivityForResult(intent, requestCode);
			}
		});
		imageAdatper = new ImageGridAdapter(mContext, imageDir,
				R.layout.item_grid_image2);
		imageGrid.setAdapter(imageAdatper);
		imageGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (imageDir.size() > position) {
					imageDir.remove(position);
					imageAdatper.notifyDataSetChanged();
				}
			}
		});
		startTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						EditEventActivity.this, "");
				dateTimePicKDialog.dateTimePicKDialog(startTime);
				timeStart = dateTimePicKDialog.getTime();
			}
		});
		endTimeLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						EditEventActivity.this, "");
				dateTimePicKDialog.dateTimePicKDialog(endTime);
				timeEnd = dateTimePicKDialog.getTime();
			}
		});
		locateLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final PlaceChooseDialog placeChoose = new PlaceChooseDialog(
						mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, "北京",
						"北京");
				placeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								locate.setText(placeChoose.getPlace().getText()
										.toString());
							}
						});
				placeChoose.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				placeChoose.setTitle(R.string.choose_place);
				placeChoose.show();
			}
		});
		locateMoreLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivityForResult(new Intent(EditEventActivity.this,
						ChooseLocateActivity.class), requestLocate);
			}
		});
		addPeople.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addContactPeopel();
			}
		});
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = nameET.getText().toString();
				String content = desET.getText().toString();
				String address = locate.getText().toString()
						+ locateMore.getText().toString();
				StringBuilder contacts = new StringBuilder();
				StringBuilder phone = new StringBuilder();
				for (int i = peopleList.size() - 1; i >= 0; i--) {
					if (i > 0)
						contacts.append(peopleList.get(i).getText().toString()
								+ ",");
					else
						contacts.append(peopleList.get(i).getText().toString());
				}
				for (int i = phoneList.size() - 1; i >= 0; i--) {
					if (i > 0)
						phone.append(phoneList.get(i).getText().toString()
								+ ",");
					else
						phone.append(phoneList.get(i).getText().toString());
				}
				if (groupeId == null || name == null || name.isEmpty()
						|| content == null || content.isEmpty()
						|| timeStart == null || timeStart.isEmpty()
						|| timeEnd == null || timeEnd.isEmpty()
						|| address == null || address.isEmpty()
						|| contacts.toString().isEmpty()
						|| phone.toString().isEmpty() || imageDir.size() == 0) {
					CommonUtil.displayToast(mContext, R.string.please_finish);
					return;
				} else
					AppClientLef.getInstance(EditEventActivity.this)
							.createEvent(EditEventActivity.this, uiHandler,
									MsgTagVO.PUB_INFO, longitude,latitude,groupeId, name, content,
									contacts.toString(), timeStart, timeEnd,
									address, phone.toString(), imageDir);
			}
		});
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MsgTagVO.PUB_INFO) {
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						EditEventActivity.this.finish();
					}
				};
				if (JsonHandler.checkResult((String) msg.obj)) {
					View v = findViewById(R.id.event_edit_activity);
					pwh.showPopDlgOne(v, listener, R.string.info66);
				} else {
					View v = findViewById(R.id.event_edit_activity);
					ResultVO res = JsonHandler.parseResult((String)msg.obj);
					String out = getResources().getString(R.string.submit_error);
					if(res.getMsg()!=null){
						out = "\n"+res.getMsg();
					}
					pwh.showPopDlgOne(v, null, out);
				}
			}
		};
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == this.requestCode && resultCode == RESULT_OK) {
			imageDir.addAll(data.getStringArrayListExtra("data"));
			imageAdatper.notifyDataSetChanged();
		} else if (requestCode == requestLocate && resultCode == RESULT_OK) {
			locateMore.setText(data.getStringExtra("locate"));
			longitude = String.valueOf(data.getStringExtra("longitude"));
			latitude = String.valueOf(data.getStringExtra("latitude"));
		}
	}

	private void addContactPeopel() {
		LayoutInflater infloater = EditEventActivity.this.getLayoutInflater();
		View view = infloater.inflate(R.layout.item_add_people, null);
		EditText people = (EditText) view.findViewById(R.id.idp_people);
		EditText peopleTitle = (EditText) view.findViewById(R.id.idp_peopel_title);
		EditText phoneTitle = (EditText) view.findViewById(R.id.idp_phone_title);
		EditText phone = (EditText) view.findViewById(R.id.idp_phone);
		people.setTextColor(Color.BLACK);
		peopleTitle.setTextColor(Color.BLACK);
		phoneTitle.setTextColor(Color.BLACK);
		phone.setTextColor(Color.BLACK);
		contactPeople.addView(view);
		peopleList.add(people);
		phoneList.add(phone);
	}
}
