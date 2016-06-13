package com.tourism.app.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.webkit.WebView;

public class MyWebView extends WebView {

	private boolean isBottom = false;
	private boolean isTop = false;
	private boolean isScrollUp = false;
	private int lastT = 0;
	
	private GestureDetector mGestureDetector;
	
	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		
//		Log.i("onScrollChanged url:", " t" + t);
		if(t == 0){
			isTop = true;
		}else{
			isTop = false;
		}
		lastT = t;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		mGestureDetector.onTouchEvent(event);
		// 在scrollview底部，不在webview顶部的时候滑动
		// 在scrollview底部，并且向下滑动的时候滑动
		Log.i("onTouchEvent url:", " isBottom" + isBottom + " isTop:" + isTop + " isScrollDown:" + isScrollUp + " contentHeight:" + getContentHeight() + " height:" + getHeight());
		if(((isBottom && !isTop) || (isScrollUp && isBottom)) && getContentHeight() > getHeight()){
			requestDisallowInterceptTouchEvent(true);
		}else{
			requestDisallowInterceptTouchEvent(false);
		}
        return super.onTouchEvent(event);
    } 

	@Override
	public boolean onDragEvent(DragEvent event) {
		return super.onDragEvent(event);
	}
	
	public void onBottom(boolean isBottom){
		this.isBottom = isBottom;
		this.isScrollUp = true;
	}
	
	public void isScrollUp(boolean isScrollUp){
		this.isScrollUp = isScrollUp;
	}
	

	// Return false if we're scrolling in the x direction
    class YScrollDetector extends SimpleOnGestureListener {
    	@Override
    	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//    		Log.i("YScrollDetector url:", " distanceY:" + distanceY);
    		if (isTop && distanceY < 0) {
    			isScrollUp = false;
            	return true;
            }
            return false;
    	}
    }
}
