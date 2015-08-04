package com.cpstudio.zhuojiaren.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.cpstudio.zhuojiaren.R;
import com.utils.CipherUtil;

public class CommonUtil {

	public static boolean isEmpty(String paramString) {
		if ((paramString != null) && (paramString.length() != 0)) {
			return false;
		}
		return true;
	}

	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase(Locale.getDefault());
		} else {
			return "#";
		}
	}

	public static boolean startWithEn(String str) {
		char c = str.trim().substring(0, 1).charAt(0);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static String removeHTMLTag(String html) {
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}

	@SuppressWarnings("unchecked")
	public static String mapToUrl(HashMap<String, String> paramHashMap) {
		try {
			UrlEncodedFormEntity localUrlEncodedFormEntity = null;
			ArrayList<BasicNameValuePair> localArrayList = new ArrayList<BasicNameValuePair>();
			StringBuilder localStringBuilder = new StringBuilder();
			for (Object localObject = paramHashMap.keySet().iterator(); ((Iterator<Object>) localObject)
					.hasNext();) {
				String str = (String) ((Iterator<Object>) localObject).next();
				localArrayList.add(new BasicNameValuePair(str,
						(String) paramHashMap.get(str)));
			}

			localUrlEncodedFormEntity = new UrlEncodedFormEntity(
					localArrayList, "UTF-8");
			BufferedReader localObject = new BufferedReader(
					new InputStreamReader(
							localUrlEncodedFormEntity.getContent()));
			String str = null;
			while ((str = ((BufferedReader) localObject).readLine()) != null) {
				localStringBuilder.append(str);
			}

			return localStringBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static HashMap<String, String> urlToMap(String paramString) {
		HashMap<String, String> localHashMap = new HashMap<String, String>();

		if (paramString != null) {
			String[] arrayOfString1 = paramString.split("&");
			for (String str : arrayOfString1) {
				String[] arrayOfString3 = str.split("=");
				try {
					if (arrayOfString3.length == 2) {
						localHashMap.put(
								URLDecoder.decode(arrayOfString3[0], "UTF-8"),
								URLDecoder.decode(arrayOfString3[1], "UTF-8"));
					} else
						localHashMap.put(
								URLDecoder.decode(arrayOfString3[0], "UTF-8"),
								"");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		return localHashMap;
	}

	public static boolean checkWifiState(Context paramContext) {
		WifiManager localWifiManager = (WifiManager) paramContext
				.getSystemService("wifi");

		if (localWifiManager.getWifiState() == 3) {
			return true;
		}
		return false;
	}

	public static void displayToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void displayToast(Context context, int resid) {
		Toast.makeText(context, resid, Toast.LENGTH_SHORT).show();
	}
/**
 * 网络判断 0已连接
 * 1正在连接
 * 2未联网及其他
 * @param context
 * @return
 */
	public static int getNetworkState(Context context) {
		if(context==null)
			return 2;
		ConnectivityManager conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (mobile == State.CONNECTED || wifi == State.CONNECTED) {
			return 0;
		}
		if (mobile == State.CONNECTING || wifi == State.CONNECTING) {
			return 1;
		}
		return 2;
	}

	public static String getMD5String(String strOrg) {
		if ((strOrg == null) || (strOrg.length() == 0))
			return "";
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.reset();
			localMessageDigest.update(strOrg.getBytes("UTF-8"));
			return getType16Md5(localMessageDigest.digest(), "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String get32RandomString(String str) {
		if (null != str) {
			return getMD5String(str + System.currentTimeMillis());
		} else {
			return getMD5String(System.currentTimeMillis() + "");
		}
	}

	public static String getAesString(String strHX, String strOrg) {
		try {
			byte[] arrayOfByte1 = strHX.getBytes("UTF-8");
			byte[] arrayOfByte2 = new byte[16];
			System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0,
					Math.min(arrayOfByte1.length, 16));
			byte[] arrayOfByte3 = strOrg.getBytes("UTF-8");
			SecretKeySpec localSecretKeySpec = new SecretKeySpec(arrayOfByte2,
					"AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
			cipher.init(1, localSecretKeySpec);
			byte[] arrayOfByte4 = cipher.doFinal(arrayOfByte3);
			byte[] arrayOfByte5 = CipherUtil.getAESByte(arrayOfByte4, 2);
			return new String(arrayOfByte5);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getDesString(String keySpec, String strOrg) {
		try {
			DESKeySpec localDESKeySpec = new DESKeySpec(keySpec.getBytes());
			SecretKeyFactory localSecretKeyFactory = SecretKeyFactory
					.getInstance("DES");
			SecretKey localSecretKey = localSecretKeyFactory
					.generateSecret(localDESKeySpec);
			Cipher cipher = Cipher.getInstance("DES");
			IvParameterSpec localIvParameterSpec1 = new IvParameterSpec(
					"12345678".getBytes());
			IvParameterSpec localIvParameterSpec2 = localIvParameterSpec1;
			cipher.init(1, localSecretKey, localIvParameterSpec2);
			byte[] arrayOfByte = cipher.doFinal(strOrg.getBytes());
			return CipherUtil.getDESString(arrayOfByte, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFileMD5(String paramString) {
		FileInputStream localFileInputStream = null;
		try {
			localFileInputStream = new FileInputStream(paramString);
			byte[] arrayOfByte = new byte[1024];
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			int i = 0;
			while ((i = localFileInputStream.read(arrayOfByte)) > 0) {
				localMessageDigest.update(arrayOfByte, 0, i);
			}
			return getType16Md5(localMessageDigest.digest(), "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (localFileInputStream != null) {
				try {
					localFileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	private static String getType16Md5(byte[] paramArrayOfByte,
			String paramString) {
		StringBuilder sb = new StringBuilder();
		for (int k : paramArrayOfByte) {
			String str = Integer.toHexString(0xFF & k);
			if (str.length() == 1)
				sb.append("0").append(str);
			else {
				sb.append(str);
			}
		}
		return sb.toString();
	}

	public static boolean checkNum(String paramString) {
		if (paramString == null) {
			return false;
		}
		Pattern localPattern = Pattern.compile("[0-9]*");
		return localPattern.matcher(paramString).matches();
	}

	public static void startNetWorkDialog(final Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View textEntryView = factory.inflate(R.layout.dlg_open_network,
				null);
		new AlertDialog.Builder(context)
				.setTitle(R.string.info8)
				.setIcon(R.drawable.ico_alert)
				.setView(textEntryView)
				.setPositiveButton(R.string.OK,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								RadioButton wifi = (RadioButton) (textEntryView)
										.findViewById(R.id.wifiRadio);
								if (wifi.isChecked()) {
									context.startActivity(new Intent(
											Settings.ACTION_WIFI_SETTINGS));
								} else {
									context.startActivity(new Intent(
											Settings.ACTION_WIRELESS_SETTINGS));
								}
							}
						})
				.setNegativeButton(R.string.CANCEL,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();

	}

	public static String getNowTimeStr() {
		return getNowTimeStr("yyyyMMddHHmmssSSS");
	}

	public static String getNowTimeStr(String format) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(format,
				Locale.CHINESE);
		String str = localSimpleDateFormat.format(new Date());
		return str;
	}

	public static String getTime(long time) {
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		long curDate = time;
		String str = formatter.format(curDate);
		return str;
	}

	public static String getMSTime(int time) {
		String timeStr = "00:00";
		int hour = time / 3600;
		int minute = (time - hour * 3600) / 60;
		int second = time - hour * 3600 - minute * 60;
		timeStr = toMSTimeStr(minute) + ":" + toMSTimeStr(second);
		if (hour > 0) {
			timeStr = toMSTimeStr(hour) + ":" + timeStr;
		}
		return timeStr;
	}

	public static String getMSTime(double time) {
		time = (Math.round(time * 10)) / 10.0;
		String timeStr = "00:00";
		int hour = (int) (time / 3600);
		int minute = (int) ((time - hour * 3600) / 60);
		int second = (int) (time - hour * 3600 - minute * 60);
		int ss = (int) Math
				.round((time - hour * 3600 - minute * 60 - second) * 10);
		timeStr = toMSTimeStr(minute) + ":" + toMSTimeStr(second) + "." + ss;
		if (hour > 0) {
			timeStr = toMSTimeStr(hour) + ":" + timeStr;
		}
		return timeStr;
	}

	public static double returnMStime(String timeStr) {
		double time = 0;
		String ss = null;
		if (timeStr.indexOf(".") != -1) {
			String[] timeStrs = timeStr.split("\\.");
			ss = timeStrs[1];
			timeStr = timeStrs[0];
		}
		if (timeStr.indexOf(":") != -1) {
			String[] ms = timeStr.split(":");
			if (ms.length == 3) {
				time = Integer.valueOf(ms[2]) + Integer.valueOf(ms[1]) * 60
						+ Integer.valueOf(ms[0]) * 3600;
			} else {
				time = Integer.valueOf(ms[1]) + Integer.valueOf(ms[0]) * 60;
			}
		}
		if (ss != null) {
			time += Double.valueOf(ss) / 10;
		}
		return time;
	}

	public static int returnMStimeInt(String timeStr) {
		int time = 0;
		if (timeStr.indexOf(":") != -1) {
			String[] ms = timeStr.split(":");
			if (ms.length == 3) {
				time = Integer.valueOf(ms[2]) + Integer.valueOf(ms[1]) * 60
						+ Integer.valueOf(ms[0]) * 3600;
			} else {
				time = Integer.valueOf(ms[1]) + Integer.valueOf(ms[0]) * 60;
			}
		} else {
			try {
				time = Integer.valueOf(timeStr);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return time;
	}

	public static String toMSTimeStr(int time) {
		String timeStr = "00";
		if (time > 0) {
			if (time < 10) {
				timeStr = "0" + time;
			} else {
				timeStr = "" + time;
			}
		}
		return timeStr;
	}

	public static String readSDCardStat() {
		String totalSpaceStr = "";
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long blockCount = sf.getBlockCount();
			double totalSpace = Double.valueOf(blockSize * blockCount / (1024)) / 1024;
			String temp = String.valueOf(totalSpace);
			if (temp.indexOf(".") != -1) {
				totalSpaceStr = temp.substring(0, temp.indexOf(".") + 2);
			} else {
				totalSpaceStr = temp;
			}
			totalSpaceStr = totalSpace + "MB";
			if (totalSpace > 1024) {
				totalSpace = totalSpace / 1024;
				temp = String.valueOf(totalSpace);
				if (temp.indexOf(".") != -1) {
					totalSpaceStr = temp.substring(0, temp.indexOf(".") + 2);
				} else {
					totalSpaceStr = temp;
				}
				totalSpaceStr = totalSpaceStr + "GB";
			}
		}
		return totalSpaceStr;
	}

	public static String calcTime(String time, boolean showSec,
			boolean showFull) {
		if (time != null && !time.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());
			try {
				Date date = df.parse(time);
				long day = date.getTime();
				long today = df.parse(df.format(System.currentTimeMillis()))
						.getTime();
				String showTime = "";
				String HHmmss = time.split(" ")[1];
				String[] str = HHmmss.split(":");
				if (showSec) {
					showTime = getDaySplit(str[0]) + str[0] + ":" + str[1] + ":" + str[2];
				} else {
					showTime = getDaySplit(str[0]) + str[0] + ":" + str[1];				
				}
				if (day == today) {
					return showTime;
				} else if (day > today - 24 * 3600 * 1000) {
					if(showFull){
						return "昨天 " + showTime;						
					}else{
						return "昨天 " + getDaySplit(str[0]);	
					}
				} else {
					if(!showFull){
						showTime = "";
					}
					String yyMMdd = time.split(" ")[0];
					String[] str2 = yyMMdd.split("-|/");
					SimpleDateFormat df2 = new SimpleDateFormat("yyyy",
							Locale.getDefault());
					Date date2 = df2.parse(time);
					long day2 = date2.getTime();
					long thisYear = df2.parse(
							df2.format(System.currentTimeMillis())).getTime();
					if (day2 != thisYear) {
						return str2[0] + "年" + str2[1] + "月" + str2[2] + "日"
								+ showTime;
					}
					return str2[1] + "月" + str2[2] + "日" + showTime;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return time;
		} else {
			return "";
		}
	}


	public static String getDaySplit(String hourStr) {
		String time = "";
		try {
			int hour = Integer.valueOf(hourStr);
			if (hour < 1) {
				time = "午夜";
			} else if (hour < 6) {
				time = "凌晨";
			} else if (hour < 9) {
				time = "早晨";
			} else if (hour < 12) {
				time = "上午";
			} else if (hour < 14) {
				time = "中午";
			} else if (hour < 18) {
				time = "下午";
			} else if (hour < 24) {
				time = "晚上";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String calcTime(String time, boolean showSec) {
		return calcTime(time, showSec, false);
	}

	public static String calcTime(String time) {
		return calcTime(time, false);
	}

	public static long calcTimeToNow(String time) {
		if (time != null && !time.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			try {
				Date date = df.parse(time);
				return System.currentTimeMillis() - date.getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static long calcTimeToTime(String from, String to) {
		if (from != null && !from.equals("") && to != null && !to.equals("")) {
			SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			try {
				Date fromdate = df.parse(from);
				Date todate = df.parse(to);
				return todate.getTime() - fromdate.getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	public static void recycleBitmap(ViewGroup viewGroup) {
		// if (viewGroup != null) {
		// // recycleDrawable(viewGroup.getBackground());
		// int count = viewGroup.getChildCount();
		// for (int i = 0; i < count; i++) {
		// View view = viewGroup.getChildAt(i);
		// // recycleDrawable(view.getBackground());
		// if (view instanceof GifView) {
		// // recycleGifView((GifView) view);
		// } else if (view instanceof ImageView) {
		// // recycleDrawable(((ImageView) view).getDrawable());
		// } else if (view instanceof ViewGroup) {
		// recycleBitmap((ViewGroup) view);
		// }
		// }
		// }
	}

	public static void recycleGifView(GifView view) {
		if (view != null) {
			view.destroy();
			view = null;
		}
	}

	public static void recycleDrawable(Drawable drawable) {
		if (drawable != null) {
			if (drawable instanceof BitmapDrawable) {
				BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
				Bitmap bitmap = bitmapDrawable.getBitmap();
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
				}
			}
		}
	}
}
