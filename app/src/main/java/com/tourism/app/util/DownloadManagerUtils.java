package com.tourism.app.util;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

public class DownloadManagerUtils {
	private static long reference;
	
	public static long getReference(){
		return reference;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void downloadApk(Context context, String url, String title, String content) {
		if (url == null)
			return;
		DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		request.setTitle(title);
		request.setDescription(content);
		request.setMimeType("application/vnd.android.package-archive");
		request.setDestinationInExternalPublicDir("down", "ggtv.apk");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		reference = downloadManager.enqueue(request);
	}
}
