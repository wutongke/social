package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.CommentAdapter;
import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.ScrollOverListView;
import com.cpstudui.zhuojiaren.lz.BeanBanner;
import com.cpstudui.zhuojiaren.lz.Bee_PageAdapter;
import com.cpstudui.zhuojiaren.lz.MainActivity;
import com.external.viewpagerindicator.PageIndicator;

public class GoodsDetailLActivity extends BaseActivity {
	@InjectView(R.id.agd_pulldown)
	PullDownView mPullDownView;
	@InjectView(R.id.agd_cart)
	TextView cart;
	@InjectView(R.id.agd_cart_count)
	TextView cartCount;
	@InjectView(R.id.agd_collection)
	TextView collection;
	//评论列表
	ListView mListView;
	CommentAdapter mAdapter;
	ArrayList<CommentVO> mDataList = new ArrayList<CommentVO>();
	//圆点
	private PageIndicator bannerIndicator;
	//头部
	private ViewPager bannerViewPager;
	Bee_PageAdapter bannerPageAdapter;
	private ArrayList<BeanBanner> bannerListData;
	private TextView productName;
	private TextView productDes;
	private TextView marketPrice;
	private TextView zhuoPrice;
	private TextView company;
	private TextView companyDes;
	private RelativeLayout productMoreInfo;
	private LinearLayout companyMoreInfo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail_l);
		ButterKnife.inject(this);
		initTitle();
		title.setText(R.string.label_goods_detail);
		initPullDown();
		initClick();
	}

	private void initPullDown() {
		// TODO Auto-generated method stub
		mPullDownView.initHeaderViewAndFooterViewAndListView(this, R.layout.head_goods_detail);
		
		
		mListView = mPullDownView.getListView();
		mAdapter = new CommentAdapter(this, mDataList, R.layout.item_comment);
		mListView.setAdapter(mAdapter);
		//设置不能下拉刷新
		((ScrollOverListView)mListView).showRefresh=false;
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		//图片展示
		bannerViewPager = (ViewPager) findViewById(R.id.hgd_viewpager);
		bannerIndicator = (PageIndicator) findViewById(R.id.hgd_indicator);
		bannerIndicator.setViewPager(bannerViewPager);
		bannerListData = new ArrayList<BeanBanner>();
		for (int i = 0; i < 5; i++) {

			BeanBanner item = new BeanBanner();
			item.setPicUrl("http://pic.nipic.com/2008-05-07/20085722191339_2.jpg");
			bannerListData.add(item);
		}
		bannerPageAdapter = new Bee_PageAdapter(this,
				bannerListData);
		bannerViewPager.setAdapter(bannerPageAdapter);
		//其他信息
		productName = (TextView)findViewById(R.id.hgd_product_name);
		productDes = (TextView)findViewById(R.id.hgd_product_des);
		marketPrice = (TextView)findViewById(R.id.hgd_market_price);
		zhuoPrice = (TextView)findViewById(R.id.hgd_zhuo_price);
		company = (TextView)findViewById(R.id.hgd_company_name);
		companyDes = (TextView)findViewById(R.id.hgd_company_des);
		productMoreInfo = (RelativeLayout)findViewById(R.id.product_layout);
		companyMoreInfo = (LinearLayout)findViewById(R.id.company_layout);
	}

	private void initClick() {
		// TODO Auto-generated method stub
		
	}
	Handler uiHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case 1:
				
			}
		};
	};
}
