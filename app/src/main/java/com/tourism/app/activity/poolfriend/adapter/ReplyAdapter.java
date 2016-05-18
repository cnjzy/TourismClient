package com.tourism.app.activity.poolfriend.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.ReplyVO;
import com.tourism.app.widget.imageloader.CircleBitmapDisplayer;

/**
 * Created by Jzy on 16/5/11.
 */
public class ReplyAdapter extends ETSBaseAdapter {

    public DisplayImageOptions circleOptions;

    public ReplyAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);

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

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_reply, parent, false);
        }

        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);
        TextView item_content_tv = ViewHolderUtil.get(convertView, R.id.item_content_tv);
        TextView item_date_tv = ViewHolderUtil.get(convertView, R.id.item_date_tv);

        ReplyVO vo = (ReplyVO) getItem(position);
        if(vo != null){
            ImageLoader.getInstance().displayImage(vo.getUser_avatar(), item_icon_iv, circleOptions, animateFirstListener);
            item_name_tv.setText(vo.getUser_nickname());
            item_content_tv.setText(vo.getContent());
            item_date_tv.setText(vo.getPubdate());
        }

        return convertView;
    }
}
