package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.List;

import com.cpstudio.zhuojiaren.facade.CardMsgFacade;
import com.cpstudio.zhuojiaren.facade.UserFacade;
import com.cpstudio.zhuojiaren.helper.AsyncConnectHelper.FinishCallback;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ResHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoCommHelper;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CardMsgVO;
import com.cpstudio.zhuojiaren.model.DreamVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ZhuoInfoVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ProductVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PopupWindows;
import com.utils.NumberUtil;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;

public class UserCardActivity extends Activity {
	private LoadImage mLoadImage = null;
	private List<PicVO> mPics = new ArrayList<PicVO>();
	private List<UserVO> mFris = new ArrayList<UserVO>();
	private List<QuanVO> mGros = new ArrayList<QuanVO>();
	private List<ZhuoInfoVO> mActives = new ArrayList<ZhuoInfoVO>();
	private boolean mInit = false;
	private String ismy = "0";
	private PopupWindows pwh = null;
	private ZhuoConnHelper mConnHelper = null;
	private String userid = null;
	private String isfollow = "0";
	private String toBlack = "0";
	private Button blackButton;
	private UserVO mUser = null;
	private UserFacade userFacade = null;
	private CardMsgFacade mFacade = null;
	private String myid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_card);
		userFacade = new UserFacade(UserCardActivity.this);
		mFacade = new CardMsgFacade(UserCardActivity.this);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(UserCardActivity.this);
		Intent i = getIntent();
		userid = i.getStringExtra("userid");
		myid = ResHelper.getInstance(getApplicationContext()).getUserid();
		if (userid.equals(myid)) {
			ismy = "1";
		}
		mLoadImage = new LoadImage();
		if (!ismy.equals("0")) {
			findViewById(R.id.buttonEdit).setVisibility(View.VISIBLE);
		}
		loadInfo();
		initClick();
	}

	private void resetFollowButton() {
		TextView tv = ((TextView) findViewById(R.id.textViewFollow));
		if (isfollow == null || isfollow.equals("0")) {
			Drawable drawable = getResources().getDrawable(R.drawable.ico_eye);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			tv.setCompoundDrawables(null, drawable, null, null);
			tv.setText(R.string.label_follow);
		} else {
			Drawable drawable = getResources().getDrawable(
					R.drawable.ico_relate);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(),
					drawable.getMinimumHeight());
			tv.setCompoundDrawables(null, drawable, null, null);
			tv.setText(R.string.label_relatesetting);
		}
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				UserCardActivity.this.finish();
			}
		});

		findViewById(R.id.buttonEdit).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(UserCardActivity.this,
						CardEditActivity.class);
				startActivity(i);
			}
		});

		findViewById(R.id.relativeLayoutChat).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(UserCardActivity.this,
								ChatActivity.class);
						i.putExtra("userid", userid);
						startActivity(i);
					}
				});

		findViewById(R.id.relativeLayoutRecommand).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent i = new Intent(UserCardActivity.this,
								UserSelectActivity.class);
						ArrayList<String> tempids = new ArrayList<String>(1);
						tempids.add(userid);
						i.putStringArrayListExtra("otherids", tempids);
						startActivityForResult(i, MsgTagVO.USER_SELECT);
					}
				});

		findViewById(R.id.textViewMpcps).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mUser != null) {
							Intent i = new Intent(UserCardActivity.this,
									CardAddUserProductActivity.class);
							i.putParcelableArrayListExtra(
									CardEditActivity.EDIT_PRODUCT_STR,
									(ArrayList<ProductVO>) mUser.getProduct());
							startActivityForResult(i,
									CardEditActivity.EDIT_PRODUCT);
						} else {
							CommonUtil.displayToast(getApplicationContext(),
									R.string.error12);
						}
					}
				});

		findViewById(R.id.relativeLayoutFollow).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (isfollow != null) {
							if (isfollow.equals("0")) {
								mConnHelper.followUser(userid, "1", mUIHandler,
										MsgTagVO.MSG_FOWARD, null, true, null,
										null);
							} else {
								OnClickListener sendCard = new OnClickListener() {

									@Override
									public void onClick(View paramView) {
										sendCard();
									}
								};
								OnClickListener cancelfollow = new OnClickListener() {

									@Override
									public void onClick(View paramView) {
										mConnHelper.followUser(userid, "0",
												mUIHandler,
												MsgTagVO.MSG_FOWARD, null,
												true, null, null);
									}
								};
								// OnClickListener report = new
								// OnClickListener() {
								//
								// @Override
								// public void onClick(View paramView) {
								// mConnHelper.prosecute(userid, mUIHandler,
								// MsgTagVO.ADD_PROSECUTE, null);
								// }
								// };
								OnClickListener black = new OnClickListener() {

									@Override
									public void onClick(View v) {
										blackButton = (Button) v;
										mConnHelper.black(userid, toBlack,
												mUIHandler, MsgTagVO.ADD_BACK,
												null, true, null, null);
									}
								};
								OnClickListener[] onClickListeners = new OnClickListener[] {
										sendCard, cancelfollow, black };
								pwh.showBottomPop(
										findViewById(R.id.scrollViewCard),
										onClickListeners, new int[] {
												R.string.label_cardsend,
												R.string.label_unfollow,
												R.string.label_black }, 10,
										"manage");
							}
						}
					}
				});

	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				String data = (String) msg.obj;
				if (data != null && !data.equals("dbdata")) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					mUser = nljh.parseUser();
					if (null != mUser) {
						userFacade.saveOrUpdate(mUser);
					}
				}
				if (null != mUser) {
					isfollow = mUser.getIsfollow();
					if (ismy.equals("0")) {
						resetFollowButton();
						findViewById(R.id.linearLayoutBottom).setVisibility(
								View.VISIBLE);
					}
					final String name = mUser.getUsername();
					((TextView) findViewById(R.id.userNameShow)).setText(name);
					((TextView) findViewById(R.id.textViewUsername))
							.setText(name);
					String sex = mUser.getSex();
					((TextView) findViewById(R.id.textViewSex)).setText(sex);
					int level = Integer.valueOf(mUser.getLevel());
					LinearLayout lll = (LinearLayout) findViewById(R.id.linearLayoutLevel);
					LayoutParams lllp = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					lllp.rightMargin = 8;

					String[] levels = getResources().getStringArray(
							R.array.array_level_type);
					if (level > levels.length - 1) {
						level = levels.length - 1;
					}
					((TextView) findViewById(R.id.textViewLevel))
							.setText(levels[level]);

					for (int j = 0; j < levels.length - 1; j++) {
						ImageView iv = new ImageView(UserCardActivity.this);
						iv.setLayoutParams(lllp);
						if (j < level) {
							iv.setImageResource(R.drawable.ico_level_star_on);
						} else {
							iv.setImageResource(R.drawable.ico_level_star_off);
						}
						lll.addView(iv);
					}
					List<PicVO> images = mUser.getPics();
					if (images != null) {
						mInit = true;
						mPics = images;
					}
					String company = mUser.getCompany();
					((TextView) findViewById(R.id.textViewCompany))
							.setText(company);
					final String fields = mUser.getIndustry();
					String work = mUser.getPost();
					String field = getString(R.string.mp_hy);
					if (fields != null) {
						field += fields.replaceAll(";", " ");
						findViewById(R.id.textViewMphx).setOnClickListener(
								new OnClickListener() {
									@Override
									public void onClick(View paramView) {
										Intent i = new Intent(
												UserCardActivity.this,
												NormalListActivity.class);
										i.putExtra("name", name);
										String tempStr = fields;
										if (fields.endsWith(";")
												|| fields.endsWith("£º")
												|| fields.endsWith(" ")) {
											tempStr = ZhuoCommHelper
													.subLast(fields);
										}
										String[] items = tempStr.split(";|£»| ");
										for (int j = 0; j < items.length; j++) {
											items[j] = getString(R.string.label_field)
													+ NumberUtil
															.numberArab2CN(j + 1)
													+ ": " + items[j];
										}
										i.putExtra("list", items);
										startActivity(i);
									}

								});
					}
					((TextView) findViewById(R.id.textViewMphx)).setText(field);
					((TextView) findViewById(R.id.textViewMpgszw))
							.setText(getString(R.string.mp_gszw) + work);
					String place = mUser.getCity();
					if (place != null) {
						((TextView) findViewById(R.id.textViewMpqy))
								.setText(getString(R.string.mp_qy) + place);
					}
					String homeTown = mUser.getHometown();
					if (homeTown != null) {
						((TextView) findViewById(R.id.textViewMpjx))
								.setText(getString(R.string.mp_jx) + homeTown);
					}
					final String othertowns = mUser.getTravelCities();
					if (othertowns != null) {
						TextView lwCity = (TextView) findViewById(R.id.textViewMplw);
						lwCity.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent i = new Intent(UserCardActivity.this,
										NormalListActivity.class);
								i.putExtra("name", name);
								String tempStr = othertowns;
								if (fields.endsWith(";")
										|| fields.endsWith("£º")
										|| fields.endsWith(" ")) {
									tempStr = ZhuoCommHelper.subLast(fields);
								}
								String[] items = tempStr.split(";|£»| ");
								for (int j = 0; j < items.length; j++) {
									items[j] = getString(R.string.label_lwcities)
											+ NumberUtil.numberArab2CN(j + 1)
											+ ": " + items[j];
								}
								i.putExtra("list", items);
								startActivity(i);
							}
						});
						lwCity.setText(getString(R.string.mp_lw) + othertowns);
					}
					String birthshowtype = mUser.getBirthdayType();
					if (birthshowtype.equals("0")) {
						String birthday = mUser.getBirthday();
						if (birthday.indexOf("-") != -1) {
							if (birthday.indexOf(" ") != -1) {
								birthday = birthday.substring(0,
										birthday.indexOf(" "));
							}
							String[] birth = birthday.split("-");
							String sign = ZhuoCommHelper.dayToSign(
									UserCardActivity.this,
									Integer.valueOf(birth[1]),
									Integer.valueOf(birth[2]));
							birthday = birthday.replaceFirst("-",
									getString(R.string.label_year));
							birthday = birthday.replace("-",
									getString(R.string.label_month));
							birthday += getString(R.string.label_day);
							((TextView) findViewById(R.id.textViewMpsr))
									.setText(getString(R.string.mp_sr)
											+ birthday + "    " + sign);
						}
					} else {
						((TextView) findViewById(R.id.textViewMpsr))
								.setText(getString(R.string.mp_sr));
					}
					String lastoffer = mUser.getLastoffer();
					if (lastoffer == null) {
						lastoffer = "";
					}
					findViewById(R.id.textViewGongXu).setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View paramView) {
									Intent i = new Intent(
											UserCardActivity.this,
											CardAddUserResourceActivity.class);
									i.putExtra(CardEditActivity.EDIT_RES_STR1,
											0);
									i.putExtra(CardEditActivity.EDIT_RES_STR2,
											userid);
									startActivity(i);
								}

							});
					((TextView) findViewById(R.id.textViewGong)).setText("["
							+ getString(R.string.mp_gong) + "]" + lastoffer);
					String lastdemand = mUser.getLastdemand();
					if (lastdemand == null) {
						lastdemand = "";
					}
					((TextView) findViewById(R.id.textViewXu)).setText("["
							+ getString(R.string.mp_xu) + "]" + lastdemand);
					List<ProductVO> products = mUser.getProduct();
					int productNum = 0;
					if (products != null) {
						productNum = products.size();
						String cps = "";
						for (int i = 0; i < products.size(); i++) {
							cps += products.get(i).getTitle() + "    ";
						}
						((TextView) findViewById(R.id.textViewMpcps))
								.setText(cps);
						findViewById(R.id.relativeLayoutProducts)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View paramView) {
										Intent intent = new Intent(
												UserCardActivity.this,
												ProductsActivity.class);
										intent.putParcelableArrayListExtra(
												CardEditActivity.EDIT_PRODUCT_STR,
												(ArrayList<ProductVO>) mUser
														.getProduct());
										startActivity(intent);
									}

								});
					}
					TextView cp = (TextView) findViewById(R.id.textViewMpcpTotal);
					cp.setText(productNum + "");
					String website = mUser.getWebsite();
					if (website != null) {
						((TextView) findViewById(R.id.textViewWebSite))
								.setText(getString(R.string.mp_wz) + website);
					}
					List<DreamVO> dreams = mUser.getDream();
					if (dreams != null) {
						String dreamStr = getString(R.string.mp_wdmx);
						for (DreamVO dream : dreams) {
							dreamStr += dream.getDream() + " ";
						}
						((TextView) findViewById(R.id.textViewMx))
								.setText(dreamStr);
						final String dreamsStr = dreamStr;
						findViewById(R.id.textViewMx).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View paramView) {
										Intent i = new Intent(
												UserCardActivity.this,
												TagListActivity.class);
										i.putExtra("name", name);
										String tempStr = dreamsStr;
										if (dreamsStr.endsWith(" ")) {
											tempStr = ZhuoCommHelper
													.subLast(dreamsStr);
										}
										String[] items = tempStr.split(" ");
										i.putExtra("title",
												getString(R.string.label_dream));
										i.putExtra("list", items);
										startActivity(i);
									}

								});

					}
					final String customer = mUser.getMycustomer();
					if (customer != null) {
						((TextView) findViewById(R.id.textViewWdkh))
								.setText(getString(R.string.mp_wdkh) + customer);
						findViewById(R.id.textViewWdkh).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View paramView) {
										Intent i = new Intent(
												UserCardActivity.this,
												TagListActivity.class);
										i.putExtra("name", name);
										String tempStr = customer;
										if (customer.endsWith(";")
												|| customer.endsWith("£º")
												|| customer.endsWith(" ")) {
											tempStr = ZhuoCommHelper
													.subLast(customer);
										}
										String[] items = tempStr.split(";|£»| ");
										i.putExtra(
												"title",
												getString(R.string.label_customer));
										i.putExtra("list", items);
										startActivity(i);
									}

								});
					}
					final String motto = mUser.getMaxim();
					if (motto != null) {
						((TextView) findViewById(R.id.textViewZym))
								.setText(getString(R.string.mp_zym) + motto);
						findViewById(R.id.textViewZym).setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View paramView) {
										Intent i = new Intent(
												UserCardActivity.this,
												TagListActivity.class);
										i.putExtra("name", name);
										String tempStr = motto;
										if (motto.endsWith(";")
												|| motto.endsWith("£º")
												|| motto.endsWith(" ")) {
											tempStr = ZhuoCommHelper
													.subLast(motto);
										}
										String[] items = tempStr.split(";|£»| ");
										i.putExtra("title",
												getString(R.string.label_zym));
										i.putExtra("list", items);
										startActivity(i);
									}

								});
					}
					final String hobby = mUser.getHobby();
					if (hobby != null) {
						TextView ahTV = (TextView) findViewById(R.id.textViewAh);
						ahTV.setText(getString(R.string.mp_ah)
								+ hobby.replaceAll(";", " "));
						ahTV.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								Intent i = new Intent(UserCardActivity.this,
										NormalListActivity.class);
								i.putExtra("name", name);
								String tempStr = hobby;
								if (hobby.endsWith(";") || hobby.endsWith("£º")
										|| hobby.endsWith(" ")) {
									tempStr = ZhuoCommHelper.subLast(hobby);
								}
								String[] items = tempStr.split(";|£»| ");
								for (int j = 0; j < items.length; j++) {
									items[j] = getString(R.string.label_hobby)
											+ NumberUtil.numberArab2CN(j + 1)
											+ ": " + items[j];
								}
								i.putExtra("list", items);
								startActivity(i);
							}

						});
					}
					String phone = "";
					if (mUser.getIsphoneopen() != null
							&& mUser.getIsphoneopen().equals("1")) {
						phone = mUser.getUserid();
					}
					((TextView) findViewById(R.id.textViewSjh))
							.setText(getString(R.string.mp_sjh) + phone);
					String email = mUser.getEmail();
					if (email != null) {
						((TextView) findViewById(R.id.textViewYx))
								.setText(getString(R.string.mp_yx) + email);
					}
					String friendsnum = mUser.getFamilytotal();
					if (null == friendsnum) {
						friendsnum = "0";
					}
					TextView fris = (TextView) findViewById(R.id.textViewWdjrTotal);
					fris.setText(friendsnum + "");
					String groupsnum = mUser.getGrouptotal();
					if (null == groupsnum) {
						groupsnum = "0";
					}
					TextView gros = (TextView) findViewById(R.id.textViewQuanTotal);
					gros.setText(groupsnum + "");
					List<UserVO> friends = mUser.getFamily();
					if (null != friends) {
						mFris = friends;
						mInit = true;
					}
					List<QuanVO> groups = mUser.getGroups();
					if (null != groups) {
						mGros = groups;
						mInit = true;
					}
					List<ZhuoInfoVO> actives = mUser.getGrowth();
					if (null != actives) {
						mActives = actives;
						mInit = true;
					}
					loadImage();
				}
				break;
			}
			case MsgTagVO.DATA_OTHER: {
				initImages();
				break;
			}
			case MsgTagVO.MSG_RCMDUSER: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					pwh.showPopTip(findViewById(R.id.scrollViewCard), null,
							R.string.label_recommandSuccess);
				}
				break;
			}
			case MsgTagVO.ADD_BACK: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (toBlack.equals("0")) {
						blackButton.setText(R.string.label_unblack);
						toBlack = "1";
					} else {
						blackButton.setText(R.string.label_black);
						toBlack = "0";
					}
				}
				break;
			}
			case MsgTagVO.PROSECUTE: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {

				}
				break;
			}
			case MsgTagVO.MSG_FOWARD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					if (isfollow.equals("0")) {
						isfollow = "1";
						pwh.showPopDlg(findViewById(R.id.scrollViewCard),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										sendCard();
									}
								}, null, R.string.label_followsendcard);
					} else {
						isfollow = "0";
					}
					resetFollowButton();
				}
				break;
			}
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					pwh.showPopDlgOne(findViewById(R.id.scrollViewCard), null,
							R.string.label_sendcardsuccess);
				}
			}
			}

		}

	};

	private void sendCard() {
		CardMsgVO msg = new CardMsgVO();
		UserVO sender = userFacade.getById(myid);
		if (sender == null) {
			FinishCallback callback = new FinishCallback() {

				@Override
				public boolean onReturn(String rs, int responseCode) {
					JsonHandler nljh = new JsonHandler(rs,
							getApplicationContext());
					UserVO user = nljh.parseUser();
					if (null != user) {
						userFacade.add(user);
						sendCard();
					}
					return false;
				}
			};
			mConnHelper.getFromServer(ZhuoCommHelper.getUrlUserInfo() + "?uid="
					+ myid, callback);
			return;
		}
		UserVO receiver = userFacade.getById(userid);
		if (receiver == null) {
			FinishCallback callback = new FinishCallback() {

				@Override
				public boolean onReturn(String rs, int responseCode) {
					JsonHandler nljh = new JsonHandler(rs,
							getApplicationContext());
					UserVO user = nljh.parseUser();
					if (null != user) {
						userFacade.add(user);
						sendCard();
					}
					return false;
				}
			};
			mConnHelper.getFromServer(ZhuoCommHelper.getUrlUserInfo() + "?uid="
					+ userid, callback);
			return;
		}
		String tempId = System.currentTimeMillis() + "";
		msg.setId(tempId);
		msg.setSender(sender);
		msg.setReceiver(receiver);
		msg.setState("send");
		msg.setIsread("1");
		msg.setLeavemsg("");
		msg.setIsopen("0");
		msg.setAddtime(CommonUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss"));
		mFacade.insert(msg);
		mConnHelper.sendCard(userid, "", mUIHandler, MsgTagVO.PUB_INFO, null,
				true, null, null);
	}

	private void loadInfo() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
			mUser = userFacade.getById(userid);
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
					UserCardActivity.this, true, new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							UserCardActivity.this.finish();
						}
					});
		}
	}

	private void loadImage() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (mInit) {
					try {
						Thread.sleep(1000);
						Message msg = mUIHandler
								.obtainMessage(MsgTagVO.DATA_OTHER);
						msg.sendToTarget();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void initImages() {
		if (mInit) {
			mInit = false;
			final List<PicVO> images = mPics;
			if (images == null && mFris == null && mGros == null
					&& mActives == null) {
				return;
			}
			if (images != null) {
				LinearLayout imagesContainer = (LinearLayout) findViewById(R.id.linearLayoutPicContainer);
				int w = imagesContainer.getMeasuredWidth();
				if (w == 0) {
					return;
				}
				int width = (w - 10) / 3;
				int marginRight = (w - width * 3) / 2;
				LayoutParams ivlp = new LayoutParams(width, width);
				ivlp.rightMargin = marginRight;
				ivlp.bottomMargin = marginRight;
				for (int i = 0; i < images.size(); i++) {
					if (i % 3 == 0) {
						LinearLayout ll = new LinearLayout(
								UserCardActivity.this);
						LayoutParams lp = new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT);
						ll.setLayoutParams(lp);
						imagesContainer.addView(ll);
					}
					ImageView iv = new ImageView(UserCardActivity.this);
					iv.setLayoutParams(ivlp);
					String headurl = images.get(i).getUrl();
					final String orgUrl = images.get(i).getOrgurl();
					iv.setTag(headurl);
					iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
					iv.setImageResource(R.drawable.default_image);
					mLoadImage.addTask(headurl, iv);
					((LinearLayout) imagesContainer.getChildAt(i / 3))
							.addView(iv);
					iv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent intent = new Intent(UserCardActivity.this,
									PhotoViewMultiActivity.class);
							ArrayList<String> orgs = new ArrayList<String>();
							for (int j = 0; j < images.size(); j++) {
								orgs.add(images.get(j).getOrgurl());
							}
							intent.putStringArrayListExtra("pics", orgs);
							intent.putExtra("pic", orgUrl);
							startActivity(intent);
						}
					});
				}
			}
			if (mFris != null) {
				LinearLayout imagesContainer = (LinearLayout) findViewById(R.id.linearLayoutWdjr);
				findViewById(R.id.viewJr).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								Intent intent = new Intent(
										UserCardActivity.this,
										UserListActivity.class);
								intent.putExtra("userid", userid);
								startActivity(intent);
							}
						});
				int w = imagesContainer.getMeasuredWidth();
				if (w == 0) {
					return;
				}
				int width = (w - 20) / 7;
				int marginRight = (w - width * 7) / 6;
				LayoutParams ivlp = new LayoutParams(width, width);
				ivlp.rightMargin = marginRight;
				for (int i = 0; i < mFris.size(); i++) {
					ImageView iv = new ImageView(UserCardActivity.this);
					iv.setLayoutParams(ivlp);
					String headurl = mFris.get(i).getUheader();
					iv.setTag(headurl);
					iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
					iv.setImageResource(R.drawable.default_userhead);
					mLoadImage.addTask(headurl, iv);
					imagesContainer.addView(iv);
				}
			}
			if (mGros != null) {
				LinearLayout imagesContainer = (LinearLayout) findViewById(R.id.linearLayoutQuan);
				findViewById(R.id.viewQuan).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								Intent intent = new Intent(
										UserCardActivity.this,
										QuanListMoreActivity.class);
								intent.putExtra("userid", userid);
								startActivity(intent);
							}
						});
				int w = imagesContainer.getMeasuredWidth();
				if (w == 0) {
					return;
				}
				int width = (w - 20) / 7;
				int marginRight = (w - width * 7) / 6;
				LayoutParams ivlp = new LayoutParams(width, width);
				ivlp.rightMargin = marginRight;
				for (int i = 0; i < mGros.size(); i++) {
					ImageView iv = new ImageView(UserCardActivity.this);
					iv.setLayoutParams(ivlp);
					String headurl = mGros.get(i).getGheader();
					iv.setTag(headurl);
					iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
					iv.setImageResource(R.drawable.default_grouphead);
					mLoadImage.addTask(headurl, iv);
					imagesContainer.addView(iv);
				}
			}
			if (mActives != null) {
				LinearLayout imagesContainer = (LinearLayout) findViewById(R.id.linearLayoutCz);
				findViewById(R.id.viewCz).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View paramView) {
								Intent intent = new Intent(
										UserCardActivity.this,
										UserHomeActivity.class);
								intent.putExtra("userid", userid);
								startActivity(intent);
							}
						});
				int w = imagesContainer.getMeasuredWidth();
				if (w == 0) {
					return;
				}
				int width = (w - 10) / 3;
				int marginRight = (w - width * 3) / 2;
				LayoutParams ivlp = new LayoutParams(width, width);
				ivlp.rightMargin = marginRight;
				for (int i = 0; i < mActives.size(); i++) {
					ZhuoInfoVO active = mActives.get(i);
					List<PicVO> pics = active.getPic();
					if (pics != null) {
						for (int j = 0; j < pics.size(); j++) {
							ImageView iv = new ImageView(UserCardActivity.this);
							iv.setLayoutParams(ivlp);
							String headurl = pics.get(j).getUrl();
							iv.setTag(headurl);
							iv.setImageResource(R.drawable.default_image);
							iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
							mLoadImage.addTask(headurl, iv);
							imagesContainer.addView(iv);
						}
					}
				}
			}
			mLoadImage.doTask();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case MsgTagVO.USER_SELECT:
				ArrayList<String> mSelectlist = data
						.getStringArrayListExtra("ids");
				if (mSelectlist != null && mSelectlist.size() > 0) {
					String useridlist = "";
					for (String id : mSelectlist) {
						useridlist += id + ";";
					}
					useridlist = ZhuoCommHelper.subLast(useridlist);
					mConnHelper.recommandUser(userid, useridlist, mUIHandler,
							MsgTagVO.MSG_RCMDUSER, null, true, null, null);
				}
				break;

			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
