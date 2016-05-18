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
public class GuidesLocationSortAdapter extends ETSBaseAdapter {

    public GuidesLocationSortAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public void loadData() {

    }

    public void removeItem(int arg0) {//删除指定位置的item
        dataList.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insertItem(Object item, int arg0) {
        dataList.add(arg0, item);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_guides_location_sort, parent, false);
        }

        View item_title_ll = ViewHolderUtil.get(convertView, R.id.item_title_ll);
        TextView item_title_date_name_tv = ViewHolderUtil.get(convertView, R.id.item_title_date_name_tv);
        TextView item_title_date_tv = ViewHolderUtil.get(convertView, R.id.item_title_date_tv);

        View contenet_ll = ViewHolderUtil.get(convertView, R.id.contenet_ll);
        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);

        GuidesLocationVO vo = (GuidesLocationVO) getItem(position);

        if (vo != null){
            if (vo.isDate()){
                item_title_ll.setVisibility(View.VISIBLE);
                contenet_ll.setVisibility(View.GONE);
                item_title_date_tv.setText(vo.getDateStr());
            }else{
                item_title_ll.setVisibility(View.GONE);
                contenet_ll.setVisibility(View.VISIBLE);
                item_name_tv.setText(vo.getLocation_name());
            }
        }

        return convertView;
    }
}
