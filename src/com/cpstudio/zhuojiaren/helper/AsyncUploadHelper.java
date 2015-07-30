package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.cpstudio.zhuojiaren.R;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

public class AsyncUploadHelper extends
		AsyncTask<List<String>, Integer, List<String>> {

	private ProgressDialog mDialog;

	UploadManager uploadManager;
	private UploadCompleteCallback mCallback = null;
	ArrayList<String> keysList = new ArrayList<String>();
	String token;
	Context mContext = null;
	public AsyncUploadHelper(Context context, String token,
			UploadCompleteCallback mCallback) {
		super();
		this.mContext = context;
		this.mCallback = mCallback;
		this.token = token;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

		if (mContext != null) {
			mDialog = new ProgressDialog(mContext);
			mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mDialog.setTitle(R.string.info63);
			mDialog.setMessage(mContext.getString(R.string.infofile));
			mDialog.setIcon(R.drawable.ico_alert);
			mDialog.setIndeterminate(false);
			mDialog.setCanceledOnTouchOutside(false);

			mDialog.show();
		}

	}

	@Override
	protected List<String> doInBackground(List<String>... files) {
		// TODO Auto-generated method stub

		if (token == null)
			token = "gqyn9mD9OEVHoayK16ivmeCMcUgLNxVnxIjcrGCm:i7rT4MfI_Wg2guWBLyOV5CJQmYw=:eyJzY29wZSI6InpodW90ZXN0IiwiZGVhZGxpbmUiOjE0MzgyMzk1ODJ9";
		
//token="gqyn9mD9OEVHoayK16ivmeCMcUgLNxVnxIjcrGCm:Ujs0kdVbywu6OQwYpZhuqLJSIL8=:eyJzY29wZSI6InpodW90ZXN0IiwiZGVhZGxpbmUiOjE0MzgyNTgyOTZ9";
		
		// 真实环境需要替换 final String token = uploadFileToken;
		List<String> filePathsList = files[0];
		final List<String> keys = Collections
				.synchronizedList(new ArrayList<String>());
		final CountDownLatch countLock = new CountDownLatch(
				filePathsList.size());
		if (uploadManager == null)
			uploadManager = new UploadManager();

		for (String path : files[0]) {
			uploadManager.put(path, null, token, new UpCompletionHandler() {
				@Override
				public void complete(String key, ResponseInfo arg1,
						JSONObject arg2) {
					// arg1:{ResponseInfo:1438236401224267,status:401,
					// reqId:RmcAAAf1X1xjpPUT, xlog:UP/401, xvia:null,
					// host:upload.qiniu.com, path:/mkblk/3226274,
					// ip:106.38.227.5, port:-1, duration:1.412000 s,
					// time:1438236402, sent:42,error:token is expired 72273
					// seconds}
					// status为200则正常

					// TODO Auto-generated method stub
					// token过期时会返回null
					if (arg1.isOK() && arg2 != null) {
						String s = arg2.optString("key", "");
						Log.i("token", arg1.toString());
						if (s != null)
							keys.add(s);
					}
					countLock.countDown();
				}

			}, new UploadOptions(null, null, false, new UpProgressHandler() {
				public void progress(String key, double percent) {
				}
			}, null));
		}
		try {
			countLock.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return keys;
	}

	@Override
	protected void onPostExecute(List<String> result) {
		// TODO Auto-generated method stub
		if (mDialog != null)
			mDialog.dismiss();
		if (mCallback != null)
			mCallback.onReturn(result);
	}

	public interface UploadCompleteCallback {
		public void onReturn(List<String> keyList);
	}
}