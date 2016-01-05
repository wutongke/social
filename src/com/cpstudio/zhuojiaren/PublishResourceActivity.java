package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.helper.BaiduLocationHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.GXTypeCodeData;
import com.cpstudio.zhuojiaren.model.GXTypeItemVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.gtype;
import com.cpstudio.zhuojiaren.ui.BaseActivity;
import com.cpstudio.zhuojiaren.ui.MyFriendActivity;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudio.zhuojiaren.widget.TwoLeverChooseDialog;

public class PublishResourceActivity extends BaseActivity {
	@InjectView(R.id.btnAddContactPeople)
	Button btnAddContacts;
	@InjectView(R.id.linearLayoutContacts)
	LinearLayout contactsContainer;// 用于添加联系人的layout

	private ArrayList<EditText> peopleList = new ArrayList<EditText>();
	private ArrayList<EditText> phoneList = new ArrayList<EditText>();
	private PopupWindows pwh = null;
	private ImageSelectHelper mIsh = null;
	private String mType = "demand";
	private String mCategory = "1";
	private String mLocation = "";
	private ConnHelper mConnHelper = null;
	private BaiduLocationHelper locationHelper = null;
	// 供需类型（1-5为资源信息，5-00为需求信息。见 基础-获取基础数据编码 接口文档
	int typecode;
	List<List<String>> subStrings;
	List<gtype> gtypes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_gong_xu_new);

		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.title_pub_gxxq);

		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(PublishResourceActivity.this);
		mIsh = ImageSelectHelper.getIntance(PublishResourceActivity.this,
				R.id.linearLayoutPicContainer);
		initClick();
		locationHelper = new BaiduLocationHelper(getApplicationContext(),
				mUIHandler, MsgTagVO.UPDATE_LOCAL);
		getCodedData();
	}

	void getCodedData() {
		GXTypeCodeData baseCodeData = mConnHelper.getGxTypeCodeDataSet();
		gtypes = new ArrayList<gtype>();
		if (baseCodeData != null) {
			// gtypes = baseCodeData.getSdtype();
			List<String> zyList = new ArrayList<String>(), xqList = new ArrayList<String>();

			for (GXTypeItemVO item : baseCodeData.getSupply()) {
				zyList.add(item.getTitle());
				if (item.getSdtype() != null && item.getSdtype().size() > 0)
					gtypes.add(new gtype(item.getSdtype().get(0).getId(), item
							.getTitle()));

			}
			for (GXTypeItemVO item : baseCodeData.getDemand()) {
				xqList.add(item.getTitle());
				if (item.getSdtype() != null && item.getSdtype().size() > 0)
					gtypes.add(new gtype(item.getSdtype().get(0).getId(), item
							.getTitle()));

			}
			subStrings = new ArrayList<List<String>>();
			subStrings.add(zyList);
			subStrings.add(xqList);
		}
	}

	int getCodeByName(String name) {
		if (gtypes == null)
			return 0;
		for (int i = 0; i < gtypes.size(); i++)
			if (name.equals(gtypes.get(i).getContent()))
				return gtypes.get(i).getId();
		return 0;
	}

	@Override
	protected void onDestroy() {
		if (locationHelper != null) {
			locationHelper.stopLocation();
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		if (locationHelper != null) {
			locationHelper.stopLocation();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (locationHelper != null) {
			locationHelper.startLocation();
		}
		super.onResume();
	}

	private void initClick() {
		// findViewById(R.id.buttonBack).setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// pwh.showPopDlg(findViewById(R.id.rootLayout),
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// PublishResourceActivity.this.finish();
		// }
		// }, null, R.string.info69);
		// }
		// });
		function.setText(R.string.label_publish);
		function.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				publish();
			}
		});
		btnAddContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 添加联系人
				final AlertDialog.Builder alterDialog = new AlertDialog.Builder(
						PublishResourceActivity.this);
				alterDialog.setMessage("是否从好友中选择？");
				alterDialog.setCancelable(true);
				alterDialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent i = new Intent(
										PublishResourceActivity.this,
										MyFriendActivity.class);
								i.putExtra("type", 100);
								startActivityForResult(i, 100);
							}
						});
				alterDialog.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								addContactPeopel(null, null);
							}
						});
				alterDialog.show();

				// contactsContainer
			}
		});
		// findViewById(R.id.buttonSubmit).setOnClickListener(
		// new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// publish();
		// }
		// });

		findViewById(R.id.relativeLayoutchoiceType).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {

						int type1 = R.array.share_gx;
						final TwoLeverChooseDialog typeChoose = new TwoLeverChooseDialog(
								PublishResourceActivity.this,
								AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, "分享资源",
								"商业资源", type1, subStrings);
						typeChoose.setButton(DialogInterface.BUTTON_POSITIVE,
								"确定", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										String content = typeChoose
												.getSelectedContent().getText()
												.toString();
										String array[] = content.split(" ");
										mType = array[0];
										mCategory = array[1];
										typecode = getCodeByName(mCategory);
										((TextView) findViewById(R.id.editTextchoiceType))
												.setText(content);
									}
								});
						typeChoose.setButton(DialogInterface.BUTTON_NEGATIVE,
								"取消", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});
						typeChoose.setTitle("选择类型");
						typeChoose.show();
					}
				});

		// String[] type1 = new String[] {
		// getString(R.string.share_resources),
		// getString(R.string.share_need) };
		// String[] temp = getResources().getStringArray(
		// R.array.share_resource_items);
		// String[] type2 = new String[temp.length - 1];
		// for (int i = 1; i < temp.length; i++) {
		// type2[i - 1] = temp[i];
		// }
		// pwh.showWheelPop(
		// findViewById(R.id.imageViewchoiceType), type1,
		// type2, new WheelOKClick() {
		//
		// @Override
		// public String onClick(String[] selected,
		// int[] selectedId) {
		// ((TextView) findViewById(R.id.editTextchoiceType))
		// .setText(selected[0]
		// + selected[1]);
		// mType = ZhuoCommHelper
		// .transferMsgStringToType(
		// selected[0],
		// PublishResourceActivity.this);
		// mCategory = ZhuoCommHelper
		// .transferMsgStringToCategory(
		// selected[1],
		// PublishResourceActivity.this);
		// return null;
		// }
		// });
		// }
		// });
		mIsh.getmAddButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mIsh.initParams();
				if (mIsh.getTags() != null && mIsh.getTags().size() < 9) {
					pwh.showPop(findViewById(R.id.rootLayout));
				} else {
					mIsh.getmAddButton().setVisibility(View.GONE);
					CommonUtil.displayToast(getApplicationContext(),
							R.string.error24);
				}
			}
		});
		findViewById(R.id.textViewPosInfo).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						((TextView) v).setText("加载位置中...");
						if (locationHelper != null) {
							locationHelper.stopLocation();
							locationHelper.startLocation();
						}
					}
				});
	}

	private void addContactPeopel(String nameVal, String phoneVal) {
		LayoutInflater infloater = PublishResourceActivity.this
				.getLayoutInflater();
		View view = infloater.inflate(R.layout.item_add_people, null);
		EditText people = (EditText) view.findViewById(R.id.idp_people);
		EditText phone = (EditText) view.findViewById(R.id.idp_phone);
		contactsContainer.addView(view);
		peopleList.add(people);
		phoneList.add(phone);
		if (nameVal != null)
			people.setText(nameVal);
		if (phoneVal != null)
			people.setText(phoneVal);
	}

	private void publish() {
		EditText titleEditText = (EditText) findViewById(R.id.editTextchoiceTitle);
		String title = titleEditText.getText().toString();
		EditText typeEditText = (EditText) findViewById(R.id.editTextchoiceType);
		String type = typeEditText.getText().toString();
		EditText contentEditText = (EditText) findViewById(R.id.editTextchoiceContent);
		String content = contentEditText.getText().toString();
		String tag = ((TextView) findViewById(R.id.editTextchoiceTag))
				.getText().toString();
		if (title.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info22);
			titleEditText.requestFocus();
			return;
		}
		if (type.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info23);
			return;
		}
		if (content.trim().equals("")) {
			pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
					R.string.info24);
			contentEditText.requestFocus();
			return;
		}
		String imgCnt = "0";
		if (mIsh.getTags() != null) {
			imgCnt = mIsh.getTags().size() + "";
		}
		// mConnHelper.pubZhuoInfo(mIsh.getTags(), mUIHandler,
		// MsgTagVO.PUB_INFO,
		// PublishResourceActivity.this, content, tag, mLocation, imgCnt,
		// mType, mCategory, title, true, null, null);
		mConnHelper.pubGongxu(PublishResourceActivity.this, mUIHandler,
				MsgTagVO.PUB_INFO, typecode, title, content, mIsh.getTags(),
				tag, getItems(peopleList), getItems(phoneList));
	}

	String getItems(List<EditText> list) {
		if (list == null || list.size() == 0)
			return "none";
		if (list.size() == 1)
			return list.get(0).getText().toString().trim();
		String str = new String();
		for (EditText name : list) {
			str += name.getText().toString().trim() + ",";
		}
		return str;
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (msg.obj != null && !msg.obj.equals("")) {
					if (JsonHandler.checkResult((String) msg.obj)) {
						OnClickListener listener = new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								setResult(RESULT_OK);
								PublishResourceActivity.this.finish();
							}
						};
						pwh.showPopDlgOne(findViewById(R.id.rootLayout),
								listener, R.string.info62);
					}
				}
				break;
			}
			case MsgTagVO.UPDATE_LOCAL: {
				String locationinfo = (String) msg.obj;
				if (null != locationinfo && !locationinfo.trim().equals("")) {
					mLocation = locationinfo;
					((TextView) findViewById(R.id.textViewPosInfo))
							.setText(locationinfo);
				} else {
					((TextView) findViewById(R.id.textViewPosInfo))
							.setText(getString(R.string.error10));
				}
				break;
			}
			default: {

			}

			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 100) {
				addContactPeopel(data.getStringExtra("name"),
						data.getStringExtra("phone"));
			} else {
				String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
						data, false);
				if (filePath != null) {
					try {
						mIsh.insertLocalImage(filePath);
						if (mIsh.getTags() != null
								&& mIsh.getTags().size() == 9) {
							mIsh.getmAddButton().setVisibility(View.GONE);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					PublishResourceActivity.this.finish();
				}
			};
			pwh.showPopDlg(findViewById(R.id.rootLayout), listener, null,
					R.string.info69);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
