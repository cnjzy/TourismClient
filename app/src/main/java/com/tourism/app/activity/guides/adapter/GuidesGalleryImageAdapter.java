package com.tourism.app.activity.guides.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.util.image.ImageFolder;

/**
 * Created by Jzy on 16/5/9.
 */
public class GuidesGalleryImageAdapter extends ETSBaseAdapter {

    public GuidesGalleryImageAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public void loadData() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_guides_gallery_image, parent, false);
        }

        final ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        final ImageView item_mask_iv = ViewHolderUtil.get(convertView, R.id.item_mask_iv);
        final ImageView item_selected_iv = ViewHolderUtil.get(convertView, R.id.item_selected_iv);

        final ImageFolder vo = (ImageFolder) getItem(position);

        if(vo != null){
            LoadLocalImageUtil.getInstance().displayFromSDCard(vo.getPath(), item_icon_iv, options);
            if(vo.isSelected()){
                item_mask_iv.setVisibility(View.VISIBLE);
                item_selected_iv.setVisibility(View.VISIBLE);
            }else{
                item_mask_iv.setVisibility(View.INVISIBLE);
                item_selected_iv.setVisibility(View.INVISIBLE);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                vo.setIsSelected(!vo.isSelected());
                if(vo.isSelected()){
                    item_mask_iv.setVisibility(View.VISIBLE);
                    item_selected_iv.setVisibility(View.VISIBLE);
                }else{
                    item_mask_iv.setVisibility(View.INVISIBLE);
                    item_selected_iv.setVisibility(View.INVISIBLE);
                }
            }
        });

        return convertView;
    }
}
