package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
/**
 * 用户名片详细信息
 * @author lz
 *
 */
public class CardEditActivity extends Activity {
	@InjectView(R.id.buttonBack)
	Button buttonBack;
	@InjectView(R.id.buttonSubmit)
	Button buttonSubmit;
	@InjectView(R.id.ivHead)
	ImageView ivHead;

	@InjectView(R.id.textViewChangeHead)
	TextView textViewChangeHead;

	@InjectView(R.id.ivTDCard)
	ImageView ivTDCard;
	@InjectView(R.id.etSignature)
	EditText etSignature;
	@InjectView(R.id.textViewEditNameShow)
	TextView textViewEditNameShow;
	@InjectView(R.id.textViewEditImagesShow)
	TextView textViewEditImagesShow;// 我的照片
	@InjectView(R.id.textViewEditWorkShow)
	TextView textViewEditWorkShow;// 我的企业
	@InjectView(R.id.textViewEditBirthShow)
	TextView textViewEditBirthShow;// 我的生日
	@InjectView(R.id.textViewEditPlaceShow)
	TextView textViewEditPlaceShow;// 我的位置
	@InjectView(R.id.textViewEditHobbyShow)
	TextView textViewEditHobbyShow;
	@InjectView(R.id.textViewEditZymShow)
	TextView textViewEditZymShow;// 我的价值观与信念(座右铭)
	@InjectView(R.id.textViewEditDreamShow)
	TextView textViewEditDreamShow;
	@InjectView(R.id.textViewEditPhoneShow)
	TextView textViewEditPhoneShow;
	@InjectView(R.id.textViewEditEmailShow)
	TextView textViewEditEmailShow;
	@InjectView(R.id.textViewEditQQShow)
	TextView textViewEditQQShow;
	@InjectView(R.id.textViewEditWeixinShow)
	TextView textViewEditWeixinShow;
	@InjectView(R.id.userNameShow)
	TextView textViewuserNameShow;

	public final static String EDITABLE = "editable";// 阳历生日
	public final static String USERID = "userid";// 阳历生日
	public final static int EDIT_BIRTH = 0;
	public final static String EDIT_BIRTH_STR1 = "birth";// 阳历生日
	public final static String EDIT_BIRTH_STR2 = "birthopen";
	public final static String EDIT_BIRTH_STR3 = "birthdayLunar";// 阴历生日
	public final static String EDIT_BIRTH_STR4 = "constellation";// 星座
	public final static String EDIT_BIRTH_STR5 = "zodiac";// 生肖

	public final static int EDIT_PLACE = 1;
	public final static String EDIT_PLACE_STR1 = "place";// 所在城市
	public final static String EDIT_PLACE_STR2 = "hometown";// 家乡
	public final static String EDIT_PLACE_STR3 = "othercity";// 来往城市
	public final static int EDIT_DREAM = 2;
	public final static String EDIT_DREAM_STR = "dream";
	public final static int EDIT_EMAIL = 3;
	public final static String EDIT_EMAIL_STR1 = "email";
	public final static String EDIT_EMAIL_STR2 = "emailopen";

	public final static int EDIT_QQ = 14;
	public final static String EDIT_QQ_STR1 = "qq";
	public final static String EDIT_QQ_STR2 = "qqopen";
	public final static int EDIT_WEIXIN = 15;
	public final static String EDIT_WEIXIN_STR1 = "weixin";
	public final static String EDIT_WEIXIN_STR2 = "weixinopen";

