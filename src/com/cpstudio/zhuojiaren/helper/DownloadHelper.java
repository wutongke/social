package com.cpstudio.zhuojiaren.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.utils.FileUtil;

import android.os.AsyncTask;

public class DownloadHelper extends AsyncTask<String, Integer, Boolean> {
	private String mFileUrl;
	private String mFilePath;
	private String mFileName;
	private FinishCallback mCallback = null;
	private String jsonData = null;
	private String mId = null;
	private int respCode = 0;

	public DownloadHelper(String fileUrl, String savePath, String saveName,
			FinishCallback callback, String id) {
		this.mFileUrl = fileUrl;
		this.mFilePath = savePath;
		this.mFileName = saveName;
		this.mCallback = callback;
		this.mId = id;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected Boolean doInBackground(String... arg0) {
		InputStream inputStream = null;
		HttpURLConnection urlConn = null;
		try {
			FileUtil fileUtils = new FileUtil();
			if (fileUtils.isFileExist(mFilePath + mFileName)) {
				return true;
			}
			URL url = new URL(ZhuoCommHelper.SERVER + "/" + mFileUrl);
			HttpURLConnection.setFollowRedirects(true);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.connect();
			respCode = urlConn.getResponseCode();
			inputStream = urlConn.getInputStream();
			File resultFile = fileUtils.write2SDFromInput(mFilePath, mFileName,
					inputStream);
			if (resultFile != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) {
					inputStream.close();
				}
				if (null != urlConn) {
					urlConn.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (null != mCallback) {
			if (result == true) {
				jsonData = "{\"code\":\"10000\",\"msg\":\"\",\"data\":{\"id\":\""
						+ mId
						+ "\",\"savepath\":\""
						+ mFilePath
						+ mFileName
						+ "\"}}";
			} else {
				if (respCode != 0) {
					jsonData = "{\"code\":\"10000\",\"msg\":\"\",\"data\":{\"id\":\""
							+ mId + "\",\"savepath\":\"\"}}";
				} else {
					jsonData = "{\"code\":\"90001\",\"msg\":\"download error\",\"data\":{\"id\":\""
							+ mId + "\",\"savepath\":\"\"}}";
				}
			}
			this.mCallback.onReturn(jsonData);
		}
	}

	public interface FinishCallback {
		public boolean onReturn(String rs);
	}
}
