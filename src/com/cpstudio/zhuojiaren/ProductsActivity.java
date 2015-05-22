package com.cpstudio.zhuojiaren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpstudio.zhuojiaren.model.ProductVO;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ProductsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_products);
		Intent intent = getIntent();
		ArrayList<ProductVO> mProducts = intent
				.getParcelableArrayListExtra(CardEditActivity.EDIT_PRODUCT_STR);
		List<Map<String, String>> contents = new ArrayList<Map<String, String>>();
		for (int i = 0; i < mProducts.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("title", mProducts.get(i).getTitle());
			map.put("desc", mProducts.get(i).get_desc());
			map.put("value", mProducts.get(i).get_value());
			contents.add(map);
		}
		ListView mListView = (ListView) findViewById(R.id.listView);
		SimpleAdapter mAdapter = new SimpleAdapter(this, contents,
				R.layout.item_product_list, new String[] { "title", "desc",
						"value" }, new int[] { R.id.textViewTitle,
						R.id.textViewDesc, R.id.textViewValue });
		mListView.setAdapter(mAdapter);
		initClick();
	}

	private void initClick() {
		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ProductsActivity.this.finish();
			}
		});

	}
	
}
