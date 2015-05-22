package com.cpstudio.zhuojiaren.imageloader;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;

public class ImageMemoryCache {
	private static final int HARD_CACHE_CAPACITY = 30;
	private HashMap<String, Bitmap> mHardBitmapCache;
	private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			HARD_CACHE_CAPACITY / 2);

	public ImageMemoryCache() {
		mHardBitmapCache = new LinkedHashMap<String, Bitmap>(
				HARD_CACHE_CAPACITY / 2, 0.75f, true) {
			/**
			 * 
			 * */
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(
					LinkedHashMap.Entry<String, Bitmap> eldest) {
				if (size() > HARD_CACHE_CAPACITY) {
					// Entries push-out of hard reference cache are transferred
					// to soft reference cache
					mSoftBitmapCache.put(eldest.getKey(),
							new SoftReference<Bitmap>(eldest.getValue()));
					return true;
				} else
					return false;
			}
		};
	}

	/**
	 * 
	 * 从缓存中获取图片
	 */
	public Bitmap getBitmapFromCache(String url) {
		synchronized (mHardBitmapCache) {
			final Bitmap bitmap = mHardBitmapCache.get(url);//final
			if (bitmap != null) {
				mHardBitmapCache.remove(url);
				mHardBitmapCache.put(url, bitmap);
				return bitmap;
			}
		}
		SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
		if (bitmapReference != null) {
			final Bitmap bitmap = bitmapReference.get();//final
			if (bitmap != null) {
				mHardBitmapCache.put(url, bitmap);
				mSoftBitmapCache.remove(url);
				return bitmap;
			} else {
				mSoftBitmapCache.remove(url);
			}
		}
		return null;
	}

	/*** 添加图片到缓存 ***/
	public void addBitmapToCache(String url, Bitmap bitmap) {
		long size = getBitmapsize(bitmap);
		if (size > 256 * 256 * 4) {
			return;
		}
		if (bitmap != null) {
			synchronized (mHardBitmapCache) {
				mHardBitmapCache.put(url, bitmap);
			}
		}
	}

	@SuppressLint("NewApi")
	public long getBitmapsize(Bitmap bitmap) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		return bitmap.getRowBytes() * bitmap.getHeight();
	}
}
