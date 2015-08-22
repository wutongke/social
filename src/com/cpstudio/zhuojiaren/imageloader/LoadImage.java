package com.cpstudio.zhuojiaren.imageloader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.utils.ImageRectUtil;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
/**
 * 加载本地图片，图片下载类
 * @author lef
 * 使用了线程池、内存缓存、采用了同步下载，只有所有下载完成后，才能添加任务
 */
public class LoadImage {
	private ExecutorService executorService;
	private ImageMemoryCache memoryCache;
	private ImageFileCache fileCache;
	private Map<String, ImageView> taskMap;
	private Callback onReturnListener = null;
	private int round = 0;
	private int height = 0;
	private int width = 0;
	public LoadImage(String savePath) {
		executorService = Executors.newFixedThreadPool(5);
		memoryCache = new ImageMemoryCache();
		fileCache = new ImageFileCache(savePath);
		taskMap = new HashMap<String, ImageView>();
	}

	public LoadImage() {
		executorService = Executors.newFixedThreadPool(5);
		memoryCache = new ImageMemoryCache();
		fileCache = new ImageFileCache("zhuojiaren/userhead");
		taskMap = new HashMap<String, ImageView>();
	}

	public LoadImage(int round) {
		executorService = Executors.newFixedThreadPool(5);
		memoryCache = new ImageMemoryCache();
		fileCache = new ImageFileCache("zhuojiaren/userhead");
		taskMap = new HashMap<String, ImageView>();
		this.round = round;
	}
	public LoadImage(int round,int height,int width) {
		executorService = Executors.newFixedThreadPool(10);
		memoryCache = new ImageMemoryCache();
		fileCache = new ImageFileCache("zhuojiaren/userhead");
		taskMap = new HashMap<String, ImageView>();
		this.height = height;
		this.width = width;
		this.round = round;
	}
	public void beginLoad(String url, ImageView img){
		img.setTag(url);
		addTask(url, img);
		doTask();
	}
	public void addTask(String url, ImageView img) {
		if (null != url && !url.equals("")) {
			Bitmap bitmap = memoryCache.getBitmapFromCache(url);
			if (bitmap != null) {
				img.setImageBitmap(ImageRectUtil.toRoundCorner(bitmap, round));
			} else {
				synchronized (taskMap) {
					taskMap.put(Integer.toString(img.hashCode()), img);
				}
			}
		}
	}

	public void doTask() {
		synchronized (taskMap) {
			Collection<ImageView> con = taskMap.values();
			for (ImageView i : con) {
				if (i != null) {
					if (i.getTag() != null) {
						loadImage((String) i.getTag(), i);
					}
				}
			}
			taskMap.clear();
		}
	}

	private void loadImage(String url, ImageView img) {
		executorService.submit(new TaskWithResult(new TaskHandler(url, img),
				url));
	}

	private Bitmap getBitmap(String url) {
		Bitmap result;
		result = memoryCache.getBitmapFromCache(url);
		if (result == null) {
			result = fileCache.getImage(url);
			if (result == null) {
				if(width!=0&&height!=0){
					//从网络获取图片
					result = ImageGetForHttp.downloadBitmap(url, fileCache,height,width);
				}else{
					//从网络获取图片
					result = ImageGetForHttp.downloadBitmap(url, fileCache);
				}
				
				if (result != null) {
					memoryCache.addBitmapToCache(url, result);
					fileCache.saveBmpToSd(result, url);
				}
			} else {
				memoryCache.addBitmapToCache(url, result);
			}
		}
		return result;
	}

	@SuppressLint("HandlerLeak")
	private class TaskHandler extends Handler {
		private String url;
		private ImageView img;

		public TaskHandler(String url, ImageView img) {
			this.url = url;
			this.img = img;
		}

		@Override
		public void handleMessage(Message msg) {
			if (img.getTag().equals(url)) {
				if (msg.obj != null) {
					Bitmap bitmap = (Bitmap) msg.obj;
//					BitmapDrawable bitmapDrawable = (BitmapDrawable) img
//							.getDrawable();
//					if (bitmapDrawable != null
//							&& bitmapDrawable.getBitmap() != null
//							&& bitmapDrawable.getBitmap().isRecycled()) {
//						bitmapDrawable.getBitmap().recycle();
//					}
					if (round != 0) {
						img.setImageBitmap(ImageRectUtil.toRoundCorner(bitmap,
								round));
					} else {
						img.setImageBitmap(bitmap);
					}
					bitmap = null;
				}
			}
		}
	}

	private class TaskWithResult implements Callable<String> {
		private String url;
		private Handler handler;

		public TaskWithResult(Handler handler, String url) {
			this.url = url;
			this.handler = handler;
		}

		@Override
		public String call() throws Exception {
			Message msg = new Message();
			msg.obj = getBitmap(url);
			if (msg.obj != null) {
				handler.sendMessage(msg);
				doCallback(true, url);
			} else {
				doCallback(false, url);
			}
			return url;
		}
	}

	public void setOnReturnListener(Callback onReturnListener) {
		this.onReturnListener = onReturnListener;
	}

	private void doCallback(boolean result, String url) {
		if (onReturnListener != null) {
			onReturnListener.onReturn(result, url);
		}
	}

	public interface Callback {
		public boolean onReturn(boolean result, String url);
	}
}
