/**
 * Project Name:TourismAppClient
 * File Name:StrategyListAdapter.java
 * Package Name:com.tourism.app.activity.poolfriend.adapter
 * Date:2016年4月27日下午3:38:06
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 */

package com.tourism.app.activity.poolfriend.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tourism.app.R;
import com.tourism.app.activity.poolfriend.StrategyInfoActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.util.ViewUtil;
import com.tourism.app.vo.StrategyVO;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:StrategyListAdapter
 * Date:     2016年4月27日 下午3:38:06
 *
 * @author Jzy
 * @see
 */
public class StrategyListAdapter extends ETSBaseAdapter {

    public DisplayImageOptions bgOptions;
    public DisplayImageOptions circleOptions;
    private StrategyVO strategyVO;
    private Map<Integer, Integer> likeMap = new HashMap<Integer, Integer>();

    private float screenWidth = 0;
    private LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    public StrategyListAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);

        screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();

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

    public void setStrategyVO(StrategyVO strategyVO) {
        this.strategyVO = strategyVO;
        for (int i = 0; i < strategyVO.getCurrent_user_like_photos().size(); i++){
            likeMap.put(strategyVO.getCurrent_user_like_photos().get(i), strategyVO.getCurrent_user_like_photos().get(i));
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_event_strategy_list, parent, false);
        }

        View item_day_ll = ViewHolderUtil.get(convertView, R.id.item_day_ll);
        TextView item_day_title_tv = ViewHolderUtil.get(convertView, R.id.item_day_title_tv);
        TextView item_day_name_tv = ViewHolderUtil.get(convertView, R.id.item_day_name_tv);

        View item_content_ll = ViewHolderUtil.get(convertView, R.id.item_content_ll);
        View item_top_location_ll = ViewHolderUtil.get(convertView, R.id.item_top_location_ll);
        ImageView item_location_icon_iv = ViewHolderUtil.get(convertView, R.id.item_location_icon_iv);
        TextView item_location_name_tv = ViewHolderUtil.get(convertView, R.id.item_location_name_tv);

        TextView item_day_content_tv = ViewHolderUtil.get(convertView, R.id.item_day_content_tv);
        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        TextView item_image_content_tv = ViewHolderUtil.get(convertView, R.id.item_image_content_tv);

        View item_bottom_location_ll = ViewHolderUtil.get(convertView, R.id.item_bottom_location_ll);
        TextView item_bottom_location_name_tv = ViewHolderUtil.get(convertView, R.id.item_bottom_location_name_tv);
        final TextView item_like_btn = ViewHolderUtil.get(convertView, R.id.item_like_btn);

        ImageView item_line_iv = ViewHolderUtil.get(convertView, R.id.item_line_iv);

        final StrategyVO vo = (StrategyVO) getItem(position);

        if (vo != null) {
            if (vo.isDate()) {
                item_content_ll.setVisibility(View.GONE);
                item_day_ll.setVisibility(View.VISIBLE);
                item_day_title_tv.setText("DAY" + vo.getDay());
                item_day_name_tv.setText(vo.getTrip_date());
                item_line_iv.setVisibility(View.GONE);
            } else {
                StrategyVO nextVO = (StrategyVO) getItem(position + 1);
                if (nextVO != null && nextVO.isDate()) {
                    item_line_iv.setVisibility(View.GONE);
                } else {
                    item_line_iv.setVisibility(View.VISIBLE);
                }

                item_content_ll.setVisibility(View.VISIBLE);
                item_day_ll.setVisibility(View.GONE);

                if (TextUtils.isEmpty(vo.getLocation_name())) {
                    item_top_location_ll.setVisibility(View.GONE);
                    item_bottom_location_name_tv.setVisibility(View.INVISIBLE);
                } else {
                    item_top_location_ll.setVisibility(View.VISIBLE);
                    item_bottom_location_name_tv.setVisibility(View.VISIBLE);

                    item_location_name_tv.setText(vo.getLocation_name());
                    item_bottom_location_name_tv.setText(vo.getLocation_name());
                }

                if (vo.getPhoto() == null) {
                    item_like_btn.setVisibility(View.GONE);
                } else {
                    item_like_btn.setVisibility(View.VISIBLE);
                    item_like_btn.setText(vo.getPhoto().getLike_count() + "");
                    ViewUtil.setViewLeftDrawableAndTextColor(context, item_like_btn, strategyVO.getCurrent_user_favorite() == 0 ? R.drawable.icon_xh_d : R.drawable.icon_xh_e, R.color.text_black);

                    item_like_btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if (context instanceof StrategyInfoActivity) {
                                String action = "";
                                if (likeMap.get(vo.getPhoto().getId()) == null) {
                                    action = "on";
                                    vo.getPhoto().setLike_count(vo.getPhoto().getLike_count() + 1);
                                    likeMap.put(vo.getPhoto().getId(), vo.getPhoto().getId());
                                    ViewUtil.setViewLeftDrawableAndTextColor(context, item_like_btn, R.drawable.icon_xh_e, R.color.text_black);
                                } else {
                                    action = "off";
                                    vo.getPhoto().setLike_count(vo.getPhoto().getLike_count() - 1);
                                    likeMap.put(vo.getPhoto().getId(), null);
                                    ViewUtil.setViewLeftDrawableAndTextColor(context, item_like_btn, R.drawable.icon_xh_d, R.color.text_black);
                                }

                                ((StrategyInfoActivity) context).requestLikePhoto(vo.getPhoto().getId() + "", action);
                                item_like_btn.setText(vo.getPhoto().getLike_count() + "");
                            }
                        }
                    });
                }

                item_day_content_tv.setText("");
                item_day_content_tv.setVisibility(View.GONE);

                if (vo.getPhoto() != null) {
                    item_icon_iv.setVisibility(View.VISIBLE);
                    float scale = screenWidth / vo.getPhoto().getWidth();
                    float height = vo.getPhoto().getHeight() * scale;
                    imageParams.height = (int) height;
                    item_icon_iv.setLayoutParams(imageParams);
                    ImageLoader.getInstance().displayImage(vo.getPhoto().getUrl(), item_icon_iv, options, animateFirstListener);
                } else {
                    item_icon_iv.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(vo.getDescription())) {
                    item_image_content_tv.setVisibility(View.GONE);
                } else {
                    item_image_content_tv.setVisibility(View.VISIBLE);
                    item_image_content_tv.setText(vo.getDescription());
                }
            }
        }

        return convertView;
    }

}
  