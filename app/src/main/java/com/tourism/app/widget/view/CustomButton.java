package com.tourism.app.widget.view;

import java.math.BigDecimal;

import com.tourism.app.util.NumberUtils;
import com.tourism.app.util.ViewUtil;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button{
	
	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init(){
	    if (ViewUtil.isSpecialMode(getContext())) {
            int size = (int) NumberUtils.round(getTextSize()
                    / getContext().getResources().getDisplayMetrics().scaledDensity, 0, BigDecimal.ROUND_HALF_UP);
            size += 4;
            ViewUtil.setSpecialModeTextSize(getContext(), this, size);
        }
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
				getBackground().setAlpha((int)(255*0.7f));
				return;
			}
		}
		getBackground().setAlpha(255);
		return;
	}
}
