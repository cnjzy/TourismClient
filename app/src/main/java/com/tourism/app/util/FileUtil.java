package com.tourism.app.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileUtil {

	/**
	 * 获取文件夹大小
	 * 
	 * @param file
	 *            File实例
	 * @return long 单位为M
	 * @throws Exception
	 */
	public static long getFolderSize(java.io.File file) throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size / 1048576;
	}

	public static File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(context)
				.getPath() : context.getCacheDir().getPath();

		File file = new File(cachePath + File.separator + uniqueName);
		if (!file.exists())
			file.mkdirs();
		return file;
	}

	public static File getExternalCacheDir(Context context) {
		final String cacheDir = "/Android/data/" + context.getPackageName()
				+ "/cache/";
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath() + cacheDir);
		if (!file.exists())
			file.mkdirs();
		return file;
	}

	public static final String TEMP_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "yuehui"
			+ File.separator + "TEMP" + File.separator;

	/**
	 * 删除文件夹中的文件
	 */
	public static void delectDirs(String path) {
		File file = new File(path);
		if (!file.isDirectory())
			file.delete();
		else {
			File[] fileSet = file.listFiles();
			for (File delFile : fileSet)
				delectDirs(delFile.getPath());
		}
	}


	public static String getFileNameFromUrl(String url) {
		int start = url.lastIndexOf("/");
		if (start != -1) {
			return url.substring(start + 1, url.length());
		}
		return null;
	}

}
