package com.tourism.app.weixinpay;

public class Constants {
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
	public static final String APP_ID = "wx4f67167b10dfbdf8";

	/** 商家向财付通申请的商家id */
	public static final String PARTNER_ID = "1230274401";

	/**
	 * 微信开放平台和商户约定的密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	 */
	public static final String APP_SECRET = "02728adf6c9d6db62b5abaf6a6208d0b"; // wxd930ea5d5a258f4f
																				// 对应的密钥

	/**
	 * 微信开放平台和商户约定的支付密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	 */
	public static final String APP_KEY = "Uv25myOglNm00ukKISjffSNOKlxRFqUxBLDlu77yfrvD7bSKonKBQAS3qodlLZHzqFJg9rvmqGM3SAHj4bOsFaIoSYEmo3x4g8CHK0XRY18O0sWO1OvTVQ18qNDGy6Dt"; // wxd930ea5d5a258f4f
																																												// 对应的支付密钥

	/**
	 * 微信公众平台商户模块和商户约定的密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genPackage这个过程由服务器端完成
	 */
	public static final String PARTNER_KEY = "f90ad1f9f591a3c08793cae51d7d5248";


	public static class ShowMsgActivity {
		public static final String STitle = "showmsg_title";
		public static final String SMessage = "showmsg_message";
		public static final String BAThumbData = "showmsg_thumb_data";
	}
}
