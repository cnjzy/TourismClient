package com.tourism.app.activity.user.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tourism.app.R;
import com.tourism.app.activity.guides.GuidesSyncActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.DateUtils;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.GuidesVO;

/**
 * Created by Jzy on 16/5/10.
 * todo 用户攻略适配器
 */
public class UserStrategyAdapter extends ETSBaseAdapter {
    public DisplayImageOptions bgOptions;

    public UserStrategyAdapter(BaseActivity context, AbsListView listView) {
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

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_user_guides_list, parent, false);
        }

        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        TextView item_city_tv = ViewHolderUtil.get(convertView, R.id.item_city_tv);
        TextView item_date_tv = ViewHolderUtil.get(convertView, R.id.item_date_tv);
        View item_sync_ll = ViewHolderUtil.get(convertView, R.id.item_sync_ll);
        TextView item_sync_tv = ViewHolderUtil.get(convertView, R.id.item_sync_tv);
        TextView item_pv_tv = ViewHolderUtil.get(convertView, R.id.item_pv_tv);
        TextView item_follow_count_tv = ViewHolderUtil.get(convertView, R.id.item_follow_count_tv);

        final GuidesVO vo = (GuidesVO) getItem(position);
        if(vo != null){
            if (vo.getPhotoVO() != null && !TextUtils.isEmpty(vo.getPhotoVO().getPath())) {
                LoadLocalImageUtil.getInstance().displayFromSDCard(vo.getPhotoVO().getPath(), item_icon_iv, options);
            }
            item_city_tv.setText(vo.getName());
            if (!TextUtils.isEmpty(vo.getStart_date()))
                item_date_tv.setText(vo.getStart_date() + " / " + DateUtils.getIntervalDays(vo.getStart_date(), vo.getEnd_date(), DateUtils.parsePatterns[1]) + "天 / " + vo.getPhotos_count() + "张");

            if (vo.is_upload() == 0){
                item_sync_ll.setVisibility(View.VISIBLE);
                item_sync_tv.setText("未同步");
            }else if (vo.is_upload() == 1){
                item_sync_ll.setVisibility(View.VISIBLE);
                item_sync_tv.setText("未完成");
            }else{
                item_sync_ll.setVisibility(View.INVISIBLE);
            }

            item_sync_ll.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Bundle data = new Bundle();
                    data.putSerializable("vo", vo);
                    BaseActivity.showActivityForResult(context, GuidesSyncActivity.class, 101, data);
                }
            });

            item_pv_tv.setText("0");
            item_follow_count_tv.setText("0");
        }


        return convertView;
    }
}
