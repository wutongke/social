package com.cpstudio.zhuojiaren.imageloader;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.cpstudio.zhuojiaren.helper.UrlHelper;
import com.cpstudio.zhuojiaren.util.DeviceInfoUtil;

public class ImageGetForHttp {
	private static final String LOG_TAG = "ImageGetForHttp";
	private static final String DEFAULT_SERVER = UrlHelper.SERVER + "/";

	public static Bitmap downloadBitmap(String url, ImageFileCache ifc) {
		// final int IO_BUFFER_SIZE = 4 * 1024;
		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = AndroidHttpClient.newInstance("Android");
		if (!url.toLowerCase(Locale.getDefault()).startsWith("http")) {
			String middle = DeviceInfoUtil.getImagePrefix();
			if(middle == null){
				middle = "";
			}else{
				middle = middle + "/";
			}
			url = DEFAULT_SERVER + middle + url;
		}
		final HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					FilterInputStream fit = new FlushedInputStream(inputStream);
					String filePath = ifc.saveInputStreamToSd(fit, url);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(filePath, options);
					options.inSampleSize = calculateInSampleSize(options);
					options.inJustDecodeBounds = false;
					return BitmapFactory.decodeFile(filePath, options);// BitmapFactory.decodeStream(fit);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}
	public static Bitmap downloadBitmap(String url, ImageFileCache ifc, int height,int width) {
		// final int IO_BUFFER_SIZE = 4 * 1024;
		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = AndroidHttpClient.newInstance("Android");
		if (!url.toLowerCase(Locale.getDefault()).startsWith("http")) {
			String middle = DeviceInfoUtil.getImagePrefix();
			if(middle == null){
				middle = "";
			}else{
				middle = middle + "/";
			}
			url = DEFAULT_SERVER + middle + url;
		}
		final HttpGet getRequest = new HttpGet(url);
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}
			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
					FilterInputStream fit = new FlushedInputStream(inputStream);
					String filePath = ifc.saveInputStreamToSd(fit, url);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(filePath, options);
					options.inSampleSize = calculateInSampleSize(options,width,height);
					options.inJustDecodeBounds = false;
					return BitmapFactory.decodeFile(filePath, options);// BitmapFactory.decodeStream(fit);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// 源图片的宽度
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (width > reqWidth && height > reqHeight) {
			// 计算出实际宽度和目标宽度的比率
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.max(widthRatio, heightRatio);
		}
		return inSampleSize;
	}
	private static int calculateInSampleSize(BitmapFactory.Options options) {
		int width = options.outWidth;
		int height = options.outHeight;
		int i = 1;
		long bitmapSize = width * height * 4;
		while (true) {
			long maxMemory = Runtime.getRuntime().maxMemory();
			if (bitmapSize * 4 <= maxMemory * i) {
				return i;
			}
			i++;
		}
	}

	/*
	 * 
	 * An InputStream that skips the exact number of bytes provided, unless it
	 * reaches EOF.
	 */

	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}
}
