package com.tourism.app.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tourism.app.MyApp;
import com.tourism.app.R;
import com.tourism.app.common.Constants;
import com.tourism.app.net.AsyncHttpFilePost;
import com.tourism.app.net.AsyncHttpGet;
import com.tourism.app.net.AsyncHttpPost;
import com.tourism.app.net.BaseRequest;
import com.tourism.app.net.DefaultThreadPool;
import com.tourism.app.net.ThreadCallBack;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.util.DeviceUtil;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.StringUtil;
import com.tourism.app.util.preference.Preferences;
import com.tourism.app.util.preference.PreferencesUtils;
import com.tourism.app.vo.UserInfoVO;
import com.tourism.app.widget.dialog.CustomLoadingDialog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author jzy
 * 
 */
public abstract class BaseActivity extends FragmentActivity implements ThreadCallBack, View.OnClickListener {

    /**
     * 增加几种返回方式
     */
    public static final int REQUEST_LOCATION_UPDATE_CODE = 10001;

    /**
     * 
     */
    private static final long serialVersionUID = 1111L;

    /**
     * 
     */
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 上下文
     */
    protected BaseActivity context = this;

    /**
     * 当前activity所持有的所有请求
     */
    protected List<BaseRequest> requestList = null;

    /**
     * Activity集合
     */
    public static List<Activity> activityList = new ArrayList<Activity>();

    /**
     * 主线程Handler
     */
    protected Handler mHandler = new Handler() {

    };

    /**
     * 加载提示框
     */
    public CustomLoadingDialog loadingDialog;

    /**
     * 配置文件信息
     */
    public PreferencesUtils preferencesUtils;

    /**
     * 访问成功tag
     */
    public final int SUCCESS = 1;

    /**
     * 当前App对象
     */
    public MyApp mApp;
    
    /**
     * 网络弹出框
     */
    protected Dialog notNetworkDialog;

    /**
     * 图片加载参数选项
     */
    public DisplayImageOptions options;
    
    public UserInfoVO getUserInfo() {
        return mApp.getUserInfo();
    }

    public void setUserInfo(UserInfoVO userInfo) {
        mApp.setUserInfo(userInfo);
    }

    public boolean isUserEmpty(){
        if (getUserInfo() == null || TextUtils.isEmpty(getUserInfo().getToken())){
            return true;
        }else{
            return false;
        }
    }

