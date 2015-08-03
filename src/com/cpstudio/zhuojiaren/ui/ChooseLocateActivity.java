package com.cpstudio.zhuojiaren.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLongClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.cpstudio.zhuojiaren.BaseActivity;
import com.cpstudio.zhuojiaren.R;

public class ChooseLocateActivity extends BaseActivity implements
OnMapLongClickListener, OnMapClickListener, OnGetGeoCoderResultListener{


	private SearchView serchEdit;
	private EditText locateEdit;
	private Button okBtn;
	// ��ͼ���
	private MapView mMapView;
	private BaiduMap mBaiduMap;
	private Button searchBtn;
	String longitude;
	String latitude;
	GeoCoder mSearch = null; // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_locate);
		initTitle();
		title.setText("��ϸ��ַ");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		serchEdit = (SearchView) findViewById(R.id.edit1);
		locateEdit = (EditText) findViewById(R.id.edit2);
		okBtn = (Button) findViewById(R.id.ok);
		searchBtn = (Button) findViewById(R.id.search);
		// ��ʼ����ͼ
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// ��ʼ������ģ�飬ע���¼�����
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		mBaiduMap.setOnMapClickListener(this);
		mBaiduMap.setOnMapLongClickListener(this);
		serchEdit.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				// Geo����
				String[] strs = query.split(",");
				if(strs.length<2){
					strs = query.split("��");
					if (strs.length<2) {
						Toast.makeText(ChooseLocateActivity.this,
								"������ʽ���������֣���ϸ��ַ", Toast.LENGTH_SHORT).show();
						return false;
					}
				}
					
				mSearch.geocode(new GeoCodeOption().city(strs[0]).address(strs[1]));
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				if(!newText.isEmpty()){
					searchBtn.setVisibility(View.VISIBLE);
				}else{
					searchBtn.setVisibility(View.GONE);
				}
				return true;
			}
		});
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("locate", locateEdit.getText().toString());
				intent.putExtra("longitude", longitude);
				intent.putExtra("latitude", latitude);
				setResult(RESULT_OK, intent);
				ChooseLocateActivity.this.finish();
			}
		});
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMapLongClick(LatLng arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(ChooseLocateActivity.this, "����λ��",
				Toast.LENGTH_SHORT).show();
		mSearch.reverseGeoCode(new ReverseGeoCodeOption()
		.location(arg0));
	}

	// ʹ��λ�������Ľ��
	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		// TODO Auto-generated method stub
		locateEdit.setText(result.getAddress());
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(ChooseLocateActivity.this, "��Ǹ��δ���ҵ����",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		longitude = String.valueOf(result.getLocation().longitude);
		latitude = String.valueOf(result.getLocation().latitude);
		String strInfo = String.format("γ�ȣ�%f ���ȣ�%f",
				result.getLocation().latitude, result.getLocation().longitude);
		Toast.makeText(ChooseLocateActivity.this, strInfo, Toast.LENGTH_LONG)
				.show();
	}

	// ʹ�þ�γ�������Ľ��
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		// TODO Auto-generated method stub
		locateEdit.setText(result.getAddress());
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(ChooseLocateActivity.this, "��Ǹ��δ���ҵ����",
					Toast.LENGTH_LONG).show();
			return;
		}
		mBaiduMap.clear();
		mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.icon_marka)));
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
				.getLocation()));
		longitude = String.valueOf(result.getLocation().longitude);
		latitude = String.valueOf(result.getLocation().latitude);
		Toast.makeText(ChooseLocateActivity.this, result.getAddress(),
				Toast.LENGTH_LONG).show();
	}


}
