package com.tourism.app.activity.guides.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.GuidesCategoryVO;

/**
 * Created by Jzy on 16/5/16.
 */
public class GuidesCategoryAdapter extends ETSBaseAdapter {

    public GuidesCategoryAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public void loadData() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_guides_category, parent, false);
        }

        TextView item__name_tv = ViewHolderUtil.get(convertView, R.id.item__name_tv);

        GuidesCategoryVO vo = (GuidesCategoryVO) getItem(position);
        if (vo != null){
            item__name_tv.setText(vo.getName());
        }

        return convertView;
    }
}
