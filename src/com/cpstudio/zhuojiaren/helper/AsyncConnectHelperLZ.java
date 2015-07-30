package com.cpstudio.zhuojiaren.helper;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.cpstudio.zhuojiaren.R;
import com.utils.CommunicationUtil;

//对于文件只上传对应的key,及一个普通的key,value
public class AsyncConnectHelperLZ extends AsyncTask<String, Integer, Boolean> {
	private ProgressDialog mDialog;
	private Activity mActivity = null;
	private String mUrl = null;
	private boolean mThunckMode = true;
	private List<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();
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

	public AsyncConnectHelperLZ(String url, FinishCallback callback,
			Activity activity) {
		this.mActivity = activity;
		mType = HTTP_GET;
		mUrl = url;
		this.mCallback = callback;
	}

	public AsyncConnectHelperLZ(List<NameValuePair> nameValuePairs, String url,
			FinishCallback callback, Activity activity) {
		this.mActivity = activity;
		mNameValuePairs = nameValuePairs;
		mType = HTTP_POST;
		mUrl = url;
		this.mCallback = callback;
	}

	public AsyncConnectHelperLZ(
			List<NameValuePair> nameValuePairs, String url, boolean thunckMode,
			FinishCallback callback, Activity activity) {
		this.mActivity = activity;
		mNameValuePairs = nameValuePairs;
//		int i = 0;
//		if (fileKeys != null && fileKeys.size() > 0) {
//			String keysStr = fileKeys.get(0);
//			for (i = 1; i < fileKeys.size(); i++)
//				keysStr += ("," + fileKeys.get(i));
//			mNameValuePairs.add(new BasicNameValuePair("file", keysStr));
//		}
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
				mDialog.setMessage(mActivity.getString(R.string.info64));
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
			if (mType == HTTP_GET) {
				httpClient = comm.executeGet(mUrl);
			} else {
				httpClient = comm.executePost(mUrl, mNameValuePairs);
			}

			jsonData = comm.getResult();
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
