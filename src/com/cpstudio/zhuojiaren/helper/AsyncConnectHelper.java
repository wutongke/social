package com.cpstudio.zhuojiaren.helper;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.cpstudio.zhuojiaren.R;
import com.utils.CommunicationUtil;

public class AsyncConnectHelper extends AsyncTask<String, Integer, Boolean> {
	private ProgressDialog mDialog;
	private Activity mActivity = null;
	private String mUrl = null;
	private boolean mThunckMode = true;
	private List<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();
	private Map<String, File> mFiles = null;
	private FinishCallback mCallback = null;
	private static final int HTTP_GET = 0;
	private static final int HTTP_POST = 1;
	private int mType = HTTP_POST;
	private String jsonData = null;
	private OnCancelListener cancel = null;
	private boolean cancelable = true;
	private HttpURLConnection conn = null;
	private HttpClient httpClient = null;
	private int responseCode = -1;

	public AsyncConnectHelper(String url, FinishCallback callback,
			Activity activity) {
		this.mActivity = activity;
		mType = HTTP_GET;
		mUrl = url;
		this.mCallback = callback;
	}

	public AsyncConnectHelper(List<NameValuePair> nameValuePairs, String url,
			FinishCallback callback, Activity activity) {
		this.mActivity = activity;
		mNameValuePairs = nameValuePairs;
		mType = HTTP_POST;
		mUrl = url;
		this.mCallback = callback;
	}

	public AsyncConnectHelper(ArrayList<String> paths,
			List<NameValuePair> nameValuePairs, String url, boolean thunckMode,
			FinishCallback callback, Activity activity) {
		this.mActivity = activity;
		mNameValuePairs = nameValuePairs;
		Map<String, File> files = new HashMap<String, File>();
		int i = 0;
		for (String path : paths) {
			files.put("img" + i, new File(path));
			i++;
		}
		mFiles = files;
		mThunckMode = thunckMode;
		mUrl = url;
		this.mCallback = callback;
	}

	public AsyncConnectHelper(Map<String, String> paths,
			List<NameValuePair> nameValuePairs, String url, boolean thunckMode,
			FinishCallback callback, Activity activity) {
		this.mActivity = activity;
		mNameValuePairs = nameValuePairs;
		Map<String, File> files = new HashMap<String, File>();
		for (String name : paths.keySet()) {
			String path = paths.get(name);
			files.put(name, new File(path));
		}
		mFiles = files;
		mThunckMode = thunckMode;
		mUrl = url;
		this.mCallback = callback;
	}

	public AsyncConnectHelper(String path, List<NameValuePair> nameValuePairs,
			String url, boolean thunckMode, FinishCallback callback,
			Activity activity) {
		this.mActivity = activity;
		mNameValuePairs = nameValuePairs;
		Map<String, File> files = new HashMap<String, File>();
		files.put("img0", new File(path));
		mFiles = files;
		mThunckMode = thunckMode;
		mUrl = url;
		this.mCallback = callback;
	}

	public void setCancel(OnCancelListener cancel) {
		this.cancel = cancel;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mActivity != null) {
			mDialog = new ProgressDialog(mActivity);
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setTitle(R.string.info63);
			if (mType == HTTP_GET) {
				mDialog.setMessage(mActivity.getString(R.string.info79));
			} else {
				mDialog.setMessage(mActivity.getString(R.string.info79));
			}
			mDialog.setIcon(R.drawable.ico_alert);
			mDialog.setIndeterminate(false);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.setCancelable(cancelable);
			mDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					if (conn != null) {
						conn.disconnect();
					}
					if (httpClient != null) {
						httpClient.getConnectionManager().shutdown();
					}
					if (null != cancel) {
						cancel.onCancel(dialog);
					}
				}
			});
			mDialog.show();
		}
	}

	@Override
	protected Boolean doInBackground(String... url) {
		boolean result = false;
		try {
			CommunicationUtil comm = new CommunicationUtil();
			if (mFiles != null) {
				conn = comm.executePost(mUrl, mNameValuePairs, mFiles,
						mThunckMode);
			} else {
				Log.i("Debug",mUrl);
				if (mType == HTTP_GET) {
					httpClient = comm.executeGet(mUrl);
				} else {
					httpClient = comm.executePost(mUrl, mNameValuePairs);
				}
			}
			jsonData = comm.getResult();
			Log.i("Debug","post"+mUrl+"²ÎÊý£º"+mNameValuePairs.toString());
			Log.i("Debug",jsonData);
			responseCode = comm.getResponseCode();
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (mActivity != null) {
			mDialog.dismiss();
		}
		if (null != mCallback) {
			this.mCallback.onReturn(jsonData, responseCode);
		}
	}

	public interface FinishCallback {
		public boolean onReturn(String rs, int responseCode);
	}
}
