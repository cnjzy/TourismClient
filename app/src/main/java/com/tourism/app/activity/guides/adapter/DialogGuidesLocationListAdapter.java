package com.tourism.app.activity.guides.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.GuidesLocationVO;

/**
 * Created by Jzy on 16/6/5.
 */
public class DialogGuidesLocationListAdapter extends ETSBaseAdapter {

    public DialogGuidesLocationListAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public void loadData() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_guides_location_diaog_list, parent, false);
        }

        TextView item_date_tv = ViewHolderUtil.get(convertView, R.id.item_date_tv);
        TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);

        GuidesLocationVO vo = (GuidesLocationVO) getItem(position);

        if (vo != null){
            item_date_tv.setText(vo.getDateStr());
            item_name_tv.setText(vo.getLocation_name());
        }

        return convertView;
    }
}
