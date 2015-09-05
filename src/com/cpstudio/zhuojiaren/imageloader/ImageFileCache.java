package com.cpstudio.zhuojiaren.imageloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.cpstudio.zhuojiaren.util.CommonUtil;
import com.utils.ImageRectUtil;

public class ImageFileCache {
	private final String cachdir;

	private static final String WHOLESALE_CONV = ".cach";

	private static final long mTimeDiff = 3 * 24 * 60 * 60 * 1000;

	public ImageFileCache(String savePath) {
		this.cachdir = savePath;
		removeCache(getDirectory());
	}

	public Bitmap getImage(final String url) {
		final String path = getDirectory() + "/" + convertUrlToFileName(url);
		File file = new File(path);
		if (file.exists()) {
			Bitmap bmp = null;
			try {
				bmp = ImageRectUtil.memoryMaxImage(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (bmp == null) {
				file.delete();
			} else {
				updateFileTime(path);
				return bmp;
			}
		}

		return null;

	}
	public Bitmap getImage(Context context,final String url,int height,int width) {
		final String path = getDirectory() + "/" + convertUrlToFileName(url);
		File file = new File(path);
		if (file.exists()) {
			Bitmap bmp = null;
			try {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(path, options);
				options.inSampleSize = ImageGetForHttp.calculateInSampleSize(options,width,height);
				options.inJustDecodeBounds = false;
				bmp = BitmapFactory.decodeFile(path, options);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (bmp == null) {
				file.delete();
			} else {
				updateFileTime(path);
				return bmp;
			}
		}

		return null;

	}

	/*** ����ռ��С ****/

	private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10;

	public void saveBmpToSd(Bitmap bm, String url) {
		if (bm == null) {
			return;
		}

		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			return;
		}
		String filename = convertUrlToFileName(url);
		String dir = getDirectory();
		File file = new File(dir + "/" + filename);
		if(file.exists()){
			return;
		}
		try {
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (FileNotFoundException e) {
			Log.w("ImageFileCache", "FileNotFoundException");
		} catch (IOException e) {
			Log.w("ImageFileCache", "IOException");
		}
	}
	
	public String saveInputStreamToSd(InputStream is, String url) {
		if (is == null) {
			return null;
		}
		if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			return null;
		}
		String filename = convertUrlToFileName(url);
		String dir = getDirectory();
		File file = new File(dir + "/" + filename);
		if(file.exists()){
			return null;
		}
		try {
			int byteread = 0;
			file.createNewFile();
			OutputStream outStream = new FileOutputStream(file);
			byte[] buffer = new byte[1204];
            while ((byteread = is.read(buffer)) != -1) {
            	outStream.write(buffer, 0, byteread);
            }
			outStream.flush();
			outStream.close();
			return file.getAbsolutePath();
		} catch (FileNotFoundException e) {
			Log.w("ImageFileCache", "FileNotFoundException");
		} catch (IOException e) {
			Log.w("ImageFileCache", "IOException");
		}
		return null;
	}

	private static final int CACHE_SIZE = 10;


	/**
	 * 
	 * ����洢Ŀ¼�µ��ļ���С��
	 * 
	 * ���ļ��ܴ�С���ڹ涨��CACHE_SIZE����sdcardʣ��ռ�С��FREE_SD_SPACE_NEEDED_TO_CACHE�Ĺ涨
	 * 
	 * ��ôɾ��40%���û�б�ʹ�õ��ļ�
	 * 
	 * 
	 * 
	 * @param dirPath
	 * 
	 * @param filename
	 */

	private boolean removeCache(String dirPath) {
		File dir = new File(dirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return true;
		}

		if (!android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}

		int dirSize = 0;
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().contains(WHOLESALE_CONV)) {
				dirSize += files[i].length();
			}
		}

		if (dirSize > CACHE_SIZE * MB
				|| FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			int removeFactor = (int) ((0.4 * files.length) + 1);
			Arrays.sort(files, new FileLastModifSort());
			Log.i("ImageFileCache", "clear cache");
			for (int i = 0; i < removeFactor; i++) {
				if (files[i].getName().contains(WHOLESALE_CONV)) {
					files[i].delete();
				}
			}
		}

		if (freeSpaceOnSd() <= CACHE_SIZE) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * �����ļ�������޸�ʱ���������*
	 */

	private class FileLastModifSort implements Comparator<File> {
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 
	 * ɾ�������ļ�
	 * 
	 * 
	 * 
	 * @param dirPath
	 * 
	 * @param filename
	 */

	public void removeExpiredCache(String dirPath, String filename) {
		File file = new File(dirPath, filename);
		if (System.currentTimeMillis() - file.lastModified() > mTimeDiff) {
			Log.i("ImageFileCache", "Clear some expiredcache files ");
			file.delete();
		}
	}

	/**
	 * 
	 * �޸��ļ�������޸�ʱ��
	 * 
	 * ������Ҫ����,�Ƿ�ʹ�õ�ͼƬ���ڸ�Ϊ��ǰ����
	 * 
	 * @param path
	 */

	public void updateFileTime(String path) {
		File file = new File(path);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/**
	 * 
	 * ����sdcard�ϵ�ʣ��ռ�
	 * 
	 * @return
	 */

	private int MB = 1024 * 1024;

	private int freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
				.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	/** ��urlת���ļ��� **/

	private String convertUrlToFileName(String url) {
		String[] strs = url.split("/");
		String filename = strs[strs.length - 1] + WHOLESALE_CONV;
		filename = CommonUtil.getMD5String(url) + WHOLESALE_CONV;
		return filename;
	}

	/** ��û���Ŀ¼ **/

	private String getDirectory() {
		String dir = getSDPath() + "/" + cachdir;
//		String substr = dir.substring(0, 4);
//		if (substr.equals("/mnt")) {
//			dir = dir.replace("/mnt", "");
//		}
		if(!new File(dir).exists()){
			new File(dir).mkdirs();
		}
		return dir;
	}

	/**** ȡSD��·������/ ****/

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); 
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}
}
