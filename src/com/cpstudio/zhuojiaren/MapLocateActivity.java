package com.cpstudio.zhuojiaren;

import android.app.Activity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.cpstudio.zhuojiaren.util.CommonUtil;

public class MapLocateActivity extends Activity {
	// 地图相关
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	// 经纬度
	String longitude;
	String latitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_locate);
		longitude = getIntent().getStringExtra("LONGITUDE");
		latitude = getIntent().getStringExtra("LATITUDE");
		// 初始化地图
		mMapView = (MapView) findViewById(R.id.l_bmapView);
		mBaiduMap = mMapView.getMap();
		if(longitude!=null&&latitude!=null){
			try {
				double a = Double.parseDouble(longitude);
				double b = Double.parseDouble(latitude);
				mBaiduMap.addOverlay(new MarkerOptions().position(new LatLng(Double.parseDouble(longitude), Double.parseDouble(latitude)))
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.icon_marka)));
			} catch (Exception e) {
				// TODO: handle exception
				CommonUtil.displayToast(MapLocateActivity.this, "位置信息不能显示");
			}
		}
		
	}
}
