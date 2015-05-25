package com.cpstudio.zhuojiaren;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.utils.CommunicationUtil;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Bitmap;

public class MyLocationActivity extends Activity {
	private BMapManager mBMapMan = null;
	private MapView mMapView = null;
	private LocationClient mLocationClient = null;
	private MKSearch mMKSearch = null;
	private Bitmap popbitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init("D43e1d8957287554e149360085840fbb", null);
		setContentView(R.layout.activity_my_location);
		mMapView = (MapView) findViewById(R.id.bmapsView);
		mMapView.setBuiltInZoomControls(true);
		MapController mMapController = mMapView.getController();
		GeoPoint point = new GeoPoint((int) (31.215 * 1E6),
				(int) (121.404 * 1E6));
		mMapController.setCenter(point);
		mMapController.setZoom(16);
		mMKSearch = new MKSearch();
		mMKSearch.init(mBMapMan, null);
		initLoc();
		initClick();
	}

	private void initClick() {

		findViewById(R.id.buttonBack).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MyLocationActivity.this.finish();
			}
		});
	}

	private void initLoc() {
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.setAK("D43e1d8957287554e149360085840fbb");
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
					return;
				if (location.getLocType() == BDLocation.TypeGpsLocation) {

				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

				}
				LocationData locData = new LocationData();
				locData.latitude = location.getLatitude();
				locData.longitude = location.getLongitude();
				initMyOverlay(locData);
				String locationName = location.getAddrStr();
				if (locationName == null) {
					double lat = location.getLatitude();
					double lit = location.getLongitude();
					int state = CommonUtil
							.getNetworkState(MyLocationActivity.this);
					if (state == 0) {
						getPositionFromNetwork(lat, lit);
					} else if (state == 1) {
						boolean wait = true;
						while (wait) {
							try {
								Thread.sleep(2000);
								state = CommonUtil
										.getNetworkState(MyLocationActivity.this);
								if (state == 0) {
									getPositionFromNetwork(lat, lit);
									wait = false;
								} else if (state == 2) {
									wait = false;
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					} else {

					}
				} else {
					final GeoPoint ptTAM = new GeoPoint(
							(int) (locData.latitude * 1e6),
							(int) (locData.longitude * 1e6));
					showPopOverlay(ptTAM, locationName);
				}
			}

			public void onReceivePoi(BDLocation poiLocation) {
			}
		});

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");
		option.setCoorType("bd09ll");
		option.setScanSpan(60000);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	// private void startLoc(){
	// mLocationClient.start();
	// if (mLocationClient != null && mLocationClient.isStarted()) {
	// mLocationClient.requestLocation();
	// } else {
	// Log.d("LocSDK3", "locClient is null or not started");
	// }
	// }

	private void getPositionFromNetwork(final double lat, final double lit) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					GeoPoint ptTAM = new GeoPoint((int) (lat * 1e6),
							(int) (lit * 1e6));
					String url = "http://api.map.baidu.com/geocoder?output=json&location="
							+ lat
							+ ","
							+ lit
							+ "&key=b3b3ab3588dd410aee70f4bcea9fe688";
					String jsonData = CommunicationUtil.executeStaticGet(url);
					if (null != jsonData) {
						String locationName = JsonHandler.parseGeo(jsonData)
								.getResult().getFormatted_address();

						showPopOverlay(ptTAM, locationName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void initMyOverlay(LocationData locData) {
		try {
			MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
					mMapView);
			myLocationOverlay.setData(locData);
			mMapView.getOverlays().add(myLocationOverlay);
			mMapView.refresh();
			GeoPoint ptTAM = new GeoPoint((int) (locData.latitude * 1e6),
					(int) (locData.longitude * 1e6));
			mMapView.getController().animateTo(ptTAM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showPopOverlay(GeoPoint ptTAM, String locationName) {
		View popview = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.pop_location, null);
		TextView TestText = (TextView) popview.findViewById(R.id.test_text);
		TestText.setText(locationName);
		if (null != popbitmap) {
			popbitmap.recycle();
		}
		popbitmap = convertViewToBitmap(popview);
		PopupOverlay pop = new PopupOverlay(mMapView, new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
			}
		});
		try {
			pop.showPopup(popbitmap, ptTAM, 32);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Bitmap convertViewToBitmap(View view) {
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		view.buildDrawingCache();
		Bitmap bitmap = view.getDrawingCache();

		return bitmap;
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		if (mBMapMan != null) {
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mLocationClient != null && !mLocationClient.isStarted()) {
			mLocationClient.start();
		}
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}
}
