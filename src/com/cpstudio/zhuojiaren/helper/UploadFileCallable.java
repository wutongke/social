package com.cpstudio.zhuojiaren.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
/**
 * 七牛云图片上传核心类，保证多张图片上传完成
 * @author lz
 *
 */
public class UploadFileCallable implements Callable<Map<String, StringBuilder>> {
	UploadManager uploadManager;
	ArrayList<String> keysList = new ArrayList<String>();
	String token;
	Context mContext = null;
	Map<String, ArrayList<String>> filesMap;
	Map<String, StringBuilder> resultMap = new HashMap<String, StringBuilder>();

	public UploadFileCallable(String token, Context mContext,
			Map<String, ArrayList<String>> filesMap) {
		this.filesMap = filesMap;
		this.token = token;
		this.mContext = mContext;
	}

	@Override
	public Map<String, StringBuilder> call() throws Exception {
		// TODO Auto-generated method stub
		if (filesMap == null || filesMap.size() < 1)
			return resultMap;
		if (uploadManager == null)
			uploadManager = new UploadManager();
		resultMap = Collections.synchronizedMap(resultMap);
		int fileNums = 0;
		for (Map.Entry<String, ArrayList<String>> entry : filesMap.entrySet()) {
			ArrayList<String> fileKeysList = entry.getValue();
			if (fileKeysList != null)
				fileNums += fileKeysList.size();
		}

		final CountDownLatch countLock = new CountDownLatch(fileNums);

		for (Map.Entry<String, ArrayList<String>> entry : filesMap.entrySet()) {
			final String key = entry.getKey();
			ArrayList<String> fileKeysList = entry.getValue();
			for (String path : fileKeysList) {
				uploadManager.put(path, null, token, new UpCompletionHandler() {
					@Override
					public void complete(String reqKey, ResponseInfo arg1,
							JSONObject arg2) {
						if (arg1.isOK() && arg2 != null) {
							String s = arg2.optString("key", "");
							Log.i("token", key + arg1.toString());
							if (s != null) {
								StringBuilder sb = null;
								if (resultMap.containsKey(key)) {
									sb = resultMap.get(key);
									sb.append(",");
									sb.append(s);
								} else
									sb = new StringBuilder(s);
								resultMap.put(key, sb);
							}
						} else {
							Log.i("token", key + "，上传失败:" + arg1.toString());
						}
						countLock.countDown();
					}
				}, new UploadOptions(null, null, false,
						new UpProgressHandler() {
							public void progress(String reqkey, double percent) {
							}
						}, null));
			}
		}
		try {
			countLock.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return resultMap;
	}
}
