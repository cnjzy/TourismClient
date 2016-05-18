package com.tourism.app.activity.user.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.UserFollowVO;

import me.maxwin.view.ScaleImageView;

/**
 * todo 用户喜欢适配器
 */
public class UserFollowAdapter extends ETSBaseAdapter {

	private int screenWidth;

	public UserFollowAdapter(BaseActivity context, AbsListView listView) {
		super(context, listView);
		screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
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

		UserFollowVO vo = (UserFollowVO) getItem(position);
		
		if(vo != null){
			int imageWidth = screenWidth / 2;
			float scale = (float)vo.getWidth() / imageWidth;
			int imageHeight = (int)(vo.getHeight() / scale);
			item_image_iv.setImageWidth(imageWidth);
			item_image_iv.setImageHeight(imageHeight);

			ImageLoader.getInstance().displayImage(vo.getLitpic(), item_image_iv, options, animateFirstListener);
		}
		
		return convertView;
	}
}
