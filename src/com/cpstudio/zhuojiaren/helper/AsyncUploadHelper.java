package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

public class AsyncUploadHelper extends
		AsyncTask<List<String>, Integer, List<String>> {
	UploadManager uploadManager;
	private UploadCompleteCallback mCallback = null;
	ArrayList<String> keysList = new ArrayList<String>();

	public AsyncUploadHelper(UploadCompleteCallback mCallback) {
		super();
		this.mCallback = mCallback;
	}

	@Override
	protected List<String> doInBackground(List<String>... files) {
		// TODO Auto-generated method stub
		final String token = "gqyn9mD9OEVHoayK16ivmeCMcUgLNxVnxIjcrGCm:HBXaleKw-ugLBORZvT0zfOrT85Y=:eyJzY29wZSI6InpodW90ZXN0IiwiZGVhZGxpbmUiOjE0Mzc4ODY4ODl9";
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
					// TODO Auto-generated method stub
					if (arg2 != null) {
						String s = arg2.optString("key", "");
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
		mCallback.onReturn(result);
	}

	public interface UploadCompleteCallback {
		public void onReturn(List<String> keyList);
	}
}