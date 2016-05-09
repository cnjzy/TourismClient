package com.tourism.app.activity;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.tourism.app.R;
import com.tourism.app.activity.brand.BrandFragment;
import com.tourism.app.activity.guides.GuidesImageActivity;
import com.tourism.app.activity.news.NewsFragment;
import com.tourism.app.activity.poolfriend.PoolFriendFragment;
import com.tourism.app.activity.user.UserHomeActivity;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.BaseFragment;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.DialogUtil.OnCallbackListener;
import com.tourism.app.util.PopupWindowsUtils;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.widget.view.NavigationView;
import com.tourism.app.widget.view.NavigationView.OnTitleChangeListener;

public class MainActivity extends BaseActivity {
	
	private ViewPager viewPager;
	private TabFragmentPagerAdapter tabAdapter;
	
	private NavigationView navigationView;
	public String[] tabTitle = { "拼友", "品牌", "服务"}; // 标题
	public NavigationView.OnTitleChangeListener onTitleChangeListener = new OnTitleChangeListener() {
		public void change(int i) {
			viewPager.setCurrentItem(i, true);
		}
	};
	
	private PoolFriendFragment poolFriendFragment;
	private BrandFragment brandFragment;
	private NewsFragment newsFragment;
	
	/**
	 * 更多按钮
	 */
	private View more_btn;

	private GuidesDao guidesDao;
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_main);
	}
	@Override
	public void init() {
		guidesDao = new GuidesDao(context);
	}
	@Override
	public void initView() {
		navigationView = (NavigationView) findViewById(R.id.page_title);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		more_btn = findViewById(R.id.more_btn);
	}
	@Override
	public void initListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				navigationView.setSelectPosition(position);
				BaseFragment bf = (BaseFragment) tabAdapter.getItem(position);
				tabAdapter.notifyDataSetChanged();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				navigationView.setLinePoint(arg0, arg2 / tabTitle.length);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	@Override
	public void initValue() {
		viewPager.setOffscreenPageLimit(3);
		tabAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabAdapter);

		navigationView.setTab(tabTitle, 0);
		navigationView.setOnTitleChangeListener(onTitleChangeListener);
	}
	
	public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment ft = null;
			switch (arg0) {
			case 0:
				if (poolFriendFragment == null) {
					poolFriendFragment = new PoolFriendFragment();
				}
				ft = poolFriendFragment;
				break;
			case 1:
				if (brandFragment == null) {
					brandFragment = new BrandFragment();
				}
				ft = brandFragment;
				break;
			case 2:
				if (newsFragment == null) {
					newsFragment = new NewsFragment();
				}
				ft = newsFragment;
				break;

			}
			return ft;
		}

		@Override
		public int getCount() {
			return tabTitle.length;
		}

	}
	static int i = 1;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.serach_btn:
//			showActivity(context, SearchActivity.class);

			GuidesVO vo = new GuidesVO();
			vo.setName("name" + i);
			vo.setServer_id("server_id_" + i);
			System.err.println("id=" + guidesDao.add(vo));

//			System.err.println("count=" + guidesDao.getByParams(null).size());
			break;
		case R.id.user_btn:
			showActivity(context, UserHomeActivity.class);
			break;
		case R.id.more_btn:
			PopupWindowsUtils.getHomeMenu(context, more_btn);
			break;
		}
	}
}
