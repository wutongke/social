package com.cpstudio.zhuojiaren.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.cpstudio.zhuojiaren.helper.JsonHandler;
import com.cpstudio.zhuojiaren.model.GeoVO;
import com.utils.CommunicationUtil;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;

public class DeviceInfoUtil {
	private static String networkOperatorName;
	private static String deviceId;
	private static String androidId;
	private static String mUserAgent;
	private static String packageName;
	private static int versionCode;
	private static String versionName;
	private static String labelRes;
	private static String mOrientation;
	private static float mRsd;
	private static float mCsd;
	private static int mCsw;
	private static int mCsh;
	private static String mWidth = null;

	private static void gentInfo(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo;
			if ((packageManager != null)
					&& ((packageInfo = packageManager.getPackageInfo(
							context.getPackageName(), 0)) != null)) {
				packageName = packageInfo.packageName;
				versionCode = packageInfo.versionCode;
				versionName = packageInfo.versionName;
			}
			ApplicationInfo applicationInfo = packageManager
					.getApplicationInfo(context.getPackageName(), 128);

			if (applicationInfo != null) {
				int i1 = applicationInfo.labelRes;
				if (i1 != 0)
					labelRes = context.getResources().getString(
							applicationInfo.labelRes);
				else
					labelRes = applicationInfo.nonLocalizedLabel == null ? null
							: applicationInfo.nonLocalizedLabel.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getImagePrefix() {
		return mWidth;
	}

	public static void setWidth(int width) {
		String minWidth = "720";
		if (width >= 1080) {
			minWidth = "1080";
		} else if (width >= 720) {
			minWidth = 1080 - width < width - 720 ? "1080" : "720";
		} else if (width >= 480) {
			minWidth = 720 - width < width - 480 ? "720" : "480";
		} else if (width >= 320) {
			minWidth = 480 - width < width - 320 ? "480" : "320";
		} else if (width >= 240) {
			minWidth = 320 - width < width - 240 ? "320" : "240";
		} else {
			minWidth = "240";
		}
		DeviceInfoUtil.mWidth = minWidth;
	}

	public static String getNetworkType(Context context) {
		if (context
				.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") == -1) {
			return "unknown";
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService("connectivity");

		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			int i1 = networkInfo.getType();
			if (i1 == 0) {
				String str = networkInfo.getSubtypeName();
				if (str != null) {
					return str;
				}
				return "gprs";
			}
			if (i1 == 1) {
				return "wifi";
			}
		}
		return "unknown";
	}

	public static Cursor s(Context context) {
		try {
			String str1 = getNetworkType(context);
			if ((str1 != null) && (str1.equals("wifi"))) {
				return null;
			}

			String str2 = "content://telephony/carriers/preferapn";
			Uri uri = Uri.parse(str2);
			return context.getContentResolver().query(uri, null, null, null,
					null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} catch (Error e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getNetworkOperatorName(Context context) {
		try {
			if (networkOperatorName == null) {
				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService("phone");

				networkOperatorName = telephonyManager.getNetworkOperatorName();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return networkOperatorName;
	}

	public static String getDeviceId(Context context) {
		try {
			if (deviceId == null) {
				TelephonyManager telephonyManager = (TelephonyManager) context
						.getSystemService("phone");

				deviceId = telephonyManager.getDeviceId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return deviceId;
	}

	public static String getDeviceIdNew(Context context) {
		try {
			if (androidId == null)
				androidId = Settings.Secure.getString(
						context.getContentResolver(), "android_id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return androidId;
	}

	public static boolean checkSDKVersion(int sdk, boolean equal) {
		if (equal) {
			if (Build.VERSION.SDK_INT >= sdk) {
				return true;
			}
			return false;
		}
		if (Build.VERSION.SDK_INT > sdk) {
			return true;
		}
		return false;
	}

	public static String getUa(Context context) {
		if (mUserAgent == null) {
			mUserAgent = new WebView(context).getSettings()
					.getUserAgentString();
		}
		return mUserAgent;
	}

	public static String getDeviceLanguage() {
		return Locale.getDefault().getLanguage();
	}

	public static int getDeviceLocacc() {
		return LocationHelper.getLocacc(LocationHelper.getInstance());
	}

	public static int getDeviceLocstatus() {
		return LocationHelper.getLocstatus(LocationHelper.getInstance());
	}

	public static long getDeviceLoctime() {
		return LocationHelper.getLoctime(LocationHelper.getInstance());
	}

	public static String getDeviceVerison(Context context) {
		if (packageName == null) {
			gentInfo(context);
		}
		return versionName;
	}

	public static int getDeviceVerisonCode(Context context) {
		if (packageName == null) {
			gentInfo(context);
		}
		return versionCode;
	}

	public static String getDeviceAppName(Context context) {
		if (packageName == null) {
			gentInfo(context);
		}
		return labelRes;
	}

	public static String getDevicePKG(Context context) {
		if (packageName == null) {
			gentInfo(context);
		}
		return packageName;
	}

	public static boolean getDeviceInstallInfo(Context context, String str) {
		if ((null != str) && (!str.equals(""))) {
			try {
				PackageInfo packageInfo = context.getPackageManager()
						.getPackageInfo(str, 1);
				if (null != packageInfo) {
					return true;
				}
			} catch (PackageManager.NameNotFoundException e) {
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static String getDeviceOrientation(Context context) {
		mOrientation = "v";
		Display display = ((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay();
		if ((display.getOrientation() == 1) || (display.getOrientation() == 3)) {
			mOrientation = "h";
		}
		return mOrientation;
	}

	public static float getDeviceRsd(Context context) {
		try {
			if (mRsd == 0.0F) {
				Display display = ((WindowManager) context
						.getSystemService("window")).getDefaultDisplay();
				DisplayMetrics displayMetrics = new DisplayMetrics();
				display.getMetrics(displayMetrics);
				mRsd = displayMetrics.density;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRsd;
	}

	public static float getDeviceCsd(Context context) {
		try {
			if (mCsd == 0.0F) {
				DisplayMetrics displayMetrics = context.getResources()
						.getDisplayMetrics();
				mCsd = displayMetrics.density;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCsd;
	}

	@SuppressWarnings("deprecation")
	public static int getDeviceCsw(Context context) {
		Display display = ((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay();
		if (display != null) {
			mCsw = display.getWidth();
		}
		return mCsw;
	}

	@SuppressWarnings("deprecation")
	public static int getDeviceCsh(Context context) {
		Display display = ((WindowManager) context.getSystemService("window"))
				.getDefaultDisplay();
		if (display != null) {
			mCsh = display.getHeight();
		}
		return mCsh;
	}

	public static String getDeviceLocinfo(Context context) {
		LocationHelper helper = LocationHelper.getInstance();
		Location location = LocationHelper.getLocation(helper, context);
		if (location != null) {
			return LocationHelper.getLocationInfo(helper, location);
		}
		return null;
	}

	public static String getDeviceLocationPlace(Context context) {
		LocationHelper helper = LocationHelper.getInstance();
		Location location = LocationHelper.getLocation(helper, context);
		if (location != null) {
			return LocationHelper.getLocationPlace(helper, location);
		}
		return null;
	}

	public static String getDeviceLocationCity(Context context) {
		LocationHelper helper = LocationHelper.getInstance();
		Location location = LocationHelper.getLocation(helper, context);
		if (location != null) {
			return LocationHelper.getLocationCity(helper, location);
		}
		return null;
	}

	public static String getDeviceMa(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService("wifi");
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}

	public static String getDeviceAma(Context context) {
		if (CommonUtil.checkWifiState(context)) {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService("wifi");
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			return wifiInfo.getBSSID();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getDeviceScan(Context context) {
		String str = "";
		try {
			if (CommonUtil.checkWifiState(context)) {
				WifiManager wifiManager = (WifiManager) context
						.getSystemService("wifi");
				List<ScanResult> list = wifiManager.getScanResults();
				ScanResult[] arrayOfScanResult1 = new ScanResult[list.size()];
				for (int i1 = 0; i1 < list.size(); i1++) {
					arrayOfScanResult1[i1] = ((ScanResult) list.get(i1));
				}
				ArrayList<String> arrayList = new ArrayList<String>();
				Arrays.sort(arrayOfScanResult1, new Comparator() {
					public int a(ScanResult anonymousScanResult1,
							ScanResult anonymousScanResult2) {
						int i = anonymousScanResult2.level
								- anonymousScanResult1.level;
						int j = 0;
						if (i > 0)
							j = 1;
						else if (i < 0) {
							j = -1;
						}

						return j;
					}

					public int compare(Object x0, Object x1) {
						return a((ScanResult) x0, (ScanResult) x1);
					}

				});
				for (ScanResult scanResult : arrayOfScanResult1) {
					arrayList.add(scanResult.BSSID);
				}

				str = TextUtils.join(",", arrayList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String[] getDeviceCode(Context context) {
		String[] arrayOfString = { "-1", "-1", "-1", "-1" };
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService("phone");
			if (telephonyManager != null) {
				if ((telephonyManager.getNetworkOperator() != null)
						&& (telephonyManager.getNetworkOperator().length() >= 5)) {
					int i1 = telephonyManager.getPhoneType();

					switch (i1) {
					case 1:
						GsmCellLocation gsmCellLocation = (GsmCellLocation) telephonyManager
								.getCellLocation();
						if (gsmCellLocation != null) {
							int i2 = ((GsmCellLocation) gsmCellLocation)
									.getCid();
							int i3 = ((GsmCellLocation) gsmCellLocation)
									.getLac();
							int i4 = Integer.valueOf(
									telephonyManager.getNetworkOperator()
											.substring(0, 3)).intValue();
							int i5 = Integer.valueOf(
									telephonyManager.getNetworkOperator()
											.substring(3, 5)).intValue();
							arrayOfString[0] = String.valueOf(i2);
							arrayOfString[1] = String.valueOf(i3);
							arrayOfString[2] = String.valueOf(i4);
							arrayOfString[3] = String.valueOf(i5);
						}
						break;
					case 0:
					case 2:
					default:
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayOfString;
	}

	private static class LocationHelper {
		private static LocationHelper mLocationHelper = new LocationHelper();
		private Location mLocation;
		private int mLocationType;
		private int mLocationState;
		private boolean e;

		private LocationHelper() {
			this.mLocationType = -1;
			this.mLocationState = -1;
			this.e = true;
		}

		public static LocationHelper getInstance() {
			return mLocationHelper;
		}

		private Location getLocation(Context context) {
			try {
				if (!this.e) {
					return null;
				}
				LocationManager locationManager = null;
				locationManager = (LocationManager) context
						.getSystemService("location");
				if (locationManager != null) {
					Location location = null;
					if (context
							.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0) {
						location = locationManager.getLastKnownLocation("gps");
					}
					if (location == null) {
						location = locationManager
								.getLastKnownLocation("network");

						if ((location != null)
								&& (System.currentTimeMillis()
										- location.getTime() < 600000L)) {
							this.mLocationType = 2;
							this.mLocation = location;
							return location;
						}

					} else if (System.currentTimeMillis() - location.getTime() < 600000L) {
						this.mLocationType = 0;
						this.mLocation = location;
						return location;
					}

					if ((context != null)
							&& ((this.mLocation == null) || (System
									.currentTimeMillis() > this.mLocation
									.getTime() + 600000L))) {
						synchronized (context) {
							String str = null;

							if (context
									.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == 0) {
								Criteria criteria;
								(criteria = new Criteria()).setAccuracy(2);
								((Criteria) criteria).setCostAllowed(false);
								str = locationManager.getBestProvider(
										(Criteria) criteria, true);
							}

							if (str == null) {
								this.mLocationState = 0;
								return null;
							}
							LocationListener listener = new LocationHelperListener(
									locationManager);
							locationManager.requestLocationUpdates(str, 0L,
									0.0F, listener, context.getMainLooper());
						}

					} else {
						return this.mLocation;
					}
				}

				this.mLocationState = 2;
				return null;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		private void setLocationInfo(Location location, int type) {
			this.mLocation = location;
			this.mLocationType = type;
		}

		private int getLocacc() {
			switch (this.mLocationType) {
			case 0:
				// GPS
				break;
			case 1:
				// Base
				break;
			case 2:
				// Wifi
				break;
			default:
				// Unknown
			}

			return this.mLocationType;
		}

		private int getLocstatus() {
			switch (this.mLocationState) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			}

			return this.mLocationState;
		}

		private long getLoctime() {
			if (this.mLocation != null) {
				return this.mLocation.getTime();
			}
			return 0L;
		}

		private String getLocationInfo(Location location) {
			String str = null;
			if (location != null) {
				str = location.getLatitude() + "," + location.getLongitude();
			}

			return str;
		}

		private String getLocationPlace(Location location) {
			String str = null;
			if (location != null) {
				GeoVO geo = getLocationGeoVO(location);
				if (geo != null && geo.getStatus().equals("OK")) {
					str = geo.getResult().getFormatted_address();
				}
			}
			return str;
		}

		private String getLocationCity(Location location) {
			String str = null;
			if (location != null) {
				GeoVO geo = getLocationGeoVO(location);
				if (geo != null && geo.getStatus().equals("OK")) {
					str = geo.getResult().getAddressComponent().getCity();
				}
			}
			return str;
		}

		private GeoVO getLocationGeoVO(Location location) {
			GeoVO geo = null;
			if (location != null) {
				String lat = location.getLatitude() + "";
				String lit = location.getLongitude() + "";
				String url = "http://api.map.baidu.com/geocoder?output=json&location="
						+ lat
						+ ","
						+ lit
						+ "&key=b3b3ab3588dd410aee70f4bcea9fe688";
				String jsonData = CommunicationUtil.executeStaticGet(url);
				if (null != jsonData) {
					geo = JsonHandler.parseGeo(jsonData);
				}
			}
			return geo;
		}

		static String getLocationPlace(LocationHelper locationHelper,
				Location location) {
			return locationHelper.getLocationPlace(location);
		}

		static String getLocationCity(LocationHelper locationHelper,
				Location location) {
			return locationHelper.getLocationCity(location);
		}

		static Location getLocation(LocationHelper locationHelper,
				Context context) {
			Location location = null;
			for (int i = 0; i < 20; i++) {
				location = locationHelper.getLocation(context);
				if (location == null) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					break;
				}
			}
			return location;
		}

		static String getLocationInfo(LocationHelper locationHelper,
				Location location) {
			return locationHelper.getLocationInfo(location);
		}

		static int getLocacc(LocationHelper locationHelper) {
			return locationHelper.getLocacc();
		}

		static int getLocstatus(LocationHelper locationHelper) {
			return locationHelper.getLocstatus();
		}

		static long getLoctime(LocationHelper locationHelper) {
			return locationHelper.getLoctime();
		}

		static void setLocationInfo(LocationHelper locationHelper,
				Location location, int i1) {
			locationHelper.setLocationInfo(location, i1);
		}

		private class LocationHelperListener implements LocationListener {
			public LocationManager mLocationManager;

			LocationHelperListener(LocationManager lm) {
				super();
				this.mLocationManager = lm;
			}

			public final void onLocationChanged(Location location) {
				DeviceInfoUtil.LocationHelper.setLocationInfo(
						DeviceInfoUtil.LocationHelper.this, location, 2);
				this.mLocationManager.removeUpdates(this);
			}

			public final void onProviderDisabled(String s) {
			}

			public final void onProviderEnabled(String s) {
			}

			public final void onStatusChanged(String s, int i, Bundle bundle) {
			}
		}
	}
}
