package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.QuanVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.ViewHolder;

public class GoodsCollectionActivity extends BaseActivity {
	@InjectView(R.id.agc_categary)
	GridView gridView;
	@InjectView(R.id.agc_listview)
	ListView listView;
	@InjectView(R.id.agc_show_categary)
	ImageView showCategary;
	LoadImage loader = LoadImage.getInstance();
	private CommonAdapter<GoodsVO> mAdapter;
	private ArrayList<GoodsVO> mDataList = new ArrayList<GoodsVO>();
	private AppClient appClient;
	private ListViewFooter mListViewFooter = null;
	private int mPage = 1;
	private int type = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_collection);
		ButterKnife.inject(this);
		appClient = AppClient.getInstance(getApplicationContext());
		initTitle();
		title.setText(R.string.collection_goods);
		initView();
		showCategary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(gridView.getVisibility()==View.GONE)
					gridView.setVisibility(View.VISIBLE);
				else
					gridView.setVisibility(View.GONE);
			}
		});
		loadData(type);
	}

	private void initView() {
		// TODO Auto-generated method stub
		mAdapter = new CommonAdapter<GoodsVO>(this, mDataList,
				R.layout.item_goods_collection) {

			@Override
			public void convert(ViewHolder helper, final GoodsVO item) {
				// TODO Auto-generated method stub
				
				helper.setText(R.id.igc_name, item.getGoodsName());
				loader.beginLoad(item.getImg(),
						(ImageView) helper.getView(R.id.igc_goods_image));
				helper.setText(R.id.igc_price, item.getZhuoPrice());
				helper.getView(R.id.igc_collec_image).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								new AlertDialog.Builder(
										GoodsCollectionActivity.this,
										AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
										.setTitle(R.string.collection_goods)
										.setMessage(
												R.string.delete_collection_goods)
										.setNegativeButton(R.string.CANCEL,
												null)
										.setPositiveButton(
												R.string.OK,
												new AlertDialog.OnClickListener() {

													@Override
													public void onClick(
															DialogInterface dialog,
															int which) {
														// TODO Auto-generated
														// method stub
														if(mDataList.contains(item)){
															mDataList.remove(item);
															mAdapter.notifyDataSetChanged();
														}
													}
												}).create().show();
							}
						});
			}
		};
		
		RelativeLayout mFooterView = (RelativeLayout)getLayoutInflater().inflate(
				R.layout.listview_footer, null);
		listView.addFooterView(mFooterView);
		mListViewFooter = new ListViewFooter(mFooterView, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadMore(type);
			}
		});
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(GoodsCollectionActivity.this,
						GoodsDetailLActivity.class);
				i.putExtra("goodsId", ((GoodsVO)mAdapter.getItem(position)).getGoodsId());
				startActivity(i);
			}
		});
		int[] categary = { R.string.all_catgary, R.string.phone_fee,
				R.string.plane, R.string.movie, R.string.hotel, R.string.tea,
				R.string.intelligence, R.string.red_wine, R.string.tele_goods,
				R.string.cosmetic };
		ArrayList<String> strs = new ArrayList<String>();
		for (int i : categary) {
			strs.add(GoodsCollectionActivity.this.getResources().getString(i));
		}
		gridView.setAdapter(new CommonAdapter<String>(GoodsCollectionActivity.this, strs, R.layout.item_text) {

			@Override
			public void convert(ViewHolder helper, String item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.it_text, item);
			}
		});
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				type = position;
				loadData(position);
				gridView.setVisibility(View.GONE);
			}

		});
	}

	private void loadData(int type) {
		if (mListViewFooter.startLoading()) {
			mDataList.clear();
			mPage = 0;
			mAdapter.notifyDataSetChanged();
			appClient.getGoodsCollectionList(mPage,10,uiHandler,MsgTagVO.DATA_LOAD,this,null);
		}
	}

	private void loadMore(int type) {
		if (mListViewFooter.startLoading()) {
			appClient.getGoodsCollectionList(mPage,10,uiHandler,MsgTagVO.DATA_MORE,this,null);
		}
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			ResultVO res;
			if (JsonHandler.checkResult((String) msg.obj, GoodsCollectionActivity.this)) {
				res = JsonHandler.parseResult((String) msg.obj);
			} else {
				CommonUtil.displayToast(GoodsCollectionActivity.this, R.string.data_error);
				return;
			}
			String data = res.getData();
			if (msg.what==MsgTagVO.DATA_LOAD) {
				updateItemList(data, true, false);
			}else if(msg.what ==MsgTagVO.DATA_MORE){
				updateItemList(data, false, true);
			}
		};
	};
	private void updateItemList(String data, boolean refresh, boolean append) {
		try {
			mListViewFooter.finishLoading();
			if (data != null && !data.equals("")) {
				ArrayList<GoodsVO> list = JsonHandler_Lef.parseGoodsVOList(data);
				if (!list.isEmpty()) {
					mListViewFooter.hasData();
					if (!append) {
						mDataList.clear();
					}
					mDataList.addAll(list);
					mAdapter.notifyDataSetChanged();
					mPage++;
				} else {
					mListViewFooter.noData(!refresh);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
