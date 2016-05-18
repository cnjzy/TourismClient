package com.tourism.app.activity.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.PhotoManager;
import com.tourism.app.vo.FileVO;
import com.tourism.app.vo.UserInfoVO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends BaseActivity{

	/**
	 * 网络请求key
	 */
	private final int REQUEST_GET_USER_INFO_CODE = 10001;
	private final int REQUEST_UPLOAD_IMAGE_CODE = 10002;
	private final int REQUEST_EDIT_USER_INFO_CODE = 10003;
	
	/**
	 * 修改用户信息key
	 */
	public final static int REQUEST_ACTIVITY_CODE = 99;
	
	private ImageView user_avatar_iv;
	private TextView user_setting_nickname_tv;
	private TextView user_setting_gender_tv;
	private TextView user_setting_verify_check_tv;
	
	/**
	 * 图片管理类
	 */
	private PhotoManager photoMgr;
	private Bitmap bmp;
	
	/**
	 * 圆形图片加载器
	 */
	private DisplayImageOptions circleOptions;
	/**
	 * 用户信息
	 */
	private UserInfoVO vo;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user_info);
	}

	@Override
	public void init() {
		photoMgr = new PhotoManager(this);
		
		circleOptions = new DisplayImageOptions
				.Builder()
				.showImageOnLoading(R.drawable.img_default_horizon)
				.showImageForEmptyUri(R.drawable.img_default_horizon)
				.showImageOnFail(R.drawable.img_default_horizon)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(10))
				.build();
	}

	@Override
	public void initView() {
		user_avatar_iv = (ImageView) findViewById(R.id.user_avatar_iv);
		user_setting_nickname_tv = (TextView) findViewById(R.id.user_setting_nickname_tv);
		user_setting_gender_tv = (TextView) findViewById(R.id.user_setting_gender_tv);
		user_setting_verify_check_tv = (TextView) findViewById(R.id.user_setting_verify_check_tv);
	}

	@Override
	public void initListener() {
		user_avatar_iv.setOnClickListener(this);
	}

	@Override
	public void initValue() {
		requestUserInfo();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	private void setViewsValue() {
		ImageLoader.getInstance().displayImage(vo.getAvatar(), user_avatar_iv, circleOptions, animateFirstListener);
		user_setting_nickname_tv.setText(TextUtils.isEmpty(vo.getNickname()) ? "未设置" : vo.getNickname());
		if(vo.getSex() == 0){
			user_setting_gender_tv.setText("未设置");
		}else{
			user_setting_gender_tv.setText(vo.getSex() == 1 ? "男" : "女");
		}
		user_setting_verify_check_tv.setText(TextUtils.isEmpty(vo.getType()) || TextUtils.isEmpty(vo.getReview()) ? "未认证" : "已认证");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.user_avatar_iv:
			photoMgr.showGetPhotoDialog(this, "avatar.png");
			break;
		case R.id.user_setting_nickname_btn:
			showActivityForResult(context, UserNickNameEditActivity.class, REQUEST_ACTIVITY_CODE, null);
			break;
		case R.id.user_setting_gender_btn:
			showActivityForResult(context, UserGenderEditActivity.class, REQUEST_ACTIVITY_CODE, null);
			break;
		case R.id.user_setting_verify_check_btn:
			Bundle data = new Bundle();
			data.putSerializable("vo", vo);
			showActivityForResult(context, UserIdEditActivity.class, REQUEST_ACTIVITY_CODE, data);
			break;
		case R.id.logout_submit_btn:
			setUserInfo(null);
			clearTask();
			showActivity(context, UserLoginActivity.class);
			break;
		}
	}
	
	/**
	 * 获取用户信息
	 */
	private void requestUserInfo() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("token", getUserInfo().getToken()));
		startHttpRequest(Constants.HTTP_GET, Constants.URL_GET_USER_INFO,
				parameter, true, REQUEST_GET_USER_INFO_CODE);
	}
	
	/**
	 * 上传图片
	 */
	private void requestUploadImage() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("Filedata", photoMgr.getFilePath(), true));
		startHttpRequest(Constants.HTTP_FILE, Constants.URL_UPLOAD_FILE,
				parameter, true, REQUEST_UPLOAD_IMAGE_CODE);
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
			case REQUEST_GET_USER_INFO_CODE:
				brm.parseResponse(resultJson, new TypeToken<UserInfoVO>(){});
				if(brm.isSuccess()){
					vo = (UserInfoVO) brm.getResult();
					vo.setToken(getUserInfo().getToken());
					setUserInfo(vo);
					setViewsValue();
				}
				break;
			case REQUEST_UPLOAD_IMAGE_CODE:
				brm.parseResponse(resultJson, new TypeToken<FileVO>(){});
				if(brm.isSuccess()){
					FileVO fileVO = (FileVO) brm.getResult();
					requestUpdateUserInfoByKey("avatar", fileVO.getFilename());
					vo.setAvatar(fileVO.getImgurl() + File.separator + fileVO.getFilename());
					ImageLoader.getInstance().displayImage(vo.getAvatar(), user_avatar_iv, circleOptions, animateFirstListener);
				}
				break;
			case REQUEST_EDIT_USER_INFO_CODE:
				brm.parseResponse(resultJson, new TypeToken<FileVO>(){});
				if(brm.isSuccess()){
					showToast("修改成功");
					requestUserInfo();
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		releaseBtimap();
		bmp = photoMgr.onActivityResult(this, requestCode, resultCode, data);
		if(bmp != null){
			requestUploadImage();
		}
		if(requestCode == REQUEST_ACTIVITY_CODE && resultCode == RESULT_OK){
			requestUserInfo();
		}
	}
	
	private void releaseBtimap(){
		if(bmp != null && !bmp.isRecycled()){
			bmp.recycle();
			bmp = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		releaseBtimap();
		super.onDestroy();
	}
}
