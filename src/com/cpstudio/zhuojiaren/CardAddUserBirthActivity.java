package com.cpstudio.zhuojiaren;

import java.text.ParseException;
import java.util.Calendar;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cpstudui.zhuojiaren.lz.Lunar;
import com.utils.LundarToSolar;
import com.utils.SolarToLundar;

public class CardAddUserBirthActivity extends Activity {
	private boolean scrolling = false;
	// private OnClickListener yingOnClickListener = null;
	// private OnClickListener yangOnClickListener = null;
	private WheelView wheel1 = null;
	private WheelView wheel2 = null;
	private WheelView wheel3 = null;
	// private boolean mLunar = false;
	int constellation = 0, zodiac = 0;
	boolean isYangli = true;
	int isOpen = 0;

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
		String birthdayopen = i
				.getStringExtra(CardEditActivity.EDIT_BIRTH_STR2);
		// String birthdayLunar = i
		// .getStringExtra(CardEditActivity.EDIT_BIRTH_STR3);// 阴历生日
		if (birthdayopen != null && birthdayopen.equals("0")) {
			((RadioButton) (findViewById(R.id.radioPrivate))).setChecked(true);
		} else {
			((RadioButton) (findViewById(R.id.radioOpen))).setChecked(true);
		}
		((RadioGroup) this.findViewById(R.id.radioGroup))
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup arg0, int arg1) {
						// TODO Auto-generated method stub
						// 获取变更后的选中项的ID
						int radioButtonId = arg0.getCheckedRadioButtonId();
						if (radioButtonId == R.id.radioPrivate)
							isOpen = 0;
						else
							isOpen = 1;
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

			gentBirtdayTextInfo(year, month, date, isYangli);
			initNormalWheel(year, month, date);
		}

		// if (birthdayLunar != null) {
		// initChineseWheelBySolar(year, month, date);
		// }
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
						Intent intent = new Intent();

						intent.putExtra(CardEditActivity.EDIT_BIRTH_STR2,
								isOpen);

						intent.putExtra(CardEditActivity.EDIT_BIRTH_STR4,
								constellation + 1);
						intent.putExtra(CardEditActivity.EDIT_BIRTH_STR5,
								zodiac + 1);
						int[] solor = LundarToSolar.getLundarToSolar(year,
								month, day);
						intent.putExtra(CardEditActivity.EDIT_BIRTH_STR3,
								solor[0] + "-" + solor[1] + "-" + solor[2]);
						intent.putExtra(CardEditActivity.EDIT_BIRTH_STR1, year
								+ "-" + month + "-" + day);
						setResult(RESULT_OK, intent);
						CardAddUserBirthActivity.this.finish();
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
		// String text = "   " + lunar[0] + "   " +
		// getString(R.string.label_year)
		// + "   " + month2Str + "   " + getString(R.string.label_month)
		// + "   " + day2Str + "   " + getString(R.string.label_day);

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

		// 还要设置其对应的动物图片
		// ((TextView) findViewById(R.id.textViewShuXiang))
		// .setCompoundDrawables(left, top, right, bottom);

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

		// if (yangOnClickListener == null) {
		// yangOnClickListener = new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// int year = wheel1.getCurrentItem() + 1900;
		// int month = wheel2.getCurrentItem() + 1;
		// int day = wheel3.getCurrentItem() + 1;
		// gentBirtdayTextInfo(year, month, day, isYangli);
		// }
		// };
		// }
		// findViewById(R.id.buttonOK).setOnClickListener(yangOnClickListener);

		findViewById(R.id.buttonYingli).setBackgroundResource(
				R.drawable.button_lunar_off);
		findViewById(R.id.buttonYangli).setBackgroundResource(
				R.drawable.button_solar_on);
		findViewById(R.id.buttonYingli).setEnabled(true);
		findViewById(R.id.buttonYangli).setEnabled(false);
//		mLunar = false;
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

		// if (yingOnClickListener == null) {
		// yingOnClickListener = new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// int year = wheel1.getCurrentItem() + 1900;
		// int month = wheel2.getCurrentItem();
		// int leapMonth = SolarToLundar.leapMonth(year);
		// if (month < leapMonth || leapMonth == 0) {
		// month += 1;
		// }
		// int day = wheel3.getCurrentItem() + 1;
		// String monthStr = month + "";
		// if (month < 10) {
		// monthStr = "0" + monthStr;
		// }
		// String dayStr = day + "";
		// if (day < 10) {
		// dayStr = "0" + dayStr;
		// }
		// String text = "   " + year + "   "
		// + getString(R.string.label_year) + "   " + monthStr
		// + "   " + getString(R.string.label_month) + "   "
		// + dayStr + "   " + getString(R.string.label_day);
		// int[] solar = LundarToSolar.getLundarToSolar(year, month,
		// day);
		// String month2Str = solar[1] + "";
		// if (solar[1] < 10) {
		// month2Str = "0" + month2Str;
		// }
		// String day2Str = solar[2] + "";
		// if (solar[2] < 10) {
		// day2Str = "0" + day2Str;
		// }
		// String solarText = "   " + solar[0] + "   "
		// + getString(R.string.label_year) + "   "
		// + month2Str + "   "
		// + getString(R.string.label_month) + "   " + day2Str
		// + "   " + getString(R.string.label_day);
		// ((TextView) findViewById(R.id.textViewLunar))
		// .setText(getString(R.string.mp_yilsr) + text);
		// ((TextView) findViewById(R.id.textViewSolar))
		// .setText(getString(R.string.mp_yalsr) + solarText);
		// }
		// };
		// }
		// findViewById(R.id.buttonOK).setOnClickListener(yingOnClickListener);

		findViewById(R.id.buttonYingli).setBackgroundResource(
				R.drawable.button_lunar_on);
		findViewById(R.id.buttonYangli).setBackgroundResource(
				R.drawable.button_solar_off);
		findViewById(R.id.buttonYingli).setEnabled(false);
		findViewById(R.id.buttonYangli).setEnabled(true);
//		mLunar = true;
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
