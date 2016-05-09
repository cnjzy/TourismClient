package com.tourism.app.activity.user;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tencent.connect.UserInfo;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.util.PhotoManager;
import com.tourism.app.vo.FileVO;
import com.tourism.app.vo.UserInfoVO;

public class UserIdEditActivity extends BaseActivity {

	/**
	 * 网络请求key
	 */
	private final int REQUEST_SET_USER_AUTH_CODE = 10002;
	private final int REQUEST_UPLOAD_IMAGE_CODE = 10001;

	private RadioButton acction_type_person_rb;
	private ImageView user_auth_idcard_img_iv;

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
	private UserInfoVO userInfoVO;
	
	/**
	 * 文件信息
	 */
	private FileVO fileVO;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user_id_auth);
	}

	@Override
	public void init() {
		userInfoVO = (UserInfoVO) getIntent().getExtras().getSerializable("vo");
		
		photoMgr = new PhotoManager(this);

		circleOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.img_default_horizon)
				.showImageForEmptyUri(R.drawable.img_default_horizon)
				.showImageOnFail(R.drawable.img_default_horizon)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(10)).build();
	}

	@Override
	public void initView() {
		acction_type_person_rb = (RadioButton) findViewById(R.id.acction_type_person_rb);
		user_auth_idcard_img_iv = (ImageView) findViewById(R.id.user_auth_idcard_img_iv);
	}

	@Override
	public void initListener() {
		user_auth_idcard_img_iv.setOnClickListener(this);
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.user_auth_idcard_img_iv:
			photoMgr.showGetPhotoDialog(this, "auth.png");
			break;
		case R.id.user_auth_submit_btn:
			int type = acction_type_person_rb.isChecked() ? 1 : 2;
			if(bmp == null){
				showToast("请选择认证图片");
			}else{
				requestUploadUserAuth(String.valueOf(type), fileVO.getFilename());
			}
			break;
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
	}

	/**
	 * 上传用户认证信息
	 */
	private void requestUploadUserAuth(String type, String fileName) {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("token", getUserInfo().getToken()));
		parameter.add(new RequestParameter("type", type));
		parameter.add(new RequestParameter("review", fileName, true));
		startHttpRequest(Constants.HTTP_POST, Constants.URL_USER_REVIEW,
				parameter, true, REQUEST_SET_USER_AUTH_CODE);
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


	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		super.onCallbackFromThread(resultJson, resultCode);
		try {
			BaseResponseMessage brm = new BaseResponseMessage();
			switch (resultCode) {
			case REQUEST_SET_USER_AUTH_CODE:
				brm.parseResponse(resultJson, new TypeToken<FileVO>() {
				});
				if (brm.isSuccess()) {
					showToast("修改成功");
					setResult(RESULT_OK);
					finish();
				}
				break;
			case REQUEST_UPLOAD_IMAGE_CODE:
				brm.parseResponse(resultJson, new TypeToken<FileVO>(){});
				if(brm.isSuccess()){
					fileVO = (FileVO) brm.getResult();
					user_auth_idcard_img_iv.setImageBitmap(bmp);
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
