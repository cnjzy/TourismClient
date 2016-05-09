/** 
 * Project Name:TourismAppClient 
 * File Name:BrandUserFragment.java 
 * Package Name:com.tourism.app.activity.brand.menu 
 * Date:2016年4月6日下午3:23:39 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.brand.menu;  

import java.io.InputStream;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourism.app.R;
import com.tourism.app.base.BaseFragment;

/** 
 * ClassName:BrandUserFragment 
 * Date:     2016年4月6日 下午3:23:39
 * @author   Jzy 
 * @version   
 * @see       
 */
public class BrandUserFragment extends BaseFragment{
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_brand_user, container, false);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initValue() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onClick(View v) {
		System.err.println("===============onClick");
		super.onClick(v);
		switch(v.getId()){
		case R.id.home_tv:
			break;
		}
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		// TODO Auto-generated method stub
		
	}

}
  