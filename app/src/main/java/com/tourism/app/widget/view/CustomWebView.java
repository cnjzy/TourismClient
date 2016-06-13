package com.tourism.app.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by Jzy on 16/6/7.
 */
public class CustomWebView extends WebView {
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        super.onScrollChanged(l, t, oldl, oldt);

        Log.i("onScrollChanged url", l + " " + t + " " + oldl + " " + oldt);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        super.onSizeChanged(w, h, ow, oh);

        Log.i("onSizeChanged url", w + " " + h + " " + ow + " " + oh);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        Log.i("drawableStateChanged url", "======================");
    }

}
