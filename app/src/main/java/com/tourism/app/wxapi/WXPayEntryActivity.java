package com.tourism.app.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tourism.app.MyApp;
import com.tourism.app.util.LogUtil;

import java.util.HashMap;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	public static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	 private IWXAPI api;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.err.println("=========================WXPayEntryActivity");
		api = WXAPIFactory.createWXAPI(this, MyApp.preferencesUtils.getString("wx_app_id", ""));
        api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		LogUtil.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			String payResult = "";
			switch (resp.errCode) {
			case 0:
				payResult = "支付成功";
				// 友盟统计 点击事件
				HashMap<String, String> paramAlia = new HashMap<String, String>();
				paramAlia.put("paySuccessMethod", "微信");
				break;
			default:
				payResult = "支付失败";
				break;
			}
			System.err.println("========================= " + payResult);
			Intent wxPayIntent = new Intent();
			wxPayIntent.setAction(TAG);
			wxPayIntent.putExtra("code", resp.errCode);
			sendBroadcast(wxPayIntent);
			finish();
		}
	}
}