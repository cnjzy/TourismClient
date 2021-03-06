/** 
 * Project Name:TourismAppClient 
 * File Name:BrandListAdapter.java 
 * Package Name:com.tourism.app.activity.brand.adapter 
 * Date:2016年4月5日下午4:33:15 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
*/  
  
package com.tourism.app.activity.poolfriend.adapter;  

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.activity.brand.BrandInfoActivity;
import com.tourism.app.activity.poolfriend.CategoryListActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.CategoryVO;
import com.tourism.app.vo.NewsVO;

/** 
 * ClassName:BrandListAdapter
 * Date:     2016年4月5日 下午4:33:15
 * @author   Jzy 
 * @version  
 * @see       
 */
public class FriendListAdapter extends ETSBaseAdapter {

	public FriendListAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getCount() {
		return dataList.size() % 2 == 1 ? dataList.size() / 2 + 1 : dataList.size() / 2;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_brand_list, parent, false);
		}
		
		View item_left_layout = ViewHolderUtil.get(convertView, R.id.item_left_layout);
		TextView item_left_name_tv = ViewHolderUtil.get(convertView, R.id.item_left_name_tv);
		ImageView item_left_icon_iv = ViewHolderUtil.get(convertView, R.id.item_left_icon_iv);
		View item_right_layout = ViewHolderUtil.get(convertView, R.id.item_right_layout);
		TextView item_right_name_tv = ViewHolderUtil.get(convertView, R.id.item_right_name_tv);
		ImageView item_right_icon_iv = ViewHolderUtil.get(convertView, R.id.item_right_icon_iv);

		item_left_name_tv.setVisibility(View.GONE);
		item_right_name_tv.setVisibility(View.GONE);

		final CategoryVO.Category vo1 = (CategoryVO.Category) getItem(position*2);
		if(vo1 != null){
			item_left_layout.setVisibility(View.VISIBLE);
			item_left_name_tv.setText(vo1.getName());
			ImageLoader.getInstance().displayImage(vo1.getLitpic(), item_left_icon_iv, options, animateFirstListener);
			item_left_layout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (vo1.getName().equals("斯巴鲁专区")){
						NewsVO newsVO = new NewsVO();
						newsVO.setLink("http://api.yxj.76iw.com/v1/brand/details?id=5");
						Bundle data = new Bundle();
						data.putSerializable("vo", newsVO);
						BaseActivity.showActivity(context, BrandInfoActivity.class, data);
					}else{
						Bundle data = new Bundle();
						data.putSerializable("vo", vo1);
						BaseActivity.showActivity(context, CategoryListActivity.class, data);
					}
				}
			});
		}else{
			item_left_layout.setOnClickListener(null);
			item_left_layout.setVisibility(View.INVISIBLE);
		}
		
		final CategoryVO.Category vo2 = (CategoryVO.Category) getItem(position*2 + 1);
		if(vo2 != null){
			item_right_layout.setVisibility(View.VISIBLE);
			item_right_name_tv.setText(vo2.getName());
			ImageLoader.getInstance().displayImage(vo2.getLitpic(), item_right_icon_iv, options, animateFirstListener);
			item_right_layout.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (vo2.getName().equals("斯巴鲁专区")){
						NewsVO newsVO = new NewsVO();
						newsVO.setLink("http://api.yxj.76iw.com/v1/brand/details?id=5");
						Bundle data = new Bundle();
						data.putSerializable("vo", newsVO);
						BaseActivity.showActivity(context, BrandInfoActivity.class, data);
					}else {
						Bundle data = new Bundle();
						data.putSerializable("vo", vo2);
						BaseActivity.showActivity(context, CategoryListActivity.class, data);
					}
				}
			});
		}else{
			item_right_layout.setOnClickListener(null);
			item_right_layout.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}
}
  