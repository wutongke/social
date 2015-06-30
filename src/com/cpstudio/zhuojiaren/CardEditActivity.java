package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.DreamVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ProductVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.utils.SolarToLundar;

public class CardEditActivity extends Activity {
	
	@InjectView(R.id.ivHead)
	ImageView ivHead;
	@InjectView(R.id.textViewChangeHead)
	TextView textViewChangeHead;
	@InjectView(R.id.etSignature)
	EditText etSignature;

	private String userid = "";
	private UserVO mUser = null;
	public final static int EDIT_BIRTH = 0;
	public final static String EDIT_BIRTH_STR1 = "birth";
	public final static String EDIT_BIRTH_STR2 = "birthopen";
	public final static String EDIT_BIRTH_STR3 = "birthtype";
	public final static int EDIT_PLACE = 1;
	public final static String EDIT_PLACE_STR1 = "place";
	public final static String EDIT_PLACE_STR2 = "hometown";
	public final static String EDIT_PLACE_STR3 = "othertowns";
	public final static int EDIT_DREAM = 2;
	public final static String EDIT_DREAM_STR = "dream";
	public final static int EDIT_EMAIL = 3;
	public final static String EDIT_EMAIL_STR1 = "email";
	public final static String EDIT_EMAIL_STR2 = "emailopen";
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
	private ZhuoConnHelper mConnHelper = null;
	private ArrayList<String> dreamsList = new ArrayList<String>();
	private ArrayList<String> localImages = new ArrayList<String>();
	public final static String LOCAL_IMAGE = "localImage";
	private PopupWindows pwh = null;
	private UserFacade mFacade = null;
	private boolean edit = false;
//Î´ÍêÉÆ
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_edit);
		ButterKnife.inject(this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		mFacade = new UserFacade(getApplicationContext());
		pwh = new PopupWindows(CardEditActivity.this);
		userid = ResHelper.getInstance(getApplicationContext()).getUserid();
		loadInfo();
		initClick();
	}

	private void initClick() {
		
		etSignature.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart ;
            private int selectionEnd ;
            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
                temp = s;
            }
 
            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,
                    int arg3) {
            }
 
            @Override
            public void afterTextChanged(Editable s) {
                 selectionStart = etSignature.getSelectionStart();
                selectionEnd = etSignature.getSelectionEnd();
                
                if (temp.length() > 15) {
                    Toast.makeText(CardEditActivity.this,
                            R.string.edit_nsignature_limit, Toast.LENGTH_SHORT)
                            .show();
                    s.delete(selectionStart-1, selectionEnd);
                    int tempSelection = selectionStart;
                    etSignature.setText(s);
                    etSignature.setSelection(tempSelection);
                }
            }
        });
		
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!showLeaveConfirmDlg()) {
					CardEditActivity.this.finish();
				}
			}
		});

		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (edit) {
							Map<String, String> files = new HashMap<String, String>();
							int imgcnt = localImages.size();
							if (imgcnt > 0) {
								if (localImages.size() - 1 > 0) {
									for (int i = 0; i < localImages.size() - 1; i++) {
										files.put("img" + i, localImages.get(i));
									}
								}
								files.put("uheader",
										localImages.get(localImages.size() - 1));
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
							if (mUser.getBirthday() != null
									&& !mUser.getBirthday().equals("")
									&& mUser.getBirthday().indexOf("-") != -1) {
								String[] solar = mUser.getBirthday().split("-");
								String constellation = ZhuoCommHelper
										.dayToSign(CardEditActivity.this,
												Integer.valueOf(solar[1]),
												Integer.valueOf(solar[2]));
								mUser.setConstellation(constellation);
							}
							mConnHelper.updateUserDetail(files, mUIHandler,
									MsgTagVO.PUB_INFO, CardEditActivity.this,
									mUser.getUserid(), mUser.getUsername(),
									String.valueOf(imgcnt), mUser.getSex(),
									mUser.getCompany(), mUser.getPost(),
									mUser.getIndustry(), mUser.getCity(),
									mUser.getHometown(),
									mUser.getTravelCities(),
									mUser.getBirthday(),
									mUser.getBirthdayType(),
									mUser.getConstellation(), mUser.getMaxim(),
									mUser.getHobby(), mUser.getEmail(), "",
									mUser.getWebsite(), mUser.getIsmarry(),
									mUser.getIsworking(),
									mUser.getIsphoneopen(),
									mUser.getIsisentrepreneurship(),
									mUser.getIsemailopen(),
									mUser.getIsbirthdayopen(),
									mUser.getMycustomer(), true, null, null);
						} else {
							CardEditActivity.this.finish();
						}
					}
				});

		((View) findViewById(R.id.textViewEditBirthShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserBirthActivity.class);
							i.putExtra(EDIT_BIRTH_STR1, mUser.getBirthday());
							i.putExtra(EDIT_BIRTH_STR2,
									mUser.getIsbirthdayopen());
							i.putExtra(EDIT_BIRTH_STR3, mUser.getBirthdayType());
							startActivityForResult(i, EDIT_BIRTH);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});

		((View) findViewById(R.id.textViewEditPlaceShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserCityActivity.class);
							i.putExtra(EDIT_PLACE_STR1, mUser.getCity());
							i.putExtra(EDIT_PLACE_STR2, mUser.getHometown());
							i.putExtra(EDIT_PLACE_STR3, mUser.getTravelCities());
							startActivityForResult(i, EDIT_PLACE);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);

						}
					}
				});
		((View) findViewById(R.id.textViewEditDreamShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserDreamActivity.class);
							i.putStringArrayListExtra(EDIT_DREAM_STR,
									dreamsList);
							startActivityForResult(i, EDIT_DREAM);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});

		((View) findViewById(R.id.textViewEditEmailShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserEmailActivity.class);
							i.putExtra(EDIT_EMAIL_STR1, mUser.getEmail());
							i.putExtra(EDIT_EMAIL_STR2, mUser.getIsemailopen());
							startActivityForResult(i, EDIT_EMAIL);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditHobbyShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserHobbyActivity.class);
							i.putExtra(EDIT_HOBBY_STR, mUser.getHobby());
							startActivityForResult(i, EDIT_HOBBY);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditZymShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserMottoActivity.class);
							i.putExtra(EDIT_MOTTO_STR, mUser.getMaxim());
							startActivityForResult(i, EDIT_MOTTO);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});

		((View) findViewById(R.id.textViewEditFieldsShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserFieldsActivity.class);
							i.putExtra(EDIT_FIELD_STR, mUser.getIndustry());
							startActivityForResult(i, EDIT_FIELD);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditImagesShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							ArrayList<String> images = new ArrayList<String>();
							ArrayList<String> ids = new ArrayList<String>();
							if (mUser.getPics() != null) {
								String headUrl = null;
								String headId = null;
								for (int i = 0; i < mUser.getPics().size(); i++) {
									PicVO pic = mUser.getPics().get(
											mUser.getPics().size() - 1 - i);
									if (pic.getUrl().equals(mUser.getUheader())) {
										headUrl = pic.getUrl();
										headId = pic.getId();
									} else {
										images.add(pic.getUrl());
										ids.add(pic.getId());
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
							i.putStringArrayListExtra(EDIT_IMAGE_STR1, images);
							i.putStringArrayListExtra(EDIT_IMAGE_STR2, ids);
							startActivityForResult(i, EDIT_IMAGE);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditGongShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserResourceActivity.class);
							i.putExtra(EDIT_RES_STR1, 2);
							i.putExtra(EDIT_RES_STR2, userid);
							startActivity(i);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditXuShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserResourceActivity.class);
							i.putExtra(EDIT_RES_STR1, 1);
							i.putExtra(EDIT_RES_STR2, userid);
							startActivity(i);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});

		((View) findViewById(R.id.textViewEditLevelShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserStarActivity.class);
							i.putExtra(EDIT_LEVEL_STR, mUser.getLevel());
							startActivity(i);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditWebSiteShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserUrlActivity.class);
							i.putExtra(EDIT_WEBSITE_STR, mUser.getWebsite());
							startActivityForResult(i, EDIT_WEBSITE);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditCustomerShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserCustomerActivity.class);
							i.putExtra(EDIT_CUSTOMER_STR, mUser.getMycustomer());
							startActivityForResult(i, EDIT_CUSTOMER);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});

		((View) findViewById(R.id.textViewEditPhoneShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserPhoneActivity.class);
							i.putExtra(EDIT_PHONE_STR1, mUser.getUserid());
							i.putExtra(EDIT_PHONE_STR2, mUser.getIsphoneopen());
							startActivityForResult(i, EDIT_PHONE);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditNameShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserNameActivity.class);
							i.putExtra(EDIT_NAME_STR1, mUser.getUsername());
							i.putExtra(EDIT_NAME_STR2, mUser.getSex());
							i.putExtra(EDIT_NAME_STR3, mUser.getIsmarry());
							startActivityForResult(i, EDIT_NAME);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
