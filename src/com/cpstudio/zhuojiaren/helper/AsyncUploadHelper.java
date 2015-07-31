package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.cpstudio.zhuojiaren.R;
import com.qiniu.android.storage.UploadManager;

public class AsyncUploadHelper extends
		AsyncTask<String, Integer, Map<String, StringBuilder>> {

	private ProgressDialog mDialog;

	UploadManager uploadManager;
	private ICompleteCallback mCallback = null;
	Map<String, ArrayList<String>> filesMap;
	String token;
	Activity mContext = null;

	public AsyncUploadHelper(Activity context, String token,
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
//		Future<Map<String, StringBuilder>> result = executor.submit(new Callable<Map<String, StringBuilder>>() {
//
//			@Override
//			public Map<String, StringBuilder> call() throws Exception {
//				// TODO Auto-generated method stub
//				int x=3;
//				x++;
//				return null;
//			}
//		});
		Map<String, StringBuilder> ans = null;
		try {
//			ans = result.get(5000, TimeUnit.MILLISECONDS);
			ans = result.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//			catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // ȡ�ý����ͬʱ���ó�ʱִ��ʱ��Ϊ5�롣ͬ��������future.get()��������ִ�г�ʱʱ��ȡ�ý��
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