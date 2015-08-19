package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.CommentAdapter;
import com.cpstudio.zhuojiaren.helper.AppClientLef;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.model.CrowdFundingVO;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.ScrollOverListView;
import com.cpstudui.zhuojiaren.lz.BeanBanner;
import com.cpstudui.zhuojiaren.lz.Bee_PageAdapter;
import com.external.viewpagerindicator.PageIndicator;
import com.google.gson.Gson;

public class GoodsDetailLActivity extends BaseActivity {
	@InjectView(R.id.agd_pulldown)
	PullDownView mPullDownView;
	@InjectView(R.id.agd_cart)
	TextView cart;
	@InjectView(R.id.agd_cart_count)
	TextView cartCount;
	@InjectView(R.id.agd_collection)
	TextView collection;
	// 评论列表
	ListView mListView;
	CommentAdapter mAdapter;
	// 产品
	GoodsVO goods;
	ArrayList<CommentVO> mDataList = new ArrayList<CommentVO>();
	// 圆点
	private PageIndicator bannerIndicator;
	// 头部
	private ViewPager bannerViewPager;
	Bee_PageAdapter bannerPageAdapter;
	private ArrayList<BeanBanner> bannerListData;
	private TextView productName;
	private TextView productDes;
	private TextView marketPrice;
	private TextView zhuoPrice;
	private TextView company;
	private TextView companyDes;
	private ImageView companyImage;
	private RelativeLayout productMoreInfo;
	private LinearLayout companyMoreInfo;
	private AppClientLef appClient;
	private String goodsId;
	LoadImage lImage = new LoadImage();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail_l);
		ButterKnife.inject(this);
		appClient = AppClientLef.getInstance(this.getApplicationContext());
		goodsId = getIntent().getStringExtra("goodsId");
		if(goodsId==null){
			CommonUtil.displayToast(this, R.string.data_error);
			return;
		}
		initTitle();
		title.setText(R.string.label_goods_detail);
		initPullDown();
		initClick();
		loadData();
	}

	private void initClick() {
		// TODO Auto-generated method stub
		collection.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (goods.getIsCollection()!=null&&goods.getIsCollection().equals(0)) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.zcollect2);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					collection.setCompoundDrawables(drawable, null, null, null);
				} else {
					Drawable drawable = getResources().getDrawable(
							R.drawable.zuncollect2);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					collection.setCompoundDrawables(drawable, null, null, null);
				}
			}
		});
		cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GoodsDetailLActivity.this,CartActivity.class);
				intent.putExtra("goods", goods);
				startActivity(intent);
			}
		});
	}

	private void loadData() {
		// TODO Auto-generated method stub
		appClient.getGoodsDetail(this, uiHandler, MsgTagVO.DATA_LOAD, goodsId);
	}

	private void initPullDown() {
		// TODO Auto-generated method stub
		mPullDownView.initHeaderViewAndFooterViewAndListView(this,
				R.layout.head_goods_detail);
		
		mListView = mPullDownView.getListView();
		mAdapter = new CommentAdapter(this, mDataList, R.layout.item_comment);
		mListView.setAdapter(mAdapter);
		// 设置不能下拉刷新
		((ScrollOverListView) mListView).showRefresh = false;
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});

		mPullDownView.setShowFooter(false);
		// 图片展示
		bannerViewPager = (ViewPager) findViewById(R.id.hgd_viewpager);
		bannerIndicator = (PageIndicator) findViewById(R.id.hgd_indicator);
		bannerListData = new ArrayList<BeanBanner>();
