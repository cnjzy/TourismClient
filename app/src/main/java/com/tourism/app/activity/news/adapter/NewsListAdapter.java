package com.tourism.app.activity.news.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.NewsVO;

public class NewsListAdapter extends ETSBaseAdapter{

	public NewsListAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);
	}

	@Override
	public void loadData() {
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_news_list, parent, false);
		}
		
		NewsVO vo = (NewsVO) getItem(position);
		
		if(vo != null){
			TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);
			item_name_tv.setText(vo.getTitle());
		}
		
		return convertView;
	}

}
