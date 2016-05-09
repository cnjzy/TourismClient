package com.tourism.app.util;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.tourism.app.R;
import com.tourism.app.activity.FeedbackActivity;
import com.tourism.app.activity.SettingActivity;
import com.tourism.app.base.BaseActivity;

public class PopupWindowsUtils {
	private static PopupWindowsUtils share;

	public synchronized static PopupWindowsUtils getInstance() {
		if (share == null) {
			share = new PopupWindowsUtils();
		}
		return share;
	}

	/**
	 * 获取首页菜单
	 * 
	 * @param act
	 * @param auther
	 * @return
	 */
	public static PopupWindow getHomeMenu(final Activity act, View auther) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(act).inflate(
				R.layout.pop_home_menu, null);
		// 设置按钮的点击事件
		Button button1 = (Button) contentView.findViewById(R.id.menu_1_btn);
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BaseActivity.showActivity(act, FeedbackActivity.class);
			}
		});
		
		Button button2 = (Button) contentView.findViewById(R.id.menu_2_btn);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				BaseActivity.showActivity(act, SettingActivity.class);
			}
		});

		final PopupWindow popupWindow = new PopupWindow(contentView,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.i("mengdd", "onTouch : ");
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		// 设置好参数之后再show
		popupWindow.showAsDropDown(auther);

		return popupWindow;
	}
}
