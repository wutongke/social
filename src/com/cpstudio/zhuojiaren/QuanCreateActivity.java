package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 创建圈子和修改圈子
 * 
 * @author lef
 * 
 */
public class QuanCreateActivity extends BaseActivity {
	@InjectView(R.id.QuanType)
	TextView quanTypeView;
	@InjectView(R.id.QuanLocation)
	TextView quanLocationView;
	@InjectView(R.id.add_quanzi_right)
	RadioGroup addQuanRight;
	@InjectView(R.id.add_quanzi_right1)
	RadioGroup addQuanRight1;
	@InjectView(R.id.add_quanzi_right2)
	RadioGroup addQuanRight2;
	@InjectView(R.id.visite_quanzi_right)
	RadioGroup QuanRight;
	@InjectView(R.id.visite_quanzi_right1)
	RadioGroup visiteQuanRight1;
	@InjectView(R.id.visite_quanzi_right2)
	RadioGroup visiteQuanRight2;
	private PopupWindows pwh = null;
	// 管理员头像
	private ImageSelectHelper mIsh = null;
	// 圈子图片
	private ImageSelectHelper mIsh2 = null;
	private ArrayList<String> mSelectlist = new ArrayList<String>();
	private ZhuoConnHelper mConnHelper = null;
	private String groupid = null;
	private LoadImage mLoadImage = new LoadImage();
	private boolean mHeadChanged = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_create);

		ButterKnife.inject(this);

		initTitle();
		title.setText(R.string.title_activity_create_quan);
		function.setText(R.string.finish);

		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		if (null != groupid && !groupid.equals("")) {
			loadData();
			((TextView) findViewById(R.id.userNameShow))
					.setText(R.string.title_activity_quan_update);
			((Button) findViewById(R.id.buttonCreateQuan))
					.setText(R.string.label_qzupdate);
			findViewById(R.id.linearLayoutManagers).setVisibility(View.VISIBLE);
		}
		pwh = new PopupWindows(QuanCreateActivity.this);
		mIsh = ImageSelectHelper.getIntance(QuanCreateActivity.this,
				R.id.linearLayoutManagerContainer);

		mIsh2 = ImageSelectHelper.getIntance(QuanCreateActivity.this,
				R.id.linearLayoutPicContainer);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonCreateQuan).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						createOrUpdateGroup();
					}
				});
		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createOrUpdateGroup();
			}
		});
		// 展示管理员
		mIsh.getmAddButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.requestFocus();
				mIsh.initParams();
				Intent i = new Intent(QuanCreateActivity.this,
						QuanMngActivity.class);
				i.putExtra("max", 3);
				i.putExtra("groupid", groupid);
				i.putStringArrayListExtra("selected", mSelectlist);
				startActivityForResult(i, MsgTagVO.ADD_USER);
			}
		});
		// 圈子图片
		mIsh2.getmAddButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mIsh2.initParams();
				pwh.showPop(findViewById(R.id.rootLayout));

			}
		});
	}

	private void loadData() {
		String params = ZhuoCommHelper.getUrlGroupDetail() + "?groupid="
				+ groupid;
		mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
				QuanCreateActivity.this, true, new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						QuanCreateActivity.this.finish();
					}
				});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					OnClickListener listener = new OnClickListener() {

						@Override
						public void onClick(View paramView) {
							Intent intent = new Intent();
							if (groupid != null && !groupid.equals("")) {
								intent.putExtra("groupid", groupid);
							} else {
								// ...
							}
							setResult(RESULT_OK, intent);
							QuanCreateActivity.this.finish();
						}
					};
					View v = findViewById(R.id.rootLayout);
					if (groupid != null && !groupid.equals("")) {
						pwh.showPopDlgOne(v, listener, R.string.info67);
					} else {
						pwh.showPopDlgOne(v, listener, R.string.info66);
					}
				}
				break;
			}

			case MsgTagVO.DATA_LOAD: {
				if (msg.obj != null && !msg.obj.equals("")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					QuanVO detail = nljh.parseQuan();
					String name = detail.getGname();
					((EditText) findViewById(R.id.editTextQuanTitle))
							.setText(name);
					String jj = detail.getGintro();
					((EditText) findViewById(R.id.editTextQuanIntro))
							.setText(jj);
					String property = detail.getGproperty();
					if (property.equals(getString(R.string.group_type_9))) {
						((RadioButton) findViewById(R.id.radio0))
								.setChecked(true);
					} else if (property
							.equals(getString(R.string.group_type_8))) {
						((RadioButton) findViewById(R.id.radio1))
								.setChecked(true);
					} else if (property
							.equals(getString(R.string.group_type_10))) {
						((RadioButton) findViewById(R.id.radio2))
								.setChecked(true);
					}
					String headUrl = detail.getGheader();
					mIsh.initParams();
					mIsh2.initParams();
					mIsh2.updateNetworkImage(headUrl, mLoadImage, null);
					List<UserVO> managers = detail.getManagers();
					Map<String, String> netUheader = new HashMap<String, String>();
					Map<String, Integer> defaultUheader = new HashMap<String, Integer>();
					for (int i = 0; i < managers.size(); i++) {
						String header = managers.get(i).getUheader();
						String id = managers.get(i).getUserid();
						mSelectlist.add(id);
						if (header != null && !header.equals("")) {
							netUheader.clear();
							netUheader.put(id, header);
							mIsh.insertNetworkImage(netUheader, mLoadImage,
									removeManagerListener, "card");
						} else {
							defaultUheader.clear();
							defaultUheader.put(id, R.drawable.default_userhead);
							mIsh.insertDefaultImage(defaultUheader,
									removeManagerListener, "card");
						}
					}
					mLoadImage.doTask();
				}
				break;
			}
			}
		}
	};

	private OnClickListener removeManagerListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mSelectlist.remove(((View) v.getParent()).getTag());
			mIsh.removeFromContainer(v);
		}
	};

	// 创建或更新圈子按钮
	private void createOrUpdateGroup() {
		EditText editTextTitle = (EditText) findViewById(R.id.editTextQuanTitle);
		EditText introTextTitle = (EditText) findViewById(R.id.editTextQuanIntro);
		RadioGroup radios = (RadioGroup) findViewById(R.id.radioGroupType);
		String title = editTextTitle.getText().toString();
		String intro = introTextTitle.getText().toString();
		RadioButton radio = (RadioButton) findViewById(radios
				.getCheckedRadioButtonId());
		String gproperty = radio.getText().toString();
		if (title.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info25);
			editTextTitle.requestFocus();
			return;
		}
		String ids = "";
		if (mSelectlist != null && mSelectlist.size() > 0) {
			for (String id : mSelectlist) {
				ids += id + ";";
			}
			ids = ZhuoCommHelper.subLast(ids);
		}
		Map<String, String> files = new HashMap<String, String>();
		if (mHeadChanged) {
			ArrayList<String> temp = mIsh2.getTags();
			for (String path : temp) {
				files.put("gheader", path);
			}
		}
		mConnHelper.createGroup(files, mUIHandler, MsgTagVO.PUB_INFO,
				QuanCreateActivity.this, intro, gproperty, title, "0", groupid,
				ids, true, null, null);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MsgTagVO.ADD_USER) {
			if (resultCode == Activity.RESULT_OK) {
				mSelectlist = data.getStringArrayListExtra("ids");
				ArrayList<String> headers = data
						.getStringArrayListExtra("headers");
				if (mSelectlist != null && mSelectlist.size() > 0) {
					Map<String, String> map = new HashMap<String, String>();
					for (int i = 0; i < mSelectlist.size(); i++) {
						map.put(mSelectlist.get(i), headers.get(i));
					}
					mIsh.insertNetworkImage(map, mLoadImage,
							removeManagerListener, "card");
				}
			}
		} else {
			String filePath = pwh
					.dealPhotoReturn(requestCode, resultCode, data);
			if (filePath != null) {
				try {
					mIsh2.updateImage(filePath);
					mHeadChanged = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
