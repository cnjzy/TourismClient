/** 
 * Project Name:TourismAppClient 
 * File Name:UserRegisterActivity.java 
 * Package Name:com.tourism.app.activity.user 
 * Date:2016年4月19日下午2:45:11 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.user;  

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;

/** 
 * ClassName:UserRegisterActivity 
 * Date:     2016年4月19日 下午2:45:11
 * @author   Jzy 
 * @version   
 * @see       
 */
public class UserRegisterActivity extends BaseActivity{
	private EditText login_user_et;
	private EditText login_password_et;
	private Button login_submit_btn;
	
	private TextWatcher mTextWatcher = new TextWatcher() {
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String userName = login_user_et.getText().toString();
			String password = login_password_et.getText().toString();
			if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
				login_submit_btn.setBackgroundResource(R.drawable.btn_blue2_round);
			}else{
				login_submit_btn.setBackgroundResource(R.drawable.btn_gray_round);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user_register);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		login_user_et = (EditText) findViewById(R.id.login_user_et);
		login_password_et = (EditText) findViewById(R.id.login_password_et);
		login_submit_btn = (Button) findViewById(R.id.login_submit_btn);
	}

	@Override
	public void initListener() {
		login_user_et.addTextChangedListener(mTextWatcher);
		login_password_et.addTextChangedListener(mTextWatcher);
	}

	@Override
	public void initValue() {
		setNavigationTitle(R.string.regiter_title);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.login_sina_btn:
			break;
		case R.id.login_weixin_btn:
			break;
		case R.id.login_submit_btn:
			String userName = login_user_et.getText().toString();
			String password = login_password_et.getText().toString();
			if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
				if (password.length() < 6) {
					showToast(R.string.toast_user_password_is_empty);
				} else {
					Bundle data = new Bundle();
					data.putString("userName", userName);
					data.putString("password", password);
					showActivity(context, UserRegisterVerifyCodeActivity.class, data);
				}
			}
			break;
		}
	}
}
  