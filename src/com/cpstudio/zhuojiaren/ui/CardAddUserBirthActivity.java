package com.cpstudio.zhuojiaren.ui;

import java.text.ParseException;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.drawable;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.UserNewVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudui.zhuojiaren.lz.Lunar;
import com.utils.LundarToSolar;
import com.utils.SolarToLundar;

public class CardAddUserBirthActivity extends Activity {
	private boolean scrolling = false;
	private WheelView wheel1 = null;
	private WheelView wheel2 = null;
	private WheelView wheel3 = null;
	int constellation = 0, zodiac = 0;
	boolean isYangli = true;
	int isOpen = 0;

	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_success);
				} else
					CommonUtil.displayToast(getApplicationContext(),
							R.string.bg_failed);
				break;
			}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_birth);
		wheel1 = (WheelView) findViewById(R.id.wheelType1);
		wheel2 = (WheelView) findViewById(R.id.wheelType2);
		wheel3 = (WheelView) findViewById(R.id.wheelType3);
		initClick();
		Calendar solar = Calendar.getInstance();
		Intent i = getIntent();
		String birthday = i.getStringExtra(CardEditActivity.EDIT_BIRTH_STR1);
		int  birthdayopen = i
				.getIntExtra(CardEditActivity.EDIT_BIRTH_STR2,0);
		if (birthdayopen==1) {
			((RadioButton) (findViewById(R.id.radioPrivate))).setChecked(true);
			isOpen = 1;
		} else {
			((RadioButton) (findViewById(R.id.radioOpen))).setChecked(true);
			isOpen = 0;
		}
		((RadioGroup) this.findViewById(R.id.radioGroup))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						// 获取变更后的选中项的ID
						int radioButtonId = arg0.getCheckedRadioButtonId();
						if (radioButtonId == R.id.radioPrivate)
							isOpen = 1;
						else
							isOpen = 0;
					}
				});

		int year = solar.get(Calendar.YEAR);
		int month = solar.get(Calendar.MONTH) + 1;
		int date = solar.get(Calendar.DATE);
		if (birthday != null && birthday.indexOf("-") != -1) {
			birthday = birthday.trim();
			if (birthday.indexOf(" ") != -1) {
				birthday = birthday.substring(0, birthday.indexOf(" "));
			}
			String[] str = birthday.split("-");
			year = Integer.valueOf(str[0]);
			month = Integer.valueOf(str[1]);
			date = Integer.valueOf(str[2]);
		}
		gentBirtdayTextInfo(year, month, date, isYangli);
		initNormalWheel(year, month, date);
	}

	private void initClick() {
		findViewById(R.id.buttonOK).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int year = wheel1.getCurrentItem() + 1900;
				int month = wheel2.getCurrentItem() + 1;
				int day = wheel3.getCurrentItem() + 1;
				gentBirtdayTextInfo(year, month, day, isYangli);
			}
		});

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserBirthActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						int year = wheel1.getCurrentItem() + 1900;
						int month = wheel2.getCurrentItem() + 1;
						int day = wheel3.getCurrentItem() + 1;

						int[] solor = LundarToSolar.getLundarToSolar(year,
								month, day);
						UserNewVO userInfo = new UserNewVO();
						userInfo.setBirthday(year + "-" + month + "-" + day);
						userInfo.setZodiac(zodiac + 1);
						userInfo.setBirthdayLunar(solor[0] + "-" + solor[1]
								+ "-" + solor[2]);
						userInfo.setIsBirthdayOpen(isOpen);
						userInfo.setConstellation(constellation + 1);
						ConnHelper.getInstance(getApplicationContext())
								.modifyUserInfo(mUIHandler, MsgTagVO.DATA_LOAD,
										userInfo);
					}
				});
		findViewById(R.id.buttonYangli).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						isYangli = true;
						int year = wheel1.getCurrentItem() + 1900;
						int month = wheel2.getCurrentItem() + 1;
						int day = wheel3.getCurrentItem() + 1;
						int[] solar = LundarToSolar.getLundarToSolar(year,
								month, day);
						initNormalWheel(solar[0], solar[1], solar[2]);
						year = solar[0];
						month = solar[1];
						day = solar[2];
					}
				});
		findViewById(R.id.buttonYingli).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							isYangli = false;
							int year = wheel1.getCurrentItem() + 1900;
							int month = wheel2.getCurrentItem() + 1;
							int day = wheel3.getCurrentItem() + 1;
							int[] lunar = SolarToLundar.getLunar(year, month,
									day);
							initChieseWheel(lunar[0], lunar[1], lunar[2]);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				});
	}

	private void gentBirtdayTextInfo(int year, int month, int day,
			boolean yangli) {

		if (!yangli) {
			int[] solar = LundarToSolar.getLundarToSolar(year, month, day);
			year = solar[0];
			month = solar[1];
			day = solar[2];
			gentBirtdayTextInfo(year, month, day, true);
			return;
		}

		String monthStr = month + "";
		if (month < 10) {
			monthStr = "0" + monthStr;
		}
		String dayStr = day + "";
		if (day < 10) {
			dayStr = "0" + dayStr;
		}
		String solarText = "   " + year + "   "
				+ getString(R.string.label_year) + "   " + monthStr + "   "
				+ getString(R.string.label_month) + "   " + dayStr + "   "
				+ getString(R.string.label_day);
		int[] lunar = new int[3];
		try {
			lunar = SolarToLundar.getLunar(year, month, day);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		String month2Str = lunar[1] + "";
		if (lunar[1] < 10) {
			month2Str = "0" + month2Str;
		}
		String day2Str = lunar[2] + "";
		if (lunar[2] < 10) {
			day2Str = "0" + day2Str;
		}

		Calendar birth = Calendar.getInstance();
		birth.set(year, month, day);
		Lunar myLunar = new Lunar(birth);
		((TextView) findViewById(R.id.textViewLunar))
				.setText(getString(R.string.mp_yilsr) + myLunar.getLunarStr());
		zodiac = myLunar.getAnimalsYear();
		((TextView) findViewById(R.id.textViewShuXiang))
				.setText(Lunar.Animals[zodiac]);
		constellation = Lunar.getConstellation(birth.getTime());
		((TextView) findViewById(R.id.textViewXinzuo))
				.setText(Lunar.constellationArr[constellation]);

		((TextView) findViewById(R.id.textViewSolar))
				.setText(getString(R.string.mp_yalsr) + solarText);
	}

	// 阳历
	private void initNormalWheel(int initYear, int initMonth, int initDate) {
		final String[] type1 = new String[150];
		for (int i = 1900; i < 2050; i++) {
			type1[i - 1900] = i + "";
		}
		final String[] type2 = new String[12];
		for (int i = 1; i < 13; i++) {
			if (i < 10) {
				type2[i - 1] = "0" + i;
			} else {
				type2[i - 1] = i + "";
			}
		}
		wheel1.setVisibleItems(type1.length);
		final ArrayWheelAdapter<String> adapter1 = new ArrayWheelAdapter<String>(
				CardAddUserBirthActivity.this, type1);
		adapter1.setTextSize(20);
		wheel1.setViewAdapter(adapter1);
		wheel2.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int current) {
				if (!scrolling) {
					int currentYear = wheel1.getCurrentItem();
					int year = currentYear + 1900;
					int month = current + 1;
					int day = LundarToSolar.iGetSYearMonthDays(year, month);
					String[] days = new String[day];
					for (int i = 0; i < day; i++) {
						if (i < 9) {
							days[i] = "0" + (i + 1);
						} else {
							days[i] = (i + 1) + "";
						}
					}
					updateDate(wheel3, days);
				}
			}
		});
		wheel2.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				int currentYear = wheel1.getCurrentItem();
				int year = currentYear + 1900;
				int month = wheel.getCurrentItem() + 1;
				int day = LundarToSolar.iGetSYearMonthDays(year, month);
				String[] days = new String[day];
				for (int i = 0; i < day; i++) {
					if (i < 9) {
						days[i] = "0" + (i + 1);
					} else {
						days[i] = (i + 1) + "";
					}
				}
				updateDate(wheel3, days);
			}
		});
		wheel1.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int current) {
				if (!scrolling) {
					updateDate(wheel2, type2);
				}
			}
		});
		wheel1.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				updateDate(wheel2, type2);
			}
		});
		wheel1.setCurrentItem(0);
		wheel1.setCurrentItem(initYear - 1900);
		wheel2.setCurrentItem(0);
		wheel2.setCurrentItem(initMonth - 1);
		wheel3.setCurrentItem(initDate - 1);


		findViewById(R.id.buttonYingli).setBackgroundResource(
				R.drawable.button_lunar_off);
		findViewById(R.id.buttonYangli).setBackgroundResource(
				R.drawable.button_solar_on);
		findViewById(R.id.buttonYingli).setEnabled(true);
		findViewById(R.id.buttonYangli).setEnabled(false);
	}

	private void initChieseWheel(int initYear, int initMonth, int initDate) {
		final String[] type1 = new String[150];
		for (int i = 1900; i < 2050; i++) {
			type1[i - 1900] = SolarToLundar.getChinaYearString(i);
		}
		wheel1.setVisibleItems(type1.length);
		final ArrayWheelAdapter<String> adapter1 = new ArrayWheelAdapter<String>(
				CardAddUserBirthActivity.this, type1);
		adapter1.setTextSize(20);
		wheel1.setViewAdapter(adapter1);
		wheel2.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int current) {
				if (!scrolling) {
					int currentYear = wheel1.getCurrentItem();
					int year = currentYear + 1900;
					int leapMonth = SolarToLundar.leapMonth(year);
					if (leapMonth > 0) {
						if (current == leapMonth) {
							current = 0;
						} else if (current < leapMonth) {
							current = current + 1;
						}
					} else {
						current = current + 1;
					}
					String[] days = getDayArray(year, current);
					updateDate(wheel3, days);
				}
			}
		});
		wheel2.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				int current = wheel.getCurrentItem();
				int currentYear = wheel1.getCurrentItem();
				int year = currentYear + 1900;
				int leapMonth = SolarToLundar.leapMonth(year);
				if (leapMonth > 0) {
					if (current == leapMonth) {
						current = 0;
					} else if (current < leapMonth) {
						current = current + 1;
					}
				} else {
					current = current + 1;
				}
				String[] days = getDayArray(year, current);
				updateDate(wheel3, days);
			}
		});
		wheel1.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int current) {
				if (!scrolling) {
					int year = current + 1900;
					String[] months = getMonthArray(year);
					updateDate(wheel2, months);
				}
			}
		});
		wheel1.addScrollingListener(new OnWheelScrollListener() {
			public void onScrollingStarted(WheelView wheel) {
				scrolling = true;
			}

			public void onScrollingFinished(WheelView wheel) {
				scrolling = false;
				int current = wheel.getCurrentItem();
				int year = current + 1900;
				String[] months = getMonthArray(year);
				updateDate(wheel2, months);
			}
		});
		wheel1.setCurrentItem(0);
		wheel1.setCurrentItem(initYear - 1900);
		wheel2.setCurrentItem(0);
		wheel2.setCurrentItem(initMonth - 1);
		wheel3.setCurrentItem(initDate - 1);


		findViewById(R.id.buttonYingli).setBackgroundResource(
				R.drawable.button_lunar_on);
		findViewById(R.id.buttonYangli).setBackgroundResource(
				R.drawable.button_solar_off);
		findViewById(R.id.buttonYingli).setEnabled(false);
		findViewById(R.id.buttonYangli).setEnabled(true);
	}

	private void updateDate(WheelView wheelView, String str[]) {
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
				str);
		adapter.setTextSize(20);
		wheelView.setViewAdapter(adapter);
		wheelView.setVisibleItems(str.length);
		wheelView.setCurrentItem(str.length / 2);
	}

	private String[] getMonthArray(int year) {
		int allMonth = 12, leapMonth = SolarToLundar.leapMonth(year);
		if (leapMonth > 0) {
			allMonth = 13;
		}
		String[] type = new String[allMonth];
		for (int i = 0; i < allMonth; i++) {
			if (leapMonth > 0 && leapMonth == i) {
				type[i] = getString(R.string.label_run)
						+ SolarToLundar.getChinaNumber(i);
			} else if (leapMonth > 0 && i >= leapMonth) {
				type[i] = SolarToLundar.getChinaMonthString(i)
						+ getString(R.string.label_month);
			} else {
				type[i] = SolarToLundar.getChinaMonthString(i + 1)
						+ getString(R.string.label_month);
			}
		}
		return type;
	}

	private String[] getDayArray(int year, int month) {
		int alldays = 0;
		if (month == 0) {
			alldays = SolarToLundar.leapDays(year);
		} else {
			alldays = SolarToLundar.monthDays(year, month);
		}
		String[] type = new String[alldays];
		for (int i = 0; i < alldays; i++) {
			type[i] = SolarToLundar.getChinaDayString(i + 1);
		}
		return type;
	}
}
