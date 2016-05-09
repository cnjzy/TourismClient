package com.tourism.app.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;

public class Utils {
	
	/**
	 * 获取当前语言环境
	 * 简体：zh_CN			country:CN		language:zh
	 * 繁体：zh_TW			country:TW		language:zh
	 * 英文：en				country:XX		language:en
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getLocaleLanguage(Context ctx){
		String country= ctx.getResources().getConfiguration().locale.getCountry();
		String language= ctx.getResources().getConfiguration().locale.getLanguage();
		
		if("zh".equals(language))
			return language+"_"+country;
		else
			return language;
	}

//	/**
//	 * Drawable → Bitmap 转换
//	 * @param drawable
//	 * @return
//	 */
//	public static Bitmap drawableToBitmap(Drawable drawable) {
//		Bitmap bitmap = Bitmap
//				.createBitmap(
//						drawable.getIntrinsicWidth(),
//						drawable.getIntrinsicHeight(),
//						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//								: Bitmap.Config.RGB_565);
//		Canvas canvas = new Canvas(bitmap);
//		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable
//				.getIntrinsicHeight());
//		drawable.draw(canvas);
//		return bitmap;
//	}  

	/**
	 * Bitmap → byte[] 转换
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}  

	/**
	 * byte[] → Bitmap 转换
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}  

	
	
	/**
	 * 取subString，适用于中英文
	 * @param str
	 * @param length
	 * @return
	 */
//	public static String subStringBybyte(String str ,String endStr, int length){
//		if(str!=null){
//			if(str.getBytes().length>length){
//				int len = 0;
//				int len2 = 0;
//				String tempStr1 = "";
//				StringBuffer result = new StringBuffer("");
//				for (int i = 0; i < length; i++) {
//					tempStr1 = str.substring(i, i + 1);
//					try {
//						len2 = tempStr1.getBytes("UTF-8").length;
//					} catch (UnsupportedEncodingException e) {
//					}
//					len += len2;
//					if (len > length)
//						break;
//					else
//						result.append(tempStr1);
//				}
//				
//				return (endStr!=null&&!"".equals(endStr))?(result.toString()+endStr):result.toString();
//			}else{
//				return str;
//			}
//		}else
//		{
//			return null;
//		}
//	}
	
	/**
     * 滤除 HTML 标记
     * @param s
     * @return
     */
    public static String setTag(String s)
    {
    	if(s!=null&&!"".equals(s)){
	        int j = s.length();
	        StringBuffer stringbuffer = new StringBuffer(j + 500);
	        for(int i = 0; i < j; i++)
	        {
	            if(s.charAt(i) == '<')
	            {
	                stringbuffer.append("&lt;");
	                continue;
	            }
	            if(s.charAt(i) == '>')
	            {
	                stringbuffer.append("&gt;");
	                continue;
	            }
	            if(s.charAt(i)=='"'){
	            	stringbuffer.append("&quot;");
	                continue;
	            }
	            if(s.charAt(i)=='\''){
	            	stringbuffer.append("&apos;");
	            	continue;
	            }
	            if(s.charAt(i) == '&')
	                stringbuffer.append("&amp;");
	            else
	                stringbuffer.append(s.charAt(i));
	        }
	
	        return stringbuffer.toString();
        }else
        	return "";
    }
    
    /**
     * 从assets文件夹中获取图片资源
     * @param context
     * @param fileName
     * @return
     */
    public static Bitmap getImageFromAssetFile(Context context,String fileName){  
        Bitmap image = null;  
        try{  
            AssetManager am = context.getAssets();  
            InputStream is = am.open(fileName);  
            image = BitmapFactory.decodeStream(is);  
            is.close();  
        }catch(Exception e){  
              
        }  
        return image;  
    }  
    
    public static byte[] hexToBytes(String hexString) {
		char[] hex = hexString.toCharArray();
		// 转rawData长度减半
		int length = hex.length / 2;
		byte[] rawData = new byte[length];
		for (int i = 0; i < length; i++) {
			// 先将hex转10进位数值
			int high = Character.digit(hex[i * 2], 16);
			int low = Character.digit(hex[i * 2 + 1], 16);
			// 將第一個值的二進位值左平移4位,ex: 00001000 => 10000000 (8=>128)
			// 然后与第二个值的二进位值作联集ex: 10000000 | 00001100 => 10001100 (137)
			int value = (high << 4) | low;
			// 与FFFFFFFF作补集
			if (value > 127) {
				value -= 256;
			}
			// 最后转回byte就OK
			rawData[i] = (byte) value;
		}
		return rawData;
	}
    
