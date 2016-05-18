package com.tourism.app.activity.brand;

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
import com.tourism.app.activity.brand.adapter.BrandBannerAdapter;
import com.tourism.app.activity.brand.adapter.BrandListAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.BaseFragment;
import com.tourism.app.common.Constants;
import com.tourism.app.net.utils.RequestParameter;
import com.tourism.app.procotol.BaseResponseMessage;
import com.tourism.app.vo.NewsVO;
import com.tourism.app.widget.view.ChildViewPager;
import com.tourism.app.widget.view.PointWidget;

import java.util.ArrayList;
import java.util.List;

public class BrandFragment extends BaseFragment{
	private final int REQUEST_GET_BRAND_CODE = 10001;
	
	/**
	 * 布局管理器
	 */
	private LayoutInflater inflater;
	/**
	 * ListView
	 */
	private ListView list_view;
	private BrandListAdapter listAdapter;
	/**
	 * bannerView
	 */
	private View headerView;
	private ChildViewPager view_pager;
	private PointWidget view_pager_point;
	private BrandBannerAdapter brandBannerAdapter;
	private RelativeLayout.LayoutParams bannerParams;
	
	/**
	 * bannner数据列表
	 */
	private NewsVO vo;
	
	@Override
	public void init() {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		bannerParams =  new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, context.getResources().getDimensionPixelSize(R.dimen.brand_banner_height));
	}

	@Override
	public View initLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_brand, container, false);
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
		list_view.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				NewsVO vo = (NewsVO) list_view.getItemAtPosition(position);
				if(vo != null) {
					Bundle data = new Bundle();
					data.putSerializable("vo", vo);
					BaseActivity.showActivity(context, BrandInfoActivity.class, data);
				}
			}
		});
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
		startHttpRequest(Constants.HTTP_GET, Constants.URL_BRAND_LIST, parameter, false, REQUEST_GET_BRAND_CODE);
	}

	@Override
	public void onCallbackFromThread(String resultJson, int resultCode) {
		try{
			BaseResponseMessage brm = new BaseResponseMessage();
			switch(resultCode){
			case REQUEST_GET_BRAND_CODE:
				brm.parseResponse(resultJson, new TypeToken<NewsVO>(){});
				if(brm.isSuccess()){
					vo = (NewsVO) brm.getResult();
					
					brandBannerAdapter = new BrandBannerAdapter(context, vo.getBanner(), view_pager, view_pager_point);
					view_pager.setAdapter(brandBannerAdapter);
					if(vo.getBanner().size() != 0){
						view_pager.setCurrentItem(100 + (100%vo.getBanner().size()), false);
					}
					list_view.addHeaderView(headerView);
					view_pager.setLayoutParams(bannerParams);
					
					listAdapter = new BrandListAdapter((BaseActivity) context, list_view);
					listAdapter.addLast(vo.getList(), false);
					list_view.setAdapter(listAdapter);
				}
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
