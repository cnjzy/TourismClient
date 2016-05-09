package com.tourism.app.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImageView extends ImageView{
	private OnClickStateChangeListener onClickStateChangeListener;
    
	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
	}
	
	@Override
	protected void drawableStateChanged () {
		super.drawableStateChanged ();
		
		for(int state :getDrawableState()){
			if (state == android.R.attr.state_focused || state == android.R.attr.state_pressed
					|| state == android.R.attr.state_selected) {
				if(onClickStateChangeListener != null)
                    onClickStateChangeListener.clickStateChange(true);
				return;
			}
		}
		if(onClickStateChangeListener != null)
            onClickStateChangeListener.clickStateChange(false);
		return;
	}
	
	public OnClickStateChangeListener getOnClickStateChangeListener() {
        return onClickStateChangeListener;
    }

    public void setOnClickStateChangeListener(OnClickStateChangeListener onClickStateChangeListener) {
        this.onClickStateChangeListener = onClickStateChangeListener;
    }

    public interface OnClickStateChangeListener{
	    public void clickStateChange(boolean state);
	}
}
