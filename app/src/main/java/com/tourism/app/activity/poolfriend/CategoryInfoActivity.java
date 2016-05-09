/** 
 * Project Name:TourismAppClient 
 * File Name:EventInfoActivity.java 
 * Package Name:com.tourism.app.activity.poolfriend.adapter 
 * Date:2016年4月27日上午10:48:45 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tourism.app.activity.poolfriend;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AppOpsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.alipay.AlixManager;
import com.tourism.app.alipay.AlixManager.OnPayListener;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.DeviceUtil;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.DialogUtil.OnCallbackListener;
import com.tourism.app.vo.EventVO;
import com.tourism.app.vo.OrderVO;
import com.tourism.app.weixinpay.WeiXinPayManager;
import com.tourism.app.widget.dialog.CustomLoadingDialog;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;
import com.tourism.app.wxapi.WXPayEntryActivity;

/**
 * ClassName:EventInfoActivity Date: 2016年4月27日 上午10:48:45
 * 
 * @author Jzy
 * @version
 * @see
 */
public class CategoryInfoActivity extends BaseActivity {
	private final int GET_EVENT_INFO_KEY = 10001;
	private final int REQUEST_SIGNED_UP_KEY = 10002;
	private final int REQUEST_MAKE_KEY = 10003;

	private ImageView event_banner_iv;
	private TextView navigation_price_tv;
	private TextView navigation_name_tv;
	private TextView event_slogan_tv;
	private TextView user_tag_tv;
	private ImageView user_icon_iv;
	private TextView user_vip_tv;
	private TextView user_name_tv;
	private TextView user_alias_tv;
	private ImageView user_id_auth_iv;
	private TextView user_id_is_auth_tv;
	private ImageView user_email_auth_iv;
	private TextView user_email_is_auth_tv;
	private ImageView user_phone_auth_iv;
	private TextView user_phone_is_auth_tv;
	private TextView event_price_tv;
	private TextView event_pv_tv;
	private WebView webview;
	
	/**
	 * 底部报名栏
	 */
	private View foot_bar_rl;
	
	/**
	 * 预约栏
	 */
	private View make_rl;

	private EventVO vo;

	private DisplayImageOptions circleOptions;
	
	/**
	 * 支付方式 
	 * 1 微信
	 * 2 支付宝
	 */
	private String payType = "0";
	
