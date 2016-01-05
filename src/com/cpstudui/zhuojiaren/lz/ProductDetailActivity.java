package com.cpstudui.zhuojiaren.lz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
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
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.CardEditActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.ImageSelectHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.ConnHelper.EditMODE;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicNewVO;
import com.cpstudio.zhuojiaren.model.ProductNewVO;
import com.cpstudio.zhuojiaren.ui.BaseActivity;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class ProductDetailActivity extends BaseActivity {
	@InjectView(R.id.edtProductName)
	EditText edtProductName;

	@InjectView(R.id.editTextDetail)
	EditText editTextDetail; // 产品描述

	@InjectView(R.id.editTextTargetClient)
	EditText editTextTargetClient;
	@InjectView(R.id.editTextValue)
	EditText editTextValue;

	@InjectView(R.id.btnAdd)
	Button btnAdd;
	@InjectView(R.id.btnModify)
	Button btnModify;
	@InjectView(R.id.btnDelete)
	Button btnDelete;

	@InjectView(R.id.lv_product)
	ListView lv_product;
	private PopupWindows pwh = null;
	private ImageSelectHelper mIsh = null;
	private Map<String, String> network = new HashMap<String, String>();
	private ArrayList<String> netids = new ArrayList<String>();
	private ArrayList<String> neturls = new ArrayList<String>();
	private Map<String, View> toDelView = new HashMap<String, View>();
	private ArrayList<String> local = new ArrayList<String>();

	ArrayList<String> localImages = new ArrayList<String>();
	ArrayList<String> urlImages = new ArrayList<String>();

	EditMODE edtMode = EditMODE.VIEW;
	private ConnHelper mConnHelper = null;
	CommonAdapter<ProductNewVO> mAdapter;
	LoadImage mLoadImage = LoadImage.getInstance();
	List<ProductNewVO> productList = new ArrayList<ProductNewVO>();
	ProductNewVO catchProduct = new ProductNewVO();
	int curIndex = 0;
	String commpanyId = "";
	boolean isEditable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		ButterKnife.inject(this);
		initTitle();
		function.setVisibility(View.VISIBLE);
		function.setText(R.string.SAVE);
		function.setBackgroundResource(R.drawable.button_save);
		title.setText(R.string.title_activity_main_product);
		commpanyId = getIntent().getStringExtra("commpanyId");
		mConnHelper = ConnHelper.getInstance(getApplicationContext());

		pwh = new PopupWindows(ProductDetailActivity.this);
		mIsh = ImageSelectHelper.getIntance(ProductDetailActivity.this,
				R.id.linearLayoutPicContainer);
		isEditable = getIntent().getBooleanExtra(CardEditActivity.EDITABLE,
				false);
		initISH(false);
		initOnClick();
		loadData();
	}

	void initISH(boolean flag)
	{
		if(flag)
		mIsh.clear();
		if(isEditable)
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
	}
	
	
	private void initSelecter() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (mIsh.isInit()) {
						mIsh.initParams();
						Thread.sleep(20);
					}
					Message msg = mUIHandler
							.obtainMessage(MsgTagVO.INIT_SELECT);
					msg.sendToTarget();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void initOnClick() {
		// TODO Auto-generated method stub
		if (!isEditable) {
			function.setVisibility(View.GONE);
			findViewById(R.id.editMenue).setVisibility(View.GONE);
		} else {
		function.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edtMode == EditMODE.VIEW)
					return;
				else if (edtMode == EditMODE.ADD) {
					getFileStrings();
					mConnHelper.addProduct(ProductDetailActivity.this,
							mUIHandler, MsgTagVO.PUB_INFO, commpanyId,
							edtProductName.getText().toString(), editTextDetail
									.getText().toString(), editTextTargetClient
									.getText().toString(), editTextValue
									.getText().toString(), localImages,
							getPhotoStr(urlImages));
				} else {
					getFileStrings();
					mConnHelper.updateProduct(ProductDetailActivity.this,
							mUIHandler, MsgTagVO.UPDATE, catchProduct
									.getProductid(), edtProductName.getText()
									.toString(), editTextDetail.getText()
									.toString(), editTextTargetClient.getText()
									.toString(), editTextValue.getText()
									.toString(),localImages,
									getPhotoStr(urlImages));
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
				mConnHelper.deleteProduct(mUIHandler, MsgTagVO.DATA_OTHER,
						catchProduct.getProductid());
			}
		});
		}
	}

	void clear() {
		edtProductName.setText("");
		editTextDetail.setText("");
		editTextTargetClient.setText("");
		editTextValue.setText("");
		edtProductName.setText("");
	}

	void setEnable(boolean flag) {
		edtProductName.setEnabled(flag);
		editTextDetail.setEnabled(flag);
		editTextTargetClient.setEnabled(flag);
		editTextValue.setEnabled(flag);
		edtProductName.setEnabled(flag);
	}

	private void loadData() {
		if (CommonUtil.getNetworkState(getApplicationContext()) == 2) {
		} else {
			mConnHelper.getCompanyProduct(mUIHandler, MsgTagVO.DATA_LOAD,
					commpanyId);
		}
	}

	void fillItemInfo(int i) {
		network.clear();
		netids.clear();
		neturls.clear();
		toDelView.clear();
		initISH(true);
		initSelecter();
		if (i == -1) {
			catchProduct = new ProductNewVO();
			clear();
			return;
		}
		if (productList == null || i < 0 || i >= productList.size())
			return;
		ProductNewVO item = productList.get(i);

		catchProduct.setComid(item.getComid());
		catchProduct.setCustomer(item.getCustomer());
		catchProduct.setDescription(item.getDescription());
		catchProduct.setProduct(item.getProduct());
		catchProduct.setProductid(item.getProductid());
		catchProduct.setValue(item.getValue());
		catchProduct.setProductPic(item.getProductPic());

		fillNotNullData(edtProductName, item.getProduct());
		fillNotNullData(editTextDetail, item.getDescription());
		fillNotNullData(editTextTargetClient, item.getCustomer());
		fillNotNullData(editTextValue, item.getValue());

		setEnable(false);

		network.clear();
		netids.clear();
		neturls.clear();

		for (PicNewVO pic : item.getProductPic()) {
			network.put(pic.getPic(), pic.getPic());
			netids.add(pic.getPic());
			neturls.add(pic.getPic());
		}

		initSelecter();
	}

	void fillNotNullData(EditText tv, String text) {
		if (tv != null && text != null) {
			tv.setText(text);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.INIT_SELECT:
				mIsh.insertNetworkImage(netids, neturls, mLoadImage,
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								String id = (String) v.getTag();
								toDelView.put(id, v);
								mIsh.removeFromContainer(toDelView.get(id));
							}
						}, null);
				mIsh.insertLocalImage(local);
				break;
			case MsgTagVO.DATA_LOAD:
				if (msg.obj instanceof List<?>)// 加载的本地数据
				{
					productList = (List<ProductNewVO>) msg.obj;
				} else if (JsonHandler.checkResult((String) msg.obj,
						getApplicationContext())) {
					JsonHandler nljh = new JsonHandler((String) msg.obj,
							getApplicationContext());
					productList.clear();
					productList.addAll(nljh.parseProductInfoList());
					resetListVIew();
					fillItemInfo(productList.size() - 1);
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
		mAdapter = new CommonAdapter<ProductNewVO>(ProductDetailActivity.this,
				productList, R.layout.list_item_simple_text) {
			@Override
			public void convert(ViewHolder helper, ProductNewVO item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.itemId, item.getProductid());
				helper.setText(R.id.itemName, item.getProduct());
			}
		};
		lv_product.setAdapter(mAdapter);
		lv_product.setOnItemClickListener(new OnItemClickListener() {
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


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		String filePath = pwh.dealPhotoReturn(requestCode, resultCode, data,
				false);
		if (filePath != null) {
			try {
				mIsh.insertLocalImage(filePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void getFileStrings() {
		localImages.clear();
		urlImages.clear();
		for (String tag : mIsh.getTags()) {
			if (network.containsKey(tag)) {
				urlImages.add(tag);
			} else {
				localImages.add(tag);
			}
		}
	}

	String getPhotoStr(ArrayList<String> pics) {
		StringBuilder sb = new StringBuilder();
		if (pics == null || pics.size() < 1) {
			return sb.toString();
		}
		for (String item : pics) {
			sb.append(item);
			sb.append(",");
		}
		return sb.toString();
	}
}
