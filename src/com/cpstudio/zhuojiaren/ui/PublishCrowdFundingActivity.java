package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.BoringLayout.Metrics;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.color;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.util.ImageLoader;
import com.cpstudio.zhuojiaren.widget.ImageChooseAdapter;
import com.cpstudio.zhuojiaren.widget.PicChooseActivity;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class PublishCrowdFundingActivity extends BaseActivity {
	@InjectView(R.id.apcf_linearLayoutPicContainer)
	LinearLayout titleImageLL;
	@InjectView(R.id.apcd_edit_funding_title)
	EditText NameET;
	@InjectView(R.id.apcf_des_edit)
	EditText desET;
	@InjectView(R.id.apcf_image_text_container)
	LinearLayout imageAndTextLL;
	@InjectView(R.id.apcf_add_image)
	Button addIamgeBtn;
	@InjectView(R.id.apcf_add_text)
	Button addTextBtn;
	@InjectView(R.id.apcf_support_zhuobi)
	EditText supportZhuoBiET;
	@InjectView(R.id.apcf_support_rmb)
	EditText supportRmbET;
	@InjectView(R.id.apcf_aim_price_rmb)
	EditText aimPriceRmbET;
	@InjectView(R.id.apcf_aim_price_zhuo)
	EditText aimPriceZhuoBiET;
	
	//title图片是否改变
	private boolean mHeadChanged = false;
	private ZhuoConnHelper mConnHelper = null;
	private LoadImage mLoadImage = new LoadImage();
	private Context mContext;
	private ImageSelectHelper mIsh2 = null;
	private PopupWindows pwh = null;
	//
	private ArrayList<EditText>ETList ;
	private ArrayList<ImageView>IVList;
	private int imageRequest=5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_crowd_funding);
		ButterKnife.inject(this);
		mContext = this;
		initTitle();
		title.setText(R.string.new_crowdfunding_project);
		function.setText(R.string.finish);
		mContext = this;
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mIsh2 = ImageSelectHelper.getIntance(this,
				R.id.apcf_linearLayoutPicContainer);
		pwh = new PopupWindows(this);
		
		ETList = new ArrayList<EditText>();
		IVList = new ArrayList<ImageView>();
		initOnclick();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		// 标题图片
		mIsh2.getmAddButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsh2.initParams();
				
				pwh.showPop(findViewById(R.id.activity_publish_crowd_funding));
				
			}
		});
		addIamgeBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,PicChooseActivity.class);
				//清除之前的选择
				ImageChooseAdapter.mSelectedImage.clear();
				intent.putExtra("IMAGECOUNT", 8-IVList.size());
				startActivityForResult(intent, imageRequest);
			}
		});
		addTextBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addEditText();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==imageRequest){
			if(resultCode==RESULT_OK){
				ArrayList<String> paths = data.getStringArrayListExtra("data");
				for(int i=0;i<paths.size();i++){
					addImage(paths.get(i));
				}
				
			}
		}
		
		String filePath = pwh.dealPhotoReturn(requestCode, resultCode, data);
		if (filePath != null) {
			try {
				mIsh2.updateImage(filePath);
				mHeadChanged = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void addImage(String path){
		ImageView iv = new ImageView(mContext);
		WindowManager wm = (WindowManager) mContext.getSystemService(WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = width*9/10;
		LayoutParams lp = new LayoutParams(width, height);
		iv.setLayoutParams(lp);
		ImageLoader.getInstance().loadImage(path, iv);
		iv.setTag(path);
		iv.setScaleType(ScaleType.CENTER_CROP);
		IVList.add(iv);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, PhotoViewMultiActivity.class);
				ArrayList<String> orgs = new ArrayList<String>();
				for(ImageView iv:IVList){
					orgs.add((String)iv.getTag());
				}
				intent.putStringArrayListExtra("pics", orgs);
				intent.putExtra("pic", (String)v.getTag());
				intent.putExtra("type", "local");
				mContext.startActivity(intent);
			}
		});
		imageAndTextLL.addView(iv);
	}
	@SuppressLint("ResourceAsColor")
	private void addEditText(){
		EditText iv = new EditText(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		iv.setLayoutParams(lp);
		iv.setBackgroundResource(R.color.graywhitem);
		iv.setTextColor(color.graywhite);
		imageAndTextLL.addView(iv);
	}
}
