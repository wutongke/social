package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.drawable;
import com.cpstudio.zhuojiaren.R.id;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.string;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ProductVO;
import com.cpstudio.zhuojiaren.widget.PopupWindows;

public class CardAddUserProductActivity extends Activity {
	private LinearLayout linearLayout;
	private ArrayList<String> titleTags = new ArrayList<String>();
	private ArrayList<String> detailTags = new ArrayList<String>();
	private ArrayList<String> valueTags = new ArrayList<String>();
	private ArrayList<ProductVO> mProducts = new ArrayList<ProductVO>();
	private PopupWindows pwh = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card_add_user_product);
		pwh = new PopupWindows(CardAddUserProductActivity.this);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.hideSoftInputFromWindow(CardAddUserProductActivity.this.getCurrentFocus().getWindowToken(), 0);
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
