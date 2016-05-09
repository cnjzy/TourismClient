package com.tourism.app.base;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tourism.app.common.Constants;
import com.tourism.app.net.AsyncHttpGet;
import com.tourism.app.net.AsyncHttpPost;
import com.tourism.app.net.BaseRequest;
import com.tourism.app.net.DefaultThreadPool;
import com.tourism.app.net.ThreadCallBack;
import com.tourism.app.net.utils.CheckNetWorkUtil;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.util.DeviceUtil;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.preference.Preferences;
import com.tourism.app.util.preference.PreferencesUtils;
import com.tourism.app.widget.dialog.CustomLoadingDialog;

public abstract class BaseFragment extends Fragment implements ThreadCallBack, View.OnClickListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 11111L;

    protected final String TAG = this.getClass().getSimpleName();

    /**
     * 当前activity所持有的所有请求
     */
    List<BaseRequest> requestList = new ArrayList<BaseRequest>();

    /**
     * 主线程Handler
     */
    protected Handler handler = new Handler();

    /**
     * 加载提示框
     */
    public CustomLoadingDialog loadingDialog;

    /**
     * Activity 前缀
     */
    public final String FIRST_ACTION = "com.etaoshi.app.";

    /**
     * 配置文件信息
     */
    public static PreferencesUtils preferencesUtils;

    /**
     * 是否初始化
     */
    protected boolean isInit = true;

    /**
     * 访问网络成功Tag
     */
    protected final int SUCCESS = 1;

    /**
     * 上下文
     */
    protected Context context;

    /**
     * 延迟请求时间
     */
    protected long postDelayed = 300;

    /**
     * 根View
     */
    protected View rootView;

    /**
     * 私有化当前对象默认构造
     */
    protected BaseFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
    	this.context = activity;
    	super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        preferencesUtils = new PreferencesUtils(getActivity(), Preferences.CONFIG_FILE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        cancelRequest();
        super.onDestroy();
    }

    public void cancelRequest() {
        if (requestList != null && requestList.size() > 0) {
            for (BaseRequest request : requestList) {
                if (request.getRequest() != null) {
                    try {
                        request.getRequest().abort();
                        requestList.remove(request.getRequest());
                    } catch (UnsupportedOperationException e) {
                        // do nothing .
                    }
                }
            }
        }
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
            boolean isShowLoadingDialog, String loadingDialogContent) {
        startHttpRequst(requestType, url, parameter, isShowLoadingDialog, loadingDialogContent, true,
                Constants.CONNECTION_SHORT_TIMEOUT, Constants.READ_SHORT_TIMEOUT);
    }

    protected void startHttpRequst(String requestType, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, String loadingDialogContent, boolean isHideCloseBtn, int connectTimeout,
            int readTimeout) {
        startHttpRequst(requestType, url, parameter, isShowLoadingDialog, loadingDialogContent, isHideCloseBtn,
                connectTimeout, readTimeout, -1);
    }

    public void startHttpRequst(String requestType, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, String loadingDialogContent, boolean isHideCloseBtn, int connectTimeout,
            int readTimeout, int resultCode) {

        if (!CheckNetWorkUtil.checkNetWork(getActivity())) {
            // return;
        }

        if (null != parameter) {
//            parameter.add(new RequestParameter("channel", "android"));
            // parameter.add(new RequestParameter("imei", Constants.IMEI));
            // parameter.add(new RequestParameter("ver", DeviceUtil.getVersionName(context)));
        }
        for (int i = 0; i < parameter.size(); i++) {
            RequestParameter requestParameter = parameter.get(i);
            LogUtil.d("requestParameter", requestParameter.getName() + "=" + requestParameter.getValue());
        }
        BaseRequest httpRequest = null;
        if ("POST".equalsIgnoreCase(requestType)) {
            httpRequest = new AsyncHttpPost(this, url, parameter, isShowLoadingDialog, loadingDialogContent,
                    isHideCloseBtn, connectTimeout, readTimeout, resultCode);
        } else {
            httpRequest = new AsyncHttpGet(this, url, parameter, isShowLoadingDialog, loadingDialogContent,
                    isHideCloseBtn, connectTimeout, readTimeout, resultCode, context);
        }
        DefaultThreadPool.getInstance().execute(httpRequest);
        this.requestList.add(httpRequest);
    }

    public void showToast(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }

    protected List<RequestParameter> getBaseRequestParams() {
        ArrayList<RequestParameter> params = new ArrayList<RequestParameter>();
        params.add(new RequestParameter("version_code", DeviceUtil.getVersionName(context)));
        params.add(new RequestParameter("platform", "Android"));
        // params.add(new RequestParameter("channel", DeviceUtil.getChannelName(context, "test")));
        // params.add(new RequestParameter("channel", "test"));
        params.add(new RequestParameter("deviceId", DeviceUtil.getImei(context)));
        return params;
    }

    /**
     * 初始化方法
     */
    public abstract void init();

    public abstract View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public abstract void initView();

    public abstract void initListener();

    public abstract void initValue();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = initLayout(inflater, container, savedInstanceState);
            if (rootView.getParent() != null) {
                ((ViewGroup) rootView.getParent()).removeAllViewsInLayout();
            }
            init();
            initView();
            initListener();
            initValue();
        }
        return rootView;
    }

    public void onCallbackFromThread(String resultJson) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
    
    @Override
    public void onCallbackFromThread(InputStream is, int resultCode) {
    	
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 弹出输入窗口
     * 
     * @param view
     */
    protected void showIMM(final View view) {
        // 弹出键盘
        handler.postDelayed(new Runnable() {
            public void run() {
                DeviceUtil.showIMM(context, view);
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
    	// 关闭键盘
        if (DeviceUtil.getIMMStatus(getActivity()) && getActivity().getCurrentFocus() != null) {
            DeviceUtil.hideIMM(context, getActivity().getCurrentFocus());
        }
    }
    
    /**
     * 
     * @param rsid
     * @return
     */
    protected <T extends View> T findViewById(int rsid){
    	if(rootView == null){
    		return null;
    	}else{
    		return (T) rootView.findViewById(rsid);
    	}
    }
}
