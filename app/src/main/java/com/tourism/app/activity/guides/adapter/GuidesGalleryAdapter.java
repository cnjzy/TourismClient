package com.tourism.app.activity.guides.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.util.image.ImageFolder;
import com.tourism.app.util.image.ImageUtils;

import java.util.List;

/**
 * Created by Jzy on 16/5/9.
 */
public class GuidesGalleryAdapter extends ETSBaseAdapter {

    public GuidesGalleryAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);

        List<ImageFolder> ifList = ImageUtils.getAllSDImageFolder(context);
        addLast(ifList);
    }

    @Override
    public void loadData() {

    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_guides_gallery, parent, false);
        }

        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);
        TextView item_count_tv = ViewHolderUtil.get(convertView, R.id.item_count_tv);

        ImageFolder vo = (ImageFolder) getItem(position);

        if(vo != null){
            LoadLocalImageUtil.getInstance().displayFromSDCard(vo.getPath(), item_icon_iv, null);
            item_name_tv.setText(vo.getName());
            item_count_tv.setText(vo.getCount() + "");
        }

        return convertView;
    }
}