//¶þÎ¬Âë
		((View) findViewById(R.id.textViewEditTDCard).getParent())
		.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			//
//				if (mUser != null) {
//					Intent i = new Intent(CardEditActivity.this,
//							CardAddUserNameActivity.class);
//					i.putExtra(EDIT_NAME_STR1, mUser.getUsername());
//					i.putExtra(EDIT_NAME_STR2, mUser.getSex());
//					i.putExtra(EDIT_NAME_STR3, mUser.getIsmarry());
//					startActivityForResult(i, EDIT_NAME);
//				} else {
//					CommonUtil.displayToast(getApplicationContext(),
//							R.string.error12);
//				}
			}
		});
		
		
		((View) findViewById(R.id.textViewEditProductShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserProductActivity.class);
							i.putParcelableArrayListExtra(EDIT_PRODUCT_STR,
									(ArrayList<ProductVO>) mUser.getProduct());
							startActivityForResult(i, EDIT_PRODUCT);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});
		((View) findViewById(R.id.textViewEditWorkShow).getParent())
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(CardEditActivity.this,
									CardAddUserWorkActivity.class);
							i.putExtra(EDIT_WORK_STR1, mUser.getCompany());
							i.putExtra(EDIT_WORK_STR2, mUser.getPost());
							i.putExtra(EDIT_WORK_STR3, mUser.getIsworking());
							i.putExtra(EDIT_WORK_STR4,
									mUser.getIsisentrepreneurship());
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
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case EDIT_BIRTH:
				String birth = data.getStringExtra(EDIT_BIRTH_STR1);
				String birthopen = data.getStringExtra(EDIT_BIRTH_STR2);
				String birthdayType = data.getStringExtra(EDIT_BIRTH_STR3);
				mUser.setBirthday(birth);
				mUser.setIsbirthdayopen(birthopen);
				mUser.setBirthdayType(birthdayType);
				((TextView) findViewById(R.id.textViewEditBirthShow))
						.setText(birth);
				break;
			case EDIT_PLACE:
				String place = data.getStringExtra(EDIT_PLACE_STR1);
				String hometown = data.getStringExtra(EDIT_PLACE_STR2);
				String othertowns = data.getStringExtra(EDIT_PLACE_STR3);
				mUser.setCity(place);
				mUser.setHometown(hometown);
				mUser.setTravelCities(othertowns);
				((TextView) findViewById(R.id.textViewEditPlaceShow))
						.setText(place);
				break;
			case EDIT_DREAM:
				dreamsList = data.getStringArrayListExtra(EDIT_DREAM_STR);
				String dream = "";
				if (dream != null) {
					for (String str : dreamsList) {
						dream += str + " ";
					}
				}
				((TextView) findViewById(R.id.textViewEditDreamShow))
						.setText(dream);
				break;
			case EDIT_EMAIL:
				String email = data.getStringExtra(EDIT_EMAIL_STR1);
				String emailopen = data.getStringExtra(EDIT_EMAIL_STR2);
				mUser.setEmail(email);
				mUser.setIsemailopen(emailopen);
				((TextView) findViewById(R.id.textViewEditEmailShow))
						.setText(email);
				break;
			case EDIT_HOBBY:
				String hobby = data.getStringExtra(EDIT_HOBBY_STR);
				mUser.setHobby(hobby);
				((TextView) findViewById(R.id.textViewEditHobbyShow))
						.setText(hobby);
				break;
			case EDIT_MOTTO:
				String motto = data.getStringExtra(EDIT_MOTTO_STR);
				mUser.setMaxim(motto);
				((TextView) findViewById(R.id.textViewEditZymShow))
						.setText(motto);
				break;
			case EDIT_FIELD:
				String fields = data.getStringExtra(EDIT_FIELD_STR);
				mUser.setIndustry(fields);
				fields = fields.replace(";", " ");
				((TextView) findViewById(R.id.textViewEditFieldsShow))
						.setText(fields);
				break;
			case EDIT_IMAGE:
				try {
					ArrayList<String> images = data
							.getStringArrayListExtra(EDIT_IMAGE_STR1);
					localImages.clear();
					localImages.addAll(images);
					ArrayList<String> ids = data
							.getStringArrayListExtra(EDIT_IMAGE_STR2);
					ArrayList<PicVO> pics = new ArrayList<PicVO>();
					if (mUser.getPics() != null) {
						PicVO temp = null;
						for (PicVO pic : mUser.getPics()) {
							if (ids.contains(pic.getId())) {
								if (ids.get(ids.size() - 1).equals(pic.getId())) {
									temp = pic;
								} else {
									pics.add(pic);
								}
							}
						}
						if (temp != null) {
							pics.add(temp);
						}
					}
					mUser.setPics(pics);
					((TextView) findViewById(R.id.textViewEditImagesShow))
							.setText(getString(R.string.mp_has)
									+ (images.size() + ids.size())
									+ getString(R.string.mp_imgasall));
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case EDIT_WEBSITE:
				String webSite = data.getStringExtra(EDIT_WEBSITE_STR);
				mUser.setWebsite(webSite);
				((TextView) findViewById(R.id.textViewEditWebSiteShow))
						.setText(webSite);
				break;
			case EDIT_CUSTOMER:
				String customer = data.getStringExtra(EDIT_CUSTOMER_STR);
				mUser.setMycustomer(customer);
				((TextView) findViewById(R.id.textViewEditCustomerShow))
						.setText(customer);
				break;
			case EDIT_PHONE:
				String phone = data.getStringExtra(EDIT_PHONE_STR1);
				String phoneopen = data.getStringExtra(EDIT_PHONE_STR2);
				mUser.setPhone(phone);
				mUser.setIsphoneopen(phoneopen);
				((TextView) findViewById(R.id.textViewEditPhoneShow))
						.setText(phone);
				break;
			case EDIT_NAME:
				String name = data.getStringExtra(EDIT_NAME_STR1);
				String sex = data.getStringExtra(EDIT_NAME_STR2);
				String ismarry = data.getStringExtra(EDIT_NAME_STR3);
				mUser.setUsername(name);
				mUser.setSex(sex);
				mUser.setIsmarry(ismarry);
				((TextView) findViewById(R.id.textViewEditNameShow))
						.setText(name);
				break;
			case EDIT_PRODUCT:
				ArrayList<ProductVO> products = data
						.getParcelableArrayListExtra(EDIT_PRODUCT_STR);
				mUser.setProduct(products);
				String product = "";
				if (product != null) {
					for (ProductVO productVO : products) {
						product += productVO.getTitle() + " ";
					}
				}
				((TextView) findViewById(R.id.textViewEditProductShow))
						.setText(product);
				break;
			case EDIT_WORK:
				String company = data.getStringExtra(EDIT_WORK_STR1);
				String work = data.getStringExtra(EDIT_WORK_STR2);
				String isworking = data.getStringExtra(EDIT_WORK_STR3);
				String isentrepreneurship = data.getStringExtra(EDIT_WORK_STR4);
				mUser.setCompany(company);
				mUser.setPost(work);
				mUser.setIsworking(isworking);
				mUser.setIsisentrepreneurship(isentrepreneurship);
				company = ZhuoCommHelper.getFirst(company, ";");
				work = ZhuoCommHelper.getFirst(work, ";");
				((TextView) findViewById(R.id.textViewEditWorkShow))
						.setText(company + " " + work);
				break;
			default:
				break;
			}
			edit = true;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				String data = (String) msg.obj;
				if (!data.equals("dbdata")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					mUser = nljh.parseUser();
					if (null != mUser) {
						mFacade.saveOrUpdate(mUser);
					}
				}
				if (null != mUser) {
					String name = mUser.getUsername();
					((TextView) findViewById(R.id.textViewEditNameShow))
							.setText(name);
					List<PicVO> images = mUser.getPics();
					if (images != null) {
						((TextView) findViewById(R.id.textViewEditImagesShow))
								.setText(getString(R.string.mp_has)
										+ images.size()
										+ getString(R.string.mp_imgasall));
					}
					String addTime = mUser.getJoinZhuoDate();
					((TextView) findViewById(R.id.textViewEditAddTimeShow))
							.setText(addTime);
					String company = mUser.getCompany();
					String work = mUser.getPost();
					company = ZhuoCommHelper.getFirst(company, ";");
					work = ZhuoCommHelper.getFirst(work, ";");
					((TextView) findViewById(R.id.textViewEditWorkShow))
							.setText(company + " " + work);
					String field = mUser.getIndustry();
					if (field != null) {
						String fields = field.replaceAll(";", " ");
						((TextView) findViewById(R.id.textViewEditFieldsShow))
								.setText(fields);
					}
					String place = mUser.getCity();
					((TextView) findViewById(R.id.textViewEditPlaceShow))
							.setText(place);
					String birthday = mUser.getBirthday();
					if (birthday != null && !birthday.equals("")) {
						String birthshowtype = mUser.getBirthdayType();
						if (birthshowtype.equals("0")) {
							((TextView) findViewById(R.id.textViewEditBirthShow))
									.setText(birthday);
						} else {
							try {
								String[] birth = ZhuoCommHelper
										.getBirthday(birthday);
								birthday = SolarToLundar.getStringLunar(
										Integer.valueOf(birth[0]),
										Integer.valueOf(birth[1]),
										Integer.valueOf(birth[2]));
								((TextView) findViewById(R.id.textViewEditBirthShow))
										.setText(birthday);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					List<DreamVO> dreams = mUser.getDream();
					String dreamStr = "";
					if (dreamStr != null) {
						for (DreamVO dream : dreams) {
							dreamStr += dream.getDream() + " ";
							dreamsList.add(dreamStr);
						}
					}
					((TextView) findViewById(R.id.textViewEditDreamShow))
							.setText(dreamStr);
					String motto = mUser.getMaxim();
					((TextView) findViewById(R.id.textViewEditZymShow))
							.setText(motto);
					String hobby = mUser.getHobby();
					((TextView) findViewById(R.id.textViewEditHobbyShow))
							.setText(hobby);
					List<ProductVO> products = mUser.getProduct();
					if (products != null) {
						String cps = "";
						for (int i = 0; i < products.size(); i++) {
							cps += products.get(i).getTitle() + "    ";
						}
						((TextView) findViewById(R.id.textViewEditProductShow))
								.setText(cps);
					}
					int level = Integer.valueOf(mUser.getLevel());

					String[] levels = getResources().getStringArray(
							R.array.array_level_type);
					if (level > levels.length - 1) {
						level = levels.length - 1;
					}
					((TextView) findViewById(R.id.textViewEditLevelShow))
							.setText(levels[level]);
					String lastoffer = mUser.getLastoffer();
					if (lastoffer == null) {
						lastoffer = "";
					}
					((TextView) findViewById(R.id.textViewEditGongShow))
							.setText(lastoffer);
					String lastdemand = mUser.getLastdemand();
					if (lastdemand == null) {
						lastdemand = "";
					}
					((TextView) findViewById(R.id.textViewEditXuShow))
							.setText(lastdemand);
					String website = mUser.getWebsite();
					((TextView) findViewById(R.id.textViewEditWebSiteShow))
							.setText(website);
					String phone = mUser.getUserid();
					((TextView) findViewById(R.id.textViewEditPhoneShow))
							.setText(phone);
					String email = mUser.getEmail();
					((TextView) findViewById(R.id.textViewEditEmailShow))
							.setText(email);
					String customer = mUser.getMycustomer();
					((TextView) findViewById(R.id.textViewEditCustomerShow))
							.setText(customer);
				}
				break;
			}
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					mFacade.saveOrUpdate(mUser);
					CommonUtil.displayToast(getApplicationContext(),
							R.string.info10);
					CardEditActivity.this.finish();
				}
			}
			}
		}
	};

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			mUser = mFacade.getById(userid);
			if (mUser == null) {
				CommonUtil.displayToast(getApplicationContext(),
						R.string.error0);
			} else {
				Message msg = mUIHandler.obtainMessage(MsgTagVO.DATA_LOAD);
				msg.obj = "dbdata";
				msg.sendToTarget();
			}
		} else {
			String params = ZhuoCommHelper.getUrlCard() + "?uid=" + userid;
			mConnHelper.getFromServer(params, mUIHandler, MsgTagVO.DATA_LOAD,
					CardEditActivity.this, true, new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							CardEditActivity.this.finish();
						}
					});
		}
	}

	private boolean showLeaveConfirmDlg() {
		if (edit) {
			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					CardEditActivity.this.finish();
				}
			};
			pwh.showPopDlg(findViewById(R.id.rootLayout), listener, null,
					R.string.info27);
		}
		return edit;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (!showLeaveConfirmDlg()) {
				CardEditActivity.this.finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
