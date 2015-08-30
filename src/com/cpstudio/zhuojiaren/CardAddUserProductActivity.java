package com.cpstudio.zhuojiaren;

import java.util.ArrayList;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.ZhuoConnHelper;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ProductVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CardAddUserProductActivity extends Activity {
//产品添加图片等还未做
	private LinearLayout linearLayout;
	private ArrayList<String> titleTags = new ArrayList<String>();
	private ArrayList<String> detailTags = new ArrayList<String>();
	private ArrayList<String> valueTags = new ArrayList<String>();
	private ArrayList<ProductVO> mProducts = new ArrayList<ProductVO>();
	private PopupWindows pwh = null;
	private ZhuoConnHelper mConnHelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_product);
		mConnHelper = ZhuoConnHelper.getInstance(getApplicationContext());
		pwh = new PopupWindows(CardAddUserProductActivity.this);
//		Intent i = getIntent();
//		mProducts = i
//				.getParcelableArrayListExtra(CardEditActivity.EDIT_PRODUCT_STR);
//		linearLayout = (LinearLayout) findViewById(R.id.linearLayoutProducts);
//		EditText title = (EditText) findViewById(R.id.editTextTitle);
//		title.setTag("title0");
//		titleTags.add("title0");
//		EditText detail = (EditText) findViewById(R.id.editTextDetail);
//		detail.setTag("detail0");
//		detailTags.add("detail0");
//		EditText value = (EditText) findViewById(R.id.editTextValue);
//		value.setTag("value0");
//		valueTags.add("value0");
//		if (mProducts != null && mProducts.size() > 0) {
//			title.setText(mProducts.get(0).getTitle());
//			detail.setText(mProducts.get(0).get_desc());
//			value.setText(mProducts.get(0).get_value());
//			for (int j = 1; j < mProducts.size(); j++) {
//				addProduct(mProducts.get(j));
//			}
//		}
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CardAddUserProductActivity.this.finish();
			}
		});
		findViewById(R.id.buttonSubmit).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						String productStr = "[";
						ArrayList<ProductVO> productVOs = new ArrayList<ProductVO>();
						for (int i = 0; i < titleTags.size(); i++) {
							ProductVO productVO = new ProductVO();
							String title = ((EditText) linearLayout
									.findViewWithTag(titleTags.get(i)))
									.getText().toString();
							productVO.setTitle(title);
							productStr += "{\"title\":\"" + title + "\",";
							String detail = ((EditText) linearLayout
									.findViewWithTag(detailTags.get(i)))
									.getText().toString();
							productVO.set_desc(detail);
							productStr += "\"_desc\":\"" + detail + "\",";
							String value = ((EditText) linearLayout
									.findViewWithTag(valueTags.get(i)))
									.getText().toString();
							productVO.set_value(value);
							productStr += "\"_value\":\"" + value + "\"},";
							productVOs.add(productVO);
						}
						mProducts.clear();
						if (productStr.length() > 1) {
							productStr = productStr.substring(0,
									productStr.length() - 1);
							productStr += "]";
							mProducts.addAll(productVOs);
						}
//						mConnHelper.addProduct(productStr, mUIHandler,
//								MsgTagVO.PUB_INFO,
//								CardAddUserProductActivity.this, true, null,
//								null);
					}
				});
		findViewById(R.id.buttonAdd).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addProduct(null);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.PUB_INFO: {
				if (JsonHandler.checkResult((String) msg.obj)) {
					Intent intent = new Intent();
					intent.putParcelableArrayListExtra(
							CardEditActivity.EDIT_PRODUCT_STR, mProducts);
					setResult(RESULT_OK, intent);
					CardAddUserProductActivity.this.finish();
				} else {
					pwh.showPopDlgOne(findViewById(R.id.rootLayout), null,
							R.string.error5);
				}
				break;
			}
			}
		}

	};

	private void addProduct(ProductVO productVO) {
		LayoutInflater inflater = LayoutInflater
				.from(CardAddUserProductActivity.this);
		LayoutParams lp = (LayoutParams) findViewById(R.id.linearLayoutProduct)
				.getLayoutParams();
		View view = inflater.inflate(R.layout.item_product_add, null);
		view.setLayoutParams(lp);
		EditText title = (EditText) view.findViewById(R.id.editTextTitle);
		EditText detail = (EditText) view.findViewById(R.id.editTextDetail);
		EditText value = (EditText) view.findViewById(R.id.editTextValue);
		if (null != productVO) {
			title.setText(productVO.getTitle());
			detail.setText(productVO.get_desc());
			value.setText(productVO.get_value());
		}
		title.setTag("title" + titleTags.size());
		titleTags.add("title" + titleTags.size());
		detail.setTag("detail" + detailTags.size());
		detailTags.add("detail" + detailTags.size());
		value.setTag("value" + valueTags.size());
		valueTags.add("value" + valueTags.size());
		ImageView iv = new ImageView(CardAddUserProductActivity.this);
		LayoutParams lllp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		lllp.topMargin = 20;
		lllp.bottomMargin = 10;
		iv.setLayoutParams(lllp);
		iv.setImageResource(R.drawable.bg_border3);
		linearLayout.addView(iv);
		linearLayout.addView(view);
	}
}