    /*** 半角转换为全角
    *
    * @param input   
    * @return     
    */     
    public static String ToDBC(String input) {         
             char[] c = input.toCharArray();
             for (int i = 0; i < c.length; i++) {             
             if (c[i] == 12288) {                
             c[i] = (char) 32;                 
             continue;
              }
              if (c[i] > 65280 && c[i] < 65375)
                 c[i] = (char) (c[i] - 65248);
             }
             return new String(c);
         }  

    
    
    /**
     * 验证手机号的格式
     * @param mobile
     * @return 是否为手机号
     */
    public static boolean verifyMobileNumber(String mobile){
    	if(TextUtils.isEmpty(mobile)){
    		return false;
    	}else{
    		Pattern pattern = Pattern.compile("^1[3|5|7|8]\\d{9}$");
    		Matcher matcher = pattern.matcher(mobile);
    		if(matcher.find()){
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 是否是正确的邮件或者手机号格式
     * @param contect
     * @return
     */
    public static boolean verifyEmailOrMobile(String contect){
        if(verifyMobileNumber(contect) || verifyEmailFormat(contect)){
            return true;
        }else{
            return false;
        }
    }
    
    /**
     * 验证邮箱地址的格式
     * @param email
     * @return 是否有有效邮箱地址
     */
    public static boolean verifyEmailFormat(String email){
    	if(TextUtils.isEmpty(email)){
    		return false;
    	}else{
    		Pattern pattern = Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
    		Matcher matcher = pattern.matcher(email);
    		if(matcher.find()){
    			return true;
    		}
    	}
    	return false;
    }

    /**
     * 验证密码的格式
     * @param password
     * @return 是否为6～24位字符（字母、数字、特殊符号）
     */
    public static boolean verifyNewPasswordFormat(String password){
        if(TextUtils.isEmpty(password)){
            return false;
        }else{
            Pattern pattern = Pattern.compile("^.{6,24}$");
            Matcher matcher = pattern.matcher(password);
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }   
    

    /**
     * 验证用户名的格式
     * @param userName
     * @return 是否为字母、汉字开头的2~12个字符
     */
    public static boolean verifyUserNameFormat(String userName){
        if(TextUtils.isEmpty(userName)){
            return false;
        }else{
            Pattern pattern = Pattern.compile("^[a-zA-Z\u4E00-\u9FA5][0-9a-zA-Z\u4E00-\u9FA5]{1,11}$");
            Matcher matcher = pattern.matcher(userName);
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }   
    
    

    /**
     * 验证用户名的格式
     * @param userName
     * @return 是否为字母、汉字开头的1~100个字符
     */
    public static boolean verifyAddressUserNameFormat(String userName){
        if(TextUtils.isEmpty(userName)){
            return false;
        }else{
            Pattern pattern = Pattern.compile("^[a-zA-Z\u4E00-\u9FA5][0-9a-zA-Z\u4E00-\u9FA5]{0,99}$");
            Matcher matcher = pattern.matcher(userName);
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }  
    
    
    /**
     * 验证密码的格式
     * @param password
     * @return 密码在1～100个字符之间（包含1和100）
     */
    public static boolean verifyLoginPasswordFormat(String password){
        if(TextUtils.isEmpty(password)){
            return false;
        }else{
            Pattern pattern = Pattern.compile("^.{1,100}$");
            Matcher matcher = pattern.matcher(password);
            if(matcher.find()){
                return true;
            }
        }
        return false;
    }
    
    /**
     * 日期格式化
     * @param dateString /Date(时间戳)/格式的字符串
     * @return
     */
    public static String getFormatDate(String dateString) {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Pattern pattern = Pattern.compile("\\((\\d*)");
        Matcher matcher = pattern.matcher(dateString);
        if (matcher.find()) {
            String value = matcher.group(1);
            try {
                long longValue = Long.parseLong(value);
                return mDateFormat.format(longValue);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return dateString;
    }
    
    /**
     * Calculate the bounds for a view's title
     *
     * @param index
     * @param paint
     * @return
     */
    public static Rect calcBounds(String text, Paint paint) {
         // Get the title
         // Calculate the text bounds
        Rect bounds = new Rect();
        bounds. right = ( int) paint.measureText(text);
        bounds. bottom = ( int) (paint.descent()-paint.ascent());
         return bounds;
    }

    
}
