package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.Bee_PageAdapter;
import com.cpstudio.zhuojiaren.helper.ConnHelper;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.UrlHelper;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BeanBanner;
import com.cpstudio.zhuojiaren.model.GoodsComment;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.cpstudio.zhuojiaren.widget.PullDownView;
import com.cpstudio.zhuojiaren.widget.ScrollOverListView;
import com.cpstudio.zhuojiaren.widget.ViewHolder;
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
	CommonAdapter<GoodsComment> mAdapter;
	// 产品
	GoodsVO goods;
	ArrayList<GoodsComment> mDataList = new ArrayList<GoodsComment>();
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
	private ConnHelper appClient;
	private String goodsId;
	private static final int Collection = 333;
	LoadImage lImage = LoadImage.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_detail_l);
		ButterKnife.inject(this);
		appClient = ConnHelper.getInstance(this.getApplicationContext());
		goodsId = getIntent().getStringExtra("goodsId");
		if (goodsId == null) {
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
				String type = "1";
				appClient.collection(GoodsDetailLActivity.this,uiHandler,Collection,
						UrlHelper.getGoodsCollection(), "goodsid",
						goodsId, "type", type);
			}
		});
		cart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				appClient.GoodsAddToCart(GoodsDetailLActivity.this,uiHandler,MsgTagVO.PUB_INFO,goodsId);
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
		mAdapter = new CommonAdapter<GoodsComment>(this, mDataList,
				R.layout.item_comment) {

			@Override
			public void convert(ViewHolder helper, GoodsComment item) {
				// TODO Auto-generated method stub
				helper.getView(R.id.ic_reply_layout).setVisibility(View.GONE);
				helper.getView(R.id.ic_function).setVisibility(View.GONE);
				helper.setText(R.id.ic_people_name, item.getUserId());
				helper.setText(R.id.ic_content, item.getContent());
				if(item.getImg()!=null)
				lImage.beginLoad(item.getContent(), (ImageView)helper.getView(R.id.ic_people_image));
			}
		};
		mListView.setAdapter(mAdapter);
		((ScrollOverListView) mListView).showRefresh = false;
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});

		mPullDownView.setShowFooter(false);
		mPullDownView.noFoot();
		// 图片展示
		bannerViewPager = (ViewPager) findViewById(R.id.hgd_viewpager);
		bannerIndicator = (PageIndicator) findViewById(R.id.hgd_indicator);
		bannerListData = new ArrayList<BeanBanner>();
		productName = (TextView) findViewById(R.id.hgd_product_name);
		productDes = (TextView) findViewById(R.id.hgd_product_des);
		marketPrice = (TextView) findViewById(R.id.hgd_market_price);
		zhuoPrice = (TextView) findViewById(R.id.hgd_zhuo_price);
		company = (TextView) findViewById(R.id.hgd_company_name);
		companyDes = (TextView) findViewById(R.id.hgd_company_des);
		companyImage = (ImageView) findViewById(R.id.hgd_company_image);
	}

	Handler uiHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ResultVO res = null;
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD:
				mPullDownView.finishLoadData(true);
				if (JsonHandler.checkResult((String) msg.obj,
						GoodsDetailLActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
				} else {
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							"商品不存在");
					GoodsDetailLActivity.this.finish();
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
				if (goods.getImgList() != null) {
					for (int i = 0; i < goods.getImgList().size(); i++) {
						BeanBanner item = new BeanBanner();
						item.setPicUrl(goods.getImgList().get(i));
						bannerListData.add(item);
					}
					bannerPageAdapter = new Bee_PageAdapter(
							GoodsDetailLActivity.this, bannerListData, lImage,ScaleType.MATRIX);
					bannerViewPager.setAdapter(bannerPageAdapter);
					bannerViewPager.setCurrentItem(0);
					bannerIndicator.setViewPager(bannerViewPager);
				}
				if (goods.getProvider()!=null&&goods.getProvider().getPic() != null)
					lImage.beginLoad(goods.getProvider().getPic(), companyImage);
				productName.setText(goods.getGoodsName());
				productDes.setText(goods.getContent());
				marketPrice.setText(goods.getMarkeyPrice());
				if(goods.getProvider()!=null){
					lImage.beginLoad(goods.getProvider().getPic(), companyImage);
					company.setText(goods.getProvider().getProviderName());
					companyDes.setText(goods.getProvider().getContent());
					
				}
				zhuoPrice.setText(goods.getZhuoPrice());
				// goodsComment
				mDataList.clear();
				//delete comment
//				if(goods.getComments()!=null)
//				mDataList.addAll(goods.getComments());
//				mAdapter.notifyDataSetChanged();
				break;
			case MsgTagVO.PUB_INFO:
				if (JsonHandler.checkResult((String) msg.obj,
						GoodsDetailLActivity.this)) {
					res = JsonHandler.parseResult((String) msg.obj);
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							"添加到购物车成功");
				} else {
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							res.getData());
					return;
				}
//				Intent i = new Intent(GoodsDetailLActivity.this,CartActivity.class);
//				startActivity(i);
				break;
			case Collection:
				res = JsonHandler.parseResult((String) msg.obj);
				if (JsonHandler.checkResult((String) msg.obj,
						GoodsDetailLActivity.this)) {
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							"成功收藏商品");
				} else {
					CommonUtil.displayToast(GoodsDetailLActivity.this,
							res.getData());
					return;
				}
				break;
			default:break;
			}
		};
	};
}
