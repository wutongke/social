package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.app.Activity;
import android.content.Intent;

public class CardAddUserWorkActivity extends Activity {
	private LinearLayout linearLayoutWorks;
	private ArrayList<String> companyTags = new ArrayList<String>();
	private ArrayList<String> workTags = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_work);
		linearLayoutWorks = (LinearLayout) findViewById(R.id.linearLayoutWorks);
		Intent intent = getIntent();
		String companys = intent
				.getStringExtra(CardEditActivity.EDIT_WORK_STR1);
		String works = intent.getStringExtra(CardEditActivity.EDIT_WORK_STR2);
		String isworking = intent
				.getStringExtra(CardEditActivity.EDIT_WORK_STR3);
		String isisentrepreneurship = intent
				.getStringExtra(CardEditActivity.EDIT_WORK_STR4);
		if (isisentrepreneurship != null && isisentrepreneurship.equals("1")) {
			((RadioButton) findViewById(R.id.radioIsEntrepreneurship))
					.setChecked(true);
		} else {
			((RadioButton) findViewById(R.id.radioNotEntrepreneurship))
					.setChecked(true);
		}
		if (isworking != null && isworking.equals("1")) {
			((RadioButton) findViewById(R.id.radioIsWorking)).setChecked(true);
		} else {
			((RadioButton) findViewById(R.id.radioNotWorking)).setChecked(true);
		}
		if (companys != null || works != null) {
			try {
				if (companys.indexOf(";") != -1) {
					String[] company = companys.split(";");
					String[] work = works.split(";");
					for (int i = 0; i < company.length && i < work.length; i++) {
						addWork(company[i], work[i]);
					}
				} else {
					addWork(companys, works);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			addWork("", "");
		}
		initClick();

	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserWorkActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String companys = "";
						String works = "";
						for (int i = 0; i < companyTags.size(); i++) {
							String company = ((EditText) linearLayoutWorks
									.findViewWithTag(companyTags.get(i)))
									.getText().toString();
							String work = ((EditText) linearLayoutWorks
									.findViewWithTag(workTags.get(i)))
									.getText().toString();
							companys += company + ";";
							works += work + ";";
						}
						if (companys.length() > 1) {
							companys = companys.substring(0,
									companys.length() - 1);
							works = works.substring(0, works.length() - 1);
						}
						Intent intent = new Intent();
						intent.putExtra(CardEditActivity.EDIT_WORK_STR1,
								companys);
						intent.putExtra(CardEditActivity.EDIT_WORK_STR2, works);
						if (((RadioButton) findViewById(R.id.radioIsWorking))
								.isChecked()) {
							intent.putExtra(CardEditActivity.EDIT_WORK_STR3,
									"1");
						} else {
							intent.putExtra(CardEditActivity.EDIT_WORK_STR3,
									"0");
						}
						if (((RadioButton) findViewById(R.id.radioIsEntrepreneurship))
								.isChecked()) {
							intent.putExtra(CardEditActivity.EDIT_WORK_STR4,
									"1");
						} else {
							intent.putExtra(CardEditActivity.EDIT_WORK_STR4,
									"0");
						}
						setResult(RESULT_OK, intent);
						CardAddUserWorkActivity.this.finish();
					}
				});
		findViewById(R.id.buttonAdd).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addWork("", "");
			}
		});
	}

	private void addWork(String company, String work) {
		LayoutInflater inflater = LayoutInflater
				.from(CardAddUserWorkActivity.this);
		View view = inflater.inflate(R.layout.item_work_add, null);
		EditText companyET = (EditText) view.findViewById(R.id.editTextCompany);
		companyET.setTag("company" + companyTags.size());
		if (null != company) {
			companyET.setText(company);
		}
		EditText workET = (EditText) view.findViewById(R.id.editTextWork);
		workET.setTag("work" + workTags.size());
		if (null != work) {
			workET.setText(work);
		}
		companyTags.add("company" + companyTags.size());
		workTags.add("work" + workTags.size());
		linearLayoutWorks.addView(view);
	}
}
