package com.tourism.app.util;  

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tourism.app.R;


/**
 * @ClassName: ESTAnimationUtil
 * @Description: TODO 动画工具类
 * @author jzy
 * @date 2014-12-18 下午6:15:06
 * 
 */
public class ETSAnimationUtil {

	/**
	 * 设置曲线动画
	 * @param activity 当前Activity对象
	 * @param author	依赖View用于获取起始坐标位置
	 * @param moveView	移动的View(购物车小圆球)
	 * @param endView	到达的View（购物车图标）
	 * @param animationListener	动画监听（需要注意，start需要显示moveView，end需要隐藏moveView）
	 */
	public static void setCurveAnimation(Activity activity, View author, View moveView, View endView, AnimationListener animationListener){
		// 获取初始移动View的屏幕坐标
		int[] start_location = new int[2];
		author.getLocationInWindow(start_location);
		// 创建动画层
		ViewGroup anim_mask_layout = createAnimLayout(activity);
		//把动画小球添加到动画层
		anim_mask_layout.addView(moveView);
		final View view = addViewToAnimLayout(anim_mask_layout, moveView, start_location);
		// 获取到达位置坐标
		int[] end_location = new int[2];
		endView.getLocationInWindow(end_location);
		// 获取中心点
		int moveViewSize = activity.getResources().getDimensionPixelSize(R.dimen.global_padding_mid2);
		end_location[1] += (endView.getHeight()-moveViewSize)/2;

		// 计算位移
		int endX = 0 - start_location[0] + (endView.getWidth()+moveViewSize) / 2;// 动画位移的X坐标
		int endY = end_location[1] - start_location[1];// 动画位移的y坐标
		TranslateAnimation translateAnimationX = new TranslateAnimation(0, endX, 0, 0);
		translateAnimationX.setInterpolator(new LinearInterpolator());
		translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
		translateAnimationY.setInterpolator(new AccelerateInterpolator());
		translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
		translateAnimationX.setFillAfter(true);

		AnimationSet set = new AnimationSet(false);
		set.setFillAfter(false);
		set.addAnimation(translateAnimationY);
		set.addAnimation(translateAnimationX);
		set.setDuration(500);// 动画的执行时间
		view.startAnimation(set);
		// 动画监听事件
		set.setAnimationListener(animationListener);
	}
	
	/**
	 * 创建动画层
	 * @param activity
	 * @return
	 */
	private static ViewGroup createAnimLayout(Activity activity) {
		ViewGroup rootView = (ViewGroup) activity.getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(activity);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setId(Integer.MAX_VALUE);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}
	
	/**
	 * 添加移动View到动画层布局
	 * @param vg
	 * @param view
	 * @param location
	 * @return
	 */
	private static View addViewToAnimLayout(final ViewGroup vg, final View view,
			int[] location) {
		int x = location[0];
		int y = location[1];
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		view.setLayoutParams(lp);
		return view;
	}
	
	/**
	 * 执行购物车图标动画
	 * @param context
	 * @param view
	 */
	public static void startShopCarViewAnim(Context context, View view){
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.shop_car_scale);
		view.startAnimation(anim);
	}
	
	/**
	 * 加载图片背景
	 */
	private static int[] loadingImageBackgrouds = {R.drawable.takeout_icon_round_all, R.drawable.takeout_icon_round_fu, R.drawable.takeout_icon_round_juan};
	/**
	 * 开始执行加载动画
	 * @param img1
	 * @param img2
	 * @param img3
	 */
	public static void startLoadingAnim(final ImageView img1, final  ImageView img2, final ImageView img3){
		TranslateAnimation t1 = new TranslateAnimation(0, 100, 0, 0);
		TranslateAnimation t2 = new TranslateAnimation(0, -100, 0, 0);
		
		t1.setDuration(500);
		t1.setRepeatCount(-1);
		t1.setRepeatMode(Animation.REVERSE);
		t1.setInterpolator(new DecelerateInterpolator());
		t1.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation arg0) {
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				if(img1.getTag() == null)
					img1.setTag(0);
				int m = (Integer) img1.getTag();
				m++;
				img1.setTag(m);
				if(m % 2 == 0){
					img1.setBackgroundResource(loadingImageBackgrouds[0]);
					img2.setBackgroundResource(loadingImageBackgrouds[1]);
					img3.setBackgroundResource(loadingImageBackgrouds[2]);
					int c = loadingImageBackgrouds[0];
					loadingImageBackgrouds[0] = loadingImageBackgrouds[1];
					loadingImageBackgrouds[1] = loadingImageBackgrouds[2];
					loadingImageBackgrouds[2] = c;
				}
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
			}
		});
		
		t2.setDuration(500);
		t2.setRepeatCount(-1);
		t2.setRepeatMode(Animation.REVERSE);
		t2.setInterpolator(new DecelerateInterpolator());
		
		img1.startAnimation(t1);
		img2.startAnimation(t2);
	}
}
 