	public final static int EDIT_HOBBY = 4;
	public final static String EDIT_HOBBY_STR = "hobby";
	public final static int EDIT_MOTTO = 5;
	public final static String EDIT_MOTTO_STR = "motto";
	public final static int EDIT_FIELD = 6;
	public final static String EDIT_FIELD_STR = "field";
	public final static int EDIT_IMAGE = 7;
	public final static String EDIT_IMAGE_STR1 = "image";
	public final static String EDIT_IMAGE_STR2 = "ids";
	public final static String EDIT_GONG_STR = "gong";
	public final static String EDIT_XU_STR = "xu";
	public final static String EDIT_RES_STR1 = "type";
	public final static String EDIT_RES_STR2 = "userid";
	public final static String EDIT_LEVEL_STR = "level";
	public final static int EDIT_WEBSITE = 8;
	public final static String EDIT_WEBSITE_STR = "website";
	public final static int EDIT_PHONE = 9;
	public final static String EDIT_PHONE_STR1 = "phone";
	public final static String EDIT_PHONE_STR2 = "phoneopen";
	public final static int EDIT_NAME = 10;
	public final static String EDIT_NAME_STR1 = "name";
	public final static String EDIT_NAME_STR2 = "sex";
	public final static String EDIT_NAME_STR3 = "ismarray";
	public final static int EDIT_PRODUCT = 11;
	public final static String EDIT_PRODUCT_STR = "products";
	public final static int EDIT_WORK = 12;
	public final static String EDIT_WORK_STR1 = "company";
	public final static String EDIT_WORK_STR2 = "post";
	public final static String EDIT_WORK_STR3 = "isworking";
	public final static String EDIT_WORK_STR4 = "isisentrepreneurship";
	public final static int EDIT_CUSTOMER = 13;
	public final static String EDIT_CUSTOMER_STR = "customer";
	private ArrayList<String> localImages = new ArrayList<String>();
	private ConnHelper mConnHelper = null;
	public final static String LOCAL_IMAGE = "localImage";
	public final static String photosStr = "";
	private PopupWindows pwh = null;
	private boolean edit = false;
	boolean isEditable = true;
	UserNewVO userInfo;
	private LoadImage mLoadImage = new LoadImage();
	String guestId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_edit);
		ButterKnife.inject(this);
		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(CardEditActivity.this);
		String userid = ResHelper.getInstance(getApplicationContext())
				.getUserid();
		buttonSubmit.setVisibility(View.GONE);
		guestId = getIntent().getStringExtra("id");
		if (guestId != null && guestId != userid) {
			isEditable = false;
			textViewuserNameShow.setText(getString(R.string.view_into));
		} else {
			textViewuserNameShow.setText(getString(R.string.edit_info));
		}
		buttonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CardEditActivity.this.finish();
			}
		});
		if (isEditable)
			initClick();
		else {
			etSignature.setEnabled(false);
			etSignature.setFocusable(false);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(etSignature.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
		loadInfo();
		super.onResume();
	}

	void updateInfo() {
		Map<String, String> files = new HashMap<String, String>();
		int imgcnt = localImages.size();
		if (imgcnt > 0) {
			if (localImages.size() - 1 > 0) {
				for (int i = 0; i < localImages.size() - 1; i++) {
					files.put("img" + i, localImages.get(i));
				}
			}
			files.put("uheader", localImages.get(localImages.size() - 1));
			imgcnt = imgcnt - 1;
		} else {
		}

		userInfo.setSignature(etSignature.getText().toString());
		mConnHelper.modifyUserInfo(mUIHandler, MsgTagVO.PUB_INFO, userInfo);
	}

	private void initClick() {
		if (!isEditable) {
			textViewChangeHead.setVisibility(View.GONE);
			etSignature.setEnabled(false);
		} else {

			etSignature.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						String text = v.getText().toString();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						UserNewVO userInfo = new UserNewVO();
						userInfo.setSignature(text);
						ConnHelper.getInstance(getApplicationContext())
								.modifyUserInfo(mUIHandler, MsgTagVO.PUB_INFO,
										userInfo);
					}
					return false;
				}
			});

			textViewChangeHead.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					pwh.showPop(findViewById(R.id.rootLayout));
				}
			});
			etSignature.addTextChangedListener(new TextWatcher() {
				private CharSequence temp;
				private int selectionStart;
				private int selectionEnd;

				@Override
				public void beforeTextChanged(CharSequence s, int arg1,
						int arg2, int arg3) {
					temp = s;
				}

				@Override
				public void onTextChanged(CharSequence s, int arg1, int arg2,
						int arg3) {
					edit = true;
				}

				@Override
				public void afterTextChanged(Editable s) {
					selectionStart = etSignature.getSelectionStart();
					selectionEnd = etSignature.getSelectionEnd();

					if (temp.length() > 15) {
						Toast.makeText(CardEditActivity.this,
								R.string.edit_nsignature_limit,
								Toast.LENGTH_SHORT).show();
						s.delete(selectionStart - 1, selectionEnd);
						int tempSelection = selectionStart;
						etSignature.setText(s);
						etSignature.setSelection(tempSelection);

					}
				}
			});
			buttonSubmit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (edit) {
						updateInfo();
					} else {
						CardEditActivity.this.finish();
					}
				}
			});
			textViewEditBirthShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserBirthActivity.class);

						i.putExtra(EDIT_BIRTH_STR1, userInfo.getBirthday());// 阳历生日
						i.putExtra(EDIT_BIRTH_STR2,
								userInfo.getIsBirthdayOpen());
						i.putExtra(EDIT_BIRTH_STR3, userInfo.getBirthdayLunar());
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			textViewEditDreamShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserDreamActivity.class);
						i.putExtra(EDIT_DREAM_STR, userInfo.getDream());
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			textViewEditEmailShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserEmailActivity.class);
						i.putExtra(EDIT_EMAIL_STR1, userInfo.getEmail());
						i.putExtra(EDIT_EMAIL_STR2, userInfo.getIsEmailOpen());
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});

			textViewEditQQShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserQQActivity.class);
						String qq = userInfo.getQq();
						int qqopen = userInfo.getIsQqOpen();
						if (qq != null)
							i.putExtra(EDIT_QQ_STR1, qq);

						i.putExtra(EDIT_QQ_STR2, qqopen);
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			textViewEditWeixinShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserWeiXinActivity.class);
						String weixin = userInfo.getWeixin();
						int weixinopen = userInfo.getIsWeixinOpen();
						if (weixin != null)
							i.putExtra(EDIT_WEIXIN_STR1, weixin);
						i.putExtra(EDIT_WEIXIN_STR2, weixinopen + "");
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});

			textViewEditHobbyShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserHobbyActivity.class);
						i.putExtra(EDIT_HOBBY_STR, userInfo.getHobby());
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			textViewEditZymShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserMottoActivity.class);
						i.putExtra(EDIT_MOTTO_STR, userInfo.getFaith());
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			// 二维码
			ivTDCard.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 显示二维码图片
				}
			});
			textViewEditPhoneShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserPhoneActivity.class);
						i.putExtra(EDIT_PHONE_STR1, userInfo.getPhone());
						i.putExtra(EDIT_PHONE_STR2, userInfo.getIsPhoneOpen());
						startActivity(i);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
		}

		

		textViewEditPlaceShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userInfo != null) {
					Intent i = new Intent(CardEditActivity.this,
							CardAddUserCityActivity.class);
					i.putExtra(EDITABLE, isEditable);// 是否可以编辑
					i.putExtra(EDIT_PLACE_STR1, userInfo.getCity());
					i.putExtra(EDIT_PLACE_STR2, userInfo.getHometown());
					i.putExtra(EDIT_PLACE_STR3, userInfo.getTravelCity());
					startActivity(i);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.error12);
				}
			}
		});

		textViewEditImagesShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userInfo != null) {
					ArrayList<String> images = new ArrayList<String>();
					if (userInfo.getPhoto() != null) {
						for (int i = 0; i < userInfo.getPhoto().size(); i++) {
							PicNewVO pic = userInfo.getPhoto().get(
									userInfo.getPhoto().size() - 1 - i);
							images.add(pic.getPic());
						}
					}
					Intent i = new Intent(CardEditActivity.this,
							CardAddUserImageActivity.class);
					i.putExtra(EDITABLE, isEditable);
					i.putStringArrayListExtra(EDIT_IMAGE_STR1, images);
					startActivity(i);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.error12);
				}
			}
		});

		textViewEditNameShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userInfo != null) {
					Intent i = new Intent(CardEditActivity.this,
							CardAddUserNameActivity.class);
					i.putExtra(EDITABLE, isEditable);
					i.putExtra(EDIT_NAME_STR1, userInfo.getName());
					i.putExtra(EDIT_NAME_STR2, userInfo.getGender());
					i.putExtra(EDIT_NAME_STR3, userInfo.getMarried());
					startActivity(i);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.error12);
				}
			}
		});

		textViewEditWorkShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userInfo != null) {
					Intent i = new Intent(CardEditActivity.this,
							CompanyDetailActivity.class);
					i.putExtra(EDITABLE, isEditable);
					i.putExtra(USERID, userInfo.getUserid());
					startActivity(i);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.error12);
				}
			}
		});
	}

	String getPhotoStr(ArrayList<PicNewVO> pics) {
		if (pics == null || pics.size() < 1) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (PicNewVO item : pics) {
			sb.append(item.getPic());
			sb.append(",");
		}
		return sb.toString();
	}

	void fillInfo() {
		if (userInfo == null)
			return;

		mLoadImage.beginLoad(userInfo.getUheader(), ivHead);

		int placeId = userInfo.getCity();
		String placeText = getString(R.string.nofill);
		if (mConnHelper.getCitys() != null && placeId >= 1
				&& placeId <= mConnHelper.getCitys().size())
			placeText = mConnHelper.getCitys().get(placeId - 1).getCityName();
		textViewEditPlaceShow.setText(placeText);
		fillText(textViewEditNameShow, userInfo.getName(), 0);
		List<PicNewVO> images = userInfo.getPhoto();
		if (images != null && images.size() > 0) {
			((TextView) findViewById(R.id.textViewEditImagesShow))
					.setText(getString(R.string.mp_has) + images.size()
							+ getString(R.string.mp_imgasall));
		} else
			((TextView) findViewById(R.id.textViewEditImagesShow))
					.setText(getString(R.string.txt_uncompleted));
		fillText(etSignature, userInfo.getSignature(), 0);
		fillText(textViewEditDreamShow, userInfo.getDream(), 0);
		fillText(textViewEditZymShow, userInfo.getFaith(), 0);
		fillText(textViewEditHobbyShow, userInfo.getHobby(), 0);
		String birthday = userInfo.getBirthday();
		if (birthday == null)
			birthday = userInfo.getBirthdayLunar();
		fillText(textViewEditBirthShow, birthday, userInfo.getIsBirthdayOpen());

		fillText(textViewEditPhoneShow, userInfo.getPhone(),
				userInfo.getIsPhoneOpen());
		fillText(textViewEditEmailShow, userInfo.getEmail(),
				userInfo.getIsEmailOpen());
		fillText(textViewEditQQShow, userInfo.getQq(), userInfo.getIsQqOpen());
		fillText(textViewEditWeixinShow, userInfo.getWeixin(),
				userInfo.getIsWeixinOpen());

	}

	void fillText(TextView tv, String text, int isOpen) {
		if (tv == null)
			return;
		if (text == null || text.trim().toString().equals(""))
			tv.setText(getString(R.string.nofill));
		else {
			if (isOpen == 1 && userInfo.getRelation() != 1
					&& userInfo.getRelation() != 2)
				tv.setText(getString(R.string.secret));// 只对好友公开
			else
				tv.setText(text);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					userInfo = nljh.parseNewUser();
					fillInfo();
				}
				break;
			}
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_success);
				} else
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_failed);
			}
				break;
			}
		}
	};

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			if (mConnHelper != null)
				mConnHelper
						.getUserInfo(mUIHandler, MsgTagVO.DATA_LOAD, guestId);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			CardEditActivity.this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
