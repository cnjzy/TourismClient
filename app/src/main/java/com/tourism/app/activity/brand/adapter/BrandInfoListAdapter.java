/** 
 * Project Name:TourismAppClient 
 * File Name:BrandListAdapter.java 
 * Package Name:com.tourism.app.activity.brand.adapter 
 * Date:2016年4月5日下午4:33:15 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.brand.adapter;  

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.web.WebViewActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.BrandInfoVO;

/** 
 * ClassName:BrandListAdapter
 * Date:     2016年4月5日 下午4:33:15
 * @author   Jzy 
 * @version  
 * @see       
 */
public class BrandInfoListAdapter extends ETSBaseAdapter {
	private BrandInfoVO vo;
	
	public BrandInfoListAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_brand_info_list, parent, false);
		}
		
		ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
		Button item_make_maintenance_btn = ViewHolderUtil.get(convertView, R.id.item_make_maintenance_btn);
		Button item_make_test_drive_btn = ViewHolderUtil.get(convertView, R.id.item_make_test_drive_btn);
		
		ImageLoader.getInstance().displayImage(dataList.get(position).toString(), item_icon_iv, options, animateFirstListener);
		item_make_maintenance_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(vo != null){
					WebViewActivity.show(context, vo.getMaintain(), context.getString(R.string.brand_make_maintenance));
				}
			}
		});
		item_make_test_drive_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(vo != null){
					WebViewActivity.show(context, vo.getTestdrive(), context.getString(R.string.brand_make_test_drive));
				}
			}
		});
		
		return convertView;
	}
	
	public void setVo(BrandInfoVO vo) {
		this.vo = vo;
	}
}
  