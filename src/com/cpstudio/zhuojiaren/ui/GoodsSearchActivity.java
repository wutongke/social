package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.R.layout;
import com.cpstudio.zhuojiaren.R.menu;
import com.cpstudio.zhuojiaren.adapter.StoreGoodsListAdapter;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.ViewHolder;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudui.zhuojiaren.lz.StoreMainActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class GoodsSearchActivity extends Activity {
	@InjectView(R.id.search_input)
	EditText searchEditText;
	@InjectView(R.id.goods_list)
	ListView goodsListView;
	String mSearchKey = "";

	private AppClientLef mConnHelper = null;
	private int mPage = 1;
	private StoreGoodsListAdapter mAdapter = null;
	private ArrayList<GoodsVO> mList = new ArrayList<GoodsVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_search);
		ButterKnife.inject(this);
		searchEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					mSearchKey = v.getText().toString();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					loadData();

				}
				return false;
			}
		});
		mConnHelper = AppClientLef.getInstance(getApplicationContext());
		mAdapter = new StoreGoodsListAdapter(this, mList);
		goodsListView.setAdapter(mAdapter);
		goodsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position >= 0) {
					Intent i = new Intent(GoodsSearchActivity.this,
							GoodsDetailLActivity.class);
					i.putExtra("goodsId", mList.get(position).getGoodsId());
					startActivity(i);
				}
			}
		});
	}

	private void loadData() {
		mPage = 0;
		mConnHelper.getGoodsList(mPage, 5, mUIHandler, MsgTagVO.DATA_LOAD,
				this, null, null, null);
	}

	private void updateItemList(String data, boolean refresh, boolean append) {
		if (data != null && !data.equals("")) {
			JsonHandler nljh = new JsonHandler(data, getApplicationContext());
			ArrayList<GoodsVO> list = nljh.parseGoodsList();
			if (!list.isEmpty()) {
				if (!append) {
					mList.clear();
				}
				mPage++;
				mList.addAll(list);
				mAdapter.notifyDataSetChanged();
			}
		}
	}

	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				updateItemList((String) msg.obj, true, false);
				break;
			}
			}
		};
	};
}
