package com.tourism.app.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.tourism.app.R;

/**
 * Created by lee on 16-5-6.
 *
 * @author lee
 *         自定义progressbar
 */
public class CustomRoundProgress extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 圆环的颜色
	 */
	private int roundColor;

	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;

	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;

	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;

	/**
	 * 圆环的宽度
	 */
	private float roundWidth;

	/**
	 * 最大进度
	 */
	private int maxProgress;

	/**
	 * 当前进度
	 */
	private int progress = 0;

	/**
	 * 上下文
	 */
	private Context context;

	/**
	 * 状态回调接口监听器
	 */
	private onProgressListener mProgressListener;

	public CustomRoundProgress(Context context) {
		this(context, null);
	}

	public CustomRoundProgress(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomRoundProgress(Context context, AttributeSet attrs,
	                  int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		paint = new Paint();

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperProgressWheel);

		roundColor = mTypedArray.getColor(R.styleable.SuperProgressWheel_roundColor, Color.BLUE);
		roundProgressColor = mTypedArray.getColor(R.styleable.SuperProgressWheel_roundProgressColor, Color.GRAY);
		textColor = mTypedArray.getColor(R.styleable.SuperProgressWheel_textColor, Color.BLACK);
		textSize = mTypedArray.getDimension(R.styleable.SuperProgressWheel_textSize, 45);
		roundWidth = mTypedArray.getDimension(R.styleable.SuperProgressWheel_roundWidth, 35);

		mTypedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/**
		 * 画初始的大圆环
		 */
		int centre = getWidth() / 2; //获取圆心的x坐标
		int radius = (int) (centre - roundWidth / 2); //圆环的半径
		paint.setColor(roundColor); //设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); //设置画笔类型
		paint.setStrokeWidth(roundWidth); //设置圆环的宽度
		paint.setAntiAlias(true);  //消除锯齿
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

		canvas.drawArc(oval, -90, 360, false, paint);

		/**
		 * 画进度圆环
		 */
		paint.setColor(roundProgressColor);
		for (int i = 0; i < progress; i++) {
			canvas.drawArc(oval, -90, 36 * progress / 10, false, paint);
		}

		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
		int percent = (int) (((float) progress / (float) maxProgress) * 100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
		float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
		canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize / 2, paint); //画出进度百分比

	}

	public synchronized int getProgress() {
		return progress;
	}

	public synchronized void setProgress(int progress) {
		if (progress < maxProgress) {
			this.progress = progress;
			postInvalidate();
		} else {
			this.progress = maxProgress;
			postInvalidate();
			mHandler.sendEmptyMessage(1);
		}
	}

	public int getRoundColor() {
		return roundColor;
	}

	public void setRoundColor(int roundColor) {
		this.roundColor = roundColor;
	}

	public int getRoundProgressColor() {
		return roundProgressColor;
	}

	public void setRoundProgressColor(int roundProgressColor) {
		this.roundProgressColor = roundProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
	}

	public void setOnProgressListener(onProgressListener mProgressListener) {
		this.mProgressListener = mProgressListener;
	}

	public interface onProgressListener {
		void onCompleted(View v);
	}

	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 1:
					mProgressListener.onCompleted(CustomRoundProgress.this);
					break;
			}
		}
	};
}