	private WxPayReceiver wxPayReceiver;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_event_info);
	}

	@Override
	public void init() {
		wxPayReceiver = new WxPayReceiver();
		
		vo = (EventVO) getIntent().getExtras().getSerializable("vo");
		loadingDialog = new CustomLoadingDialog(context, "", false);

		circleOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_default_horizon)
				.showImageForEmptyUri(R.drawable.img_default_horizon)
				.showImageOnFail(R.drawable.img_default_horizon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new CircleBitmapDisplayer()).build();
	}

	@Override
	public void initView() {
		event_banner_iv = (ImageView) findViewById(R.id.event_banner_iv);
		navigation_price_tv = (TextView) findViewById(R.id.navigation_price_tv);
		navigation_name_tv = (TextView) findViewById(R.id.navigation_name_tv);
		event_slogan_tv = (TextView) findViewById(R.id.event_slogan_tv);
		user_tag_tv = (TextView) findViewById(R.id.user_tag_tv);
		user_icon_iv = (ImageView) findViewById(R.id.user_icon_iv);
		user_vip_tv = (TextView) findViewById(R.id.user_vip_tv);
		user_name_tv = (TextView) findViewById(R.id.user_name_tv);
		user_alias_tv = (TextView) findViewById(R.id.user_alias_tv);
		user_id_auth_iv = (ImageView) findViewById(R.id.user_id_auth_iv);
		user_id_is_auth_tv = (TextView) findViewById(R.id.user_id_is_auth_tv);
		user_email_auth_iv = (ImageView) findViewById(R.id.user_email_auth_iv);
		user_email_is_auth_tv = (TextView) findViewById(R.id.user_email_is_auth_tv);
		user_phone_auth_iv = (ImageView) findViewById(R.id.user_phone_auth_iv);
		user_phone_is_auth_tv = (TextView) findViewById(R.id.user_phone_is_auth_tv);
		event_price_tv = (TextView) findViewById(R.id.event_price_tv);
		event_pv_tv = (TextView) findViewById(R.id.event_pv_tv);
		webview = (WebView) findViewById(R.id.webview);
		foot_bar_rl = findViewById(R.id.foot_bar_rl);
		make_rl = findViewById(R.id.make_rl);
	}

	private void setViewsValue() {
		String tag = "";
		for (int i = 0; i < vo.getTags().size(); i++) {
			tag += vo.getTags().get(i);
			if (i < vo.getTags().size() - 1) {
				tag += "||";
			}
		}
		
		if(vo.getType() == 1){
			foot_bar_rl.setVisibility(View.VISIBLE);
			make_rl.setVisibility(View.GONE);
		}else{
			foot_bar_rl.setVisibility(View.GONE);
			make_rl.setVisibility(View.VISIBLE);
		}

		ImageLoader.getInstance().displayImage(vo.getPoster(), event_banner_iv,
				options, animateFirstListener);
		navigation_price_tv.setVisibility(View.INVISIBLE);
		navigation_name_tv.setText(vo.getTitle());
		event_slogan_tv.setText(vo.getSlogan());
		user_tag_tv.setText(tag);
		ImageLoader.getInstance().displayImage(vo.getUser_avatar(),
				user_icon_iv, circleOptions, animateFirstListener);
		user_name_tv.setText(vo.getUser_nickname());
		user_alias_tv.setVisibility(View.INVISIBLE);
		if (vo.getIs_review() == 1) {
			user_id_auth_iv.setBackgroundResource(R.drawable.icon_is_auth);
			user_id_is_auth_tv.setText("已认证");
		} else {
			user_id_auth_iv.setBackgroundResource(R.drawable.icon_not_auth);
			user_id_is_auth_tv.setText("未认证");
		}

		if (vo.getIs_bind() == 1) {
			user_phone_auth_iv.setBackgroundResource(R.drawable.icon_is_auth);
			user_phone_is_auth_tv.setText("已绑定");
		} else {
			user_phone_auth_iv.setBackgroundResource(R.drawable.icon_not_auth);
			user_phone_is_auth_tv.setText("未绑定");
		}
		event_price_tv.setText(vo.getDeposit() + "/人");
		event_pv_tv.setText("浏览数\n" + vo.getPv());

		webview.loadUrl(vo.getUrl_web());
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.cancel();
		}
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initValue() {

		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {// 载入进度改变而触发
				super.onProgressChanged(view, progress);
				if (progress == 100) {
					webview.getSettings().setBlockNetworkImage(false);
				}
				// if (progress > 50 && loadingDialog != null &&
				// loadingDialog.isShowing()) {
				// loadingDialog.cancel();
				// }
			}
		});

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webview.getSettings().setBlockNetworkImage(true);
				// loadingDialog.show();
				return false;
			}
		});
		webview.getSettings().setDefaultTextEncodingName("utf-8");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			webview.getSettings().setLayoutAlgorithm(
					WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
		} else {
			webview.getSettings().setLayoutAlgorithm(
					WebSettings.LayoutAlgorithm.NORMAL);
		}

		requestEventInfo(vo.getLink());
		loadingDialog.show();
	}

	@Override
	protected void onStart() {
		super.onStart();
		registerReceiver(wxPayReceiver, new IntentFilter(WXPayEntryActivity.TAG));
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unregisterReceiver(wxPayReceiver);
	}
	
	/**
	 * 获取服务信息
	 */
	public void requestEventInfo(String url) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequest(Constants.HTTP_GET, url, parameter, false,
				GET_EVENT_INFO_KEY);
	}
	
	/**
	 * 发送预约信息
	 */
	public void requestMake(String userName, String userPhone, String payType) {
		this.payType = payType;
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("token", getUserInfo().getToken()));
		parameter.add(new RequestParameter("aid", vo.getId()));
		parameter.add(new RequestParameter("name", userName));
		parameter.add(new RequestParameter("phone", userPhone));
		parameter.add(new RequestParameter("pay_type", payType));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_CATEGORY_MAKE, parameter, true,
				REQUEST_MAKE_KEY);
	}
	
	/**
	 * 发送报名信息
	 */
	public void requestSignedUp(String userName, String userPhone, String remarks) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("token", getUserInfo().getToken()));
		parameter.add(new RequestParameter("aid", vo.getId()));
		parameter.add(new RequestParameter("name", userName));
		parameter.add(new RequestParameter("phone", userPhone));
		parameter.add(new RequestParameter("content", remarks));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_CATEGORY_SIGNED_UP, parameter, true,
				REQUEST_SIGNED_UP_KEY);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		// TODO Auto-generated method stub
		super.onCallbackFromThread(resultJson, resultCode);
		try {
			switch (resultCode) {
			case GET_EVENT_INFO_KEY:
				BaseResponseMessage br1 = new BaseResponseMessage();
				br1.parseResponse(resultJson, new TypeToken<EventVO>() {
				});
				if (br1.isSuccess()) {
					vo = (EventVO) br1.getResult();
					setViewsValue();
				}
				break;
			case REQUEST_SIGNED_UP_KEY:
				BaseResponseMessage br2 = new BaseResponseMessage();
				br2.parseResponse(resultJson, new TypeToken<EventVO>() {
				});
				if (br2.isSuccess()) {
					DialogUtil.showTipDialog(this, "恭喜！您已经报名成功", "详情，请到我的订单查询", null);
				}
				break;
			case REQUEST_MAKE_KEY:
				BaseResponseMessage br3 = new BaseResponseMessage();
				br3.parseResponse(resultJson, new TypeToken<OrderVO>() {});
				if (br3.isSuccess()) {
					OrderVO orderVO = (OrderVO) br3.getResult();
					if("1".equals(payType)){
						preferencesUtils.putString("wx_app_id", orderVO.getAppid());
						WeiXinPayManager weixinManager = new WeiXinPayManager(this, orderVO);
						weixinManager.sendPayReq();
					}else if("2".equals(payType)){
						AlixManager am = new AlixManager(this, br3.getData(), new OnPayListener() {
							public void payCallback(boolean success, String statusCode, String msg) {
								showToast(msg);
								if(success){
									DialogUtil.showTipDialog(context, "恭喜！您已经预约成功", "详情，请到我的订单查询", null);
								}else{
									DialogUtil.showTipDialog(context, "支付失败", "详情，请到我的订单查询", null);
								}
							}
						});
						am.pay();
					}
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		System.err.println("==========onActivityResult");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.navigation_share_btn:
			break;
		case R.id.navigation_reply_btn:
			break;
		case R.id.navigation_collection_btn:
			break;
		case R.id.navigation_down_btn:
			break;
		case R.id.user_pay_btn:
			// 预定
			DialogUtil.showCategorySignedUpDialog(this, vo.getDeposit(), vo.getAmount(),
					new OnCallbackListener() {
						public void onClick(int whichButton, Object o) {
							Map<String, String> map = (Map<String, String>) o;
							String userName = map.get("userName");
							String userPhone = map.get("userPhone");
							String payType = map.get("payType");
							requestMake(userName, userPhone, payType);
						}
					});

			break;
		case R.id.call_phone_btn:
			DeviceUtil.openCallPhone(this, getString(R.string.service_phone));
			break;
		case R.id.signed_up_btn:
			// 报名
			DialogUtil.showCategoryMakeDialog(this, new OnCallbackListener() {
						public void onClick(int whichButton, Object o) {
							Map<String, String> map = (Map<String, String>) o;
							String userName = map.get("userName");
							String userPhone = map.get("userPhone");
							String remarks = map.get("remarks");
							requestSignedUp(userName, userPhone, remarks);
						}
					});
			break;
		}
	}
	
	class WxPayReceiver extends BroadcastReceiver{
		public void onReceive(Context ctx, Intent intent) {
			if(intent != null && intent.getAction().equals(WXPayEntryActivity.TAG)){
				if(intent.getIntExtra("code", 1) == 0){
						DialogUtil.showTipDialog(context, "恭喜！您已经预约成功", "详情，请到我的订单查询", null);
				}else{
					DialogUtil.showTipDialog(context, "支付失败", "详情，请到我的订单查询", null);
				}
			}
		}
		
	}
}