//
//		String[] urls = {
//				"http://img3.imgtn.bdimg.com/it/u=2628293733,2370129064&fm=21&gp=0.jpg",
//				"http://img2.imgtn.bdimg.com/it/u=2906966334,223089362&fm=21&gp=0.jpg",
//				"http://img4.imgtn.bdimg.com/it/u=1704061436,275613074&fm=21&gp=0.jpg",
//				"http://img3.imgtn.bdimg.com/it/u=1440545533,1670448902&fm=21&gp=0.jpg",
//				"http://img2.imgtn.bdimg.com/it/u=697459274,2261536128&fm=21&gp=0.jpg",
//				"http://img4.imgtn.bdimg.com/it/u=2079958976,1443524702&fm=21&gp=0.jpg",
//				"http://img3.imgtn.bdimg.com/it/u=1849853359,1757644016&fm=21&gp=0.jpg",
//				"http://img0.imgtn.bdimg.com/it/u=1703091849,1006427253&fm=21&gp=0.jpg",
//				"http://img1.imgtn.bdimg.com/it/u=3254378695,3573443632&fm=21&gp=0.jpg",
//				"http://img4.imgtn.bdimg.com/it/u=420516615,2115785755&fm=21&gp=0.jpg" };
//		for (int i = 0; i < 5; i++) {
//
//			BeanBanner item = new BeanBanner();
//			item.setPicUrl(urls[i]);
//			bannerListData.add(item);
//		}
//		
//		bannerPageAdapter = new Bee_PageAdapter(this, bannerListData);
//		bannerViewPager.setAdapter(bannerPageAdapter);
//		bannerViewPager.setCurrentItem(0);
//		bannerIndicator.setViewPager(bannerViewPager);
		// 其他信息
		productName = (TextView) findViewById(R.id.hgd_product_name);
		productDes = (TextView) findViewById(R.id.hgd_product_des);
		marketPrice = (TextView) findViewById(R.id.hgd_market_price);
		zhuoPrice = (TextView) findViewById(R.id.hgd_zhuo_price);
		company = (TextView) findViewById(R.id.hgd_company_name);
		companyDes = (TextView) findViewById(R.id.hgd_company_des);
		companyImage = (ImageView) findViewById(R.id.hgd_company_image);
		productMoreInfo = (RelativeLayout) findViewById(R.id.product_layout);
		companyMoreInfo = (LinearLayout) findViewById(R.id.company_layout);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResultVO res;
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				if (JsonHandler.checkResult((String) msg.obj,
						GoodsDetailLActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							R.string.data_error);
					return;
				}
				Gson gson = new Gson();
				try {
					goods = gson.fromJson(res.getData(), GoodsVO.class);
				} catch (Exception e) {
					// TODO: handle exception
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							R.string.data_error);
					Log.d("Debug", "json数据出错。。。。。。。。。。。。。。");
					return;
				}
				
				bannerListData.clear();
				if (goods.getPic()!=null) {
					for (int i = 0; i < goods.getPic().size(); i++) {
						BeanBanner item = new BeanBanner();
						item.setPicUrl(goods.getPic().get(i).getOrgurl());
						bannerListData.add(item);
					}
					bannerPageAdapter = new Bee_PageAdapter(
							GoodsDetailLActivity.this, bannerListData, lImage);
					bannerViewPager.setAdapter(bannerPageAdapter);
					bannerViewPager.setCurrentItem(0);
					bannerIndicator.setViewPager(bannerViewPager);
				}
				if(goods.getCompanyPic()!=null)
				lImage.beginLoad(goods.getCompanyPic().getUrl(), companyImage);
				
				productName.setText(goods.getGoodsName());
				productDes.setText(goods.getContent());
				marketPrice.setText(goods.getMarkeyPrice());
				company.setText(goods.getCompanyName());
				companyDes.setText(goods.getCompanyDes());
				zhuoPrice.setText(goods.getZhuoPrice());
				break;
			case MsgTagVO.MSG_CMT:
				if (JsonHandler.checkResult((String) msg.obj,
						GoodsDetailLActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							R.string.data_error);
					return;
				}
				ArrayList<CommentVO> temp = (ArrayList<CommentVO>) msg.obj;
				mDataList.addAll(temp);
				mAdapter.notifyDataSetChanged();
			}
		};
	};
}
