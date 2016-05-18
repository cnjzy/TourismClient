package com.tourism.app.activity.user.adapter;

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
import com.tourism.app.vo.UserActionsVO;

/**
 * Created by Jzy on 16/5/10.
 * todo 用户活动列表
 */
public class UserActionsAdapter extends ETSBaseAdapter{

    public DisplayImageOptions bgOptions;

    public UserActionsAdapter(BaseActivity context, AbsListView listView) {
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

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_user_collection_list, parent, false);
        }

        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        TextView item_city_tv = ViewHolderUtil.get(convertView, R.id.item_city_tv);
        TextView item_date_tv = ViewHolderUtil.get(convertView, R.id.item_date_tv);

        UserActionsVO vo = (UserActionsVO) getItem(position);

        if (vo != null){
            ImageLoader.getInstance().displayImage(vo.getLitpic(), item_icon_iv, bgOptions, animateFirstListener);
            item_city_tv.setText(vo.getTitle());
            item_date_tv.setText(vo.getPubdate());
        }

        return convertView;
    }
}
