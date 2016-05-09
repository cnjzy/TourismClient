package com.tourism.app.activity.user;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.FileVO;
import com.tourism.app.vo.UserInfoVO;

public class UserNickNameEditActivity extends BaseActivity{
	private final int REQUEST_EDIT_USER_INFO_CODE = 10003;
	
	private EditText user_nickname_et;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user_nickname);
	}

	@Override
	public void init() {
		
	}

	@Override
	public void initView() {
		user_nickname_et = (EditText)  findViewById(R.id.user_nickname_et);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initValue() {
		setNavigationRightButton(View.VISIBLE, 0, R.drawable.ico_ok);
		setNavigationTitle("设置昵称");
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.navigation_right_btn:
			String nickName = user_nickname_et.getText().toString();
			if(TextUtils.isEmpty(nickName)){
				showToast("昵称不能为空");
			}else{
				requestUpdateUserInfoByKey("nickname", nickName);
			}
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
