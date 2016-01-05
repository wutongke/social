package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper.EditMODE;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.BaseCodeData;
import com.cpstudio.zhuojiaren.model.City;
import com.cpstudio.zhuojiaren.model.CompanyNewVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.industry;
import com.cpstudio.zhuojiaren.model.position;
import com.cpstudio.zhuojiaren.ui.BaseActivity;
import com.cpstudio.zhuojiaren.ui.CardEditActivity;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.PlaceChooseDialog;
/**
 * 企业详细信息界面
 * @author lz
 *
 */
public class CompanyDetailActivity extends BaseActivity {
	
	@InjectView(R.id.editTextCompany)
	EditText editTextCompany;
	@InjectView(R.id.tvIndustry)
	TextView tvIndustry;
	@InjectView(R.id.tvCompanyLocal)
	TextView tvCompanyLocal;
	@InjectView(R.id.tvPosition)
	TextView tvPosition;
	@InjectView(R.id.editTextWeb)
	EditText editTextWeb;
	@InjectView(R.id.textViewEditMainProduce)
	View vMainProduct;
	@InjectView(R.id.btnAdd)
	Button btnAdd;
	@InjectView(R.id.btnModify)
	Button btnModify;
	@InjectView(R.id.btnDelete)
	Button btnDelete;
	@InjectView(R.id.lv_company)
	ListView lv_company;
	EditMODE edtMode = EditMODE.VIEW;
	private ConnHelper mConnHelper = null;
	CommonAdapter<CompanyNewVO> mAdapter;
	List<CompanyNewVO> companyList = new ArrayList<CompanyNewVO>();
	// BaseCodeData codeDatas;
	List<City> cityList;
	CompanyNewVO catchCompany = new CompanyNewVO();
	int curIndex = 0;
	String[] industryArray;
	String[] positionsArray;
	boolean isEditable;
	String userid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_company_detail);
		ButterKnife.inject(this);
		initTitle();
		function.setVisibility(View.VISIBLE);
		function.setText(R.string.SAVE);
		function.setBackgroundResource(R.drawable.button_save);
		title.setText(R.string.title_activity_card_add_user_work);

		mConnHelper = ConnHelper.getInstance(getApplicationContext());
		isEditable = getIntent().getBooleanExtra(CardEditActivity.EDITABLE,
				false);
		userid = getIntent().getStringExtra(CardEditActivity.USERID);
		cityList = mConnHelper.getCitys();
		prepareData();
		initOnClick();
		loadData();
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		tvCompanyLocal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final PlaceChooseDialog placeChoose = new PlaceChooseDialog(
						CompanyDetailActivity.this,
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, "北京", "北京");
				placeChoose.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								String txt = placeChoose.getPlace().getText()
										.toString();
								tvCompanyLocal.setText(txt);
								String[] strs = txt.split(" ");
								if (strs != null && strs.length > 1) {
									catchCompany
											.setCity(findCityIdByName(strs[1]));
								}
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
				placeChoose.setTitle(R.string.choose_place);
				placeChoose.show();
			}
		});

		tvIndustry.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(CompanyDetailActivity.this,
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle("选择行业")
						.setIcon(R.drawable.ico_syzy)
						.setSingleChoiceItems(industryArray, 0,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										catchCompany.setIndustry(which + 1);
										// tvIndustry.setText(industryArray[which]);
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										if (tvIndustry == null)
											return;
										if (catchCompany.getIndustry() <= 0)
											catchCompany.setIndustry(1);
										tvIndustry
												.setText(industryArray[catchCompany
														.getIndustry() - 1]);
									}
								}).setNegativeButton("取消", null).create()
						.show();

			}
		});
		vMainProduct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edtMode == EditMODE.ADD) {
					CommonUtil.displayToast(getApplicationContext(),
							"请先保存此公司后再编辑产品！");
					return;
				}
				Intent intent = new Intent(CompanyDetailActivity.this,
						ProductDetailActivity.class);
				intent.putExtra("commpanyId", catchCompany.getComid());
				intent.putExtra(CardEditActivity.EDITABLE, isEditable);
				CompanyDetailActivity.this.startActivity(intent);
			}
		});
		tvPosition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(CompanyDetailActivity.this,
						AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
						.setTitle("选择职务")
						.setIcon(R.drawable.ico_syzy)
						.setSingleChoiceItems(positionsArray, 0,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										catchCompany.setPosition(which + 1);
										// tvIndustry.setText(industryArray[which]);
									}
								})
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										if (positionsArray == null)
											return;
										tvPosition
												.setText(positionsArray[catchCompany
														.getPosition() - 1]);
									}
								}).setNegativeButton("取消", null).create()
						.show();

			}
		});
		if (!isEditable) {
			function.setVisibility(View.GONE);
			findViewById(R.id.editMenue).setVisibility(View.GONE);
			vMainProduct.setEnabled(true);
		} else {
			function.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (edtMode == EditMODE.VIEW)
						return;
					else if (edtMode == EditMODE.ADD) {
						mConnHelper.addCompany(mUIHandler, MsgTagVO.PUB_INFO,
								editTextCompany.getText().toString(),
								catchCompany.getIndustry(),
								catchCompany.getCity(),
								catchCompany.getPosition(),
								catchCompany.getHomepage(), 0);
					} else {
						mConnHelper.updateCompany(mUIHandler, MsgTagVO.UPDATE,
								catchCompany.getComid(), editTextCompany
										.getText().toString(), catchCompany
										.getIndustry(), catchCompany.getCity(),
								catchCompany.getPosition(), editTextWeb
										.getText().toString(), 0);
					}
				}
			});
			btnAdd.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					edtMode = EditMODE.ADD;
					fillItemInfo(-1);
					setEnable(true);
				}
			});
			btnModify.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					edtMode = EditMODE.EDIT;
					setEnable(true);
				}
			});
			btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mConnHelper.deleteCompanyInfo(mUIHandler,
							MsgTagVO.DATA_OTHER, catchCompany.getComid());
				}
			});
		}

	}

	void prepareData() {
		BaseCodeData codeDatas = mConnHelper.getBaseDataSet();
		if (codeDatas == null)
			return;
		if (codeDatas.getIndustry() != null) {
			List<String> list = new ArrayList<String>();
			for (industry item : codeDatas.getIndustry())
				list.add(item.getContent());
			// 这个参数不可少new String[list.size()]
			industryArray = (String[]) list.toArray(new String[list.size()]);
		}
		if (codeDatas.getPosition() != null) {
			List<String> list = new ArrayList<String>();
			for (position item : codeDatas.getPosition())
				list.add(item.getContent());
			positionsArray = (String[]) list.toArray(new String[list.size()]);
		}
	}

	int findCityIdByName(String name) {
		int id = 1;
		for (City c : cityList) {
			if (c.getCityName().contains(name)) {
				try {
					id = Integer.parseInt(c.getCityId());
					break;
				} catch (Exception e) {
					// TODO: handle exception
					id = 1;
				}
			}
		}
		return id;
	}

	void clear() {
		editTextCompany.setText("");
		tvCompanyLocal.setText("");
		tvIndustry.setText("");
		tvPosition.setText("");
		editTextWeb.setText("");
	}

	void setEnable(boolean flag) {
		editTextCompany.setEnabled(flag);
		tvCompanyLocal.setEnabled(flag);
		tvIndustry.setEnabled(flag);
		tvPosition.setEnabled(flag);
		editTextWeb.setEnabled(flag);
		if (isEditable)
			vMainProduct.setEnabled(flag);
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getCompanyInfo(mUIHandler, MsgTagVO.DATA_LOAD, userid);
		}
	}

	void fillItemInfo(int i) {
		if (i == -1) {
			catchCompany = new CompanyNewVO();
			clear();
			return;
		}
		if (companyList == null || i < 0 || i >= companyList.size())
			return;
		CompanyNewVO item = companyList.get(i);

		catchCompany.setComid(item.getComid());
		catchCompany.setCity(item.getCity());
		catchCompany.setCompany(item.getCompany());
		catchCompany.setHomepage(item.getHomepage());
		catchCompany.setIndustry(item.getIndustry());
		catchCompany.setPosition(item.getPosition());
		catchCompany.setStatus(item.getStatus());

		fillNotNullData(editTextCompany, item.getCompany());
		fillNotNullData(editTextWeb, item.getHomepage());

		int industry = item.getIndustry();
		if (industryArray != null && industry >= 1
				&& industry <= industryArray.length) {
			fillNotNullData(tvIndustry, industryArray[industry - 1]);
		}

		int position = item.getPosition();
		if (positionsArray != null && position >= 1
				&& position <= positionsArray.length) {
			fillNotNullData(tvPosition, positionsArray[position - 1]);
		}

		int city = item.getCity();
		if (city >= 1 && city <= cityList.size()) {
			fillNotNullData(tvCompanyLocal, cityList.get(city - 1)
					.getCityName());
		}
		setEnable(false);
	}

	void fillNotNullData(TextView tv, String text) {
		if (tv != null && text != null) {
			tv.setText(text);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (msg.obj instanceof List<?>)// 加载的本地数据
				{
					companyList = (List<CompanyNewVO>) msg.obj;
				} else if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					companyList.clear();
					companyList.addAll(nljh.parseCompanyInfoList());
					resetListVIew();
					fillItemInfo(companyList.size() - 1);
				}
				break;
			case MsgTagVO.DATA_OTHER:// 删除
			case MsgTagVO.UPDATE:// 更新
			case MsgTagVO.PUB_INFO:// 增加
				if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					loadData();
					CommonUtil.displayToast(getApplicationContext(),
							R.string.label_success);
				} else {
					CommonUtil.displayToast(getApplicationContext(),
							R.string.FAILED);
				}
				break;
			}
		}
	};

	private void resetListVIew() {
		mAdapter = new CommonAdapter<CompanyNewVO>(CompanyDetailActivity.this,
				companyList, R.layout.list_item_simple_text) {
			@Override
			public void convert(ViewHolder helper, CompanyNewVO item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.itemId, item.getComid());
				helper.setText(R.id.itemName, item.getCompany());
			}
		};
		lv_company.setAdapter(mAdapter);
		lv_company.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				curIndex = position;
				fillItemInfo(position);
				setEnable(false);
				edtMode = EditMODE.VIEW;
			}
		});
	}

}
