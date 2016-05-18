package com.tourism.app.activity.guides.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.GuidesLocationVO;

/**
 * Created by Jzy on 16/5/16.
 */
public class GuidesLocationSearchAdapter extends ETSBaseAdapter {
    public GuidesLocationSearchAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public void loadData() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_guides_location, parent, false);
        }

        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);
        TextView item_des_tv = ViewHolderUtil.get(convertView, R.id.item_des_tv);

        GuidesLocationVO vo = (GuidesLocationVO) getItem(position);
        if (vo != null){
            item_name_tv.setText(vo.getLocation_name());
            item_des_tv.setText(vo.getLocation_name_en());
        }

        return convertView;
    }
}
