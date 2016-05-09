package com.tourism.app.weixinpay;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tourism.app.R;
import com.tourism.app.vo.OrderVO;
import com.tourism.app.widget.dialog.CustomLoadingDialog;

public class WeiXinPayManager {

	private static final String TAG = "MicroMsg.SDKSample.WeiXinPayManager";

	private IWXAPI api;

	/**
	 * 上下文
	 */
	private Activity act;
	private CustomLoadingDialog dialog;

	private OrderVO vo;

	public WeiXinPayManager(Activity act, OrderVO vo) {
		this.act = act;
		this.vo = vo;
		api = WXAPIFactory.createWXAPI(act, Constants.APP_ID);
		api.registerApp(Constants.APP_ID);
		// 初始化加载框
		dialog = new CustomLoadingDialog(act, "", true);
	}

	public void pay() {
		if (checkIsSupportPay()) {
			new GetAccessTokenTask().execute();
		} else {
			Toast.makeText(act, String.valueOf("不支持微信支付"), Toast.LENGTH_SHORT)
					.show();
		}

	}

	public boolean checkIsSupportPay() {
		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
		return isPaySupported;

	}

	private String genPackage(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.PARTNER_KEY); // 注意：不能hardcode在客户端，建议genPackage这个过程都由服务器端完成

