package com.tourism.app.activity;

import android.view.View;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.NumberUtils;

import java.io.File;
import java.math.BigDecimal;

public class SettingActivity extends BaseActivity{

	private TextView cache_size_tv;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_setting);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		cache_size_tv = (TextView) findViewById(R.id.cache_size_tv);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initValue() {
		cache_size_tv.setText(NumberUtils.round(getDirSize(ImageLoader.getInstance().getDiskCache().getDirectory()), 2, BigDecimal.ROUND_HALF_UP) + "M");
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
			case R.id.clear_cache_btn:
				ImageLoader.getInstance().clearDiskCache();
				showToast("清除成功");
				cache_size_tv.setText("0M");
				break;
			case R.id.logout_btn:
				setUserInfo(null);
				finish();
				break;
		}
	}

	public static double getDirSize(File file) {
		//判断文件是否存在
		if (file.exists()) {
			//如果是目录则递归计算其内容的总大小
			if (file.isDirectory()) {
				File[] children = file.listFiles();
				double size = 0;
				for (File f : children)
					size += getDirSize(f);
				return size;
			} else {//如果是文件则直接返回其大小,以“兆”为单位
				double size = (double) file.length() / 1024 / 1024;
				return size;
			}
		} else {
			System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
			return 0.0;
		}
	}
}
