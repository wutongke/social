package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.PhotoViewMultiActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.color;
import com.cpstudio.zhuojiaren.adapter.ImageGridAdapter;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CrowdFundingDes;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PayBackVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ImageLoader;
import com.cpstudio.zhuojiaren.widget.ImageChooseAdapter;
import com.cpstudio.zhuojiaren.widget.PicChooseActivity;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.google.gson.Gson;

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
	@InjectView(R.id.apcf_aim_price_rmb)
	EditText aimPriceRmbET;
	@InjectView(R.id.apcf_aim_price_zhuo)
	EditText aimPriceZhuoBiET;
	@InjectView(R.id.apcf_add_payback_btn)
	Button addPaybackBtn;
	@InjectView(R.id.apcf_add_payback)
	LinearLayout addPaybackLayout;
	@InjectView(R.id.apcf_type)
	TextView crowdFundingType;
	int type = 1;
	//回报项目index
	private int index = 1;
	String[] crowdFundingTypeStr;
	// title图片是否改变
	private boolean mHeadChanged = false;
	private AppClient mConnHelper = null;
	private LoadImage mLoadImage = new LoadImage();
	private Context mContext;
	private ImageSelectHelper mIsh2 = null;
	private PopupWindows pwh = null;
	//
	private ArrayList<EditText> ETList;
	private ArrayList<ImageView> IVList;
	private ArrayList<View> DesViews = new ArrayList<View>();
	// 项目回报的view
	private ArrayList<View> payBackViewList = new ArrayList<View>();
	private ArrayList<ImageGridAdapter> payBackImageList = new ArrayList<ImageGridAdapter>();

	private ArrayList<PayBackVO> paybacks = new ArrayList<PayBackVO>();
	private ArrayList<CrowdFundingDes> dess = new ArrayList<CrowdFundingDes>();
	// 需要上传的图片
	Map<String, ArrayList<String>> filesMap = new HashMap<String, ArrayList<String>>();
	private int imageRequest = 200;
	// 头像图片上传后获取到的key
	String header;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_crowd_funding);
		ButterKnife.inject(this);
		mContext = this;
		crowdFundingTypeStr = getResources().getStringArray(
				R.array.crowdfunding);
		initTitle();
		title.setText(R.string.new_crowdfunding_project);
		function.setText(R.string.finish);
		mContext = this;
		mConnHelper = AppClient.getInstance(getApplicationContext());
		mIsh2 = ImageSelectHelper.getIntance(this,
				R.id.apcf_linearLayoutPicContainer);
		pwh = new PopupWindows(this);

		ETList = new ArrayList<EditText>();
		IVList = new ArrayList<ImageView>();
		initOnclick();
	}

	private void initOnclick() {
		// TODO Auto-generated method stub
		function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateFilse();
			}
		});
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
				Intent intent = new Intent(mContext, PicChooseActivity.class);
				// 清除之前的选择
				ImageChooseAdapter.mSelectedImage.clear();
				intent.putExtra("IMAGECOUNT", 8 - IVList.size());
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
		addPaybackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				addPayBack();
			}
		});
		crowdFundingType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext,
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle("选择众筹类型")
						.setIcon(R.drawable.ico_syzy)
						.setSingleChoiceItems(R.array.crowdfunding, 0,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										type = which + 1;
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										crowdFundingType
												.setText(crowdFundingTypeStr[type - 1]);
									}
								}).setNegativeButton("取消", null).create()
						.show();
			}
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 回报项目图片回调
		if (requestCode <= payBackViewList.size()) {
			if (resultCode == RESULT_OK) {
				payBackImageList.get(requestCode - 1).mImages.addAll(data
						.getStringArrayListExtra("data"));
				payBackImageList.get(requestCode - 1).notifyDataSetChanged();
			}
			return;
		}
		if (requestCode == imageRequest) {
			if (resultCode == RESULT_OK) {
				ArrayList<String> paths = data.getStringArrayListExtra("data");
				for (int i = 0; i < paths.size(); i++) {
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

	private void addImage(String path) {
		ImageView iv = new ImageView(mContext);
		WindowManager wm = (WindowManager) mContext
				.getSystemService(WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels;
		int height = width * 9 / 10;
		LayoutParams lp = new LayoutParams(width, height);
		iv.setLayoutParams(lp);
		ImageLoader.getInstance().loadImage(path, iv);
		iv.setTag(path);
		iv.setScaleType(ScaleType.CENTER_CROP);
		IVList.add(iv);
		DesViews.add(iv);
		iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,
						PhotoViewMultiActivity.class);
				ArrayList<String> orgs = new ArrayList<String>();
				for (ImageView iv : IVList) {
					orgs.add((String) iv.getTag());
				}
				intent.putStringArrayListExtra("pics", orgs);
				intent.putExtra("pic", (String) v.getTag());
				intent.putExtra("type", "local");
				mContext.startActivity(intent);
			}
		});
		imageAndTextLL.addView(iv);
	}

	@SuppressLint("ResourceAsColor")
	private void addEditText() {
		EditText iv = new EditText(mContext);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		iv.setLayoutParams(lp);
//		iv.setBackgroundResource(R.color.graywhitem);
		iv.setTextColor(color.graywhite);
		ETList.add(iv);
		DesViews.add(iv);
		imageAndTextLL.addView(iv);
	}

	private void addPayBack() {
		View view = getLayoutInflater().inflate(R.layout.item_payback_add,
				null);
		payBackViewList.add(view);
		TextView name = (TextView)view.findViewById(R.id.payback_name);
		name.setText("回报项目"+index++);
		TextView addImage = (TextView) view.findViewById(R.id.ipba_add_image);
		GridView imageGrid = (GridView) view
				.findViewById(R.id.ipba_image_layout);
		final ArrayList<String> imageDir = new ArrayList<String>();
		addImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ImageChooseAdapter.mSelectedImage.clear();
				Intent intent = new Intent(mContext, PicChooseActivity.class);
				intent.putExtra("IMAGECOUNT", 5 - imageDir.size());
				startActivityForResult(intent, payBackViewList.size());
			}
		});
		final ImageGridAdapter imageAdatper = new ImageGridAdapter(mContext,
				imageDir, R.layout.item_grid_image2);
		payBackImageList.add(imageAdatper);
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
		addPaybackLayout.addView(view);
	};

	// 需要先上传图片在发布
	public void pubCrowdFunding() {
		String targetZb = aimPriceZhuoBiET.getText().toString();
		Gson gson = new Gson();
		String support = gson.toJson(paybacks);
		String description = gson.toJson(dess);
		mConnHelper.createCrowdFunding(PublishCrowdFundingActivity.this,
				uiHanler, MsgTagVO.PUB_INFO, NameET.getText().toString(), type
						+ "", targetZb, header, support, description);
	}

	public void updateFilse() {
		// 判断是否为空
		if (NameET.getText().toString().isEmpty()
				|| aimPriceZhuoBiET.getText().toString().isEmpty()
				|| mIsh2.getTags().isEmpty()){
			CommonUtil.displayToast(mContext, R.string.please_finish);
			return;
		}
		

		for (View view : payBackViewList) {
			if (isTextEmpty(view, R.id.ipba_des)
					|| isTextEmpty(view, R.id.ipba_support_people)
					|| isTextEmpty(view, R.id.ipba_support_zhuobi)){
				
				CommonUtil.displayToast(mContext, R.string.please_finish);
				return;
			}
		}
		ArrayList<String> desFiles = new ArrayList<String>();
		for (ImageView view : IVList) {
			desFiles.add((String) view.getTag());
		}
		filesMap.put("des", desFiles);
		filesMap.put("header", mIsh2.getTags());
		int i = 0;
		for (ImageGridAdapter adapter : payBackImageList) {
			filesMap.put(i + "", (ArrayList<String>) adapter.mImages);
			i++;
		}

		mConnHelper.updateFilesForCrowdFunding(filesMap, uiHanler,
				MsgTagVO.UPLOAD_FILE, PublishCrowdFundingActivity.this,
				"updateCrowdingImage");
	}

	Handler uiHanler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MsgTagVO.PUB_INFO) {
				OnClickListener listener = new OnClickListener() {

					@Override
					public void onClick(View paramView) {
						PublishCrowdFundingActivity.this.finish();
					}
				};
				if (JsonHandler.checkResult((String) msg.obj)) {
					View v = findViewById(R.id.activity_publish_crowd_funding);
					pwh.showPopDlgOne(v, listener, R.string.info66);
				} else {
					View v = findViewById(R.id.activity_publish_crowd_funding);
					pwh.showPopDlgOne(v, null, R.string.submit_error);
				}
			} else if (msg.what == MsgTagVO.UPLOAD_FILE) {
				Map<String, StringBuilder> map = (Map<String, StringBuilder>) msg.obj;
				if (map == null)
					Toast.makeText(mContext, "上传到七牛云失败", 1000).show();
				else if (map.size() > 0) {

					for (int i = 0; i < payBackViewList.size(); i++) {
						View view = payBackViewList.get(i);
						PayBackVO tempP = new PayBackVO();
						TextView nameV = (TextView) view
								.findViewById(R.id.ipba_des);
						TextView supportPrice = (TextView) view
								.findViewById(R.id.ipba_support_zhuobi);
						TextView supportPeople = (TextView) view
								.findViewById(R.id.ipba_support_people);

						tempP.setAmount(supportPrice.getText().toString());
						tempP.setIntro(nameV.getText().toString());
						tempP.setLimits(supportPeople.getText().toString());
						if(map.get(i+"")==null)
							break;
						String[] keys = map.get(i + "").toString().split(",");
						ArrayList<String> urls = new ArrayList<String>();
						for(String temp:keys){
							urls.add(temp);
						}
						tempP.setPics( map.get(i + "").toString());
						paybacks.add(tempP);
					}
					header = map.get("header").toString();
					//添加描述
					CrowdFundingDes des = new CrowdFundingDes();
					des.setType("text");
					des.setContent(desET.getText().toString());
					dess.add(des);
					if(map.get("des")!=null){
						String[] desImage = map.get("des").toString().split(",");
						int index = 0;
						for (int j = 0; j < DesViews.size(); j++) {

							CrowdFundingDes temp = new CrowdFundingDes();
							// 保证按顺序生成对象
							View view = DesViews.get(j);
							if (view instanceof EditText) {
								temp.setType("text");
								temp.setContent(((EditText) view).getText()
										.toString());
							} else if (view instanceof ImageView) {
								temp.setType("img");
								temp.setContent(desImage[index++]);
							}
							dess.add(temp);
						}
					}
					
					pubCrowdFunding();
				}
			}
		};
	};

	private boolean isTextEmpty(View view, final int resid) {
		if (((EditText) view.findViewById(resid)).getText().toString()
				.isEmpty())
			return true;
		else
			return false;
	}
}
