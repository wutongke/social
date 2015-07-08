package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.util.ViewHolder;

public class GoodsCollectionActivity extends BaseActivity {
	@InjectView(R.id.agc_categary)
	GridView gridView;
	@InjectView(R.id.agc_listview)
	ListView listView;
	@InjectView(R.id.agc_show_categary)
	TextView showCategary;
	LoadImage loader = new LoadImage();
	private CommonAdapter<GoodsVO> mAdapter;
	private ArrayList<GoodsVO> mDataList = new ArrayList<GoodsVO>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_collection);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.collection_goods);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mAdapter = new CommonAdapter<GoodsVO>(this, mDataList,
				R.layout.item_goods_collection) {

			@Override
			public void convert(ViewHolder helper, GoodsVO item) {
				// TODO Auto-generated method stub
				helper.setText(R.id.igc_name, item.getName());
				loader.beginLoad(item.getFirstPic().getOrgurl(),
						(ImageView) helper.getView(R.id.igc_goods_image));
				helper.setText(R.id.igc_price, item.getPrice());
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

													}
												}).create().show();
							}
						});
			}
		};
		listView.setAdapter(mAdapter);
		int[]categary = {R.string.all_catgary,R.string.phone_fee,R.string.plane,R.string.movie,R.string.hotel,R.string.tea,R.string.intelligence,R.string.red_wine,R.string.tele_goods,R.string.cosmetic};
		ArrayList<String> strs = new ArrayList<String>();
		for(int i :categary){
			strs.add(GoodsCollectionActivity.this.getResources().getString(i));
		}
		gridView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strs));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				loadData(position);
				CommonUtil.displayToast(GoodsCollectionActivity.this, position+"");
			}

			
		});
	}
	private void loadData(int position) {
		// TODO Auto-generated method stub
		GoodsVO goods = new GoodsVO();
		goods.setGid("1");
		goods.setName("吉大小天鹅");
		goods.setDetail("千百万美图阿登省就疯狂拉升的房间");
		goods.setCompanyName("北京");
		goods.setCompanyDes("为恶劣的高科技阿里；拉时间断开连接");
		goods.setIsCollection("0");
		PicVO pic = new PicVO();
		pic.setOrgurl("http://img13.360buyimg.com/vclist/jfs/t931/269/1375027638/11748/d0421ed8/559a312fN059bda44.jpg");
		goods.setCompanyPic(pic);
		ArrayList<PicVO> list = new ArrayList<PicVO>();
		list.add(pic);
		goods.setPic(list);
		goods.setMoney("152");
		goods.setPrice("152");
		goods.setZhuobi("120");
		ArrayList<GoodsVO> result = new ArrayList<GoodsVO>();
		result.add(goods);
		result.add(goods);
		result.add(goods);
		result.add(goods);
		result.add(goods);
		Message msg = uiHandler.obtainMessage();
		msg.what=result.size();
		msg.obj = result;
		msg.sendToTarget();
	}
	Handler uiHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what>=1){
				mDataList.clear();
				mDataList.addAll((ArrayList<GoodsVO>)msg.obj);
				mAdapter.notifyDataSetChanged();
			}
		};
	};
}
