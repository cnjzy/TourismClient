package com.tourism.app.activity.poolfriend;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.R;
import com.tourism.app.activity.poolfriend.adapter.FriendBannerAdapter;
import com.tourism.app.activity.poolfriend.adapter.FriendListAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.BaseFragment;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.CategoryVO;
import com.tourism.app.vo.CategoryVO.Category;
import com.tourism.app.widget.view.ChildViewPager;
import com.tourism.app.widget.view.PointWidget;

public class PoolFriendFragment extends BaseFragment {

	private final int REQUEST_GET_BRAND_CODE = 10001;

	/**
	 * 布局管理器
	 */
	private LayoutInflater inflater;
	/**
	 * ListView
	 */
	private ListView list_view;
	private FriendListAdapter listAdapter;
	/**
	 * bannerView
	 */
	private View headerView;
	private ChildViewPager view_pager;
	private PointWidget view_pager_point;
	private FriendBannerAdapter brandBannerAdapter;
	private RelativeLayout.LayoutParams bannerParams;

	/**
	 * bannner数据列表
	 */
	private CategoryVO vo;

	@Override
	public void init() {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bannerParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, context.getResources().getDimensionPixelSize(R.dimen.brand_banner_height));
	}

	@Override
	public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_pool_friend, container, false);
	}

	@Override
	public void initView() {
		list_view = findViewById(R.id.list_view);
		headerView = inflater.inflate(R.layout.view_view_pager, null);
		view_pager = (ChildViewPager) headerView.findViewById(R.id.view_pager);
		view_pager_point = (PointWidget) headerView.findViewById(R.id.view_pager_point);
	}

	@Override
	public void initListener() {
	}

	@Override
	public void initValue() {
		requestBrand();
	}

	/**
	 * 获取品牌信息
	 */
	public void requestBrand() {
		List<RequestParameter> parameter = new ArrayList<RequestParameter>();
		startHttpRequest(Constants.HTTP_GET, Constants.URL_HOME, parameter, true, REQUEST_GET_BRAND_CODE);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		try {
			BaseResponseMessage brm = new BaseResponseMessage();
			switch (resultCode) {
			case REQUEST_GET_BRAND_CODE:
				brm.parseResponse(resultJson, new TypeToken<CategoryVO>() {
				});
				if (brm.isSuccess()) {
					vo = (CategoryVO) brm.getResult();

					brandBannerAdapter = new FriendBannerAdapter(context, vo.getBanner(), view_pager, view_pager_point);
					view_pager.setAdapter(brandBannerAdapter);
					view_pager.setCurrentItem(100 + (100 % vo.getBanner().size()), false);
					list_view.addHeaderView(headerView);
					view_pager.setLayoutParams(bannerParams);

					listAdapter = new FriendListAdapter((BaseActivity) context, list_view);
					listAdapter.addLast(vo.getCategory(), false);
					list_view.setAdapter(listAdapter);
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
