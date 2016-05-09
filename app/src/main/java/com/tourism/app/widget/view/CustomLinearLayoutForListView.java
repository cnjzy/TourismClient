/**
 * 
 */
package com.tourism.app.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

/**
 * @ClassName: CustomLinearLayoutForListView
 * @Description: TODO
 * @author sr
 * @date 2014-12-29 下午4:36:37
 */
public class CustomLinearLayoutForListView extends LinearLayout {

	public CustomLinearLayoutForListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomLinearLayoutForListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	private BaseAdapter adapter;
	private OnClickListener onClickListener = null;

	/**
	 * 绑定布局
	 */
	public void setAdapter(BaseAdapter baseAdapter) {
		adapter = baseAdapter;
	}

	public void notifyDataSetUpdate() {
		this.removeAllViews();
		if (adapter != null) {
			int count = adapter.getCount();
			for (int i = 0; i < count; i++) {
				View v = adapter.getView(i, null, null);

				v.setOnClickListener(this.onClickListener);
				addView(v, i);
			}
		}
	}

}
