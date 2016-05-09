package com.tourism.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.tourism.app.R;
import com.tourism.app.activity.user.UserLoginActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.preference.Preferences;

public class SplashActivity extends BaseActivity {
	private ImageView ivWelcome;
	private AlphaAnimation animation = new AlphaAnimation(1.0f, 1.0f);

	private void showNextActivity() {
		if(getUserInfo() == null){
			showActivity(context, UserLoginActivity.class);
		}else{
			showActivity(context, MainActivity.class);
		}
		finish();
	}

	@Override
	public void init() {
		animation.setDuration(1500);
		animation.setFillAfter(true);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				showNextActivity();
			}
		});
	}

	@Override
	public void initView() {
		ivWelcome = (ImageView) findViewById(R.id.iv_welcome);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initValue() {
		ivWelcome.startAnimation(animation);
	}

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_splash);
	}

}
