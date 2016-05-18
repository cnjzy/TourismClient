/**
 * Project Name:TourismAppClient
 * File Name:BrandListAdapter.java
 * Package Name:com.tourism.app.activity.brand.adapter
 * Date:2016年4月5日下午4:33:15
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 */

package com.tourism.app.activity.brand.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.NewsVO;

/**
 * ClassName:BrandListAdapter
 * Date:     2016年4月5日 下午4:33:15
 * @author Jzy
 * @version
 * @see
 */
public class BrandListAdapter extends ETSBaseAdapter {

    public DisplayImageOptions bgOptions;

    public BrandListAdapter(BaseActivity context, AbsListView listView) {
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
    }

    @Override
    public void loadData() {
        // TODO Auto-generated method stub

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_brand_hroiz_list, parent, false);
        }

        ImageView item_bg_iv = ViewHolderUtil.get(convertView, R.id.item_bg_iv);

        final NewsVO vo1 = (NewsVO) getItem(position);
        if (vo1 != null) {
            ImageLoader.getInstance().displayImage(vo1.getLitpic(), item_bg_iv, bgOptions, animateFirstListener);
        }

        return convertView;
    }
}
  