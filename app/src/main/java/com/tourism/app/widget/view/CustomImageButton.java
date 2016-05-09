package com.tourism.app.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class CustomImageButton extends ImageButton {

	private String text = null; // 要显示的文字
	private int color; // 文字的颜色

	public CustomImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();

		for (int state : getDrawableState()) {
			if (state == android.R.attr.state_focused || state == android.R.attr.state_pressed || state == android.R.attr.state_selected) {
				getBackground().setAlpha((int) (255 * 0.7f));
				return;
			}
		}
		getBackground().setAlpha(255);
		return;
	}

	public void setText(String text) {
		this.text = text; // 设置文字
	}

	public void setTextColor(int color) {
		this.color = color; // 设置文字颜色
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 单元测试使用
		if(canvas == null){
			canvas = new Canvas();
		}
		Paint paint = new Paint();
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setColor(color);
//		canvas.drawText(text, 15, 20, paint); // 绘制文字
	}
}
