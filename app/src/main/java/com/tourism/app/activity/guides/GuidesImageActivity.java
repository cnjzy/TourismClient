package com.tourism.app.activity.guides;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.DeviceUtil;
import com.tourism.app.util.ViewUtil;

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
					ViewUtil.controlKeyboardLayout(reply_ll, reply_bottom_ll);
				}
			}, 100);
			break;
		}
	}
}
