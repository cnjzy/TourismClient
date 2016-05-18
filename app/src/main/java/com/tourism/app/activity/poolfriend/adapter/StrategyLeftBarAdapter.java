package com.tourism.app.activity.poolfriend.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.StrategyVO;

/**
 * Created by Jzy on 16/5/10.
 */
public class StrategyLeftBarAdapter extends ETSBaseAdapter {

    public StrategyLeftBarAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public void loadData() {

    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_event_strategy_left_bar_list, parent, false);
        }

        ImageView item_top_line_iv = ViewHolderUtil.get(convertView, R.id.item_top_line_iv);
        ImageView item_center_line_tv = ViewHolderUtil.get(convertView, R.id.item_center_line_tv);
        ImageView item_bottom_line_iv = ViewHolderUtil.get(convertView, R.id.item_bottom_line_iv);
        TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);

        if (position == getCount() -1){
            item_top_line_iv.setVisibility(View.INVISIBLE);
            item_bottom_line_iv.setVisibility(View.INVISIBLE);
            item_center_line_tv.setBackgroundResource(R.drawable.icon_circle_big);
            item_name_tv.setText("TheEnd");
        }else {

            StrategyVO vo = (StrategyVO) getItem(position);
            StrategyVO pvo = (StrategyVO) getItem(position - 1);
            StrategyVO nvo = (StrategyVO) getItem(position + 1);

            if (vo.isDate()) {
                item_top_line_iv.setVisibility(View.INVISIBLE);
                item_center_line_tv.setBackgroundResource(R.drawable.icon_circle_big);
                item_name_tv.setText("DAY" + vo.getDay());
            } else {
                item_top_line_iv.setVisibility(View.VISIBLE);
                item_center_line_tv.setBackgroundResource(R.drawable.icon_circle_little);
                item_name_tv.setText(vo.getLocation_name());
            }

            if (nvo != null && nvo.isDate()) {
                item_bottom_line_iv.setVisibility(View.INVISIBLE);
            } else {
                item_bottom_line_iv.setVisibility(View.VISIBLE);
            }

            if (position + 1 == getCount() - 1){
                item_bottom_line_iv.setVisibility(View.INVISIBLE);
            }
        }


        return convertView;
    }
}
