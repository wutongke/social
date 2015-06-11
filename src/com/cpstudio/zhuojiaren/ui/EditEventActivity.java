package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.ImageGridAdapter;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.ImageChooseAdapter;
import com.cpstudio.zhuojiaren.widget.PicChooseActivity;

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
	@InjectView(R.id.aee_add_contact_people)
	Button addPeople;
	private Context mContext;
	private int requestCode=1;
	private ArrayList<String>imageDir = new ArrayList<String>();
	private CommonAdapter<String>imageAdatper;
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
				Intent intent = new Intent(mContext,PicChooseActivity.class); 
				intent.putExtra("IMAGECOUNT", 5-imageDir.size());
				startActivityForResult(intent,requestCode); 
			}
		});
		imageAdatper = new ImageGridAdapter(mContext,imageDir,R.layout.item_grid_image);
		imageGrid.setAdapter(imageAdatper);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==this.requestCode&&resultCode==RESULT_OK){
			imageDir.addAll(data.getStringArrayListExtra("data"));
		}
	}
}
