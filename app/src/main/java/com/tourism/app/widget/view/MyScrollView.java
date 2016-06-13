package com.tourism.app.widget.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	private GestureDetector mGestureDetector;
	private OnTouchListener mGestureListener;
	private MyWebView webview;
    
    private boolean isBottom = false;

    private Handler handler = new Handler();
    private ScrollLinstenerRunnable scrollLinstenerRunnable = new ScrollLinstenerRunnable();
    private long delayed = 300;

    class ScrollLinstenerRunnable implements Runnable{
        @Override
        public void run() {
            checkBottom();
            handler.postDelayed(scrollLinstenerRunnable, delayed);
        }
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);

        handler.postDelayed(scrollLinstenerRunnable, delayed);
    }

    private void checkBottom(){
        View view = (View)getChildAt(getChildCount()-1);
        int d = view.getBottom();
        d -= (getHeight()+getScrollY());
        if(d==0) {
            //you are at the end of the list in scrollview
            //do what you wanna do here
            isBottom = true;
        }else{
            isBottom = false;
        }
        webview.onBottom(isBottom);
    }

    public void onDesctory(){
        handler.removeCallbacks(scrollLinstenerRunnable);
    }
    
    public void setWebview(MyWebView webview) {
		this.webview = webview;
	}
    
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
    	super.onScrollChanged(l, t, oldl, oldt);

        checkBottom();
//    	Log.i("onScrollChanged url:", " d:" + d);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	mGestureDetector.onTouchEvent(ev);
        checkBottom();
        return super.onInterceptTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    class YScrollDetector extends SimpleOnGestureListener {
    	@Override
    	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//    		Log.i("YScrollDetector url:", " distanceY:" + distanceY);
    		if (Math.abs(distanceY) > Math.abs(distanceX) && distanceY > 0) {
            	webview.isScrollUp(true);
            	return true;
            }
            webview.isScrollUp(false);
            return false;
    	}
    }
}