    // 图片第一次加载动画
    protected ImageLoadingListener animateFirstListener = null;
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    
    protected void onCreate(Bundle savedInstanceState) {
        requestList = new ArrayList<BaseRequest>();
        super.onCreate(savedInstanceState);

        preferencesUtils = new PreferencesUtils(context, Preferences.CONFIG_FILE);

        mApp = (MyApp) getApplication();
        if (!activityList.contains(this)) {
            activityList.add(this);
        }
        
        options = mApp.getDefaultImageLoaderOptions();
        animateFirstListener = mApp.getAnimateFirstListener();
        
        initLayout();
        init();
        initView();
        initListener();
        initValue();
    }


    
    protected void onStart() {
        super.onStart();
    }

    
    protected void onStop() {
        super.onStop();

    }

    
    public void onResume() {
        super.onResume();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onPause()
     */
    
    protected void onPause() {
        // 关闭键盘
        if (DeviceUtil.getIMMStatus(context) && getCurrentFocus() != null) {
            DeviceUtil.hideIMM(context, getCurrentFocus());
        }
        super.onPause();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy()
     */
    
    protected void onDestroy() {
        /**
         * 在activity销毁的时候同时设置停止请求，停止线程请求回调
         */
        cancelRequest();
        /**
         * 清除当前对象
         */
        if (activityList.contains(this)) {
            activityList.remove(this);
        }
        /**
         * 取消无网络连接弹出框
         */
        if (notNetworkDialog != null && notNetworkDialog.isShowing()) {
            notNetworkDialog.dismiss();
        }
        notNetworkDialog = null;
        /**
         * 释放图片缓存
         */
        AnimateFirstDisplayListener.displayedImages.clear();

        super.onDestroy();
    }

    public void cancelRequest() {
        if (requestList != null && requestList.size() > 0) {
            for (BaseRequest request : requestList) {
                if (request.getRequest() != null) {
                    try {
                        request.getRequest().abort();
                        requestList.remove(request.getRequest());

                        // Log.d("netlib", "netlib ,onDestroy request to  "
                        // + request.getRequest().getURI()
                        // + "  is removed");
                    } catch (UnsupportedOperationException e) {
                        // do nothing .
                    }
                }
            }
        }
    }

    
    public void onCallbackFromThread(String resultJson) {
        // TODO Auto-generated method stub

    }

    protected void showToast(int resId) {
    	showToast(getString(resId));
    }
    protected void showToast(String message) {
        Toast toast = Toast.makeText(this, (!StringUtil.isEmpty(message)) ? message : "", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void startHttpRequest(String requestType, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, int resultCode) {
        startHttpRequst(requestType, url, parameter, isShowLoadingDialog, null, true,
                Constants.CONNECTION_SHORT_TIMEOUT, Constants.READ_SHORT_TIMEOUT, resultCode);
    }
    
    public void startHttpRequest(String requestType, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, String loadingDialogContent, int resultCode) {
        startHttpRequst(requestType, url, parameter, isShowLoadingDialog, loadingDialogContent, true,
                Constants.CONNECTION_SHORT_TIMEOUT, Constants.READ_SHORT_TIMEOUT, resultCode);
    }

    public void startHttpRequst(String requestType, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, String loadingDialogContent, boolean isHideCloseBtn, int resultCode) {
        startHttpRequst(requestType, url, parameter, isShowLoadingDialog, loadingDialogContent, isHideCloseBtn,
                Constants.CONNECTION_SHORT_TIMEOUT, Constants.READ_SHORT_TIMEOUT, resultCode);
    }

    public void startHttpRequst(String requestType, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, String loadingDialogContent, boolean isHideCloseBtn, int connectTimeout,
            int readTimeout, int resultCode) {

        if (!DeviceUtil.isConnectNet(context) && !context.isFinishing()) {
            if (notNetworkDialog == null) {
//                notNetworkDialog = DialogUtil.showSettingWIFIDialog(context);
            } else {
                if (!notNetworkDialog.isShowing()) {
                    if (notNetworkDialog.getContext() instanceof Activity) {
                        if (!((Activity) notNetworkDialog.getContext()).isFinishing())
                            notNetworkDialog.show();
                    } else {
                        notNetworkDialog.show();
                    }
                }
            }
            return;
        }

//        if (null != parameter) {
//            parameter.add(new RequestParameter("version_code", String.valueOf(DeviceUtil.getVersionCode(context))));
//            parameter.add(new RequestParameter("platform", Constants.PLATFORM));
//            parameter.add(new RequestParameter("channel", Constants.CHANNEL));
//            parameter.add(new RequestParameter("deviceId", DeviceUtil.getImei(context)));
//        }
        for (int i = 0; i < parameter.size(); i++) {
            RequestParameter requestParameter = parameter.get(i);
            LogUtil.d("requestParameter", requestParameter.getName() + "=" + requestParameter.getValue());
        }
        BaseRequest httpRequest = null;
        if ("POST".equalsIgnoreCase(requestType)) {
            httpRequest = new AsyncHttpPost(this, url, parameter, isShowLoadingDialog, loadingDialogContent,
                    isHideCloseBtn, connectTimeout, readTimeout, resultCode);
        } else if ("GET".equalsIgnoreCase(requestType)) {
            httpRequest = new AsyncHttpGet(this, url, parameter, isShowLoadingDialog, loadingDialogContent,
                    isHideCloseBtn, connectTimeout, readTimeout, resultCode, this);
        }else {
            httpRequest = new AsyncHttpFilePost(this, url, parameter, isShowLoadingDialog, loadingDialogContent,
                    isHideCloseBtn, connectTimeout, readTimeout, resultCode);
        }
        DefaultThreadPool.getInstance().execute(httpRequest);
        this.requestList.add(httpRequest);
    }

    public static void showActivity(Context context, Class<?> c) {
        showActivity(context, c, "");
    }

    public static void showActivity(Context context, Class<?> c, String info) {
        Bundle data = new Bundle();
        data.putString("info", info);
        showActivity(context, c, data);
    }

    public static void showActivity(Context context, Class<?> cls, Bundle data) {
        if (data == null)
            data = new Bundle();
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtras(data);
        context.startActivity(intent);
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
    }

    /**
     * 可以接受返回数据跳转Activity
     * 
     * @param context
     * @param c
     * @param requestCode
     * @param data
     */
    public static void showActivityForResult(Activity context, Class<?> c, int requestCode, Bundle data) {
        if (data == null)
            data = new Bundle();
        Intent intent = new Intent();
        intent.setClass(context, c);
        intent.putExtras(data);
        context.startActivityForResult(intent, requestCode);
        if (context instanceof Activity)
            ((Activity) context).overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
    }

    
    public void onCallbackFromThread(String resultJson, int resultCode) {
        try {
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    @Override
    public void onCallbackFromThread(InputStream is, int resultCode) {
        // TODO Auto-generated method stub
        
    }
    
    public void onClick(View v) {
        // 关闭键盘
        if (DeviceUtil.getIMMStatus(context) && getCurrentFocus() != null) {
            DeviceUtil.hideIMM(context, getCurrentFocus());
        }
        switch (v.getId()) {
        case R.id.navigation_left_btn:
            goBack();
            break;
        }
    }

    
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goBack() {
        // 退出页面
        finish();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#finish()
     */
    
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
    }

    /**
     * 弹出输入窗口
     * 
     * @param view
     */
    protected void showIMM(final View view) {
        // 弹出键盘
        mHandler.postDelayed(new Runnable() {
            public void run() {
                DeviceUtil.showIMM(context, view);
            }
        }, 500);
    }

    protected List<RequestParameter> getBaseRequestParams() {
        ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
        // params.add(new RequestParameter("version_code", String.valueOf(DeviceUtil.getVersionCode(context))));
        // params.add(new RequestParameter("platform", "Android"));
        // params.add(new RequestParameter("channel", DeviceUtil.getChannelName(context, "test")));
        // params.add(new RequestParameter("channel", "test"));
        // params.add(new RequestParameter("deviceId", DeviceUtil.getImei(context)));
        return params;
    }

    public abstract void initLayout();

    public abstract void init();

    public abstract void initView();

    public abstract void initListener();

    public abstract void initValue();

    public static void clearTask() {
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity act = activityList.get(i);
            activityList.remove(i);
            act.finish();
        }
    }
    
    /**
     * 设置导航标题
     * @param rid
     */
    public void setNavigationTitle(int rid){
    	setNavigationTitle(getString(rid));
    }
    public void setNavigationTitle(String text){
        TextView btn_title = (TextView) findViewById(R.id.navigation_title_tv);
        if(btn_title != null && !TextUtils.isEmpty(text)){
            btn_title.setText(text);
        }
    }
    
    /**
     * 设置右侧按钮
     * @param visibility
     * @param text
     * @param resBg
     */
    public void setNavigationRightButton(int visibility, int resId, int resBg){
    	setNavigationRightButton(visibility, resId > 0 ? getString(resId) : "", resBg);
    }
    public void setNavigationRightButton(int visibility, String text, int resBg){
    	ImageButton navigation_right_btn = (ImageButton) findViewById(R.id.navigation_right_btn);
    	if(navigation_right_btn != null){
    		navigation_right_btn.setVisibility(visibility);
    		if(visibility == View.VISIBLE){
    			navigation_right_btn.setOnClickListener(this);
	    		if(!TextUtils.isEmpty(text)){
//	    			navigation_right_btn.setText(text);
	    		}
	    		if(resBg > 0){
	    			navigation_right_btn.setImageResource(resBg);
	    		}
    		}
    	}
    }
}
