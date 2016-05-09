package com.tourism.app.activity.user.adapter;

import me.maxwin.view.ScaleImageView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.NewsVO;

public class UserFollowAdapter extends ETSBaseAdapter {

	public UserFollowAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void loadData() {
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_user_follow_temp, parent, false);
		}
		
		ScaleImageView item_image_iv = ViewHolderUtil.get(convertView, R.id.item_image_iv);
		ImageView item_vip_iv = ViewHolderUtil.get(convertView, R.id.item_vip_iv);
		TextView item_name_tv = ViewHolderUtil.get(convertView, R.id.item_name_tv);
		TextView item_count_tv = ViewHolderUtil.get(convertView, R.id.item_count_tv);
		
		NewsVO vo = (NewsVO) getItem(position);
		
		if(vo != null){
			item_name_tv.setText(vo.getTitle());
			item_count_tv.setText("111");
			ImageLoader.getInstance().displayImage(vo.getLitpic(), item_image_iv, options, animateFirstListener);
		}
		
		return convertView;
	}
}