		// 进行md5摘要前，params内容为原始内容，未经过url encode处理
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.d("d", "package签名串：" + sb.toString());
		return URLEncodedUtils.format(params, "utf-8") + "&sign=" + packageSign;
	}

	private class GetAccessTokenTask extends
			AsyncTask<Void, Void, GetAccessTokenResult> {

		@Override
		protected void onPreExecute() {
			dialog.show();
		}

		@Override
		protected void onPostExecute(GetAccessTokenResult result) {

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				// Toast.makeText(act, R.string.get_access_token_succ,
				// Toast.LENGTH_LONG).show();
				Log.d(TAG, "onPostExecute, accessToken = " + result.accessToken);

				GetPrepayIdTask getPrepayId = new GetPrepayIdTask(
						result.accessToken);
				getPrepayId.execute();
			} else {
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(
						act,
						act.getString(R.string.get_access_token_fail,
								result.localRetCode.name()), Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		protected GetAccessTokenResult doInBackground(Void... params) {
			GetAccessTokenResult result = new GetAccessTokenResult();

			String url = String
					.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
							Constants.APP_ID, Constants.APP_SECRET);
			Log.d(TAG, "get access token, url = " + url);

			byte[] buf = Util.httpGet(url);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}

			String content = new String(buf);
			result.parseFrom(content);
			return result;
		}
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, GetPrepayIdResult> {

		private String accessToken;

		public GetPrepayIdTask(String accessToken) {
			this.accessToken = accessToken;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onPostExecute(GetPrepayIdResult result) {

			if (result.localRetCode == LocalRetCode.ERR_OK) {
				// Toast.makeText(act, R.string.get_prepayid_succ,
				// Toast.LENGTH_LONG).show();
				sendPayReq(result);
			} else {
				if (dialog != null) {
					dialog.dismiss();
				}
				Toast.makeText(
						act,
						act.getString(R.string.get_prepayid_fail,
								result.localRetCode.name()), Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected GetPrepayIdResult doInBackground(Void... params) {

			String url = String.format(
					"https://api.weixin.qq.com/pay/genprepay?access_token=%s",
					accessToken);
			String entity = ""; //genProductArgs();

			Log.d(TAG, "doInBackground, url = " + url);
			Log.d(TAG, "doInBackground, entity = " + entity);

			GetPrepayIdResult result = new GetPrepayIdResult();

			byte[] buf = Util.httpPost(url, entity);
			if (buf == null || buf.length == 0) {
				result.localRetCode = LocalRetCode.ERR_HTTP;
				return result;
			}

			String content = new String(buf);
			Log.d(TAG, "doInBackground, content = " + content);
			result.parseFrom(content);
			return result;
		}
	}

	private static enum LocalRetCode {
		ERR_OK, ERR_HTTP, ERR_JSON, ERR_OTHER
	}

	private static class GetAccessTokenResult {

		private static final String TAG = "MicroMsg.SDKSample.PayActivity.GetAccessTokenResult";

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String accessToken;
		public int expiresIn;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("access_token")) { // success case
					accessToken = json.getString("access_token");
					expiresIn = json.getInt("expires_in");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					errCode = json.getInt("errcode");
					errMsg = json.getString("errmsg");
					localRetCode = LocalRetCode.ERR_JSON;
				}

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	private static class GetPrepayIdResult {

		private static final String TAG = "MicroMsg.SDKSample.PayActivity.GetPrepayIdResult";

		public LocalRetCode localRetCode = LocalRetCode.ERR_OTHER;
		public String prepayId;
		public int errCode;
		public String errMsg;

		public void parseFrom(String content) {

			if (content == null || content.length() <= 0) {
				Log.e(TAG, "parseFrom fail, content is null");
				localRetCode = LocalRetCode.ERR_JSON;
				return;
			}

			try {
				JSONObject json = new JSONObject(content);
				if (json.has("prepayid")) { // success case
					prepayId = json.getString("prepayid");
					localRetCode = LocalRetCode.ERR_OK;
				} else {
					localRetCode = LocalRetCode.ERR_JSON;
				}

				errCode = json.getInt("errcode");
				errMsg = json.getString("errmsg");

			} catch (Exception e) {
				localRetCode = LocalRetCode.ERR_JSON;
			}
		}
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	/**
	 * 建议 traceid 字段包含用户信息及订单信息，方便后续对订单状态的查询和跟踪
	 */
	private String getTraceId() {
		return "crestxu_" + genTimeStamp();
	}

	/**
	 * 注意：商户系统内部的订单号,32个字符内、可包含字母,确保在商户系统唯一
	 */
	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long timeStamp;
	private String nonceStr, packageValue;

	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());

		String sha1 = Util.sha1(sb.toString());
		Log.d("d", "sha1签名串：" + sb.toString());
		Log.d(TAG, "genSign, sha1 = " + sha1);
		return sha1;
	}

	// private String genProductArgs() {
	// JSONObject json = new JSONObject();
	//
	// try {
	// json.put("appid", Constants.APP_ID);
	// String traceId = getTraceId(); // traceId
	// 由开发者自定义，可用于订单的查询与跟踪，建议根据支付用户信息生成此id
	// json.put("traceid", traceId);
	// nonceStr = genNonceStr();
	// json.put("noncestr", nonceStr);
	//
	// List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
	// packageParams.add(new BasicNameValuePair("bank_type", "WX"));
	// packageParams.add(new BasicNameValuePair("body", goodsDescription));
	// packageParams.add(new BasicNameValuePair("fee_type", "1"));
	// packageParams.add(new BasicNameValuePair("input_charset", "UTF-8"));
	// packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
	// packageParams.add(new BasicNameValuePair("out_trade_no", orderId));
	// packageParams.add(new BasicNameValuePair("partner",
	// Constants.PARTNER_ID));
	// packageParams.add(new BasicNameValuePair("spbill_create_ip",
	// "196.168.1.1"));
	// packageParams.add(new BasicNameValuePair("total_fee", totalFee+""));
	// packageValue = genPackage(packageParams);
	//
	// json.put("package", packageValue);
	// timeStamp = genTimeStamp();
	// json.put("timestamp", timeStamp);
	//
	// List<NameValuePair> signParams = new LinkedList<NameValuePair>();
	// signParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
	// signParams.add(new BasicNameValuePair("appkey", Constants.APP_KEY));
	// signParams.add(new BasicNameValuePair("noncestr", nonceStr));
	// signParams.add(new BasicNameValuePair("package", packageValue));
	// signParams.add(new BasicNameValuePair("timestamp",
	// String.valueOf(timeStamp)));
	// signParams.add(new BasicNameValuePair("traceid", traceId));
	// json.put("app_signature", genSign(signParams));
	//
	// json.put("sign_method", "sha1");
	// } catch (Exception e) {
	// Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
	// return null;
	// }
	//
	// return json.toString();
	// }

	private void sendPayReq(GetPrepayIdResult result) {

		PayReq req = new PayReq();
		req.appId = Constants.APP_ID;
		req.partnerId = Constants.PARTNER_ID;
		req.prepayId = result.prepayId;
		req.nonceStr = nonceStr;
		req.timeStamp = String.valueOf(timeStamp);
		req.packageValue = "Sign=" + packageValue;
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("appkey", Constants.APP_KEY));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genSign(signParams);
		Log.d("d", "调起支付的package串：" + req.packageValue);
		Log.d("d", "调起支付的sign串：" + genSign(signParams));
		Log.d("d", "prepayId：" + result.prepayId);
		Log.d("d", "nonceStr：" + nonceStr);
		Log.d("d", "timeStamp：" + String.valueOf(timeStamp));
		Log.d("d", "appId：" + Constants.APP_ID);
		Log.d("d", "partnerId：" + Constants.PARTNER_ID);
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.sendReq(req);
		if (dialog != null) {
			dialog.dismiss();
		}
	}

	public void sendPayReq() {

		PayReq req = new PayReq();
		req.appId = vo.getAppid();
		req.partnerId = vo.getPartnerid();
		req.prepayId = vo.getPrepayid();
		req.nonceStr = vo.getNoncestr();
		req.timeStamp = vo.getTimestamp();
		req.packageValue = vo.getPackageValue();
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", vo.getAppid()));
//		signParams.add(new BasicNameValuePair("appkey", Constants.APP_KEY));
		signParams.add(new BasicNameValuePair("noncestr", vo.getNoncestr()));
		signParams.add(new BasicNameValuePair("package", vo.getPackageValue()));
		signParams.add(new BasicNameValuePair("partnerid", vo.getPartnerid()));
		signParams.add(new BasicNameValuePair("prepayid", vo.getPrepayid()));
		signParams.add(new BasicNameValuePair("timestamp", vo.getTimestamp()));
		req.sign = genSign(signParams);
		Log.d("d", "调起支付的package串：" + vo.getPackageValue());
		Log.d("d", "调起支付的sign串：" + genSign(signParams));
		Log.d("d", "prepayId：" + vo.getPrepayid());
		Log.d("d", "nonceStr：" + vo.getNoncestr());
		Log.d("d", "timeStamp：" + vo.getTimestamp());
		Log.d("d", "appId：" + vo.getAppid());
		Log.d("d", "partnerId：" + vo.getPartnerid());
		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.sendReq(req);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
