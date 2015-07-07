package com.cpstudio.zhuojiaren.ui;

import java.awt.List;
import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
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
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.CommentVO;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.PicVO;
import com.cpstudio.zhuojiaren.model.UserVO;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.ScrollOverListView;
import com.cpstudui.zhuojiaren.lz.BeanBanner;
import com.cpstudui.zhuojiaren.lz.Bee_PageAdapter;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail_l);
		ButterKnife.inject(this);
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
				if (goods.getIsCollection().equals(0)) {
					Drawable drawable = getResources().getDrawable(
							R.drawable.zcollect2);
					// / 这一步必须要做,否则不会显示.
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					collection.setCompoundDrawables(drawable, null, null, null);
				}else{
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
				
			}
		});
	}

	private void loadData() {
		// TODO Auto-generated method stub
		GoodsVO goods = new GoodsVO();
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
		Message msg1 = uiHandler.obtainMessage();
		msg1.what = 1;
		msg1.obj = goods;
		msg1.sendToTarget();
		
		
		CommentVO test = new CommentVO();
		test.setContent("写的不错，加油");
		test.setIsPraise("1");
		test.setTime("刚刚");
		UserVO user = new UserVO();
		user.setUsername("张来才");
		user.setPost("php董事");
		user.setCompany("php902大集团");
		test.setUser(user);
		test.setReplyUser(user);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mDataList.add(test);
		mAdapter.notifyDataSetChanged();
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

		mPullDownView.setShowHeader();
		mPullDownView.setShowFooter(false);
		// 图片展示
		bannerViewPager = (ViewPager) findViewById(R.id.hgd_viewpager);
		bannerIndicator = (PageIndicator) findViewById(R.id.hgd_indicator);
		bannerListData = new ArrayList<BeanBanner>();

		bannerPageAdapter = new Bee_PageAdapter(this, bannerListData);
		bannerViewPager.setAdapter(bannerPageAdapter);
		bannerIndicator.setViewPager(bannerViewPager);
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
			switch (msg.what) {
			case 1:
				GoodsVO goods = (GoodsVO) msg.obj;
				for (int i = 0; i < goods.getPic().size(); i++) {
					BeanBanner item = new BeanBanner();
					item.setPicUrl(goods.getPic().get(i).getOrgurl());
					bannerListData.add(item);
				}
				bannerPageAdapter.notifyDataSetChanged();
				
				
				productName.setText(goods.getName());
				productDes.setText(goods.getDetail());
				marketPrice.setText(goods.getPrice());
				company.setText(goods.getCompanyName());
				companyDes.setText(goods.getCompanyDes());
				zhuoPrice.setText(goods.getZhuobi());
				LoadImage lImage = new LoadImage();
				lImage.beginLoad(goods.getCompanyPic().getUrl(), companyImage);
				break;
			case 2:
				ArrayList<CommentVO> temp = (ArrayList<CommentVO>) msg.obj;
				mDataList.addAll(temp);
				mAdapter.notifyDataSetChanged();
			}
		};
	};
}
