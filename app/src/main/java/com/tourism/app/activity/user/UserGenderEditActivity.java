package com.tourism.app.activity.user;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.RadioButton;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.FileVO;

public class UserGenderEditActivity extends BaseActivity{
	private final int REQUEST_EDIT_USER_INFO_CODE = 10003;
	
	private RadioButton gender_male_rb;
	private RadioButton gender_female_rb;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user_gender);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		gender_male_rb = (RadioButton) findViewById(R.id.gender_male_rb);
		gender_female_rb = (RadioButton) findViewById(R.id.gender_female_rb);
		
	}

	@Override
	public void initListener() {
		
	}

	@Override
	public void initValue() {
		setNavigationRightButton(View.VISIBLE, 0, R.drawable.ico_ok);
		setNavigationTitle("设置性别");
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.navigation_right_btn:
			int gender = gender_male_rb.isChecked() ? 1 : 2;
			requestUpdateUserInfoByKey("sex", String.valueOf(gender));
			break;
		}
	}

	/**
	 * 上传用户信息
	 * @param key
	 * @param value
	 */
	private void requestUpdateUserInfoByKey(String key, String value){
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("token", getUserInfo().getToken()));
		parameter.add(new RequestParameter("action", key));
		parameter.add(new RequestParameter("value", value));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_SET_USER_INFO,
				parameter, true, REQUEST_EDIT_USER_INFO_CODE);
	}
	
	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);
		try{
			BaseResponseMessage brm = new BaseResponseMessage();
			switch(resultCode){
			case REQUEST_EDIT_USER_INFO_CODE:
				brm.parseResponse(resultJson, new TypeToken<FileVO>(){});
				if(brm.isSuccess()){
					showToast("修改成功");
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
