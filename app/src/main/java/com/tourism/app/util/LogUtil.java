package com.tourism.app.util;

import android.util.Log;

import com.tourism.app.BuildConfig;

/**
 * 
 * @author jzy
 *
 */
public class LogUtil {

	private final static String LOG_TAG_STRING = "ETS_Android";

	public static void d(String tag, String msg) {
		try {
//			if (BuildConfig.DEBUG) {
				Log.d(LOG_TAG_STRING, tag + " : " + msg);
//			}
		} catch (Throwable t) {
		}
	}

	public static void i(String tag, String msg) {
		try {
			if (BuildConfig.DEBUG) {
				Log.i(LOG_TAG_STRING, tag + " : " + msg);
			}
		} catch (Throwable t) {
		}
	}

	public static void e(String tag, String msg) {
		try {
			if (BuildConfig.DEBUG) {
				Log.e(LOG_TAG_STRING, tag + " : " + msg);
			}
		} catch (Throwable t) {
		}
	}
	
	public static void w(String tag, String msg) {
		try {
			if (BuildConfig.DEBUG) {
				Log.w(LOG_TAG_STRING, tag + " : " + msg);
			}
		} catch (Throwable t) {
		}
	}
	
	public static void e(Exception e){
	    try {
//            if (BuildConfig.DEBUG) {
                e.printStackTrace();
//            }
        } catch (Throwable t) {
        }
	}
}
