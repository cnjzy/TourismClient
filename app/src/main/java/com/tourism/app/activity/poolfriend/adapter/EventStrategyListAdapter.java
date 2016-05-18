/** 
 * Project Name:TourismAppClient 
 * File Name:EventListAdapter.java 
 * Package Name:com.tourism.app.activity.poolfriend.adapter 
 * Date:2016年4月27日下午3:37:21 
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved. 
 * 
 */

package com.tourism.app.activity.poolfriend.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.StrategyVO;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;

/**
 * ClassName:EventListAdapter Date: 2016年4月27日 下午3:37:21
 * 
 * @author Jzy
 * @version
 * @see
 */
public class EventStrategyListAdapter extends ETSBaseAdapter {

	public DisplayImageOptions bgOptions;
	public DisplayImageOptions circleOptions;

	public EventStrategyListAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);

		bgOptions = new DisplayImageOptions
				.Builder()
				.showImageOnLoading(R.drawable.img_default_horizon)
				.showImageForEmptyUri(R.drawable.img_default_horizon)
				.showImageOnFail(R.drawable.img_default_horizon)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(10))
				.build();
		
		circleOptions = new DisplayImageOptions
				.Builder()
				.showImageOnLoading(R.drawable.img_default_horizon)
				.showImageForEmptyUri(R.drawable.img_default_horizon)
				.showImageOnFail(R.drawable.img_default_horizon)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.displayer(new CircleBitmapDisplayer())
				.build();
	}

	@Override
	public void loadData() {
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_search_list, parent, false);
		}
		
		
		ImageView item_bg_iv = ViewHolderUtil.get(convertView, R.id.item_bg_iv);
		ImageView item_new_iv = ViewHolderUtil.get(convertView, R.id.item_new_iv);
		TextView item_price_tv = ViewHolderUtil.get(convertView, R.id.item_price_tv);
		TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);
		TextView item_date_tv = ViewHolderUtil.get(convertView, R.id.item_date_tv);
		ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
		TextView item_vip_tv = ViewHolderUtil.get(convertView, R.id.item_vip_tv);

		StrategyVO vo = (StrategyVO) getItem(position);
		if(vo != null){
			ImageLoader.getInstance().displayImage(vo.getLitpic(), item_bg_iv, bgOptions, animateFirstListener);
			ImageLoader.getInstance().displayImage(vo.getUser_avatar(), item_icon_iv, circleOptions, animateFirstListener);
			
			item_new_iv.setVisibility(View.INVISIBLE);
			item_price_tv.setVisibility(View.INVISIBLE);
			item_name_tv.setText(vo.getName());
			item_date_tv.setText(vo.getStart_date().replaceAll("-", "."));
			item_vip_tv.setText(vo.getUser_type() == 1 ? "V友" : "V企");
			
		}
		
		return convertView;
	}

}
