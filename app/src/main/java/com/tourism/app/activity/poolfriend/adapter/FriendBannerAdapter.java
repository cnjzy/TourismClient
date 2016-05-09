package com.tourism.app.activity.poolfriend.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tourism.app.MyApp;
import com.tourism.app.R;
import com.tourism.app.activity.brand.BrandInfoActivity;
import com.tourism.app.activity.poolfriend.CategoryInfoActivity;
import com.tourism.app.activity.web.WebViewActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.NewsVO;
import com.tourism.app.widget.view.ChildViewPager;
import com.tourism.app.widget.view.ChildViewPager.OnSingleTouchListener;
import com.tourism.app.widget.view.PointWidget;

/**
 * 首页轮播adpter
 * 
 * @author admin
 * 
 */
public class FriendBannerAdapter extends PagerAdapter {
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 显示View集合
	 */
	private List<View> viewList = new ArrayList<View>();
	/**
	 * 广告数据对象集合
	 */
	private List<NewsVO> dataList;
	/**
	 * 最大数量
	 */
	private int count = Integer.MAX_VALUE;
	/**
	 * 当前ViewPager
	 */
	private ChildViewPager viewPager;
	/**
	 * 轮播显示器
	 */
	private PointWidget pw;
	/**
	 * 图片缓存属性
	 */
	private DisplayImageOptions options;
	/**
	 * 轮播Handler
	 */
	private BannerLooper bannerLooper = new BannerLooper();
	private Handler handler = new Handler();

	private void initImageLoader(Context context) {
		options = new DisplayImageOptions.Builder()
				 .showImageOnLoading(R.drawable.img_default_bannber)
				 .showImageForEmptyUri(R.drawable.img_default_bannber)
				 .showImageOnFail(R.drawable.img_default_bannber)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.build();
	}

	public FriendBannerAdapter(Context mContext, List<NewsVO> mDataList, ChildViewPager mViewPager, PointWidget pw) {
		initImageLoader(mContext);
		this.context = mContext;
		this.viewPager = mViewPager;
		this.pw = pw;
		this.dataList = mDataList;
		this.pw.setPointCount(dataList.size());
		viewPager.setOnPageChangeListener(onPageChangeListener);
		if (dataList != null && dataList.size() > 0) {
			// 判断如果数据小于3条，则增加，用于轮训
			if (dataList.size() < 3 && dataList.size() == 2) {
				while (dataList.size() < 3) {
					dataList.addAll(dataList);
				}
			}

			// 添加子View
			viewList.clear();
			for (int i = 0; i < dataList.size(); i++) {
				viewList.add(((Activity) context).getLayoutInflater().inflate(
						R.layout.item_pager_image, null));
			}

			if (dataList.size() > 1) {
				this.pw.setVisibility(View.VISIBLE);
				// 设置轮播
				startLooper();
			} else {
				this.pw.setVisibility(View.INVISIBLE);
				stopLooper();
			}
		}

		// 单机
		viewPager.setOnSingleTouchListener(new OnSingleTouchListener() {
			public void onSingleTouch() {
				if (dataList != null && dataList.size() > 0 && dataList.size() > viewPager.getCurrentItem()%dataList.size()) {
					final NewsVO vo = dataList.get(viewPager.getCurrentItem() % dataList.size());
					if(vo.getType() == 1){
						WebViewActivity.show(context, vo.getLink(), vo.getTitle());
					}else if(vo.getType() == 2){
						// 活动
						// 攻略
						Bundle data = new Bundle();
						data.putSerializable("vo", vo);
						BaseActivity.showActivity(context, CategoryInfoActivity.class, data);
					}else{
						// 攻略
						Bundle data = new Bundle();
						data.putSerializable("vo", vo);
						BaseActivity.showActivity(context, CategoryInfoActivity.class, data);
					}
				}
			}
		});
	}

	public void resetList(List resetDataList) {
		stopLooper();
		dataList.clear();
		dataList.addAll(resetDataList);
		if (dataList != null && dataList.size() > 0) {
			// 判断如果数据小于3条，则增加，用于轮训
			if (dataList.size() < 3 && dataList.size() == 2) {
				dataList.addAll(dataList);
			}

			// 添加子View
			viewList.clear();
			for (int i = 0; i < dataList.size(); i++) {
				viewList.add(((Activity) context).getLayoutInflater().inflate(
						R.layout.item_pager_image, null));
			}
		}

		handler.postDelayed(new Runnable() {
			public void run() {
				if (dataList.size() > 3) {
					// 设置轮播
					startLooper();
				}
			}
		}, 5 * 1000);

		notifyDataSetChanged();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		if (dataList != null && dataList.size() > 1) {
			return count;
		}
		return viewList.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		try {
			if (dataList != null && dataList.size() > 0) {
				position = position % dataList.size();
			}

			if (viewList.get(position).getParent() != null
					&& ((View) viewList.get(position).getParent())
							.equals(container)) {
				((ViewPager) container).removeView(viewList.get(position));
			}

			((ViewPager) container).addView(viewList.get(position));
			if (dataList != null && dataList.size() > 0) {
				final NewsVO bean = dataList.get(position);
				ImageView imgView = ViewHolderUtil.get(viewList.get(position), R.id.item_image_iv);
				ImageLoader.getInstance().displayImage(bean.getLitpic(), imgView, options, ((MyApp) context.getApplicationContext()).getAnimateFirstListener());
			}
		} catch (Exception e) {
			LogUtil.e(e);
		}

		return viewList.get(position);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
	}

	@Override
	public void finishUpdate(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	/**
	 * 页面改变监听
	 */
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			if(pw.getPointCount() != 0){
				pw.setPoint(arg0 % pw.getPointCount());
			}
			if (dataList != null && dataList.size() != 0) {
				if (arg0 == dataList.size() - 1) {
					return;
				}
			}
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
			if (positionOffset != 0 && positionOffsetPixels != 0) {
				stopLooper();
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (arg0 == 0) {
				// 设置轮播
				startLooper();
			}
		}
	};

	public void release() {
		stopLooper();
		viewList.clear();
	}

	/**
	 * 广告循环线程 (Description)
	 * 
	 * @author admin
	 * @version
	 */
	private class BannerLooper implements Runnable {
		@Override
		public void run() {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
		}
	}

	/**
	 * 开始轮播
	 */
	private void startLooper() {
		handler.removeCallbacks(bannerLooper);
		handler.postDelayed(bannerLooper, 5 * 1000);
	}

	/**
	 * 停止轮播
	 */
	private void stopLooper() {
		handler.removeCallbacks(bannerLooper);
	}
}
