package com.tourism.app.activity.user;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.guides.GuidesAddActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.UserInfoVO;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;

public class UserHomeActivity extends BaseActivity {

	private final int REQUEST_GET_USER_INFO_CODE = 10001;

	private ImageView user_icon_iv;
	private TextView user_name_tv;
	private ImageView user_gender_iv;
	private TextView user_notepad_tv;
	private ImageView user_down_arrow_iv;

	/**
	 * 屏幕宽度
	 */
	private int screenWidth = 0;

	/**
	 * Fragment 管理
	 */
	private FragmentManager fm;
	private Fragment showFragment = null;

	/**
	 * 圆形图片加载器
	 */
	private DisplayImageOptions circleOptions;
	
	private UserInfoVO vo;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_user);
	}

	@Override
	public void init() {
		fm = getSupportFragmentManager();
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		
		circleOptions = new DisplayImageOptions
				.Builder()
				.showImageOnLoading(R.drawable.img_default_horizon)
				.showImageForEmptyUri(R.drawable.img_default_horizon)
				.showImageOnFail(R.drawable.img_default_horizon)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new CircleBitmapDisplayer())
				.build();
	}

	@Override
	public void initView() {
		user_icon_iv = (ImageView) findViewById(R.id.user_icon_iv);
		user_name_tv = (TextView) findViewById(R.id.user_name_tv);
		user_gender_iv = (ImageView) findViewById(R.id.user_gender_iv);
		user_notepad_tv = (TextView) findViewById(R.id.user_notepad_tv);
		user_down_arrow_iv = (ImageView) findViewById(R.id.user_down_arrow_iv);
	}

	@Override
	public void initListener() {
		user_icon_iv.setOnClickListener(this);
	}

	@Override
	public void initValue() {
		setNavigationRightButton(View.VISIBLE, 0, R.drawable.btu_tj);
		setDownArrowPoint(0);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		requestUserInfo();
	}
	
	private void setViewsValue(){
		ImageLoader.getInstance().displayImage(vo.getAvatar(), user_icon_iv, circleOptions, animateFirstListener);
		user_name_tv.setText(TextUtils.isEmpty(vo.getNickname()) ? "游客" : vo.getNickname());
		if(vo.getSex() == 0 || vo.getSex() == 3){
			user_gender_iv.setVisibility(View.INVISIBLE);
		}else{
			user_gender_iv.setBackgroundResource(vo.getSex() == 1 ? R.drawable.ico_nan : R.drawable.ico_nv);
		}
		user_notepad_tv.setText(vo.getMid() + "篇游记");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.user_gl_rb:
			setDownArrowPoint(0);
			// if (messageFragment == null) {
			// messageFragment = new MessageFragment();
			// }
			// showFragment(messageFragment);
			break;
		case R.id.user_sc_rb:
			setDownArrowPoint(1);
			break;
		case R.id.user_xh_rb:
			setDownArrowPoint(2);
			break;
		case R.id.user_gd_rb:
			break;
		case R.id.user_icon_iv:
			showActivity(context, UserInfoActivity.class);
			break;
		case R.id.navigation_right_btn:
			showActivity(context, GuidesAddActivity.class);
			break;
		}
	}

	/**
	 * 设置下箭头坐标
	 * 
	 * @param i
	 */
	private void setDownArrowPoint(int i) {
		int itemWidth = screenWidth / 4;
		int marginLeft = itemWidth * i
				+ (itemWidth - user_down_arrow_iv.getWidth()) / 2;
		LinearLayout.LayoutParams arrowParams = (LinearLayout.LayoutParams) user_down_arrow_iv
				.getLayoutParams();
		arrowParams.leftMargin = marginLeft;
		user_down_arrow_iv.setLayoutParams(arrowParams);
	}

	/**
	 * 显示Fragment
	 * 
	 * @param f
	 */
	public void showFragment(Fragment f) {
		FragmentTransaction ft = fm.beginTransaction();
		if (!f.isAdded()) {
			if (showFragment != null) {
				ft.hide(showFragment).add(R.id.content, f);
			} else {
				ft.add(R.id.content, f);
			}
		} else {
			if (showFragment != null) {
				ft.hide(showFragment).show(f);
			} else {
				ft.show(f);
			}
		}
		ft.commit();
		showFragment = f;
	}

	/**
	 * 获取用户信息
	 */
	public void requestUserInfo() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		parameter.add(new RequestParameter("token", getUserInfo().getToken()));
		startHttpRequest(Constants.HTTP_GET, Constants.URL_GET_USER_INFO,
				parameter, true, REQUEST_GET_USER_INFO_CODE);
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
					setViewsValue();
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
