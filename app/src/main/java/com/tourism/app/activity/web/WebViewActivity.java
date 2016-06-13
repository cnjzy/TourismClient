package com.tourism.app.activity.web;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.widget.dialog.CustomLoadingDialog;


/**
 * @ClassName: WebActivity
 * @Description: TODO
 * @author jzy
 * @date 2014-12-15 下午5:06:55
 * 
 */
public class WebViewActivity extends BaseActivity{
	private static final String URL_KEY = "url";
	private static final String URL_TITLE = "title";
	
	private WebView webview;
	private View loading_layout;
	private String url;
	private String title;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_web);
	}

	@Override
	public void init() {
		url = getIntent().getStringExtra(URL_KEY);
		title =  getIntent().getStringExtra(URL_TITLE);
		loadingDialog = new CustomLoadingDialog(context, "", true);
	}

	@Override
	public void initView() {
		webview = (WebView) findViewById(R.id.webview);
		loading_layout =  findViewById(R.id.loading_layout);
	}

	@Override
	public void initListener() {
		
	}

	@Override
	public void initValue() {
		if(!TextUtils.isEmpty(title)){
			setNavigationTitle(title);
		}else{
			setNavigationTitle(R.string.loading_titile);
		}
	    
//	    webview.getSettings().setDisplayZoomControls(false);
		webview.getSettings().setJavaScriptEnabled(true);
//		webview.getSettings().setSavePassword(false);
//		webview.getSettings().setBlockNetworkImage(true);
//		webview.getSettings().setRenderPriority(RenderPriority.HIGH);
//		webview.getSettings().setBuiltInZoomControls(false);
//		webview.getSettings().setBlockNetworkImage(true);
//		webview.getSettings().setUseWideViewPort(true);
//		webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//		webview.getSettings().setBuiltInZoomControls(true); // 设置显示缩放按钮
//		webview.getSettings().setSupportZoom(true); // 支持缩放
//		webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLoadWithOverviewMode(true);

		webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webview.setVerticalScrollBarEnabled(false);
		webview.setVerticalScrollbarOverlay(false);
		webview.setHorizontalScrollBarEnabled(false);
		webview.setHorizontalScrollbarOverlay(false);

//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			webview.getSettings().setLayoutAlgorithm(
//					WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
//		} else {
//			webview.getSettings().setLayoutAlgorithm(
//					WebSettings.LayoutAlgorithm.NORMAL);
//		}

		webview.setWebChromeClient(new WebChromeClient(){
        	public void onProgressChanged(WebView view,int progress){//载入进度改变而触发 
                super.onProgressChanged(view, progress);  
                if (progress == 100) {
					webview.getSettings().setBlockNetworkImage(false);
					if(loadingDialog != null && loadingDialog.isShowing()){
						loadingDialog.dismiss();
					}
					if(TextUtils.isEmpty(title)){
						setNavigationTitle(view.getTitle());
					}
					loading_layout.setVisibility(View.GONE);
				}else{
					loading_layout.setVisibility(View.VISIBLE);
				}
			}
		});

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.getSettings().setBlockNetworkImage(true);
				return false;
			}
		});

		webview.loadUrl(url);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
	        if(webview != null && webview.canGoBack()){
    	        webview.goBack();
    	        return false;
	        }
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		if(loadingDialog != null && loadingDialog.isShowing()){
			loadingDialog.cancel();
		}
		if(webview != null)
		    webview.stopLoading();
		super.onDestroy();
	}
	
	@Override
	public void onClick(View v) {
	    super.onClick(v);
	    switch(v.getId()){
	    case R.id.navigation_right_btn:
	        if(webview != null)
	            webview.reload(); 
	        break;
	    }
	}
	
	public static void show(Context context, String url, String title){
		Bundle data = new Bundle();
		data.putString(URL_KEY, url);
		data.putString(URL_TITLE, title);
		BaseActivity.showActivity(context, WebViewActivity.class, data);
	}
}
 