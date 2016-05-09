/** 
 * Project Name:TourismAppClient 
 * File Name:BrandOnlineAdviceActivity.java 
 * Package Name:com.tourism.app.activity.brand 
 * Date:2016年4月6日下午4:35:21 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.brand;  

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.BaseVO;

/** 
 * ClassName:BrandOnlineAdviceActivity 
 * Date:     2016年4月6日 下午4:35:21
 * @author   Jzy 
 * @version   
 * @see       
 */
public class BrandFeedbackActivity extends BaseActivity{
	private final int REQUEST_SEND_FEEDBACK_CODE = 10001;

	private EditText feedback_content_tv;
	private EditText feedback_user_tv;
	private EditText feedback_phone_tv;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_brand_feedback);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		feedback_content_tv = (EditText) findViewById(R.id.feedback_content_tv);
		feedback_user_tv = (EditText) findViewById(R.id.feedback_user_tv);
		feedback_phone_tv = (EditText) findViewById(R.id.feedback_phone_tv);
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initValue() {
		setNavigationTitle(R.string.feedback_title);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.feedback_submit_btn:
			String phone = feedback_phone_tv.getText().toString();
			String name = feedback_user_tv.getText().toString();
			String content = feedback_content_tv.getText().toString();
			
			if(TextUtils.isEmpty(phone)){
				showToast("电话号码不能为空");
			}else if(TextUtils.isEmpty(name)){
				showToast("姓名不能为空");
			}else if(TextUtils.isEmpty(content)){
				showToast("内容不能为空");
			}else{
				sendBrandFeedback(phone, name, content);
			}
			
			break;
		}
	}
	
	/**
	 * 获取品牌信息
	 */
	public void sendBrandFeedback(String phone, String name, String content) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("phone", phone));
		parameter.add(new RequestParameter("name", name));
		parameter.add(new RequestParameter("content", content));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_BRAND_FEEDBACK, parameter, true, REQUEST_SEND_FEEDBACK_CODE);
	}
	
	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);
		try{
			BaseResponseMessage brm = new BaseResponseMessage();
			switch(resultCode){
			case REQUEST_SEND_FEEDBACK_CODE:
				brm.parseResponse(resultJson, new TypeToken<BaseVO>(){});
				if(brm.isSuccess()){
					showToast(R.string.toast_send_success);
					goBack();
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
  