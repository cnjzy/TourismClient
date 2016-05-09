package com.tourism.app.activity.guides;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.DeviceUtil;

public class GuidesImageActivity extends BaseActivity {

	private View reply_ll;
	private View reply_bottom_ll;
	private EditText guides_reply_et;
	private TextView guides_reply_count_tv;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_guides_image);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initView() {
		reply_ll = findViewById(R.id.reply_ll);
		reply_bottom_ll = reply_ll.findViewById(R.id.reply_bottom_ll);
		guides_reply_et = (EditText) reply_ll.findViewById(R.id.guides_reply_et);
		guides_reply_count_tv = (TextView) reply_ll.findViewById(R.id.guides_reply_count_tv);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initValue() {
		reply_ll.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.guides_add_reply_btn:
			reply_ll.setVisibility(View.VISIBLE);
			mHandler.postDelayed(new Runnable() {
				public void run() {
					DeviceUtil.showIMM(context, guides_reply_et);
					controlKeyboardLayout(reply_ll, reply_bottom_ll);
				}
			}, 100);
			break;
		}
	}

	/**
	 * @param root
	 *            最外层布局，需要调整的布局
	 * @param scrollToView
	 *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
	 */
	private void controlKeyboardLayout(final View root, final View scrollToView) {
		root.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				// 获取root在窗体的可视区域
				root.getWindowVisibleDisplayFrame(rect);
				// 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
				int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
				// 若不可视区域高度大于100，则键盘显示
				if (rootInvisibleHeight > 100) {
					int[] location = new int[2];
					// 获取scrollToView在窗体的坐标
					scrollToView.getLocationInWindow(location);
					// 计算root滚动高度，使scrollToView在可见区域
					int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
					root.scrollTo(0, srollHeight);
				} else {
					// 键盘隐藏
					root.scrollTo(0, 0);
				}
			}
		});
	}
}
