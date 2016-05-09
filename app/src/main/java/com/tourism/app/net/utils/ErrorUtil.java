package com.tourism.app.net.utils;

public class ErrorUtil {
	public static String errorJson(String resultCode,String message){
		return "{\"success\":\""+resultCode+"\",\"message\":\""+message+"\"}";
	}
}
