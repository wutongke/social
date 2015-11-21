package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.cpstudui.zhuojiaren.lz.CompanyDetailActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

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

	// lz add
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
	private ZhuoConnHelper mConnHelper = null;
	private ArrayList<String> dreamsList = new ArrayList<String>();
	public final static String LOCAL_IMAGE = "localImage";
	public final static String photosStr = "";
	private PopupWindows pwh = null;
	private UserFacade mFacade = null;
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
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new UserFacade(getApplicationContext());
		pwh = new PopupWindows(CardEditActivity.this);
		String userid = ResHelper.getInstance(getApplicationContext())
				.getUserid();
		buttonSubmit.setVisibility(View.GONE);
		guestId = getIntent().getStringExtra("id");
		if (guestId != null && guestId != userid) {
			isEditable = false;
			setTitle(getString(R.string.view_into));
		} else {
			setTitle(getString(R.string.edit_info));
		}
		initClick();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
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
			// List<PicVO> picsNow = mUser.getPics();
			// if(picsNow.size() > 0){
			// if(!mUser.getUheader().equals(picsNow.get(picsNow.size()
			// - 1).getUrl())){
			// files.put("uheader",
			// picsNow.get(picsNow.size() - 1).getUrl());
			// }
			// }
			// delete first Image
		}

		userInfo.setSignature(etSignature.getText().toString());
		mConnHelper.modifyUserInfo(mUIHandler, MsgTagVO.PUB_INFO, userInfo);
	}

	private void initClick() {
		if (!isEditable) {
			textViewChangeHead.setVisibility(View.GONE);
			etSignature.setEnabled(false);
		} else {
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
						startActivityForResult(i, EDIT_BIRTH);
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
						startActivityForResult(i, EDIT_DREAM);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			// 此处要改为两个选项：对好友公开和对所有人公开。。
			textViewEditEmailShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserEmailActivity.class);
						i.putExtra(EDIT_EMAIL_STR1, userInfo.getEmail());
						i.putExtra(EDIT_EMAIL_STR2, userInfo.getIsEmailOpen());
						startActivityForResult(i, EDIT_EMAIL);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});

			// lz add
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
						startActivityForResult(i, EDIT_QQ);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			// lz add
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
						startActivityForResult(i, EDIT_WEIXIN);
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
						startActivityForResult(i, EDIT_HOBBY);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
			// 价值观与信念，接口暂时无此字段
			textViewEditZymShow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (userInfo != null) {
						Intent i = new Intent(CardEditActivity.this,
								CardAddUserMottoActivity.class);
						i.putExtra(EDIT_MOTTO_STR, userInfo.getFaith());
						startActivityForResult(i, EDIT_MOTTO);
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
						// 此处要改为两个选项：对好友公开和对所有人公开。。
						i.putExtra(EDIT_PHONE_STR2, userInfo.getIsPhoneOpen());
						startActivityForResult(i, EDIT_PHONE);
					} else {
						CommonUtil.displayToast(getApplicationContext(),
								R.string.error12);
					}
				}
			});
		}

		buttonBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CardEditActivity.this.finish();
			}
		});

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
					startActivityForResult(i, EDIT_PLACE);
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
					ArrayList<String> ids = new ArrayList<String>();
					if (userInfo.getPhoto() != null) {
						String headUrl = null;
						String headId = null;
						for (int i = 0; i < userInfo.getPhoto().size(); i++) {
							PicNewVO pic = userInfo.getPhoto().get(
									userInfo.getPhoto().size() - 1 - i);
							if (pic.getPic().equals(userInfo.getUheader())) {
								headUrl = pic.getPic();
								headId = pic.getPic();
							} else {
								images.add(pic.getPic());
								ids.add(pic.getPic());
							}
						}
						if (headId != null) {
							images.add(headUrl);
							ids.add(headId);
						}
					}
					for (String localImage : localImages) {
						images.add(localImage);
						ids.add(LOCAL_IMAGE);
					}
					Intent i = new Intent(CardEditActivity.this,
							CardAddUserImageActivity.class);
					i.putExtra(EDITABLE, isEditable);// 是否可以编辑
					i.putStringArrayListExtra(EDIT_IMAGE_STR1, images);
					i.putStringArrayListExtra(EDIT_IMAGE_STR2, ids);
					startActivityForResult(i, EDIT_IMAGE);
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
					i.putExtra(EDITABLE, isEditable);// 是否可以编辑
					i.putExtra(EDIT_NAME_STR1, userInfo.getName());
					i.putExtra(EDIT_NAME_STR2, userInfo.getGender());
					i.putExtra(EDIT_NAME_STR3, userInfo.getMarried());
					startActivityForResult(i, EDIT_NAME);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.error12);
				}
			}
		});

		// 移到了我的企业中的主营产品,注意对EDIT_PRODUCT处理
		// ((View) findViewById(R.id.textViewEditProductShow).getParent())
		// .setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// if (userInfo != null) {
		// Intent i = new Intent(CardEditActivity.this,
		// CardAddUserProductActivity.class);
		// i.putParcelableArrayListExtra(EDIT_PRODUCT_STR,
		// (ArrayList<ProductVO>) userInfo.getProduct());
		// startActivityForResult(i, EDIT_PRODUCT);
		// } else {
		// CommonUtil.displayToast(getApplicationContext(),
		// R.string.error12);
		// }
		// }
		// });
		textViewEditWorkShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (userInfo != null) {
					Intent i = new Intent(CardEditActivity.this,
							CompanyDetailActivity.class);
					i.putExtra(EDITABLE, isEditable);// 是否可以编辑
					i.putExtra(USERID, userInfo.getUserid());// 是否可以编辑
					startActivityForResult(i, EDIT_WORK);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.error12);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (resultCode == Activity.RESULT_OK) {
		// switch (requestCode) {
		// case EDIT_NAME:
		// String name = data.getStringExtra(EDIT_NAME_STR1);
		// int gend = data.getIntExtra(EDIT_NAME_STR2, 0);
		// int ismarry = data.getIntExtra(EDIT_NAME_STR3, 0);
		// userInfo.setName(name);
		// userInfo.setGender(gend);
		// userInfo.setMarried(ismarry);
		// ((TextView) findViewById(R.id.textViewEditNameShow))
		// .setText(name);
		// break;
		// case EDIT_BIRTH:
		// String birth = data.getStringExtra(EDIT_BIRTH_STR1);
		// // int birthopen = data.getIntExtra(EDIT_BIRTH_STR2, 0);
		// // String birthdayLunar = data.getStringExtra(EDIT_BIRTH_STR3);
		// // int constellation = data.getIntExtra(EDIT_BIRTH_STR4, 0);// 星座编号
		// // int zodiac = data.getIntExtra(EDIT_BIRTH_STR5, 0);//
		// 星座编号EDIT_BIRTH_STR5
		// userInfo.setBirthday(birth);
		// // userInfo.setZodiac(zodiac);
		// // userInfo.setBirthdayLunar(birthdayLunar);
		// // userInfo.setIsBirthdayOpen(birthopen);
		// // userInfo.setConstellation(constellation);
		// textViewEditBirthShow.setText(birth);
		// break;
		// case EDIT_PLACE:
		// int place = data.getIntExtra(EDIT_PLACE_STR1, 0);
		// int hometown = data.getIntExtra(EDIT_PLACE_STR2, 0);
		// String othertowns = data.getStringExtra(EDIT_PLACE_STR3);
		// userInfo.setCity(place);
		// userInfo.setHometown(hometown);
		// userInfo.setTravelCity(othertowns);
		//
		// if (mConnHelper.getCitys() != null && place >= 1
		// && place <= mConnHelper.getCitys().size())
		// textViewEditPlaceShow.setText(mConnHelper.getCitys()
		// .get(place - 1).getCityName());
		// break;
		// case EDIT_DREAM:
		// // dreamsList = data.getStringArrayListExtra(EDIT_DREAM_STR);
		// // String dream = "";
		// // if (dream != null) {
		// // for (String str : dreamsList) {
		// // dream += str + " ";
		// // }
		// // }
		// String dream = data.getStringExtra(EDIT_DREAM_STR);
		// userInfo.setDream(dream);
		// textViewEditDreamShow.setText(dream);
		// break;
		// case EDIT_EMAIL:
		// String email = data.getStringExtra(EDIT_EMAIL_STR1);
		// int emailopen = data.getIntExtra(EDIT_EMAIL_STR2, 0);
		// userInfo.setEmail(email);
		// userInfo.setIsEmailOpen(emailopen);
		// textViewEditEmailShow.setText(email);
		// break;
		//
		// case EDIT_QQ:
		// String qq = data.getStringExtra(EDIT_QQ_STR1);
		// int qqopen = data.getIntExtra(EDIT_QQ_STR2, 0);
		// userInfo.setQq(qq);
		// userInfo.setIsQqOpen(qqopen);
		// textViewEditQQShow.setText(qq);
		// break;
		// case EDIT_WEIXIN:
		// String weixin = data.getStringExtra(EDIT_WEIXIN_STR1);
		// int weixinopen = data.getIntExtra(EDIT_WEIXIN_STR2, 0);
		// userInfo.setWeixin(weixin);
		// userInfo.setIsWeixinOpen(weixinopen);
		// textViewEditWeixinShow.setText(weixin);
		// break;
		// case EDIT_HOBBY:
		// String hobby = data.getStringExtra(EDIT_HOBBY_STR);
		// userInfo.setHobby(hobby);
		// textViewEditHobbyShow.setText(hobby);
		// break;
		// case EDIT_MOTTO:
		// String motto = data.getStringExtra(EDIT_MOTTO_STR);
		// userInfo.setFaith(motto);
		// textViewEditZymShow.setText(motto);
		// break;
		// case EDIT_IMAGE:
		// try {
		// ArrayList<String> images = data
		// .getStringArrayListExtra(EDIT_IMAGE_STR1); // 本地需要上传的
		// localImages.clear();
		// localImages.addAll(images);
		// ArrayList<String> ids = data
		// .getStringArrayListExtra(EDIT_IMAGE_STR2);
		// ArrayList<PicNewVO> pics = new ArrayList<PicNewVO>();
		// if (userInfo.getPhoto() != null) {
		// PicNewVO temp = null;
		// for (PicNewVO pic : userInfo.getPhoto()) {
		// if (ids.contains(pic.getPic())) {
		// if (ids.get(ids.size() - 1)
		// .equals(pic.getPic())) {
		// temp = pic;
		// } else {
		// pics.add(pic);
		// }
		// }
		// }
		// if (temp != null) {
		// pics.add(temp);
		// }
		// }
		// // userInfo.setPhoto(pics);
		//
		// userInfo.setPhoto(pics);
		// String originStr = getPhotoStr(pics);
		//
		// mConnHelper.pubPhoto(CardEditActivity.this, mUIHandler,
		// MsgTagVO.DATA_OTHER, originStr, images);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// break;
		// case EDIT_PHONE:
		// String phone = data.getStringExtra(EDIT_PHONE_STR1);
		// int phoneopen = data.getIntExtra(EDIT_PHONE_STR2, 0);
		// userInfo.setPhone(phone);
		// userInfo.setIsPhoneOpen(phoneopen);
		// textViewEditPhoneShow.setText(phone);
		// break;
		// default:
		// String filePath = pwh.dealPhotoReturn(requestCode, resultCode,
		// data, false);
		// if (filePath != null) {
		// try {
		// mConnHelper.setUserHeadImage(CardEditActivity.this,
		// mUIHandler, MsgTagVO.SELECT_PICTURE, filePath);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// break;
		// }
		// edit = true;
		// }
		super.onActivityResult(requestCode, resultCode, data);
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

		// 生成名片二维码信息图片
		createQRImage(ivTDCard,
				userInfo.getUserid() + "," + userInfo.getName(), 100, 100);

		mLoadImage.beginLoad(userInfo.getUheader(), ivHead);
		fillText(textViewEditDreamShow, userInfo.getDream());

		int placeId = userInfo.getCity();
		String placeText = getString(R.string.nofill);
		if (mConnHelper.getCitys() != null && placeId >= 1
				&& placeId <= mConnHelper.getCitys().size())
			placeText = mConnHelper.getCitys().get(placeId - 1).getCityName();
		textViewEditPlaceShow.setText(placeText);
		fillText(textViewEditNameShow, userInfo.getName());
		List<PicNewVO> images = userInfo.getPhoto();
		if (images != null) {
			((TextView) findViewById(R.id.textViewEditImagesShow))
					.setText(getString(R.string.mp_has) + images.size()
							+ getString(R.string.mp_imgasall));
		}

		String birthday = userInfo.getBirthday();
		if (birthday == null)
			birthday = userInfo.getBirthdayLunar();
		fillText(textViewEditBirthShow, birthday);

		fillText(textViewEditDreamShow, userInfo.getDream());
		fillText(textViewEditZymShow, userInfo.getFaith());
		fillText(textViewEditHobbyShow, userInfo.getHobby());
		fillText(textViewEditPhoneShow, userInfo.getPhone());
		fillText(textViewEditEmailShow, userInfo.getEmail());
		fillText(textViewEditQQShow, userInfo.getQq());
		fillText(textViewEditWeixinShow, userInfo.getWeixin());
		fillText(etSignature, userInfo.getSignature());
	}

	void fillText(TextView tv, String text) {
		if (tv == null)
			return;
		if (text == null || text.trim().toString().equals(""))
			tv.setText(getString(R.string.nofill));
		else
			tv.setText(text);
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
					// mFacade.saveOrUpdate(userInfo);
					CommonUtil.displayToast(getApplicationContext(),
							R.string.info10);
					CardEditActivity.this.finish();
				}
			}
				break;
			case MsgTagVO.DATA_OTHER:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					// mFacade.saveOrUpdate(userInfo);
					((TextView) findViewById(R.id.textViewEditImagesShow))
							.setText(getString(R.string.mp_has)
									+ (userInfo.getPhoto().size() + localImages
											.size())
									+ getString(R.string.mp_imgasall));
					CommonUtil.displayToast(getApplicationContext(),
							R.string.update_photo_success);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.update_photo_failed);

				}
				break;
			case MsgTagVO.SELECT_PICTURE:
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					// CommonUtil.displayToast(getApplicationContext(),
					// R.string.info10);
					String data = JsonHandler.getSingleResult((String) msg.obj);
					if (data == null)
						CommonUtil.displayToast(getApplicationContext(),
								R.string.tip_jsonerror);
					else {
						try {
							JSONObject jobj = new JSONObject(data);
							String uheader = jobj.getString("uheader");
							userInfo.setUheader(uheader);
							mLoadImage.beginLoad(uheader, ivHead);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							CommonUtil.displayToast(getApplicationContext(),
									R.string.tip_jsonerror);
						}
						// CommonUtil.displayToast(getApplicationContext(),
						// R.string.uhead_update_success);
					}

				}
			}
		}
	};

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			// userInfo = mFacade.getById(userid);
			// if (userInfo == null) {
			// CommonUtil.displayToast(getApplicationContext(),
			// R.string.error0);
			// } else {
			// Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
			// msg.obj = "dbdata";
			// msg.sendToTarget();
			// }
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

	// 要转换的地址或字符串,可以是中文
	public void createQRImage(ImageView sweepIV, String url, int w, int h) {
		try {
			// 判断URL合法性
			if (url == null || "".equals(url) || url.length() < 1) {
				return;
			}
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 图像数据转换，使用了矩阵转换
			BitMatrix bitMatrix = new QRCodeWriter().encode(url,
					BarcodeFormat.QR_CODE, w, h, hints);
			int[] pixels = new int[w * h];
			// 下面这里按照二维码的算法，逐个生成二维码的图片，
			// 两个for循环是图片横列扫描的结果
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					if (bitMatrix.get(x, y)) {
						pixels[y * w + x] = 0xff000000;
					} else {
						pixels[y * w + x] = 0xffffffff;
					}
				}
			}
			// 生成二维码图片的格式，使用ARGB_8888
			Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
			// 显示到一个ImageView上面
			sweepIV.setImageBitmap(bitmap);
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
}
