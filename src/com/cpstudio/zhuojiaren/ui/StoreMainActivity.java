package com.cpstudio.zhuojiaren.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.adapter.Bee_PageAdapter;
import com.cpstudio.zhuojiaren.adapter.StoreGoodsListAdapter;
import com.cpstudio.zhuojiaren.helper.AppClient;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.helper.JsonHandler_Lef;
import com.cpstudio.zhuojiaren.imageloader.LoadImage;
import com.cpstudio.zhuojiaren.model.BeanBanner;
import com.cpstudio.zhuojiaren.model.GoodsVO;
import com.cpstudio.zhuojiaren.model.MsgTagVO;
import com.cpstudio.zhuojiaren.model.ResultVO;
import com.cpstudio.zhuojiaren.util.CommonAdapter;
import com.cpstudio.zhuojiaren.widget.ListViewFooter;
import com.cpstudio.zhuojiaren.widget.ViewHolder;
import com.external.viewpagerindicator.PageIndicator;
/**
 * 倬脉商城，lef2改
 * @author lz
 *
 */
public class StoreMainActivity extends BaseActivity implements
		OnItemClickListener {
	@InjectView(R.id.gv_cats)
	GridView gvCats;
	@InjectView(R.id.gv_goods)
	GridView gvGoods;
	@InjectView(R.id.ads_viewpager)
	ViewPager adsViewPager;

	volatile boolean isContinue = true;
	PageIndicator adsIndicator;
	private ArrayList<BeanBanner> adsListData;
	private Bee_PageAdapter adsPageAdapter;
	private static final int GoodsCategory = 10;
	// 需要改
	Class[] classArrays = { GoodsTypedListActivity.class };
	int iconIds[] = { R.drawable.jiarendongtai, R.drawable.chengzhangzaixian,
			R.drawable.zhuoyuanyuyin, R.drawable.wodemingpian, R.drawable.zhuo,
			R.drawable.resource, R.drawable.shop, R.drawable.project,
			R.drawable.travel, R.drawable.city };
	String[] tags;

	private AppClient mConnHelper = null;
	private int mPage = 1;
	private ListViewFooter mListViewFooter = null;
	private StoreGoodsListAdapter mAdapter = null;
	private ArrayList<GoodsVO> mList = new ArrayList<GoodsVO>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_main);
		initTitle();
		ButterKnife.inject(this);
		function.setText(R.string.tab_item5);
		function.setVisibility(View.VISIBLE);
		title.setText(R.string.title_activity_store_main);
		initView();
		mConnHelper = AppClient.getInstance(getApplicationContext());
		mAdapter = new StoreGoodsListAdapter(this, mList);
		gvGoods.setAdapter(mAdapter);
		gvGoods.setOnItemClickListener(this);
		RelativeLayout mFooterView = (RelativeLayout) findViewById(R.id.layoutFooter);
		mListViewFooter = new ListViewFooter(mFooterView, onMoreClick);

		loadData();
		loadGoodsCatgory();
	}

	private void initView() {
		// TODO Auto-generated method stub

		function.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(StoreMainActivity.this,
						StoreMyHomeActivity.class));
			}
		});

		final EditText searchView = (EditText) findViewById(R.id.search_input);

		searchView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(StoreMainActivity.this,
						StoreSearchMainActivity.class);
				startActivity(i);
			}
		});
		findViewById(R.id.search).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(StoreMainActivity.this,
						StoreSearchMainActivity.class);
				// i.putExtra("filePath", filePath);
				startActivity(i);
			}
		});

		adsListData = new ArrayList<BeanBanner>();
		String[] urls = {
				"http://img3.imgtn.bdimg.com/it/u=2628293733,2370129064&fm=21&gp=0.jpg",
				"http://img2.imgtn.bdimg.com/it/u=2906966334,223089362&fm=21&gp=0.jpg",
				"http://img4.imgtn.bdimg.com/it/u=1704061436,275613074&fm=21&gp=0.jpg",
				"http://img3.imgtn.bdimg.com/it/u=1440545533,1670448902&fm=21&gp=0.jpg",
				"http://img2.imgtn.bdimg.com/it/u=697459274,2261536128&fm=21&gp=0.jpg",
				"http://img4.imgtn.bdimg.com/it/u=2079958976,1443524702&fm=21&gp=0.jpg",
				"http://img3.imgtn.bdimg.com/it/u=1849853359,1757644016&fm=21&gp=0.jpg",
				"http://img0.imgtn.bdimg.com/it/u=1703091849,1006427253&fm=21&gp=0.jpg",
				"http://img1.imgtn.bdimg.com/it/u=3254378695,3573443632&fm=21&gp=0.jpg",
				"http://img4.imgtn.bdimg.com/it/u=420516615,2115785755&fm=21&gp=0.jpg" };
		for (int i = 0; i < 5; i++) {

			BeanBanner item = new BeanBanner();
			item.setPicUrl(urls[i]);
			adsListData.add(item);
		}

		adsPageAdapter = new Bee_PageAdapter(StoreMainActivity.this,
				adsListData);
		adsViewPager.setAdapter(adsPageAdapter);
		adsViewPager.setCurrentItem(0);
		adsIndicator = (PageIndicator) findViewById(R.id.ads_indicator);
		adsIndicator.setViewPager(adsViewPager);

		tags = getResources().getStringArray(R.array.store_types);

		ArrayList<HashMap<String, Object>> imagelist = new ArrayList<HashMap<String, Object>>();

		// 使用HashMap将图片添加到一个数组中，注意一定要是HashMap<String,Object>类型的，因为装到map中的图片要是资源ID，而不是图片本身
		// 如果是用findViewById(R.drawable.image)这样把真正的图片取出来了，放到map中是无法正常显示的
		int n = iconIds.length < tags.length ? iconIds.length : tags.length;
		for (int i = 0; i < n; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ivIcon", iconIds[i]);
			map.put("tvText", tags[i]);
			imagelist.add(map);
		}

		SimpleAdapter simpleAdapter = new SimpleAdapter(StoreMainActivity.this,
				imagelist, R.layout.gridview_item_type, new String[] {
						"ivIcon", "tvText" }, new int[] { R.id.ivIcon,
						R.id.tvText });
		// 设置GridView的适配器为新建的simpleAdapter

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		isContinue = false;
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		isContinue = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isContinue) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mUIHandler.sendEmptyMessage(MsgTagVO.FLIP);
				}
			}
		}).start();
		super.onResume();
	}

	@SuppressLint("HandlerLeak")
	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MsgTagVO.DATA_LOAD: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, true, false);
				break;
			}
			case MsgTagVO.DATA_MORE: {
				mListViewFooter.finishLoading();
				updateItemList((String) msg.obj, false, true);
				break;
			}
			case MsgTagVO.FLIP:
				if(adsViewPager.getCurrentItem()==adsPageAdapter.getCount()){
					adsViewPager.setCurrentItem((adsViewPager.getCurrentItem() + 1)
							% adsPageAdapter.getCount(),false);
				}else
				adsViewPager.setCurrentItem((adsViewPager.getCurrentItem() + 1)
						% adsPageAdapter.getCount());

				break;
			case GoodsCategory:
				try {
					ResultVO res;
					if (JsonHandler.checkResult((String) msg.obj,
							StoreMainActivity.this)) {
						res = JsonHandler.parseResult((String) msg.obj);
					} else {
						return;
					}
					String data = res.getData();
					if (data != null && !data.equals("")) {
						final ArrayList<com.cpstudio.zhuojiaren.model.GoodsCategory> list = JsonHandler_Lef
								.parseGoodsCategory(data);
						gvCats.setAdapter(new CommonAdapter<com.cpstudio.zhuojiaren.model.GoodsCategory>(
								StoreMainActivity.this, list,
								R.layout.gridview_item_type) {

							@Override
							public void convert(
									ViewHolder helper,
									com.cpstudio.zhuojiaren.model.GoodsCategory item) {
								// TODO Auto-generated method stub
								helper.setText(R.id.tvText, item.getCategoryName());
								ImageView imageView = (ImageView)helper.getView(R.id.ivIcon);
								LoadImage.getInstance().beginLoad(item.getCategoryPic(), imageView);
							}
						});

						gvCats.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub

								Intent i = new Intent(StoreMainActivity.this,
										classArrays[0]);
								i.putExtra("type", arg2);
								i.putExtra("typeName", list.get(arg2).getCategoryName());
								startActivity(i);
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	};

	private void updateItemList(String data, boolean refresh, boolean append) {
		if (data != null && !data.equals("")) {
			JsonHandler nljh = new JsonHandler(data, getApplicationContext());
			ArrayList<GoodsVO> list = nljh.parseGoodsList();
			if (!list.isEmpty()) {
				mListViewFooter.hasData();
				if (!append) {
					mList.clear();
				}
				mPage++;
				mList.addAll(list);
				mAdapter.notifyDataSetChanged();
			} else {
				mListViewFooter.noData(!refresh);
			}
		}

	}

	private void loadGoodsCatgory() {
		AppClient.getInstance(this).getGoodsCategory(mUIHandler,
				GoodsCategory, this, false, null, null);
	}

	private void loadData() {
		mList.clear();
		mPage = 0;
		mAdapter.notifyDataSetChanged();
		mConnHelper.getGoodsList(mPage, 5, mUIHandler, MsgTagVO.DATA_LOAD,
				this, null, null, null);

	}

	private void loadMore() {
		mConnHelper.getGoodsList(mPage, 5, mUIHandler, MsgTagVO.DATA_MORE,
				this, null, null, null);
	}

	private OnClickListener onMoreClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			loadMore();
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg3 != -1) {
			Intent i = new Intent(StoreMainActivity.this,
					GoodsDetailLActivity.class);
			i.putExtra("goodsId", mList.get(arg2).getGoodsId());
			startActivity(i);
		}
	}
}
