/** 
 * Project Name:TourismAppClient 
 * File Name:UserLoginActivity.java 
 * Package Name:com.tourism.app.activity.user 
 * Date:2016年4月19日下午2:44:56 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tourism.app.activity.user;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.UserInfoVO;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName:UserLoginActivity Date: 2016年4月19日 下午2:44:56
 * 
 * @author Jzy
 * @version
 * @see
 */
public class UserLoginActivity extends BaseActivity {
	private final int REQUEST_LOGIN_CODE = 10001;

	private EditText login_user_et;
	private EditText login_password_et;

	private UMShareAPI mShareAPI = null;
	private SHARE_MEDIA platform;

	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//			Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
			System.out.println("data:" + data.toString());
			String uid = data.get("uid");
			String token = data.get("access_token");
			requestLoginBySina(uid, token);
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//			Toast.makeText( getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
//			Toast.makeText( getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("auth", "on activity re 2");
		mShareAPI.onActivityResult(requestCode, resultCode, data);
		Log.d("auth", "on activity re 3");
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user_login);
	}

	@Override
	public void init() {
		mShareAPI = UMShareAPI.get( this );
	}

	@Override
	public void initView() {
		login_user_et = (EditText) findViewById(R.id.login_user_et);
		login_password_et = (EditText) findViewById(R.id.login_password_et);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initValue() {
		setNavigationTitle(R.string.login_title);
//		setNavigationRightButton(View.VISIBLE, R.string.regiter_title, 0);
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("Constants.WEICHAT_CODE:" + Constants.WEICHAT_CODE);
		if (!TextUtils.isEmpty(Constants.WEICHAT_CODE)){
			requestLoginByWeichat(Constants.WEICHAT_CODE);
			Constants.WEICHAT_CODE = "";
		}
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.navigation_right_btn:
			showActivity(context, UserRegisterActivity.class);
			break;
		case R.id.login_sina_btn:
			platform = SHARE_MEDIA.SINA;
			mShareAPI.doOauthVerify(this, platform, umAuthListener);
			break;
		case R.id.login_weixin_btn:
			platform = SHARE_MEDIA.WEIXIN;
			mShareAPI.doOauthVerify(this, platform, umAuthListener);
			break;
		case R.id.forget_password_btn:
			showActivity(context, UserForgetPasswordActivity.class);
			break;
		case R.id.login_submit_btn:
			String userName = login_user_et.getText().toString();
			String password = login_password_et.getText().toString();
			if (TextUtils.isEmpty(userName)) {
				showToast(R.string.toast_user_name_is_empty);
			} else if (TextUtils.isEmpty(password) || password.length() < 6) {
				showToast(R.string.toast_user_password_is_empty);
			} else {
				requestLogin(userName, password);
			}
			break;
		}
	}

	/**
	 * 登录
	 * @param userName
	 * @param password
	 */
	public void requestLogin(String userName, String password) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("phone", userName));
		parameter.add(new RequestParameter("password", password));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_ACCOUNT_LOGIN, parameter, true, REQUEST_LOGIN_CODE);
	}

	/**
	 * 微博登录
	 * @param uid
	 * @param token
	 */
	public void requestLoginBySina(String uid, String token) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("uid", uid));
		parameter.add(new RequestParameter("token", token));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_SINA_LOGIN, parameter, true, REQUEST_LOGIN_CODE);
	}

	/**
	 * 微信登录
	 * @param token
	 */
	public void requestLoginByWeichat(String token) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("code", token));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_WEICHAT_LOGIN, parameter, true, REQUEST_LOGIN_CODE);
	}
	
	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);
		try{
			BaseResponseMessage brm = new BaseResponseMessage();
			switch(resultCode){
			case REQUEST_LOGIN_CODE:
				brm.parseResponse(resultJson, new TypeToken<UserInfoVO>(){});
				if(brm.isSuccess()){
					UserInfoVO mUserInfo = new UserInfoVO();
					mUserInfo.setToken(brm.getToken());
					setUserInfo(mUserInfo);
					setResult(RESULT_OK);
					finish();
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
