package com.cpstudio.zhuojiaren.helper;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.utils.CommunicationUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class BaiduLocationHelper {
	private Context mContext;
	private LocationClient mLocationClient = null;
	private Handler mHandler;
	private int mTag;

	public BaiduLocationHelper(Context context, Handler handler, int tag) {
		this.mContext = context;
		this.mHandler = handler;
		this.mTag = tag;
		initLocation();
	}

	public void initLocation() {
		mLocationClient = new LocationClient(mContext);
		mLocationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				if (location == null)
					return;
				String city = location.getCity();
				if (city == null) {
					double lat = location.getLatitude();
					double lit = location.getLongitude();
					int state = CommonUtil.getNetworkState(mContext);
					if (state == 0) {
						getPositionFromNetwork(lat, lit);
					} else if (state == 1) {
						boolean wait = true;
						while (wait) {
							try {
								Thread.sleep(2000);
								state = CommonUtil.getNetworkState(mContext);
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
					}
				} else {
					sendMsg(city);
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
		// option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	private void getPositionFromNetwork(final double lat, final double lit) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String url = "http://api.map.baidu.com/geocoder?output=json&location="
							+ lat
							+ ","
							+ lit
							+ "&key=b3b3ab3588dd410aee70f4bcea9fe688";
					String jsonData = CommunicationUtil.executeStaticGet(url);
					if (null != jsonData) {
						String city = JsonHandler.parseGeo(jsonData)
								.getResult().getAddressComponent().getCity();
						sendMsg(city);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void stopLocation() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
		}
	}

	public void startLocation() {
		if (mLocationClient != null && !mLocationClient.isStarted()) {
			mLocationClient.start();
		}
	}

	private void sendMsg(String city) {
		Message msg = mHandler.obtainMessage(mTag);
		msg.obj = city;
		msg.sendToTarget();
	}
}
