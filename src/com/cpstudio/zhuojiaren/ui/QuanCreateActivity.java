package com.cpstudio.zhuojiaren.ui;

import io.rong.app.DemoContext;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.RongIMClient.OperationCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.QuanMngActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.facade.GroupFacade;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.Province;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudui.zhuojiaren.lz.ChangeBackgroundActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
	RadioButton addQuanRight1;
	@InjectView(R.id.add_quanzi_right2)
	RadioButton addQuanRight2;
	@InjectView(R.id.visite_quanzi_right)
	RadioGroup QuanRight;
	@InjectView(R.id.visite_quanzi_right1)
	RadioButton visiteQuanRight1;
	@InjectView(R.id.visite_quanzi_right2)
	RadioButton visiteQuanRight2;
	private PopupWindows pwh = null;
	// 管理员头像
	private ImageSelectHelper mIsh = null;
	// 圈子图片
	private ImageSelectHelper mIsh2 = null;
	private ArrayList<String> mSelectlist = new ArrayList<String>();
	private AppClientLef mConnHelper = null;
	private String groupid = null;
	private String groupname = null;
	private LoadImage mLoadImage = LoadImage.getInstance();
	private boolean mHeadChanged = false;
	private Context mContext;
	private int typeQuanzi = 0;
	private String[] quanziType;
	List<City> cityList;

	// 城市编码
	private String locateCode;
	// 加入权限
	private String addRight = "0";
	// 查看权限
	private String seeRight = "0";
	// 选择图片
	AlertDialog adl;
	QuanVO quanNew = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quan_create);

		ButterKnife.inject(this);
		mContext = this;
		mConnHelper = AppClientLef.getInstance(getApplicationContext());
		// 圈子类型
		quanziType = getResources().getStringArray(R.array.quanzi_type);
		initTitle();
		title.setText(R.string.title_activity_create_quan);
		function.setText(R.string.finish);
		mConnHelper.getCitys(mUIHandler, MsgTagVO.DATA_OTHER,
				(Activity) mContext, true, null, null);

		mIsh = ImageSelectHelper.getIntance(QuanCreateActivity.this,
				R.id.linearLayoutManagerContainer);

		mIsh2 = ImageSelectHelper.getIntance(QuanCreateActivity.this,
				R.id.linearLayoutPicContainer);
		mIsh.initParams();
		mIsh2.initParams();
		Intent i = getIntent();
		groupid = i.getStringExtra("groupid");
		if (null != groupid && !groupid.equals("")) {
			loadData();
			title.setText(R.string.title_activity_quan_update);
		}
		pwh = new PopupWindows(QuanCreateActivity.this);

		initClick();
	}

	private void initClick() {

		/**
		 * 选择类型
		 */
		quanTypeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub
				new AlertDialog.Builder(mContext,
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle("选择圈子类型")
						.setIcon(R.drawable.ico_syzy)
						.setSingleChoiceItems(quanziType, 0,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										typeQuanzi = which;
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										quanTypeView
												.setText(quanziType[typeQuanzi]);
									}
								}).setNegativeButton("取消", null).create()
						.show();

			}
		});
		quanLocationView.setOnClickListener(new OnClickListener() {

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
								quanLocationView.setText(placeChoose.getPlace()
										.getText().toString());
								locateCode = String.valueOf(placeChoose
										.getCityCode());
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
				placeChoose.setTitle("选择活动地点");
				placeChoose.show();
			}
		});
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
				// pwh.showPop(findViewById(R.id.rootLayout));
				LinearLayout ll = (LinearLayout) getLayoutInflater().inflate(
						R.layout.dialog_choose_pictures, null);
				ll.findViewById(R.id.dcp_album).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(
										Intent.ACTION_GET_CONTENT);
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setType("image/*");
								((Activity) mContext).startActivityForResult(
										Intent.createChooser(intent, mContext
												.getString(R.string.info0)),
										MsgTagVO.SELECT_PICTURE);
							}
						});
				ll.findViewById(R.id.dcp_camera).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String state = Environment
										.getExternalStorageState();
								if (state.equals(Environment.MEDIA_MOUNTED)) {
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											ResHelper.getInstance(mContext)
													.getCaptrueUri());
									((Activity) mContext)
											.startActivityForResult(intent,
													MsgTagVO.SELECT_CAMER);
								} else {
									CommonUtil.displayToast(mContext,
											R.string.error2);
								}
							}
						});
				ll.findViewById(R.id.dcp_zhuo).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								((Activity) mContext)
										.startActivityForResult(
												new Intent(
														mContext,
														ChangeBackgroundActivity.class),
												MsgTagVO.ZHUOMAI_PIC);
							}
						});
				adl = new AlertDialog.Builder(QuanCreateActivity.this).create();
				adl.setCanceledOnTouchOutside(true);// 设置dialog外面点击则消失
				Window w = adl.getWindow();
				WindowManager.LayoutParams lp = w.getAttributes();
				adl.onWindowAttributesChanged(lp);
				lp.x = 20;
				lp.y = 0;
				adl.show();
				adl.getWindow().setContentView(ll);
			}
		});
		addQuanRight1.setChecked(true);
		visiteQuanRight1.setChecked(true);
		addQuanRight.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == addQuanRight1.getId())
					addRight = "0";
				else
					addRight = "1";
			}
		});
		QuanRight.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == visiteQuanRight1.getId())
					seeRight = "0";
				else
					seeRight = "1";
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (adl != null && adl.isShowing()) {
			adl.dismiss();
		}
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			// QuanVO quan = mFacade.getById(groupid);
			// if (quan == null) {
			// CommonUtil.displayToast(getApplicationContext(),
			// R.string.error0);
			// } else {
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			// msg.obj = quan;
			// msg.sendToTarget();
			// }
		} else {
			ZhuoConnHelper.getInstance(getApplicationContext()).getQuanInfo(
					mUIHandler, MsgTagVO.DATA_LOAD, groupid);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_REFRESH:
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {

					OnClickListener listener = new OnClickListener() {

						@Override
						public void onClick(View paramView) {

							if (groupid != null && !groupid.equals("")) {

							} else {

							}

						}
					};
					View v = findViewById(R.id.rootLayout);

					Intent intent = new Intent();

					if (groupid != null && !groupid.equals("")) {
						pwh.showPopDlgOne(v, null, R.string.info67);
					} else {
						ResultVO result = JsonHandler
								.parseResult((String) msg.obj);
						final String id = result.getData();
						quanNew.setGroupid(id);

						intent.putExtra("groupid", id);

						// 调用融云，自己加入该
						joinGroup(id, groupname);
						pwh.showPopDlgOne(v, null, R.string.info66);
					}
					setResult(RESULT_OK, intent);
					QuanCreateActivity.this.finish();
				}
				break;
			}
			case MsgTagVO.DATA_OTHER:
				ResultVO res;
				if (JsonHandler.checkResult((String) msg.obj, mContext)) {
					res = JsonHandler.parseResult((String) msg.obj);
					mConnHelper.saveObject((String) msg.obj, "citys");
				} else {
					return;
				}
				String data = res.getData();
				Type listType = new TypeToken<ArrayList<Province>>() {
				}.getType();
				Gson gson = new Gson();
				ArrayList<Province> list = gson.fromJson(data, listType);
				if (cityList == null)
					cityList = new ArrayList<City>();
				for (Province temp : list) {
					cityList.addAll(temp.getCitys());
				}
				if (cityList != null && locateCode != null) {
					int index = Integer.parseInt(locateCode);
					if (cityList != null)
						quanLocationView.setText(cityList.get(index)
								.getCityName());
				}
				break;
			case MsgTagVO.DATA_LOAD: {
				if (msg.obj != null && !msg.obj.equals("")) {
					QuanVO detail = null;
					if (msg.obj instanceof QuanVO) {
						detail = (QuanVO) msg.obj;
					} else {
						JsonHandler nljh = new JsonHandler((String) msg.obj,
								getApplicationContext());
						detail = nljh.parseQuan();
						if (null != detail) {
							// 是否需要保存到本地
							// mFacade.saveOrUpdate(etail);
						}
					}
					if (null != detail) {
						typeQuanzi = detail.getGtype();
						quanTypeView.setText(quanziType[typeQuanzi]);
						locateCode = detail.getCity() + "";
						if (cityList != null)
							quanLocationView.setText(cityList.get(
									detail.getCity()).getCityName());
						((EditText) findViewById(R.id.editTextQuanTitle))
								.setText(detail.getGname());
						((EditText) findViewById(R.id.editTextQuanIntro))
								.setText(detail.getGintro());
						((EditText) findViewById(R.id.editTextQuanPub))
								.setText(detail.getGpub());
						if (detail.getAccesspms() == 0)
							visiteQuanRight1.setChecked(true);
						else
							visiteQuanRight2.setChecked(true);

						if (detail.getFollowpms() == 0)
							addQuanRight1.setChecked(true);
						else
							addQuanRight2.setChecked(true);
						// 头像显示：：：？？？
//						String head = detail.getGheader();
//
//						if (head != null) {
//							try {
//								mIsh2.updateNetworkImage(head,mLoadImage,"mew");
//								mHeadChanged = false;
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//						}
					}
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
		groupname = title;
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
		String pub = ((EditText) findViewById(R.id.editTextQuanPub)).getText()
				.toString().trim();
		// Map<String, String> files = new HashMap<String, String>();
		// if (mHeadChanged) {
		// ArrayList<String> temp = mIsh2.getTags();
		// for (String path : temp) {
		// files.put("gheader", path);
		// }
		// }

		if (null != groupid && !groupid.equals(""))
			ZhuoConnHelper.getInstance(getApplicationContext())
					.modifyGroupInfo(mUIHandler, MsgTagVO.DATA_REFRESH,
							groupid, title, intro, String.valueOf(typeQuanzi),
							locateCode, addRight, seeRight, pub,
							mIsh2.getTags());
		else
		// 加上pub
		{
			quanNew = new QuanVO();
			quanNew.setGname(title);
			quanNew.setCity(Integer.parseInt(locateCode));
			quanNew.setGintro(intro);
			quanNew.setGtype(typeQuanzi);
			if (mIsh2.getTags() != null && mIsh2.getTags().size() > 0)
				quanNew.setGheader(mIsh2.getTags().get(0));
			mConnHelper.createQuan(QuanCreateActivity.this, mUIHandler,
					MsgTagVO.PUB_INFO, title, intro,
					String.valueOf(typeQuanzi), locateCode, addRight, seeRight,
					mIsh2.getTags());
		}
	}

	private void joinGroup(String groupid, String groupname) {
		// TODO Auto-generated method stub
		/**
		 * 加入群组。
		 * 
		 * @param groupId
		 *            群组 Id。
		 * @param groupName
		 *            群组名称。
		 * @param callback
		 *            加入群组状态的回调。
		 */
		RongIM.getInstance().getRongIMClient()
				.joinGroup(groupid, groupname, new OperationCallback() {

					@Override
					public void onSuccess() {
						CommonUtil.displayToast(QuanCreateActivity.this,
								"向融云，加入圈子成功");
						// 异步更新圈子信息到数据库
						if (quanNew != null) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-genrated method stub
									GroupFacade mgfcade = DemoContext
											.getInstance(
													getApplicationContext())
											.getmGroupInfoDao();
									mgfcade.add(quanNew);
								}
							}).start();
						}
					}

					@Override
					public void onError(ErrorCode errorCode) {
						CommonUtil.displayToast(QuanCreateActivity.this,
								"向融云，加入圈子失败");
					}
				});
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
			if (requestCode == MsgTagVO.ZHUOMAI_PIC) {
				if (resultCode == RESULT_OK) {

				}
				return;
			}
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
