/** 
 * Project Name:TourismAppClient 
 * File Name:UserRegisterVerifyCodeActivity.java 
 * Package Name:com.tourism.app.activity.user 
 * Date:2016年4月19日下午4:00:03 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.user;  

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.MainActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.UserInfoVO;

/** 
 * ClassName:UserRegisterVerifyCodeActivity 
 * Date:     2016年4月19日 下午4:00:03
 * @author   Jzy 
 * @version   
 * @see       
 */
public class UserRegisterVerifyCodeActivity extends BaseActivity{
	private final int REQUEST_GET_SMS_CODE = 10001;
	private final int REQUEST_REGISTER_CODE = 10002;
	
	private String userName;
	private String password;
	private String verifyCode;
	
	private EditText user_register_verify_code_et;
	private TextView user_phone_tv;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user_regester_verify_code);
	}

	@Override
	public void init() {
		userName = getIntent().getExtras().getString("userName");
		password = getIntent().getExtras().getString("password");
	}

	@Override
	public void initView() {
		user_register_verify_code_et = (EditText) findViewById(R.id.user_register_verify_code_et);
		user_phone_tv = (TextView) findViewById(R.id.user_phone_tv);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initValue() {
		setNavigationTitle(R.string.user_register_verify_code_title);
		user_phone_tv.setText("+86 " + userName);
		requestVerifyCode(userName);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.login_submit_btn:
			verifyCode = user_register_verify_code_et.getText().toString();
			if(TextUtils.isEmpty(verifyCode)){
				showToast(R.string.toast_verify_code_is_empty);
			}else{
				requestRegister();
			}
			break;
		}
	}

	/**
	 * 获取验证码
	 * @param userName
	 * @param password
	 */
	public void requestVerifyCode(String userName) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("phone", new String(Base64.encode(userName.getBytes(), Base64.DEFAULT))));
		startHttpRequest(Constants.HTTP_GET, Constants.URL_GET_SMS_AUTH_CODE, parameter, true, REQUEST_GET_SMS_CODE);
	}
	
	/**
	 * 注册
	 * @param userName
	 * @param password
	 */
	public void requestRegister() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("phone", userName));
		parameter.add(new RequestParameter("password", password));
		parameter.add(new RequestParameter("checkcode", verifyCode));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_ACCOUNT_REGISTER, parameter, true, REQUEST_REGISTER_CODE);
	}
	
	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);
		try{
			BaseResponseMessage brm = new BaseResponseMessage();
			switch(resultCode){
			case REQUEST_GET_SMS_CODE:
				brm.parseResponse(resultJson, new TypeToken<UserInfoVO>(){});
				if(!brm.isSuccess()){
					goBack();
				}else{
					showToast(R.string.toast_verify_code_is_success);
				}
				break;
			case REQUEST_REGISTER_CODE:
				brm.parseResponse(resultJson, new TypeToken<UserInfoVO>(){});
				if(brm.isSuccess()){
					UserInfoVO mUserInfo = new UserInfoVO();
					mUserInfo.setToken(brm.getToken());
					setUserInfo(mUserInfo);
					clearTask();
					showActivity(context, MainActivity.class);
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
  