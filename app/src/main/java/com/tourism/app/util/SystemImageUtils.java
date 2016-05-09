/** 
 * Project Name:TourismAppClient 
 * File Name:SystemImageUtils.java 
 * Package Name:com.tourism.app.util 
 * Date:2016年4月24日下午1:04:20 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tourism.app.util;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * ClassName:SystemImageUtils Date: 2016年4月24日 下午1:04:20
 * 获取系统图片工具类
 * @author Jzy
 * @version
 * @see
 */
public class SystemImageUtils {

	private static SystemImageUtils share;

	private List<ImageFolder> imageFolderList = new ArrayList<ImageFolder>();
	private List<ImageFolder> imageList = new ArrayList<ImageFolder>();

	public static synchronized SystemImageUtils getShare() {
		if (share == null) {
			share = new SystemImageUtils();
		}
		return share;
	}

	/**
	 * 获取图片文件夹列表
	 * 
	 * @param act
	 * @return
	 */
	public List<ImageFolder> getImageFolderList(Activity act) {
		if (imageFolderList == null || imageFolderList.size() == 0) {
			getAllFolder(act);
		}
		return imageFolderList;
	}
	
	/**
	 * 根据文件夹获取所有图片列表
	 * @param act
	 * @param imageFolder
	 * @return
	 */
	public List<ImageFolder> getImageList(Activity act, ImageFolder imageFolder) throws RuntimeException{
		if(imageFolder == null || TextUtils.isEmpty(imageFolder.getName())){
			Toast.makeText(act, "图片文件夹为空，获取图片列表失败", Toast.LENGTH_SHORT).show();
			throw new RuntimeException("get image list faild, ImageFolder or name is empty");
		}else{
			getAllImageByFolder(act, imageFolder);
		}
		return imageList;
	}

	/**
	 * 获取图片文件夹列表
	 * 
	 * @param act
	 */
	private void getAllFolder(Activity act) {
		String[] projection = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
				MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
				MediaStore.Images.Media.DATA, // 图片绝对路径
				"count(" + MediaStore.Images.Media._ID + ")"// 统计当前文件夹下共有多少张图片
		};
		String selection = " 0==0) group by " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " --(";

		ContentResolver cr = act.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "");

		if (null != cursor) {
			while (cursor.moveToNext()) {
				ImageFolder ifoler = new ImageFolder();
				String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
				String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
				long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
				String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
				String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				int count = cursor.getInt(5);// 该文件夹下一共有多少张图片
				BitmapFactory.Options options = new BitmapFactory.Options();
				// Bitmap bitmap = Thumbnails.getThumbnail(cr, fileId,
				// Thumbnails.MICRO_KIND, options);// 获取指定图片缩略片

				ifoler.setFolderId(folderId);
				ifoler.setName(folder);
				ifoler.setFinaName(finaName);
				ifoler.setPath(path);
				ifoler.setCount(count);
				// ifoler.setBmp(bitmap);
				imageFolderList.add(ifoler);
			}
			if (!cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	private void getAllImageByFolder(Activity act, ImageFolder imageFolder) {
		String[] projection = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
				MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
				MediaStore.Images.Media.DATA, // 图片绝对路径
		// "count(" + MediaStore.Images.Media._ID + ")"//
		// 统计当前文件夹下共有多少张图片
		};
		String selection = " " + MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = '" + imageFolder.getName() + "' ) --(";

		ContentResolver cr = act.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, null, "");

		if (null != cursor) {
			while (cursor.moveToNext()) {
				ImageFolder ifoler = new ImageFolder();
				String folderId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
				String folder = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
				long fileId = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
				String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
				String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				BitmapFactory.Options options = new BitmapFactory.Options();
				// Bitmap bitmap = Thumbnails.getThumbnail(cr, fileId,
				// Thumbnails.MICRO_KIND, options);// 获取指定图片缩略片

				ifoler.setFolderId(folderId);
				ifoler.setFileId(fileId);
				ifoler.setName(folder);
				ifoler.setFinaName(finaName);
				ifoler.setPath(path);
				// ifoler.setBmp(bitmap);
				imageList.add(ifoler);
			}
			if (!cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	class ImageFolder {
		private String folderId;
		private long fileId;
		private String name;
		private int index;
		private String finaName;
		private String path;
		private int count;
		private SoftReference<Bitmap> bmp;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}

		public Bitmap getBmp(Activity act) {
			if (bmp == null) {
				ContentResolver cr = act.getContentResolver();
				BitmapFactory.Options options = new BitmapFactory.Options();
				Bitmap bitmap = Thumbnails.getThumbnail(cr, fileId, Thumbnails.MICRO_KIND, options);// 获取指定图片缩略片
				bmp = new SoftReference<Bitmap>(bitmap);
			}

			return bmp.get();
		}

		public void setBmp(Bitmap bmp) {
			this.bmp = new SoftReference<Bitmap>(bmp);
		}

		/**
		 * @return the folderId
		 */
		public String getFolderId() {
			return folderId;
		}

		/**
		 * @param folderId
		 *            the folderId to set
		 */
		public void setFolderId(String folderId) {
			this.folderId = folderId;
		}

		/**
		 * @return the fileId
		 */
		public long getFileId() {
			return fileId;
		}

		/**
		 * @param fileId
		 *            the fileId to set
		 */
		public void setFileId(long fileId) {
			this.fileId = fileId;
		}

		/**
		 * @return the finaName
		 */
		public String getFinaName() {
			return finaName;
		}

		/**
		 * @param finaName
		 *            the finaName to set
		 */
		public void setFinaName(String finaName) {
			this.finaName = finaName;
		}

		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * @param path
		 *            the path to set
		 */
		public void setPath(String path) {
			this.path = path;
		}

		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}

		/**
		 * @param count
		 *            the count to set
		 */
		public void setCount(int count) {
			this.count = count;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ImageFolder [folderId=" + folderId + ", fileId=" + fileId + ", name=" + name + ", index=" + index + ", finaName=" + finaName + ", path=" + path + ", count="
					+ count + ", bmp=" + bmp + "]";
		}

	}
}
