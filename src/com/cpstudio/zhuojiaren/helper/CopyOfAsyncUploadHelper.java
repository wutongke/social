package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.cpstudio.zhuojiaren.R;
import com.cpstudio.zhuojiaren.helper.AsyncUploadHelper.UploadCompleteCallback;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

public class CopyOfAsyncUploadHelper extends
		AsyncTask<String, Integer, Map<String, StringBuilder>> {

	private ProgressDialog mDialog;

	UploadManager uploadManager;
	private ICompleteCallback mCallback = null;
	Map<String, ArrayList<String>> filesMap;
	String token;
	Activity mContext = null;

	public CopyOfAsyncUploadHelper(Activity context, String token,
			Map<String, ArrayList<String>> filesMap,
			ICompleteCallback mCallback) {
		this.filesMap = filesMap;
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
	protected Map<String, StringBuilder> doInBackground(String... arg0) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newCachedThreadPool();
		UploadFileCallable task = new UploadFileCallable(token, mContext,
				filesMap);
		Future<Map<String, StringBuilder>> result = executor.submit(task);

		Map<String, StringBuilder> ans = null;
		try {
			ans = result.get(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 取得结果，同时设置超时执行时间为5秒。同样可以用future.get()，不设置执行超时时间取得结果
		finally {
			executor.shutdown();
		}
		return ans;
	}

	@Override
	protected void onPostExecute(Map<String, StringBuilder> map) {
		// TODO Auto-generated method stub
		if (mDialog != null)
			mDialog.dismiss();
		if (mCallback != null)
			mCallback.onReturn(map);
	}

	public interface ICompleteCallback {
		public void onReturn(Map<String, StringBuilder> map);
	}
}