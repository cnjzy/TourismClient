package com.tourism.app.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tourism.app.MyApp;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.preference.Preferences;

/**
 * 【http请求类】
 * 
 * @ClassName:HttpManager
 * @Description:TODO
 * @author sailor
 * @date 2012-3-8上午10:12:26
 * 
 */
public final class HttpManager {
    /**
     * 【将流转换成字符串】
     * 
     * @Title :convertStreamToString
     * @Description :TODO
     * @params @param is
     * @params @return
     * @return String
     * @throws
     */
    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            LogUtil.e(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LogUtil.e(e);
            }
        }
        return sb.toString();
    }

    /**
     * 检查网络状态
     * 
     * @author huangyu
     * 
     */
    public static boolean checkNetWork(Context context) {
        // 判断网络是否可用，如果不可用，给出提示
        boolean isAvailable = netWorkIsAvailable(context);
        if (!isAvailable) {// 如果不可用
            openDialog(context);
            return false;
        }
        return true;
    }

    /**
     * 判断网络是否可用
     * 
     * @Title :netWorkIsAvailable
     * @Description :TODO
     * @params @param context
     * @params @return
     * @return boolean
     * @throws
     * 
     */
    public static boolean netWorkIsAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            if (activeNetInfo.isAvailable()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 弹出网络不可用提示框
     * 
     * @Title :openDialog
     * @Description :TODO
     * @params @param context
     * @return void
     * @throws
     * 
     */
    private static void openDialog(final Context context) {
        final Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("没有可用的网络");
        builder.setMessage("请开启GPRS或WIFI网络连接");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Intent mIntent = new Intent("/");
                ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                mIntent.setComponent(comp);
                mIntent.setAction("android.intent.action.VIEW");
                context.startActivity(mIntent);

            }
        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        }).create().show();

    }

    /**
     * 保存Cookie
     * 
     * @param resp
     */
    public static void saveCookies(HttpResponse httpResponse) {
        Header[] headers = httpResponse.getHeaders("Set-Cookie");
        StringBuilder sb = new StringBuilder();
        if (headers == null || headers.length == 0)
            return;
        for (int i = 0; i < headers.length; i++) {
            String cookie = headers[i].getValue();
            sb.append(cookie).append(";");
            // String[] cookievalues = cookie.split(";");
            // // String temp=cookie.replace(" Path=/", "");
            // // sb.append(temp);
            // for (int j = 0; j < cookievalues.length; j++) {
            // /*String[] keyPair = cookievalues[j].split("=");
            // String key = keyPair[0].trim();
            // String value = keyPair.length > 1 ? keyPair[1].trim() : "";*/
            //
            // String[] keyPair = cookievalues[j].split("=");
            // // String[] keyPair = cookievalues[0].split("=");
            // String resole=cookievalues[j].replace("\"", "");
            // // String key = keyPair[0].trim();
            // // String value = keyPair.length > 1 ? keyPair[1].trim() : "";
            // String key=resole.substring(0,resole.indexOf("="));
            // String value=resole.substring(resole.indexOf("=")+1);
            //
            // sb.append(key);
            // sb.append("=");
            // sb.append(value);
            // sb.append(";");
            // }
        }
        MyApp.preferencesUtils.putString(Preferences.COOKIE, sb.toString());
    }

    /**
     * 增加Cookie
     * 
     * @param request
     */
    public static void addCookies(HttpUriRequest request) {
        request.addHeader(Preferences.COOKIE, MyApp.preferencesUtils.getString(Preferences.COOKIE, ""));
        request.addHeader("user-agent", "123");
        LogUtil.d("cookie=", "[" + MyApp.preferencesUtils.getString(Preferences.COOKIE, "") + "]");
    }

    /**
     * 增加Cookie
     * 
     * @param request
     */
    public static void addCookies(HttpURLConnection conn) {
        conn.addRequestProperty(Preferences.COOKIE, MyApp.preferencesUtils.getString(Preferences.COOKIE, ""));
        conn.addRequestProperty("user-agent", "123");
        LogUtil.d("cookie=", "[" + MyApp.preferencesUtils.getString(Preferences.COOKIE, "") + "]");
    }
}
