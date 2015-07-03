package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ImageGridAdapter;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.DateTimePickDialogUtil;
import com.cpstudio.zhuojiaren.widget.ImageChooseAdapter;
import com.cpstudio.zhuojiaren.widget.PicChooseActivity;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;

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
	private Context mContext;
	private int requestCode = 1;
	private ArrayList<String> imageDir = new ArrayList<String>();
	private CommonAdapter<String> imageAdatper;
	private ArrayList<EditText> peopleList = new ArrayList<EditText>();
	private ArrayList<EditText> phoneList = new ArrayList<EditText>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_event);
		ButterKnife.inject(this);
		mContext = this;
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
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(EditEventActivity.this,"");
				dateTimePicKDialog.dateTimePicKDialog(startTime);
			}
		});
		endTimeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(EditEventActivity.this,"");
				dateTimePicKDialog.dateTimePicKDialog(endTime);
			}
		});
		locateLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final PlaceChooseDialog placeChoose = new PlaceChooseDialog(
						mContext, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,"北京","北京");
				placeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								locate.setText(placeChoose.getPlace().getText().toString());
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
		addPeople.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addContactPeopel();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == this.requestCode && resultCode == RESULT_OK) {
			imageDir.addAll(data.getStringArrayListExtra("data"));
			imageAdatper.notifyDataSetChanged();
		}
	}
	private void addContactPeopel(){
		LayoutInflater infloater = EditEventActivity.this.getLayoutInflater();
		View view = infloater.inflate(R.layout.item_add_people, null);
		EditText people = (EditText)view.findViewById(R.id.idp_people);
		EditText phone = (EditText)view.findViewById(R.id.idp_phone);
		contactPeople.addView(view);
		peopleList.add(people);
		phoneList.add(phone);
	}
}
