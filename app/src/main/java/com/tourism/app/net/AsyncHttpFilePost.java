package com.tourism.app.net;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.tourism.app.base.BaseFragment;
import com.tourism.app.common.Constants;
import com.tourism.app.net.exception.RequestException;
import com.tourism.app.net.utils.ErrorUtil;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.util.HttpUploadUtil;
import com.tourism.app.util.HttpUploadUtil.OnUploadProgressLinstener;
import com.tourism.app.util.LogUtil;
import com.tourism.app.widget.dialog.CustomLoadingDialog;

public final class AsyncHttpFilePost extends BaseRequest {
	DefaultHttpClient httpClient;
    List<RequestParameter> parameter = null;
    CustomLoadingDialog customLoadingDialog;
    ThreadCallBack callBack;
	
	private int resultCode = -1;//大于20000为流
	Handler resultHandler = new Handler() {
        public void handleMessage(Message msg) {
            ThreadCallBack callBack = (ThreadCallBack) msg.getData().getSerializable("callback");
            if (callBack != null) {
                if (resultCode == -1){
                	String resultData = (String) msg.obj;
                    callBack.onCallbackFromThread(resultData);
                }else if(resultCode>20000){
                    if(msg.obj instanceof InputStream)
                        callBack.onCallbackFromThread((InputStream) msg.obj,resultCode);
                    else
                        callBack.onCallbackFromThread(msg.obj.toString(), resultCode);
                }else{
	                String resultData = (String) msg.obj;
	                callBack.onCallbackFromThread(resultData, resultCode);
                }
            }
        }
    };
    
    public AsyncHttpFilePost(ThreadCallBack callBack, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, String loadingCode, boolean isHideCloseBtn, int resultCode) {
        this.callBack = callBack;
        this.resultCode = resultCode;
        if (isShowLoadingDialog) {
            if (callBack != null && callBack instanceof Context) {
                customLoadingDialog = new CustomLoadingDialog((Context) callBack, loadingCode, isHideCloseBtn);
                if (customLoadingDialog != null && !customLoadingDialog.isShowing()) {
                    customLoadingDialog.show();
                }
            } else if (callBack != null && callBack instanceof Fragment) {
                Context tempContext = ((Fragment) callBack).getActivity();
                if (tempContext != null) {
                    customLoadingDialog = new CustomLoadingDialog(tempContext, loadingCode, isHideCloseBtn);
                    if (customLoadingDialog != null && !customLoadingDialog.isShowing()) {
                        customLoadingDialog.show();
                    }
                }

            }
        }
        this.url = url;
        this.parameter = parameter;
        if (httpClient == null)
            httpClient = new DefaultHttpClient();
    }
    
    public AsyncHttpFilePost(ThreadCallBack callBack, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, int connectTimeout, int readTimeout) {
        this(callBack, url, parameter, isShowLoadingDialog, "", false, -1);
        if (connectTimeout > 0) {
            this.connectTimeout = connectTimeout;
        }
        if (readTimeout > 0) {
            this.readTimeout = readTimeout;
        }
    }

    public AsyncHttpFilePost(ThreadCallBack callBack, String url, List<RequestParameter> parameter,
            boolean isShowLoadingDialog, String loadingDialogContent, boolean isHideCloseBtn, int connectTimeout,
            int readTimeout, int resultCode) {
        this(callBack, url, parameter, isShowLoadingDialog, loadingDialogContent, isHideCloseBtn, resultCode);
        if (connectTimeout > 0) {
            this.connectTimeout = connectTimeout;
        }
        if (readTimeout > 0) {
            this.readTimeout = readTimeout;
        }
    }
    
	@Override
	public void run() {
		String ret="";
		HttpUploadUtil u=null;
		try {
			u = new HttpUploadUtil(url);
			u.setOnUploadProgressLinstener(new OnUploadProgressLinstener() {
                public void onUploadProgress(long fileLength, long curLength, String fileName, String filePath) {
                    Intent intent = new Intent();
                    intent.setAction(FILE_UPLOAD_PROGRESS_FILTER);
                    Bundle data = new Bundle();
                    data.putLong("fileLength", fileLength);
                    data.putLong("curLength", curLength);
                    data.putString("fileName", fileName);
                    data.putString("filePath", filePath);
                    intent.putExtras(data);
                    Context context = null;
                    if(callBack instanceof Context){
                        context = (Context) callBack;
                    }else if(callBack instanceof BaseFragment){
                        context = ((BaseFragment) callBack).getActivity();
                    }
                    
                    if(context != null){
                        System.out.println("send broad case");
                        context.sendBroadcast(intent);
                    }
                }
            });
			 if (parameter != null && parameter.size() > 0) {
                for (RequestParameter p : parameter) {
                	if(p.isFile()){
                		u.addFileParameter(p.getName(), new File(p.getValue()));
                	}else{
                		u.addTextParameter(p.getName(), p.getValue());
                	}
                	LogUtil.d("requestParameter：", p.getName()+"(file="+p.isFile()+"):"+ p.getValue());
                }
            }
			LogUtil.d(this.getClass().getName(), "request to url :"+url);
	        byte[] b = u.send();
	        ret = new String(b);
		}catch (Exception e) {
            RequestException exception = new RequestException(RequestException.IO_EXCEPTION, Constants.ERROR_MESSAGE);
            ret = ErrorUtil.errorJson("false", exception.getMessage());
            LogUtil.d(AsyncHttpGet.class.getName(),"AsyncHttpFilePost  request to url :" + url + "  onFail  " + e.getMessage());
        } finally {
            if (customLoadingDialog != null && customLoadingDialog.isShowing()) {
                customLoadingDialog.dismiss();
                customLoadingDialog = null;
            }

            try {
                Message msg = new Message();
                msg.obj = ret;
                LogUtil.d("返回结果", ret);
                msg.getData().putSerializable("callback", callBack);
                resultHandler.sendMessage(msg);
            } catch (Exception e) {
                LogUtil.e(e);
            } finally {
                if (customLoadingDialog != null && customLoadingDialog.isShowing()) {
                    customLoadingDialog.dismiss();
                    customLoadingDialog = null;
                }
            }
        }
		super.run();
	}

}
